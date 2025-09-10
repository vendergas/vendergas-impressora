package vendergas.impressora.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_rotas_cobertura.*
import vendergas.impressora.R
import vendergas.impressora.adapter.ItinerarioAdapter
import vendergas.impressora.base.BaseActivity
import android.widget.ArrayAdapter
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.widget.AdapterView
import vendergas.impressora.adapter.InputProdutoAdapter
import vendergas.impressora.models.*
import vendergas.impressora.models.enums.ItinerarioStatus
import vendergas.impressora.models.enums.TipoEstoque
import vendergas.impressora.models.enums.TipoPagamento
import java.util.*
import kotlin.concurrent.schedule


class RotasCoberturaActivity : BaseActivity() {

    private var FORMA_PAGAMENTO_ITENS = arrayOf("Boleto", "Cartão de Débito", "Cartão de Crédito", "Dinheiro", "Vale-Gás", "Cheque", "PIX");

    private var cobertura: NotaCobertura? = null
    private var itinerarioSelecionado: NotaCobertura.Itinerario? = null
    private var itinerarioAdapter: ItinerarioAdapter? = null;

    private var clientesAdapter: ArrayAdapter<Any>? = null
    private var filtered_cliente_List: Array<String> = arrayOf();
    private var clientes_list: List<Cliente> = listOf()
    private var cliente_search_handler = Handler()

    private var produtosAdapter: ArrayAdapter<Any>? = null
    private var filtered_produtos_List: Array<String> = arrayOf();
    private var produtos_list: List<NotaCobertura.Itinerario.Produto> = listOf()
    private var produtos_search_handler = Handler()

    private var produtos_selecionados: ArrayList<NotaCobertura.Itinerario.Produto> = arrayListOf();
    private var inputProdutoAdapter: InputProdutoAdapter? = null;

    private var mostrarRotas: Boolean = false;
    private var mostrarTudo: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rotas_cobertura)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupView()
    }

    fun setupView() {
        showLoading(false, loading)

        try {
            mostrarTudo = intent?.extras?.getBoolean("mostrarTudo", false) ?: false
            mostrarRotas = intent?.extras?.getBoolean("mostrarRotas", false) ?: false

            val def_value = NotaCobertura()
            def_value.itinerario = listOf()
            def_value.produtos = listOf()

            val itent_value = intent?.extras?.getString("cobertura", null);

            cobertura = if (itent_value != null) Gson().fromJson(itent_value, NotaCobertura::class.java) else def_value
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (mostrarTudo) setActionBarTitle("MINHAS VENDAS")
        else setActionBarTitle(if (mostrarRotas) "ROTAS" else "LANÇAR VENDA")

        rota_title?.visibility = if (mostrarRotas) View.VISIBLE else View.GONE
        itinerario_list?.visibility = if (mostrarRotas) View.VISIBLE else View.GONE
        nova_venda?.visibility = if (mostrarRotas) View.GONE else View.VISIBLE

        if (nova_venda?.visibility == View.VISIBLE) nova_venda?.setOnClickListener { adicionarVenda() }

        if (cobertura == null) {
            showConfirmDialog(
                title = "Algo aconteceu",
                message = "Não foi possível carregar as rotas da cobertura selecionada, verifique e tente novamente",
                negativeButton = null,
                callbackCancel = null,
                callback = { finish() }
            )
        } else {
            itinerario_title?.visibility = if (mostrarTudo) View.GONE else View.VISIBLE
            itinerario_info?.visibility = if (mostrarTudo) View.GONE else View.VISIBLE
            rota_title?.visibility = if (mostrarTudo) View.GONE else View.VISIBLE
            if (!mostrarTudo) renderCobertura()
            if (mostrarRotas) setupItinerarioAdapter()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (nova_venda_popup?.visibility == View.VISIBLE) toggleNovaVenda(show = false)
                else finish()
                return true
            }
            R.id.action_add -> {
                adicionarVenda()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun renderCobertura() {
        chave?.text = "${cobertura?.serie}-${cobertura?.numero}";
        valor?.text = "R$ ${String.format("%.2f", cobertura?.valor ?: 0f)}";
        emissao?.text = cobertura?.emissao

        var estabelecimento_info = "";

        if (cobertura?.empresa != null) estabelecimento_info += cobertura?.empresa?.nomeFantasia;
        if (cobertura?.loja != null) estabelecimento_info += (if (estabelecimento_info != "") ", " else "") + cobertura?.loja?.nomeFantasia;

        cobertura?.empresas?.forEach {
            estabelecimento_info += (if (estabelecimento_info != "") ", " else "") + it.nomeFantasia;
        }

        cobertura?.lojas?.forEach {
            estabelecimento_info += (if (estabelecimento_info != "") ", " else "") + it.nomeFantasia;
        }

        estabelecimento?.text = estabelecimento_info

        /**
         * POP UP NOVA VENDA
         */
        nova_venda_voltar?.setOnClickListener {
            toggleNovaVenda(show = false)
        }

        iniciar_venda?.setOnClickListener { iniciarVenda() }

        setupClientesAdapter(arrayOf(), false)

        setupProdutosAdapter(arrayOf(), false)

        var showClienteDropdown = true;
        var showProdutoDropdown = true;

        autocomplete_destinatario.setOnFocusChangeListener { v, hasFocus -> if (hasFocus && showClienteDropdown) autocomplete_destinatario.showDropDown() }
        autocomplete_produto.setOnFocusChangeListener { v, hasFocus -> if (hasFocus && showProdutoDropdown) autocomplete_produto.showDropDown() }

        autocomplete_destinatario.setOnTouchListener { v, event ->
            if (showClienteDropdown) autocomplete_destinatario.showDropDown();
            false
        }

        autocomplete_produto.setOnTouchListener { v, event ->
            if (showProdutoDropdown) autocomplete_produto.showDropDown();
            false
        }

        autocomplete_destinatario.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                cliente_search_handler.removeCallbacksAndMessages(null)
                if (showClienteDropdown) {
                    cliente_search_handler.postDelayed({ buscarCliente(autocomplete_destinatario.text.toString(), showClienteDropdown) }, 1000)
                } else showClienteDropdown = true
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        autocomplete_produto.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                produtos_search_handler.removeCallbacksAndMessages(null)
                if (showProdutoDropdown) {
                    produtos_search_handler.postDelayed({ buscarProduto(autocomplete_produto.text.toString(), showProdutoDropdown) }, 1000)
                } else showProdutoDropdown = true;
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        autocomplete_destinatario?.setOnItemClickListener { parent, view, position, id ->
            showClienteDropdown = false
            clientes_list.forEach {
                if ("${it.getClienteNome()} - ${it.telefone}" == filtered_cliente_List.get(position)) {
                    selecionarDestinatario(it)
                    return@forEach
                }
            }
        }

        autocomplete_produto?.setOnItemClickListener { parent, view, position, id ->
            showProdutoDropdown = false
            val query = filtered_produtos_List.get(position)

            for (it in produtos_list) {
                val it_query = formatarNomeProduto(it)
                if (it_query.equals(query)) {
                    selecionarProduto(it)
                    break
                }
            }
        }

        /**
         * Adapter produtos
         */
        setupInputProdutoAdapter()
    }

    fun formatarNomeProduto(p: NotaCobertura.Itinerario.Produto): String =  "${if (p.codigo != null) "${p.codigo} | " else ""}${p.tipoEstoque?.getFormatedValue()?.toUpperCase()} - ${p.nome}"

    fun setupClientesAdapter(list: Array<String>, requestFocus: Boolean = true) {
        filtered_cliente_List = list
        clientesAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item , list)
        autocomplete_destinatario.setThreshold(1)
        autocomplete_destinatario.setAdapter(clientesAdapter)
        if (requestFocus) autocomplete_destinatario.requestFocus()
    }

    fun setupProdutosAdapter(list: Array<String>, requestFocus: Boolean = true) {
        filtered_produtos_List = list
        produtosAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item , list)
        autocomplete_produto.setThreshold(1)
        autocomplete_produto.setAdapter(produtosAdapter)
        if (requestFocus) autocomplete_produto.requestFocus()
    }

    fun setupItinerarioAdapter() {
        if (itinerarioAdapter == null) {
            itinerarioAdapter = ItinerarioAdapter(this, cobertura?.itinerario ?: listOf())
            itinerario_list.adapter = itinerarioAdapter
            itinerario_list.setOnItemClickListener { parent, view, position, id ->
                val itinerario = (cobertura?.itinerario ?: listOf()).get(position)
                if (itinerario.status == ItinerarioStatus.VENDA_NAO_INICIADA) {
                    toggleNovaVenda(itinerario, true)
                } else {
                    itinerarioSelecionado = itinerario
                    iniciarVenda()
                }

            }
        }
        runOnUiThread { itinerarioAdapter?.notifyDataSetChanged() }
    }

    fun toggleNovaVenda(itinerario: NotaCobertura.Itinerario? = null, show: Boolean = true) {
        nova_venda_popup?.visibility = if (show) View.VISIBLE else View.INVISIBLE;
        itinerario_list?.visibility = if (show && !mostrarRotas) View.INVISIBLE else View.VISIBLE;
        itinerario_info?.visibility = if (show) View.INVISIBLE else View.VISIBLE;

        itinerarioSelecionado = itinerario

        if (itinerario != null) {
            selecionarDestinatario(itinerario.cliente)

            val produtosCache = itinerario.produtos ?: listOf()

            produtosCache.forEach { selecionarProduto(it, verificarBloqueioQuantidade = false) }

            if (show) {
                forma_pagamento?.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, FORMA_PAGAMENTO_ITENS)
                forma_pagamento?.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (cobertura != null) {
                            val tipoNf = FORMA_PAGAMENTO_ITENS.get(position)
                            when(tipoNf) {
                                "Boleto" -> itinerarioSelecionado?.pagamento = TipoPagamento.BOLETO
                                "Cartão de Débito" -> itinerarioSelecionado?.pagamento = TipoPagamento.CARTAO_DEBITO
                                "Cartão de Crédito" -> itinerarioSelecionado?.pagamento = TipoPagamento.CARTAO_CREDITO
                                "Dinheiro" -> itinerarioSelecionado?.pagamento = TipoPagamento.DINHEIRO
                                "Vale-Gás" -> itinerarioSelecionado?.pagamento = TipoPagamento.VALE_GAS
                                "Cheque" -> itinerarioSelecionado?.pagamento = TipoPagamento.CHEQUE
                                "PIX" -> itinerarioSelecionado?.pagamento = TipoPagamento.PIX
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                })

                if (itinerarioSelecionado?.pagamento != null) {
                    var index = 0;
                    when(itinerarioSelecionado?.pagamento) {
                        TipoPagamento.BOLETO -> index = FORMA_PAGAMENTO_ITENS.indexOf("Boleto")
                        TipoPagamento.CARTAO_DEBITO -> index = FORMA_PAGAMENTO_ITENS.indexOf("Cartão de Débito")
                        TipoPagamento.CARTAO_CREDITO -> index = FORMA_PAGAMENTO_ITENS.indexOf("Cartão de Crédito")
                        TipoPagamento.DINHEIRO -> index = FORMA_PAGAMENTO_ITENS.indexOf("Dinheiro")
                        TipoPagamento.VALE_GAS -> index = FORMA_PAGAMENTO_ITENS.indexOf("Vale-Gás")
                        TipoPagamento.CHEQUE -> index = FORMA_PAGAMENTO_ITENS.indexOf("Cheque")
                        TipoPagamento.PIX -> index = FORMA_PAGAMENTO_ITENS.indexOf("PIX")
                    }
                    forma_pagamento?.setSelection(if (index >= 0) index else 0)
                }
            }
        }
    }

    fun verificarValorNegociado() {
        Log.d("VALOR-NEGOCIADO", "On valor negociado");
        val cliente = this.itinerarioSelecionado?.cliente;
        if (cliente != null && (cliente.valorNegociados ?: listOf()).isNotEmpty()) {
            val toAdd = mutableListOf<NotaCobertura.Itinerario.Produto>();
            (this.itinerarioSelecionado?.produtos ?: listOf()).forEach { prd ->
                var valorNegociado: Cliente.ValorNegociado? = null;

                cliente.valorNegociados?.forEach { vl ->
                    if (
                        vl.produto == prd._id &&
                        (
                                vl.estoqueNegociado == "estoqueNegociado:todos" ||
                                (vl.estoqueNegociado == "estoqueNegociado:vazio" && prd.tipoEstoque == TipoEstoque.ESTOQUE_VAZIO) ||
                                (vl.estoqueNegociado == "estoqueNegociado:cheio" && prd.tipoEstoque == TipoEstoque.ESTOQUE_CHEIO)
                        )
                    ) {
                        valorNegociado = vl;
                    }
                }

                if (valorNegociado != null) {
                    Log.d("VALOR-NEGOCIADO", "Valor negociado encontrado")
                    prd.valorUnitario = valorNegociado!!.valor;
                    removerProduto(prd._id!!, prd.tipoEstoque!!)
                    toAdd.add(prd);
                }
            }
            Timer().schedule(500) {
                toAdd.forEach { selecionarProduto(it, false) }
            }
        }
    }

    fun selecionarDestinatario(cliente: Cliente?) {
        if (cliente != null && itinerarioSelecionado != null) {
            itinerarioSelecionado?.cliente = cliente
            if (itinerarioSelecionado?.endereco == null) itinerarioSelecionado?.endereco = (cliente.enderecos ?: listOf()).find { it.isDefault!! } ?: if (cliente.enderecos != null && cliente.enderecos!!.size > 0) cliente.enderecos!!.get(0) else null
            nome_destinatario_selecionado?.text = cliente.getClienteNome();
            // autocomplete_destinatario?.setText(cliente.nome)
            verificarValorNegociado();
        }
    }

    fun selecionarProduto(
        produto: NotaCobertura.Itinerario.Produto?,
        chamarValorNegociado: Boolean = true,
        verificarBloqueioQuantidade: Boolean = true
    ) {
        runOnUiThread {
            if (produto != null && itinerarioSelecionado != null) {

                val result = mutableListOf<NotaCobertura.Itinerario.Produto>()

                itinerarioSelecionado?.produtos?.forEach { result.add(it) }

                var canAdd = true;

                if (verificarBloqueioQuantidade && cobertura?.bloquearQuantidade == true && (cobertura?.produtos?.size ?: 0) > 0) {
                    if (cobertura?.produtos?.find { it._id == produto._id && it.tipoEstoque?.value == produto?.tipoEstoque?.value } == null) {
                        canAdd = false;
                        showConfirmDialog(title = "Atenção", message = "Este produto não está disponível para seleção!")
                    } else {
                        var totalCobertura = 0;
                        cobertura?.produtos?.forEach {
                            if (it._id == produto._id && it.tipoEstoque?.value == produto.tipoEstoque?.value) {
                                totalCobertura += (it.quantidade ?: 0);
                            }
                        }

                        if (totalCobertura <= 0) {
                            canAdd = false;
                            showConfirmDialog(title = "Atenção", message = "Este produto não está disponível para seleção!")
                        } else {
                            var totalUsadoVendas = 0;
                            cobertura?.itinerario?.forEach { itinerario ->
                                itinerario.produtos?.forEach { prod ->
                                    if (prod._id == produto._id && prod.tipoEstoque?.value == produto.tipoEstoque?.value) {
                                        totalUsadoVendas += (prod.quantidade ?: 0)
                                    }
                                }
                            }

                            val totalDisponivel = totalCobertura - totalUsadoVendas

                            if (totalDisponivel <= 0 || totalDisponivel < (produto.quantidade ?: 0)) {
                                canAdd = false;
                                showConfirmDialog(title = "Atenção", message = "Este produto não está disponível para seleção!")
                            }
                        }
                    }
                }

                if (result.find { formatarNomeProduto(it).equals(formatarNomeProduto(produto)) } == null && canAdd) result.add(produto)

                itinerarioSelecionado?.produtos = result

                produtos_selecionados.clear()
                setupInputProdutoAdapter()

                produtos_selecionados.addAll(result)
                setupInputProdutoAdapter()

                // autocomplete_produto?.setText(produto.nome)

                if (chamarValorNegociado) verificarValorNegociado();
            }
        }
    }

    fun removerProduto(idProduto: String, tipoEstoque: TipoEstoque, callback: (() -> Unit)? = null) {
        runOnUiThread {
            val result = mutableListOf<NotaCobertura.Itinerario.Produto>()

            produtos_selecionados.forEach { if (it._id != idProduto || it.tipoEstoque != tipoEstoque) result.add(it) }
            itinerarioSelecionado?.produtos = result

            produtos_selecionados.clear()
            setupInputProdutoAdapter()

            produtos_selecionados.addAll(result)
            setupInputProdutoAdapter()
        }
    }

    fun buscarCliente(search: String?, showDropDown: Boolean = false) {
        if (search != null && search != "") {
            if (paramDefault.idEntregador != null) {
                autocomplete_destinatario?.isEnabled = false
                loading_cliente_list?.visibility = View.VISIBLE

                requestHandler.sendRequest<Page<Cliente>>(
                    requestHandler.ROTAS.getListaCliente(
                        paramDefault.idEntregador!!,
                        searchString = search,
                        multiEmpresa = if (cobertura?.empresas != null && cobertura?.empresas?.size!! > 0) cobertura?.empresas?.joinToString { "${it._id}" } else null,
                        multiLoja = if (cobertura?.lojas != null && cobertura?.lojas?.size!! > 0) cobertura?.lojas?.joinToString { "${it._id}" } else null,
                        perPage = 99999
                    ),
                    { res ->
                        autocomplete_destinatario?.isEnabled = true
                        loading_cliente_list?.visibility = View.GONE

                        clientes_list = (res.items ?: listOf()).filter { it.tipoCliente != "1" }

                        val resultMapped = ArrayList<String>()
                        clientes_list.forEach { resultMapped.add("${it.getClienteNome()} - ${it.telefone}") }

                        setupClientesAdapter(resultMapped.toTypedArray())
                        if (showDropDown) {
                            runOnUiThread {
                                autocomplete_destinatario.showDropDown()
                            }
                        }
                    },
                    {
                        autocomplete_destinatario?.isEnabled = true
                        loading_cliente_list?.visibility = View.GONE
                    }
                )
            } else showSimpleDialog("Falha ao enviar requisição", "ID do Entregador não foi localizado")
        }
    }

    fun buscarProduto(search: String?, showDropDown: Boolean = false) {

        if (search != null && search != "") {
            if (paramDefault.idEntregador != null) {
                autocomplete_produto?.isEnabled = false
                loading_produtos_list?.visibility = View.VISIBLE

                requestHandler.sendRequest<Page<NotaCobertura.Itinerario.Produto>>(
                    requestHandler.ROTAS.getListaProduto(
                        paramDefault.idEntregador!!,
                        searchString = search,
                        multiEmpresa = if (cobertura?.empresas != null && cobertura?.empresas?.size!! > 0) cobertura?.empresas?.joinToString { "${it._id}" } else null,
                        multiLoja = if (cobertura?.lojas != null && cobertura?.lojas?.size!! > 0) cobertura?.lojas?.joinToString { "${it._id}" } else null
                    ),
                    { res ->
                        autocomplete_produto?.isEnabled = true
                        loading_produtos_list?.visibility = View.GONE

                        runOnUiThread {
                            produtos_list = res.items ?: listOf()

                            val resultMapped = ArrayList<String>()
                            (res.items ?: listOf()).forEach { resultMapped.add(formatarNomeProduto(it)) }
                            setupProdutosAdapter(resultMapped.toTypedArray())

                            if (showDropDown) autocomplete_produto.showDropDown()
                        }
                    },
                    {
                        autocomplete_produto?.isEnabled = true
                        loading_produtos_list?.visibility = View.GONE
                    }
                )
            } else showSimpleDialog("Falha ao enviar requisição", "ID do Entregador não foi localizado")
        }
    }

    fun setupInputProdutoAdapter(callback: (() -> Unit)? = null) {
        runOnUiThread {
            // if (inputProdutoAdapter == null) {
                inputProdutoAdapter = InputProdutoAdapter(
                    this,
                    produtos_selecionados,
                    cobertura?.editarValor != false,
                    callbackRemoveProduto = {_id, tipoEstoque -> removerProduto(_id, tipoEstoque)  },
                    cobertura = cobertura
                )
                produto_list.adapter = inputProdutoAdapter
            // }
            inputProdutoAdapter?.notifyDataSetChanged()
            produto_list?.requestLayout()
            setListViewHeightBasedOnItems(produto_list);
            if (callback != null) callback();
        }
    }

    fun adicionarVenda() {
        val it = NotaCobertura.Itinerario()
        it.status = ItinerarioStatus.VENDA_NAO_INICIADA;
        toggleNovaVenda(it, true)
    }

    fun iniciarVenda() {

        if ((itinerarioSelecionado?.produtos ?: listOf()).size < 1) {
            showConfirmDialog(title = "Atenção", message = "Selecione ao menos um produto antes de continuar!")
            return;
        } else {
            for (prod in (itinerarioSelecionado?.produtos ?: listOf())) {

                /*
                var has = false;
                var quantidadeDisponivel = 0;

                cobertura?.produtos?.forEach { p ->
                    if (p._id == prod._id && p.tipoEstoque.toString() == prod.tipoEstoque.toString()) {
                        has = true;
                        quantidadeDisponivel += p.quantidade!!;
                    }
                }

                if (!has) {
                    showConfirmDialog(title = "Atenção", message = "O produto ${prod.nome} - ${prod.tipoEstoque?.getFormatedValue()} não consta na nota de cobertura, remova o ${prod.nome} - ${prod.tipoEstoque?.getFormatedValue()} para continuar!")
                    return;
                }
                */

                if ((prod.quantidade ?: 0) <= 0) {
                    showConfirmDialog(title = "Atenção", message = "O produto ${prod.nome} - ${prod.tipoEstoque?.getFormatedValue()} está com a quantidade 0!")
                    return;
                }

                if ((prod.valorUnitario ?: 0f) < 0f) {
                    showConfirmDialog(title = "Atenção", message = "O produto ${prod.nome} - ${prod.tipoEstoque?.getFormatedValue()} está com um valor negativo!")
                    return;
                }

                /*
                if (quantidadeDisponivel < 1) {
                    showConfirmDialog(title = "Atenção", message = "O produto ${prod.nome} - ${prod.tipoEstoque?.getFormatedValue()} não contém mais quantidade disponível na nota de cobetura, remova o ${prod.nome} - ${prod.tipoEstoque?.getFormatedValue()} para continuar!")
                    return;
                } else if ((prod.quantidade ?: 0) > quantidadeDisponivel) {
                    showConfirmDialog(title = "Atenção", message = "A quantidade selecionada para o ${prod.nome} - ${prod.tipoEstoque?.getFormatedValue()} excedeu o limite da nota de cobertura(${quantidadeDisponivel}), altere o valor da quantidade para continuar!")
                    return;
                }
                */
            }
        }

        itinerarioSelecionado?.valor = 0f;
        itinerarioSelecionado?.produtos?.forEach {
            if (it.quantidade!! > 0) {
                itinerarioSelecionado?.valor = ((it.valorUnitario ?: 0f).toFloat() * it.quantidade!!.toFloat()) + (itinerarioSelecionado?.valor ?: 0f);
            }
        }

        if (!mostrarTudo) {
            itinerarioSelecionado?.empresa = cobertura?.empresa;
            itinerarioSelecionado?.loja = cobertura?.loja;
            itinerarioSelecionado?.empresas = cobertura?.empresas;
            itinerarioSelecionado?.lojas = cobertura?.lojas;
            itinerarioSelecionado?.idCobertura = cobertura?._id
        }

        val mBundle = Bundle()
        mBundle.putString("venda", Gson().toJson(itinerarioSelecionado))
        mBundle.putString("cobertura", Gson().toJson(cobertura))
        changeActivity(VendaActivity::class.java, mBundle)
    }

    override fun onBackPressed() {
        if (nova_venda_popup?.visibility == View.VISIBLE) toggleNovaVenda(show = false)
        else super.onBackPressed()
    }

}

package vendergas.impressora.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import android.database.DataSetObserver
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.input_produto_item.view.*
import vendergas.impressora.R
import vendergas.impressora.base.BaseActivity
import vendergas.impressora.models.NotaCobertura
import vendergas.impressora.models.enums.TipoEstoque

class InputProdutoAdapter(
    val context: Context,
    val listProdutos: List<NotaCobertura.Itinerario.Produto> = listOf(),
    val editarValor: Boolean = true,
    val callbackRemoveProduto: ((String, TipoEstoque) -> Unit)? = null,
    val cobertura: NotaCobertura?
): BaseAdapter() {

    override fun registerDataSetObserver(observer: DataSetObserver) {}
    override fun unregisterDataSetObserver(observer: DataSetObserver) {}

    override fun areAllItemsEnabled(): Boolean = false
    override fun isEnabled(position: Int): Boolean = true
    override fun getCount(): Int = listProdutos.size
    override fun getViewTypeCount(): Int = if (count > 0) count else super.getViewTypeCount()
    override fun isEmpty(): Boolean = false
    override fun getItem(position: Int): Any = position
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = false
    override fun getItemViewType(position: Int): Int = position

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        // if (view == null) {
            val produto = listProdutos.get(position)
            val newView = LayoutInflater.from(context).inflate(R.layout.input_produto_item, null);

            newView.nome_produto.text = "${if (produto.codigo != null) "${produto.codigo} | " else ""}${produto.tipoEstoque?.getFormatedValue()?.toUpperCase()} - ${produto.nome}"

            newView.quantidade_input.setText("${produto.quantidade}")
            newView.valor_input.setText("${produto.valorUnitario}")

            newView.quantidade_input.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    var qtd = 0;

                    try {
                        if (newView?.quantidade_input?.text?.toString() != "") qtd = newView.quantidade_input.text.toString().toInt()
                    } catch (e: Exception) {
                        e.printStackTrace();
                    }

                    var checkQuantidade = false;
                    var quantidadeDisponivel = 0;

                    if (cobertura?.bloquearQuantidade == true && (cobertura?.produtos?.size ?: 0) > 0) {
                        if (cobertura?.produtos?.find { it._id == produto._id && it.tipoEstoque?.value == produto.tipoEstoque?.value} == null) {
                            checkQuantidade = true;
                            (context as BaseActivity).showConfirmDialog(title = "Atenção", message = "Quantidade não disponível!")
                        } else {
                            var totalCobertura = 0;
                            cobertura?.produtos?.forEach {
                                if (it._id == produto._id && it.tipoEstoque?.value == produto.tipoEstoque?.value) {
                                    totalCobertura += (it.quantidade ?: 0);
                                }
                            }

                            if (totalCobertura <= 0) {
                                checkQuantidade = true;
                                (context as BaseActivity).showConfirmDialog(title = "Atenção", message = "Quantidade não disponível!")
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

                                checkQuantidade = true;
                                quantidadeDisponivel = totalDisponivel;

                                if (totalDisponivel <= 0 || totalDisponivel < (qtd ?: 0)) {
                                    (context as BaseActivity).showConfirmDialog(title = "Atenção", message = "Quantidade não disponível!")
                                }
                            }
                        }
                    }

                    if (quantidadeDisponivel < 0) quantidadeDisponivel = 0;

                    if (checkQuantidade) {
                        produto.quantidade = if (qtd > quantidadeDisponivel) quantidadeDisponivel else qtd;
                    } else produto.quantidade = qtd;

                    if (produto.quantidade != qtd) newView.quantidade_input.setText("${produto.quantidade}")
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable) {}
            })

            newView.valor_input.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                    var valor = 0f;

                    try {
                        if (newView?.valor_input?.text?.toString() != "") {
                            valor = newView.valor_input.text.toString().toFloat()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace();
                    }

                    produto.valorUnitario = valor;
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable) {}
            })

            newView.button_remove_produto.setOnClickListener {
                if (callbackRemoveProduto != null) callbackRemoveProduto.invoke(produto._id!!, produto.tipoEstoque!!)
            }

            newView.valor_input?.isEnabled = editarValor;

            return newView;
        // } else return view
    }

}
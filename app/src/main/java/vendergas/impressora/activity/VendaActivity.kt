package vendergas.impressora.activity

import android.app.DatePickerDialog
import android.app.LauncherActivity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import inputservice.printerLib.BoletoUtils
import kotlinx.android.synthetic.main.activity_venda.*
import vendergas.impressora.App
import vendergas.impressora.R
import vendergas.impressora.base.BaseActivity
import vendergas.impressora.models.BoletoResponse
import vendergas.impressora.models.Messages
import vendergas.impressora.models.NotaCobertura
import vendergas.impressora.models.Venda
import vendergas.impressora.models.enums.ItinerarioStatus
import vendergas.impressora.models.enums.TipoPagamento
import vendergas.impressora.print.NotaFiscal
import java.text.SimpleDateFormat
import java.util.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL


class VendaActivity : BaseActivity() {

    private var venda: NotaCobertura.Itinerario? = null
    private var cobertura: NotaCobertura? = null

    private val vendaRequest: Venda = Venda()

    private var TIPO_NF_ITENS = arrayOf("55 - NF-e", "65 - NFc-e");

    private var boletoVencimento: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venda)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupView()
    }

    fun setupView() {
        setActionBarTitle("VENDA")

        try {
            venda = Gson().fromJson(intent?.extras?.getString("venda"), NotaCobertura.Itinerario::class.java)
            cobertura = Gson().fromJson(intent?.extras?.getString("cobertura"), NotaCobertura::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.d("DEBUG-TESTE", intent?.extras?.getString("venda"));

        if (venda == null) {
            showConfirmDialog(
                title = "Algo aconteceu",
                message = "Não foi possível carregar a venda selecionada, verifique e tente novamente",
                negativeButton = null,
                callbackCancel = null,
                callback = { finish() }
            )
        } else {
            renderizarVenda()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun populateInfoNF(showEdit: Boolean) {
        nf_section_input?.visibility =  if (showEdit || venda?.status == ItinerarioStatus.ERRO_AO_EMITIR_NF) View.VISIBLE else View.GONE;
        nf_section_info?.visibility =  if (showEdit) View.GONE else View.VISIBLE;

        if (showEdit) {
            tipoNf?.text = "--"
        } else {
            when(venda?.tipoNota) {
                "55 - NF-e" -> tipoNf?.text = "nfe"
                "65 - NFc-e" -> tipoNf?.text = "nfce"
                else -> tipoNf?.text = (venda?.tipoNota ?: "N/A").toString()
            }
        }

        opFis?.text = if (showEdit) "--" else (venda?.natOperacao ?: "N/A").toString()

        if (showEdit) {
            dataEmissao?.text = "--"
        } else {
            var emissao_nota_data: String? = null;
            var emissao_nota_hora: String? = null;

            if (venda?.emissaoNota != null) {
                try {
					val baseDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(venda?.emissaoNota)
                    emissao_nota_data = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(baseDate)
                    emissao_nota_hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(baseDate)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            dataEmissao?.text = "${emissao_nota_data} ${emissao_nota_hora}"
        }

        valorNota?.text = if (showEdit) "--" else "R$ ${String.format("%.2f", venda?.valor ?: 0f)}"
        emitir_nota?.text = if (showEdit) "EMITIR NOTA" else "IMPRIMIR NOTA"
    }

    fun renderizarVenda() {
        valor?.text = "R$ ${String.format("%.2f", venda?.valor ?: 0f)}"
        pagamento?.text = venda?.pagamento?.getFormatedValue()
        cliente_nome.text = venda?.cliente?.getClienteNome() ?: "--"
        cliente_endereco.text = formatEndereco(venda?.endereco)
        status.text = venda?.status?.getFormatedValue()

        if ((venda?.cliente?.cpf?.length ?: 0) > 0) {
            cpf_input.setText(venda?.cliente?.cpf);
            cpf_input?.isEnabled = true;
            cnpj_input.setText("");
            cnpj_input?.isEnabled = false;
        } else if ((venda?.cliente?.cnpj?.length ?: 0) > 0) {
            cpf_input.setText("");
            cpf_input?.isEnabled = false;
            cnpj_input.setText(venda?.cliente?.cnpj);
            cnpj_input?.isEnabled = true;
        } else {
            cpf_input.setText("");
            cpf_input?.isEnabled = true;
            cnpj_input.setText("");
            cnpj_input?.isEnabled = true;
        }

        cpf_input?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                runOnUiThread {
                    cpf_input?.isEnabled = true;
                    cnpj_input?.isEnabled = if (cpf_input?.text?.length!! > 0) false else true;
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        });

        cnpj_input?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                runOnUiThread {
                    cpf_input?.isEnabled = if (cnpj_input?.text?.length!! > 0) false else true;
                    cnpj_input?.isEnabled = true;
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        });

        if (venda?.cliente?.prazoMaximo != null && (venda?.cliente?.prazoMaximo ?: 0) > 0) {
            val msTime = Date().time + (86400000L * (venda?.cliente?.prazoMaximo ?: 0));
            val defaultDate = SimpleDateFormat("dd/MM/yyyy").format(msTime);
            vencimento_input?.setText(defaultDate)
            boletoVencimento = SimpleDateFormat("MM/dd/yyyy").format(msTime)
        }

        vencimento_input?.setOnClickListener {
            val myCalendar: Calendar = Calendar.getInstance();
            val dialog =  DatePickerDialog(
                this@VendaActivity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    vencimento_input?.setText(SimpleDateFormat("dd/MM/yyyy").format(myCalendar.time))
                    boletoVencimento = SimpleDateFormat("MM/dd/yyyy").format(myCalendar.time)
                },
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            );
            if (venda?.cliente?.prazoMaximo != null && (venda?.cliente?.prazoMaximo ?: 0) > 0) dialog.datePicker.maxDate = Date().time + (86400000L * (venda?.cliente?.prazoMaximo ?: 0));
            dialog.show();
        };

        var init_input_callbacks = false;

        when(venda?.status) {
            ItinerarioStatus.ERRO_AO_EMITIR_NF -> {
                populateInfoNF(false)
                status?.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            }
            ItinerarioStatus.VENDA_AUTORIZADA -> {
                populateInfoNF(false)
                status?.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            }
            ItinerarioStatus.VENDA_CANCELADA -> {
                populateInfoNF(false)
                status?.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
            }
            ItinerarioStatus.VENDA_EMITIDA -> {
                populateInfoNF(false)
                status?.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light))
            }
            ItinerarioStatus.VENDA_NAO_INICIADA -> {
                init_input_callbacks = true
                populateInfoNF(true)
                status?.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            }
            else -> {
                init_input_callbacks = true
                populateInfoNF(true)
                status?.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            }
        }

        if (venda?.status?.equals(ItinerarioStatus.ERRO_AO_EMITIR_NF)!! && venda?.descErroNF != null) {
            motivo_erro_nf?.visibility = View.VISIBLE
            erro_nf?.setText(venda?.descErroNF)
            emitir_nota?.text = "EMITIR NOTA"
        } else {
            motivo_erro_nf?.visibility = View.GONE
            erro_nf?.setText("--")
        }

        var produtos = ""
        var count = 0

        (venda?.produtos ?: listOf()).forEach { p ->
            produtos += "${if (count > 0) "\n" else ""}${p.quantidade}x ${p.nome} - ${p.tipoEstoque?.getFormatedValue()} | UN R$ ${String.format("%.2f", p.valorUnitario ?: 0f)}"
            ++count
        }

        produtos_info?.text = produtos

        var estabelecimento_info = "";

        if (venda?.empresa != null) estabelecimento_info += venda?.empresa?.nomeFantasia;
        if (venda?.loja != null) estabelecimento_info += (if (estabelecimento_info != "") ", " else "") + venda?.loja?.nomeFantasia;
        venda?.empresas?.forEach { estabelecimento_info += (if (estabelecimento_info != "") ", " else "") + it.nomeFantasia; }
        venda?.lojas?.forEach { estabelecimento_info += (if (estabelecimento_info != "") ", " else "") + it.nomeFantasia; }

        estabelecimento?.text = estabelecimento_info

        title_boleto?.visibility = if (venda?.pagamento == TipoPagamento.BOLETO) View.VISIBLE else View.GONE;
        boleto_inputs?.visibility = if (venda?.pagamento == TipoPagamento.BOLETO) View.VISIBLE else View.GONE;

        val podeBoleto = venda?.pagamento == TipoPagamento.BOLETO
        emitir_boleto?.visibility = if (podeBoleto) View.VISIBLE else View.GONE
        if (podeBoleto) emitir_boleto?.setOnClickListener { verificarBoleto() }

        baixar_boleto?.visibility = if (podeBoleto) View.VISIBLE else View.GONE
        if (podeBoleto) baixar_boleto?.setOnClickListener { baixarBoletoExistenteOuEmitir() }

        emitir_nota?.visibility = View.VISIBLE;
        emitir_nota?.setOnClickListener { emitirNota() }

        if (init_input_callbacks || venda?.status == ItinerarioStatus.ERRO_AO_EMITIR_NF) {
            tipo_documento?.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, TIPO_NF_ITENS)
            tipo_documento?.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val tipoNf = TIPO_NF_ITENS.get(position)
                    when(tipoNf) {
                        "55 - NF-e" -> vendaRequest.tipoNota = "nfe"
                        "65 - NFc-e" -> vendaRequest.tipoNota = "nfce"
                        else -> vendaRequest.tipoNota = tipoNf
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })
            tipo_documento?.setSelection(0)
        }
    }

    fun emitirNota(ignorePrint: Boolean = false) {
        val progress = ProgressDialog(this@VendaActivity)

        progress.setMessage("Aguarde...")
        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog
        progress.show()

        Thread {
            val ignorar = App.IGNORAR_CONEXAO_IMPRESSORA
            val printer = (application as App).getPrinter()
            var connected = false;
            try { connected = if (ignorar) true else printer.connect(true) } catch (e: Exception) { e.printStackTrace() }

            if (connected || ignorePrint || ignorar) {
                if (venda?.cliente != null) {
                    if (cpf_input.isEnabled) venda!!.cliente!!.cpf = cpf_input.text.toString();
                    if (cnpj_input.isEnabled) venda!!.cliente!!.cnpj = cnpj_input.text.toString();
                }

                vendaRequest.observacao = observacoes_input.text.toString()
                vendaRequest.itinerario = venda;

                if ((venda?.empresas  ?: listOf()).size > 0) vendaRequest.empresa = venda?.empresas?.get(0)?._id
                else if (venda?.empresa != null) vendaRequest.empresa = venda?.empresa?._id

                if ((venda?.lojas ?: listOf()).size > 0) vendaRequest.loja = venda?.lojas?.get(0)?._id
                else if (venda?.loja != null) vendaRequest.loja = venda?.loja?._id

                requestHandler.sendRequest<NotaCobertura.Itinerario>(
                    requestHandler.ROTAS.iniciarVenda(paramDefault.idEntregador!!, vendaRequest),
                    { res ->
                        venda = res
                        // renderizarVenda()

                        var notaSucesso = false;

                        if (venda?.status == ItinerarioStatus.VENDA_EMITIDA || venda?.status == ItinerarioStatus.VENDA_AUTORIZADA) {
                            notaSucesso = true;
                            if (!ignorar) NotaFiscal.genNotaByClassA7(printer, venda!!, cobertura);
                        }

                        progress.dismiss()
                        (application as App).disconnectPrinter()

                        if (venda?.pagamento == TipoPagamento.BOLETO && notaSucesso) {
                            showConfirmDialog(
                                title = "Sucesso!",
                                message = "Nota gerado com sucesso. Deseja emitir o boleto?",
                                negativeButton = "Não",
                                cancelable = false,
                                callbackCancel = {
                                    changeActivityAndRemoveParentActivity(MainActivity::class.java)
                                    Handler().postDelayed({
                                        val mBundle = Bundle()
                                        mBundle.putString("venda", Gson().toJson(venda))
                                        mBundle.putString("cobertura", Gson().toJson(cobertura))
                                        changeActivity(VendaActivity::class.java, mBundle)
                                    }, 50)
                                },
                                confirmButton = "Emitir boleto",
                                callback = { if (ignorar) emitirBoletoParaDownload() else verificarBoleto() },
                                neutralButton = null,
                                callbackNeutral = null
                            )
                        } else {
                            changeActivityAndRemoveParentActivity(MainActivity::class.java)
                            Handler().postDelayed({
                                val mBundle = Bundle()
                                mBundle.putString("venda", Gson().toJson(venda))
                                mBundle.putString("cobertura", Gson().toJson(cobertura))
                                changeActivity(VendaActivity::class.java, mBundle)
                            }, 50)
                        }
                    },
                    {
                        (application as App).disconnectPrinter()
                        runOnUiThread { progress.dismiss() }
                        showError(it.message ?: Messages.defaultError, it.title ?: "Tente novamente")
                    }
                )
            } else {
                runOnUiThread {
                    showConfirmDialog(
                        title = "Atenção!",
                        message = "Não foi possível conectar a impressora. O que deseja fazer?",
                        negativeButton = "Cancelar",
                        callbackCancel = null,
                        confirmButton = "Tentar novamente",
                        callback = { emitirNota() },
                        neutralButton = "Baixar/Compartilhar",
                        callbackNeutral = { 
                            // Primeiro emite a nota sem imprimir, depois baixa
                            emitirNotaParaDownload()
                        }
                    )
                    (application as App).disconnectPrinter()
                    progress.dismiss()
                }
            }
        }.start()
    }

    fun verificarBoleto() {
        val progress = ProgressDialog(this@VendaActivity)

        progress.setMessage("Aguarde...")
        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog
        progress.show()

        Thread {
            val ignorar = App.IGNORAR_CONEXAO_IMPRESSORA
            val printer = (application as App).getBoletoPrinter()
            var connected = false;
            try { connected = if (ignorar) true else printer.connect(true) } catch (e: Exception) { e.printStackTrace() }

            if (connected) {
                emitirBoleto(venda!!) {
                    runOnUiThread { progress.dismiss() }
                    if (it) {
                        (application as App).disconnectBoletoPrinter()
                        changeActivityAndRemoveParentActivity(MainActivity::class.java)
                        Handler().postDelayed({
                            val mBundle = Bundle()
                            mBundle.putString("venda", Gson().toJson(venda))
                            mBundle.putString("cobertura", Gson().toJson(cobertura))
                            changeActivity(VendaActivity::class.java, mBundle)
                        }, 50)
                    }
                }
            } else {
                runOnUiThread {
                    showConfirmDialog(
                        title = "Atenção!",
                        message = "Não foi possível conectar a impressora. O que deseja fazer?",
                        negativeButton = "Cancelar",
                        callbackCancel = null,
                        confirmButton = "Tentar novamente",
                        callback = { verificarBoleto() },
                        neutralButton = "Emitir/baixar",
                        callbackNeutral = { emitirBoletoParaDownload() }
                    )
                    (application as App).disconnectBoletoPrinter()
                    progress.dismiss()
                }
            }
        }.start()
    }

    fun emitirBoleto(_v: NotaCobertura.Itinerario, callbackFinish: ((success: Boolean) -> (Unit))? = null) {
        var cpf_cnpj: String? = null;

        if (cpf_input.isEnabled) cpf_cnpj = cpf_input.text.toString();
        else if (cnpj_input.isEnabled) cpf_cnpj = cnpj_input.text.toString();

        val numeroBoleto = when {
            !_v.numeroNota.isNullOrBlank() && !_v.serieNota.isNullOrBlank() -> "${_v.numeroNota}-${_v.serieNota}"
            else -> null // Se não houver NF, deixa null para evitar erro de limite de caracteres
        }

        requestHandler.sendRequest<BoletoResponse>(
            requestHandler.ROTAS.iniciarBoleto(
                paramDefault.idEntregador!!,
                _v.pedido,
                _v.cliente?._id,
                cpf_cnpj,
                _v.cliente?.getClienteNome(),
                boletoVencimento,
                _v.idCobertura,
                numeroBoleto
            ),
            { res ->
                if (res.jaEmitido == true) {
                    showConfirmDialog(
                        title = "Atenção!",
                        message = "Essa venda já contém um BOLETO emitido, deseja imprimi-lo?",
                        negativeButton = "Cancelar",
                        callbackCancel = {
                            if (callbackFinish != null) callbackFinish(false);
                        },
                        confirmButton = "Imprimir",
                        callback = {
                            imprimirBoleto(res)
                            if (callbackFinish != null) callbackFinish(true);
                        },
                        neutralButton = null,
                        callbackNeutral = null
                    )
                } else {
                    imprimirBoleto(res)
                    if (callbackFinish != null) callbackFinish(true);
                }
            },
            {
                if (callbackFinish != null) callbackFinish(false);
                showError(it.message ?: "", it.title)
            }
        )
    }

    fun emitirBoletoParaDownload() {
        val progress = ProgressDialog(this@VendaActivity)
        progress.setMessage("Emitindo boleto...")
        progress.setCancelable(false)
        progress.show()

        var cpf_cnpj: String? = null
        if (cpf_input.isEnabled) cpf_cnpj = cpf_input.text.toString()
        else if (cnpj_input.isEnabled) cpf_cnpj = cnpj_input.text.toString()

        val v = venda ?: run {
            progress.dismiss()
            showError("Venda não carregada")
            return
        }

        val numeroBoleto = when {
            !v.numeroNota.isNullOrBlank() && !v.serieNota.isNullOrBlank() -> "${v.numeroNota}-${v.serieNota}"
            else -> null // Se não houver NF, deixa null para evitar erro de limite de caracteres
        }

        requestHandler.sendRequest<BoletoResponse>(
            requestHandler.ROTAS.iniciarBoleto(
                paramDefault.idEntregador!!,
                v.pedido,
                v.cliente?._id,
                cpf_cnpj,
                v.cliente?.getClienteNome(),
                boletoVencimento,
                v.idCobertura,
                numeroBoleto
            ),
            { res ->
                progress.dismiss()
                val link = res.linkBoleto
                if (!link.isNullOrBlank()) {
                    baixarArquivoBoleto(link, res.nomeBoleto ?: "boleto_${System.currentTimeMillis()}.pdf")
                } else {
                    showError("Link do boleto não disponível")
                }
            },
            {
                progress.dismiss()
                showError(it.message ?: Messages.defaultError, it.title ?: "Erro ao emitir boleto")
            }
        )
    }

    fun baixarBoletoExistenteOuEmitir() {
        val progress = ProgressDialog(this@VendaActivity)
        progress.setMessage("Verificando boleto...")
        progress.setCancelable(false)
        progress.show()

        val v = venda ?: run {
            progress.dismiss()
            showError("Venda não carregada")
            return
        }

        requestHandler.sendRequest<BoletoResponse>(
            requestHandler.ROTAS.consultarBoleto(
                paramDefault.idEntregador!!,
                v.pedido
            ),
            { res ->
                progress.dismiss()
                Log.d("DEBUG_BOLETO", "Resposta da API: jaEmitido=${res.jaEmitido}, linkBoleto=${res.linkBoleto}")
                
                if (res.jaEmitido == true && !res.linkBoleto.isNullOrBlank()) {
                    Log.d("DEBUG_BOLETO", "Boleto já emitido com link disponível")
                    showConfirmDialog(
                        title = "Boleto já emitido",
                        message = "Este boleto já foi emitido. O que deseja fazer?",
                        negativeButton = "Cancelar",
                        callbackCancel = null,
                        confirmButton = "Baixar PDF",
                        callback = { 
                            Log.d("DEBUG_BOLETO", "Usuário escolheu baixar PDF")
                            baixarArquivoBoleto(res.linkBoleto!!, res.nomeBoleto ?: "boleto_${System.currentTimeMillis()}.pdf") 
                        },
                        neutralButton = "Imprimir",
                        callbackNeutral = { 
                            Log.d("DEBUG_BOLETO", "Usuário escolheu imprimir")
                            if (App.IGNORAR_CONEXAO_IMPRESSORA) {
                                showError("Modo de teste ativo - impressão desabilitada")
                            } else {
                                imprimirBoleto(res)
                            }
                        }
                    )
                } else if (res.jaEmitido == true) {
                    Log.d("DEBUG_BOLETO", "Boleto já emitido mas sem link")
                    showError("Boleto já emitido, mas link indisponível.")
                } else {
                    Log.d("DEBUG_BOLETO", "Boleto não encontrado")
                    showConfirmDialog(
                        title = "Boleto não encontrado",
                        message = "Deseja emitir o boleto agora (sem nota fiscal)?",
                        negativeButton = "Cancelar",
                        callbackCancel = null,
                        confirmButton = "Emitir/baixar",
                        callback = { emitirBoletoParaDownload() },
                        neutralButton = null,
                        callbackNeutral = null
                    )
                }
            },
            {
                progress.dismiss()
                showError(it.message ?: Messages.defaultError, it.title ?: "Erro ao consultar boleto")
            }
        )
    }

    fun baixarArquivoBoleto(link: String, nomeArquivo: String) {
        val progress = ProgressDialog(this@VendaActivity)
        progress.setMessage("Baixando boleto...")
        progress.setCancelable(false)
        progress.show()

        Thread {
            try {
                val url = URL(link)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Accept", "application/pdf")
                connection.connect()
                
                Log.d("DEBUG_BOLETO", "Content-Type: ${connection.contentType}")
                Log.d("DEBUG_BOLETO", "Content-Length: ${connection.contentLength}")
                
                if (connection.responseCode in 200..299) {
                    val contentType = connection.contentType?.toLowerCase()
                    
                    // Verifica se realmente é um PDF
                    if (contentType?.contains("pdf") == true || contentType?.contains("application/octet-stream") == true) {
                        // Força extensão .pdf independente do nome original
                        val fileName = if (nomeArquivo.trim().isNotEmpty()) {
                            if (nomeArquivo.endsWith(".pdf")) nomeArquivo else "${nomeArquivo}.pdf"
                        } else {
                            "boleto_${System.currentTimeMillis()}.pdf"
                        }
                        
                        val file = File(getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS), fileName)
                        val inputStream = connection.inputStream
                        val outputStream = java.io.FileOutputStream(file)
                        val buffer = ByteArray(4096)
                        var bytesRead: Int
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }
                        outputStream.close()
                        inputStream.close()

                        runOnUiThread {
                            progress.dismiss()
                            showConfirmDialog(
                                title = "Boleto salvo!",
                                message = "Arquivo salvo em: ${file.absolutePath}",
                                negativeButton = "OK",
                                callbackCancel = null,
                                confirmButton = "Abrir",
                                callback = { abrirPDF(file) },
                                neutralButton = "Compartilhar",
                                callbackNeutral = { compartilharPDF(file) }
                            )
                        }
                    } else {
                        // Se não for PDF, mostra erro específico
                        runOnUiThread {
                            progress.dismiss()
                            showError("Link do boleto não está disponível ou expirou. Content-Type: $contentType")
                        }
                    }
                } else {
                    runOnUiThread {
                        progress.dismiss()
                        showError("Falha ao baixar boleto (HTTP ${connection.responseCode})")
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    progress.dismiss()
                    showError("Erro ao baixar boleto: ${e.message}")
                }
            }
        }.start()
    }

    fun imprimirBoleto(_b: BoletoResponse) {
        try {

            val boleto = BoletoUtils(
                /* nomeBanco */ stripAccents(_b.nomeBanco ?: ""),
                /* codBanco */ stripAccents(_b.codBanco ?: ""),
                /* linhaDigitavel */ stripAccents(_b.linhaDigitavel ?: ""),
                /* localPagamento */ stripAccents(_b.nomeBanco ?: ""),
                /* localOpcionalPagamento */ stripAccents(_b.localPagamento ?: ""),
                /* vencimento */ stripAccents(_b.vencimento ?: ""),
                /* cedente */ stripAccents(_b.cedente ?: ""),
                /* agenciaCodigoCedente */ stripAccents(_b.agenciaCodigoCedente ?: ""),
                /* datadocumento */ stripAccents(_b.datadocumento ?: ""),
                /* numeroDocumento */ stripAccents(_b.numeroDocumento ?: ""),
                /* especieDoc */ stripAccents(_b.especieDoc ?: ""),
                /* aceite */ stripAccents("Nao"),
                /* dataProcessameto */ stripAccents(_b.datadocumento ?: ""),
                /* nossoNumero */ stripAccents(_b.nossoNumero ?: ""),
                /* usoDoBanco */ stripAccents(""),
                /* cip */ stripAccents(""),
                /* carteira */ stripAccents(_b.carteira ?: ""),
                /* especieMoeda */ stripAccents(_b.especieMoeda ?: ""),
                /* quantidade */ stripAccents(" "),
                /* valor */ stripAccents(""),
                /* valorDocumento */ stripAccents(_b.valorDocumento ?: ""),
                /* instrucoesCedente */ stripAccents(""),
                /* desconto */ stripAccents(_b.desconto ?: ""),
                /* deducoes */ stripAccents("0.00"),
                /* multa */ stripAccents("0.00"),
                /* acrescimos */ stripAccents("0.00"),
                /* valorCobrado */ stripAccents(_b.valorCobrado ?: ""),
                /* sacadoNome */ stripAccents(_b.sacadoNome ?: ""),
                /* sacadoEndereco */ stripAccents(_b.sacadoEndereco ?: ""),
                /* sacadoCep */ stripAccents(_b.sacadoCep ?: ""),
                /* sacadoCidade */ stripAccents(_b.sacadoCidade ?: ""),
                /* sacadoUF */ stripAccents(_b.sacadoUF ?: ""),
                /* sacadoCnpj */ stripAccents(_b.sacadoCnpj ?: ""),
                /* autentificacao */ stripAccents(_b.autentificacao ?: ""),
                /* fichaCompensacao */ stripAccents(_b.fichaCompensacao ?: "")
            )

            boleto.instrucoesCedente = arrayOf(
                "",
                "",
                stripAccents("Após vencimento, cobrar multa de ${_b.multa ?: "--"}"),
                stripAccents("Após vencimento, cobrar juros de ${_b.juros ?: "--"}"),
                stripAccents(if (_b.instrucoesCedente != null && _b.instrucoesCedente != "") _b.instrucoesCedente else "SEM INSTRUÇÃO"),
                "",
                ""
            )

            val boletoprinter = (application as App).getBoletoPrinter();
            boletoprinter.mobilePrinter.Reset();
            boletoprinter.printBoleto(boleto);
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            requestHandler.logCrash(e)
        }
    }

    fun abrirPDF(file: java.io.File) {
        try {
            val uri = androidx.core.content.FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                file
            )
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } catch (e: Exception) {
            showError("Nenhum aplicativo encontrado para abrir PDF")
        }
    }

    private fun compartilharPDF(file: java.io.File) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/pdf"
            val uri = androidx.core.content.FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                file
            )
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(intent, "Compartilhar PDF"))
        } catch (e: Exception) {
            e.printStackTrace()
            showError("Não foi possível compartilhar o PDF")
        }
    }

}

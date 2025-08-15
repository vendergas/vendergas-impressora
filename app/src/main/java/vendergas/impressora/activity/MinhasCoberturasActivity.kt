package vendergas.impressora.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_minhas_coberturas.*
import vendergas.impressora.R
import vendergas.impressora.adapter.NotaCoberturaAdapter
import vendergas.impressora.base.BaseActivity
import vendergas.impressora.models.NotaCobertura
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.google.gson.Gson
import vendergas.impressora.App
import vendergas.impressora.models.Cliente
import vendergas.impressora.models.Page
import vendergas.impressora.print.NotaFiscal


class MinhasCoberturasActivity : BaseActivity() {

    private var coberturas: ArrayList<NotaCobertura> = arrayListOf();
    private var coberturaAdapter: NotaCoberturaAdapter? = null;
    private var activeSearch: Boolean = false;
    private var imprimirRelacao: Boolean = false;
    private var mostrarRotas: Boolean = false;

    private var cobertura_search_handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minhas_coberturas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupView()

        setupCoberturaAdapter()
        carregarListaDeCoberturas()
    }

    fun setupView() {
        try {
            mostrarRotas = intent?.extras?.getBoolean("mostrarRotas", false) ?: false
            activeSearch = intent?.extras?.getBoolean("activeSearch", false) ?: false
            imprimirRelacao = intent?.extras?.getBoolean("imprimirRelacao", false) ?: false
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setActionBarTitle(intent?.extras?.getString("titulo", "") ?: "")
        showLoading(false, loading)

        autocomplete_cobertura?.visibility = if (activeSearch) View.VISIBLE else View.GONE

        if (activeSearch) {
            autocomplete_cobertura.addTextChangedListener(object : TextWatcher {

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    cobertura_search_handler.removeCallbacksAndMessages(null)
                    cobertura_search_handler.postDelayed({ carregarListaDeCoberturas(autocomplete_cobertura.text.toString()) }, 500)
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable) {}
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_minhas_coberturas, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_reresh -> {
                carregarListaDeCoberturas()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun carregarListaDeCoberturas(search: String? = null) {
        if (paramDefault.idEntregador != null) {
            requestHandler.sendRequest<List<NotaCobertura>>(
                requestHandler.ROTAS.getListaNotaCobertura(
                    _idEntregador = paramDefault.idEntregador!!,
                    searchString = search,
                    perPage = if (search != null) 10 else null,
                    page = if (search != null) 1 else null
                ),
                { res ->
                    coberturas.clear()
                    coberturas.addAll(res)
                    setupCoberturaAdapter()
                }
            )
        } else showSimpleDialog("Falha ao enviar requisição", "ID do Entregador não foi localizado")
    }

    fun setupCoberturaAdapter() {
        // if (coberturaAdapter == null) {
            coberturaAdapter = NotaCoberturaAdapter(this, coberturas)
            coberturas_list.adapter = coberturaAdapter
            coberturas_list.setOnItemClickListener { parent, view, position, id ->
                if (imprimirRelacao) {
                    imprimirRelacao(coberturas.get(position))
                } else {
                    val mBundle = Bundle()
                    mBundle.putString("cobertura", Gson().toJson(coberturas.get(position)))
                    mBundle.putBoolean("mostrarRotas", mostrarRotas)
                    changeActivity(RotasCoberturaActivity::class.java, mBundle)
                }
            }
        // }
        runOnUiThread { coberturaAdapter?.notifyDataSetChanged() }
    }

    fun imprimirRelacao(cobertura: NotaCobertura) {

        val progress = ProgressDialog(this@MinhasCoberturasActivity)

        progress.setMessage("Aguarde...")
        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog
        progress.show()

        Thread {
            val printer = (application as App).getPrinter()
            var connected = false
            try { connected = printer.connect(true) } catch (e: Exception) { e.printStackTrace() }
            if (connected) {
                NotaFiscal.genRelacaoNFRemessa(printer, cobertura)
                (application as App).disconnectPrinter()
                runOnUiThread { progress.dismiss() }
            } else {
                runOnUiThread {
                    showConfirmDialog(
                        title = "Atenção!",
                        message = "Não foi possível conectar a impressora, verifique se o pareamento está ativo e tente novamente.",
                        negativeButton = "Cancelar",
                        callbackCancel = null,
                        confirmButton = "Tentar novamente",
                        callback = { imprimirRelacao(cobertura) },
                        neutralButton = null,
                        callbackNeutral = null
                    )
                    (application as App).disconnectPrinter()
                    progress.dismiss()
                }
            }
        }.start()
    }

}

package vendergas.impressora.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import inputservice.printerLib.BoletoUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import vendergas.impressora.App
import vendergas.impressora.R
import vendergas.impressora.base.BaseActivity
import vendergas.impressora.connection.Bluetooth
import vendergas.impressora.models.BoletoResponse
import vendergas.impressora.models.Messages
import vendergas.impressora.models.NotaCobertura
import vendergas.impressora.print.NotaFiscal

class MainActivity : BaseActivity() {

    internal lateinit var mBth: Bluetooth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setActionBarTitle("VENDERGAS")

        fab.setOnClickListener { view -> teste(view) }

        imprimir.setOnClickListener { imprimir() }

        minhas_coberturas.setOnClickListener {
            val mBundle = Bundle()
            mBundle.putBoolean("activeSearch", true)
            mBundle.putBoolean("imprimirRelacao", false)
            mBundle.putBoolean("mostrarRotas", false)
            mBundle.putString("titulo", "SELECIONAR DESPACHO")
            changeActivity(MinhasCoberturasActivity::class.java, mBundle)
        }

        buscar_cobertura.setOnClickListener {
            val mBundle = Bundle()
            mBundle.putBoolean("activeSearch", true)
            mBundle.putBoolean("imprimirRelacao", false)
            mBundle.putBoolean("mostrarRotas", true)
            mBundle.putString("titulo", "MINHAS ROTAS")
            changeActivity(MinhasCoberturasActivity::class.java, mBundle)
        }

        imprimir_relacao_remessa.setOnClickListener {
            val mBundle = Bundle()
            mBundle.putBoolean("activeSearch", true)
            mBundle.putBoolean("imprimirRelacao", true)
            mBundle.putBoolean("mostrarRotas", false)
            mBundle.putString("titulo", "IMPRIMIR NF POR DESPACHO")
            changeActivity(MinhasCoberturasActivity::class.java, mBundle)
        }

        busucar_venda.setOnClickListener {
            if (paramDefault.idEntregador != null) {

                val progress = ProgressDialog(this@MainActivity)

                progress.setMessage("Buscando vendas...")
                progress.setCancelable(false)
                progress.show()

                requestHandler.sendRequest<List<NotaCobertura.Itinerario>>(
                    requestHandler.ROTAS.getListaVendasCobertura(_idEntregador = paramDefault.idEntregador!!),
                    { res ->
                        val cobertura = NotaCobertura()
                        cobertura.itinerario = res
                        cobertura.produtos = listOf()

                        runOnUiThread { progress.dismiss() }

                        val mBundle = Bundle()
                        mBundle.putString("cobertura",  Gson().toJson(cobertura))
                        mBundle.putBoolean("mostrarRotas", true)
                        mBundle.putBoolean("mostrarTudo", true)
                        changeActivity(RotasCoberturaActivity::class.java, mBundle)
                    },
                    {
                        runOnUiThread { progress.dismiss() }
                        showError(it.message ?: Messages.defaultError, it.title ?: "Tente novamente")
                    }
                )
            } else showSimpleDialog("Falha ao enviar requisição", "ID do Entregador não foi localizado")
        }

        mBth = Bluetooth(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        closeBth()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sair -> {
                sharedPreference.clear()
                changeActivityAndRemoveParentActivity(LoginActivity::class.java)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun teste(view: View) {
        Toast.makeText(this@MainActivity,"Impressão de TESTE", Toast.LENGTH_LONG).show();
        try {

            val progress = ProgressDialog(this@MainActivity)

            progress.setMessage("Aguarde...")
            progress.setCancelable(false) // disable dismiss by tapping outside of the dialog
            progress.show()

            Thread {
                val printer = (application as App).getBoletoPrinter()
                var connected = false
                try { connected = printer.connect(false) } catch (e: Exception) { e.printStackTrace() }

                if (!connected) {
                    runOnUiThread {
                        showConfirmDialog(
                            title = "Atenção!",
                            message = "Não foi possível conectar a impressora, verifique se o pareamento está ativo e tente novamente.",
                            negativeButton = null,
                            callbackCancel = null,
                            confirmButton = "OK",
                            callback = null,
                            neutralButton = null,
                            callbackNeutral = null
                        )
                        (application as App).disconnectBoletoPrinter()
                        progress.dismiss()
                    }
                    return@Thread
                }

                try {
                    val boleto = BoletoUtils(
                        "Bradesco",
                        "237-2",
                        "23792.38401 61130.370598 68001.271805 1 56220000320000",
                        "Banco Bradesco S.A.",
                        "Pagavel preferencialmente na rede bradesco ou banco postal.",
                        "27/02/2013",
                        "Input Service Informatica Ltda",
                        "237-2",
                        "06/02/2013",
                        "06/11/303705968-5",
                        "OU",
                        "Nao",
                        "06/02/2013",
                        "06/11/303705968-5",
                        "08650",
                        "000",
                        "06",
                        "R$",
                        "",
                        "",
                        "3.200,00",
                        "",
                        " -200,00",
                        "  -10,00",
                        "   50,00",
                        "   10,00",
                        "3.050,00",
                        "INPUT SERVICE INFORMATCA LTDA",
                        "RUA DEPUTADO MIGUEL PETRELLI, 355  - COTIA",
                        "06705-442",
                        "COTIA", // "COTIA",
                        "SP",
                        "061.557.856/0001-57",
                        "Autenticação Mecânica",
                        ""
                    )

                    val boletoprinter = (application as App).getBoletoPrinter()
                    boletoprinter.getMobilePrinter().Reset()
                    boletoprinter.printBoleto(boleto)
                    (application as App).disconnectBoletoPrinter()
                    runOnUiThread { progress.dismiss() }
                } catch (e: Exception) {
                    e.printStackTrace()
                    requestHandler.logCrash(e)
                }
            }.start()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            requestHandler.logCrash(e)
        }
    }

    fun imprimir() {
        // NotaFiscal.genRelacaoNFRemessa(this@MainActivity);

        /*
        if (checkBth()) {
            val bmp = NotaFiscal.createSample(this@MainActivity);

            runOnUiThread {
                imageView.setImageBitmap(bmp)
            }

            ESCP.ImageToEsc(bmp, mBth.ostream, 7, 1)
        }
        */
    }

    fun checkBth(): Boolean {
        if (!mBth.isEnabled) {
            if (!mBth.Enable()) {
                Alert("Nao foi possivel habilitar o bluetooth, tente habilitar manualmente e tente novamente.")
                return false
            }
        }

        deviceCheck@ for (device in mBth.GetBondedDevices()) {
            when(device.name) {
                "MPT-III", "GS-MTP8" -> {
                    val mac = device.address

                    if (!mBth.Open(mac)) {
                        Alert("Nao foi possivel conectar ao dispositivo [$mac]\n\nLigue ou conecte o dispositivo e tente novamente.")
                        return false
                    } else {
                        Alert("Conectado ao dispositivo [$mac]/${device.name}")
                    }

                    break@deviceCheck;
                }
            }
        }

        if (!mBth.isConnected) {
            Alert("Nao foi encontrada nenhuma conexão com dispositivo\n\nFaça o pareamento com o disposivo e tente novamente.")
            return false
        }
        return true
    }

    fun closeBth(): Boolean = if (mBth.isConnected) { mBth.Close() } else false

    fun Alert(message: String) {
        val ad = AlertDialog.Builder(this).create()
        ad.setCancelable(false)
        ad.setMessage(message)
        ad.setButton("OK") { dialog, which -> dialog.dismiss() }
        ad.show()
    }

}

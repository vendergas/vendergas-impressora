package vendergas.impressora

import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import inputservice.printerLib.BoletoPrinter
import inputservice.printerLib.NfePrinterA7
import okhttp3.internal.waitMillis

class App: MultiDexApplication() {

    private var nfePrinterA7: NfePrinterA7? = null

    @Synchronized
    fun getPrinter(): NfePrinterA7 {
        if (nfePrinterA7 == null) nfePrinterA7 = NfePrinterA7();
        return nfePrinterA7!!
    }

    @Synchronized
    fun disconnectPrinter(): Boolean {
        if (nfePrinterA7 == null) nfePrinterA7 = NfePrinterA7();
        try {
            if (NfePrinterA7.connected) {
                if(!NfePrinterA7.isA7Light) {
                    while (nfePrinterA7!!.mobilePrinter.QueryPrinterStatus() != 0) { }
                }
                nfePrinterA7!!.disconnect()
            }
        } catch (e: Exception) {
            _logCrash(e)
        }
        return !NfePrinterA7.connected
    }

    private var boletoPrinter: BoletoPrinter? = null

    @Synchronized
    fun getBoletoPrinter(): BoletoPrinter {
        if (boletoPrinter == null) boletoPrinter = BoletoPrinter();
        return boletoPrinter!!
    }

    @Synchronized
    fun disconnectBoletoPrinter(): Boolean {
        if (boletoPrinter == null) boletoPrinter = BoletoPrinter();
        try {
            if (BoletoPrinter.connected) {
                if(!NfePrinterA7.isA7Light) {
                    while (boletoPrinter!!.mobilePrinter.QueryPrinterStatus() != 0) { }
                }
                boletoPrinter!!.disconnect()
            }
        } catch (e: Exception) {
            _logCrash(e)
        }
        return !BoletoPrinter.connected
    }

    fun _logCrash(error: Exception) {
        error.printStackTrace()
        try { Crashlytics.logException(error) } catch (e: Exception) { e.printStackTrace() }
    }

}
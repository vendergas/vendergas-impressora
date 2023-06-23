package vendergas.impressora.base

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import vendergas.impressora.R
import vendergas.impressora.models.Endereco
import vendergas.impressora.models.Entregador
import vendergas.impressora.models.ParamDefault
import vendergas.impressora.network.RequestHandler
import java.text.Normalizer
import kotlin.Exception
import android.view.ViewGroup
import android.widget.ListView
import vendergas.impressora.activity.MainActivity

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private var progressBar: ProgressBar? = null

    val sharedPreference by lazy { SharedPreference(this) }
    val paramDefault by lazy { ParamDefault(sharedPreference.getObject<Entregador>(SharedPreference.KEYS.DELIVERYMAN, Entregador::class.java)) }
    val requestHandler by lazy { RequestHandler(this, paramDefault.token) }

    fun showLoading(show: Boolean, bar: ProgressBar? = null) {
        bar?.let { progressBar = it }
        if (show) progressBar?.visibility = View.VISIBLE
        else progressBar?.visibility = View.GONE
    }

    fun showError(message: String, title: String? = "Erro") = showSimpleDialog(title!!, message)

    fun showMessage(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    fun showSuccess(message: String) = showMessage(message)

    fun exit() = finishAffinity()

    fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun getContext(): Context = this

    fun commitFragment(containerId: Int, fragment: Fragment, addToBackStackName: String? = null) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // fragmentTransaction.add(containerId, fragment)
        fragmentTransaction.replace(containerId, fragment)
        fragmentTransaction.addToBackStack(addToBackStackName)
        fragmentTransaction.commit()
        // fragmentTransaction.commitAllowingStateLoss()
    }

    fun removeFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.commit()
    }

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    /**
     * -------------------------------------
     * ACTION BAR
     * -------------------------------------
     */

    fun setActionBarTitle(cs: CharSequence) {
        supportActionBar?.title = cs
    }

    /**
     * -------------------------------------
     * DIALOG
     * -------------------------------------
     */

    private fun startDialog(title: String, message: String, setButton: Boolean = true, create: Boolean = true): AlertDialog {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        var boletoJaEmitido = message == "Nota fiscal já emitida para esse cliente. Clique abaixo para imprimir a nota e boleto."
        if (setButton) alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, if(boletoJaEmitido) "Atualizar para imprimir" else "OK") { dialog, _ ->
            if(boletoJaEmitido) {
                changeActivityAndRemoveParentActivity(MainActivity::class.java)
                finishAffinity()
            }
            else {
                dialog.dismiss()
            }
        }
        return alertDialog;
    }

    fun showSimpleDialog(title: String, message: String) = try { startDialog(title, message).show() } catch (e: Exception) { e.printStackTrace() }

    fun showBackDialog(title: String, message: String) {
        try {
            val d = startDialog(title, message);
            d.setOnDismissListener { dialog -> this.onBackPressed() }
            d.show()
        } catch (e: Exception) { e.printStackTrace() }
    }

    fun showDialogNewActivity(title: String, message: String, destinyActivity: Class<*>) {
        try {
            val d = startDialog(title, message, false)
            d.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { dialog, _ ->
                dialog.dismiss()
                this.startActivity(Intent(this, destinyActivity::class.java))
            }
            d.show()
        } catch (e: Exception) { e.printStackTrace() }
    }

    fun showConfirmDialog(
        title: String,
        message: String,
        cancelable: Boolean? = false,
        confirmButton: String? = "OK",
        negativeButton: String? = null,
        neutralButton: String? = null,
        callback: (() -> (Unit))? = null,
        callbackCancel: (() -> (Unit))? = null,
        callbackNeutral: (() -> (Unit))? = null
    ) {
        try {
            val builder = AlertDialog.Builder(this, R.style.VENDERGASDialogThemeGreen)
            builder.setMessage(message).setTitle(title)

            if (confirmButton != null) builder.setCancelable(cancelable!!)
            if (confirmButton != null) {
                builder.setPositiveButton(confirmButton) { dialog, id ->
                    if (callback != null) callback()
                }
            }

            if (negativeButton != null) {
                builder.setNegativeButton(negativeButton) { dialog, id ->
                    dialog.cancel()
                    if (callbackCancel != null) callbackCancel()
                }
            }

            if (neutralButton != null) {
                builder.setNeutralButton(neutralButton) { dialog, id ->
                    if (callbackNeutral != null) callbackNeutral()
                }
            }

            builder.create().show()
        } catch (e: Exception) { e.printStackTrace() }
    }

    /**
     * -------------------------------------
     * Activity Manager
     * -------------------------------------
     */

    fun changeActivity(destinyActivity: Class<*>, bundleExtras: Bundle? = null) = changeActivityBuilder(destinyActivity, bundleExtras)

    fun changeActivityOnTopOfHome(destinyActivity: Class<*>, bundleExtras: Bundle? = null) {
        changeActivityBuilder(destinyActivity, bundleExtras, Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME)

        // this.startActivity(IntentCompat.makeRestartActivityTask(Intent(this, destinyActivity).component))
    }

    fun changeActivityAndRemoveParentActivity(destinyActivity: Class<*>, bundleExtras: Bundle? = null) = changeActivityBuilder(destinyActivity, bundleExtras, Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

    private fun changeActivityBuilder(destinyActivity: Class<*>, bundleExtras: Bundle? = null, activityFlags: Int? = null) {
        val intent = Intent(this, destinyActivity)
        if (activityFlags != null) intent.addFlags(activityFlags)
        if (bundleExtras != null) intent.putExtras(bundleExtras)
        this.startActivity(intent)
    }

    /**
     *
     *
     *
     */
    fun formatEndereco(e: Endereco?): String {
        var endFormated: String? = null
        if (e != null) {
            if (e.endereco != null) endFormated = e.endereco
            if (e.numero != null) endFormated += (if (endFormated != null && endFormated != "") ", " else "") + e.numero
            if (e.bairro != null) endFormated += (if (endFormated != null && endFormated != "") ", " else "") + e.bairro
            if (e.cidade != null) endFormated += (if (endFormated != null && endFormated != "") ", " else "") + e.cidade
            if (e.estadoAcronimo != null) endFormated += (if (endFormated != null && endFormated != "") (if (e.cidade != null) " - " else ", ") else "") + e.estadoAcronimo
            if (e.cep != null) endFormated += (if (endFormated != null && endFormated != "") ", " else "") + e.cep
        }
        return endFormated ?: "--"
    }

    fun stripAccents(s: String?): String {
        var _s = s ?: ""
        try {
            _s = Normalizer.normalize(_s, Normalizer.Form.NFD)
            _s = _s.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return _s
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    fun setListViewHeightBasedOnItems(listView: ListView): Boolean {
        val listAdapter = listView.getAdapter()
        if (listAdapter != null) {

            val numberOfItems = listAdapter!!.getCount()

            // Get total height of all items.
            var totalItemsHeight = 0
            for (itemPos in 0 until numberOfItems) {
                val item = listAdapter!!.getView(itemPos, null, listView)
                item.measure(0, 0)
                totalItemsHeight += item.getMeasuredHeight()
            }

            // Get total height of all item dividers.
            val totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1)

            // Set list height.
            val params = listView.getLayoutParams()
            params.height = totalItemsHeight + totalDividersHeight
            listView.setLayoutParams(params)
            listView.requestLayout()

            return true
        } else return false
    }

}

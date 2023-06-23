package vendergas.impressora.activity

import android.os.Bundle
import android.os.Handler
import vendergas.impressora.R
import vendergas.impressora.base.BaseActivity
import vendergas.impressora.base.SharedPreference

class LauncherScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher_screen)

        Handler().postDelayed({
            changeActivity(if (sharedPreference.getBoolean(SharedPreference.KEYS.LOGGED)!!) MainActivity::class.java else LoginActivity::class.java)
            finish()
        }, 2500)
    }
}

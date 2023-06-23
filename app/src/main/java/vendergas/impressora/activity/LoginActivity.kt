package vendergas.impressora.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import vendergas.impressora.R
import vendergas.impressora.base.BaseActivity
import vendergas.impressora.base.SharedPreference
import vendergas.impressora.models.AuthResponse
import vendergas.impressora.models.Entregador

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupView()
    }

    fun setupView() {
        button_sing_in.setOnClickListener { autenticarPorEmail() }
        showLoading(false, loading)
    }

    fun autenticarPorEmail() {
        val body = validate()
        if (body != null) {
            requestHandler.sendRequest<AuthResponse>(
                requestHandler.ROTAS.authByEmail(body),
                { res ->
                    val defaultInfo = res.user!!;
                    defaultInfo.token = res.token;

                    sharedPreference.setObject(SharedPreference.KEYS.DELIVERYMAN, defaultInfo)
                    sharedPreference.setBoolean(SharedPreference.KEYS.LOGGED, true)
                    sharedPreference.setString(SharedPreference.KEYS.EMAIL, defaultInfo.email!!)

                    changeActivityAndRemoveParentActivity(MainActivity::class.java)
                }
            )
        }
    }

    fun validate(): Entregador? {
        val e = Entregador();

        if (edit_email.getText().toString() == "") {
            edit_email.setError("Preencha o campo por favor!")
            return null
        } else {
            e.email = edit_email.getText().toString();
        }

        /*
        if (editPassword.getText().toString().equals("")) {
            editPassword.setError("Preencha o campo por favor!");
            return false;
        } else {
            deliveryman.setPassword(editPassword.getText().toString());
        }
        */

        return e
    }

}

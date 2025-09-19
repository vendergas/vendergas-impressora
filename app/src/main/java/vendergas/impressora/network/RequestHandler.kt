package vendergas.impressora.network

import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import vendergas.impressora.BuildConfig
import vendergas.impressora.base.BaseActivity
import vendergas.impressora.models.Messages
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

const val ENDPOINT: String = "https://api.vendergas.com.br";
//const val ENDPOINT: String = "http://192.168.0.11:3000";
//const val ENDPOINT: String = "https://api.meu.vendergas.com.br";

typealias RequestCallback<O> = (O) -> Unit

@Suppress("UNCHECKED_CAST")

class RequestHandler(private val view: BaseActivity? = null, private val authToken: String? = null) {

    lateinit var ROTAS: RetrofitInterface

    init {
        this.setupRestClient(authToken)
    }

    fun <O> sendRequest(observable: Observable<*>?, callback: RequestCallback<O>, callbackError: RequestCallback<ErrorResponse>? = null) {
        try {
            if (view != null) view.showLoading(true)
            val o = observable?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
                    { result -> callback.invoke(result as O) },
                    { error ->
                        logCrash(error)
                        if (callbackError != null) {
                            if (view != null) view.showLoading(false)
                            callbackError.invoke(formatErrorResponse(if (error is HttpException) error else null))
                        } else if (view != null) showFormatedError(error)
                    },
                    { if (view != null) view.showLoading(false) }
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if (view != null) Toast.makeText(view.getContext(),"Não foi possível realizar a requisição, verifique sua conexão e tente novamente",Toast.LENGTH_SHORT).show()
        }
    }

    fun formatErrorResponse(e: Throwable?): ErrorResponse {
        try {
            if (e is HttpException) {
                return Gson().fromJson<ErrorResponse>(e.response().errorBody()!!.string(), ErrorResponse::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ErrorResponse(Messages.defaultError, "");
    }

    fun showFormatedError(e: Throwable) {
        if (view == null) return

        view.showLoading(false)

        if (e is HttpException) {
            val response: ErrorResponse = formatErrorResponse(e);

            if (response.title != null && response.message != null) {
                view.showError(response.message!!, response.title!!)
            } else {
                when (e.code()) {
                    400 -> view.showError(response.message!!)
                    401 -> view.showError("Erro: falha de autenticação.")
                    403 -> view.showError("Erro: operação não permitida para este usuário.")
                    404 -> view.showError("Erro: servidor não encontrado.")
                    413 -> view.showError("Erro: os dados são muito grandes para serem enviados.")
                    500 -> view.showError("Erro interno no servidor.")
                    else -> view.showError("Erro.")
                }
            }
        } else if (e is UnknownHostException) try { view.showError("Verifique a conexão com a Internet.") } catch (e: Exception) { e.printStackTrace() }

        e.printStackTrace()
    }

    fun logCrash(error: kotlin.Throwable) {
        error.printStackTrace()
        try { Crashlytics.logException(error) } catch (e: Exception) { e.printStackTrace() }
    }

    class ErrorResponse (
            @Expose
            @SerializedName("message")
            var message: String? = null,

            @Expose
            @SerializedName("title")
            var title: String? = null,

            @Expose
            @SerializedName("code")
            var code: String? = null
    ) {}

    /**
     * ---------------------------------------------
     * SETUP API
     * ---------------------------------------------
     */

    private fun setupRestClient(token: String?) {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .excludeFieldsWithoutExposeAnnotation()
            .create()

        val builder = okhttp3.OkHttpClient.Builder()

        val httpLoggingInterceptor = HttpLoggingInterceptor()

        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        builder.networkInterceptors().add(httpLoggingInterceptor)
        builder.readTimeout(5, TimeUnit.MINUTES)
        builder.connectTimeout(5, TimeUnit.MINUTES)

        builder.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("app", "Android")
                .addHeader("version", BuildConfig.VERSION_NAME)
                .addHeader("componentId", "app:impressora")
            if (token != null) request.addHeader("Authorization", token)
            return@addInterceptor chain.proceed(request.build())
        }

        val retrofit = Retrofit.Builder()
            .client(builder.build())
            .baseUrl(ENDPOINT)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        ROTAS = retrofit.create<RetrofitInterface>(RetrofitInterface::class.java)
    }
}

package vendergas.impressora.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthResponse {

    @Expose
    @SerializedName("user")
    var user: Entregador? = null

    @Expose
    @SerializedName("token")
    var token: String? = null

}
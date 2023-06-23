package vendergas.impressora.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Entregador {

    var cpfImage: String? = null
    var token: String? = null

    @Expose
    @SerializedName("idEntregador")
    var deliverymanId: String? = null

    @Expose
    @SerializedName("nome")
    var name: String? = null

    @Expose
    @SerializedName("email")
    var email: String? = null

    @Expose
    @SerializedName("cpf")
    var cpf: String? = null

    @Expose
    @SerializedName("senha")
    var password: String? = null

    @Expose
    @SerializedName("imagemUrl")
    var imageURL: String? = null

}

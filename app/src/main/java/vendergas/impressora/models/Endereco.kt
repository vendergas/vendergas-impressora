package vendergas.impressora.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Endereco: CommonFields() {

    @Expose
    @SerializedName("isDefault")
    var isDefault: Boolean? = null

    @Expose
    @SerializedName("complemento")
    var complemento: String? = null

    @Expose
    @SerializedName("endereco")
    var endereco: String? = null

    @Expose
    @SerializedName("numero")
    var numero: String? = null

    @Expose
    @SerializedName("bairro")
    var bairro: String? = null

    @Expose
    @SerializedName("cidade")
    var cidade: String? = null

    @Expose
    @SerializedName("estadoAcronimo")
    var estadoAcronimo: String? = null

    @Expose
    @SerializedName("cep")
    var cep: String? = null

    @Expose
    @SerializedName("lat")
    var lat: String? = null

    @Expose
    @SerializedName("long")
    var long: String? = null

}

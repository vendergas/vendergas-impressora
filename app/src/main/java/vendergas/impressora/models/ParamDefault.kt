package vendergas.impressora.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ParamDefault (deliveryman: Entregador?) {

    @Expose
    @SerializedName("idEntregador")
    var idEntregador: String? = deliveryman?.deliverymanId

    @Expose
    @SerializedName("entregador")
    var entregador: Entregador? = deliveryman

    @Expose
    @SerializedName("Authorization")
    var token: String? = deliveryman?.token

    @Expose
    @SerializedName("date")
    var date: String? = null

    @Expose
    @SerializedName("offset")
    var offset: Int = 0

    @Expose
    @SerializedName("limit")
    var limit: Int = 0

}

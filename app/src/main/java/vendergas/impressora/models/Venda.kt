package vendergas.impressora.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Venda {

    @Expose
    @SerializedName("tipoNota")
    var tipoNota: String? = null

    @Expose
    @SerializedName("observacao")
    var observacao: String? = null

    @Expose
    @SerializedName("itinerario")
    var itinerario: NotaCobertura.Itinerario? = null

    @Expose
    @SerializedName("empresa")
    var empresa: String? = null

    @Expose
    @SerializedName("loja")
    var loja: String? = null

}
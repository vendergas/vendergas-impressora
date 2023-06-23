package vendergas.impressora.models.enums

import com.google.gson.annotations.SerializedName

enum class ItinerarioStatus(val value: String) {

    @SerializedName("venda_nao_iniciada")
    VENDA_NAO_INICIADA("venda_nao_iniciada"),

    @SerializedName("venda_cancelada")
    VENDA_CANCELADA("venda_cancelada"),

    @SerializedName("venda_emitida")
    VENDA_EMITIDA("venda_emitida"),

    @SerializedName("venda_autorizada")
    VENDA_AUTORIZADA("venda_autorizada"),

    @SerializedName("erro_ao_emitir_nf")
    ERRO_AO_EMITIR_NF("erro_ao_emitir_nf");

    fun getFormatedValue(): String {
        when (value) {
            VENDA_NAO_INICIADA.value -> return "Venda não iniciada"
            VENDA_CANCELADA.value -> return "Venda cancelada"
            VENDA_EMITIDA.value -> return "Venda emitida"
            VENDA_AUTORIZADA.value -> return "Venda autorizada"
            ERRO_AO_EMITIR_NF.value -> return "Erro ao emitir NF"
            else -> return "--"
        }
    }

}
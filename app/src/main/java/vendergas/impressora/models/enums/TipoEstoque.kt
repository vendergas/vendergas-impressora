package vendergas.impressora.models.enums

import com.google.gson.annotations.SerializedName

enum class TipoEstoque(val value: String) {

    @SerializedName("1")
    ESTOQUE_VAZIO("1"),

    @SerializedName("2")
    ESTOQUE_CHEIO("2"),

    @SerializedName("3")
    ESTOQUE_UNICO("3");

    fun getFormatedValue(): String {
        when (value) {
            ESTOQUE_VAZIO.value -> return "Vazio"
            ESTOQUE_CHEIO.value -> return "Cheio"
            ESTOQUE_UNICO.value -> return "Único"
            else -> return "--"
        }
    }
}
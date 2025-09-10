package vendergas.impressora.models.enums

import com.google.gson.annotations.SerializedName

enum class TipoPagamento(val value: String) {

    @SerializedName("boleto")
    BOLETO( "boleto"),

    @SerializedName("cartao_debito")
    CARTAO_DEBITO( "cartao_debito"),

    @SerializedName("cartao_credito")
    CARTAO_CREDITO( "cartao_credito"),

    @SerializedName("dinheiro")
    DINHEIRO( "dinheiro"),

    @SerializedName("valeGas")
    VALE_GAS( "valeGas"),

    @SerializedName("cheque")
    CHEQUE( "cheque"),

    @SerializedName("pix")
    PIX( "pix"),

    @SerializedName("cartao_todos")
    CARTAO_TODOS( "cartao_todos"),

    @SerializedName("outros")
    OUTROS( "outros");

    fun getFormatedValue(): String {
        when (value) {
            BOLETO.value -> return "Boleto"
            CARTAO_DEBITO.value -> return "Cartão de Débito"
            CARTAO_CREDITO.value -> return "Cartão de Crédito"
            DINHEIRO.value -> return "Dinheiro"
            VALE_GAS.value -> return "Vale-Gás"
            CHEQUE.value -> return "Cheque"
            PIX.value -> return "PIX"
            CARTAO_TODOS.value -> return "Cartão de Débito/Cartão de Crédito"
            OUTROS.value -> return "Outros"
            else -> return "--"
        }
    }
}
package vendergas.impressora.models.enums

import com.google.gson.annotations.SerializedName

enum class TipoEstoqueNegociado(val value: String) {

    @SerializedName("estoqueNegociado:vazio")
    ESTOQUE_NEGOCIADO_VAZIO("estoqueNegociado:vazio"),

    @SerializedName("estoqueNegociado:cheio")
    ESTOQUE_NEGOCIADO_CHEIO("estoqueNegociado:cheio"),

    @SerializedName("estoqueNegociado:todos")
    ESTOQUE_NEGOCIADO_TODOS("estoqueNegociado:todos");

    fun getFormatedValue(): String {
        when (value) {
            ESTOQUE_NEGOCIADO_VAZIO.value -> return "Vazio"
            ESTOQUE_NEGOCIADO_CHEIO.value -> return "Cheio"
            ESTOQUE_NEGOCIADO_TODOS.value -> return "Todos"
            else -> return "--"
        }
    }
}
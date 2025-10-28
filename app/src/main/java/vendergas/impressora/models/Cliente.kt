package vendergas.impressora.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vendergas.impressora.models.enums.TipoEstoque
import vendergas.impressora.models.enums.TipoEstoqueNegociado

class Cliente: CommonFields() {

    @Expose
    @SerializedName("nome")
    var nome: String? = null

    fun getClienteNome(): String {
        return if (this.tipoCliente != "1" && this.razaoSocial != null) this.razaoSocial!! else this.nome ?: "Cliente sem nome"
    }

    @Expose
    @SerializedName("telefone")
    var telefone: String? = null

    @Expose
    @SerializedName("tipoCliente")
    var tipoCliente: String? = null

    @Expose
    @SerializedName("razaoSocial")
    var razaoSocial: String? = null

    @Expose
    @SerializedName("outrosTelefones")
    var outrosTelefones: List<String>? = listOf()

    @Expose
    @SerializedName("email")
    var email: String? = null

    @Expose
    @SerializedName("cnpj")
    var cnpj: String? = null

    @Expose
    @SerializedName("cpf")
    var cpf: String? = null

    @Expose
    @SerializedName("inscricaoEstadual")
    var inscricaoEstadual: String? = null


    @Expose
    @SerializedName("prazoMaximo")
    var prazoMaximo: Int? = null

    @Expose
    @SerializedName("enderecos")
    var enderecos: List<Endereco>? = null

    @Expose
    @SerializedName("valorNegociados")
    var valorNegociados: List<ValorNegociado>? = null

    class ValorNegociado {

        @Expose
        @SerializedName("cliente")
        var cliente: String? = null

        @Expose
        @SerializedName("produto")
        var produto: String? = null

        @Expose
        @SerializedName("valor")
        var valor: Float? = null

        @Expose
        @SerializedName("estoqueNegociado")
        var estoqueNegociado: String? = null

    }

}

package vendergas.impressora.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class CommonFields {

    @Expose
    @SerializedName("_id")
    var _id: String? = null

    @Expose
    @SerializedName("ativo")
    var ativo: Boolean? = null

    @Expose
    @SerializedName("empresa")
    var empresa: Estabelecimento? = null

    @Expose
    @SerializedName("loja")
    var loja: Estabelecimento? = null

    @Expose
    @SerializedName("empresas")
    var empresas: List<Estabelecimento>? = listOf()

    @Expose
    @SerializedName("lojas")
    var lojas: List<Estabelecimento>? = listOf()

    class Estabelecimento {

        @Expose
        @SerializedName("_id")
        var _id: String? = null

        @Expose
        @SerializedName("nomeFantasia")
        var nomeFantasia: String? = null

        @Expose
        @SerializedName("razaoSocial")
        var razaoSocial: String? = null

        @Expose
        @SerializedName("enderecos")
        var enderecos: List<Endereco>? = null

        @Expose
        @SerializedName("cnpj")
        var cnpj: String? = null

        @Expose
        @SerializedName("inscricaoEstadual")
        var inscricaoEstadual: String? = null

        @Expose
        @SerializedName("telefone")
        var telefone: String? = null

        @Expose
        @SerializedName("nfceVendergas")
        var nfceVendergas: Boolean? = null

        @Expose
        @SerializedName("jaEmitiuNfceVendergas")
        var jaEmitiuNfceVendergas: Boolean? = null
    }

}
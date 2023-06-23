package vendergas.impressora.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vendergas.impressora.models.enums.ItinerarioStatus
import vendergas.impressora.models.enums.TipoEstoque
import vendergas.impressora.models.enums.TipoPagamento

class NotaCobertura: CommonFields() {

    @Expose
    @SerializedName("chave")
    var chave: String? = null

    @Expose
    @SerializedName("serie")
    var serie: String? = null

    @Expose
    @SerializedName("numero")
    var numero: String? = null

    @Expose
    @SerializedName("valor")
    var valor: Float? = null

    @Expose
    @SerializedName("emissao")
    var emissao: String? = null

    @Expose
    @SerializedName("editarValor")
    var editarValor: Boolean? = null

    @Expose
    @SerializedName("bloquearQuantidade")
    var bloquearQuantidade: Boolean? = null

    @Expose
    @SerializedName("itinerario")
    var itinerario: List<NotaCobertura.Itinerario>? = listOf()

    @Expose
    @SerializedName("produtos")
    var produtos: List<NotaCobertura.Itinerario.Produto>? = listOf()

    class Itinerario: CommonFields() {

        @Expose
        @SerializedName("idCobertura")
        var idCobertura: String? = null

        @Expose
        @SerializedName("_uniqueId")
        var _uniqueId: String? = null

        @Expose
        @SerializedName("pedido")
        var pedido: String? = null

        @Expose
        @SerializedName("cliente")
        var cliente: Cliente? = null

        @Expose
        @SerializedName("endereco")
        var endereco: Endereco? = null

        @Expose
        @SerializedName("valor")
        var valor: Float? = null

        @Expose
        @SerializedName("produtos")
        var produtos: List<NotaCobertura.Itinerario.Produto>? = listOf()

        @Expose
        @SerializedName("emissaoNota")
        var emissaoNota: String? = null

        @Expose
        @SerializedName("chaveNota")
        var chaveNota: String? = null

        @Expose
        @SerializedName("tipoNota")
        var tipoNota: String? = null

        @Expose
        @SerializedName("natOperacao")
        var natOperacao: String? = null

        @Expose
        @SerializedName("descErroNF")
        var descErroNF: String? = null

        @Expose
        @SerializedName("serieNota")
        var serieNota: String? = null

        @Expose
        @SerializedName("numeroNota")
        var numeroNota: String? = null

        @Expose
        @SerializedName("urlNF")
        var urlNF: String? = null

        @Expose
        @SerializedName("status")
        var status: ItinerarioStatus? = null

        @Expose
        @SerializedName("pagamento")
        var pagamento: TipoPagamento? = null

        class Produto: CommonFields() {

            @Expose
            @SerializedName("nome")
            var nome: String? = null

            @Expose
            @SerializedName("quantidade")
            var quantidade: Int? = null

            @Expose
            @SerializedName("unCom")
            var unCom: String? = null

            @Expose
            @SerializedName("tipoEstoque")
            var tipoEstoque: TipoEstoque? = null

            @Expose
            @SerializedName("codigo")
            var codigo: String? = null

            @Expose
            @SerializedName("valorUnitario")
            var valorUnitario: Float? = null
        }

    }

}

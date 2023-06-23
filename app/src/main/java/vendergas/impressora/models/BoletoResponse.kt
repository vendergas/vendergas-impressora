package vendergas.impressora.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BoletoResponse {

    @Expose
    @SerializedName("linkBoleto")
    var linkBoleto: String? = null

    @Expose
    @SerializedName("nomeBoleto")
    var nomeBoleto: String? = null

    @Expose
    @SerializedName("jaEmitido")
    var jaEmitido: Boolean? = null

    @Expose
    @SerializedName("nossoNumero")
    var nossoNumero: String? = null

    @Expose
    @SerializedName("localPagamento")
    var localPagamento: String? = null

    @Expose
    @SerializedName("especieDoc")
    var especieDoc: String? = null

    @Expose
    @SerializedName("desconto")
    var desconto: String? = null

    @Expose
    @SerializedName("valorCobrado")
    var valorCobrado: String? = null

    @Expose
    @SerializedName("sacadoNome")
    var sacadoNome: String? = null

    @Expose
    @SerializedName("sacadoEndereco")
    var sacadoEndereco: String? = null

    @Expose
    @SerializedName("sacadoCep")
    var sacadoCep: String? = null

    @Expose
    @SerializedName("sacadoCidade")
    var sacadoCidade: String? = null

    @Expose
    @SerializedName("sacadoUF")
    var sacadoUF: String? = null

    @Expose
    @SerializedName("sacadoCnpj")
    var sacadoCnpj: String? = null

    @Expose
    @SerializedName("valor")
    var valor: String? = null

    @Expose
    @SerializedName("cedente")
    var cedente: String? = null

    @Expose
    @SerializedName("agenciaCodigoCedente")
    var agenciaCodigoCedente: String? = null

    @Expose
    @SerializedName("datadocumento")
    var datadocumento: String? = null

    @Expose
    @SerializedName("carteira")
    var carteira: String? = null

    @Expose
    @SerializedName("numeroDocumento")
    var numeroDocumento: String? = null

    @Expose
    @SerializedName("dataProcessameto")
    var dataProcessameto: String? = null

    @Expose
    @SerializedName("localOpcionalPagamento")
    var localOpcionalPagamento: String? = null

    @Expose
    @SerializedName("instrucoesCedente")
    var instrucoesCedente: String? = null

    @Expose
    @SerializedName("usoDoBanco")
    var usoDoBanco: String? = null

    @Expose
    @SerializedName("quantidade")
    var quantidade: String? = null

    @Expose
    @SerializedName("cip")
    var cip: String? = null

    @Expose
    @SerializedName("deducoes")
    var deducoes: String? = null

    @Expose
    @SerializedName("acrescimos")
    var acrescimos: String? = null

    @Expose
    @SerializedName("fichaCompensacao")
    var fichaCompensacao: String? = null

    @Expose
    @SerializedName("multa")
    var multa: String? = null

    @Expose
    @SerializedName("juros")
    var juros: String? = null

    @Expose
    @SerializedName("autentificacao")
    var autentificacao: String? = null

    @Expose
    @SerializedName("linhaDigitavel")
    var linhaDigitavel: String? = null

    @Expose
    @SerializedName("codBanco")
    var codBanco: String? = null

    @Expose
    @SerializedName("nomeBanco")
    var nomeBanco: String? = null

    @Expose
    @SerializedName("vencimento")
    var vencimento: String? = null

    @Expose
    @SerializedName("valorDocumento")
    var valorDocumento: String? = null

    @Expose
    @SerializedName("especieMoeda")
    var especieMoeda: String? = null

}
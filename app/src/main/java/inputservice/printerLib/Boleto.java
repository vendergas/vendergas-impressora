package inputservice.printerLib;

//responsavel por receber os dados do boneto
public abstract class Boleto {
	private String nomeBanco;

	private String codBanco = "";
	private String linhaDigitavel = "";
	private String localPagamento = "";
	private String localOpcionalPagamento = "";
	private String vencimento = "";
	private String cedente = "";
	private String agenciaCodigoCedente = "";
	private String datadocumento = "";
	private String numeroDocumento = "";
	private String especieDoc = "";
	private String aceite = "";
	private String dataProcessameto = "";
	private String nossoNumero = "";
	private String usoDoBanco = "";
	private String cip = "";
	private String carteira = "";
	private String especieMoeda = "";
	private String quantidade = "";
	private String valor = "";
	private String valorDocumento = "";
	private String[] instrucoesCedente =  { "", "", "", "", "", "", "" }; // new String[7];
	private String desconto = "";
	private String deducoes = "";
	private String multa = "";
	private String acrescimos = "";
	private String valorCobrado = "";
	private String sacadoNome = "";
	private String sacadoEndereco = "";
	private String sacadoCep = "";
	private String sacadoCidade = "";
	private String sacadoUF = "";
	private String sacadoCnpj = "";
	private String autentificacao = "";
	private String fichaCompensacao = "";
	private static String numeroCodBarras = "";

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public String getCodBanco() {
		return codBanco;
	}

	public void setCodBanco(String codBanco) {
		this.codBanco = codBanco;
	}

	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}

	public void setLinhaDigitavel(String linhaDigitavel) {

		this.linhaDigitavel = linhaDigitavel;
	}

	public String getLocalPagamento() {
		return localPagamento;
	}

	public void setLocalPagamento(String localPagamento) {
		this.localPagamento = localPagamento;
	}

	public String getLocalOpcionalPagamento() {
		return localOpcionalPagamento;
	}

	public void setLocalOpcionalPagamento(String localOpcionalPagamento) {
		this.localOpcionalPagamento = localOpcionalPagamento;
	}

	public String getVencimento() {
		return vencimento;
	}

	public void setVencimento(String vencimento) {
		this.vencimento = vencimento;
	}

	public String getCedente() {
		return cedente;
	}

	public void setCedente(String cedente) {
		this.cedente = cedente;
	}

	public String getAgenciaCodigoCedente() {
		return agenciaCodigoCedente;
	}

	public void setAgenciaCodigoCedente(String agenciaCodigoCedente) {
		this.agenciaCodigoCedente = agenciaCodigoCedente;
	}

	public String getDatadocumento() {
		return datadocumento;
	}

	public void setDatadocumento(String datadocumento) {
		this.datadocumento = datadocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getEspecieDoc() {
		return especieDoc;
	}

	public void setEspecieDoc(String especieDoc) {
		this.especieDoc = especieDoc;
	}

	public String getAceite() {
		return aceite;
	}

	public void setAceite(String aceite) {
		this.aceite = aceite;
	}

	public String getDataProcessameto() {
		return dataProcessameto;
	}

	public void setDataProcessameto(String dataProcessameto) {
		this.dataProcessameto = dataProcessameto;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getUsoDoBanco() {
		return usoDoBanco;
	}

	public void setUsoDoBanco(String usoDoBanco) {
		this.usoDoBanco = usoDoBanco;
	}

	public String getCip() {
		return cip;
	}

	public void setCip(String cip) {
		this.cip = cip;
	}

	public String getCarteira() {
		return carteira;
	}

	public void setCarteira(String carteira) {
		this.carteira = carteira;
	}

	public String getEspecieMoeda() {
		return especieMoeda;
	}

	public void setEspecieMoeda(String especieMoeda) {
		this.especieMoeda = especieMoeda;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValorDocumento() {
		return valorDocumento;
	}

	public void setValorDocumento(String valorDocumento) {
		this.valorDocumento = valorDocumento;
	}

	public String[] getInstrucoesCedente() {
		return instrucoesCedente;
	}

	public void setInstrucoesCedente(String instrucoesCedente) {
		String[] value = { instrucoesCedente != null ? instrucoesCedente : "", "", "", "", "", "", "" };
		this.setInstrucoesCedente(value);
	}

	public void setInstrucoesCedente(String[] instrucoesCedente) {
		if (instrucoesCedente.length > 7) {
			throw new IllegalArgumentException(
					"Erro em InstrucaoCedente: instruções nao podem ter mais que 7 linhas");
		}
		for (int i = 0; i < instrucoesCedente.length; i++) {
			if (instrucoesCedente[i] == null)
				instrucoesCedente[i] = "";
		}

		for (int i = 0; i < instrucoesCedente.length; i++) {
			if (instrucoesCedente[i].length() > 88) {
				throw new IllegalArgumentException(
						"Erro em InstrucaoCedente: linha " + (i + 1)
								+ " nao pode ter mais que 88 caracteres");
			}

		}
		this.instrucoesCedente = instrucoesCedente;
	}

	public String getDesconto() {
		return desconto;
	}

	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}

	public String getDeducoes() {
		return deducoes;
	}

	public void setDeducoes(String deducoes) {
		this.deducoes = deducoes;
	}

	public String getMulta() {
		return multa;
	}

	public void setMulta(String multa) {
		this.multa = multa;
	}

	public String getAcrescimos() {
		return acrescimos;
	}

	public void setAcrescimos(String acrescimos) {
		this.acrescimos = acrescimos;
	}

	public String getValorCobrado() {
		return valorCobrado;
	}

	public void setValorCobrado(String valorCobrado) {
		this.valorCobrado = valorCobrado;
	}

	public String getSacadoNome() {
		return sacadoNome;
	}

	public void setSacadoNome(String sacadoNome) {
		this.sacadoNome = sacadoNome;
	}

	public String getSacadoEndereco() {
		return sacadoEndereco;
	}

	public void setSacadoEndereco(String sacadoEndereco) {
		this.sacadoEndereco = sacadoEndereco;
	}

	public String getSacadoCep() {
		return sacadoCep;
	}

	public void setSacadoCep(String sacadoCep) {
		this.sacadoCep = sacadoCep;
	}

	public String getSacadoCidade() {
		return sacadoCidade;
	}

	public void setSacadoCidade(String sacadoCidade) {
		this.sacadoCidade = sacadoCidade;
	}

	public String getSacadoUF() {
		return sacadoUF;
	}

	public void setSacadoUF(String sacadoUF) {
		this.sacadoUF = sacadoUF;
	}

	public String getSacadoCnpj() {
		return sacadoCnpj;
	}

	public void setSacadoCnpj(String sacadoCnpj) {
		this.sacadoCnpj = sacadoCnpj;
	}

	public String getAutentificacao() {
		return autentificacao;
	}

	public void setAutentificacao(String autentificacao) {
		this.autentificacao = autentificacao;
	}

	public String getFichaCompensacao() {
		return fichaCompensacao;
	}

	public void setFichaCompensacao(String fichaCompensacao) {
		this.fichaCompensacao = fichaCompensacao;
	}

	public String getNumeroCodBarras() {
		return numeroCodBarras;
	}

	public static void setNumeroCodBarras(String codBarras) {
		Boleto.numeroCodBarras = codBarras;
	}

	public abstract void genNumeroCodBarras();

	/*
	 * Sugestoes de melhorias
	 * 
	 * Criar método setInstruçoes(String instrucao), que receberá uma string que
	 * será convertida em String[], o que ira facilitar na utilição da classe
	 */

}

/*
 
 
 
 
 */

package inputservice.printerLib;

public class Template {

	public final static BoletoBradesco BoletoBradesco() {
		return new BoletoBradesco();

	}

	public final static class BoletoBradesco {

		public static String[] Template() {

			return new String[] {
					"\u00AA\u00AB                   \u0010             \u0010",
					"\u00AC\u00AD  #nomebanco \u0010   #codba    \u0010 #linhadig",
					"_____________________\u0004_____________\u0004________________________________________________________________________________________",
					"Local de Pagamento   #localpag  \u0010 Vencimento",
					"                     #opcaopag  \u0010                           #venc",
					"______________________________________________________________________________________\u0004_____________________________________",
					"Cedente                                                                               \u0010 Agencia/Codigo cedente",
					"#cedente  \u0010        #agenciacodcedente",
					" _____________________________________________________________________________________\u0004_____________________________________",
					"Data do Documento  \u0010 N  do Documento \u0010 Especie Doc.\u0010 Aceite  \u0010 Data do Processamento  \u0010 Cart./Nosso Numero",
					"       #datadoc  \u0010      #numerodoc \u0010  #espdoc    \u0010  #ace   \u0010             #datapro \u0010                    #nossonum",
					"___________________\u0004_________________\u0004_____________\u0004_________\u0004________________________\u0004_____________________________________",
					"Uso do Banco  \u0010 Cip \u0010 Carteira     \u0010 Especie Moeda \u0010 Quantidade  \u0010  Valor             \u0010 1(=) Valor do Documento",
					"       #usob  \u0010 #cip\u0010 #cart        \u0010       #espmo  \u0010    #qtd \u0010       #valor \u0010                             #docvalor",
					"______________\u0004_____\u0004______________\u0004_______________\u0004_____________\u0004____________________\u0004_____________________________________",
					"                                                                                      \u0010 2(-) Desc./Abatimento       #descabat",
					" #1instrucoes   \u0004_____________________________________",
					" #2instrucoes   \u0010 3(-) Outras Deducoes        #outrasded",
					" #3instrucoes   \u0004_____________________________________",
					" #4instrucoes   \u0010 4(+) Mora/Multa             #moramulta",
					" #5instrucoes   \u0004_____________________________________",
					" #6instrucoes   \u0010 5(+) Outros Acrescimos      #outrosacr",
					" #7instrucoes   \u0004_____________________________________",
					"                                                                                      \u0010 6(=) Valor Cobrado          #valcobrado",
					"______________________________________________________________________________________\u0004_____________________________________",
					" Sacado   #sacado  CNPJ  #cnpj",
					"          #rua                        ISO 9001",
					"          #cep                             #estado",
					" Sacador/avalista:",
					"____________________________________________________________________________________________________________________________",
					"####################################################################################  Autentificacao   Ficha de Compensacao",
					"####################################################################################",
					"####################################################################################",
					"####################################################################################",
					"####################################################################################",
					"####################################################################################" };

		}

		public final String[] tags() {

			return new String[] { "#nomebanco", "#codba", "#linhadig",
					"#localpag", "#opcaopag", "#venc", "#cedente",
					"#agenciacodcedente", "#datadoc", "#numerodoc", "#espdoc",
					"#ace", "#datapro", "#nossonum", "#usob", "#cip", "#cart",
					"#espmo", "#qtd", "#valor", "#docvalor", "#descabat",
					"#1instrucoes", "#2instrucoes", "#outrasded",
					"#3instrucoes", "#4instrucoes", "#moramulta",
					"#5instrucoes", "#6instrucoes", "#outrosacr",
					"#7instrucoes", "#valcobrado", "#sacado", "#cnpj", "#rua",
					"#cep", "#estado" };

		}

		public final int[] sizesTags() {

			return new int[] { 16, 6, 54, 63, 63, 10, 84, 20, 10, 10, 7, 4, 10,
					17, 5, 4, 5, 6, 8, 12, 12, 9, 82, 82, 10, 82, 82, 12, 82,
					82, 12, 82, 12, 48, 20, 54, 9, 7 };

		}
	}

	public final static Boleto Boleto() {
		return new Boleto();

	}

	public final static class Boleto {

		public static String[] Template() {

			return new String[] {
					"                     \u0010             \u0010",
					"    #nomebanco \u0010   #codba    \u0010 #linhadig",
					"_____________________\u0004_____________\u0004________________________________________________________________________________________",
					"Local de Pagamento   #localpag  \u0010 Vencimento",
					"                     #opcaopag  \u0010                           #venc",
					"______________________________________________________________________________________\u0004_____________________________________",
					"Cedente                                                                               \u0010 Agencia/Codigo cedente",
					"#cedente  \u0010        #agenciacodcedente",
					" _____________________________________________________________________________________\u0004_____________________________________",
					"Data do Documento  \u0010 N  do Documento \u0010 Especie Doc.\u0010 Aceite  \u0010 Data do Processamento  \u0010 Cart./Nosso Numero",
					"       #datadoc  \u0010      #numerodoc \u0010  #espdoc    \u0010  #ace   \u0010             #datapro \u0010                    #nossonum",
					"___________________\u0004_________________\u0004_____________\u0004_________\u0004________________________\u0004_____________________________________",
					"Uso do Banco  \u0010 Cip \u0010 Carteira     \u0010 Especie Moeda \u0010 Quantidade  \u0010  Valor             \u0010 1(=) Valor do Documento",
					"       #usob  \u0010 #cip\u0010 #cart        \u0010       #espmo  \u0010    #qtd \u0010       #valor \u0010                             #docvalor",
					"______________\u0004_____\u0004______________\u0004_______________\u0004_____________\u0004____________________\u0004_____________________________________",
					"                                                                                      \u0010 2(-) Desc./Abatimento       #descabat",
					" #1instrucoes   \u0004_____________________________________",
					" #2instrucoes   \u0010 3(-) Outras Deducoes        #outrasded",
					" #3instrucoes   \u0004_____________________________________",
					" #4instrucoes   \u0010 4(+) Mora/Multa             #moramulta",
					" #5instrucoes   \u0004_____________________________________",
					" #6instrucoes   \u0010 5(+) Outros Acrescimos      #outrosacr",
					" #7instrucoes   \u0004_____________________________________",
					"                                                                                      \u0010 6(=) Valor Cobrado          #valcobrado",
					"______________________________________________________________________________________\u0004_____________________________________",
					" Sacado   #sacado  CNPJ  #cnpj",
					"          #rua                        ISO 9001",
					"          #cep                             #estado",
					" Sacador/avalista:",
					"____________________________________________________________________________________________________________________________",
					"####################################################################################  Autentificacao   Ficha de Compensacao",
					"####################################################################################",
					"####################################################################################",
					"####################################################################################",
					"####################################################################################",
					"####################################################################################" };

		}

		public final String[] tags() {

			return new String[] { "#nomebanco", "#codba", "#linhadig",
					"#localpag", "#opcaopag", "#venc", "#cedente",
					"#agenciacodcedente", "#datadoc", "#numerodoc", "#espdoc",
					"#ace", "#datapro", "#nossonum", "#usob", "#cip", "#cart",
					"#espmo", "#qtd", "#valor", "#docvalor", "#descabat",
					"#1instrucoes", "#2instrucoes", "#outrasded",
					"#3instrucoes", "#4instrucoes", "#moramulta",
					"#5instrucoes", "#6instrucoes", "#outrosacr",
					"#7instrucoes", "#valcobrado", "#sacado", "#cnpj", "#rua",
					"#cep", "#estado" };

		}

		public final int[] sizesTags() {

			return new int[] { 16, 6, 54, 63, 63, 10, 84, 20, 10, 10, 7, 4, 10,
					17, 5, 4, 5, 6, 8, 12, 12, 9, 82, 82, 10, 82, 82, 12, 82,
					82, 12, 82, 12, 48, 20, 54, 9, 7 };

		}
	}

}

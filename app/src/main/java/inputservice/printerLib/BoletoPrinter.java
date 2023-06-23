package inputservice.printerLib;

import java.util.ArrayList;

public class BoletoPrinter extends BMap {

	public BoletoPrinter() {

	}

	// Template padrao do boleto
	// contendo suas tags e caracteres de moldura

	public static String[] template = {
			"                     \u0010             \u0010",
			"    #nomebanco \u0010   #codba    \u0010 #linhadig",
			"_____________________\u0004_____________\u0004________________________________________________________________________________________",
			"Local de Pagamento   #localpag  \u0010 Vencimento",
			"                     #opcaopag  \u0010                           #venc",
			"______________________________________________________________________________________\u0004_____________________________________",
			"Cedente                                                                               \u0010 Agencia/Codigo cedente",
			"#cedente  \u0010                             #agenciacodcedente",
			" ___________________________________________________0__________________________________\u0004_____________________________________",
			"Data do Documento  \u0010 N  do Documento \u0010 Especie Doc.\u0010 Aceite  \u0010 Data do Processamento  \u0010 Cart./Nosso Numero",
			"       #datadoc  \u0010      #numerodoc \u0010  #espdoc    \u0010  #ace   \u0010            #datapro \u0010                           #nossonum",
			"___________________\u0004_________________\u0004_____________\u0004_________\u0004________________________\u0004_____________________________________",
			"Uso do Banco  \u0010 Cip \u0010 Carteira     \u0010 Especie Moeda \u0010 Quantidade  \u0010  Valor             \u0010 1(=) Valor do Documento",
			"       #usob  \u0010 #cip\u0010 #cart        \u0010       #espmo  \u0010    #qtd \u0010       #valor \u0010                             #docvalor",
			"______________\u0004_____\u0004______________\u0004_______________\u0004_____________\u0004____________________\u0004_____________________________________",
			"                                                                                      \u0010 2(-) Desc./Abatimento    #descabat",
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

	// tags que serão encontradas no boleto
	public static String[] tagsTemplate = { "#nomebanco", "#codba",
			"#linhadig", "#localpag", "#opcaopag", "#venc", "#cedente",
			"#agenciacodcedente", "#datadoc", "#numerodoc", "#espdoc", "#ace",
			"#datapro", "#nossonum", "#usob", "#cip", "#cart", "#espmo",
			"#qtd", "#valor", "#docvalor", "#descabat", "#1instrucoes",
			"#2instrucoes", "#outrasded", "#3instrucoes", "#4instrucoes",
			"#moramulta", "#5instrucoes", "#6instrucoes", "#outrosacr",
			"#7instrucoes", "#valcobrado", "#sacado", "#cnpj", "#rua", "#cep",
			"#estado" };

	// tamanho de cada tag
	public static int[] sizesTag = { 16, 6, 54, 63, 63, 10, 84, 20, 10, 10, 7,
			4, 10, 17, 5, 4, 5, 6, 8, 12, 12, 9, 82, 82, 10, 82, 82, 12, 82,
			82, 12, 82, 12, 48, 20, 54, 9, 7 };

	public int tagposition;

	/*
	 * dado um template, as tags e os valores (variáveis) esta função substitui
	 * os valores pelas tags
	 */
	public String[] fillTemplate(String[] sourceRemplate, String[] tags, int[] sizes,
			BoletoUtils boleto) {

		tagposition = 0;
		// linha indice 1
		sourceRemplate[1] = subsTag(sourceRemplate[1], boleto.getNomeBanco(), tagposition++);
		sourceRemplate[1] = subsTag(sourceRemplate[1], boleto.getCodBanco(), tagposition++);
		sourceRemplate[1] = subsTag(sourceRemplate[1], boleto.getLinhaDigitavel(),
				tagposition++);
		// linha indice 3
		sourceRemplate[3] = subsTag(sourceRemplate[3], boleto.getLocalPagamento(),
				tagposition++);
		// linha indice 4
		sourceRemplate[4] = subsTag(sourceRemplate[4], boleto.getLocalOpcionalPagamento(),
				tagposition++);
		sourceRemplate[4] = subsTag(sourceRemplate[4], boleto.getVencimento(),
				tagposition++);
		// linha indice 7
		sourceRemplate[7] = subsTag(sourceRemplate[7], boleto.getCedente(), tagposition++);
		sourceRemplate[7] = subsTag(sourceRemplate[7], boleto.getAgenciaCodigoCedente(), tagposition++); // getAutentificacao()
		// linha indice 10
		sourceRemplate[10] = subsTag(sourceRemplate[10], boleto.getDatadocumento(),
				tagposition++);
		sourceRemplate[10] = subsTag(sourceRemplate[10], boleto.getNumeroDocumento(),
				tagposition++);
		sourceRemplate[10] = subsTag(sourceRemplate[10], boleto.getEspecieDoc(),
				tagposition++);
		sourceRemplate[10] = subsTag(sourceRemplate[10], boleto.getAceite(), tagposition++);
		sourceRemplate[10] = subsTag(sourceRemplate[10], boleto.getDataProcessameto(),
				tagposition++);
		sourceRemplate[10] = subsTag(sourceRemplate[10], boleto.getNossoNumero(),
				tagposition++);
		// linha indice 13
		sourceRemplate[13] = subsTag(sourceRemplate[13], boleto.getUsoDoBanco(),
				tagposition++);
		sourceRemplate[13] = subsTag(sourceRemplate[13], boleto.getCip(), tagposition++);
		sourceRemplate[13] = subsTag(sourceRemplate[13], boleto.getCarteira(),
				tagposition++);
		sourceRemplate[13] = subsTag(sourceRemplate[13], boleto.getEspecieMoeda(),
				tagposition++);
		sourceRemplate[13] = subsTag(sourceRemplate[13], boleto.getQuantidade(),
				tagposition++);
		sourceRemplate[13] = subsTag(sourceRemplate[13], boleto.getValor(), tagposition++);
		sourceRemplate[13] = subsTag(sourceRemplate[13], boleto.getValorDocumento(),
				tagposition++);
		// linha indice 15
		sourceRemplate[15] = subsTag(sourceRemplate[15], boleto.getDesconto(),
				tagposition++);
		// linha indice 16
		sourceRemplate[16] = subsTag(sourceRemplate[16], boleto.getInstrucoesCedente()[0],
				tagposition++);
		// linha indice 17
		sourceRemplate[17] = subsTag(sourceRemplate[17], boleto.getInstrucoesCedente()[1],
				tagposition++);
		sourceRemplate[17] = subsTag(sourceRemplate[17], boleto.getDeducoes(),
				tagposition++);
		// linha indice 18
		sourceRemplate[18] = subsTag(sourceRemplate[18], boleto.getInstrucoesCedente()[2],
				tagposition++);
		// linha indice 19
		sourceRemplate[19] = subsTag(sourceRemplate[19], boleto.getInstrucoesCedente()[3],
				tagposition++);
		sourceRemplate[19] = subsTag(sourceRemplate[19], boleto.getMulta(), tagposition++);
		// linha indice 20
		sourceRemplate[20] = subsTag(sourceRemplate[20], boleto.getInstrucoesCedente()[4],
				tagposition++);
		// linha indice 21
		sourceRemplate[21] = subsTag(sourceRemplate[21], boleto.getInstrucoesCedente()[5],
				tagposition++);
		sourceRemplate[21] = subsTag(sourceRemplate[21], boleto.getAcrescimos(),
				tagposition++);
		// linha indice 22
		sourceRemplate[22] = subsTag(sourceRemplate[22], boleto.getInstrucoesCedente()[6],
				tagposition++);
		// linha indice 23
		sourceRemplate[23] = subsTag(sourceRemplate[23], boleto.getValorCobrado(),
				tagposition++);
		// linha indice 25
		sourceRemplate[25] = subsTag(sourceRemplate[25], boleto.getSacadoNome(),
				tagposition++);
		sourceRemplate[25] = subsTag(sourceRemplate[25], boleto.getSacadoCnpj(),
				tagposition++);
		// linha indice 26
		sourceRemplate[26] = subsTag(sourceRemplate[26], boleto.getSacadoEndereco(),
				tagposition++);
		// linha indice 27
		sourceRemplate[27] = subsTag(sourceRemplate[27], boleto.getSacadoCep(),
				tagposition++);
		sourceRemplate[27] = subsTag(sourceRemplate[27], boleto.getSacadoUF(),
				tagposition++);

		return sourceRemplate;
	}

	/*
	 * dado um template, as tags e os valores (variáveis) esta função substitui
	 * os valores pelas tags
	 */
	public String subsTag(String line, String data, int postag) {
		String newLine = line;

		if (data.length() <= sizesTag[postag]) {
			newLine = newLine.replace(tagsTemplate[postag], fillNumSpaces(data, sizesTag[postag] - data.length()));
		} else {
			newLine = newLine.replace(tagsTemplate[postag], data.substring(0, sizesTag[postag]));
		}

		return newLine;
	}

	/* pega objeto a partir de caracter correspondente */
	private static byte[][] getPos(char l) {
		if (l != ' ') {
			int pos = 0;
			for (int i = 0; i < alphaC.length; i++) {
				if (alphaC[i] == l) {
					pos = i;
				}
			}
			if (pos > 0) {
				return alpha[pos];
			}
		}
		return alpha[0];
	}

	public static ArrayList<String> fillTop(ArrayList<String> text) {
		text.add(
				0,
				'\u001B' + "\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A2");
		return text;
	}

	public ArrayList<String> fillContent(ArrayList<String> text, String[] textInv) {
		// text.add('\u0004'+"\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0007");
		for (int i = 0; i < textInv.length; i++) {
			if (textInv[i].length() < 36) {// 34
				textInv[i] = fillNumSpaces(textInv[i], 36 - textInv[i].length());
				System.out.println("linha" + textInv[i] + "length" + textInv[i].length());
			}
			// text.add('_'+textInv[i]+'\u0008');
			text.add(textInv[i]);
		}
		// text.add('\u0006'+"\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0009");
		return text;
	}

	/*
	 * fill content with spaces after, and add border public static
	 * ArrayList<String> fillContent(ArrayList<String> text,String[] textInv){
	 * text.add('\u0004'+
	 * "\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0007"
	 * ); for(int i=0;i<textInv.length;i++){ if(textInv[i].length()<34){//34
	 * textInv[i] = fillNumSpaces(textInv[i], 34-textInv[i].length()); }
	 * text.add('_'+textInv[i]+'\u0008'); } text.add('\u0006'+
	 * "\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0009"
	 * ); return text; }
	 */

	public static ArrayList<String> fillContentSpacesBefore(
			ArrayList<String> text, String[] textInv) {
		text.add('\u0004' + "\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0007");
		for (int i = 0; i < textInv.length; i++) {
			if (textInv[i].length() < 34) {
				textInv[i] = fillNumSpacesBefore(textInv[i],
						34 - textInv[i].length());
			}
			text.add('_' + textInv[i] + '\u0008');
		}
		text.add('\u0006' + "\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0009");
		return text;
	}

	public static ArrayList<String> fillBottom(ArrayList<String> text) {
		text.add('\u00A1' + "\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A6\u00A4");
		return text;
	}

	public static String[] fillStringByArray(ArrayList<String> array) {
		String[] str = new String[array.size()];
		for (int i = 0; i < array.size(); i++) {
			str[i] = array.get(i);
			System.out.println(str[i]);
		}
		return str;
	}

	public static String[] textRotate(String[] text) {
		int biggerLine = 0;
		int sizeText = text.length;

		biggerLine = biggerLine(text);

		String[] textInvert = new String[biggerLine];

		String word = "";

		for (int i = 0; i < biggerLine; i++) {
			for (int j = 0; j < sizeText; j++) {
				if (i >= text[j].length()) {
					word += " ";
				} else {
					word += text[j].substring(i, i + 1);
				}

			}
			textInvert[i] = word;
			word = "";

		}
		return textInvert;
	}

	public String[] textInvert(String[] textInv) {
		String[] text = textInv.clone();
		String[] newTextInv = new String[text.length];
		int count = 0;
		for (int i = newTextInv.length - 1; i >= 0; i--) {
			newTextInv[count++] = text[i];
		}
		return newTextInv;
	}

	public static int biggerLine(String[] text) {
		int sizeText = text.length;
		int biggerLine = 0;

		if (sizeText != 0) {
			for (int i = 0; i < sizeText; i++) {
				if (biggerLine == 0) {
					biggerLine = text[i].length();
				} else {
					if (biggerLine < text[i].length()) {
						biggerLine = text[i].length();
					}

				}
			}

		}
		return biggerLine;
	}

	private static String fillNumSpaces(String data, int numSpaces) {
		int times = 0;
		if (numSpaces > 10) {
			times = numSpaces / 10;
			for (int i = 0; i < times; i++) {
				data += spaces[10];
			}
			data += spaces[numSpaces % 10];
		} else {
			data += spaces[numSpaces];
		}
		return data;
	}

	private static String fillNumSpacesBefore(String data, int numSpaces) {
		int times = 0;
		String tmp = data;
		data = new String();
		if (numSpaces > 10) {
			times = numSpaces / 10;
			for (int i = 0; i < times; i++) {
				data += spaces[10];
			}
			data += spaces[numSpaces % 10];
		} else {
			data += spaces[numSpaces];
		}
		data += tmp;
		return data;
	}

	public void printStringLine(String text) {
		byte[] line = new byte[4000];
		// boleto.connect("L42","00:0A:3A:31:AB:6C",false);
		// boleto.connect(true);
		line[0] = 0x1D;
		line[1] = '*';
		line[2] = 72;// x=2 ( Width 460 dots)//60
		line[3] = 2;// y=3 ( Height 16 dots)

		int pos;
		int posCharLine = 0;
		char[] arrayCharLine = text.toCharArray();
		pos = 4;// 4
		for (int i = 0; i < 36; i++) {
			byte[][] letra = getPos(posCharLine < text.length() ? arrayCharLine[posCharLine++]
					: ' ');
			// System.out.println("text lenght" + text.length());
			for (int j = 0; j < 16; j++) {
				line[pos] = letra[j][0];
				pos += 1;
				line[pos] = letra[j][1];
				pos += 1;
				// System.out.println("byte letra" + letra[j][0] + letra[j][1]);

			}

		}
		// mp.SendBuffer(line, pos); // =(x*8*2)+4 ou =(x*16)+4 //1156
		line[pos++] = 0x1D;// 0
		line[pos++] = 0x2F;// 1
		line[pos++] = 0x00;// 2
		mobileprint.SendBuffer(line, pos);// print image //3
	}

	/*
	 * prints String line and substitute '#' for letter that contains barcode
	 * data
	 */
	public void printStringLineBarcode(String text, byte[][] barletter) {
		byte[] line = new byte[4000];
		// boleto.connect("L42","00:0A:3A:31:AB:6C",false);
		// boleto.connect(true);

		line[0] = 0x1D;
		line[1] = '*';
		line[2] = 72;// x=2 ( Width 460 dots)//60
		line[3] = 2;// y=3 ( Height 16 dots)

		int pos;
		int posCharLine = 0;
		char[] arrayCharLine = text.toCharArray();
		pos = 4;// 4
		for (int i = 0; i < 36; i++) {
			byte[][] letra;
			// System.out.println("text" +text + text.substring(posCharLine,
			// posCharLine+1));
			if (text.substring(posCharLine, posCharLine + 1).equals("#")) {
				letra = barletter;
				posCharLine++;
				// System.out.println("byte letra" + letra[0][0] + letra[0][1]);
			} else {
				letra = getPos(posCharLine < text.length() ? arrayCharLine[posCharLine++]
						: ' ');
				// System.out.println("byte letra original" + letra[0][0] +
				// letra[0][1]);
			}
			// System.out.println("text lenght" + text.length());
			for (int j = 0; j < 16; j++) {
				line[pos] = letra[j][0];
				pos += 1;
				line[pos] = letra[j][1];
				pos += 1;
				// System.out.println("byte letra" + letra[j][0] + letra[j][1]);
			}

		}
		// mp.SendBuffer(line, pos); // =(x*8*2)+4 ou =(x*16)+4 //1156
		line[pos++] = 0x1D;// 0
		line[pos++] = 0x2F;// 1
		line[pos++] = 0x00;// 2
		mobileprint.SendBuffer(line, pos);// print image //3
	}

	public void printStringText(String[] text) {
		for (int i = 0; i < text.length; i++) {
			mobileprint.Reset();
			printStringLine(text[i]);
		}
	}

	public void printStringTextPlusBarcode(String[] text, byte[][][] barcode) {
		int pos = 0;
		for (int i = 0; i < text.length; i++) {
			mobileprint.Reset();

			// try {
			// mobileprint.SendString("/n/n/n TESTE /n/n/n");
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			if (pos < barcode.length && i > 0) {
				// System.out.println("barlength"+barcode.length);
				printStringLineBarcode(text[i], barcode[pos++]);
				// System.out.println("tamanho" +barcode.length);
				// System.out.println("byte letra" + barcode[i][0][0] +
				// barcode[i][0][1]);
			} else
				printStringLine(text[i]);

		}

	}

	public void printArrayListText(ArrayList<String> text) {
		for (int i = 0; i < text.size(); i++) {
			mobileprint.Reset();
			printStringLine(text.get(i));
		}
	}

	public void printBoleto(String[] template, String[] tags, int[] sizes,
			BoletoUtils boleto) {
		String textInv[];

		fillTemplate(template, tags, sizes, boleto);
		textInv = textInvert(template);
		textInv = textRotate(textInv);
		ArrayList<String> arrayText = new ArrayList<String>();
		arrayText = fillContent(arrayText, textInv);
		textInv = fillStringByArray(arrayText);
		boleto.genNumeroCodBarras();
		printStringTextPlusBarcode(textInv,
				Interleave2of5.genBarCode(boleto.getNumeroCodBarras()));

	}

	public void printBoleto(BoletoUtils boleto) {
		String[] textInv = fillTemplate(BoletoPrinter.template.clone(), BoletoPrinter.tagsTemplate.clone(), BoletoPrinter.sizesTag.clone(), boleto);
		textInv = textInvert(textInv);
		textInv = textRotate(textInv);
		ArrayList<String> arrayText = new ArrayList<String>();
		arrayText = fillContent(arrayText, textInv);
		textInv = fillStringByArray(arrayText);
		boleto.genNumeroCodBarras();
		printStringTextPlusBarcode(textInv, Interleave2of5.genBarCode(boleto.getNumeroCodBarras()));
		// printStringLine("");
	}

	/*
	 * Sugestoes de melhorias
	 * 
	 * Criar uma classe separada para o template afim melhorar o isolamento e
	 * manutenção dos templates;
	 * 
	 * Criar uma classe separada para metodos como printStringText, fillContent,
	 * substags, entre outros, com o objetivo de aumentar a reutilização e
	 * manutenção do código;
	 */

}

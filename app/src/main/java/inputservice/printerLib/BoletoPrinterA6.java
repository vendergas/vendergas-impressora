package inputservice.printerLib;

import java.util.ArrayList;

public class BoletoPrinterA6 extends BMap {
	
	public BoletoPrinterA6(){
		
	}
	/**/
	public static String[] template = {
		"	                         \u0010               \u0010",                                                                                                
		"                         \u0010               \u0010",                                                                                                
		"#nomeBanco          \u0010      #codBa    \u0010        #linhaDig",                                  
		"________________________\u0004_____________\u0004______________________________________________________________________________________________", 
		"                                                                                                       \u0010",                                  
		"Local de pagamento    #localPag                           \u0010  VENCIMENTO",                      
		"                      #opcaoPag                           \u0010                      #venc     ",  
		"______________________________________________________________________________________________________\u0004________________________________",
		"Cedente                                                                                                \u0010  Agencia / Codigo Cedente",        
		"#cedente                            \u0010             #agenciaCodCedente",   
		"______________________________________________________________________________________________________\u0004________________________________", 
		"                           \u0010                           \u0010              \u0010        \u0010                       \u0010",                                  
		"Data do Documento          \u0010 N do Documento            \u0010 Especie Doc. \u0010 Aceite \u0010 Data do Processamento \u0010 Cart. / Nosso Numero",             
		"               #dataDoc          \u0010            #numeroDoc         \u0010    #espDoc        \u0010   #ace \u0010              #dataPro       \u0010               #nossoNum",  
		"__________________________\u0004_________________________\u0004____________\u0004______\u0004_____________________\u0004________________________________", 
		"             \u0010             \u0010           \u0010               \u0010                       \u0010                       \u0010",                                    
		"Uso do Banco \u0010 CIP         \u0010  Carteira \u0010 Especie Moeda \u0010 Quantidade            \u0010 Valor                 \u0010 1(=) Valor do Documento",          
		"     #usoB       \u0010  #cip       \u0010   #cart   \u0010      #espMo     \u0010    #qtd           \u0010   #valor   \u0010                        #docValor",  
		"____________\u0004___________\u0004_________\u0004_____________\u0004_____________________\u0004_____________________\u0004________________________________", 
		"                                          \u0010 2(-) Desconto/Abatimento",         
		"#1instrucao                               \u0010             #descontos",                                  
		"#2instrucao                                \u0010_________________________________", 
		"#3instrucao                                \u0010 3(-) Outras Deduçoes",             
		"#4instrucao                                \u0010            #deducoes",                                  
		"#5instrucao                                             \u0010_________________________________", 
		"#6instrucao                                \u0010 4(-) Mora / Multa",                
		"#7instrucao                                \u0010            #multas",                                  
		"#8instrucao                                             \u0010_________________________________", 
		"#9instrucao                                \u0010 5(+) Outros Acrecimos",            
		"#10instrucao                                 \u0010            #acrescimos",                                  
		"#11instrucao                                              \u0010_________________________________", 
		"#12instrucao                               \u0010 6(+) Valor Cobrado",               
		"#13instrucao                               \u0010            #valCobrado",                                  
		"______________________________________________________________________________________________________\u0004________________________________", 
		"  ",                                                                                                                                        
		"Sacado            #sacado             #cnpj",                                        
		"                  #endereco",                                                                                       
		"                  #cep              #cidade          #uf",                                                                                               
		"Sacador / Avalista",                                                                                                                        
		"_________________________________________________________________________________________________________________________________________", 
		"               ",                                                                                                                           
		"###################################################################################    Autentificaçao                Ficha de compensaçao",  
		"###################################################################################    ",                                                   
		"###################################################################################    ",                                                    
		"###################################################################################    ",                                                    
		"###################################################################################    ",                                                    
		"###################################################################################    ",                                                     
		"###################################################################################    ",                                                    
		"###################################################################################    ",							                         								                         
	};
	public static String[] tagsTemplate = {"#nomebanco","#codbanc","#linhadig","#localpag","#opcaopag","#venc","#cedente","#agenciacodcedente","#datadoc","#numerodoc",
											"#espdoc","#ace","#datapro","#nossonum","#usob","#cip","#cart","#espMo","#qtd","#valor",
											"#docValor","#1instrucao","#desconto","#2instrucao","#outrasded","#3instrucoes","#4instrucoes","#moramulta","#5instrucoes","#6instrucoes",
											"#outrosacr","#7instrucoes","#valcobrado","#sacado","#cnpj","#rua","#cep","#estado"};
	public static int[] sizesTag = {16,6,54,63,63,10,84,20,10,10,
									  7,4,10,17,5,4,5,6,8,12,
									  12,82,9,82,10,82,82,12,82,82,
									  12,82,12,48,20,54,9, 7};
	
	/*
	public static String[] tagsTemplate = {"#nomeBanco","#codBanc","#linhaDigi","#localPag","#opcaoPag","#venc","#cedente","#codAgenciaCedente","#dataDoc","#numDoc","#espDoc","#ace","#dataPro","#nossoNum", "#uso","#cip","#cart","#espMo","#qtd","#valor","#docValor","#instrucao","#2instrucao","#3instrucao","#4instrucao","#5instrucao","#6instrucao","#7instrucao","8instrucao","9instrucao","instrucao10","instrucao11","instrucao12","#descontos", "#deducoes", "#multas","#acrescimos","#valcobrado","#sacado","#cnpj","#endereco","#cep","#cidade","#uf"};
	public static int[] sizesTag = {16,7,54,63,63,10,84,20,10,10,
									7,4,10,17,5,4,5,6,8,12,
									12,82,82,82,82,82,82,82,82,82,
									82,82,82,12,12,12,12,48,50,12,
									54,10,20,3 };*/
	public int tagposition;
	
	/*dado um template, as tags e os valores (variáveis) esta função substitui os valores pelas tags*/
    public String[] fillTemplate(String[] template,String[] tags,int[] sizes,BoletoUtils boleto ){
    	tagposition = 0;
    	//linha indice 1
    	template[1] = subsTag(template[1],boleto.getNomeBanco(),tagposition++);
    	template[1] = subsTag(template[1],boleto.getCodBanco(),tagposition++);
    	template[1] = subsTag(template[1],boleto.getLinhaDigitavel(),tagposition++);
    	//linha indice 3
    	template[3] = subsTag(template[3],boleto.getLocalPagamento(),tagposition++);
    	//linha indice 4
    	template[4] = subsTag(template[4],boleto.getLocalOpcionalPagamento(),tagposition++);
    	template[4] = subsTag(template[4],boleto.getVencimento(),tagposition++);
    	//linha indice 7
    	template[7] = subsTag(template[7],boleto.getCedente(),tagposition++);
    	template[7] = subsTag(template[7],boleto.getAutentificacao(),tagposition++);
    	//linha indice 10
    	template[10] = subsTag(template[10],boleto.getDatadocumento(),tagposition++);
    	template[10] = subsTag(template[10],boleto.getNumeroDocumento(),tagposition++);
    	template[10] = subsTag(template[10],boleto.getEspecieDoc(),tagposition++);
    	template[10] = subsTag(template[10],boleto.getAceite(),tagposition++);
    	template[10] = subsTag(template[10],boleto.getDataProcessameto(),tagposition++);
    	template[10] = subsTag(template[10],boleto.getNossoNumero(),tagposition++);
    	//linha indice 13
    	template[13] = subsTag(template[13],boleto.getUsoDoBanco(),tagposition++);
    	template[13] = subsTag(template[13],boleto.getCip(),tagposition++);
    	template[13] = subsTag(template[13],boleto.getCarteira(),tagposition++);
    	template[13] = subsTag(template[13],boleto.getEspecieMoeda(),tagposition++);
    	template[13] = subsTag(template[13],boleto.getQuantidade(),tagposition++);
    	template[13] = subsTag(template[13],boleto.getValor(),tagposition++);
    	template[13] = subsTag(template[13],boleto.getValorDocumento(),tagposition++);
    	//linha indice 15
    	template[15] = subsTag(template[15],boleto.getDesconto(),tagposition++);
    	//linha indice 16
    	template[16] = subsTag(template[16],boleto.getInstrucoesCedente()[0],tagposition++);
    	//linha indice 17
    	template[17] = subsTag(template[17],boleto.getInstrucoesCedente()[1],tagposition++);
    	template[17] = subsTag(template[17],boleto.getDeducoes(),tagposition++);
    	//linha indice 18
    	template[18] = subsTag(template[18],boleto.getInstrucoesCedente()[2],tagposition++);
    	//linha indice 19
    	template[19] = subsTag(template[19],boleto.getInstrucoesCedente()[3],tagposition++);
    	template[19] = subsTag(template[19],boleto.getMulta(),tagposition++);
    	//linha indice 20
    	template[20] = subsTag(template[20],boleto.getInstrucoesCedente()[4],tagposition++);
    	//linha indice 21
    	template[21] = subsTag(template[21],boleto.getInstrucoesCedente()[5],tagposition++);
    	template[21] = subsTag(template[21],boleto.getAcrescimos(),tagposition++);
    	//linha indice 22
    	template[22] = subsTag(template[22],boleto.getInstrucoesCedente()[6],tagposition++);
    	//linha indice 23
    	template[23] = subsTag(template[23],boleto.getValorCobrado(),tagposition++);
    	//linha indice 25
    	template[25] = subsTag(template[25],boleto.getSacadoNome(),tagposition++);
    	template[25] = subsTag(template[25],boleto.getSacadoCnpj(),tagposition++);
    	//linha indice 26
    	template[26] = subsTag(template[26],boleto.getSacadoEndereco(),tagposition++);
    	//linha indice 27
    	template[27] = subsTag(template[27],boleto.getSacadoCep(),tagposition++);
    	template[27] = subsTag(template[27],boleto.getSacadoUF(),tagposition++);
    	
    	return template;
    }
    
    /*dado um template, as tags e os valores (variáveis) esta função substitui os valores pelas tags*/
    public String subsTag(String line,String data,int postag){
    	
    	if(data.length() <= sizesTag[postag])
    		line = line.replace(tagsTemplate[postag],fillNumSpaces(data, sizesTag[postag]-data.length()));
    	else
    		line = line.replace(tagsTemplate[postag],data.substring(0,sizesTag[postag]));

    	return line;
    }
      
	
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
		text.add(0,'\u001B' + "\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A5\u00A2");
		return text;
	}

	public ArrayList<String> fillContent(ArrayList<String> text,
			String[] textInv) {
		// text.add('\u0004'+"\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0007");
		for (int i = 0; i < textInv.length; i++) {
			if (textInv[i].length() < 52) {// 34
				textInv[i] = fillNumSpaces(textInv[i], 52 - textInv[i].length());
				System.out.println("linha" + textInv[i] + "length"
						+ textInv[i].length());
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
		String[] text = textInv;
		textInv = new String[text.length];
		int count = 0;
		for (int i = textInv.length - 1; i >= 0; i--) {
			textInv[count++] = text[i];
		}
		return textInv;
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
		byte[] line = new byte[10000];
		// boleto.connect("L42","00:0A:3A:31:AB:6C",false);
		// boleto.connect(true);
		line[0] = 0x1D;
		line[1] = '*';
		line[2] = 104;// 72 para 36letras
		line[3] = 2;// y=3 ( Height 16 dots)

		int pos;
		int posCharLine = 0;
		char[] arrayCharLine = text.toCharArray();
		pos = 4;// 4
		for (int i = 0; i < 52; i++) {
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

	public void printStringLineSmaller(String text) {
		byte[] line = new byte[10000];
		byte[] line2 = new byte[10000];
		// boleto.connect("L42","00:0A:3A:31:AB:6C",false);
		// boleto.connect(true);
		line[0] = line2[0] = 0x1D;
		line[1] = line2[1] = '*';
		line[2] = line2[2] = 104;// 72 para 36letras
		line[3] = line2[3] = 1;// y=3 ( Height 16 dots)

		int pos, pos2;
		int posCharLine = 0;
		char[] arrayCharLine = text.toCharArray();
		pos = pos2 = 4;// 4
		for (int i = 0; i < 52; i++) {
			byte[][] letra = getPos(posCharLine < text.length() ? arrayCharLine[posCharLine++]
					: ' ');
			// System.out.println("text lenght" + text.length());
			for (int j = 0; j < 16; j++) {
				line[pos++] = letra[j][0];
				line2[pos2++] = letra[j][1];
				// System.out.println("byte letra" + letra[j][0] + letra[j][1]);

			}

		}
		// mp.SendBuffer(line, pos); // =(x*8*2)+4 ou =(x*16)+4 //1156
		line[pos++] = line2[pos2++] = 0x1D;// 0
		line[pos++] = line2[pos2++] = 0x2F;// 1
		line[pos++] = line2[pos2++] = 0x00;// 2
		mobileprint.SendBuffer(line, pos);// print image //3
		mobileprint.Reset();
		mobileprint.SendBuffer(line2, pos2);// print image //3
	}
	
	/*
	 * prints String line and substitute '#' for letter that contains barcode
	 * data
	 */
	public void printStringLineBarcode(String text,
			byte[][] barletter) {
		byte[] line = new byte[10000];
		// boleto.connect("L42","00:0A:3A:31:AB:6C",false);
		// boleto.connect(true);

		line[0] = 0x1D;
		line[1] = '*';
		line[2] = 104;// x=2 ( Width 460 dots)//60
		line[3] = 2;// y=3 ( Height 16 dots)

		int pos;
		int posCharLine = 0;
		char[] arrayCharLine = text.toCharArray();
		pos = 4;// 4
		for (int i = 0; i < 52; i++) {
			byte[][] letra;
			// System.out.println("text" +text + text.substring(posCharLine,
			// posCharLine+1));
			//System.out.println("substring"+text + "len"+text.length());
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
	
	/*
	 * prints String line and substitute '#' for letter that contains barcode
	 * data, it prints smaller, prints only 1 of heigth
	 */
	public void printStringLineBarcodeSmaller(String text,byte[][] barletter) {
		byte[] line = new byte[10000];
		byte[] line2 = new byte[10000];
		// boleto.connect("L42","00:0A:3A:31:AB:6C",false);
		// boleto.connect(true);
		line[0] = line2[0] = 0x1D;
		line[1] = line2[1] = '*';
		line[2] = line2[2] = 104;// 72 para 36letras
		line[3] = line2[3] = 1;// y=3 ( Height 16 dots)

		int pos,pos2;
		int posCharLine = 0;
		char[] arrayCharLine = text.toCharArray();
		pos = pos2 = 4;// 4
		for (int i = 0; i < 52; i++) {
			byte[][] letra;
			// System.out.println("text" +text + text.substring(posCharLine,
			// posCharLine+1));
			//System.out.println("substring"+text + "len"+text.length());
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
				line[pos++] = letra[j][0];
				line2[pos2++] = letra[j][1];
				// System.out.println("byte letra" + letra[j][0] + letra[j][1]);
			}

		}
		// mp.SendBuffer(line, pos); // =(x*8*2)+4 ou =(x*16)+4 //1156
		line[pos++] = line2[pos2++] = 0x1D;// 0
		line[pos++] = line2[pos2++] = 0x2F;// 1
		line[pos++] = line2[pos2++] = 0x00;// 2
		mobileprint.SendBuffer(line, pos);// print image //3
		mobileprint.Reset();
		mobileprint.SendBuffer(line2, pos2);// print image //3
	}

	public void printStringText(String[] text) {
		for (int i = 0; i < text.length; i++) {
			mobileprint.Reset();
			printStringLine(text[i]);
		}
	}
	
	public void printStringTextSmaller(String[] text) {
		for (int i = 0; i < text.length; i++) {
			mobileprint.Reset();
			printStringLineSmaller(text[i]);
		}
	}

	public void printStringTextPlusBarcode(String[] text, byte[][][] barcode) {
		int pos = 0;
		for (int i = 0; i < text.length; i++) {
			mobileprint.Reset();
			
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
	
	public void printStringTextPlusBarcodeSmaller(String[] text, byte[][][] barcode) {
		int pos = 0;
		for (int i = 0; i < text.length; i++) {
			mobileprint.Reset();
			
			if (pos < barcode.length && i > 0) {
				// System.out.println("barlength"+barcode.length);
				printStringLineBarcodeSmaller(text[i], barcode[pos++]);
				// System.out.println("tamanho" +barcode.length);
				// System.out.println("byte letra" + barcode[i][0][0] +
				// barcode[i][0][1]);
			} else
				printStringLineSmaller(text[i]);

		}
	}

	public void printArrayListText(ArrayList<String> text) {
		for (int i = 0; i < text.size(); i++) {
			mobileprint.Reset();
			printStringLine(text.get(i));
		}
	}
	
	public void printArrayListTextSmaller(ArrayList<String> text) {
		for (int i = 0; i < text.size(); i++) {
			mobileprint.Reset();
			printStringLineSmaller(text.get(i));
		}
	}
	
	public void printBoleto(String[] template,String[] tags,int[] sizes,BoletoUtils boleto ){
		String textInv[];
		
		fillTemplate(template, tags, sizes, boleto);
		textInv = textInvert(template);
		textInv = textRotate(textInv);
		ArrayList<String> arrayText = new ArrayList<String>();
		arrayText = fillContent(arrayText, textInv);
		textInv = fillStringByArray(arrayText);	
		boleto.genNumeroCodBarras();
		printStringTextPlusBarcode(textInv,Interleave2of5.genBarCode(boleto.getNumeroCodBarras()));

	}
	//teste sem barcode
	public void printBoletoSmaller(BoletoUtils boleto ){
		String textInv[];
		
		fillTemplate(BoletoPrinterA6.template, BoletoPrinterA6.tagsTemplate, BoletoPrinterA6.sizesTag, boleto);
		textInv = textInvert(template);
		textInv = textRotate(textInv);
		ArrayList<String> arrayText = new ArrayList<String>();
		arrayText = fillContent(arrayText, textInv);
		textInv = fillStringByArray(arrayText);	
		boleto.genNumeroCodBarras();
		printStringTextPlusBarcodeSmaller(textInv,Interleave2of5.genBarCode(boleto.getNumeroCodBarras()));

	}
	
	public void printBoleto(BoletoUtils boleto ){
		String textInv[];
		
		fillTemplate(BoletoPrinterA6.template, BoletoPrinterA6.tagsTemplate, BoletoPrinterA6.sizesTag, boleto);
		textInv = textInvert(template);
		textInv = textRotate(textInv);
		ArrayList<String> arrayText = new ArrayList<String>();
		arrayText = fillContent(arrayText, textInv);
		textInv = fillStringByArray(arrayText);	
		boleto.genNumeroCodBarras();
		printStringTextPlusBarcode(textInv,Interleave2of5.genBarCode(boleto.getNumeroCodBarras()));

	}

	/*
	 * public static void printBitmap(mobilePrinter mp){ BufferedImage image =
	 * new BufferedImage(144, 32, BufferedImage.TYPE_INT_RGB); Graphics g =
	 * image.getGraphics(); g.setFont(new Font("Dialog", Font.PLAIN, 24));
	 * Graphics2D graphics = (Graphics2D) g;
	 * graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	 * RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	 * graphics.drawString("Hello World!", 6, 24); ImageIO.write(image, "png",
	 * new File("text.png"));
	 * 
	 * for (int y = 0; y < 32; y++) { StringBuilder sb = new StringBuilder();
	 * for (int x = 0; x < 144; x++) sb.append(image.getRGB(x, y) == -16777216 ?
	 * " " : image.getRGB(x, y) == -1 ? "#" : "*"); if
	 * (sb.toString().trim().isEmpty()) continue; System.out.println(sb); } }
	 */

}

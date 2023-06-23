package inputservice.printerLib;

import android.util.Log;
import rego.PrintLib.mobilePrinter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NfePrinterA7 extends Printer {
	/** arrays for code128 */
	public int[] ascii = { 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
			45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61,
			62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78,
			79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95,
			96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109,
			110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122,
			123, 124, 125, 126, 200, 201, 202, 203, 204, 205, 206, 207, 208,
			209, 210, 211 };// 107 positions
	public String[] binary = { "11011001100", "11001101100", "11001100110",
			"10010011000", "10010001100", "10001001100", "10011001000",
			"10011000100", "10001100100", "11001001000", "11001000100",
			"11000100100", "10110011100", "10011011100", "10011001110",
			"10111001100", "10011101100", "10011100110", "11001110010",
			"11001011100", "11001001110", "11011100100", "11001110100",
			"11101101110", "11101001100", "11100101100", "11100100110",
			"11101100100", "11100110100", "11100110010", "11011011000",
			"11011000110", "11000110110", "10100011000", "10001011000",
			"10001000110", "10110001000", "10001101000", "10001100010",
			"11010001000", "11000101000", "11000100010", "10110111000",
			"10110001110", "10001101110", "10111011000", "10111000110",
			"10001110110", "11101110110", "11010001110", "11000101110",
			"11011101000", "11011100010", "11011101110", "11101011000",
			"11101000110", "11100010110", "11101101000", "11101100010",
			"11100011010", "11101111010", "11001000010", "11110001010",
			"10100110000", "10100001100", "10010110000", "10010000110",
			"10000101100", "10000100110", "10110010000", "10110000100",
			"10011010000", "10011000010", "10000110100", "10000110010",
			"11000010010", "11001010000", "11110111010", "11000010100",
			"10001111010", "10100111100", "10010111100", "10010011110",
			"10111100100", "10011110100", "10011110010", "11110100100",
			"11110010100", "11110010010", "11011011110", "11011110110",
			"11110110110", "10101111000", "10100011110", "10001011110",
			"10111101000", "10111100010", "11110101000", "11110100010",
			"10111011110", "10111101110", "11101011110", "11110101110",
			"11010000100", "11010010000", "11010011100", "1100011101011" };
	public int arrayLineSize = 0;// tamanho do array usado para imprimir dados
									// no quadrado

	private static String hline = "-----------------------------------------------";

	public static final String[] templateHeader = { " Recebemos da",
			" #empresa", "                            NF-e: #nfe",
			"                            Serie: #serie",
			"                   Identificacao/Assinatura",
			"                                           ",
			" ___/___/_____     _________________________" };

	public static final String[] templateHeaderNFCE = { " Recebemos da",
			" #empresa", "                            NFc-e: #nfe",
			"                            Serie: #serie",
			"                   Identificacao/Assinatura",
			"                                           ",
			" ___/___/_____     _________________________" };

	public static final String[] templateHeader1 = { " Recebemos da",
			" #empresa", " #2empresa",
			"                                NF-e: #nfe",
			"                                Serie: #serie",
			"                   Identificacao/Assinatura",
			"                                           ",
			" ___/___/_____     _________________________" };

	public static final String[] templateSubHeader = {
			"         Danfe Simplificado          #tipo      ",
			"         Documento Auxiliar da       NF-e #nfe  ",
			"         Nota Fiscal Eletronica      Serie #ser ",
			"        										   " };

	public static final String[] templateNatOp = { " Nat. Op: #natop" };

	public static final String[] templateNatOp1 = { " Nat. Op: #natop",
			" #2natop" };

	public static final String[] templateAccessKey = { " CHAVE DE ACESSO",
			" #key" };

	public static final String[] templateEmitter = { " EMITENTE:", " #empresa",
			" #rua", " #cides", " CEP #cep TEL #tel", " CNPJ #cnpj",
			" IE #ie" };

	public static final String[] templateReceiver = {
			" DESTINATARIO:                    | EMISSAO  ",// 34//10
			" $#dest|#dtemi    ", " $#end|  SAIDA   ", " $#cides|#dtsaida  ",
			" CEP #cep00000 TEL $#tel|HORA SAIDA", " CNPJ/CPF $#cnpj|#horsaida",
			" IE $#ie|         " };

	public static final String[] templateProduct = {
			" Descricao | un | qtde | Valor   |  valor     ",
			" Produto   |    |      | unit    |  total     ",
			"___________|____|______|_________|____________",
			"#product00 | #u | #qtd | #valor0 | #vltot     ",
			"           |    |      |         |            ",
			"Total      |    |      |         | #tot       " };
	public static final String[] tagsHeader = { "#empresa", "#nfe", "#serie" };
	public static final String[] tagsHeader1 = { "#empresa", "#2empresa",
			"#nfe", "#serie" };
	public static final String[] tagsSubHeader = { "#tipo", "#nfe", "#ser" };
	public static final String[] tagsNatOp = { "#natop" };
	public static final String[] tagsNatOp1 = { "#natop", "#2natop" };
	public static final String[] tagsAccessKey = { "#key" };
	public static final String[] tagsEmitter = {
			"#empresa", "#rua", "#cides",
			"#cep", "#tel", "#cnpj", "#ie"
	};
	public static final String[] tagsReceiver = { "#dest", "#dtemi", "#end",
			"#cides", "#dtsaida", "#cep00000", "#tel", "#cnpj", "#horsaida",
			"#ie" };
	public static final String[] tagsProduct = { "#product00", "#u", "#qtd",
			"#valor0", "#vltot" };
	public static final int[] sizesProduct = { 10, 2, 4, 7, 10 };// numero de
																	// caracteres
																	// que cabem
																	// em cad
																	// tag/colula
	public static final String linePro = "#product00 | #u | #qtd | #valor0 | #vltot     ";
	public static final String lineBlank = "           |    |      |         |            ";
	public static final String[] spaces = new String[] { "", " ", "  ", "   ",
			"    ", "     ", "      ", "       ", "        ", "         ",
			"          " };
	public static final int[] sizeslastitem = new int[] { 33, 33, 33, 15, 28,
			30 };// for filltemplatediv

	public NfePrinterA7(mobilePrinter mp) {
		super(mp);
	}

	public NfePrinterA7() {
		super();
	}

	/* calculate verification digit for data */
	public String calcDV(String data) {
		int sum = 0, weight = 2, DV;
		for (int i = 42; i >= 0; i--) {
			sum += Integer.valueOf(data.subSequence(i, i + 1).toString())
					* weight;
			if (weight == 9) {
				weight = 2;
			} else {
				weight++;
			}
		}
		DV = 11 - (sum % 11);
		data += String.valueOf(DV);
		// Log.d("printer",data);
		return data;
	}

	/* calculate verification digit for code128 */
	public int calcDV128(String data) {
		int sum = 105, position = 0, weight = 1;
		for (int i = 0; i < 22; i++) {
			sum += Integer.valueOf(data.substring(position, position + 2))
					* weight++;
			position += 2;
		}
		// Log.d("printer","sum"+sum+" dv"+String.valueOf(sum%103));
		return sum % 103;
	}

	/* generate array with ascii values of each group of two characters */
	private int[] genArrayDataAscii(String data) {
		int[] dataAscii = new int[25];
		int positionData = 0;
		data = calcDV(data);
		dataAscii[0] = ascii[105];
		dataAscii[23] = ascii[calcDV128(data)];
		dataAscii[24] = ascii[106];
		// Log.d("printer",data.substring(positionData, positionData+2) + " " +
		// " " + String.valueOf(dataAscii[0]) + " " +
		// String.valueOf(dataAscii[23]) + " " + String.valueOf(dataAscii[24]));
		/** arrays for code128 */

		for (int i = 1; i < 23; i++) {
			dataAscii[i] = ascii[Integer.valueOf(data.substring(positionData,
					positionData + 2))];
			positionData += 2;
			// Log.d("printer","i:"+String.valueOf(i) + " data: " +
			// String.valueOf(dataAscii[i]) );
		}
		/*
		 * for(int i=0;i<25;i++){ Log.d("printer",String.valueOf(dataAscii[i]));
		 * }
		 */
		return dataAscii;
	}

	/*
	 * method that receives dataAscii array and generates the data hole mode for
	 * horizontal
	 */
	private String genDataBinHor(int[] dataAscii) {
		String[] dataBin11 = new String[25];// bits agrupados em 11
		String holeDataBin = "";

		/* converts to binary and put in dataBin11 */
		for (int i = 0; i < 25; i++) {
			int pos = Arrays.binarySearch(ascii, dataAscii[i]);
			dataBin11[i] = binary[pos];
			holeDataBin += dataBin11[i];
		}
		return holeDataBin;
	}

	public void genAccessKey(String key) {
		genAllByFields(NfePrinterA7.templateAccessKey,
				NfePrinterA7.tagsAccessKey, new String[] { calcDV(key) });
	}

	/*
	 * method that uses all previous methods to generate a barcode128 in
	 * horizontal with y = 6
	 */
	public void genBarCode128HorNRI6(String key) {
		/**
		 * imprimir cabeçalho: Chave de acesso
		 */
		mobileprint.Reset();
		String dataBin;
		byte[] line = new byte[1688];
		dataBin = genDataBinHor(genArrayDataAscii(key));

		byte[] hex = { 0x00, (byte) 0xFF };

		line[0] = 0x1D;
		line[1] = '*';
		line[2] = 35;// x=2 ( Width 16 dots)
		line[3] = 6;// y=3 ( Height 24 dots)

		int startLine = 4;
		int length = dataBin.length();
		for (int i = 0; i < length + 2; i++) {
			for (int j = 0; j < 6; j++) {
				// Log.d("printer",String.valueOf(x));
				if (i == length || i == length + 1) {
					line[startLine++] = hex[0];
				} else {
					int x = Integer.parseInt(dataBin.substring(i, i + 1));
					line[startLine++] = hex[x];
				}
			}
		}

		// 840valores para y=3 -- 1680 para y=6

		line[1684] = 0x1D;
		line[1685] = 0x2F;
		line[1686] = 0x03;
		mobileprint.SendBuffer(line, 1687);
		// mobileprint.FeedLines(1);
		line = new byte[4];
		line[0] = 0x1B;
		line[1] = '3';
		line[2] = 32;
		mobileprint.SendBuffer(line, 3);
		mobileprint.Reset();
		mobileprint.FormatString(false, false, true, false, false);
		try {
			mobileprint.SendString(" " + calcDV(key));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mobileprint.FeedLines(1);
	}

	/* preenche o array do quadrado a ser impresso com o topo do quadrado */
	private byte[] fillTop(byte[] line, int ini) {
		for (int i = ini; i < ini + 47; i++) {// 47+0D+0A=49
			if (i == ini || i == ini + 46) {
				if (i == ini)
					line[i] = (byte) 0xDA;
				else
					line[i] = (byte) 0xBF;
			} else {
				line[i] = (byte) 0xC4;
			}
		}
		line[ini + 47] = 0x0D;
		line[ini + 48] = 0x0A;
		arrayLineSize += 49;
		return line;
	}

	/* preenche o array do quadrado a ser impresso com o rodapé do quadrado */
	private byte[] fillBottom(byte[] line, int ini) {
		for (int i = ini; i < ini + 47; i++) {
			if (i == ini || i == ini + 46) {
				if (i == ini)
					line[i] = (byte) 0xC0;
				else
					line[i] = (byte) 0xD9;
			} else {
				line[i] = (byte) 0xC4;
			}
		}
		arrayLineSize += 47;
		return line;
	}

	/* preenche o array do quadrado a ser impresso com o conteúdo do quadrado */
	private byte[] fillContent(byte[] line, int ini, String[] content) {
		/*
		 * for(int i =0 ; i<line.length;i++){
		 * Log.d("printer",String.valueOf(line[i])); }
		 */

		int iniPos = ini;// ini = arrayLineSize
		for (int i = 0; i < content.length; i++) {
			int iniPosLine = 0;
			for (int j = iniPos; j < iniPos + 47; j++) {
				if (j == iniPos || j == iniPos + 46) {
					line[j] = (byte) '|';
				} else {
					if (iniPosLine < content[i].length()) {
						if (content[i].charAt(iniPosLine) == '|')
							line[j] = (byte) '|';
						else
							line[j] = (byte) content[i].charAt(iniPosLine);
						iniPosLine++;
					} else
						line[j] = (byte) 0x20;
				}
			}
			line[iniPos + 47] = 0x0D;
			line[iniPos + 48] = 0x0A;
			arrayLineSize = iniPos += 49;
			iniPosLine = 0;

		}
		return line;
	}

	/*
	 * dado um template, as tags e os valores (fixos) esta função substitui os
	 * valores pelas tags
	 */
	public String[] fillTemplate(String[] template, String empresa, int nfe,
			int serie) {
		for (int i = 0; i < template.length; i++) {
			template[i] = template[i].replace("#empresa", empresa)
					.replace("#nfe", String.valueOf(nfe))
					.replace("#serie", String.valueOf(serie));
		}
		return template;
	}

	/*
	 * dado um template, as tags e os valores (variáveis) esta função substitui
	 * os valores pelas tags
	 */
	public String[] fillTemplate(String[] template, String[] tags, String[] data) {
		String[] tempTemplate = template.clone();
		for (int i = 0; i < tempTemplate.length; i++) {
			for (int j = 0; j < tags.length; j++) {
				tempTemplate[i] = tempTemplate[i].replace(tags[j], data[j]);
			}
		}
		return tempTemplate;
	}

	private String fillNumSpaces(String data, int numSpaces) {
		int times = 0;
		if (numSpaces > 10) {
			times = numSpaces / 10;
			for (int i = 0; i < times; i++) {
				data += spaces[10];
			}
			data += spaces[numSpaces % 10];
		} else {
			try {
				data += spaces[numSpaces];
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	/*
	 * dado um template, as tags e os valores (variáveis) esta função substitui
	 * os valores pelas tags
	 */
	public String[] fillTemplateDiv(String[] template, String[] tags, int[] sizeslastitem, String[] data, int indexini) {
		// sizeslastitem = new int[]{33,33,33,15,28,30};//este array contem o
		// tamanho desde o inicio ultima tag até a barra
		// indexini = 1;//inicio da linha que possui tags para substituição
		// tags com marcador $ devem ser colocados espaços após
		int numSpaces = 0, $ = 0;// $ é a posição do array sizeslastitem
		String[] tempTemplate = template.clone();
		for (int i = indexini; i < tempTemplate.length; i++) {
			for (int j = 0; j < tags.length; j++) {
				int index = tempTemplate[i].indexOf("$" + tags[j]);
				if (tempTemplate[i].charAt(index == -1 ? 0 : index) == '$') {
					numSpaces = sizeslastitem[$++] - data[j].length();
					data[j] = fillNumSpaces(data[j], numSpaces);
					tempTemplate[i] = tempTemplate[i].replace("$" + tags[j], data[j]);// +spaces[numSpaces]
				} else {
					tempTemplate[i] = tempTemplate[i].replace(tags[j], data[j]);
				}
			}
		}
		return tempTemplate;
	}

	/*
	 * dado um template, as tags e os valores (variáveis) esta função substitui
	 * os valores pelas tags
	 */
	public List<String> fillTemplateProducts(String[] template, String[] tagsp,
			int[] sizesp, String[][] datap, String tot, String linePro,
			String lineBlank) {
		List<String> fullTemplate = new ArrayList<String>();
		int ini = 3;
		/*
		 * template = new String[] {
		 * " Descricao | un | qtde | Valor   |  valor     ",
		 * " Produto   |    |      | unit    |  total     ",
		 * "___________|____|______|_________|____________",
		 * "#product00 | #u | #qtd | #valor0 | #vltot     ",
		 * "           |    |      |         |            ",
		 * "Total      |    |      |         | #tot       "};
		 */
		/*
		 * String linhaPro = "#product00 | #u | #qtd | #valor0 | #vltot     ";
		 * String lineBlank = "           |    |      |         |            ";
		 * String[] spaces = new
		 * String[]{""," ","  ","   ","    ","     ","      "
		 * ,"       ","        ","         ","          "};
		 */
		boolean writeDown = true;

		int biggersize = 0;
		fullTemplate.add(template[0]);
		fullTemplate.add(template[1]);
		fullTemplate.add(template[2]);

		for (int i = 0; i < datap.length; i++) {
			biggersize = ini;
			for (int j = 0; j < 5; j++) {
				int itensize = datap[i][j].length();// 22
				int num = (int) itensize / sizesp[j];
				num += (int) itensize % sizesp[j] != 0 ? 1 : 0;// 3
				// fullTemplate.add(ini, linhaPro);
				for (int k = biggersize; k < ini + num; k++) {
					fullTemplate.add(biggersize, linePro);
					biggersize++; // ini3 e big 6
				}// agora bigsize terá a próxima posição útil, e ele menos ini
					// será a quantidade de linhas usadas
				int iniP = 0;
				for (int k = ini; k < biggersize; k++) {
					String data;
					// inicio da substring da palavra
					if (num > 1) {
						writeDown = true;
						if (k == biggersize - 1) {
							data = datap[i][j].substring(iniP);
							data += spaces[sizesp[j]
									- datap[i][j].substring(iniP).length()];
							fullTemplate
									.set(k,
											fullTemplate.get(k).replace(
													tagsp[j], data));
							// Log.d("lines","last "+fullTemplate.get(k)+
							// "spaces"+String.valueOf(sizesp[j]-datap[i][j].substring(iniP).length()));
						} else {
							fullTemplate.set(
									k,
									fullTemplate.get(k).replace(
											tagsp[j],
											datap[i][j].substring(iniP, iniP
													+ sizesp[j])));
							// Log.d("lines","not last "+fullTemplate.get(k) );
						}
					} else {
						if (writeDown) {
							data = datap[i][j];
							data += spaces[sizesp[j] - datap[i][j].length()];
							fullTemplate
									.set(k,
											fullTemplate.get(k).replace(
													tagsp[j], data));
						} else {
							data = spaces[sizesp[j]];
							fullTemplate
									.set(k,
											fullTemplate.get(k).replace(
													tagsp[j], data));
						}
						writeDown = false;// se é permitido escrever na linha
											// abaixo
						// Log.d("lines","one "+fullTemplate.get(k) );
					}
					iniP += sizesp[j];
				}
				writeDown = true;
				// maiornum=maior;

			}
			fullTemplate.add(biggersize++, lineBlank);
			ini = biggersize;
		}
		fullTemplate.add(biggersize, template[5].replace("#tot", tot));

		return fullTemplate;
	}

	/* gerar cabeçalho da nota com os campos variáveis a partir de layout */
	public void genNotaHeaderByFields(String[] template, String[] tags,
			String[] data) {
		// area útil de cada linha é de 45 caracteres, 43 se ficar um espaço
		// entre o inicio e o fim.
		byte[] line = new byte[10000];

		ArrayList<String> templ = new ArrayList<>();
		templ.add(hline);
		for (String i:template) {
			templ.add(i);
		}
		templ.add(hline);
		String[] templ2 = new String[templ.size()];
		templ2 = templ.toArray(templ2);

		// begin
		line[0] = (byte) 0x0D;// 0d
		line[1] = (byte) 0x0A;
		line[2] = (byte) 0x1B;
		line[3] = (byte) 0x33;
		line[4] = (byte) 0x06;
		line[5] = (byte) 0x0D;
		line[6] = (byte) 0x0A;
		arrayLineSize = 7;
		// top
		//line = fillTop(line, arrayLineSize);
		// content
		String[] resultTemplate = fillTemplate(templ2, tags, data);
		/**/

		line = fillContent(line, arrayLineSize, resultTemplate);
		// bottom
		//line = fillBottom(line, arrayLineSize);

		// 0d carriage/0a line feed
		mobileprint.SendBuffer(line, arrayLineSize);
		mobileprint.FeedLines(1);

	}

	/* gerar cabeçalho da nota com os campos fixos a partir de layout */
	public void genNotaProductsByFields(String[] template, String[] tagsp,
			int[] sizesp, String[][] datap, String tot) {
		// area útil de cada linha é de 45 caracteres, 43 se ficar um espaço
		// entre o inicio e o fim.
		byte[] line = new byte[10000];
		List<String> templateList;

		ArrayList<String> templ = new ArrayList<>();
		templ.add(hline);
		for (String i:template) {
			templ.add(i);
		}
		templ.add(hline);
		String[] templ2 = new String[templ.size()];
		templ2 = templ.toArray(templ2);

		// begin
		line[0] = (byte) 0x0D;// 0d
		line[1] = (byte) 0x0A;
		line[2] = (byte) 0x1B;
		line[3] = (byte) 0x33;
		line[4] = (byte) 0x06;
		line[5] = (byte) 0x0D;
		line[6] = (byte) 0x0A;
		arrayLineSize = 7;
		// top
		//line = fillTop(line, arrayLineSize);
		// content
		templateList = fillTemplateProducts(templ2, tagsp, sizesp, datap,
				tot, linePro, lineBlank);
		template = new String[templateList.size()];
		for (int i = 0; i < templateList.size(); i++) {
			template[i] = templateList.get(i);
		}
		line = fillContent(line, arrayLineSize, template);
		// bottom
		//line = fillBottom(line, arrayLineSize);

		// 0d carriage/0a line feed
		mobileprint.SendBuffer(line, arrayLineSize);
		mobileprint.FeedLines(1);

	}

	/* gerar qualquer parte da nota com os campos a partir de layout */
	public void genAllByFields(String[] template, String[] tags, String[] data) {
		// area útil de cada linha é de 45 caracteres, 43 se ficar um espaço
		// entre o inicio e o fim.
		byte[] line = new byte[10000];
		// type 0 = template sem barra divisória, e 1 com barra divisória

		ArrayList<String> templ = new ArrayList<>();
		templ.add(hline);
		for (String i:template) {
			templ.add(i);
		}
		templ.add(hline);
		String[] templ2 = new String[templ.size()];
		templ2 = templ.toArray(templ2);

		// begin
		line[0] = (byte) 0x0D;// 0d
		line[1] = (byte) 0x0A;
		line[2] = (byte) 0x1B;
		line[3] = (byte) 0x33;
		line[4] = (byte) 0x06;
		line[5] = (byte) 0x0D;
		line[6] = (byte) 0x0A;
		arrayLineSize = 7;
		// top
		//line = fillTop(line, arrayLineSize);
		// content
		String[] resultTtemplate = fillTemplate(templ2, tags, data);
		line = fillContent(line, arrayLineSize, resultTtemplate);
		// bottom
		//line = fillBottom(line, arrayLineSize);

		// 0d carriage/0a line feed
		mobileprint.SendBuffer(line, arrayLineSize);
		mobileprint.FeedLines(1);

	}

	/*
	 * gerar qualquer parte da nota com os campos a partir de layout tamanho dos
	 * itens no caso de ter barra divisória
	 */
	public void genAllByFields(String[] template, String[] tags, String[] data,
			int type, int[] sizeslastitem, int indexini) {
		// area útil de cada linha é de 45 caracteres, 43 se ficar um espaço
		// entre o inicio e o fim.
		byte[] line = new byte[10000];
		// type 0 = template sem barra divisória, e 1 com barra divisória
		// indexini = inicio da linha que contem tags para substituição, somente
		// usada no tipo 2(type = 1).

		ArrayList<String> templ = new ArrayList<>();
		templ.add(hline);
		for (String i:template) {
			templ.add(i);
		}
		templ.add(hline);
		String[] templ2 = new String[templ.size()];
		templ2 = templ.toArray(templ2);

		// begin
		line[0] = (byte) 0x0D;// 0d
		line[1] = (byte) 0x0A;
		line[2] = (byte) 0x1B;
		line[3] = (byte) 0x33;
		line[4] = (byte) 0x06;
		line[5] = (byte) 0x0D;
		line[6] = (byte) 0x0A;
		arrayLineSize = 7;

		String[] resultTemplate;

		// top
		//line = fillTop(line, arrayLineSize);
		// content
		if (type == 0) {
			resultTemplate = fillTemplate(templ2, tags, data);
		} else {
			resultTemplate = fillTemplateDiv(templ2, tags, sizeslastitem, data,
					indexini);
		}

		line = fillContent(line, arrayLineSize, resultTemplate);
		// bottom
		//line = fillBottom(line, arrayLineSize);

		// 0d carriage/0a line feed
		mobileprint.SendBuffer(line, arrayLineSize);
		mobileprint.FeedLines(1);
	}

	/* gerar qualquer parte da nota de forma estática */
	public void genAllStatic(String[] template) {
		// area útil de cada linha é de 45 caracteres, 43 se ficar um espaço
		// entre o inicio e o fim.
		byte[] line = new byte[10000];
		// type 0 = template sem barra divisória, e 1 com barra divisória

		ArrayList<String> templ = new ArrayList<>();
		templ.add(hline);
		for (String i:template) {
			templ.add(i);
		}
		templ.add(hline);
		String[] templ2 = new String[templ.size()];
		templ2 = templ.toArray(templ2);

		// begin
		line[0] = (byte) 0x0D;// 0d
		line[1] = (byte) 0x0A;
		line[2] = (byte) 0x1B;
		line[3] = (byte) 0x33;
		line[4] = (byte) 0x06;
		line[5] = (byte) 0x0D;
		line[6] = (byte) 0x0A;
		arrayLineSize = 7;
		// top
		//line = fillTop(line, arrayLineSize);
		// content
		line = fillContent(line, arrayLineSize, templ2);
		// bottom
		//line = fillBottom(line, arrayLineSize);

		// 0d carriage/0a line feed
		mobileprint.SendBuffer(line, arrayLineSize);
		mobileprint.FeedLines(1);

	}

	public void genNotaByClassSample() {
		// header
		getMobilePrinter().Reset();
		genNotaHeaderByFields(NfePrinterA7.templateHeader,
				NfePrinterA7.tagsHeader, new String[] { "Input Service LTDA",
						"652", "2" });
		// descrição
		StringBuilder valornfeStr = new StringBuilder();
		valornfeStr.append("         Danfe Simplificado          1-Saida    ");
		valornfeStr.append("         Documento Auxiliar da       NFe 617    ");
		valornfeStr.append("         Nota Fiscal Eletronica      Serie 2    ");
		valornfeStr.append("                                                ");
		try {
			getMobilePrinter().SendString(valornfeStr.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//

		// accesskey
		genAccessKey("1234567890123456789012345678901234567890123");

		// barcode
		genBarCode128HorNRI6("1234567890123456789012345678901234567890123");

		// natop - é necessário resetar a impressora após a impressão do código
		// de barras, pois é nesta função é colocado o texto em negrito
		getMobilePrinter().Reset();
		genAllByFields(
				NfePrinterA7.templateNatOp1,
				NfePrinterA7.tagsNatOp1,
				new String[] { "Venda de Mercadoria, adquirida de", "terceiros" });

		// emitente
		// nfeprinter.getMobilePrinter().Reset();
		genAllByFields(NfePrinterA7.templateEmitter, NfePrinterA7.tagsEmitter,
				new String[] { "Input Service LTDA", "Deputado",
						"Cotia - Sao Paulo", "07654", "09678", "98989", "999" });

		// destinatario
		// nfeprinter.getMobilePrinter().Reset();
		genAllByFields(NfePrinterA7.templateReceiver,
				NfePrinterA7.tagsReceiver, new String[] {
						"Distribuidora Imaginaria Ltda", "11-04-2012",
						"Av Nossa Senhora Aparecida N123", "Sao Paulo - SP",
						"11-04-2012", "54321-123", "11 9291 7463",
						"9999999999/0001-99", "18:35", "999 999 999 999" }, 1,
				NfePrinterA7.sizeslastitem, 1);

		// produtos
		// nfeprinter.getMobilePrinter().Reset();
		genNotaProductsByFields(
				NfePrinterA7.templateProduct,
				NfePrinterA7.tagsProduct,
				NfePrinterA7.sizesProduct,
				new String[][] {
						{ "impressora portatil a7", "pc", "0010", "999,00",
								"980" },
						{ "Bobina de papel termico", "pc", "0100", "4", "500" },
						{ "Bobina de papel termico", "pc", "0100", "4", "500" } },
				"9999,00");
		// nfeprinter.disconnect();
	}
}

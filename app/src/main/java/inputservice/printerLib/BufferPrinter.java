package inputservice.printerLib;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;

public class BufferPrinter extends BMap {

	public static final byte[][] BLANK = {
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 },
			{ (byte) 0x00, (byte) 0x00, (byte) 0x00 } };

	public void printLineBuffer(Bitmap bp) {

		getMobilePrinter().Reset();

		final Bitmap bitmap = bp;

		printStringBufferLine(getBuffer(byteToHex8(InvertArray(fillStringByArray(GrayscaleImageToASCII(bitmap))))));
	}

	/*
	 * prints on a higher heigth-gets a bitmap and send the generated array of
	 * letters to a function that prints a line
	 */
	public void printLineBufferHigher(Bitmap bp) {

		getMobilePrinter().Reset();

		final Bitmap bitmap = bp;
		// byte[][][] b = getBuffer(
		// byteToHex8(fillStringByArray(GrayscaleImageToASCII(bitmap))));

		printStringBufferLine(getBuffer3(byteToHex8By24(InvertArray(fillStringByArray(GrayscaleImageToASCII(bitmap))))));
	}

	public void printStringBufferLines(Bitmap bp) {

		getMobilePrinter().Reset();

		final Bitmap bitmap = bp;

		printStringBufferLine(getBuffer(byteToHex8(InvertArray(fillStringByArray(GrayscaleImageToASCII(bitmap))))));
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

	public void printStringBufferLine(byte[][][] buffer) {
		byte[] line = new byte[4000];
		// boleto.connect("L42","00:0A:3A:31:AB:6C",false);
		// boleto.connect(true);
		line[0] = 0x1D;
		line[1] = '*';
		line[2] = 72;// x=2 ( Width 460 dots)//60
		line[3] = 3;// y=3 ( Height 16 dots)

		int pos;
		// int posCharLine = 0;
		byte[][] letra;
		// char[] arrayCharLine = text.toCharArray();
		pos = 4;// 4
		for (int i = 0; i < 36; i++) {
			if (i < buffer.length) {
				letra = buffer[i];
				// System.out.println("text lenght" + text.length());
				for (int j = 0; j < 16; j++) {
					line[pos] = letra[j][0];
					pos += 1;
					line[pos] = letra[j][1];
					pos += 1;
					line[pos] = letra[j][2];
					pos += 1;
					// System.out.println("byte letra" + letra[j][0] +
					// letra[j][1]);

				}
			} else {
				letra = BLANK;
				// System.out.println("text lenght" + text.length());
				for (int j = 0; j < 16; j++) {
					line[pos] = letra[j][0];
					pos += 1;
					line[pos] = letra[j][1];
					pos += 1;
					line[pos] = letra[j][2];
					pos += 1;
					// System.out.println("byte letra" + letra[j][0] +
					// letra[j][1]);

				}
			}

		}
		// mp.SendBuffer(line, pos); // =(x*8*2)+4 ou =(x*16)+4 //1156
		line[pos++] = 0x1D;// 0
		line[pos++] = 0x2F;// 1
		line[pos++] = 0x00;// 2
		mobileprint.SendBuffer(line, pos);// print image //3
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

	public void printStringText(String[] text) {
		for (int i = 0; i < text.length; i++) {
			mobileprint.Reset();
			printStringLine(text[i]);
		}
	}

	public static byte[][][] getBuffer(byte[] hexOrg) {
		byte[] hex;
		int groups = (hexOrg.length / 32) + (hexOrg.length % 32 != 0 ? 1 : 0);
		if (hexOrg.length % 32 != 0) {
			hex = new byte[hexOrg.length + (32 - (hexOrg.length % 32))];

			int a = 0;
			for (byte b : hexOrg) {
				hex[a++] = b;
			}
			while (a < hex.length - 1) {
				hex[a++] = (byte) 0x00;
			}
			// hex[hex.length-1] = (byte)0x00;
		} else {
			hex = hexOrg;
		}

		byte[][][] buffer = new byte[groups][16][2];
		int posHex = 0;

		for (int i = 0; i < groups; i++) {
			// System.out.println("I ->" + i);

			for (int j = 0; j < 16; j++) {

				buffer[i][j][0] = hex[posHex++];
				buffer[i][j][1] = hex[posHex++];

			}
			// posHex += 2;

		}
		// System.out.println(buffer[0][0][0]);
		return buffer;
	}

	public static byte[][][] getBuffer3(byte[] hexOrg) {
		byte[] hex;
		// 2 *24 = 48 | 3*16 = 48 -
		int groups = (hexOrg.length / 48) + (hexOrg.length % 48 != 0 ? 1 : 0);
		if (hexOrg.length % 48 != 0) {
			hex = new byte[hexOrg.length + (48 - (hexOrg.length % 48))];

			int a = 0;
			for (byte b : hexOrg) {
				hex[a++] = b;
			}
			while (a < hex.length - 1) {
				hex[a++] = (byte) 0x00;
			}
			// hex[hex.length-1] = (byte)0x00;
		} else {
			hex = hexOrg;
		}

		byte[][][] buffer = new byte[groups][16][3];
		int posHex = 0;

		for (int i = 0; i < groups; i++) {
			// System.out.println("I ->" + i);

			for (int j = 0; j < 16; j++) {

				buffer[i][j][0] = hex[posHex++];
				buffer[i][j][1] = hex[posHex++];
				buffer[i][j][2] = hex[posHex++];

			}
			// posHex += 2;

		}
		// System.out.println(buffer[0][0][0]);
		return buffer;
	}

	public static byte[] byteToHex8(String[] bytes) {

		String[] dataHex = new String[bytes.length * 2];
		byte[] dataHexByte = new byte[bytes.length * 2];
		int pos = 0;

		for (int i = 0; i < bytes.length; i++) {
			dataHex[pos] = Integer.toHexString(Integer.parseInt(
					bytes[i].substring(0, 8), 2));
			dataHex[pos + 1] = Integer.toHexString(Integer.parseInt(
					bytes[i].substring(8, 16), 2));

			dataHexByte[pos] = (byte) Integer.parseInt(dataHex[pos++], 16);
			dataHexByte[pos] = (byte) Integer.parseInt(dataHex[pos++], 16);

		}

		/*
		 * for(byte b : dataHexByte){
		 * System.out.println("byte = "+String.valueOf(b)); }
		 */

		return dataHexByte;
	}

	public static byte[] byteToHex8By24(String[] bytes) {

		String[] dataHex = new String[bytes.length * 3];
		byte[] dataHexByte = new byte[bytes.length * 3];
		int pos = 0;

		for (int i = 0; i < bytes.length; i++) {
			dataHex[pos] = Integer.toHexString(Integer.parseInt(
					bytes[i].substring(0, 8), 2));
			dataHex[pos + 1] = Integer.toHexString(Integer.parseInt(
					bytes[i].substring(8, 16), 2));
			dataHex[pos + 2] = Integer.toHexString(Integer.parseInt(
					bytes[i].substring(16, 24), 2));

			dataHexByte[pos] = (byte) Integer.parseInt(dataHex[pos++], 16);
			dataHexByte[pos] = (byte) Integer.parseInt(dataHex[pos++], 16);
			dataHexByte[pos] = (byte) Integer.parseInt(dataHex[pos++], 16);

		}

		/*
		 * for(byte b : dataHexByte){
		 * System.out.println("byte = "+String.valueOf(b)); }
		 */

		return dataHexByte;
	}

	public static String[] fillStringByArray(ArrayList<String> array) {
		String[] str = new String[array.size()];
		for (int i = 0; i < array.size(); i++) {
			str[i] = array.get(i);
			System.out.println(str[i]);
		}
		return str;
	}

	/*
	 * public static String[] InvertArray(String[] array){ String[] str = new
	 * String[array.length]; String tmp = ""; for(int i=0;i<array.length;i++){
	 * for(int j = 0;j<array.length;j++){ tmp+=array[j].substring(i,i+1);
	 * //System.out.println("tmp"+tmp); } str[i]=tmp;
	 * System.out.println(str[i]); tmp=""; } return str; }
	 */
	public static String[] InvertArray(String[] array) {
		String[] str = new String[array[0].length()];
		String tmp = "";
		for (int i = 0; i < array[0].length(); i++) {
			for (int j = 0; j < array.length; j++) {
				tmp += array[j].substring(i, i + 1);
				// System.out.println("tmp"+tmp);
			}
			str[i] = tmp;
			System.out.println(str[i]);
			tmp = "";
		}
		return str;
	}

	public static String[] byteToGroup8(String codBarras) {
		// System.out.println("AQUI -> "+codBarras.length());
		int groups = (codBarras.length() / 8)
				+ (codBarras.length() % 8 != 0 ? 1 : 0);
		// System.out.println("AQUI2 -> "+groups);
		String bytes[] = new String[groups];

		int j = 0;

		// System.out.println(groups);
		for (int i = 0; i < groups; i++) {
			if (i == groups - 1) {
				bytes[i] = codBarras.substring(j, j + codBarras.length() % 8);
				for (int k = 0; k < (8 - (codBarras.length() % 8)); k++) {
					bytes[i] += "0";
				}
			} else {
				bytes[i] = codBarras.substring(j, j + 8);
			}

			j += 8;

		}
		/*
		 * for(String a : bytes){ System.out.println("bytetogroup= "+a); }
		 */
		return bytes;
	}

	public static ArrayList<String> GrayscaleImageToASCII(Bitmap bmp) {

		int pos = 0;
		ArrayList<String> array = new ArrayList<String>();

		try {
			// Create a bitmap from the image

			// The text will be enclosed in a paragraph tag with the class

			// ascii_art so that we can apply CSS styles to it.

			// html.append("&lt;br/&rt;");

			// Loop through each pixel in the bitmap

			for (int y = 0; y < bmp.getHeight(); y++) {
				for (int x = 0; x < bmp.getWidth(); x++) {
					// Get the color of the current pixel

					int c = bmp.getPixel(x, y);

					// To convert to grayscale, the easiest method is to add

					// the R+G+B colors and divide by three to get the gray

					// scaled color.

					int r = Color.red(c);
					int g = Color.green(c);
					int b = Color.blue(c);

					// Get the R(ed) value from the grayscale color,

					// parse to an int. Will be between 0-255.

					/** int rValue = (r + g + b) / 3; */
					int rValue = (int) (r * 0.21 + g * 0.71 + b * 0.07);

					// Append the "color" using various darknesses of ASCII

					// character.

					// html.append(getGrayShade(rValue));
					// array.add(object) +=
					if (x == 0) {
						array.add(pos, getGrayShade(rValue, 1));
					} else {
						array.set(pos, array.get(pos) + getGrayShade(rValue, 1));
					}

					// If we're at the width, insert a line break

					// if (x == bmp.getWidth() - 1)
					// html.append("&lt;br/&rt");
				}
				pos++;
			}

			// Close the paragraph tag, and return the html string.

			// html.append("&lt;/p&rt;");

			// return html.toString();
			return array;
		} catch (Exception exc) {
			return array;// exc.toString();
		} finally {
			bmp.recycle();
		}
	}

	private static String getGrayShade(int redValue, int scale) {
		int white;
		switch (scale) {
		case 1:
			white = 230;
			break;
		case 2:
			white = 235;
			break;
		case 3:
			white = 240;
			break;
		case 4:
			white = 245;
			break;
		default:
			white = 230;
			break;
		}
		String asciival = "1";
		if (redValue >= white)
			asciival = "0";

		return asciival;
	}
}

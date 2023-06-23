package inputservice.printerLib;

public class Interleave2of5 {
	static private String barCode = "";
	static private int size = barCode.length();

	private static String INICIO ="111000111000"; //"111000111000";
	private static String FIM = "111111111000111";//"111111111000111"
	private static String[] bin = { "00110", "10001", "01001", "11000",
			"00101", "10100", "01100", "00011", "10010", "01010" };

	private static String intercalado;

	public static byte[][][] genBarCode(String barCode) {
		Interleave2of5.barCode = barCode;
		Interleave2of5.size = Interleave2of5.barCode.length();
		
		intercalado = ""; //intercalado só era zerado na primeira vez
		genBin();

		String codBarras = genLargura();
	 ///byteToHex(	byteToGroup8(modToByte(codBarras)));
		
		return getBuffer( byteToHex(byteToGroup8(modToByte(codBarras))));
		
		
	}
	
	public static byte[][][] getBuffer(byte[] hexOrg){
		byte[] hex;
		int groups = (hexOrg.length / 2) + (hexOrg.length % 2 != 0 ? 1 : 0);
		if(hexOrg.length % 2 != 0 ){
			hex = new byte[hexOrg.length+1];
			
			int a=0;
			for(byte b : hexOrg){
				hex[a++] = b;
			}
			hex[hex.length-1] = (byte)0x00;
		}else{
			hex = hexOrg;
		}

		

		byte[][][] buffer = new byte[groups][16][2];
		int posHex = 0;

		for (int i = 0; i < groups; i++) {
			//System.out.println("I ->" + i);
			
			for (int j = 0; j < 16; j++) {

				buffer[i][j][0] = hex[posHex];
				buffer[i][j][1] = hex[posHex + 1];
				
				
			}
			posHex += 2;

		}
		//System.out.println(buffer[0][0][0]);
		return buffer;
	}
	

	public static String modToByte(String codBarras) {
		System.out.println("Cod"+codBarras);
		codBarras = codBarras.replaceAll("1-1 ", "111");
		codBarras = codBarras.replaceAll("0-3 ", "000000000");
		codBarras = codBarras.replaceAll("1-3 ", "111111111");
		codBarras = codBarras.replaceAll("0-1 ", "000");

		//System.out.println("Cod"+codBarras);
		//System.out.println("ini+Cod+fim"+INICIO+codBarras+FIM);
		//System.out.println("Cod+fim"+codBarras+FIM);
		return INICIO+codBarras+FIM;
	}
	
	public static byte[] byteToHex(String[] bytes) {
		
		String[] dataHex = new String[bytes.length];
		byte[] dataHexByte = new byte[bytes.length];

		
		for(int i = 0 ; i< bytes.length; i++){
			dataHex[i] = Integer.toHexString(Integer.parseInt(bytes[i],2));
			
			dataHexByte[i] = (byte)Integer.parseInt(dataHex[i],16);
			
		}
		
		/*for(byte b : dataHexByte){
			System.out.println("byte = "+String.valueOf(b));
		}*/
		
		return dataHexByte;
	}
	
	
	

	public static String[] byteToGroup8(String codBarras) {
	//	System.out.println("AQUI -> "+codBarras.length());
		int groups = (codBarras.length() / 8) + (codBarras.length() % 8 != 0 ?1:0);
	//	System.out.println("AQUI2 -> "+groups);
		String bytes[] = new String[groups];

		int j = 0;
		
		//System.out.println(groups);
		for (int i = 0; i < groups; i++) {
			if(i==groups-1){
				bytes[i] = codBarras.substring(j, j + codBarras.length() % 8);
				for(int k=0;k<(8-(codBarras.length() % 8));k++){
					bytes[i]+="0";
				}
			}else{
				bytes[i] = codBarras.substring(j, j + 8);
			}

			j += 8;

		}
		/*for(String a : bytes){
			System.out.println("bytetogroup= "+a);
		}*/
		return bytes;
	}
	
	
	

	public static String genLargura() {
		String codBarras = "";

		for (int i = 0; i < intercalado.length(); i++) {
			int imagem = i % 2 == 0 ? 1 : 0;

			int largura = Integer.valueOf(intercalado.substring(i, i + 1)) == 1 ? 3
					: 1;

			codBarras += String.valueOf(imagem) + "-" + String.valueOf(largura)
					+ " ";

		}
		return codBarras;
	}

	public static void genBin() {
		for (int i = 0; i < size; i++) {
			if (i % 2 != 0) {
				continue;
			}

			String d1 = barCode.substring(i, i + 1);
			String d2 = barCode.substring(i + 1, i + 2);

			/* System.out.println(d1+" - "+ d2); */

			for (int j = 0; j < 5; j++) {
				intercalado += bin[Integer.valueOf(d1)].substring(j, j + 1)
						+ bin[Integer.valueOf(d2)].substring(j, j + 1);

			}

		}
	}
}

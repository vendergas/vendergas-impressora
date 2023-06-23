package inputservice.printerLib;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptPrinterA8 extends Printer{
	public int arrayLineSize = 0;//tamanho do array usado para imprimir dados no quadrado
	
	public static final String[] template = new String[]{
			  "Protocolo: #prot",
			  "Fornecedora: #forne",
			  "CNPJ: #cnpj",
			  "           ",
		  	  "Cliente: #cliente",
		  	  "#2cliente",
		  	  "CNPJ: #2cnpj",
		  	  "Endereco: #end",
		  	  "#2end",
		  	  "     ",
		  	  "Produto              Quantidade",
		  	  "#prod    #qtd",
		  	  "                            ",
		  	  "Condicao de pagamento: #cond",
		  	  "Recebido por: #rec",
		  	  "Identificacao do Recebedor: #ide",
		  	  "                                  ",
		      "Data: #data",
		      "Hora: #hora",
		  	  "           ",
		  	  "Assinatura do cliente",
		  	  "                     ",
		  	  "....................."};
  	public static final String[] tags = {"#prot","#forne","#cnpj","#cliente","#2cliente","#2cnpj","#end","#2end","#cond","#rec","#ide","#data","#hora"};
  	public static final String[] testData = {"0000","Moggiana Ltda","123.456.789/0001-99","DRISERVE EMPRESA MINERA","CAO FONTES AGUAS MINERAL LTDA EPP","00000000016063","AV JACU-PESSEGO, 4710","VILA JACUI SAO PAULO SP BRASIL","A VISTA","JOSE","TESTE","13-03-2013","15:06"};
	
	public static final String[] spaces = new String[]{""," ","  ","   ","    ","     ","      ","       ","        ","         ","          ","           ","            ","             ","              ","               ","                ","                 ","                  ","                   "};//17
	
	public static final String linhaPro = "#prod    #qtd";
	public static final String[] tagsProduct = {"#prod","#qtd"};
	public static final int[] sizesProduct = {20,6};//numero de caracteres que cabem em cad tag/colula
	public static final String[][] testDataProduct = {{"Teste teste teste teste teste","50"},{"agua agua agua agua","20"}};
	
	/*dado um template, as tags e os valores (variáveis) esta função substitui os valores pelas tags*/
    private List<String> fillTemplateProducts(String[] template,String[] tagsp,int[] sizesp,String[][] datap,int iniTagPosition,String linePro){
    	List<String> fullTemplate = new ArrayList<String>();
    	//iniTagPosition-posição da linha onde começam as tags de produtos
    	String lineBlank = "                            ";
    	boolean writeDown = true;
    	
    	int biggersize = 0;
    	//preenche a lista com o conteúdo anterior aos produtos
    	for(int n =0; n < 11; n++){
    		fullTemplate.add(template[n]);
    	}
    	
    	for(int i =0; i< datap.length ; i++ ){
    		biggersize = iniTagPosition;
    		for(int j= 0; j<2;j++){
    			int itensize = datap[i][j].length();//22
    			int num = (int)itensize/sizesp[j];
    			num += (int) itensize%sizesp[j]!=0?1:0;//3
    			//fullTemplate.add(iniTagPosition, linhaPro);
    			for(int k = biggersize ; k < iniTagPosition+num; k++){
    				fullTemplate.add(biggersize, linePro);
    				biggersize++; //ini3 e big 6
    			}//agora bigsize terá a próxima posição útil, e ele menos ini será a quantidade de linhas usadas
    			int iniP = 0;
    			for (int k=iniTagPosition;k<biggersize;k++){
    				String data;
    				//inicio da substring da palavra
    				if(num>1){
    					writeDown = true;
    					if(k==biggersize-1){
    						data = datap[i][j].substring(iniP);
    						data+=spaces[sizesp[j]-datap[i][j].substring(iniP).length()];
    						fullTemplate.set(k, fullTemplate.get(k).replace(tagsp[j],data));
    						//Log.d("lines","last "+fullTemplate.get(k)+ "spaces"+String.valueOf(sizesp[j]-datap[i][j].substring(iniP).length()));
    					}else{
    						fullTemplate.set(k, fullTemplate.get(k).replace(tagsp[j], datap[i][j].substring(iniP, iniP+sizesp[j])));
    						//Log.d("lines","not last "+fullTemplate.get(k) );
    					}
    				}else{
    					if(writeDown){
    						data = datap[i][j];
    						data+=spaces[sizesp[j]-datap[i][j].length()];
    						fullTemplate.set(k, fullTemplate.get(k).replace(tagsp[j], data));
    					}else{
    						data = spaces[sizesp[j]];
    						fullTemplate.set(k, fullTemplate.get(k).replace(tagsp[j], data));
    					}
    					writeDown = false;//se é permitido escrever na linha abaixo
    					//Log.d("lines","one "+fullTemplate.get(k) );
    				}
    				iniP+=sizesp[j];
    			}
    			writeDown = true;
    			//maiornum=maior;
    			
    		}
    		if(i!= datap.length-1)
    			fullTemplate.add(biggersize++,lineBlank);
    		
    		iniTagPosition = biggersize;
    	}
    	//preenche a lista com o restante do conteudo após os produtos
    	for(int n =12; n < 23; n++){
    		fullTemplate.add(template[n]);
    	}
    	
    	return fullTemplate;
    }
	
    /*dado um template, as tags e os valores (variáveis) esta função substitui os valores pelas tags*/
    private String[] fillTemplate(String[] template,String[] tags,String[] data){
		String[] tempTemplate = new String[template.length];
		for(int i = 0; i < template.length; i++){
			for(int j = 0; j < tags.length; j++){
				tempTemplate[i] = template[i].replace(tags[j], data[j]);
			}
		}
		return tempTemplate;
    }
    
    /*preenche o array do quadrado a ser impresso com o conteúdo do quadrado*/
    private byte[] fillContent(byte[] line, int ini,String[] content){
    	/*for(int i =0 ; i<line.length;i++){
    		Log.d("printer",String.valueOf(line[i]));
    	}*/
    	int iniPos = ini;//ini = arrayLineSize
    	for(int i = 0 ; i < content.length; i++ ){
    		int iniPosLine = 0;
    		for(int j = iniPos; j < iniPos+32; j++ ){
        		if(iniPosLine < content[i].length()){
        			if(content[i].charAt(iniPosLine) == '|')
        				line[j] = (byte) 0xB3;
        			else
        				line[j] = (byte) content[i].charAt(iniPosLine);
        			iniPosLine++;
        		}
        		else
        			line[j] = (byte) 0x20;
    		}
    		line[iniPos+32]=0x0D;
        	line[iniPos+33]=0x0A;
    		arrayLineSize=iniPos+=34;
    		iniPosLine = 0;
    		
    	}
    	return line;
    }
    
	 /*gerar qualquer parte da nota com os campos a partir de layout*/
    public void genAllByFields(String[] template,String[] tags,String[] data,int iniTagPosition,String[][] dataP){
    	//area útil de cada linha é de 45 caracteres, 43 se ficar um espaço entre o inicio e o fim.
    	byte[] line = new byte[10000];
    	//type 0 = template sem barra divisória, e 1 com barra divisória
    	
    	//begin
    	line[0] = (byte) 0x0D;//0d
    	line[1] = (byte) 0x0A;
    	line[2] = (byte) 0x1B;
    	line[3] = (byte) 0x33;
    	line[4] = (byte) 0x06;
    	line[5] = (byte) 0x0D;
    	line[6] = (byte) 0x0A;
    	arrayLineSize = 7;
    	//content
    	template = fillTemplate(template,tags,data);
    	//products
    	//content
    	List<String> templateList = fillTemplateProducts(template,tagsProduct,sizesProduct,dataP,iniTagPosition,linhaPro);
    	template = new String[templateList.size()];
    	for(int i =0 ;i<templateList.size();i++){
    		template[i]=templateList.get(i);
    	}
    	//bytes
    	line = fillContent(line, arrayLineSize, template);
    	
    	//0d carriage/0a line feed
    	mobileprint.SendBuffer(line, arrayLineSize);
    	try {
			mobileprint.SendString("                                ");
			mobileprint.SendString("                                ");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    mobileprint.FeedLines(1);

    }
    
    /*gerar cabeçalho da nota com os campos fixos a partir de layout*/
    /*public void genNotaProductsByFields(String[] template,String[] tagsp,int[] sizesp,String[][] datap,String tot){
    	//area útil de cada linha é de 45 caracteres, 43 se ficar um espaço entre o inicio e o fim.
    	byte[] line = new byte[10000];
    	List<String> templateList;
    	
    	//begin
    	line[0] = (byte) 0x0D;//0d
    	line[1] = (byte) 0x0A;
    	line[2] = (byte) 0x1B;
    	line[3] = (byte) 0x33;
    	line[4] = (byte) 0x06;
    	line[5] = (byte) 0x0D;
    	line[6] = (byte) 0x0A;
    	arrayLineSize = 7;
    	//content
    	templateList = fillTemplateProducts(template,tagsp,sizesp,datap,tot,linePro,lineBlank);
    	template = new String[templateList.size()];
    	for(int i =0 ;i<templateList.size();i++){
    		template[i]=templateList.get(i);
    	}
    	line = fillContent(line, arrayLineSize, template);
    	//bottom
    	line=fillBottom(line, arrayLineSize);
    	
    	//0d carriage/0a line feed
    	mobileprint.SendBuffer(line, arrayLineSize);
	    mobileprint.FeedLines(1);

    }*/

}

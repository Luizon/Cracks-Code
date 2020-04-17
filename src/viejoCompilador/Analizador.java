package viejoCompilador;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.JOptionPane;

import estructuraDeDatos.ListaDoble;
import estructuraDeDatos.NodoDoble;
import estructuraDeDatos.Token;
import interfaz.Vista;
import misc.Statics;
public class Analizador
{
	int renglon=1,columna=1;
	ArrayList<String> impresion; //Aqui los que voy analizar sintacticamente
	ArrayList<Identificador> identi = new ArrayList<Identificador>();
	ListaDoble<Token> tokens;//Aqui voy metiendo los que ya aplique el analisis lexico
	Token vacio=new Token("", 9,0,0);//Utilizo esto para identificar el final en mi lista doble
	boolean bandera=true;//para saber si hubo un error l�xico
	Vista vista;
	String  css =
		"strong {"
		+ "	font-style: italic;"
		+ "}"
		+ "p {"
		+ "	color: #DD0000"
		+ "}";
	
	public ArrayList<Identificador> getIdenti() {
		return identi;
	}
	public Analizador(String ruta, Vista vista) {//Recibe el nombre del archivo de texto y la pantalla con que trabajamos
		this.vista = vista;
		analizaCodigo(ruta);
		boolean band = true;
		int tab = vista.codigoTabs.getSelectedIndex();
		if(vista != null)
			if(vista.txtCodigo.getByIndex(tab).dato.getText().length() == 0) {
				band = false;
				impresion.add("C�digo vac�o");
				vista.hayError = false;
			}
		if(band) {
			if(bandera) {//Si la bandera sigue true quiere decir que no hay errores
				impresion.add("No hay errores lexicos");
				analisisSintactio(tokens.getInicio());//Y mando analizar sintacticamente los token
			}
			else
				vista.hayError = true;
			if(impresion.get(impresion.size()-1).equals("No hay errores lexicos")) {//Si el ultimo token dice que no hay errores sintacticos
				impresion.add("No hay errores sintacticos");//Entonces no hay errores sintacticos
				vista.hayError = false;
			}
			else
				vista.hayError = true;
		}
		for (Identificador identificador : identi) {
			if (identificador.getTipo().equals("")) {
				String x =buscar(identificador.getNombre());
				identificador.setTipo(x);
			}
		}
	}
	public void analizaCodigo(String ruta) {//Recibe la ruta del archivo
		String linea="", token="";
		StringTokenizer tokenizer;
		try {
			FileReader file = new FileReader(ruta);//Acceso a la ruta
			BufferedReader archivoEntrada = new BufferedReader(file);//Abro el flujo del archivo
			linea = archivoEntrada.readLine();//Saco la linea
			impresion=new ArrayList<String>();//Inicializo mis arreglos
			tokens = new ListaDoble<Token>();//Y listas
			while (linea != null){//Recorro el archivo
				columna++;
				linea = separaDelimitadores(linea);//Checo si en la linea hay operadores o identificadores combinados y le agrego espacios
				tokenizer = new StringTokenizer(linea);//Luego parto la linea en diferentes partes 
				while(tokenizer.hasMoreTokens()) {
					token = tokenizer.nextToken();
					analisisLexico(token);//Y lo mando a analizar
				}
				linea=archivoEntrada.readLine();
				renglon++;//Cuento el renglon
			}
			archivoEntrada.close();
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null,"No se encontro el archivo favor de checar la ruta","Alerta",JOptionPane.ERROR_MESSAGE);
		}
	}
	public void analisisLexico(String token) {
		int tipo=-1;
		if(Arrays.asList("public","private").contains(token)) 
			tipo=0;//Modificador
		if(Arrays.asList("if","while").contains(token))
			tipo =1;//Palabra reservada
		if(Arrays.asList("int","boolean").contains(token))
			tipo =2;//Tipo de datos
		if(Arrays.asList("(",")","{","}","=",";").contains(token))
			tipo =3;//Simbolo
		if(Arrays.asList("<","<=",">",">=","==","!=").contains(token))
			tipo =4;//Operador logico
		if(Arrays.asList("+","-","*","/").contains(token))
			tipo =5;//Operador aritmetico
		if(Arrays.asList("true","false").contains(token) || esNumeroValido(token)) 
			tipo =6;//Constantes
		if(token.equals("class")) 
			tipo =8;//Clases
		//Cadenas validas
		if(tipo==-1) {//Quiere decir que no es ninguna de las anteriores y paso analizarla
			String caracter = "";
			String palabra = token;
			boolean band = true;
			while(palabra.length() > 0) {
				caracter = palabra.charAt(0) + "";
				//			 a				  -			z					,			A				-		   Z				 ,			 �			  ,			  �
				if(!(caracter.hashCode()>=97 && caracter.hashCode()<=122)/* && !(caracter.hashCode()>=65 && caracter.hashCode()<=90) && !caracter.equals("�") && !caracter.equals("�")*/) {
					band = false;
					break;
				}
				palabra = palabra.substring(1, palabra.length());
			}
			if(band)
				tipo = 7;
			else {
				impresion.add(Statics.getHTML("<p>Error en el token <strong>"+token+"</strong>",  css)); //Es un error y guardo el donde se produjo el error
				bandera = false;
				return;
			}
		}
		tokens.insertar(new Token(token,tipo,renglon,columna));
		impresion.add(new Token(token,tipo,renglon,columna).toString());
	}
	public static boolean esNumeroValido(String token) {
		if(token.length()==2) {
			String dato1 = token.charAt(0) + "", dato2 = token.charAt(1) + "";
			if(dato1.hashCode()>=49 && dato1.hashCode()<=57 && dato2.hashCode()>=48 && dato2.hashCode()<=57)
				return true;
		}
		else {
			if(token.length()==1) {
				String dato1 = token.charAt(0) + "";
				if(dato1.hashCode()>=49 && dato1.hashCode()<=57)
					return true;
			}
		}
		return false;
	}
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	public Token analisisSintactio(NodoDoble<Token> nodo) {
		Token tokensig, aux;
		if(nodo!=null) {
			aux =  nodo.dato;
			tokensig=analisisSintactio(nodo.siguiente);
			switch (aux.getTipo()) {
			case 0://Modificador
				int sig=tokensig.getTipo();
				if(sig!=2 && sig!=8)//Tipo de dato, clase comparamos
					impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> se esparaba un tipo de dato o indentificacion de clase", css));
				break;
			case 1://Palabra reservada
				if(aux.getValor().equals("if") || aux.getValor().equals("while")) {
					if(!tokensig.getValor().equals("(")) {
						impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> se esperaba un (", css));
					}
				}
				break;
			case 2://Tipo de dato
			case 3://Simbolo
				switch(aux.getValor()) {
				case "}":
					if(cuenta("{")!=cuenta("}"))
						impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> falta un {", css));
					break;
				case "{":
					if(cuenta("{")!=cuenta("}"))
						impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> falta un }", css));
					break;
				case "(":
					if(cuenta("(")!=cuenta(")"))
						impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> falta un )", css));
					else
					{
						if(!((nodo.anterior.dato.getValor().equals("if") || nodo.anterior.dato.getValor().equals("while")) && (esNumeroValido(tokensig.getValor()) || tokensig.getTipo()==7))) {
							impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> se esperaba un valor", css));
						}
					}
					break;
				case ")":
					if(cuenta("(")!=cuenta(")"))
						impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> falta un (", css));
					else {
						if(esNumeroValido(nodo.anterior.dato.getValor()) || nodo.anterior.dato.getTipo()==7) {
							if(nodo.siguiente!=null) {
								if(!nodo.siguiente.dato.getValor().equals("{")) {
									impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> faltan llaves despu�s de la sentencia if", css));
								}
							}
							else 
								impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> faltan llaves despu�s de la sentencia if", css));
						}
					}
					break;
				case "=":
					if(nodo.anterior.dato.getTipo()==7) {
						if(tokensig.getTipo()!=6)
							impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> se esperaba una constante", css));
						else {
							boolean a�adir = true;
							if(!nodo.siguiente.siguiente.dato.getValor().equals(";")) {
								if(!(esNumeroValido(tokensig.getValor()) && nodo.siguiente.siguiente.dato.getTipo()==5 && esNumeroValido(nodo.siguiente.siguiente.siguiente.dato.getValor()) && nodo.siguiente.siguiente.siguiente.siguiente.dato.getValor().equals(";"))) {
									impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> expresi�n aritm�tica inv�lida en <strong>" + nodo.anterior.dato.getValor() + "</strong>."
											+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;S�lo se puede realizar una operaci�n aritm�tica por instrucci�n", css)); //&nbsp; es un espacio en HTML
									a�adir = false;
								}
							}
							if(a�adir)
								if(nodo.anterior.anterior.dato.getTipo()==2)
									identi.add(new Identificador(nodo.anterior.dato.getValor(),tokensig.getValor(),nodo.anterior.anterior.dato.getValor()));
						}
					}else
						impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> se esperaba un identificador", css));
					break;
				}
				break;
			case 4://Operador logico
				if(nodo.anterior.dato.getTipo()!=6 && nodo.anterior.dato.getTipo()!=7) 
					impresion.add(Statics.getHTML("Error sinatactico en el token <strong>"+aux.getValor()+"</strong> se esperaba una constante", css));
				if(tokensig.getTipo()!=6 && nodo.siguiente.dato.getTipo()!=7)
					impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> se esperaba una constante", css));
				break;
			case 5:
				if(!esNumeroValido(nodo.siguiente.dato.getValor()) && nodo.siguiente.dato.getTipo()!=7)
					break;
				break;
			case 6://Constante
				if(nodo.anterior.dato.getValor().equals("="))
					if(tokensig.getTipo()!=5 && tokensig.getTipo()!=6 && !tokensig.getValor().equals(";"))
						impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> asignacion no valida", css));
				break;
			case 7://Identificador
				if(nodo.siguiente!=null && nodo.anterior!=null) {
					if((nodo.anterior.dato.getValor().equals("(") && nodo.siguiente.dato.getTipo() == 4)
							|| (nodo.siguiente.dato.getValor().equals(")") && nodo.anterior.dato.getTipo() == 4))
						break;
					if(!(Arrays.asList("{","=",";").contains(tokensig.getValor()))) 
						impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> se esparaba un simbolo", css));
					else
						if(nodo.anterior.dato.getValor().equals("class")){
							if(nodo.siguiente.dato.getValor().equals("{"))
								identi.add(new Identificador(aux.getValor(), "", "class"));
							else
								impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> se esparaba un {", css));
						}
				}
				else
					impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong>, instrucci�n incorrecta", css));
				break;
			case 8://Definicion de clase
				if(nodo.anterior!=null)
				if(nodo.anterior.dato.getTipo()==0) {
					if(tokensig.getTipo()!=7) 
						impresion.add(Statics.getHTML("Error sintactico en el token <strong>"+aux.getValor()+"</strong> se esparaba un identificador", css));
				}
				break;
			}
			return aux;
		}
		return  vacio;
	}
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	private String buscar(String id) 
	{
		for (int i = identi.size()-1; i >=0; i--) {
			Identificador identificador = identi.get(i);
			if(identificador.getNombre().equals(id))
				return identificador.tipo;
		}
		return "";
	}
	public String separaDelimitadores(String linea){
		for (String string : Arrays.asList("(",")","{","}","=",";")) {
			if(string.equals("=")) {
				//Si en medio de los parentesis hay este operador doy espaciado para que los tome y los identifique
				if(linea.indexOf(">=")>=0) {
					linea = linea.replace(">=", " >= ");
					break;
				}
				if(linea.indexOf("<=")>=0) {
					linea = linea.replace("<=", " <= ");
					break;
				}
				if(linea.indexOf("==")>=0)
				{
					linea = linea.replace("==", " == ");
					break;
				}
				if(linea.indexOf("<")>=0) {
					linea = linea.replace("<", " < ");
					break;
				}
				if(linea.indexOf(">")>=0) {
					linea = linea.replace(">", " > ");
					break;
				}
			}
			if(linea.contains(string)) 
				linea = linea.replace(string, " "+string+" ");
		}
		return linea;
	}
	public int cuenta(String token) {
		int conta=0;
		NodoDoble<Token> Aux=tokens.getInicio();
		while(Aux !=null){
			if(Aux.dato.getValor().equals(token))
				conta++;
			Aux=Aux.siguiente;
		}	
		return conta;
	}
	public ArrayList<String> getmistokens() {
		return impresion;
	}
}
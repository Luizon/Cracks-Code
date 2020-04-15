package viejoCompilador;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.JOptionPane;

import estructuraDeDatos.ListaDoble;
import estructuraDeDatos.NodoDoble;
import estructuraDeDatos.Token;
import interfaz.Vista;
public class Analizador
{
	int renglon=1,columna=1;
	ArrayList<String> impresion; //Aqui los que voy analizar sintacticamente
	ArrayList<Identificador> identi = new ArrayList<Identificador>();
	ListaDoble<Token> tokens;//Aqui voy metiendo los que ya aplique el analisis lexico
	Token vacio=new Token("", 9,0,0);//Utilizo esto para identificar el final en mi lista doble
	boolean bandera=true;//para saber si hubo un error léxico
	Vista vista;
	
	public ArrayList<Identificador> getIdenti() {
		return identi;
	}
	public Analizador(String ruta, Vista vista) {//Recibe el nombre del archivo de texto y la pantalla con que trabajamos
		this.vista = vista;
		analisaCodigo(ruta);
		boolean band = true;
		int tab = vista.codigoTabs.getSelectedIndex();
		if(vista != null)
			if(vista.txtCodigo.getByIndex(tab).dato.getText().length() == 0) {
				band = false;
				impresion.add("Código vacío");
				vista.hayError = false;
			}
		if(band) {
			if(bandera) {//Si la bandera sigue true quiere decir que no hay errores
				impresion.add("No hay errores lexicos");
//				analSintactico.analisis(tokens.getFin());//Y mando analizar sintacticamente los token
//				analisisSintactico(tokens.getFin());//Y mando analizar sintacticamente los token
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
	public void analisaCodigo(String ruta) {//Recibe la ruta del archivo
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
				//			 a				  -			z					,			A				-		   Z				 ,			 ñ			  ,			  Ñ
				if(!(caracter.hashCode()>=97 && caracter.hashCode()<=122)/* && !(caracter.hashCode()>=65 && caracter.hashCode()<=90) && !caracter.equals("ñ") && !caracter.equals("Ñ")*/) {
					band = false;
					break;
				}
				palabra = palabra.substring(1, palabra.length());
			}
			if(band)
				tipo = 7;
			else {
				impresion.add("<HTML><p style=\"color: #DD0000;\">Error en el token <strong style=\"font-style: italic;\">"+token+"</strong>"); //Es un error y guardo el donde se produjo el error
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
					impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> se esparaba un tipo de dato o indentificacion de clase");
				break;
			case 1://Palabra reservada
				if(aux.getValor().equals("if") || aux.getValor().equals("while")) {
					if(!tokensig.getValor().equals("(")) {
						impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> se esperaba un (");
					}
				}
				break;
			case 2://Tipo de dato
			case 3://Simbolo
				switch(aux.getValor()) {
				case "}":
					if(cuenta("{")!=cuenta("}"))
						impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> falta un {");
					break;
				case "{":
					if(cuenta("{")!=cuenta("}"))
						impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> falta un }");
					break;
				case "(":
					if(cuenta("(")!=cuenta(")"))
						impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> falta un )");
					else
					{
						if(!((nodo.anterior.dato.getValor().equals("if") || nodo.anterior.dato.getValor().equals("while")) && (esNumeroValido(tokensig.getValor()) || tokensig.getTipo()==7))) {
							impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> se esperaba un valor");
						}
					}
					break;
				case ")":
					if(cuenta("(")!=cuenta(")"))
						impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> falta un (");
					else {
						if(esNumeroValido(nodo.anterior.dato.getValor()) || nodo.anterior.dato.getTipo()==7) {
							if(nodo.siguiente!=null) {
								if(!nodo.siguiente.dato.getValor().equals("{")) {
									impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> faltan llaves después de la sentencia if");
								}
							}
							else 
								impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> faltan llaves después de la sentencia if");
						}
					}
					break;
				case "=":
					if(nodo.anterior.dato.getTipo()==7) {
						if(tokensig.getTipo()!=6)
							impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> se esperaba una constante");
						else {
							boolean añadir = true;
							if(!nodo.siguiente.siguiente.dato.getValor().equals(";")) {
								if(!(esNumeroValido(tokensig.getValor()) && nodo.siguiente.siguiente.dato.getTipo()==5 && esNumeroValido(nodo.siguiente.siguiente.siguiente.dato.getValor()) && nodo.siguiente.siguiente.siguiente.siguiente.dato.getValor().equals(";"))) {
									impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> expresión aritmética inválida en <strong style=\"font-style: italic;\">" + nodo.anterior.dato.getValor() + "</strong>."
											+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;Sólo se puede realizar una operación aritmética por instrucción"); //&nbsp; es un espacio en HTML
									añadir = false;
								}
							}
							if(añadir)
								if(nodo.anterior.anterior.dato.getTipo()==2)
									identi.add(new Identificador(nodo.anterior.dato.getValor(),tokensig.getValor(),nodo.anterior.anterior.dato.getValor()));
						}
					}else
						impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> se esperaba un identificador");
					break;
				}
				break;
			case 4://Operador logico
				if(nodo.anterior.dato.getTipo()!=6 && nodo.anterior.dato.getTipo()!=7) 
					impresion.add("<HTML><p style=\"color: #DD0000; \">Error sinatactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> se esperaba una constante");
				if(tokensig.getTipo()!=6 && nodo.siguiente.dato.getTipo()!=7)
					impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> se esperaba una constante");
				break;
			case 5:
				if(!esNumeroValido(nodo.siguiente.dato.getValor()) && nodo.siguiente.dato.getTipo()!=7)
					break;
				break;
			case 6://Constante
				if(nodo.anterior.dato.getValor().equals("="))
					if(tokensig.getTipo()!=5 && tokensig.getTipo()!=6 && !tokensig.getValor().equals(";"))
						impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> asignacion no valida");
				break;
			case 7://Identificador
				if(nodo.siguiente!=null && nodo.anterior!=null) {
					if((nodo.anterior.dato.getValor().equals("(") && nodo.siguiente.dato.getTipo() == 4)
							|| (nodo.siguiente.dato.getValor().equals(")") && nodo.anterior.dato.getTipo() == 4))
						break;
					if(!(Arrays.asList("{","=",";").contains(tokensig.getValor()))) 
						impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> se esparaba un simbolo");
					else
						if(nodo.anterior.dato.getValor().equals("class")){
							if(nodo.siguiente.dato.getValor().equals("{"))
								identi.add(new Identificador(aux.getValor(), "", "class"));
							else
								impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> se esparaba un {");
						}
				}
				else
					impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong>, instrucción incorrecta");
				break;
			case 8://Definicion de clase
				if(nodo.anterior!=null)
				if(nodo.anterior.dato.getTipo()==0) {
					if(tokensig.getTipo()!=7) 
						impresion.add("<HTML><p style=\"color: #DD0000; \">Error sintactico en el token <strong style=\"font-style: italic;\">"+aux.getValor()+"</strong> se esparaba un identificador");
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
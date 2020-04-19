package compilador;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import misc.Statics;

public class AnalizadorSemantico {
	static Stack<Token> pilaDeParentesis;
	static String [] cabecera;
	static JTable tabla;
	static HashMap<String, Identificador> identificadores;
	static ArrayList<String> listaDeImpresiones;
	static ArrayList<Token> tokens;
	static Stack<Integer> alcanceLocal; // para guardar el alcance de las variables
	static int contadorDeIdentificadores, alcance, contadorDeContextos;
	static boolean hayIgual = false;
	
	public static boolean analizarTokens(ArrayList<String> listaDeImpresiones, ArrayList<Token> tokens, HashMap<String, Identificador> identificadores, JTable tabla, String [] cabecera) {
		AnalizadorSemantico.cabecera = cabecera;
		AnalizadorSemantico.tabla = tabla;
		AnalizadorSemantico.identificadores = identificadores;
		AnalizadorSemantico.listaDeImpresiones = listaDeImpresiones;
		AnalizadorSemantico.tokens = tokens;
		alcanceLocal = new Stack<Integer>();
		int tipo = -1;
		alcance = contadorDeIdentificadores = contadorDeContextos = 0;
		String valor = "",
			nombre = "";
		boolean errorEnLinea = false,
			analisisCorrecto = true;
		hayIgual = false;
		int ultimaLineaDeError = -1;
		pilaDeParentesis = new Stack<Token>();
		for(int i=0; i<tokens.size(); i++) {
			Token token = tokens.get(i);
			if(errorEnLinea)
				if(ultimaLineaDeError < token.getLinea()) { // si se ha pasado a otra linea y había error, se restaura todo, el error queda en la linea anterior
					errorEnLinea = false;
					alcance = tipo = -1;
					valor = nombre = "";
				}
			if(tokens.get(i).getValor().equals(";")) {
				if(!(tipo < 0 && valor.length() == 0 && nombre.length() == 0)) {
					if(!creaNuevoSimbolo(i, alcance, tipo, nombre, valor, errorEnLinea))
						analisisCorrecto = false;
					tipo = -1;
					alcance = 0;
					valor = nombre = "";
				}
				errorEnLinea = hayIgual = false;
				continue;
			}
			if(token.getTipo() == Statics.tipoDeDatoInt) {
				if(tipo < 0 && nombre.length() == 0 && valor.length() == 0)
					tipo = Statics.deArrayEstaticaADinamica(Statics.tipoDeDato).indexOf(token.getValor());
				else {
					errorEnLinea = true;
					analisisCorrecto = false;
					ultimaLineaDeError = token.getLinea();
					mensajeSeEsperaba(i);
				}
				continue;
			}
			if(token.getTipo() == Statics.identificadorInt) {
				if(/*tipo >= 0 && */nombre.length() == 0 && valor.length() == 0) {
					nombre = token.getValor();
					continue;
				}
				String mensajeError = "";
				if(nombre.length() > 0 && valor.length() == 0) {
					if(i > 0)
						if(tokens.get(i-1).getValor().equals("="))
							if(identificadores.containsKey(token.getValor())) {
								int tipoDelPrimerIdentificador = tipo;
								if(tipoDelPrimerIdentificador < 0)
									if(identificadores.containsKey(tokens.get(i-2).getValor()))
										tipoDelPrimerIdentificador = identificadores.get(tokens.get(i-2).getValor()).getTipo();
									else {
										mensajeError = "<p>Error en la linea "+token.getLinea()+": error al asignar la variable <strong>" + token.getValor() + "</strong> a la variable " + tokens.get(i-2).getValor()
										+"<br />&nbsp;&nbsp;&nbsp;&nbsp;La variable " + tokens.get(i-2).getValor() + " no ha sido declarada anteriormente.";
										listaDeImpresiones.add(Statics.getHTML(mensajeError, Statics.consolaCss));
										analisisCorrecto = false;
									}
								if(identificadores.get(token.getValor()).getTipo() == tipoDelPrimerIdentificador) {
									if(hayIgual)
										valor = identificadores.get(token.getValor()).getValor();
									else {
										mensajeError = "<p>Error en la linea "+token.getLinea()+": error al asignar la variable <strong>" + token.getValor() + "</strong> a la variable <strong>" + nombre + "</strong>"
										+"<br />&nbsp;&nbsp;&nbsp;&nbsp;Hace falta un <b>=</b> entre ellos.";
										listaDeImpresiones.add(Statics.getHTML(mensajeError, Statics.consolaCss));
									}
									continue;
								}
								else {
									if(tipoDelPrimerIdentificador > 0) {
										mensajeError = "<p>Error en la linea "+token.getLinea()+": error al asignar la variable <strong>" + token.getValor() + "</strong> a la variable <strong>" + nombre + "</strong>"
										+"<br />&nbsp;&nbsp;&nbsp;&nbsp;La variable "+token.getValor()+" no es de un tipo compatible con el de la variable " + nombre + ", ("+Statics.tipoDeDato[identificadores.get(token.getValor()).getTipo()]+" y " + Statics.tipoDeDato[tipoDelPrimerIdentificador] + ")";
										listaDeImpresiones.add(Statics.getHTML(mensajeError, Statics.consolaCss));
									}
									analisisCorrecto = false;
								}
							}
							else {
								mensajeError = "<p>Error en la linea "+token.getLinea() + ": error al asignar la variable <strong>" + token.getValor() + "</strong> a la variable <strong>" + nombre + "</strong>"
								+"<br />&nbsp;&nbsp;&nbsp;&nbsp;La variable "+token.getValor()+" no ha sido declarada antes de su uso.";
								listaDeImpresiones.add(Statics.getHTML(mensajeError, Statics.consolaCss));
								analisisCorrecto = false;
							}
				}
				errorEnLinea = true;
				analisisCorrecto = false;
				ultimaLineaDeError = token.getLinea();
				if(mensajeError.length() == 0) {
					mensajeSeEsperaba(i);
				}
				continue;
			}
			if(token.getValor().equals("=")) {
				if(/*tipo >= 0 && */nombre.length() > 0 && valor.length() == 0) {
					hayIgual = true;
					continue;
				}
				else {
					errorEnLinea = true;
					analisisCorrecto = false;
					ultimaLineaDeError = token.getLinea();
					mensajeSeEsperaba(i);
				}
				continue;
			}
			if(token.getTipo() == Statics.dobleInt || token.getTipo() == Statics.booleanoInt || token.getTipo() == Statics.enteroInt || token.getTipo() == Statics.cadenaInt) {
				if(/*tipo >= 0 && */nombre.length() > 0 && valor.length() == 0 && hayIgual) {
					valor = token.getValor();
				}
				else {
					errorEnLinea = true;
					analisisCorrecto = false;
					ultimaLineaDeError = token.getLinea();
					if(!hayIgual) {
						String mensajeError = "<p>Error en la linea "+token.getLinea()+": error al asignar la constante <strong>" + token.getValor() + "</strong> a la variable <strong>" + nombre + "</strong>"
						+"<br />&nbsp;&nbsp;&nbsp;&nbsp;Hace falta un <b>=</b> entre ellos.";
						listaDeImpresiones.add(Statics.getHTML(mensajeError, Statics.consolaCss));
					}
					else
						mensajeSeEsperaba(i);
				}
				continue;
			}
			if(token.getTipo() == Statics.parentesisInt || token.getTipo() == Statics.llaveInt) {
				if(token.getValor().equals("(") || token.getValor().equals("{")) {
					pilaDeParentesis.push(token);
				}
				else {
					if(!pilaDeParentesis.isEmpty()) {
						if(pilaDeParentesis.peek().getValor().equals("(") && token.getValor().equals(")")
						|| pilaDeParentesis.peek().getValor().equals("{") && token.getValor().equals("}")) {
							pilaDeParentesis.pop();
						}
						else {
							String tokenEsperado;
							if(pilaDeParentesis.peek().getValor().equals("("))
								tokenEsperado = ")";
							else
								tokenEsperado = "}";
							String texto = Statics.getHTML("<p>Error la linea "+token.getLinea()+": Se esperaba un <strong>" + tokenEsperado + "</strong> para cerrar el "
								+ "<strong>" + token.getValor() + "</strong> añadido anteriormente.",  Statics.consolaCss);
							listaDeImpresiones.add(texto); //Es un error y guardo el donde se produjo el error
							System.out.println("Error sintáctico: se esperaba un " + tokenEsperado + " para cerrar el último " + token.getValor() + ".");
							analisisCorrecto = false;
						}
					}
					else {
						String tokenFaltante;
						if(token.getValor().equals(")"))
							tokenFaltante = "(";
						else
							tokenFaltante = "{";
						String texto = Statics.getHTML("<p>Error la linea "+token.getLinea()+": No hay un <strong>" + tokenFaltante + "</strong> para cerrar con el "
							+ "<strong>" + token.getValor() + "</strong> que está agregando.",  Statics.consolaCss);
						listaDeImpresiones.add(texto); //Es un error y guardo el donde se produjo el error
						System.out.println("Error sintáctico: no se esperaba un " + token.getValor() + " porque no hay un " + tokenFaltante + " anteriormente.");
						return false;
					}
				}
				continue;
			}
			if(token.getTipo() == Statics.claseInt) {
				if(tipo < 0) {
					tipo = Statics.getTipoDeConstante("class"); // no es una constante, sino declaración de clase, pero me es más cómodo usar esto así
				}
				else {
					errorEnLinea = true;
					analisisCorrecto = false;
					ultimaLineaDeError = token.getLinea();
					mensajeSeEsperaba(i);
				}
				continue;
			}
		}
		
		if(!pilaDeParentesis.isEmpty()) { // muestrá la cola de parentesis por cerrar y que no fueron cerrados
			Stack<Token> cola = new Stack<Token>();
			while(!pilaDeParentesis.isEmpty()) {
				cola.push(pilaDeParentesis.pop());
			}
			while(!cola.isEmpty()) {
				Token tokenDePila = cola.pop();
				String texto = Statics.getHTML("<p>Error la linea "+tokenDePila.getLinea()+": No se cerró correctamente el <strong>" + tokenDePila.getValor() + "</strong>.",  Statics.consolaCss);
				listaDeImpresiones.add(texto); //Es un error y guardo el donde se produjo el error
				System.out.println("Error en analisis léxico: No se cerró correctamente el " + tokenDePila.getValor() + " de la linea " + tokenDePila.getLinea() + ".");
				analisisCorrecto = false;
			}
		}
		
		if(!(tipo < 0 && valor.length() == 0 && nombre.length() == 0)) { // si hay una última declaración a la cual no se le puso ; pero de hecho estaba correcta, se hace
			Token ultimoToken = tokens.get(tokens.size()-1);
			tokens.add(new Token(ultimoToken.getLinea(), Statics.signoInt, tokens.size(), ";"));
			if(creaNuevoSimbolo(tokens.size()-1, alcance, tipo, nombre, valor, errorEnLinea))
				contadorDeIdentificadores++;
			}
		actualizaTabla();
		if(analisisCorrecto)
			listaDeImpresiones.add(Statics.getHTML("<var>Código sin errores sintácticos ni semánticos.", Statics.consolaCss));
		return analisisCorrecto;
	}
	
	private static boolean creaNuevoSimbolo(int i, int alcance, int tipo, String nombre, String valor, boolean errorEnLinea) {
		boolean esUnaDeclaracionVacia = false,
			esUnaAsignacionPeroNoUnaDeclaracion = false,
			todoCorrecto = true;
		if(alcance == 0) // si no se escribió el alcance
			alcance = (alcanceLocal.isEmpty() ? 0 : 1); // si está afuera de todo, es automáticamente global, de lo contrario es automáticamente local
		if(tipo < 0) {
			if(i >= 3) {
				int tipoDelIdentificador = tipo,
					tipoDeLaAsignacion = Statics.getTipoDeConstante(tokens.get(i-1).getValor());
				if(identificadores.containsKey(nombre))
					tipoDelIdentificador = identificadores.get(nombre).getTipo();
				else
					todoCorrecto = false;
				if(tipoDeLaAsignacion < 0) {
					if(identificadores.containsKey(tokens.get(i-1).getValor()))
						tipoDeLaAsignacion = identificadores.get(tokens.get(i-1).getValor()).getTipo();
					else
						todoCorrecto = false;
				}
				boolean esUnaAsignacionCorrecta = tipoDelIdentificador == tipoDeLaAsignacion;
				if(esUnaAsignacionCorrecta && tokens.get(i-2).getValor().equals("=") && tokens.get(i-3).getTipo() == Statics.identificadorInt)
					esUnaAsignacionPeroNoUnaDeclaracion = true;
			}
			if(!esUnaAsignacionPeroNoUnaDeclaracion) {
				if(hayIgual)
					imprimirErrorDeDeclaracion(tokens.get(i).getLinea(), "tipo de dato", " "+nombre);
				todoCorrecto = false;
			}
		}
		if(nombre.length() == 0) {
			imprimirErrorDeDeclaracion(tokens.get(i).getLinea(), "nombre", "");
			todoCorrecto = false;
		}
		if(valor.length() == 0) {
			if(i > 1) {
				if(tipo >= 0 && nombre.length() > 0 && tokens.get(i-1).getTipo() == Statics.identificadorInt && tokens.get(i-2).getTipo() == Statics.tipoDeDatoInt)
					esUnaDeclaracionVacia = true;
			}
			if(!esUnaDeclaracionVacia) {
				boolean esNulo = false;
				if(i > 0)
					if(tokens.get(i-1).getTipo() == Statics.identificadorInt)
						if(identificadores.containsKey(tokens.get(i-1).getValor()))
							if(identificadores.get(tokens.get(i-1).getValor()).getValor().length() == 0)
								esNulo= true;
				if(esNulo) {
					String texto = "<p>Error en la linea "+tokens.get(i).getLinea()+": el variable <strong>"+tokens.get(i-1).getValor()+"</strong> no tiene asignado un valor aún, por lo"
					+ "<br />&nbsp;&nbsp;&nbsp;&nbsp;que no puede ser usado para asignarse a otra variable, como intentó con <strong>"+nombre;
					listaDeImpresiones.add(Statics.getHTML(texto, Statics.consolaCss));
					todoCorrecto = false;
				}
				else {
					if(hayIgual)
						imprimirErrorDeDeclaracion(tokens.get(i).getLinea(), "valor", " "+nombre);
					todoCorrecto = false;
				}
			}
		}
		if(todoCorrecto) {
			Identificador nuevoIdentificador;
			nuevoIdentificador = new Identificador(tokens.get(i).getLinea(), alcance, tipo, nombre, valor, contadorDeIdentificadores);
			if(!identificadores.containsKey(nombre)) {
				if(!errorEnLinea) {
					if((esUnaAsignacionPeroNoUnaDeclaracion)
					|| esUnaDeclaracionVacia
					|| (tipo >= 0 && tipo <= 3 && tipo == Statics.getTipoDeConstante(valor))) {
						if(!esUnaAsignacionPeroNoUnaDeclaracion) {
							identificadores.put(nombre, nuevoIdentificador);
							contadorDeIdentificadores++;
//							listaDeImpresiones.add(Statics.getHTML(nuevoIdentificador.toHTML(), Statics.consolaCss));
//							System.out.println(nuevoIdentificador);
						}
						else {
							identificadores.get(nombre).setValor(valor);
						}
					}
					else {
						todoCorrecto = false;
						String texto = "<p>Error en la linea " + tokens.get(i).getLinea() + ": El identificador <strong>" + nombre + "</strong>, del tipo <i>" + Statics.tipoDeDato[tipo] + "</i> es incompatible"
							+ "<br />&nbsp;&nbsp;&nbsp;&nbsp;con el tipo de valor <i>" + Statics.tipoDeDato[Statics.getTipoDeConstante(valor)] + "</i> que se le está asignando";
						listaDeImpresiones.add(Statics.getHTML(texto, Statics.consolaCss));
						System.out.println("Error sintáctico: identificador "+nombre+" previamente agregado en la linea "+tokens.get(i).getLinea());
					}
				}
			}
			else {
				if(esUnaAsignacionPeroNoUnaDeclaracion) {
					identificadores.get(nombre).setValor(valor);
				}
				else {
					todoCorrecto = false;
					String texto = "<p>Error en la linea " + tokens.get(i).getLinea() + ": El identificador <strong>" + nombre + "</strong>, del tipo " + Statics.tipoDeDato[tipo] + ", ya existe"
						+ "<br />&nbsp;&nbsp;&nbsp;&nbsp;en la linea " + identificadores.get(nombre).getPosicion() + " con el tipo de identificador <i>" + Statics.tipoDeDato[identificadores.get(nombre).getTipo()];
					listaDeImpresiones.add(Statics.getHTML(texto, Statics.consolaCss));
					System.out.println("Error sintáctico: identificador "+nombre+" previamente agregado en la linea "+identificadores.get(nombre).getPosicion());
				}
			}
		}
		return todoCorrecto;
	}

	private static void imprimirErrorDeDeclaracion(int linea, String error, String identificador) {
		String mensaje = "<p>Error en la linea <strong>"+linea+"</strong>"
			+ "<br />&nbsp;&nbsp;&nbsp;&nbsp;No se definió un <strong>"+error+"</strong> para el identificador" + identificador + ".";
		String htmlImpreso = Statics.getHTML(mensaje, Statics.consolaCss);
		listaDeImpresiones.add(htmlImpreso);
	}
	
	private static void mensajeSeEsperaba(int i) {
		String seEsperaba = "Se esperaba ";
		if(i == 0)
			seEsperaba+= "un <b>modificador</b>, <b>clase</b> o <b>tipo de dato</b>.";
		else
			switch(tokens.get(i-1).getTipo()) {
			case Statics.tipoDeDatoInt:
				seEsperaba+= "un <b>identificador</b> luego del token <i>"+tokens.get(i-1).getValor();
				break;
			case Statics.identificadorInt:
				boolean esUnaClase = false;
					if(i > 1)
						if(tokens.get(i-2).getValor().equals("class"))
							esUnaClase = true;
				if(!esUnaClase)
					seEsperaba+= "un signo <b>=</b> luego del token <i>"+tokens.get(i-1).getValor();
				else
					seEsperaba+= "un signo <b>{</b> luego del token <i>"+tokens.get(i-1).getValor();
				break;
			case Statics.booleanoInt:
			case Statics.enteroInt:
			case Statics.dobleInt:
			case Statics.cadenaInt:
				seEsperaba+= "un signo <b>;</b> luego de la constante <i>"+tokens.get(i-1).getValor();
				break;
			case Statics.claseInt:
				seEsperaba+= "un <b>identificador</b> para nombrar la clase.";
				break;
			case Statics.llaveInt:
				seEsperaba+= "un <b>modificador</b>, <b>clase</b> o <b>tipo de dato</b>.";
				break;
			case Statics.parentesisInt:
				if(tokens.get(i-1).getValor().equals("("))
					seEsperaba+= "una <b>expresión lógica</b> o un <b>booleano</b> luego del <i>(";
				else
					seEsperaba+= "un <b>modificador</b>, <b>clase</b> o <b>tipo de dato</b>.";
				break;
			case Statics.operadorAritmeticoInt:
				seEsperaba+= "un <b>valor numérico</b> luego del operador <i>"+tokens.get(i-1);
				break;
			case Statics.operadorLogicoInt:
				break;
			case Statics.palabraReservadaInt: // if, while
				break;
			case Statics.signoInt: // =, ;
				break;
			default: // =, ;
				seEsperaba+= "algo luego del token <i>"+tokens.get(i-1).getValor()+", F como dicen los chavos.";
				break;
			}
		String mensajeError = "<p>Error en el token <strong>"+tokens.get(i).getValor()+"</strong> en la linea "+tokens.get(i).getLinea()
		+"<br />&nbsp;&nbsp;&nbsp;&nbsp;"+seEsperaba;
		listaDeImpresiones.add(Statics.getHTML(mensajeError, Statics.consolaCss));
	}
	
	public static void actualizaTabla() {
		Identificador [] identificador = new Identificador[identificadores.size()];
		int i = 0;
		for(Identificador identificadorDesdeLaHashMap: identificadores.values()) { // recolecta los datos del HashMap
			identificador[i] = identificadorDesdeLaHashMap;
			i++;
		}

		DefaultTableModel modelo = new DefaultTableModel(new Object[0][0], cabecera);
		tabla.setModel(modelo);
		// y mete los datos obtenidos, ya una vez ordenados ordena los datos obtenidos
		for(i = 0; i < identificador.length; i++) {
			for(int j=0; j<identificador.length; j++)
				if(identificador[j].getId() == i) {
					Identificador id = identificador[j];
					int alcanceIndex = (id.getAlcance() < 2 ? id.getAlcance() : 1);
					Object datostabla[]= {id.getPosicion(), Statics.alcance[alcanceIndex], Statics.tipoDeDato[id.getTipo()], id.getNombre(), id.getValor()};
					modelo.addRow(datostabla);
					break;
				}
		}
		
		// Esto es para que la tabla no sea taaaan fea
		Font fontHeader = new Font("Arial", Font.BOLD, 17);
		Font fontTable = new Font("Arial", Font.PLAIN, 17);
		tabla.getTableHeader().setFont(fontHeader);
		tabla.setFont(fontTable);
		tabla.setEnabled(false);
		tabla.setFocusable(false);
		DefaultTableCellRenderer celdasCentradas = new DefaultTableCellRenderer();
		celdasCentradas.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		for(i = 0; i<tabla.getColumnModel().getColumnCount(); i++) {
			tabla.getColumnModel().getColumn(i).setCellRenderer(celdasCentradas);
		}

	}
}

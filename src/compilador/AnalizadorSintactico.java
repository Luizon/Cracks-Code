package compilador;

import java.util.ArrayList;
import java.util.Stack;

import estructuraDeDatos.ArbolBinario;
import misc.Statics;

public class AnalizadorSintactico {
	static Stack<Token> pilaDeParentesis;
	static ArrayList<Token> listaDeOperadores,
		listaDeOperandos;
	public static boolean analizaTokens(ArrayList<String> listaDeImpresiones, ArrayList<Token> tokens) {
		pilaDeParentesis = new Stack<Token>();
		listaDeOperadores = new ArrayList<Token>();
		listaDeOperandos = new ArrayList<Token>();
		boolean analisisCorrecto = true;
		ArrayList<ArbolBinario<Token>> arboles = new ArrayList<ArbolBinario<Token>>();
		for(int i=0; i<tokens.size(); i++) {
			Token token = tokens.get(i);
			if(token.getTipo() != Statics.llaveInt && token.getTipo() != Statics.parentesisInt
				&& token.getTipo() != Statics.enteroInt && token.getTipo() != Statics.operadorAritmeticoInt
				&& token.getTipo() != Statics.identificadorInt
				&& !token.getValor().equals(";")) {
//				System.out.println("token "+token.getValor()+" saltado");
				continue;
			}
			if(token.getValor().equals("(") || token.getValor().equals("{")) {
				pilaDeParentesis.push(token);
			}
			else if(token.getValor().equals(")") || token.getValor().equals("}")) {
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
							+ "<strong>" + pilaDeParentesis.peek().getValor() + "</strong> añadido en la linea " + pilaDeParentesis.peek().getLinea() + ".",  Statics.consolaCss);
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
					analisisCorrecto = false;
				}
			}
			else if(token.getTipo() == Statics.enteroInt || token.getTipo() == Statics.identificadorInt) {
				if(token.getTipo() == Statics.identificadorInt) {
					if(tokens.get(i - 1).getTipo() != Statics.operadorAritmeticoInt && tokens.get(i + 1).getTipo() != Statics.operadorAritmeticoInt) {
						continue;
					}
				}
				if(listaDeOperandos.size() > listaDeOperadores.size()) {
					String texto = Statics.getHTML("<p>Error la linea "+token.getLinea()+": Expresión algebraica erronea, se esperaba un <strong>operador</strong> luego del operando <strong>" + listaDeOperandos.get(listaDeOperandos.size()-1).getValor() + "</strong>."
						+ "<br />&nbsp;&nbsp;&nbsp;&nbsp;Se obtuvo <strong>" + token.getValor() + "<strong>",  Statics.consolaCss);
					listaDeImpresiones.add(texto); //Es un error y guardo el donde se produjo el error
					analisisCorrecto = false;
				}
				listaDeOperandos.add(token);
				tokens.set(i, null);
//				System.out.println("entero "+token.getValor()+" quitado");
			}
			else if(token.getTipo() == Statics.operadorAritmeticoInt) {
				if(listaDeOperandos.size() == listaDeOperadores.size()) {
					String error;
					if(listaDeOperadores.size() > 0)
						error = "se esperaba un <strong>operando</strong> luego del operador <strong>" + listaDeOperadores.get(listaDeOperadores.size()-1).getValor() + "</strong>."
							+ "<br />&nbsp;&nbsp;&nbsp;&nbsp;Se obtuvo <strong>" + token.getValor() + "<strong>";
					else
						error = "se esperaba un <strong>operando</strong> antes del primer operador <strong>" + token.getValor() + "</strong>";
					String texto = Statics.getHTML("<p>Error la linea "+token.getLinea()+": Expresión algebraica erronea, " + error,  Statics.consolaCss);
					listaDeImpresiones.add(texto); //Es un error y guardo el donde se produjo el error
					analisisCorrecto = false;
				}
				listaDeOperadores.add(token);
				tokens.set(i, null);
//				System.out.println("operador "+token.getValor()+" quitado");
			}
			else { // ;
//				System.out.println("token "+token.getValor()+" encontrado");
				if(listaDeOperadores.isEmpty() && listaDeOperandos.isEmpty())
					continue;
//				System.out.println(listaDeOperandos.size() + ", " + listaDeOperadores.size());
				if(listaDeOperandos.size() == listaDeOperadores.size() + 1) {
					if(listaDeOperadores.size() > 0) {
						ArbolBinario<Token> arbol = new ArbolBinario<>(token.getLinea(),
							Statics.expresionAlgebraicaInt, listaDeOperandos.get(0).getId());
						arboles.add(arbol);
						arbol.insertar(0, listaDeOperandos.get(0));
						for(int z = 0 ; z < listaDeOperadores.size() ; z++) {
							arbol.insertar(z*2 + 1, listaDeOperadores.get(z));
							arbol.insertar(z*2 + 2, listaDeOperandos.get(z + 1));
						}
						arbol.setValor("arbolonski");
						tokens.set(listaDeOperandos.get(0).getId(), arbol);
//						System.out.println(arbol.getValor());
					}
					else {
						tokens.set(listaDeOperandos.get(0).getId(), listaDeOperandos.get(0));
//						System.out.println("operando "+listaDeOperandos.get(0).getValor()+" reestablecido");
					}
				}
				else {
					String texto = Statics.getHTML("<p>Error la linea "+token.getLinea()+": La cantidad de <strong>operadores</strong> debe ser igual a la cantidad de <strong>operandos</strong> menos uno.",  Statics.consolaCss);
					listaDeImpresiones.add(texto); //Es un error y guardo el donde se produjo el error
					analisisCorrecto = false;
				}
				listaDeOperandos.clear();
				listaDeOperadores.clear();
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
				System.out.println("Error en analisis sintáctico: No se cerró correctamente el " + tokenDePila.getValor() + " de la linea " + tokenDePila.getLinea() + ".");
				analisisCorrecto = false;
			}
		}
		
		// para borrar los null que dejaron las expresiones
		for(int i=0; i<tokens.size(); i++) {
			if(tokens.get(i) == null) {
				tokens.remove(i--);
			}
			else {
				tokens.get(i).setId(i);
			}
		}
		
		if(analisisCorrecto)
			listaDeImpresiones.add(Statics.getHTML("<var>Código sin errores sintácticos", Statics.consolaCss));
		return analisisCorrecto;
	}
}
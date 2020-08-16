// nada tiene que ver con el compilador ni con el Crack's Code, sólo fue creado a modo de práctica y no sabía donde meterlo xd
package misc;

import java.util.Scanner;
import java.util.Stack;

public class CambioDeNotacion {
	final String caracteresValidos = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789";
	final String operadoresValidos = "+-/*^";
	final String parentesisValidos = "()";
	final String todo = caracteresValidos + operadoresValidos + parentesisValidos;
	Scanner lee;
	public static void main(String[] args) {
		new CambioDeNotacion();
	}
	public CambioDeNotacion() {
		lee = new Scanner(System.in);
		String respuesta = "";
		while(true) {
			System.out.print("Escriba su expresión infija: ");
			respuesta = lee.nextLine();
			if(parentesisCorrectos(respuesta) && respuesta.length() > 0 && !respuesta.equalsIgnoreCase("salir")) {
				System.out.println("Notación postfija: "+aPostfija(respuesta));
				System.out.println("Notación prefija: "+aPrefija(respuesta));
			}
			else if(respuesta.equalsIgnoreCase("salir"))
				break;
			else if(!parentesisCorrectos(respuesta))
				System.out.println("Parentesis mal puestos");
			else
				System.out.println("Escriba su expresión pls, o \"salir\" para terminar el programa");
			ponBarras();
		}
		ponBarras();
		System.out.print("Programa terminado");
	}
	private void ponBarras() {
		System.out.println();
		for (int i = 0; i < 50; i++) {
			System.out.print("=");
		}
		System.out.println("\n");
	}
	private String aPostfija(String respuesta) {
		String salida = "";
		Stack<String> pila = new Stack<String>();
		for(int i=0; i<respuesta.length(); i++) {
			String datoActual = "" + respuesta.charAt(i);
			if(todo.contains(datoActual)) {
				if(caracteresValidos.contains(datoActual)) {
					salida+= datoActual + ", ";
				}
				else if(operadoresValidos.contains(datoActual)) {
					if(!pila.isEmpty()) {
						if(nivelDePemdas(datoActual) == nivelDePemdas(pila.peek())) {
							// se cambia el tope de la pila por el operando actual
							salida+= pila.peek() + ", ";
							pila.pop();
							pila.push(datoActual);
						}
						else if(nivelDePemdas(datoActual) > nivelDePemdas(pila.peek())) {
							// se agrega a la pila
							pila.push(datoActual);
						} else if(nivelDePemdas(datoActual) < nivelDePemdas(pila.peek())) {
							// saca operadores
							while(!pila.isEmpty()) {
								salida+= pila.peek() + ", ";
								pila.pop();
							}
							pila.push(datoActual);
						}
					}
					else {
						pila.push(datoActual);
					}
				}
				else if(parentesisValidos.contains(datoActual)) {
					// vacia la pila
					if(")".contains(datoActual)) {
						try {
							while(!"(".contains(pila.peek())) {
								salida+= pila.peek() + ", ";
								pila.pop();
							}
							pila.pop();
						}
						catch(Exception e) {
							return "error de parentesis";
						}
					}
					else if("(".contains(datoActual)) {
						pila.push(datoActual);
					}
				}
			}
			else 
				return "Expresión con caracteres inválidos";
		}
		while(!pila.isEmpty()) {
			salida+= pila.peek();
			if(pila.size() > 1) {
				 salida+= ", ";
			}
			pila.pop();
		}
		return salida;
	}
	private String aPrefija(String respuesta) {
		String salida = "";
		Stack<String> pila = new Stack<String>();
		for(int i=respuesta.length()-1; i>=0; i--) {
			String datoActual = "" + respuesta.charAt(i);
			if(todo.contains(datoActual)) {
				if(caracteresValidos.contains(datoActual)) {
					salida+= datoActual + " ,";
				}
				else if(operadoresValidos.contains(datoActual)) {
					if(!pila.isEmpty()) {
						if(nivelDePemdas(datoActual) == nivelDePemdas(pila.peek())) {
							// se cambia el tope de la pila por el operando actual
							salida+= pila.peek() + " ,";
							pila.pop();
							pila.push(datoActual);
						}
						else if(nivelDePemdas(datoActual) > nivelDePemdas(pila.peek())) {
							// se agrega a la pila
							pila.push(datoActual);
						} else if(nivelDePemdas(datoActual) < nivelDePemdas(pila.peek())) {
							// saca operadores
							while(!pila.isEmpty()) {
								salida+= pila.peek() + " ,";
								pila.pop();
							}
							pila.push(datoActual);
						}
					}
					else {
						pila.push(datoActual);
					}
				}
				else if(parentesisValidos.contains(datoActual)) {
					// vacia la pila
					if("(".contains(datoActual)) {
						try {
							while(!")".contains(pila.peek())) {
								salida+= pila.peek() + " ,";
								pila.pop();
							}
							pila.pop();
						}
						catch(Exception e) {
							return "error de parentesis";
						}
					}
					else if(")".contains(datoActual)) {
						pila.push(datoActual);
					}
				}
			}
			else 
				return "Expresión con caracteres inválidos";
		}
		while(!pila.isEmpty()) {
			salida+= pila.peek();
			if(pila.size() > 1) {
				 salida+= " ,";
			}
			pila.pop();
		}
		String salidaReal = "";
		for(int i=0; i<salida.length(); i++)
			salidaReal = salida.charAt(i) + salidaReal;
		return salidaReal;
	}
	private int nivelDePemdas(String dato) {
		int valor = 0;
		switch(dato) {
			case "^":
				valor = 3;
				break;
			case "*":
				valor = 2;
				break;
			case "/":
				valor = 2;
				break;
			case "+":
				valor = 1;
				break;
			case "-":
				valor = 1;
				break;
		}
		return valor;
	}
	private boolean parentesisCorrectos(String respuesta) {
		int cerrar = 0;
		int abrir = 0;
		for(int i=0; i<respuesta.length(); i++) {
			if(respuesta.charAt(i) == ')')
				cerrar++;
			else if(respuesta.charAt(i) == '(')
				abrir++;
		}
		return cerrar == abrir;
	}
}
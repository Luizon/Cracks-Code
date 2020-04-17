package compilador;

import java.util.ArrayList;

import estructuraDeDatos.Token;
import misc.Statics;

public class AnalizadorLexico {
	public static boolean analizarToken(String strToken, int renglon, ArrayList<String> impresion, ArrayList<Token> tokens) {
		ArrayList<String> auxiliar;
		int tipo=-1;
		auxiliar = deArrayEstaticaADinamica(Statics.modificador);
		if(auxiliar.contains(strToken)) 
			tipo=0; // Es un modificador
		auxiliar = deArrayEstaticaADinamica(Statics.palabraReservada);
		if(auxiliar.contains(strToken)) 
			tipo =1; // Es una palabra reservada
		auxiliar = deArrayEstaticaADinamica(Statics.tipoDeDato);
		if(auxiliar.contains(strToken)) 
			tipo =2; // Es un tipo de datos
		auxiliar = deArrayEstaticaADinamica(Statics.signo); // cambiar nombre
		if(auxiliar.contains(strToken)) 
			tipo =3; // Es un simbolo
		auxiliar = deArrayEstaticaADinamica(Statics.operadorLogico);
		if(auxiliar.contains(strToken)) 
			tipo =4; // Es un operador logico
		auxiliar = deArrayEstaticaADinamica(Statics.operadorAritmetico);
		if(auxiliar.contains(strToken))
			tipo =5; // Es un operador aritmetico
		auxiliar = deArrayEstaticaADinamica(Statics.booleano);
		if(auxiliar.contains(strToken)) 
			tipo =6; // Es una constante
		if(strToken.equals(Statics.clase)) 
			tipo =7; // Es una clases
		if(esEntero(strToken)) 
			tipo =10; // Es un número entero
		
		if(tipo==-1) { // No es ninguna de arriba
			// Si entra aquí quiere decir que no es ninguna de las anteriores y paso analizarla letra por letra
			String caracter = "";
			String palabra = strToken;
			boolean bandera = true;
			while(palabra.length() > 0) {
				caracter = palabra.charAt(0) + "";
				//			 a				  -			z					,			A				-		   Z				 ,			 ñ			  ,			  Ñ
				if(!(caracter.hashCode()>=97 && caracter.hashCode()<=122)/* && !(caracter.hashCode()>=65 && caracter.hashCode()<=90) && !caracter.equals("ñ") && !caracter.equals("Ñ")*/) {
					bandera = false;
					break;
				}
				palabra = palabra.substring(1, palabra.length());
			}
			if(bandera)
				if(!strToken.startsWith("\"") && !strToken.endsWith("\""))
					tipo = 8; // Es un identificador
				else if(strToken.startsWith("\"") && strToken.endsWith("\""))
					tipo = 9; // Es una cadena de caracteres
				else
					bandera = false;
			if(!bandera) {
				impresion.add(Statics.getHTML("<p>Error en el token <strong>"+strToken+"</strong>",  Statics.consolaCss)); //Es un error y guardo el donde se produjo el error
				return false;
			}
		}
		
		// todo bien, todo nice, entonces creas un token y lo añades a la lista de tokens
		Token newToken = new Token(strToken, tipo, renglon);
		tokens.add(newToken);
		impresion.add(newToken.toString());
		return true;
	}
	
	private static ArrayList<String> deArrayEstaticaADinamica(String [] estatica) {
		ArrayList<String> dinamica = new ArrayList<String>();
		for(String s: Statics.modificador)
			dinamica.add(s);
		return dinamica;
	}
	
	public static boolean esEntero(String token) {
		String caracter;
		for(int i=0; i<token.length(); i++) {
			caracter = token.charAt(i) + "";
			// si un caracter del String no es un valor del 0 al 9 retorna false
			if(caracter.hashCode()<48 || caracter.hashCode()>57)
				return false;
		}
		return true;
	}
}
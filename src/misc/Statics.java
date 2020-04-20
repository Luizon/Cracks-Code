package misc;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Statics {
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////   CONSTANTES DEL PROGRAMA
	///////////////////////////////////////////////////////////////////////////////////
	public final static int
		palabraReservadaInt = 0,
		tipoDeDatoInt = 1,
		signoInt = 2,
		operadorLogicoInt = 3,
		operadorAritmeticoInt = 4,
		booleanoInt = 5,
		claseInt = 6,
		identificadorInt = 7,
		cadenaInt = 8,
		enteroInt = 9,
		parentesisInt = 10,
		llaveInt = 11,
		dobleInt = 12;
	public final static String [] tipoDeToken = {
		"Palabra resevada", // 0
		"Tipo de dato", // 1
		"Signo", // 2
		"Operador logico", // 3
		"Operador aritmetico", // 4
		"Booleano", // 5
		"Clase", // 6
		"Identificador", // 7
		"Cadena", // 8
		"Entero", // 9
		"Parentesis", // 10, ()
		"Llave", // 11, {}
		"Doble", // 12
	};
	public final static String[]
		alcance = {"global", "local"},
		palabraReservada = {"if","while"},
		tipoDeDato = {"int", "string", "boolean", "double", "class", "function", "method"}, // de dato y de otra cosas, por comodidad mía xd
		signo = {"=",";"},
		operadorLogico = {"<","<=",">",">=","==","!="},
		operadorAritmetico = {"+","-","*","/"},
		booleano = {"true","false"},
		parentesis = {"(", ")"},
		llave = {"{", "}"};
	public final static String consolaCss =
			"strong {" // para destacar el token, en el error
			+ "	font-style: italic;"
			+ "}"
			+ "p {" // para el error
			+ "	color: #DD0000"
			+ "}"
			+ "em {" // para el warning
			+ "	color: #888800"
			+ "}"
			+ "var {" // para lo verde bonito acá bien
			+ "	color: #008800"
			+ "}";
	
	///////////////////////////////////////////////////////////////////////////////////
	//////////////////////////   FUNCIONES DE ANALIZADORES
	///////////////////////////////////////////////////////////////////////////////////
	public static ArrayList<String> deArrayEstaticaADinamica(String [] estatica) {
		ArrayList<String> dinamica = new ArrayList<String>();
		for(String s: estatica)
			dinamica.add(s);
		return dinamica;
	}

	public static String [] deArrayDinamicaAEstatica(ArrayList<String> dinamica) {
		String [] estatica = new String[dinamica.size()];
		for(int i=0; i<dinamica.size(); i++)
			estatica[i] = dinamica.get(i);
		return estatica;
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
	public static boolean esDoble(String token) {
		try {
			Double.parseDouble(token);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static int getTipoDeConstante(String str) {
		if(Statics.esEntero(str))
			return 0; // entero
		if(str.endsWith("\"") && str.startsWith("\""))
			return 1; // string
		if("true".equals(str) || "false".equals(str))
			return 2; // booleano
		if(Statics.esDoble(str))
			return 3; // entero
		return -1;
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////   FUNCIONES DE INTERFAZ   
	///////////////////////////////////////////////////////////////////////////////////
	public static void guardarArchivo(String ruta, String texto) {
		try {
			FileWriter archivo = new FileWriter(ruta);
			PrintWriter escritor = new PrintWriter(archivo);
			escritor.println(texto);
			archivo.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getHTML(String body, String css) {
		String output =	
				"<html>"
					+"<head>"
						+"<style>"
							+ css
						+"</style>"
					+"</head>"
					+"<body>"
						+ body
					+"</body>"
				+"</html>";
		return output;
	}
	public static String getImage(String name) {
		String imageRelativeRoute = "";
		switch(name) {
		case "about2":
			imageRelativeRoute = "src/images/iconos/md/about.png";
			break;
		case "aboutb":
			imageRelativeRoute = "src/images/iconos/l/about.png";
			break;
		case "bug":
			imageRelativeRoute = "src/images/iconos/sm/bug.png";
			break;
		case "code":
			imageRelativeRoute = "src/images/iconos/sm/code.png";
			break;
		case "exit2":
			imageRelativeRoute = "src/images/iconos/md/exit.png";
			break;
		case "folder2":
			imageRelativeRoute = "src/images/iconos/md/folder.png";
			break;
		case "error":
			imageRelativeRoute = "src/images/iconos/sm/error.png";
			break;
		case "done":
			imageRelativeRoute = "src/images/iconos/sm/done.png";
			break;
		case "info2":
			imageRelativeRoute = "src/images/iconos/md/info.png";
			break;
		case "infob":
			imageRelativeRoute = "src/images/iconos/l/info.png";
			break;
		case "new2":
			imageRelativeRoute = "src/images/iconos/md/new.png";
			break;
		case "open2":
			imageRelativeRoute = "src/images/iconos/md/open.png";
			break;
		case "recent2":
			imageRelativeRoute = "src/images/iconos/md/recent.png";
			break;
		case "run2":
			imageRelativeRoute = "src/images/iconos/md/run.png";
			break;
		case "save2":
			imageRelativeRoute = "src/images/iconos/md/save.png";
			break;
		case "saveAs2":
			imageRelativeRoute = "src/images/iconos/md/saveAs.png";
			break;
		case "settings2":
			imageRelativeRoute = "src/images/iconos/md/settings.png";
			break;
		case "table":
			imageRelativeRoute = "src/images/iconos/sm/table.png";
			break;
		case "theme2":
			imageRelativeRoute = "src/images/iconos/md/theme.png";
			break;
		}
		if(imageRelativeRoute.length() > 0) {
			File read = new File(imageRelativeRoute);
			return read.getAbsolutePath();
		}
		return null;
	}
}

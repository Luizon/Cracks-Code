package misc;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Statics {
	public static String [] tipoDeToken = {
		"Modificador", // 0
		"Palabra resevada", // 1
		"Tipo de dato", // 2
		"Signo", // 3
		"Operador logico", // 4
		"Operador aritmetico", // 5
		"Booleano", // 6
		"Clase", //7
		"Identificador", // 8
		"Cadena", //9
		"Entero", //10
	};
	public static String[] // basado en el viejo compilador
		modificador = {"public", "private"}, // 0
		palabraReservada = {"if","while"}, // 1
		tipoDeDato = {"int", "string", "boolean"}, // 2
		signo = {"(",")","{","}","=",";"}, // 3
		operadorLogico = {"<","<=",">",">=","==","!="}, // 4
		operadorAritmetico = {"+","-","*","/"}, // 5
		booleano = {"true","false"}; // 6
	public static String clase = "class"; // 7
	public static String consolaCss =
			"strong {" // para destacar el token, en el error
			+ "	font-style: italic;"
			+ "}"
			+ "p {" // para el error
			+ "	color: #DD0000"
			+ "}";
	
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

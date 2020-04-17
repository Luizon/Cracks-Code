package ejemploDelProfe;

import java.util.HashMap;

/**
 * @author Rodrigo
 */
public class EjercicioTablaSimbolos {
    public static void main(String[] args) {
        HashMap<String,TuplaSimbolo> tablaSimbolos = new HashMap<String,TuplaSimbolo>();
        TuplaSimbolo simboloAtributos; 
        //int a = 111;
        //int a;
        //int b = 222;
        //int c = 333;
        //String x = 'Rodrigo';
        //a = b+c;
        //a= b*x;
        //z= x;
        
        tablaSimbolos.put("a", new TuplaSimbolo("a", "Operando", "Entero", 1,"111"));
        tablaSimbolos.put("b", new TuplaSimbolo("b", "Operando", "Entero", 2, "222"));
        tablaSimbolos.put("c", new TuplaSimbolo("c", "Operando", "Entero", 3, "333"));
        tablaSimbolos.put("x", new TuplaSimbolo("x", "Operando", "String", 4, "Rodrigo"));
        
	   int columna1 = 20,
		   columna2 = 15,
		   columna3 = 15,
		   columna4 = 15,
		   columna5 = 15;
	   int [] tamañoColumnas = new int[] {columna1, columna2, columna3, columna4, columna5};

       // poner cabezal de la tabla
       	ponCabecera(new String[]{"Simbolo", "Valor", "Posición", "Rol", "Tipo"}, tamañoColumnas);
        
        // añadir tuplas a la tabla
         simboloAtributos =  tablaSimbolos.get("a");
         añadeTupla(simboloAtributos, tamañoColumnas);
        simboloAtributos.setValor("3");
        añadeTupla(simboloAtributos, tamañoColumnas);
         tablaSimbolos.get("a").setValor("4");
         añadeTupla(simboloAtributos, tamañoColumnas);
         
         simboloAtributos =  tablaSimbolos.get("b");
         añadeTupla(simboloAtributos, tamañoColumnas);
         
         simboloAtributos =  tablaSimbolos.get("c");
         añadeTupla(simboloAtributos, tamañoColumnas);
         
         //simboloAtributos =  tablaSimbolos.get("a");
         
         
         simboloAtributos =  tablaSimbolos.get("x");
         simboloAtributos.getTipo();
         
         añadeTupla(simboloAtributos, tamañoColumnas);
         
         // escribir fondo de la tabla
         System.out.print("└");
         for(int i=0; i<columna1; i++)
         	System.out.print("─");
         System.out.print("┴");
         for(int i=0; i<columna2; i++)
        	 System.out.print("─");
         System.out.print("┴");
         for(int i=0; i<columna3; i++)
        	 System.out.print("─");
         System.out.print("┴");
         for(int i=0; i<columna4; i++)
        	 System.out.print("─");
         System.out.print("┴");
         for(int i=0; i<columna5; i++)
        	 System.out.print("─");
         System.out.print("┘");
         System.out.println();
    }
    
    static public void ponCabecera(String [] titulo, int[] tamañoColumnas) {
    	System.out.print("┌");
        for(int i=0; i<tamañoColumnas[0]; i++)
        	System.out.print("─");
        System.out.print("┬");
        for(int i=0; i<tamañoColumnas[1]; i++)
       	 System.out.print("─");
        System.out.print("┬");
        for(int i=0; i<tamañoColumnas[2]; i++)
       	 System.out.print("─");
        System.out.print("┬");
        for(int i=0; i<tamañoColumnas[3]; i++)
       	 System.out.print("─");
        System.out.print("┬");
        for(int i=0; i<tamañoColumnas[4]; i++)
       	 System.out.print("─");
        System.out.print("┐");
        System.out.println();
         System.out.println("|"+ponEspacios(titulo[0], tamañoColumnas[0])
         		+"|"+ponEspacios(titulo[1], tamañoColumnas[1])
         		+"|"+ponEspacios(titulo[2], tamañoColumnas[2])
         		+"|"+ponEspacios(titulo[3], tamañoColumnas[3])
         		+"|"+ponEspacios(titulo[4], tamañoColumnas[4])+"|");
         System.out.print("├");
         for(int i=0; i<tamañoColumnas[0]; i++)
         	System.out.print("─");
         System.out.print("┼");
         for(int i=0; i<tamañoColumnas[1]; i++)
        	 System.out.print("─");
         System.out.print("┼");
         for(int i=0; i<tamañoColumnas[2]; i++)
        	 System.out.print("─");
         System.out.print("┼");
         for(int i=0; i<tamañoColumnas[3]; i++)
        	 System.out.print("─");
         System.out.print("┼");
         for(int i=0; i<tamañoColumnas[4]; i++)
        	 System.out.print("─");
         System.out.print("┤");
         System.out.println();
    }
    
    static public void añadeTupla(TuplaSimbolo simboloAtributos, int[] tamañoColumnas) {
        System.out.println("|"+ponEspacios(simboloAtributos.getSimbolo(), tamañoColumnas[0])
        		+"|"+ponEspacios(simboloAtributos.getValor(), tamañoColumnas[1])
        		+"|"+ponEspacios(simboloAtributos.getPosicion()+"", tamañoColumnas[2])
        		+"|"+ponEspacios(simboloAtributos.getRol(), tamañoColumnas[3])
        		+"|"+ponEspacios(simboloAtributos.getTipo(), tamañoColumnas[4])+"|");
    }
    
    static public String ponEspacios(String texto, int espacios) {
    	if(texto.length() < espacios)
    		for(int i=espacios-texto.length(); i>0; i--) {
    			if(i%2 == 0)
    				texto = " "+texto;
    			else
    				texto+= " ";
    		}
    	return texto;
    }
    
}

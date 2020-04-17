package ejemploDelProfe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Rodrigo
 */
public class AnalisisSemantico {
    
    private HashMap<String,TuplaSimbolo> tablaSimbolo;
    private ArrayList<String> listaErroresSemanticos;
    private ArrayList<String> operadoresAritmeticos; //Llenarlos
    private ArrayList<String> operadoresLogicos;//Llenarlos
    
    public void ejecutarAnalisisSemantico(/*Recibir archivo o texto*/){
        int numerorRenglon;
        /*Iteraci�n para recorrer el archivo o texto.
            -Incrementar numeroRenglon;
            -Mandar llamar el metodo validarTipoAcci�n enviandole el renglon.
            -Cachar la respuesta de validarTipoAcci�n
            -Si es "declaraci�n" entonces
                -Mandar llamar funciòn insertarDeclaraci�n enviandole el texto del renglon y el numeroRenglon.
            -Si no
                -Mandar llamar funci�n validarOperaci�n enviandole el texto del 
                 renglon y el numeroRenglon. 
        -Recorrer/immprimir la tabla de simbolos
        -Imprimir los erroree
                
        */
    }
    
    public void validarOperaci�n(String renglon, int numeroRenglon){
        /*- Lista de tipo atributoSimbolos;
            -Separar los elementos de la declaraci�n, ejemplo a = var1 + var2,
           tiene tres items
             se omiten los operadores, puedes utilizar un arraylist
             para guardarlo;
             -Obtener los operadores que se estan utiliando en toda la expresi�n.
             -Es importante validar si el item tiene puros númetos o inicia y termina 
              con "" comillas
              para no validarlo en la tabla de simbolos, estos items se pueden meter
              en una lista para
              validarlos tambien, si contiene puros numeros sin punto, es entero,
               si contiene punto es flotante
              si esta entre comillas manejarlo como string.
            -Realizar iteraci�n del arraylist de los operandos.
                -Mandar llamar funci�n encontrarSimbolo con el simbolo que tiene la iteraci�n.
                -Si la funci�n regresa falso
                    -Insertar en listaErroresSemanticos que no existe el simbolo, con los datos pedidos por el profesor
                -Si no
                   -Obtener los datos del simbolo con el metodo obtenerDatosSimbolos, descartar las constantes.
                -  -Grabar los datos en la lista atributosSimbolos
            -Termina iteraci�n
            -Mandar llamar la funci�n validarTipodeDatosOperandos enviandole la lista de los atributos.
            -Mandar llamar la funci�n validarTipodeDatosCostantes enviandole la lista de los atributos.
            -Mandar llamar la funci�n validarTipoOperadores enviandole la lista de atributos.
        */
        
    }
    
    public void validarTipoOperadores(ArrayList<TuplaSimbolo> atributo, ArrayList<String> operadoresRenglon){
        /*
            -Obtener valor de un atributo, para conocer el tipo de dato que estamos usando.
            -Dependiendo del tipo de dato, se va tomar la lista de operadores logicos o aritmeticos.
            -Validar que todos los operadores de la operaci�n correspondan a  las del contexto. de la
            lista operadoresRenglon.
        */
    }
    
    public void validarTipodeDatosCostantes(ArrayList<TuplaSimbolo> atributo,ArrayList<String> listaConstantes){
        /*
            -Realizar iteraci�n de contastes.
            -Validar que exista el tipo de dato de la constante dentro de los atributos de los simbolos.
            -Si no existe
                -Grabar en la tabla de errores que hay inconcistencias entre tipos.
        */
    }
    
    public boolean validarTipodeDatosOperandos(ArrayList<TuplaSimbolo> atributos){
        boolean regresa = true;
        /*
            -Realizar iteraci�n de lal arraylist de los atributos
            -Ir validando si los tipos tipos son iguales con el anterior.
            -Si no son iguales
                -Grabar en la tabla de errores que existe incompatibilidad de tipos.
                -Asignar falso a regresa;
        */
        return regresa;
    }
    
    public void insertarDeclaraci�n(String renglon, int numeroRenglon){
        //Separar los elementos de la declaraci�n, ejemplo int a, tiene dos items;
        //If(!encontrarSimbolo(simbolo))
            //tablaSimbolos.put(simbolo, new TuplaSimbolo(simbolo, "Operando", "Int", numeroRenglon,"0"));
        //else
            //Obtener los valores del simbolo simboloAtributos =  tablaSimbolos.get(simbolo);
            //Insertar en listaErroresSemanticos que ya existe el simbolo, con los datos pedidos por el profesor;
    }
    
    public String  validarTipoAcci�n(String renglon){
        String respuesta="";
        //Validar si es una declaraci�n, si inicia con algún tipo de dato(int, string, boolean, float, etc).
            //Asignar a respuesta "declaraci�n".
        //Validar si es una asignaci�n/Operaci�n(Teniendo el signo igual.
            //Asignar a respuesta "asignaci�n".
        return respuesta;
    }
   
    
    public boolean contieneSimbolo(String simbolo){
        boolean bRegresa = false;
        
         if (tablaSimbolo.containsKey(simbolo))
        {
            bRegresa = true;
        }
        
        return bRegresa;
    }
    
    public TuplaSimbolo obtenerDatosSimbolo(String simbolo){
         TuplaSimbolo atributosSimbolos = new TuplaSimbolo();
         
         return atributosSimbolos;
    }
    
}

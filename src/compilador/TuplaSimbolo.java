package compilador;

/**
 * @author Rodrigo
 */
public class TuplaSimbolo {
	private int linea;
	private String alcance;
	private String tipo;
    private String simbolo;
    private String valor;
    
    public TuplaSimbolo(int linea, String alcance, String tipo, String simbolo, String valor){
    	this.linea = linea;
        this.alcance = alcance;
        this.tipo = tipo;
        this.simbolo = simbolo;
        this.valor = valor;
    }
    
    
    public int getPosicion() {
    	return linea;
    }
    
    public void setPosicion(int linea) {
    	this.linea = linea;
    }
    
    public String getAlcance() {
        return alcance;
    }

    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSimbolo() {
    	return simbolo;
    }
    
    public void setSimbolo(String simbolo) {
    	this.simbolo = simbolo;
    }
    
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
	public String toString() {
		return "Simbolo: "+ simbolo +" ["
			+ "\n\tposición=" + linea + ", "
			+ "\n\talcance=" + alcance + ", "
			+ "\n\ttipo=" + tipo + ", "
			+ "\n\tvalor=" + valor
			+ "\n]";
	}
}

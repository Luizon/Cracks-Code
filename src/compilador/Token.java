package compilador;

import misc.Statics;

public class Token 
{
	private int tipo;
	private String valor;
	private int linea,
		id;
	public Token(int linea, int tipo, int id, String valor) {
		this.tipo=tipo;
		this.valor=valor;
		this.linea=linea;
		this.id=id;
	}
	public int getTipo() {
		return tipo;
	}
	public String getValor() {
		return valor;
	}
	public int getColumna() {
		return id;
	}
	public int getLinea() {
		return linea;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String toString() {
		return "Token " + valor + ":"
			+ "\n{"
			+ "\n\tid: "+id+","
			+ "\n\tlinea: "+linea+","
			+ "\n\ttipo: " + Statics.tipoDeToken[tipo]
			+ "\n}";
	}
}

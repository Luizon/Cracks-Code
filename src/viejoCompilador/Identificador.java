package viejoCompilador;

public class Identificador {

	public String nombre;
	public String valor;
	public String tipo;
	
	public Identificador(String nombre, String valor, String tipo) {
		super();
		this.nombre = nombre;
		this.valor = valor;
		this.tipo = tipo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String toString() {
		return "Identificador [nombre=" + nombre + ", valor=" + valor + ", tipo=" + tipo + "]";
	}
}
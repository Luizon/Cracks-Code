package estructuraDeDatos;
public class NodoDoble<E> {
	public E dato = null;
	int index = -1;
	public NodoDoble<E> siguiente = null;
	public NodoDoble<E> anterior = null;
	public NodoDoble(E dato) {
		this.dato=dato;
	}	
}

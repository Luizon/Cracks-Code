package interfaz;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class CeldaPersonalizada extends DefaultTableCellRenderer {
	int [] color;
	public CeldaPersonalizada(int [] color) {
		this.color = color;
	}
	@Override
	public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean estaSeleccionado, boolean tieneFoco, int fila, int columna) {
		super.getTableCellRendererComponent(tabla, valor, estaSeleccionado, tieneFoco, fila, columna);
		int aux = fila % 3 + 1;//2 - fila % 3;
//		celda.setBackground(new Color(195+30*color[aux], 195+30*color[aux], 195+30*color[aux]));
		setBackground(new Color(255/aux, 125/aux, 200/aux));
		return this;
	}
	
}

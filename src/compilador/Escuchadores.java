package compilador;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.print.attribute.HashAttributeSet;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;

import interfaz.Vista;

public class Escuchadores implements ActionListener, KeyListener, WindowListener, AncestorListener, MouseListener, CaretListener {
	private Vista vista;
	public Escuchadores(Vista vista) {
		this.vista = vista;
	}
	void guardarArchivo(String ruta) {
        try {
        	FileWriter archivo = new FileWriter(ruta);
			PrintWriter escritor = new PrintWriter(archivo);
			
			int tab = vista.codigo.getSelectedIndex() - 1;
			String str = vista.txtCodigo.getByIndex(tab).dato.getDocument().getText(0, vista.txtCodigo.getByIndex(tab).dato.getText().length());
			String salida = "", aux = "";
			while(str.length()>0) { // mientras haya elementos en el texto para añadir al archivo
				aux = str.charAt(0) + ""; // obtienes primer el elemento del texto
				salida+= aux; // lo concatenas a salida
				str = str.substring(1, str.length()); // quitas el primer elemento del texto, para que en la próxima iteración se tome el siguiente elemento
				if(aux.hashCode() == 10) { // si el dato que estás añadiendo es un salto de linea
					escritor.println(salida); // se escribe la linea de todo lo concatenado en ella
					salida = ""; // y se reinicia la variable para concatenar lo de la siguiente linea
				}
			}
			escritor.print(salida); // escribe la última linea del texto
			archivo.close();
		}catch (Exception e) {
        	e.printStackTrace();
	    }
	}
	private void modificaTitulos() {
		if(vista.hayError) {
			switch(vista.consola.getSelectedIndex()) {
			case 1:
				vista.consola.setTitleAt(1, "Consola!");
				vista.consola.setTitleAt(2, "*Datos!");
				break;
			case 2:
				vista.consola.setTitleAt(1, "*Consola!");
				vista.consola.setTitleAt(2, "Datos!");
				break;
			default:
				vista.consola.setTitleAt(1, "*Consola!");
				vista.consola.setTitleAt(2, "*Datos!");
				break;
			}
		}
		else {
			switch(vista.consola.getSelectedIndex()) {
			case 1:
				vista.consola.setTitleAt(1, "Consola");
				vista.consola.setTitleAt(2, "*Datos");
				break;
			case 2:
				vista.consola.setTitleAt(1, "*Consola");
				vista.consola.setTitleAt(2, "Datos");
				break;
			default:
				vista.consola.setTitleAt(1, "*Consola");
				vista.consola.setTitleAt(2, "*Datos");
				break;
			}
		}
	}
	private void nuevaPestaña() {
		if(vista.codigo.getSelectedIndex() == 0 || vista.teclaControl) {
			vista.nuevaPestaña();
		}
	}
	private void cerrarPestaña() {
		if(vista.teclaControl) {
			vista.cerrarPestaña();
		}
	}
	@Override
	public void keyPressed(KeyEvent key) {
		if(key.getKeyCode() == KeyEvent.VK_ALT) {
			key.consume();
			vista.teclaAlt = true;
			return;
		}
		vista.teclaAlt = false;
		switch(key.getKeyCode()) {
			case KeyEvent.VK_S:
				if(vista.teclaControl) {
					if(vista.teclaShift)
						vista.guardarComo.doClick();
					else
						vista.guardarArchivo.doClick();
					vista.teclaControl = false;
					vista.teclaShift = false;
				}
			break;
			case KeyEvent.VK_O:
				if(vista.teclaControl) {
					vista.cargarArchivo.doClick();
					vista.teclaControl = false;
					vista.teclaShift = false;
				}
			break;
			case KeyEvent.VK_F1:
				vista.consola.setSelectedIndex(0);
			break;
			case KeyEvent.VK_F2:
				vista.consola.setSelectedIndex(1);
			break;
			case KeyEvent.VK_F3:
				vista.consola.setSelectedIndex(2);
			break;
			case KeyEvent.VK_ESCAPE:
				if(vista.getExtendedState() == Frame.MAXIMIZED_BOTH) {
					vista.setExtendedState(Frame.NORMAL);
		        }
				else {
		            vista.setExtendedState(Frame.MAXIMIZED_BOTH);
		        }
			break;
			case KeyEvent.VK_F5:
				vista.compilarCodigo.doClick();
				vista.teclaControl = false;
				vista.teclaShift = false;
			break;
			case KeyEvent.VK_N:
				if(vista.teclaControl) {
					nuevaPestaña();
				}
				break;
			case KeyEvent.VK_W:
				if(vista.teclaControl) {
					vista.nuevoArchivo.doClick();
				}
				break;
			case KeyEvent.VK_PERIOD:
				if(vista.teclaControl)
					if(vista.codigo.getTabCount()>2)
						if(vista.codigo.getSelectedIndex()==vista.codigo.getTabCount()-1)
							vista.codigo.setSelectedIndex(1);
						else
							vista.codigo.setSelectedIndex(vista.codigo.getSelectedIndex()+1);
				break;
			case KeyEvent.VK_COMMA:
				if(vista.teclaControl)
					if(vista.codigo.getTabCount()>2) {
						if(vista.codigo.getSelectedIndex()==1)
							vista.codigo.setSelectedIndex(vista.codigo.getTabCount()-1);
						else
							vista.codigo.setSelectedIndex(vista.codigo.getSelectedIndex()-1);
					}
				break;
			case KeyEvent.VK_TAB:
				
			break;
			case KeyEvent.VK_ENTER:
				if(vista.teclaControl && vista.teclaShift)
					if(vista.consola.getSelectedIndex() < vista.consola.getTabCount()-1)
						vista.consola.setSelectedIndex(vista.consola.getSelectedIndex()+1);
					else
						vista.consola.setSelectedIndex(0);
				else
					for(int i=0; i<vista.tab; i++) {
						final JTextArea aux = vista.txtCodigo.getByIndex(vista.codigo.getSelectedIndex()-1).dato;
						aux.setText(aux.getText()+"\t");
					}
			break;
			case KeyEvent.VK_CONTROL:
				vista.teclaControl = true;
			break;
			case KeyEvent.VK_SHIFT:
				vista.teclaShift = true;
			break;
				
		}
	}
	@Override
	public void keyReleased(KeyEvent key) {
		final int tab;
		if(vista.codigo.getTabCount()>2)
			tab = vista.codigo.getSelectedIndex() - 1;
		else
			tab = vista.codigo.getSelectedIndex();
		if(key.getSource() == vista.txtCodigo.getByIndex(tab).dato) { // Si estás en el editor de código
			if(vista.txtCodigo.getByIndex(tab).dato.getText().length() != vista.tamañoTextoDelEditor.getByIndex(tab).dato) { // entra sólo si escribiste sobre txtCodigo
				if(vista.rutaDeArchivoActual.getByIndex(tab).dato.length() == 0)
					vista.setTitle("*archivo no guardado - CRACK'S Code");
				else
					vista.setTitle("*" + vista.nombreDelArchivo.getByIndex(tab).dato + " - " + vista.rutaDeArchivoActual.getByIndex(tab).dato + " - CRACK'S Code");
				vista.tituloVentana.getByIndex(tab).dato = vista.getTitle();
				vista.codigo.setTitleAt(vista.codigo.getSelectedIndex(), "*" + vista.nombreDelArchivo.getByIndex(tab).dato);
				vista.tamañoTextoDelEditor.getByIndex(tab).dato = vista.txtCodigo.getByIndex(tab).dato.getText().length();
				vista.cambiosGuardados.getByIndex(tab).dato = false;
			}
		}
		switch(key.getKeyCode()) {
			case KeyEvent.VK_ALT:
				key.consume();
				if(!vista.teclaAlt)
					return;
				if(vista.barraDelMenu.isFocusOwner())
					vista.barraDelMenu.transferFocus();
				else
					vista.menuArchivo.doClick();
			break;
			case KeyEvent.VK_CONTROL:
				vista.teclaControl = false;
			break;
			case KeyEvent.VK_SHIFT:
				vista.teclaShift = false;
			break;	
		}
	}
	@Override
	public void keyTyped(KeyEvent key) { // sólo para teclas de escritura; como letras, números, símbolos, etc. No jala con teclas como control, alt, fin, etc.
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == vista.nuevoArchivo) {
			cerrarPestaña();
			return;
		}
		if(evt.getSource() == vista.cargarArchivo) {
			vista.cargarArchivo();
			return;
		}
		if(evt.getSource() == vista.guardarArchivo) {
			if(vista.rutaDeArchivoActual.getByIndex(vista.codigo.getSelectedIndex() - 1).dato.length() == 0)
				vista.guardarArchivoComo();
			else {
				vista.guardarArchivo(vista.rutaDeArchivoActual.getByIndex(vista.codigo.getSelectedIndex() - 1).dato);
			}
			return;
		}
		if(evt.getSource() == vista.guardarComo) {
			vista.guardarArchivoComo();
			return;
		}
		if(evt.getSource() == vista.salir) {
			System.exit(0);
		}
		if(evt.getSource() == vista.compilarCodigo) {
			if(vista.consola.getSelectedIndex() == 0)
				vista.consola.setSelectedIndex(1);
			modificaTitulos();
			System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			int tab = 1;
			if(vista.codigo.getTabCount()>2)
				tab = vista.codigo.getSelectedIndex() - 1;
			guardarArchivo(vista.codigoTemporal.getByIndex(tab).dato.getPath());
			Analizador anal = new Analizador(vista.codigoTemporal.getByIndex(tab).dato.getPath(), vista);
			vista.tokens.setListData(anal.getmistokens().toArray( new String [0]));
			vista.modelo = new DefaultTableModel(new Object[0][0],vista.tituloVentanas);
			vista.tablaDatos.setModel(vista.modelo);
			for (int i = anal.getIdenti().size()-1; i >=0; i--) {
				Identificador id = anal.getIdenti().get(i);
				if(!id.tipo.equals("")) {
					Object datostabla[]= {id.tipo,id.nombre,id.valor};
					vista.modelo.addRow(datostabla);
				}
			}
			return;
		}
	}
	public void windowActivated(WindowEvent e) {
	}
	@Override
	public void windowClosed(WindowEvent e) {
	}
	@Override
	public void windowClosing(WindowEvent e) {
		int tab = 1, n=0;
		boolean hayArchivosSinGuardar = false;
		if(vista.codigo.getTabCount() == 2 && !vista.cambiosGuardados.getByIndex(1).dato) {
			hayArchivosSinGuardar = true;
			n++;
		}
		else
			while(vista.codigo.getTabCount() > tab) {
				if(!vista.cambiosGuardados.getByIndex(tab-1).dato) {
					hayArchivosSinGuardar = true;
					n++;
				}
				tab++;
			}
		if(hayArchivosSinGuardar) {
			String texto;
			if(n == 1)
				texto = "No ha guardado los cambios realizados en un código";
			else
				texto = "No ha guardado los cambios realizados en " + n + " códigos";
			texto+= "\n¿Desea guardar los cambios antes de salir?";
			int opcion = JOptionPane.showConfirmDialog(null, texto, "Cambios no guardados", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if(opcion == JOptionPane.YES_OPTION)
				vista.guardarArchivo.doClick();
			else
				if(opcion == JOptionPane.NO_OPTION)
					System.exit(0);
		}
		else
			System.exit(0);
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
	}
	@Override
	public void windowIconified(WindowEvent e) {
	}
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void ancestorAdded(AncestorEvent a) {
		switch(vista.consola.getSelectedIndex()) {
		case 1:
			if(vista.hayError)
				vista.consola.setTitleAt(1, "Consola!");
			else
				vista.consola.setTitleAt(1, "Consola");
			break;
		case 2:
			if(vista.hayError)
				vista.consola.setTitleAt(2, "Datos!");
			else
				vista.consola.setTitleAt(2, "Datos");
			break;
		case 0:
			if(vista.hayError) {
				vista.consola.setTitleAt(2, "Datos!");
				vista.consola.setTitleAt(1, "Consola!");
			}
			break;
		}
		if(vista.codigo.getSelectedIndex()==0)
			return;
		int tab = 1;
		if(vista.codigo.getTabCount() > 2)
			tab = vista.codigo.getSelectedIndex() - 1;
		vista.setTitle(vista.tituloVentana.getByIndex(tab).dato);
	}
	@Override
	public void ancestorMoved(AncestorEvent a) {
	}
	@Override
	public void ancestorRemoved(AncestorEvent a) {
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		nuevaPestaña();
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	
	@Override
	public void caretUpdate(CaretEvent arg0) {
		// escuchador para la auto indentación
	}
	
}
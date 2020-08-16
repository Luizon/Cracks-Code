package interfaz;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import misc.Statics;

@SuppressWarnings("serial")
public class Escuchadores implements Serializable, ActionListener, KeyListener, WindowListener, AncestorListener, MouseListener {
	private Vista vista;
	public boolean teclaControl = false,
		teclaShift = false,
		teclaAlt = false;
	public Escuchadores(Vista vista) {
		this.vista = vista;
	}
	private void cerrarPestaña() {
		if(teclaControl) {
			vista.cerrarPestaña();
		}
	}
	@Override
	public void keyPressed(KeyEvent key) {
		if(key.getKeyCode() == KeyEvent.VK_ALT) {
			key.consume();
			teclaAlt = true;
			return;
		}
		teclaAlt = false;
		switch(key.getKeyCode()) {
			case KeyEvent.VK_S:
				if(teclaControl) {
					if(teclaShift)
						vista.guardarComo.doClick();
					else
						vista.guardarArchivo.doClick();
					teclaControl = false;
					teclaShift = false;
				}
				break;
			case KeyEvent.VK_O:
				if(teclaControl) {
					vista.cargarArchivo.doClick();
					teclaControl = false;
					teclaShift = false;
				}
				break;
			case KeyEvent.VK_F1:
				vista.ayuda.doClick();
				break;
			case KeyEvent.VK_F10:
				vista.personalizarTema.doClick();
				break;
			case KeyEvent.VK_F11:
				vista.cambiarTema.doClick();
				break;
			case KeyEvent.VK_F12:
				vista.acercaDe.doClick();
				break;
			case KeyEvent.VK_ESCAPE:
				if(vista.getExtendedState() == Frame.MAXIMIZED_BOTH) {
					vista.setExtendedState(Frame.NORMAL);
		        }
				else {
		            vista.setExtendedState(Frame.MAXIMIZED_BOTH);
		        }
				break;
			case KeyEvent.VK_N:
				if(teclaControl) {
					vista.nuevoArchivo.doClick();
				}
				break;
			case KeyEvent.VK_W:
				if(teclaControl) {
					cerrarPestaña();
				}
				break;
			case KeyEvent.VK_PERIOD:
				if(teclaControl)
					if(vista.codigoTabs.getTabCount()>2)
						if(vista.getSelectedTab()==vista.codigoTabs.getTabCount()-2)
							vista.codigoTabs.setSelectedIndex(0);
						else
							vista.codigoTabs.setSelectedIndex(vista.getSelectedTab()+1);
				break;
			case KeyEvent.VK_COMMA:
				if(teclaControl)
					if(vista.codigoTabs.getTabCount()>2) {
						if(vista.getSelectedTab()==0)
							vista.codigoTabs.setSelectedIndex(vista.codigoTabs.getTabCount()-2);
						else
							vista.codigoTabs.setSelectedIndex(vista.getSelectedTab()-1);
					}
				break;
			case KeyEvent.VK_ENTER:
				for(int i=0; i<vista.tabulaciones; i++) {
					final JTextPane aux = vista.txtCodigo.getByIndex(vista.getSelectedTab()-1).dato;
					aux.setText(aux.getText()+"\t");
				}
				break;
			case KeyEvent.VK_CONTROL:
				teclaControl = true;
				break;
			case KeyEvent.VK_SHIFT:
				teclaShift = true;
				break;
			/*case KeyEvent.VK_ADD:
				System.out.println("Datos: \n"
				+ "[\n"
				+ "\ttitulos: " + vista.titulo.length() + "\n"
				+ "\trutas de archivo actual: " + vista.rutaDeArchivoActual.length() + "\n"
				+ "\tnombres de archivos: " + vista.nombreDelArchivo.length() + "\n"
				+ "\ttitulos de ventana: " + vista.tituloVentana.length() + "\n"
				+ "\ttamaños de textos del editor: " + vista.tamañoTextoDelEditor.length() + "\n"
				+ "\tcambios guardados: " + vista.cambiosGuardados.length() + "\n"
				+ "]");
				break;
				/**/
		}
	}
	@Override
	public void keyReleased(KeyEvent key) {
		final int tabs = vista.getSelectedTab();
		if(key.getSource() == vista.txtCodigo.getByIndex(tabs).dato) { // Si estás en el editor de código
			if(vista.txtCodigo.getByIndex(tabs).dato.getText().length() != vista.tamañoTextoDelEditor.getByIndex(tabs).dato) { // entra sólo si escribiste sobre txtCodigo
				if(vista.rutaDeArchivoActual.getByIndex(tabs).dato.length() == 0)
					vista.setTitle("*archivo no guardado - CRACK'S Code");
				else
					vista.setTitle("*" + vista.nombreDelArchivo.getByIndex(tabs).dato + " - " + vista.rutaDeArchivoActual.getByIndex(tabs).dato + " - CRACK'S Code");
				vista.tituloVentana.getByIndex(tabs).dato = vista.getTitle();
				vista.codigoTabs.setTitleAt(vista.getSelectedTab(), "*" + vista.nombreDelArchivo.getByIndex(tabs).dato);
				vista.tamañoTextoDelEditor.getByIndex(tabs).dato = vista.txtCodigo.getByIndex(tabs).dato.getText().length();
				vista.cambiosGuardados.getByIndex(tabs).dato = false;
			}
		}
		switch(key.getKeyCode()) {
			case KeyEvent.VK_ALT:
				key.consume();
				if(!teclaAlt)
					return;
				if(vista.barraDelMenu.isFocusOwner())
					vista.txtCodigo.getByIndex(vista.getSelectedTab()).dato.requestFocus();
				else
					vista.menuArchivo.doClick();
				break;
			case KeyEvent.VK_CONTROL:
				teclaControl = false;
				break;
			case KeyEvent.VK_SHIFT:
				teclaShift = false;
				break;	
		}
	}
	@Override
	public void keyTyped(KeyEvent key) { // sólo para teclas de escritura; como letras, nÃºmeros, sÃ­mbolos, etc. No jala con teclas como control, alt, fin, etc.
		int tabs = vista.codigoTabs.getSelectedIndex();
		String archivoNombre = vista.nombreDelArchivo.getByIndex(tabs).dato;
		if(teclaControl)
			return;
		vista.titulo.getByIndex(tabs).dato.setTitle("*" + archivoNombre);
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == vista.nuevoArchivo) {
			vista.nuevaPestaña();
			return;
		}
		if(evt.getSource() == vista.cargarArchivo) {
			vista.cargarArchivo();
			return;
		}
		if(evt.getSource() == vista.guardarArchivo) {
			if(vista.rutaDeArchivoActual.getByIndex(vista.getSelectedTab()).dato.length() == 0)
				vista.guardarArchivoComo();
			else {
				vista.guardarArchivo(vista.rutaDeArchivoActual.getByIndex(vista.getSelectedTab()).dato);
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
		if(evt.getSource() == vista.personalizarTema) {
			vista.personalizarTema();
			return;
		}
		if(evt.getSource() == vista.cambiarTema) {
			vista.cambiarTema();
			return;
		}
		if(evt.getSource() == vista.ayuda) {
			new Ayuda();
			return;
		}
		if(evt.getSource() == vista.acercaDe) {
			new AcercaDe();
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
		int tabs = 0, archivosSinGuardar=0;
		boolean hayArchivosSinGuardar = false;
		if(vista.codigoTabs.getTabCount() == 2 && !vista.cambiosGuardados.getByIndex(0).dato) {
			hayArchivosSinGuardar = true;
			archivosSinGuardar++;
		}
		else
			while(vista.codigoTabs.getTabCount() > tabs+1) {
				if(!vista.cambiosGuardados.getByIndex(tabs).dato) {
					hayArchivosSinGuardar = true;
					archivosSinGuardar++;
				}
				tabs++;
			}
		if(hayArchivosSinGuardar) {
			String texto = "<div>";
			if(archivosSinGuardar == 1)
				texto+= "No ha guardado los cambios realizados en <b>un archivo</b>";
			else
				texto+= "No ha guardado los cambios realizados en <b>" + archivosSinGuardar + " archivos</b>";
			texto+= "<br />¿Desea guardar los cambios antes de salir?";
			
			teclaShift = teclaAlt = teclaControl = false;
			String css = 
					"div {"
					+ "	text-align: center;"
					+ "	font-weight: plain;"
					+ "}";
			texto = Statics.getHTML(texto, css);
			int opcion = JOptionPane.showConfirmDialog(null, new JLabel(texto), "Cambios no guardados", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if(opcion == JOptionPane.YES_OPTION)
				vista.guardarArchivo.doClick();
			else
				if(opcion == JOptionPane.NO_OPTION) {
					// vista.guardarSesion();
					System.exit(0);
				}
		}
		else {
			// vista.guardarSesion();
			System.exit(0);
		}
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
		int tabs = vista.getSelectedTab();
		String titulo = vista.tituloVentana.getByIndex(tabs).dato;
		vista.setTitle(titulo);
		vista.actualizarBotonesDePestañas();
	}
	@Override
	public void ancestorMoved(AncestorEvent a) {
	}
	@Override
	public void ancestorRemoved(AncestorEvent a) {
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(vista.getSelectedTab()==vista.codigoTabs.getTabCount()-1)
			vista.nuevoArchivo.doClick();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
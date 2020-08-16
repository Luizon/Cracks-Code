package interfaz;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import misc.Statics;

public class Ayuda extends JFrame implements FocusListener, KeyListener {
	public Ayuda() {
		super("Ayuda");
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		setSize(500, 850);
		setLocationRelativeTo(null);
		add(contenido());
		addFocusListener(this);
		addKeyListener(this);
		setUndecorated(true);
		setVisible(true);
	}
	
	private JPanel contenido() {
		JPanel panel = new JPanel();
		final String css =
				  "body {"
				+ "		text-align: center;"
				+ "}"
				+ ".fuenteGris {"
				+ "		font-size: 15px;"
				+ "		font-weight: normal;"
				+ "		color: #888888;"
				+ "}"
				+ ".titulo {"
				+ "		font-size: 30px;"
				+ "		font-weight: bold;"
				+ "		text-decoration: underline;"
				+ "}"
				+ ".importante {"
				+ "		color: #454545;"
				+ "}"
				+ ".salir {"
				+ "		color: #DD8888;"
				+ "}"
				+ ".exit {"
				+ "		color: #BB8888;"
				+ "		font-style: italic;"
				+ "		font-size: 10px;"
				+ "}"
				+ "div {"
				+ "		padding-top: 20px;"
				+ "}";

		final String body =
				"<div width="+(getWidth()-50)+">"
				+ "<img src=\"file:\\"+Statics.getImage("infob")+"\">"
				+ "<br />"
				+ "<span class=\"titulo\">"
				+ "		Acortadores"
				+ "</span>"
				+ "<p class=\"fuenteGris\">"
				+ "		<span class=\"importante\">"
				+ "			<strong>Control + O: </strong>"
				+ "			Abrir archivo<br />"
				+ "			<strong>Control + S: </strong>"
				+ "			Guardar archivo<br />"
				+ "			<strong>Control + Shift + S: </strong>"
				+ "			Guardar archivo como<br />"
				+ "			<strong>F5: </strong>"
				+ "			Compilar código<br />"
				+ "		</span>"
				+ "		<br />"
				+ "		<strong>Control + Shift + entrar: </strong>"								
				+ "		Ir a la siguiente pestaña<br />"
				+ "		<strong>F1: </strong>"
				+ "		Mostrar esta ventana<br />"
				+ "		<strong>F2: </strong>"
				+ "		Mostrar pestaña consola<br />"
				+ "		<strong>F3: </strong>"								
				+ "		Mostrar pestaña datos<br />"
				+ "		<strong>F10: </strong>"
				+ "		Personalizar tema<br />"
				+ "		<strong>F11: </strong>"
				+ "		Cambiar de tema<br />"
				+ "		<strong>F12: </strong>"
				+ "		Mostrar ventana <i>acerca de</i><br />"
				+ "		<strong>Control + coma: </strong>"								
				+ "		Ir al código anterior<br />"
				+ "		<strong>Control + punto: </strong>"								
				+ "		Ir al código siguiente<br />"
				+ "		<strong>Control + N: </strong>"								
				+ "		Nuevo código<br />"
				+ "		<strong>Control + W: </strong>"								
				+ "		Cerrar código actual<br />"
				+ "		<strong>ESC: </strong>"								
				+ "		Pantalla completa/Modo ventana<br />"
				+ "		<br />"
				+ "		<span class=\"salir\">"
				+ "			<strong>Alt + F4: </strong>"
				+ "			Salir"
				+ "		</span>"
				+ "</p>"
				+ "<br />"
				+ "<br />"
				+ "<br />"
				+ "<p class=\"exit\">"
				+ "		Presione cualquier tecla para cerrar esta ventana"
				+ "</p>"
				+"</div>";
		
		JEditorPane editorPane = new JEditorPane("text/html", Statics.getHTML(body, css));
		editorPane.setEditable(false);
		editorPane.setFocusable(false);
		editorPane.setBackground(null);
		panel.add(editorPane);
		return panel;
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		dispose();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		dispose();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}

package interfaz;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import misc.Statics;

public class AcercaDe extends JFrame implements FocusListener, KeyListener, HyperlinkListener {
	public AcercaDe() {
		super("Acerca de");
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		setSize(400, 470);
		setLocationRelativeTo(null);
		add(contenido());
		addFocusListener(this);
		addKeyListener(this);
		setUndecorated(true);
		setVisible(true);
	}
	
	private JPanel contenido() {
		JPanel panel = new JPanel();
		String css =
				"body {"
				+ "		text-align: center;"
				+ "}"
				+ "h1 {"
				+ "		color: #000000;"
				+ "		font-size: 25px;"
				+ "}"
				+ "p {"
				+ "		color: #777777;"
				+ "		font-size: 15px;"
				+ "}"
				+ ".exit {"
				+ "		color: #BB8888;"
				+ "		font-style: italic;"
				+ "		font-size: 10px;"
				+ "}"
				+ "div {"
				+ "		padding-top: 20px;"
				+ "}";
		
		String body =
				"<div width="+(getWidth()-50)+">"
					+ "<img src=\"file:\\"+Statics.getImage("aboutb")+"\">"
					+ "<h1><u>"
						+ "Acerca de"
					+ "</u></h1>"
					+ "<p>"
						+ "Hecho por Luis C�rdenas, le pidieron un compilador y se puso a hacer "
						+ "un editor de texto plano el joven. No me arrepiento, me he entretenido "
						+ "y he aprendido cosas 8]"
					+ "</p>"
					+ "<br />"
					+ "<a href=\"https://github.com/Luizon/\">ver Github del joven</a>"
					+ "<br />"
					+ "<br />"
					+ "<br />"
					+"<p class=\"exit\">"
					+ "		Presione cualquier tecla para cerrar esta ventana"
					+ "</p>"
				+"</div>";
		JEditorPane editorPane = new JEditorPane("text/html", Statics.getHTML(body, css));
		editorPane.setEditable(false);
		editorPane.setFocusable(false);
		editorPane.setBackground(null);
		editorPane.addHyperlinkListener(this);
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

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
			try {
				Runtime.getRuntime().exec("cmd.exe /c start https://github.com/Luizon/");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}
}

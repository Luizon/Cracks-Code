package interfaz;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Theme {
	public final static int
		CLARO = 0,
		OSCURO = 1,
		PERSONALIZADO = 2;
	static private final Color
		ALMOST_WHITE = new Color(215, 215, 215),
		AZUL_CLARO = new Color(184, 207, 229);
	static Color 
		colorBack = Color.DARK_GRAY,
		colorCaret = ALMOST_WHITE,
		colorSelection = Color.DARK_GRAY,
		colorForeground = Color.WHITE,
		colorNegrita = new Color(0, 200, 200),
		colorString = new Color(102, 255, 102),
		colorNumero = new Color(255, 255, 0),
		colorComentario = new Color(230, 0, 0),
		colorLineNumberBack = new Color(127, 127, 230),
		colorLineNumber = new Color(50, 50, 50),
		defaultBack = colorBack,
		defaultCaret = colorCaret,
		defaultSelection = colorSelection,
		defaultForeground = colorForeground,
		defaultNegrita = colorNegrita,
		defaultString = colorString,
		defaultNumero = colorNumero,
		defaultComentario = colorComentario,
		defaultLineNumberBack = colorLineNumberBack,
		defaultLineNumber = colorLineNumber;

	public static void changeTheme(Vista v) {
		for (int i=0; i<v.codigoTabs.getTabCount()-1; i++) {
			changeTheme((CodePane)v.codigoTabs.getComponentAt(i), v.tema);
		}
	}
	
	public static void changeTheme(CodePane panel, int tema) {
		final Color colorBack,
			colorCaret,
			colorSelection,
			colorForeground,
			colorNegrita,
			colorString,
			colorNumero,
			colorComentario,
			colorLineNumberBack,
			colorLineNumber;
		switch(tema) {
			case CLARO:
				colorBack = Color.WHITE;
				colorCaret = Color.BLACK;
				colorSelection = AZUL_CLARO;
				colorForeground = Color.BLACK;
				colorNegrita = new Color(100, 10, 140);
				colorString = new Color(200, 0, 0);
				colorNumero = new Color(0, 50, 0);
				colorComentario = new Color(127, 127, 127);
				colorLineNumberBack = new Color(230, 230, 230);
				colorLineNumber = Color.BLACK;
				break;
			case OSCURO:
				colorBack = Color.BLACK;
				colorCaret = Color.WHITE;
				colorSelection = Color.GRAY;
				colorForeground = ALMOST_WHITE;
				colorNegrita = new Color(150, 17, 255);
				colorString = new Color(230, 0, 0);
				colorNumero = new Color(0, 230, 0);
				colorComentario = new Color(100, 100, 100);
				colorLineNumberBack = new Color(120, 120, 120);
				colorLineNumber = Color.BLACK;
				break;
			default:
				colorBack = Theme.colorBack;
				colorCaret = Theme.colorCaret;
				colorSelection = Theme.colorSelection;
				colorForeground = Theme.colorForeground;
				colorNegrita = Theme.colorNegrita;
				colorString = Theme.colorString;
				colorNumero = Theme.colorNumero;
				colorComentario = Theme.colorComentario;
				colorLineNumberBack = Theme.colorLineNumberBack;
				colorLineNumber = Theme.colorLineNumber;
				break;
		}
		JTextPane codePane = panel.codePane,
			lineNumber = panel.lineNumber;
		String texto = codePane.getText();
		int caretPosition = codePane.getCaretPosition(),
			selectionStart = codePane.getSelectionStart(),
			selectionEnd = codePane.getSelectionEnd();
		codePane.getDocument().removeDocumentListener(panel);
		CodeDocument newDocument = new CodeDocument(codePane, colorForeground, colorNegrita, colorString, colorNumero, colorComentario);
		codePane.setStyledDocument(newDocument);
		codePane.setText(texto);
		codePane.setCaretPosition(caretPosition);
		codePane.setSelectionStart(selectionStart);
		codePane.setSelectionEnd(selectionEnd);
		newDocument.addDocumentListener(panel);
		
		codePane.setBackground(colorBack);
		codePane.setCaretColor(colorCaret);
		codePane.setSelectionColor(colorSelection);
		codePane.setSelectedTextColor(Color.BLACK);

		lineNumber.setBackground(colorLineNumberBack);
		SimpleAttributeSet derecha = new SimpleAttributeSet();
		StyleConstants.setAlignment(derecha, StyleConstants.ALIGN_RIGHT);
		Numeros lineNumberDocument = new Numeros(lineNumber, colorLineNumber);
		lineNumberDocument.setParagraphAttributes(0, lineNumberDocument.getLength(), derecha, false);
		lineNumber.setStyledDocument(lineNumberDocument);
		lineNumber.setText(lineNumber.getText());
	}
}

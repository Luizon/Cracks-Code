package interfaz;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Theme {
	public final static int CLARO = 0, OSCURO = 1;
	static private final Color ALMOST_WHITE = new Color(215, 215, 215);
	static private final Color AZUL_CLARO = new Color(184, 207, 229);
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
			colorLineNumberBack;
		switch(tema) {
			case CLARO:
				colorBack = Color.WHITE;
				colorCaret = Color.BLACK;
				colorSelection = AZUL_CLARO;
				colorForeground = Color.BLACK;
				colorLineNumberBack = new Color(230, 230, 230);
				break;
			default:
				colorBack = Color.BLACK;
				colorCaret = Color.WHITE;
				colorSelection = Color.GRAY;
				colorForeground = ALMOST_WHITE;
				colorLineNumberBack = new Color(120, 120, 120);
				break;
		}
		JTextPane codePane = panel.codePane,
			lineNumber = panel.lineNumber;
		codePane.setBackground(colorBack);
		codePane.setCaretColor(colorCaret);
		codePane.setSelectionColor(colorSelection);
		codePane.setSelectedTextColor(Color.BLACK);
		lineNumber.setBackground(colorLineNumberBack);

		StyledDocument doc = codePane.getStyledDocument();
        Style style = codePane.addStyle("stylename", null);
        StyleConstants.setForeground(style, colorForeground);
        String aux = codePane.getText();
        codePane.setText("");
        try {
        	doc.insertString(0, aux.length() > 0 ? aux : " ", style);
        	if(aux.length()==0)
        		codePane.setText("");
        }
        catch (BadLocationException e) {
        	System.out.println("falló el cambio de tema");
        }
        
//        StyleContext sc = StyleContext.getDefaultStyleContext();
//        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.WHITE);
//        panel.setCharacterAttributes(aset, false);

//		panel.update(panel.getGraphics());
	}
}

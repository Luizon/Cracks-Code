package interfaz;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Theme {
	public final static int CLARO = 0, OSCURO = 1;
	static private final Color NEGRO = Color.BLACK;
	static private final Color BLANCO = Color.WHITE;
	static private final Color ALMOST_WHITE = new Color(215, 215, 215);
	static private final Color AZUL_CLARO = new Color(184, 207, 229);
	public static void darkTheme(Vista v) {
		for (int i=1; i<v.codigo.getTabCount(); i++) {
			changeTheme(v.txtCodigo.getByIndex(i).dato, OSCURO);
		}
	}
	public static void lightTheme(Vista v) {
		for (int i=0; i<v.codigo.getTabCount()-1; i++) {
			changeTheme(v.txtCodigo.getByIndex(i).dato, CLARO);
		}
	}
	public static void changeTheme(JTextPane panel, int tema) {
		final Color colorBack  =  (tema == CLARO)?BLANCO    :NEGRO,
				  colorCaret   =  (tema == CLARO)?NEGRO     :BLANCO,
				  colorSelection= (tema == CLARO)?AZUL_CLARO:Color.GRAY,
				  colorForeground=(tema == CLARO)?NEGRO     :ALMOST_WHITE;
		panel.setBackground(colorBack);
		panel.setCaretColor(colorCaret);
		panel.setSelectionColor(colorSelection);
		panel.setSelectedTextColor(NEGRO);

		StyledDocument doc = panel.getStyledDocument();
        Style style = panel.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, colorForeground);
        String aux = panel.getText();
        panel.setText("");
        try {
        	doc.insertString(0, aux.length()>0?aux:" ", style);
        	if(aux.length()==0)
        		panel.setText("");
        }
        catch (BadLocationException e)
        {}
        
//        StyleContext sc = StyleContext.getDefaultStyleContext();
//        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.WHITE);
//        panel.setCharacterAttributes(aset, false);

		panel.update(panel.getGraphics());
	}
}

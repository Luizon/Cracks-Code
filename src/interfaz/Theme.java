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
			changeTheme(v.txtCodigo.getByIndex(i).dato, v.tema);
		}
	}
	public static void changeTheme(JTextPane panel, int tema) {
		final Color colorBack  =  (tema == CLARO)?Color.WHITE:Color.BLACK,
				  colorCaret   =  (tema == CLARO)?Color.BLACK:Color.WHITE,
				  colorSelection= (tema == CLARO)?AZUL_CLARO :Color.GRAY,
				  colorForeground=(tema == CLARO)?Color.BLACK:ALMOST_WHITE;
		panel.setBackground(colorBack);
		panel.setCaretColor(colorCaret);
		panel.setSelectionColor(colorSelection);
		panel.setSelectedTextColor(Color.BLACK);

		StyledDocument doc = panel.getStyledDocument();
        Style style = panel.addStyle("stylename", null);
        StyleConstants.setForeground(style, colorForeground);
        String aux = panel.getText();
        panel.setText("");
        try {
        	doc.insertString(0, aux.length()>0?aux:" ", style);
        	if(aux.length()==0)
        		panel.setText("");
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

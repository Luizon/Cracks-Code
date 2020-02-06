package interfaz;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class Theme {
	public final static int CLARO = 0, OSCURO = 1;
	static private final Color NEGRO = Color.BLACK;
	static private final Color BLANCO = Color.WHITE;
	static private final Color ALMOST_WHITE = new Color(215, 215, 215);
	static private final Color AZUL_CLARO = new Color(184, 207, 229);
	public static void darkTheme(Vista v) {
		for (int i=0; i<v.codigo.getTabCount(); i++) {
			v.txtCodigo.getByIndex(i).dato.setBackground(NEGRO);
			v.txtCodigo.getByIndex(i).dato.setCaretColor(Color.WHITE);
			v.txtCodigo.getByIndex(i).dato.setSelectionColor(Color.GRAY);
			v.txtCodigo.getByIndex(i).dato.setSelectedTextColor(Color.BLACK);

			StyledDocument doc = v.txtCodigo.getByIndex(i).dato.getStyledDocument();
	        Style style = v.txtCodigo.getByIndex(i).dato.addStyle("I'm a Style", null);
	        StyleConstants.setForeground(style, ALMOST_WHITE);
	        String aux = v.txtCodigo.getByIndex(i).dato.getText();
	        v.txtCodigo.getByIndex(i).dato.setText("");
	        try { doc.insertString(0, aux, style);}
	        catch (BadLocationException e){}
	        
//	        StyleContext sc = StyleContext.getDefaultStyleContext();
//	        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.WHITE);
//	        v.txtCodigo.getByIndex(i).dato.setCharacterAttributes(aset, false);

			v.txtCodigo.getByIndex(i).dato.update(v.txtCodigo.getByIndex(i).dato.getGraphics());
		}
	}
	public static void lightTheme(Vista v) {
		for (int i=0; i<v.codigo.getTabCount(); i++) {
			v.txtCodigo.getByIndex(i).dato.setBackground(BLANCO);
			v.txtCodigo.getByIndex(i).dato.setCaretColor(Color.BLACK);
			v.txtCodigo.getByIndex(i).dato.setSelectionColor(AZUL_CLARO);
			v.txtCodigo.getByIndex(i).dato.setSelectedTextColor(Color.BLACK);

			StyledDocument doc = v.txtCodigo.getByIndex(i).dato.getStyledDocument();
			Style style = v.txtCodigo.getByIndex(i).dato.addStyle("I'm a Style", null);
			StyleConstants.setForeground(style, Color.BLACK);
	        String aux = v.txtCodigo.getByIndex(i).dato.getText();
	        v.txtCodigo.getByIndex(i).dato.setText("");
	        try { doc.insertString(0, aux, style);}
			catch (BadLocationException e){}
			
//			StyleContext sc = StyleContext.getDefaultStyleContext();
//			AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
//			v.txtCodigo.getByIndex(i).dato.setCharacterAttributes(aset, false);
			
			v.txtCodigo.getByIndex(i).dato.update(v.txtCodigo.getByIndex(i).dato.getGraphics());
		}
	}
}

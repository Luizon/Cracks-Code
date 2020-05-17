package interfaz;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

@SuppressWarnings("serial")
public class CodeDocument extends DefaultStyledDocument {
	private StyleContext styleContext;
	private AttributeSet attributeSetNegrita,
		attributeSetNormal,
		attributeSetString;
	private ArrayList<PalabraColoreada> palabrasEspeciales;
	public String currentString,
		oldString;
	private JTextPane codePane;
	public static Color colorNormal = new Color(230, 230, 230),
		colorNegrita = new Color(150, 17, 255),//(132, 12, 204),//(66, 6, 102)
		colorString = new Color(230, 0, 0);
	
	public CodeDocument(JTextPane codePane) {
		styleContext = StyleContext.getDefaultStyleContext();
		attributeSetNormal = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, colorNormal);
        attributeSetNegrita = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, colorNegrita);
        attributeSetString = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, colorString);
    }
	public CodeDocument(JTextPane codePane, Color normal, Color negrita, Color string) {
		styleContext = StyleContext.getDefaultStyleContext();
		attributeSetNormal = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, normal);
        attributeSetNegrita = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, negrita);
        attributeSetString = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, string);
        
        this.codePane = codePane;
	}
	
	@Override
	public void insertString(int offs, String string, AttributeSet attributeSet) throws BadLocationException {
		char letra = string.charAt(string.length() - 1);
		
		switch (letra) {
		case '{':
			string += "}";
			break;
		case '(':
			string += ")";
			break;
		}
		
		super.insertString(offs, string, attributeSet);
		switch (letra) {
			case '{':
			case '(':
				codePane.setCaretPosition(offs + 1);
				break;
		}
		actualizaPanel();
	}

	@Override
	public void remove(int arg0, int arg1) throws BadLocationException {
		super.remove(arg0, arg1);
		actualizaPanel();
	}
	private synchronized void actualizaPanel() throws BadLocationException{
		MutableAttributeSet newAttributeSet = null;
		palabrasEspeciales = new ArrayList<PalabraColoreada>();
		colorea();
		newAttributeSet = new SimpleAttributeSet(attributeSetNormal.copyAttributes());
    	StyleConstants.setBold(newAttributeSet, false);
        setCharacterAttributes(0, getText(0, getLength()).length(), newAttributeSet, true);
		
		for (int i = 0; i < palabrasEspeciales.size(); i++) {
			if(palabrasEspeciales.get(i).cadena)
				newAttributeSet = new SimpleAttributeSet(attributeSetString.copyAttributes());
			else
				newAttributeSet = new SimpleAttributeSet(attributeSetNegrita.copyAttributes());
        	StyleConstants.setBold(newAttributeSet, true);
            setCharacterAttributes(palabrasEspeciales.get(i).posicion, palabrasEspeciales.get(i).palabra.length(), newAttributeSet, true);
		}
	}
	
	private void colorea() throws BadLocationException{
		String textoCompleto = getText(0, getLength()),
			textoPorColorear = "";
		currentString = textoCompleto;
		textoCompleto += " ";
		int delimitador =  0;
		
		for (int i = 0; i < textoCompleto.length(); i++) {
			char letra = textoCompleto.charAt(i);
			if( Character.isLetterOrDigit(letra) || letra == '_'){
				textoPorColorear += letra;
			}else if(letra == '"'){
				i++;
				letra = textoCompleto.charAt(i);
				textoPorColorear += letra;
				while(letra != '"'){
					textoPorColorear += letra;
					i++;
					try{
						letra = textoCompleto.charAt(i);	
					}
					catch(StringIndexOutOfBoundsException e){
						break;
					}
				}
				i++;
				try{
					letra = textoCompleto.charAt(i);	
				}
				catch(StringIndexOutOfBoundsException e) {
					letra = textoCompleto.charAt(i-2);
				}
				textoPorColorear += letra;
				delimitador = i;
				if(textoPorColorear.length() > 0){
					//if(palabraReservada(textoPorColorear)){
						palabrasEspeciales.add(new PalabraColoreada(delimitador-textoPorColorear.length(), textoPorColorear, true));
					//}
					textoPorColorear = "";
				}
			}else{
				delimitador = i;
				if(textoPorColorear.length() > 0){
					if(palabraReservada(textoPorColorear)){
						palabrasEspeciales.add(new PalabraColoreada(delimitador-textoPorColorear.length(), textoPorColorear,false));
					}
					textoPorColorear = "";
				}
				
			}
		}
		oldString = currentString;
	}
	private boolean palabraReservada(String palabra){
		if(palabra.matches("(if|while|true|false|"
				+ "class|boolean|int|string|double)"))
			return true;
		return false;
	}
	
	private class PalabraColoreada{
		int posicion;
		String palabra;
		boolean cadena;
		PalabraColoreada(int posicion, String palabra, boolean cadena){
			this.posicion = posicion;
			this.palabra = palabra;
			this.cadena = cadena;
		}
	}
}

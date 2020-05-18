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
		attributeSetString,
		attributeSetNumero,
		attributeSetComentario;
	private ArrayList<PalabraColoreada> palabrasEspeciales;
	public String currentString,
		oldString;
	private JTextPane codePane;
	public static Color colorNormal = new Color(230, 230, 230),
		colorNegrita = new Color(150, 17, 255),//(132, 12, 204),//(66, 6, 102)
		colorString = new Color(230, 0, 0),
		colorNumero = new Color(255, 255, 0),
		colorComentario = new Color(255, 255, 0);
	
	public CodeDocument(JTextPane codePane) {
		styleContext = StyleContext.getDefaultStyleContext();
		attributeSetNormal = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, colorNormal);
        attributeSetNegrita = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, colorNegrita);
        attributeSetString = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, colorString);
        attributeSetNumero = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, colorNumero);
        attributeSetComentario = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, colorComentario);
    }
	public CodeDocument(JTextPane codePane, Color normal, Color negrita, Color string, Color numero, Color comentario) {
		styleContext = StyleContext.getDefaultStyleContext();
		attributeSetNormal = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, normal);
        attributeSetNegrita = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, negrita);
        attributeSetString = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, string);
        attributeSetNumero = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, numero);
        attributeSetComentario = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, comentario);
        
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
			if(palabrasEspeciales.get(i).tipoDePalabra == PalabraColoreada.CADENA)
				newAttributeSet = new SimpleAttributeSet(attributeSetString.copyAttributes());
			else if(palabrasEspeciales.get(i).tipoDePalabra == PalabraColoreada.PALABRA_RESERVADA)
				newAttributeSet = new SimpleAttributeSet(attributeSetNegrita.copyAttributes());
			else if(palabrasEspeciales.get(i).tipoDePalabra == PalabraColoreada.COMENTARIO)
				newAttributeSet = new SimpleAttributeSet(attributeSetComentario.copyAttributes());
			else
				newAttributeSet = new SimpleAttributeSet(attributeSetNumero.copyAttributes());
			
			if(palabrasEspeciales.get(i).tipoDePalabra == PalabraColoreada.CADENA
					|| palabrasEspeciales.get(i).tipoDePalabra == PalabraColoreada.PALABRA_RESERVADA)
				StyleConstants.setBold(newAttributeSet, true);
			if(palabrasEspeciales.get(i).tipoDePalabra == PalabraColoreada.COMENTARIO)
				StyleConstants.setItalic(newAttributeSet, true);
            setCharacterAttributes(palabrasEspeciales.get(i).posicion, palabrasEspeciales.get(i).palabra.length(), newAttributeSet, true);
		}
	}
	
	private void colorea() throws BadLocationException{
		String textoCompleto = getText(0, getLength()),
			textoPorColorear = "";
		currentString = textoCompleto;
		textoCompleto += " " + System.getProperty("line.separator");
		int delimitador =  0;
		boolean esNumero = true;
		boolean esComentario = false;
		
		for (int i = 0; i < textoCompleto.length(); i++) {
			char letra = textoCompleto.charAt(i);
			if(i > 0) {
				if(!esComentario) {
					if(letra == '/' && textoCompleto.charAt(i-1) == '/') {
						esComentario = true;
						esNumero = false;
					}
				}
			}
			if(esComentario) {
				textoPorColorear+= letra;
			}
			if((Character.isLetterOrDigit(letra) || letra == '_' || letra == '.') && !esComentario){
				if(Character.isDigit(letra) || letra == '.'){
					if(letra == '.' && textoPorColorear.contains(".")) {
						esNumero = false;
					}
				}
				else
					esNumero = false;
				textoPorColorear += letra;
			} else if((letra == '"') && !esComentario) {
				esNumero = false;
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
				palabrasEspeciales.add(new PalabraColoreada(delimitador-textoPorColorear.length(), textoPorColorear, PalabraColoreada.CADENA));
				textoPorColorear = "";
			}
			else if(letra == '/' && !esComentario) {
				textoPorColorear+= letra;
			}
			else{
				if(esComentario && !(letra == 10 || letra == 13)) {
					continue;
				}
				delimitador = i;
				if(textoPorColorear.length() > 0){
					if(palabraReservada(textoPorColorear)){
						palabrasEspeciales.add(new PalabraColoreada(delimitador-textoPorColorear.length(), textoPorColorear, PalabraColoreada.PALABRA_RESERVADA));
					}
					else if(esNumero){
						palabrasEspeciales.add(new PalabraColoreada(delimitador-textoPorColorear.length(), textoPorColorear, PalabraColoreada.NUMERO));
					}
					else if(esComentario) {
						palabrasEspeciales.add(new PalabraColoreada(delimitador-textoPorColorear.length(), textoPorColorear, PalabraColoreada.COMENTARIO));
					}
					textoPorColorear = "";
					esNumero = true;
					esComentario = false;
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
		String palabra;
		int posicion,
			tipoDePalabra;
		final static int
			CADENA = 0,
			NUMERO = 1,
			PALABRA_RESERVADA = 2,
			COMENTARIO = 3;
		PalabraColoreada(int posicion, String palabra, int tipoDePalabra){
			this.posicion = posicion;
			this.palabra = palabra;
			this.tipoDePalabra = tipoDePalabra;
		}
	}
}

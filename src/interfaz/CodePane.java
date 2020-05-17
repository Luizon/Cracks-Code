package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class CodePane extends JScrollPane implements DocumentListener {
	public JTextPane codePane,
		lineNumber;
	private FontMetrics metrics;
	public CodePane(JTextPane codePane) {
		this.codePane = codePane;

		lineNumber = new JTextPane();
		lineNumber.setFont(codePane.getFont());
		lineNumber.setText("1");
		lineNumber.setEditable(false);
		lineNumber.setFocusable(false);
//		StyledDocument lineNumberDoc = lineNumber.getStyledDocument();
		SimpleAttributeSet derecha = new SimpleAttributeSet();
		StyleConstants.setAlignment(derecha, StyleConstants.ALIGN_RIGHT);
//		lineNumberDoc.setParagraphAttributes(0, lineNumberDoc.getLength(), derecha, false);
//		lineNumber.addStyle("numeros", null);
		Numeros lineNumberDocument = new Numeros(lineNumber, Theme.colorLineNumber);
		lineNumberDocument.setParagraphAttributes(0, lineNumberDocument.getLength(), derecha, false);
		lineNumber.setStyledDocument(lineNumberDocument);
		
		getViewport().add(codePane);
		setRowHeaderView(lineNumber);
		
		metrics = lineNumber.getFontMetrics(lineNumber.getFont());
		getRowHeader().setPreferredSize(new Dimension(metrics.stringWidth("  "), 0));
		
		codePane.getDocument().addDocumentListener(this);
		codePane.setStyledDocument(new CodeDocument(codePane));
		codePane.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				int pos = codePane.getCaretPosition();
	            Element map = codePane.getDocument().getDefaultRootElement();
	            int row = map.getElementIndex(pos);
	            Element lineElem = map.getElement(row);
	            int col = pos - lineElem.getStartOffset();
	            col++;
	            row++;
	            System.out.println("c: "+col+", l: "+row);
			}
		});
	}
	
	private String getLineNumbers() {
		int caretPosition = codePane.getDocument().getLength();
        Element root = codePane.getDocument().getDefaultRootElement();
        String text = "1\n";
        for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
        	text += i + "\n";
        }
        int lastLine = root.getElementIndex(caretPosition) + 1;
        getRowHeader().setPreferredSize(new Dimension(metrics.stringWidth(lastLine + " "), 0));
        return text;
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		lineNumber.setText(getLineNumbers());
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		lineNumber.setText(getLineNumbers());
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		lineNumber.setText(getLineNumbers());
	}
}

class Numeros extends DefaultStyledDocument {
	private AttributeSet attributeSet;
	public Numeros(JTextPane lineNumber, Color color) {
		StyleContext styleContext = StyleContext.getDefaultStyleContext();
		attributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, color);
	}
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offs, str, a);
		actualizaPanel();
	}
	public void remove(int arg0, int arg1) throws BadLocationException {
		super.remove(arg0, arg1);
		actualizaPanel();
	}
	public synchronized void actualizaPanel() throws BadLocationException{
		MutableAttributeSet newAttributeSet = new SimpleAttributeSet(attributeSet.copyAttributes());
        setCharacterAttributes(0, getText(0, getLength()).length(), newAttributeSet, true);
	}
	public AttributeSet getAttributeSet() {
		return attributeSet;
	}
}

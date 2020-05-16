package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

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
		StyledDocument lineNumberDoc = lineNumber.getStyledDocument();
		SimpleAttributeSet derecha = new SimpleAttributeSet();
		StyleConstants.setAlignment(derecha, StyleConstants.ALIGN_RIGHT);
		lineNumberDoc.setParagraphAttributes(0, lineNumberDoc.getLength(), derecha, false);
		
		getViewport().add(codePane);
		setRowHeaderView(lineNumber);
		
		metrics = lineNumber.getFontMetrics(lineNumber.getFont());
		getRowHeader().setPreferredSize(new Dimension(metrics.stringWidth("  "), 0));
		
		codePane.getDocument().addDocumentListener(this);
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

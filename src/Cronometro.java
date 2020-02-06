import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Cronometro extends JFrame implements MouseListener, KeyListener, FocusListener {
	JLabel etCronometro;
	boolean esCronometro = false, corriendo = false;
	JTextField txtMinuto, txtSegundo, txtCentSeg;
	JButton btnEmpezar, btnCambiarModo;
	Tiempo cronometro;
	public Cronometro() {
		super("Cronometro y temporizador");
		final int width, height;
		Toolkit pantalla = getToolkit();
		width = pantalla.getScreenSize().width;
		height = pantalla.getScreenSize().height;
		setSize(width/2, height/2);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		iniciaComponentes();
		añadeComponentes();
		setVisible(true);
	}
	private void iniciaComponentes() {
		txtMinuto = new JTextField("00");
		txtSegundo = new JTextField("00");
		txtCentSeg = new JTextField("00");
		final Font fuente40 = new Font("Arial", Font.ITALIC, 40);
		txtMinuto.setFont(fuente40);
		txtSegundo.setFont(fuente40);
		txtCentSeg.setFont(fuente40);
		txtMinuto.setHorizontalAlignment(JTextField.CENTER);
		txtSegundo.setHorizontalAlignment(JTextField.CENTER);
		txtCentSeg.setHorizontalAlignment(JTextField.CENTER);
		
		final Font fuente30 = new Font("Arial", Font.BOLD, 30);
		btnEmpezar = new JButton("Empezar");
		btnEmpezar.setFont(fuente30);
		btnCambiarModo = new JButton("Cambiar modo");
		btnCambiarModo.setFont(fuente30);
		
		etCronometro = new JLabel("Usando temporizador");
		
		txtMinuto.addKeyListener(this);
		txtSegundo.addKeyListener(this);
		txtCentSeg.addKeyListener(this);
		txtMinuto.addFocusListener(this);
		txtSegundo.addFocusListener(this);
		txtCentSeg.addFocusListener(this);
		
		btnEmpezar.addMouseListener(this);
		btnCambiarModo.addMouseListener(this);
	}
	private void añadeComponentes() {
		setLayout(new GridLayout(3, 0));
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel21 = new JPanel();
		JPanel panel22 = new JPanel();
		JPanel panel23 = new JPanel();
		JPanel panel2sub1 = new JPanel();
		JPanel panel2sub2 = new JPanel();
		JPanel panel3 = new JPanel();
		panel1.setLayout(new GridLayout(1, 3));
		panel2.setLayout(new GridLayout(2, 1));
		panel2sub1.setLayout(new GridLayout(1, 3));
		panel3.setLayout(new GridLayout(1, 2));
		
		panel1.add(txtMinuto);
		panel1.add(txtSegundo);
		panel1.add(txtCentSeg);
		
		final Font fuente20 = new Font("Arial", Font.BOLD, 20);
		final JLabel et1 = new JLabel("Minutos");
		final JLabel et2 = new JLabel("Segundos");
		final JLabel et3 = new JLabel("<HTML>Centesimas<br>de segundo</HTML>");
		et1.setFont(fuente20);
		et2.setFont(fuente20);
		et3.setFont(fuente20);
		etCronometro.setFont(fuente20);
		panel21.add(et1);
		panel22.add(et2);
		panel23.add(et3);
		panel2sub1.add(panel21);
		panel2sub1.add(panel22);
		panel2sub1.add(panel23);
		panel2sub2.add(etCronometro);
		panel2.add(panel2sub1);
		panel2.add(panel2sub2);
		
		panel3.add(btnCambiarModo);
		panel3.add(btnEmpezar);
		
		add(panel1);
		add(panel2);
		add(panel3);
	}
	private void empezar() {
		cronometro = new Tiempo(this);
		corriendo = true;
	}
	public void detener() {
		cronometro.detener();
		cronometro = null;
		corriendo = false;
		txtMinuto.setText("00");
		txtSegundo.setText("00");
		txtCentSeg.setText("00");
	}
	public static void main(String[] args) {
		new Cronometro();
	}
	@Override
	public void mouseClicked(MouseEvent evt) {
		if(evt.getSource() == btnCambiarModo) {
			if(btnCambiarModo.getText().equals("Detener")) {
				btnCambiarModo.setText("Cambiar modo");
				btnEmpezar.setText("Empezar");
				detener();
			} else
				if(!esCronometro) {
					esCronometro = true;
					etCronometro.setText("Usando cronometro");
				}
				else {
					esCronometro = false;
					etCronometro.setText("Usando temporizador");
				}
			return;
		}
		if(evt.getSource() == btnEmpezar) {
			if(btnEmpezar.getText().equals("Empezar")) {
				btnEmpezar.setText("Pausar");
				btnCambiarModo.setText("Detener");
				empezar();
			}
			else {
				if(!corriendo)
					btnEmpezar.setText("Empezar");
				else
					if(btnEmpezar.getText().equals("Pausar")) {
						btnEmpezar.setText("Continuar");
						cronometro.pausa = true;
					}
					else {
						btnEmpezar.setText("Pausar");
						empezar();
					}
			}
			return;
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent k) {
		if(k.isControlDown() || k.isAltGraphDown())
			k.consume();
	}
	@Override
	public void keyReleased(KeyEvent k) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent k) {
		if(k.getSource() == txtMinuto) {
			final String sel = txtMinuto.getSelectedText()!=null?txtMinuto.getSelectedText():"";
			if(txtMinuto.getText().length()-sel.length() > 0)
				txtSegundo.requestFocus();
			if(!esEntero(k.getKeyChar() + "")) {
				Toolkit.getDefaultToolkit().beep();
				k.consume();
			}
			if(Integer.parseInt(txtMinuto.getText())>99) {
				txtMinuto.setText("99");
				k.consume();
			}
			return;
		}
		if(k.getSource() == txtSegundo) {
			final String sel = txtSegundo.getSelectedText()!=null?txtSegundo.getSelectedText():"";
			if(!esEntero(k.getKeyChar() + "")) {
				Toolkit.getDefaultToolkit().beep();
				k.consume();
			}
			if(Integer.parseInt(txtSegundo.getText())>59) {
				txtSegundo.setText("59");
				k.consume();
			}
			return;
		}
		if(k.getSource() == txtCentSeg) {
			final String sel = txtCentSeg.getSelectedText()!=null?txtCentSeg.getSelectedText():"";
			if(txtCentSeg.getText().length()-sel.length() > 0)
				txtMinuto.requestFocus();
			if(!esEntero(k.getKeyChar() + "")) {
				Toolkit.getDefaultToolkit().beep();
				k.consume();
			}
			return;
		}
	}
	private boolean esEntero(String str) {
		try{
			Integer.parseInt(str);
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	@Override
	public void focusGained(FocusEvent f) {
		if(f.getSource() == txtMinuto) {
			if(Integer.parseInt(txtMinuto.getText())>99) {
				txtMinuto.setText("99");
			}
			return;
		}
		if(f.getSource() == txtSegundo) {
			if(Integer.parseInt(txtSegundo.getText())>59) {
				txtSegundo.setText("59");
			}
			return;
		}
		if(f.getSource() == txtCentSeg) {
			if(Integer.parseInt(txtCentSeg.getText())>99) {
				txtCentSeg.setText("99");
			}
			return;
		}
	}
	@Override
	public void focusLost(FocusEvent f) {
		if(f.getSource() == txtMinuto) {
			if(Integer.parseInt(txtMinuto.getText())>99) {
				txtMinuto.setText("99");
			}
			return;
		}
		if(f.getSource() == txtSegundo) {
			if(Integer.parseInt(txtSegundo.getText())>59) {
				txtSegundo.setText("59");
			}
			return;
		}
		if(f.getSource() == txtCentSeg) {
			if(Integer.parseInt(txtCentSeg.getText())>99) {
				txtCentSeg.setText("99");
			}
			return;
		}
	}
}

class Tiempo extends Thread {
	private int min;
	private int seg;
	private int centSeg;
	boolean pausa = false;
	private Cronometro c;
	public Tiempo(Cronometro c) {
		this.centSeg = Integer.parseInt(c.txtCentSeg.getText());
		this.min = Integer.parseInt(c.txtMinuto.getText());
		this.seg = Integer.parseInt(c.txtSegundo.getText());
		this.c = c;
		start();
	}
	@Override
	public void run() {
		c.txtCentSeg.setEnabled(false);
		c.txtMinuto.setEnabled(false);
		c.txtSegundo.setEnabled(false);

		if(!c.esCronometro)
			while(c.corriendo && !pausa && (min > 0 || seg > 0 || centSeg > 0)) {
				try {
					if(centSeg>0)
						centSeg--;
					else
					if(seg>0) {
						centSeg = 99;
						seg--;
					}
					else if(min>0) {
						centSeg = 99;
						seg = 59;
						min--;
					}
					final String strMin = min>=10?min+"":"0"+min;
					final String strSeg = seg>=10?seg+"":"0"+seg;
					final String strCentSeg = centSeg>=10?centSeg+"":"0"+centSeg;
					c.txtMinuto.setText(strMin);
					c.txtSegundo.setText(strSeg);
					c.txtCentSeg.setText(strCentSeg);
					sleep(10);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		else
			while(c.corriendo && !pausa) {
				try {
					centSeg++;
					if(centSeg>=99) {
						centSeg = 0;
						seg++;
					}
					if(seg>=59) {
						seg = 0;
						min++;
					}
					final String strMin = min>10?min+"":"0"+min;
					final String strSeg = seg>10?seg+"":"0"+seg;
					final String strCentSeg = centSeg>10?centSeg+"":"0"+centSeg;
					c.txtMinuto.setText(strMin);
					c.txtSegundo.setText(strSeg);
					c.txtCentSeg.setText(strCentSeg);
					sleep(10);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		if(!c.esCronometro) {
			if(min == 0 && seg == 0 && centSeg == 0)
				c.detener();
		}
	}
	public void detener() {
		pausa = false;
		
		c.txtCentSeg.setEnabled(true);
		c.txtMinuto.setEnabled(true);
		c.txtSegundo.setEnabled(true);
		
		c.btnCambiarModo.setText("Cambiar modo");
		c.btnEmpezar.setText("Empezar");
	}
}

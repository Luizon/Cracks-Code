package interfaz;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelPesta�a extends JPanel implements MouseListener {
	  private JLabel etiqueta;
	  public JButton boton;
	  private Vista vista;
	  private int id;
	  private ImageIcon iconoCerrarTab;

	  public PanelPesta�a(String titulo, String toolTipText, Vista vista, int id) {
		  this.id = id;
		  setFocusable(false);
		  this.vista = vista;
		  create(titulo, toolTipText);
	  }
	  private void create(String titulo, String toolTipText) {
		  setOpaque(false);
		    setLayout(new java.awt.GridBagLayout());
		    GridBagConstraints gbc = new GridBagConstraints();
		    gbc.gridx=0;
		    gbc.gridy=0;
		    gbc.weightx=1;
		    etiqueta = new JLabel();
		    setTitle(titulo+" ", toolTipText);
		    
		    boton=new JButton();
		    boton.setFocusable(false);
		    iconoCerrarTab = vista.icoCerrarPesta�a;
		    int btnWidth = iconoCerrarTab.getIconWidth(),
		    		btnHeight = iconoCerrarTab.getIconHeight();
		    //Image cerrarPesta�aImg = iconoCerrarTab.getImage();
		    etiqueta.setIcon(vista.icoCode);
		    boton.setIcon(iconoCerrarTab);
		    boton.setPreferredSize(new Dimension(btnWidth, btnHeight));
		    /**/boton.setBorderPainted(false);
		    boton.setContentAreaFilled(false);
		    boton.setFocusPainted(false);
		    boton.setOpaque(false);/**/
		    /*
		    ButtonUI botonBonito = new ButtonUI() {
		    	@Override
		    	public void paint(Graphics g, JComponent comp) {
		    		super.paint(g, comp);
		    		g.drawImage(cerrarPesta�aImg, 0, 0, comp);
		    	}
			};
		    boton.setUI(botonBonito);
		    /**/
		    //Listener para cierre de tabs con acceso estatico al `JTabbedPane`
		    boton.setToolTipText(toolTipText);
		    cargaEscuchadores();
		    
		    JPanel espacio = new JPanel();
		    espacio.setPreferredSize(new Dimension(0, 5));
		    espacio.setOpaque(false);
		    espacio.setBackground(null);
		    add(espacio);
		    gbc.gridy++;
		    add(etiqueta,gbc);
		    gbc.gridx++;
		    gbc.weightx=0;
		    add(boton,gbc);
	  }
	  public void setTitle(String titulo, String toolTipText) {
		    etiqueta.setText(titulo+" ");
		    if(toolTipText.length() > 0) {
		    	etiqueta.setToolTipText(toolTipText);
		    	if(boton != null)
		    		boton.setToolTipText(toolTipText);
		    }
	  }
	  public void setTitle(String titulo) {
		  etiqueta.setText(titulo+" ");
	  }
	  
	  public void cargaEscuchadores() {
		  boton.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent evt) {
					for(int borrarTab=0; borrarTab < vista.codigoTabs.getTabCount()-1; borrarTab++)
						if(vista.titulo.getByIndex(borrarTab).dato.id == id)
							vista.cerrarPesta�a(borrarTab);
		    	}
		    });
		    boton.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				@Override
				public void mousePressed(MouseEvent e) {
				}
				@Override
				public void mouseExited(MouseEvent e) {
					int tab = vista.getSelectedTab();
					if(vista.titulo.getByIndex(tab).dato.id != id)
						boton.setIcon(null);
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					boton.setIcon(iconoCerrarTab);
				}
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
		    addMouseListener(this);
		    addAncestorListener(vista.escuchadores);
	  }
	  
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
			boton.setIcon(iconoCerrarTab);
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
			int tab = vista.getSelectedTab();
			if(vista.titulo.getByIndex(tab).dato.id != id)
				boton.setIcon(null);
		}
		@Override
		public void mousePressed(MouseEvent arg0) {
			for(int i=0; i < vista.codigoTabs.getTabCount()-1; i++)
				if(vista.titulo.getByIndex(i).dato.id == id)
					vista.codigoTabs.setSelectedIndex(i);
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
}



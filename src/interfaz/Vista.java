package interfaz;

import java.io.Serializable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import estructuraDeDatos.ListaDoble;
import misc.Statics;

@SuppressWarnings("serial")
public class Vista extends JFrame implements Serializable {
	public JMenuBar barraDelMenu;
	public JMenu menuArchivo,
		menuOpciones,
		menuArchivosRecientes;
	public JMenuItem nuevoArchivo,
		cargarArchivo,
		guardarArchivo,
		guardarComo,
		salir,
		cambiarTema,
		personalizarTema,
		ayuda,
		acercaDe;
	public JTabbedPane codigoTabs;
	public ListaDoble<JTextPane> txtCodigo;
	public File archivoTemporal;
	public ListaDoble<String> rutaDeArchivoActual,
		nombreDelArchivo,
		tituloVentana;
	public ListaDoble<Integer> tamañoTextoDelEditor;
	public ListaDoble<Boolean> cambiosGuardados;
	public ListaDoble<PanelPestaña> titulo;
	private ArrayList<String[]> listaArchivosRecientes;
	public Escuchadores escuchadores;
	public int tabulaciones = 0,
		tema = Theme.CLARO,
		posicionRelativaDelDividorDeLosTabbedPane = 0;
	public boolean hayError = false; // hacer algo con esto después, no me gusta
	private final String carpetaDeAppData = System.getenv("APPDATA") + "/Cracks Code/",
		archivoDeArchivosRecientes = carpetaDeAppData+"recientes.txt",
		archivoUltimoTemaUsado = carpetaDeAppData+"ultimoTemaUsado.txt",
		archivoUltimaSesion = carpetaDeAppData+"ultimaSesion.txt",
		archivoUltimoTemaPersonalizado = carpetaDeAppData+"ultimoTemaPersonalizado.txt";
	public ImageIcon iconoNuevoTab,
		icoCode,
		icoBug,
		icoError,
		icoDone,
		icoTable,
		icoCerrarPestaña;
	private int contadorDePestañas = 0;
	private Object[] sesion;
	public Font fontHeader = new Font("Arial", Font.BOLD, 17),
			fuenteConsola = new Font("Consolas", Font.PLAIN, 16);

	public Vista() {
		super("CRACK'S Code");
		constructor();
	}
	
	public void constructor() {
		hazInterfaz();
		agregaEscuchadores();
	}
	
	private void hazInterfaz() {
		setSize(1200, 950);
//		setSize(400, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(700, 600));

		barraDelMenu = new JMenuBar();
		setJMenuBar(barraDelMenu);
		creaMenu();
		crearIconos();
		codigoTabs = new JTabbedPane();
		codigoTabs.insertTab("", iconoNuevoTab, null, "Nueva pestaña", codigoTabs.getTabCount());
		codigoTabs.setSelectedIndex(codigoTabs.getTabCount()-1);
		cargarUltimoTemaUsado();
		cargarUltimoTemaPersonalizado();
		Theme.changeTheme(this);
//		if(!reanudarSesionAnterior()) { // no funciona bien, ya luego lo repararé
			titulo = new ListaDoble<PanelPestaña>();
			rutaDeArchivoActual = new ListaDoble<String>();
			nombreDelArchivo = new ListaDoble<String>();
			tituloVentana = new ListaDoble<String>();
			tamañoTextoDelEditor = new ListaDoble<Integer>();
			cambiosGuardados = new ListaDoble<Boolean>();
			txtCodigo = new ListaDoble<JTextPane>();
			nuevaPestaña();
//		}
		add(codigoTabs);
		
		setVisible(true);

		setTitle(tituloVentana.getByIndex(codigoTabs.getSelectedIndex()).dato);
		
		/*System.out.println("Datos: \n"
				+ "[\n"
				+ "\ttitulos: " + titulo.length() + "\n"
				+ "\trutas de archivo actual: " + rutaDeArchivoActual.length() + "\n"
				+ "\tnombres de archivos: " + nombreDelArchivo.length() + "\n"
				+ "\ttitulos de ventana: " + tituloVentana.length() + "\n"
				+ "\ttamaños de textos del editor: " + tamañoTextoDelEditor.length() + "\n"
				+ "\tcambios guardados: " + cambiosGuardados.length() + "\n"
				+ "]");/**/
	}
	
	private void crearIconos() {
		iconoNuevoTab = new ImageIcon("src/images/pestañaNueva.png");
		icoCode = new ImageIcon(Statics.getImage("code"));
		icoBug = new ImageIcon(Statics.getImage("bug"));
		icoError = new ImageIcon(Statics.getImage("error"));
		icoDone = new ImageIcon(Statics.getImage("done"));
		icoTable = new ImageIcon(Statics.getImage("table"));
		icoCerrarPestaña = new ImageIcon("src/images/cerrarPestaña.png");
	}

	@SuppressWarnings({ "unchecked", "unused" }) // unused pero de momento xd
	private boolean reanudarSesionAnterior() {
		sesion = new Object[8];
		try {
	    	// verificar si la carpeta existe
        	if(!Files.exists(Paths.get(carpetaDeAppData))) {
        		Files.createDirectory(Paths.get(carpetaDeAppData)); // si no existe, se crea
        	}
        	// verificar si el archivo existe
        	File fichero = new File(archivoUltimaSesion);
	    	if(!fichero.exists()) {
	    		return false; // si no existe, VETE YA, SI NO ENCUENTRAS MOTIIIVOOOOS
	    	}
	    	FileInputStream fis = new FileInputStream(fichero.getAbsoluteFile());
	    	ObjectInputStream ois = new ObjectInputStream(fis);
			sesion = (Object[]) ois.readObject();
			ois.close();
			fis.close();
		} catch (ClassNotFoundException | IOException | NullPointerException e) {
			System.out.println("Hubo un problema al leer los datos de la última sesión.");
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println("Hubo un problema al leer los datos de la última sesión."
					+ "\nNi idea de porque ocurrió este problema.");
			e.printStackTrace();
			return false;
		}
		
		titulo = (ListaDoble<PanelPestaña>)sesion[0];
		rutaDeArchivoActual = (ListaDoble<String>)sesion[1];
		nombreDelArchivo = (ListaDoble<String>)sesion[2];
		tituloVentana = (ListaDoble<String>)sesion[3];
		tamañoTextoDelEditor = (ListaDoble<Integer>)sesion[4];
		cambiosGuardados = (ListaDoble<Boolean>)sesion[5];
		txtCodigo = (ListaDoble<JTextPane>)sesion[6];
		
		int finDelFor = titulo.length();
		for(int i=0; i<finDelFor; i++) {
			String texto = txtCodigo.getByIndex(i).dato.getText(),
				ruta = rutaDeArchivoActual.getByIndex(i).dato,
				nombre = nombreDelArchivo.getByIndex(i).dato;
			nuevaPestaña(texto, ruta, nombre, -1);
			titulo.getByIndex(i).dato.cargaEscuchadores();
			codigoTabs.setTabComponentAt(Math.max(0, i), titulo.getByIndex(i).dato);
		}
		
		return true;
	}
	
	public void guardarSesion() { // unused de momento xd
		sesion = new Object[]{
				titulo, // 0
				rutaDeArchivoActual, // 1
				nombreDelArchivo, // 2
				tituloVentana, // 3
				tamañoTextoDelEditor, // 4
				cambiosGuardados, //5
				txtCodigo, //6
		};
		try {
			File archivoParaEliminar = new File(archivoUltimaSesion);
			archivoParaEliminar.delete();
			FileOutputStream fis = new FileOutputStream(archivoUltimaSesion);
			ObjectOutputStream oos = new ObjectOutputStream(fis);
			oos.close();
			oos.writeObject(sesion);
			fis.close();
		} catch (IOException e) {
			System.out.println("Hubo un problema al guardar los datos de la sesión actual.");
			e.printStackTrace();
		}
	}
	
	private void creaMenu() {
		// css para unir los iconos con los textos
		String css =
				"div {"
				+ "		text-align: center;"
				+ "}"
				+ ".lateral {"
				+ "		color: #000000;"
				+ "		font-size: 1.25em;"
				+ "		font-weight: bold;"
				+ "}"
				+ ".acortador {"
				+ "		color: #888888;"
				+ "		font-size: 1em;"
				+ "		font-weight: plain;"
				+ "}";
		
		// imagenes
		String d = "<div width=96>";
		String folder = d+"<img src=\"file:/"+Statics.getImage("folder2") + "\">";
		String run = d+"<img src=\"file:/"+Statics.getImage("run2") + "\">";
		String settings = d+"<img src=\"file:/"+Statics.getImage("settings2") + "\">";
		
		menuArchivo = new JMenu(Statics.getHTML(folder+"<p>Archivo", css));
		menuOpciones = new JMenu(Statics.getHTML(settings+"<p>Opciones", css));
		barraDelMenu.add(menuArchivo);
		barraDelMenu.add(menuOpciones);
		
		String a = "<br /><span class=\"acortador\">",
				l = "<span class=\"lateral\">";
		
		nuevoArchivo = new JMenuItem(Statics.getHTML(l+"Nuevo archivo "+a+"(ctrl+N)", css));
		nuevoArchivo.setIcon(new ImageIcon(Statics.getImage("new2")));
		cargarArchivo = new JMenuItem(Statics.getHTML(l+"Cargar archivo "+a+"(ctrl+O)", css));
		cargarArchivo.setIcon(new ImageIcon(Statics.getImage("open2")));
		guardarArchivo = new JMenuItem(Statics.getHTML(l+"Guardar archivo "+a+"(ctrl+S)", css));
		guardarArchivo.setIcon(new ImageIcon(Statics.getImage("save2")));
		guardarComo = new JMenuItem(Statics.getHTML(l+"Guardar archivo como "+a+"(ctrl+shift+S)", css));
		guardarComo.setIcon(new ImageIcon(Statics.getImage("saveAs2")));
		menuArchivosRecientes = new JMenu(Statics.getHTML(l+"Archivos Recientes", css));
		menuArchivosRecientes.setIcon(new ImageIcon(Statics.getImage("recent2")));
		salir = new JMenuItem(Statics.getHTML(l+"Salir "+a+"(alt+F4)", css));
		salir.setIcon(new ImageIcon(Statics.getImage("exit2")));
		cambiarTema = new JMenuItem(Statics.getHTML(l+"Cambiar de tema"+a+"(F11)", css));
		cambiarTema.setIcon(new ImageIcon(Statics.getImage("theme2")));
		personalizarTema = new JMenuItem(Statics.getHTML(l+"Personalizar tema"+a+"(F10)", css));
		personalizarTema.setIcon(new ImageIcon(Statics.getImage("theme2")));
		ayuda = new JMenuItem(Statics.getHTML(l+"Ayuda"+a+"(F1)", css));
		ayuda.setIcon(new ImageIcon(Statics.getImage("info2")));
		acercaDe = new JMenuItem(Statics.getHTML(l+"Acerca de"+a+"(F12)", css));
		acercaDe.setIcon(new ImageIcon(Statics.getImage("about2")));
		
		menuArchivo.add(nuevoArchivo);
		menuArchivo.add(cargarArchivo);
		menuArchivo.add(guardarArchivo);
		menuArchivo.add(guardarComo);
		menuArchivo.add(menuArchivosRecientes);
		
		cargaArchivosRecientes();
		
		menuArchivo.add(salir);
		menuOpciones.add(cambiarTema);
		menuOpciones.add(personalizarTema);
		menuOpciones.add(ayuda);
		menuOpciones.add(acercaDe);
	}
	
	public void personalizarTema() {
		JPanel panelPrincipal = new JPanel();
		Color	colorBack = Theme.colorBack,
				colorCaret = Theme.colorCaret,
				colorSelection = Theme.colorSelection,
				colorForeground = Theme.colorForeground,
				colorNegrita = Theme.colorNegrita,
				colorString = Theme.colorString,
				colorNumero = Theme.colorNumero,
				colorComentario = Theme.colorComentario,
				colorLineNumberBack = Theme.colorLineNumberBack,
				colorLineNumber = Theme.colorLineNumber;
		int width = 0;
		JButton btnBack = new JButton("Fondo del código") ,
				btnCaret = new JButton("Signo de intercalación (cursor)"),
				btnSelection = new JButton("Selección de texto"),
				btnForeground = new JButton("Texto común"),
				btnNegrita = new JButton("Palabras reservadas"),
				btnString = new JButton("Cadenas detectadas"),
				btnNumero = new JButton("Números"),
				btnComentario = new JButton("Comentarios"),
				btnLineNumberBack = new JButton("Fondo del mostrador de líneas"),
				btnLineNumber = new JButton("Números del mostrador de lineas"),
				btnDefault = new JButton("Reestablecer colores");
		Color [] color     = new Color[] {colorBack, colorCaret, colorSelection, colorForeground, colorNegrita, colorString, colorNumero, colorComentario, colorLineNumberBack, colorLineNumber};
		JButton [] boton = new JButton[] {btnBack,   btnCaret,   btnSelection,   btnForeground,   btnNegrita,   btnString,   btnNumero,   btnComentario,   btnLineNumberBack,   btnLineNumber, btnDefault};
		
		for(int i = 0 ; i < boton.length ; i++) {
			if(width < boton[i].getWidth())
				width = boton[i].getWidth();
			int indice = i;
			boton[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if(indice < boton.length - 1) {
						Color pickedColor = JColorChooser.showDialog(null, "Seleccione un color", color[indice]);
						if(pickedColor != null) {
							color[indice] = pickedColor;
							boton[indice].setBackground(color[indice]);
							boton[indice].setForeground(invertirColor(color[indice]));
						}
					}
					else {
						color[0] = Theme.defaultBack;
						color[1] = Theme.defaultCaret;
						color[2] = Theme.defaultSelection;
						color[3] = Theme.defaultForeground;
						color[4] = Theme.defaultNegrita;
						color[5] = Theme.defaultString;
						color[6] = Theme.defaultNumero;
						color[7] = Theme.defaultComentario;
						color[8] = Theme.defaultLineNumberBack;
						color[9] = Theme.defaultLineNumber;
						for(int i = 0 ; i < boton.length - 1; i++) {
							boton[i].setBackground(color[i]);
							boton[i].setForeground(invertirColor(color[i]));
						}
					}
				}
			});
		}
		
		panelPrincipal.setPreferredSize(new Dimension(width + 50, 340));
		
		for(int i = 0 ; i < boton.length - 1; i++) {
			boton[i].setBackground(color[i]);
			boton[i].setForeground(invertirColor(color[i]));
			panelPrincipal.add(boton[i]);
		}
		panelPrincipal.add(boton[boton.length - 1]);
		int opcion = JOptionPane.showConfirmDialog(null, panelPrincipal, "Personalizar tema", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
		if(opcion == JOptionPane.OK_OPTION)
			cambiarTema(color[0], color[1], color[2], color[3], color[4], color[5], color[6], color[7], color[8], color[9]);
	}
	
	private Color invertirColor(Color color) {
		int colores = color.getRed() > 150 ? 0 : color.getGreen() > 150 ? 0 : color.getBlue() > 200 ? 0 : 255;
		return new Color(colores, colores, colores);
	}
	
	private void cambiarTema(Color colorBack, Color colorCaret, Color colorSelection, Color colorForeground, Color colorNegrita,
			Color colorString, Color colorNumero, Color colorComentario, Color colorLineNumberBack, Color colorLineNumber) {
		Theme.colorBack = colorBack;
		Theme.colorCaret = colorCaret;
		Theme.colorSelection = colorSelection;
		Theme.colorForeground = colorForeground;
		Theme.colorNegrita = colorNegrita;
		Theme.colorString = colorString;
		Theme.colorNumero = colorNumero;
		Theme.colorComentario = colorComentario;
		Theme.colorLineNumberBack = colorLineNumberBack;
		Theme.colorLineNumber = colorLineNumber;
		tema = Theme.PERSONALIZADO;
		cambiarTema(tema);
		
		guardarUltimoTemaPersonalizado();
	}
	
	public void cambiarTema(int tema) {
		if(tema > Theme.PERSONALIZADO || tema < Theme.CLARO)
			return;
		Theme.changeTheme(this);
		guardarUltimoTemaUsado();
	}
	
	public void cambiarTema() {
		if(tema == Theme.PERSONALIZADO)
			tema = Theme.CLARO;
		else
			tema++;
		cambiarTema(tema);
	}
	
	private void cargarUltimoTemaUsado() {
		int temaACargar = Theme.CLARO;
		try {
        	// verificar si el archivo existe
        	File fichero = new File(archivoUltimoTemaUsado);
	    	if(!fichero.exists()) {
	    		return; // si no existe, VETE YA, SI NO ENCUENTRAS MOTIIIVOOOOS
	    	}
	    	FileReader lector = new FileReader(fichero);
			temaACargar = lector.read();
			lector.close();
		} catch (IOException e) {
			System.out.println("Hubo un problema al leer los datos del último tema usado."
				+ "\nSe cargó el tema por defecto.");
			e.printStackTrace();
		}
		
		tema = temaACargar;
	}
	
	private void guardarUltimoTemaPersonalizado() {
		ArrayList<Color> listaDeColores = new ArrayList<Color>(); // si lo siguiente falla, este crack estará vacío
		try {
	    	// verificar si la carpeta existe
        	if(!Files.exists(Paths.get(carpetaDeAppData))) {
        		Files.createDirectory(Paths.get(carpetaDeAppData)); // si no existe, se crea
        	}
        	File fichero = new File(archivoUltimoTemaPersonalizado);
	    	FileOutputStream fos = new FileOutputStream(fichero.getAbsoluteFile());
	    	ObjectOutputStream oos = new ObjectOutputStream(fos);
			listaDeColores.add(Theme.colorBack);
			listaDeColores.add(Theme.colorCaret);
			listaDeColores.add(Theme.colorSelection);
			listaDeColores.add(Theme.colorForeground);
			listaDeColores.add(Theme.colorNegrita);
			listaDeColores.add(Theme.colorString);
			listaDeColores.add(Theme.colorNumero);
			listaDeColores.add(Theme.colorComentario);
			listaDeColores.add(Theme.colorLineNumberBack);
			listaDeColores.add(Theme.colorLineNumber);
			oos.writeObject(listaDeColores);
			oos.close();
			fos.close();
		} catch (IOException e) {
			System.out.println("Hubo un problema al guardar la última modificación del tema personalizado.");
			e.printStackTrace();
		}
	}
	
	private boolean cargarUltimoTemaPersonalizado() {
		ArrayList<Color> listaDeColores = new ArrayList<Color>(); // si lo siguiente falla, este crack estará vacío
		try {
			// verificar si la carpeta existe
			if(!Files.exists(Paths.get(carpetaDeAppData))) {
				Files.createDirectory(Paths.get(carpetaDeAppData)); // si no existe, se crea
			}
			File fichero = new File(archivoUltimoTemaPersonalizado);
			if(!Files.exists(Paths.get(archivoUltimoTemaPersonalizado)))
				return false;
			FileInputStream fis = new FileInputStream(fichero.getAbsoluteFile());
			ObjectInputStream ois = new ObjectInputStream(fis);
			listaDeColores = (ArrayList<Color>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException e) {
			System.out.println("Hubo un problema al cargar la última modificación del tema personalizado.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Hubo un problema al cargar la última modificación del tema personalizado.");
			e.printStackTrace();
		}
		if(listaDeColores.size() != 10)
			return false;
		cambiarTema(listaDeColores.get(0), listaDeColores.get(1), listaDeColores.get(2), listaDeColores.get(3), listaDeColores.get(4), listaDeColores.get(5), listaDeColores.get(6), listaDeColores.get(7), listaDeColores.get(8), listaDeColores.get(9));
		return true;
	}
	
	private void guardarUltimoTemaUsado() {
		try {
        	File fichero = new File(archivoUltimoTemaUsado);
        	FileWriter escritor = new FileWriter(fichero);
        	escritor.write(tema);
	    	escritor.close();
		} catch (IOException e) {
			System.out.println("Hubo un problema al leer los datos del último tema usado."
				+ "\nSe cargó el tema por defecto.");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargaArchivosRecientes() {
		listaArchivosRecientes = new ArrayList<String[]>(); // si lo siguiente falla, este crack estará vacío
		try {
	    	// verificar si la carpeta existe
        	if(!Files.exists(Paths.get(carpetaDeAppData))) {
        		Files.createDirectory(Paths.get(carpetaDeAppData)); // si no existe, se crea
        	}
        	// verificar si el archivo existe
        	File fichero = new File(archivoDeArchivosRecientes);
	    	if(!fichero.exists()) {
	    		return; // si no existe, VETE YA, SI NO ENCUENTRAS MOTIIIVOOOOS
	    	}
	    	FileInputStream fis = new FileInputStream(fichero.getAbsoluteFile());
	    	ObjectInputStream ois = new ObjectInputStream(fis);
			listaArchivosRecientes = (ArrayList<String[]>) ois.readObject();
			menuArchivosRecientes.removeAll();
			for(int i=0; i<listaArchivosRecientes.size(); i++) {
				String rutaArchivoReciente = listaArchivosRecientes.get(i)[1];
				String nombreArchivoReciente = listaArchivosRecientes.get(i)[0];
				String css = ".link {"
						+ "		color: #888888;"
						+ "		font-size: 1em;"
						+ "		font-weight: plain;"
						+ "}";
				String l = "<span class=\"link\">";
				String body = nombreArchivoReciente+l+" - "+rutaArchivoReciente;
				String HTML = Statics.getHTML(body, css);
				JMenuItem menuItem = new JMenuItem(HTML);
				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cargarArchivo(rutaArchivoReciente);
					}
				});
				menuItem.setFocusable(false);
				menuArchivosRecientes.add(menuItem);
			}
			ois.close();
			fis.close();
		} catch (IOException e) {
			System.out.println("Hubo un problema al leer los datos de archivos recientes.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void añadirArchivoARecientes(String nombre, String ruta) {
		try {
			for(int i=0; i<listaArchivosRecientes.size(); i++) {
				if(ruta.equals(listaArchivosRecientes.get(i)[1])) // si la ruta ya está guardada en uno de los archivos recientes
					listaArchivosRecientes.remove(i); // la borras, así se añade abajo y se posiciona hasta arriba
			}
			listaArchivosRecientes.add(0, new String[]{nombre, ruta});
	    	FileOutputStream fis = new FileOutputStream(archivoDeArchivosRecientes);
			ObjectOutputStream oos = new ObjectOutputStream(fis);
			oos.writeObject(listaArchivosRecientes);
			oos.close();
			fis.close();
		} catch (IOException e) {
			System.out.println("Hubo un problema al guardar los datos de archivos recientes.");
			e.printStackTrace();
		}
		cargaArchivosRecientes();
	}
	
	private void agregaEscuchadores() {
		// Evitar que cualquier vaina pueda tener foco, sólo el texto ocupa tener foco, creo yo
			// El mismo JFrame
				setFocusable(false);
			// Panel de pestañas
				codigoTabs.setFocusable(false);

			// Los menus
				menuArchivo.setFocusable(false);
					nuevoArchivo.setFocusable(false);
					cargarArchivo.setFocusable(false);
					guardarArchivo.setFocusable(false);
					guardarComo.setFocusable(false);
					menuArchivosRecientes.setFocusable(false);
						// los items de este menu son volatiles, se le pone el focusable(false) en el momento que se crean, en cargaArchivosRecientes()
					salir.setFocusable(false);
				menuOpciones.setFocusable(false);
					cambiarTema.setFocusable(false);
					personalizarTema.setFocusable(false);
					acercaDe.setFocusable(false);
		
		// escuchadores
			escuchadores = new Escuchadores(this);
			// Teclas
				for(int i=0; i<txtCodigo.length(); i++)
					txtCodigo.getByIndex(i).dato.addKeyListener(escuchadores);
				addKeyListener(escuchadores);
			
			// Items del menu
				// Archivo
					nuevoArchivo.addActionListener(escuchadores);
					cargarArchivo.addActionListener(escuchadores);
					guardarArchivo.addActionListener(escuchadores);
					guardarComo.addActionListener(escuchadores);
					salir.addActionListener(escuchadores);
				// Opciones
					cambiarTema.addActionListener(escuchadores);
					personalizarTema.addActionListener(escuchadores);
					ayuda.addActionListener(escuchadores);
					acercaDe.addActionListener(escuchadores);
				
			// Cambio entre pestañas
				codigoTabs.addMouseListener(escuchadores);
				for(int i=0; i<txtCodigo.length(); i++)
					txtCodigo.getByIndex(i).dato.addAncestorListener(escuchadores);
				
			// Ventana
				addWindowListener(escuchadores);
	}
	
	public void nuevaPestaña(String texto, String ruta, String nombre, int tamaño) {
		int tabs = codigoTabs.getTabCount();
		CodePane codePane;
		if(tamaño >= 0) { // significa que es una adición normal
			if(texto.length() == 0) {
				texto = "Puro crack aquí.";
				tamaño = texto.length();
			}
			JTextPane cajaDeTexto = new JTextPane() {
				public boolean getScrollableTracksViewportWidth()
			    {
			        return getUI().getPreferredSize(this).width 
			            <= getParent().getSize().width;
			    }
			};
			cajaDeTexto.setFont(fuenteConsola);
			codePane = new CodePane(cajaDeTexto);
			Theme.changeTheme(codePane, tema);
			cajaDeTexto.setText(texto);
			txtCodigo.insertar(cajaDeTexto);
			cajaDeTexto.addKeyListener(escuchadores);
			cajaDeTexto.addAncestorListener(escuchadores);
			rutaDeArchivoActual.insertar(ruta);
			nombreDelArchivo.insertar(nombre);
			if(ruta.length() == 0)
				tituloVentana.insertar("CRACK'S Code");
			else
				tituloVentana.insertar(nombre + " - " + ruta + " - CRACK'S Code");
			tamañoTextoDelEditor.insertar(tamaño);
			cambiosGuardados.insertar(true);
		} // de ser tamaño un negativo es porque se quiere la pura pestaña y los datos anteriores ya se tienen
		else {
			codePane = new CodePane(txtCodigo.getByIndex(tabs-1).dato);
			Theme.changeTheme(codePane, tema);
		}
		
		codigoTabs.insertTab("esto no debería salir nunca", null, codePane, "Archivo sin guardar", tabs);
		if(tamaño >=0) {
			String toolTipText = ruta;
			if(ruta.length() == 0)
				toolTipText = "Archivo sin guardar";
			PanelPestaña panelPestaña = new PanelPestaña(nombre, toolTipText, this, contadorDePestañas++);
			titulo.insertar(panelPestaña);
			codigoTabs.setTabComponentAt(Math.max(0, tabs), panelPestaña);
		}
		
		codigoTabs.removeTabAt(codigoTabs.getTabCount()-2); // elimina el +
		codigoTabs.insertTab("", iconoNuevoTab, null, "Nueva pestaña", codigoTabs.getTabCount()); // añade el +
		codigoTabs.setSelectedIndex(codigoTabs.getTabCount()-2); // selecciona la última pestaña código
		
		txtCodigo.getByIndex(tabs-1).dato.requestFocus();
//		System.out.println("tamaño del texto: "+tamaño);
	}
	
	public void actualizarBotonesDePestañas() {
		for(int i=0; i<titulo.length(); i++)
			if(i != getSelectedTab())
				titulo.getByIndex(i).dato.boton.setIcon(null);
			else
				titulo.getByIndex(i).dato.boton.setIcon(icoCerrarPestaña);
	}

	public void nuevaPestaña() {
		nuevaPestaña("", "", "Sin título", 0);
	}
	
	public void cerrarPestaña(int tabs) {
		if(!cambiosGuardados.getByIndex(tabs).dato) {
			Escuchadores es = escuchadores;
			es.teclaShift = es.teclaAlt = es.teclaControl = false;
			String texto = "<div>No ha guardado los cambios hechos en <b><u>"+nombreDelArchivo.getByIndex(tabs).dato+"</u></b>."
					+ "<br />¿Desea guardar los cambios antes de cerrar la pestaña?";
			String css = 
					"div {"
					+ "	text-align: center;"
					+ "	font-weight: plain;"
					+ "}";
			texto = Statics.getHTML(texto, css);
			final int opcion = JOptionPane.showConfirmDialog(null, new JLabel(texto), "Cambios no guardados", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			switch(opcion) {
			case JOptionPane.YES_OPTION:
				guardarArchivo.doClick();
				break;
			case JOptionPane.NO_OPTION:
				break;
			default:
				return;
			}
		}
		titulo.borrar(tabs);
		txtCodigo.borrar(tabs);
		rutaDeArchivoActual.borrar(tabs);
		nombreDelArchivo.borrar(tabs);
		tituloVentana.borrar(tabs);
		tamañoTextoDelEditor.borrar(tabs);
		cambiosGuardados.borrar(tabs);
		codigoTabs.remove(tabs);
		if(codigoTabs.getTabCount()==1) {
			nuevaPestaña();
			txtCodigo.getByIndex(getSelectedTab()).dato.requestFocus();
		}
		int tabASeleccionar = getSelectedTab();
		if(getSelectedTab()==codigoTabs.getTabCount()-1)
			tabASeleccionar = codigoTabs.getTabCount()-2;
		codigoTabs.setSelectedIndex(tabASeleccionar);
		txtCodigo.getByIndex(tabASeleccionar).dato.requestFocus();
	}
	
	public void cerrarPestaña() {
		cerrarPestaña(getSelectedTab());
	}
	
	private void cargarArchivo(String ruta) {
		try {
			InputStreamReader inputStreamReader;
			File fichero = new File(ruta);
			FileInputStream ficheroInputStream = new FileInputStream(fichero);
			inputStreamReader =
				new InputStreamReader(ficheroInputStream, Charset.forName("UTF-8"));
			String texto = "";
			int dato = inputStreamReader.read();
			if(inputStreamReader.ready()) {
				while(dato != -1) {
					char caracterActual = (char) dato;
					dato = inputStreamReader.read();
					texto+= "" + caracterActual;
				}
				//txtCodigo.getByIndex(getSelectedTab()).dato.setText(str);
			}
			else
				System.out.print("El archivo no está listo para su lectura.");
			inputStreamReader.close();
			final int tabs = getSelectedTab();
			nuevaPestaña(texto, fichero.getPath(), fichero.getName(), texto.length());
			tituloVentana.getByIndex(tabs).dato = getTitle();
			
			codigoTabs.setToolTipTextAt(tabs+1, ruta);
			añadirArchivoARecientes(fichero.getName(), ruta);
		} catch(FileNotFoundException e) {
			String mensaje = "No se ha encontrado el archivo que se trató de abrir.";
			if(eliminarDeRecientes(ruta))
				mensaje+="\nSe ha eliminado de la lista de archivos recientes.";
        	JOptionPane.showMessageDialog(null, mensaje, "Archivo no encontrado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean eliminarDeRecientes(String ruta) {
		try {
			for(int i=0; i < listaArchivosRecientes.size(); i++) {
				if(listaArchivosRecientes.get(i)[1].equals(ruta)) {
					FileOutputStream fis = new FileOutputStream(archivoDeArchivosRecientes);
					ObjectOutputStream oos = new ObjectOutputStream(fis);
					listaArchivosRecientes.remove(i);
					oos.writeObject(listaArchivosRecientes);
					oos.close();
					fis.close();
					cargaArchivosRecientes();
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void cargarArchivo() {
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de cracks (*.txt, *.java, *.crack)", "txt", "java", "crack");
        
        JFileChooser explorador = new JFileChooser();              //Muestra una ventana que permite navegar por los directorios
        explorador.setDialogTitle("Abrir");                        //Agrega título al cuadro de diálogo
        explorador.setFileFilter(filtro);                          //Se agrega el filtro de tipo de archivo al cuadro de diálogo
 
        if (explorador.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        	cargarArchivo(explorador.getSelectedFile().getAbsolutePath());
		}
	}
	
	public void guardarArchivo(String ruta) {
        try {
			FileWriter archivo = new FileWriter(ruta);
			PrintWriter escritor = new PrintWriter(archivo);
			
			final int tabs = getSelectedTab();
			JTextPane codigoEnPestaña = txtCodigo.getByIndex(tabs).dato;
			String str = codigoEnPestaña.getText();
			escritor.print(str); 
			archivo.close();
			escritor.close();
			setTitle(nombreDelArchivo.getByIndex(tabs).dato + " - " + rutaDeArchivoActual.getByIndex(tabs).dato + " - CRACK'S Code");
			tituloVentana.getByIndex(tabs).dato = getTitle();
			String nombreDeArchivo = new File(ruta).getName(),
				toolTipText = ruta;
			titulo.getByIndex(tabs).dato.setTitle(nombreDeArchivo, toolTipText);
			
			añadirArchivoARecientes(nombreDeArchivo, toolTipText);
			codigoTabs.setToolTipTextAt(tabs, ruta);
        }catch (Exception e) {
        	e.printStackTrace();
	    }
	}
	
	public void guardarArchivoComo() {
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de cracks (*.txt, *.java, *.crack)", "txt", "java", "crack");
		JFileChooser explorador = new JFileChooser();              //Muestra una ventana que permite navegar por los directorios
        explorador.setApproveButtonText("Guardar");
		explorador.setDialogTitle("Guardar como");                 //Agrega título al cuadro de diálogo
        explorador.setFileFilter(filtro);                          //Se agrega el filtro de tipo de archivo al cuadro de diálogo
 
        try {
	        if (explorador.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File fichero = explorador.getSelectedFile();      
				
				int tabs = getSelectedTab();
				rutaDeArchivoActual.getByIndex(tabs).dato = fichero.getPath();
				nombreDelArchivo.getByIndex(tabs).dato = fichero.getName();
				guardarArchivo(fichero.getPath());
				setTitle(nombreDelArchivo.getByIndex(tabs).dato + " - " + rutaDeArchivoActual.getByIndex(tabs).dato + " - CRACK'S Code");
				añadirArchivoARecientes(fichero.getName(), fichero.getAbsolutePath());

				JOptionPane.showMessageDialog(null, "El archivo ha sido guardado con éxito.", "", JOptionPane.INFORMATION_MESSAGE);
			}
		}catch (Exception e) {
        	e.printStackTrace();
	    }
	}
	
	public int getSelectedTab() {
		return codigoTabs.getSelectedIndex();
	}
}


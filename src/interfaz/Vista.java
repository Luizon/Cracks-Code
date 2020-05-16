package interfaz;

import java.io.Serializable;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
		menuCompilar,
		menuOpciones,
		menuArchivosRecientes;
	public JMenuItem nuevoArchivo,
		cargarArchivo,
		guardarArchivo,
		guardarComo,
		salir,
		compilarCodigo,
		cambiarTema,
		ayuda,
		acercaDe;
	public JSplitPane dividor;
	public JTabbedPane codigoTabs,
		bottomTabs;
	public ListaDoble<JTextPane> txtCodigo;
	public File archivoTemporal;
	public ListaDoble<String> rutaDeArchivoActual,
		nombreDelArchivo,
		tituloVentana;
	public ListaDoble<Integer> tamañoTextoDelEditor;
	public ListaDoble<Boolean> cambiosGuardados;
	public ListaDoble<PanelPestaña> titulo;
	private ArrayList<String[]> listaArchivosRecientes;
	public JList<String> consola,
		cuadruplos;
	public Escuchadores escuchadores;
	public int tabulaciones = 0,
		tema = Theme.CLARO,
		posicionRelativaDelDividorDeLosTabbedPane = 0;
	public boolean hayError = false; // hacer algo con esto después, no me gusta
	public final String [] tituloTabla = new String[]{"Posición", "Alcance", "Tipo", "Simbolo", "Valor"};
	public DefaultTableModel modelo;
	public JTable tablaDatos;
	private final String carpetaDeAppData = System.getenv("APPDATA") + "/Cracks Code/",
		archivoDeArchivosRecientes = carpetaDeAppData+"recientes.txt",
		archivoUltimoTemaUsado = carpetaDeAppData+"ultimoTemaUsado.txt",
		archivoUltimaSesion = carpetaDeAppData+"ultimaSesion.txt";
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
		Theme.changeTheme(this);
		creaFicheroTemporal();
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
		crearPestañasDeAbajo();
		
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
				txtCodigo}; //6
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
		menuCompilar = new JMenu(Statics.getHTML(run+"<p>Compilar", css));
		menuOpciones = new JMenu(Statics.getHTML(settings+"<p>Opciones", css));
		barraDelMenu.add(menuArchivo);
		barraDelMenu.add(menuCompilar);
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
		compilarCodigo = new JMenuItem(Statics.getHTML(l+"Compilar código "+a+"(F5)", css));
		compilarCodigo.setIcon(new ImageIcon(Statics.getImage("run2")));
		cambiarTema = new JMenuItem(Statics.getHTML(l+"Cambiar de tema"+a+"(F11)", css));
		cambiarTema.setIcon(new ImageIcon(Statics.getImage("theme2")));
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
		menuCompilar.add(compilarCodigo);
		menuOpciones.add(cambiarTema);
		menuOpciones.add(ayuda);
		menuOpciones.add(acercaDe);
	}
	
	public void cambiarTema() {
		if(tema == Theme.CLARO) {
			tema = Theme.OSCURO;
		} else { // si no es claro el que traías, traías el oscuro, cambias al claro
			tema = Theme.CLARO;
		}
		Theme.changeTheme(this);
		
		guardarUltimoTemaUsado();
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
	
	private void crearPestañasDeAbajo() {
		bottomTabs = new JTabbedPane();
		consola = new JList<String>();
		cuadruplos = new JList<String>();
		consola.setFont(fuenteConsola);
		cuadruplos.setFont(fuenteConsola);
		DefaultListCellRenderer renderer = (DefaultListCellRenderer)cuadruplos.getCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		JScrollPane pestañaConsola = new JScrollPane(consola);
		JScrollPane pestañaCuadruplos = new JScrollPane(cuadruplos);
		modelo = new DefaultTableModel(new Object[0][0], tituloTabla);
		tablaDatos = new JTable(modelo);
		tablaDatos.getTableHeader().setFont(fontHeader);
		JScrollPane pestañaDatos = new JScrollPane(tablaDatos);
		pestañaConsola.setFocusable(false);
		pestañaCuadruplos.setFocusable(false);
		pestañaDatos.setFocusable(false);
		bottomTabs.insertTab("Consola", icoBug, pestañaConsola, "Consola con información del último código compilado", 0);
		bottomTabs.insertTab("Datos", icoTable, pestañaDatos, "Tabla con datos guardados del último código compilado", 1);
		bottomTabs.insertTab("Cuadruplos", icoTable, pestañaCuadruplos, "Tablas de cuadruplos de expresiones encontradas en el último código compilado", 2);
		
		dividor = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		posicionRelativaDelDividorDeLosTabbedPane = 300;
		dividor.setDividerLocation(getHeight()-posicionRelativaDelDividorDeLosTabbedPane);
        dividor.setTopComponent(codigoTabs);
		dividor.setBottomComponent(bottomTabs);
		add(dividor);
	}
	
	private void creaFicheroTemporal() {
		try {
			archivoTemporal = File.createTempFile("archivoTemporal",null);
			archivoTemporal.deleteOnExit();
		}
		catch (Exception e) {
			System.out.println("Error al guardar el archivo temporal para compilar su código.");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error al guardar el archivo temporal para compilar su código.","Alerta",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void agregaEscuchadores() {
		// Evitar que cualquier vaina pueda tener foco, sólo el texto ocupa tener foco, creo yo
			// El mismo JFrame
				setFocusable(false);
			// Paneles de pestañas
				dividor.setFocusable(false);
					codigoTabs.setFocusable(false);
					bottomTabs.setFocusable(false);

			// Paneles de las pestañas
				tablaDatos.setFocusable(false);
				consola.setFocusable(false); // la wea de la pestaña Consola
				cuadruplos.setFocusable(false); // la wea de la pestaña Consola
				
			// Los menus
				menuArchivo.setFocusable(false);
					nuevoArchivo.setFocusable(false);
					cargarArchivo.setFocusable(false);
					guardarArchivo.setFocusable(false);
					guardarComo.setFocusable(false);
					menuArchivosRecientes.setFocusable(false);
						// los items de este menu son volatiles, se le pone el focusable(false) en el momento que se crean, en cargaArchivosRecientes()
					salir.setFocusable(false);
				menuCompilar.setFocusable(false);
					compilarCodigo.setFocusable(false);
				menuOpciones.setFocusable(false);
					cambiarTema.setFocusable(false);
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
				// Compilar
					compilarCodigo.addActionListener(escuchadores);
				// Opciones
					cambiarTema.addActionListener(escuchadores);
					ayuda.addActionListener(escuchadores);
					acercaDe.addActionListener(escuchadores);
				
			// Cambio entre pestañas
				consola.addAncestorListener(escuchadores);
				tablaDatos.addAncestorListener(escuchadores);
				codigoTabs.addMouseListener(escuchadores);
				for(int i=0; i<txtCodigo.length(); i++)
					txtCodigo.getByIndex(i).dato.addAncestorListener(escuchadores);
				
			// Ventana
				addWindowListener(escuchadores);
	}
	
	public void nuevaPestaña(String texto, String ruta, String nombre, int tamaño) {
		int tabs = codigoTabs.getTabCount();
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
			cajaDeTexto.setText(texto);
			txtCodigo.insertar(cajaDeTexto);
			cajaDeTexto.setFont(fuenteConsola);
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
		
		CodePane codePane = new CodePane(txtCodigo.getByIndex(tabs-1).dato);
		Theme.changeTheme(codePane, tema);
		
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
			System.out.println("codigo: " + inputStreamReader.getEncoding());
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


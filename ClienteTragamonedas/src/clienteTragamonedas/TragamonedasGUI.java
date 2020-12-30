/********************************************
 * Christian Camilo Taborda Campiño         *
 * Código: 1632081-3743                     *
 * Fecha de creación: 12/05/2017            *
 * Fecha de última modificación: 20/05/2017 *
 * ****************************************** 
 */

package clienteTragamonedas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

public class TragamonedasGUI extends JFrame{
	
	//ATRIBUTOS:
	private static final int FILAS = 3, COLUMNAS = 5;
	private JPanel superior, central, inferior, informacion, bombillos, numeros, botones, botonesL, etiquetas, matriz;
	private Container contenedor;
	private JLabel titulo, usuario, pago, creditos, apuesta, ePago, eCreditos, eApuesta, lApuestas;
	private JLabel[] luces, cintas;
	private JButton[] lineas;
	private JButton girar, ayuda, guardar;
	private JLabel[][] tablero;
	private ImageIcon[] imagenes;
	private String nombre;
	private Clip audioGiro;
	
	//Reproduce una pista de audio:
	public void reproducirAudio(String pista, boolean tipo){
		try{	
			
			//Importación y reproducción del sonido:
			URL url = getClass().getResource("/audio/" + pista);
			AudioInputStream entrada = AudioSystem.getAudioInputStream(url);
			if(tipo){
				audioGiro = AudioSystem.getClip();
				audioGiro.open(entrada);
			}else{
				Clip clip = AudioSystem.getClip();
				clip.open(entrada);
				clip.start();
			}
			
		}catch(IOException E){
			E.printStackTrace();
		}
		catch(LineUnavailableException E){
			E.printStackTrace();
		}
		catch(UnsupportedAudioFileException E){
			E.printStackTrace();
		}		
	}
	
	//Edita la fuente y color de fondo de los JOptionPane:
	public void editarJOptionPane(){
		
		//Cambio de color de fondo para los JOptionPane:
		UIManager.put("OptionPane.background",new ColorUIResource(Color.YELLOW));
		UIManager.put("Panel.background",new ColorUIResource(Color.YELLOW));
		
		//Inicialización de fuentes para los JOptionPane:
		Font label = new Font("Arial Black",Font.BOLD,22);
		Font button = new Font("Arial Black",Font.BOLD,15);
		
		//Cambio de fuente para los JOptionPane:
		UIManager.put("OptionPane.messageFont", label);
		UIManager.put("OptionPane.buttonFont", button);
		
	}
	
	//Ubica las imágenes de una columna del tablero:
	public void pintarColumna(int nColumna, String[] columna){
		int contador = 0;
		for(int y=0; y<FILAS; y++){
			switch(columna[contador]){
				case "A":
					tablero[y][nColumna].setIcon(imagenes[10]);
					break;
				case "B":
					tablero[y][nColumna].setIcon(imagenes[11]);
					break;
				default:
					tablero[y][nColumna].setIcon(imagenes[Integer.parseInt(columna[contador])]);
					break;
			}				
			contador++;
		}
	}
	
	//Transforma el mensaje con la matriz en una matriz de cadenas:
	public String[][] transformarMatriz(String sTablero){
		String[][] matriz = new String[FILAS][COLUMNAS];
		int contador = 0;
		for(int x=0; x<FILAS; x++){
			for(int y=0; y<COLUMNAS; y++){
				switch(sTablero.charAt(contador)){
					case 'A':
						matriz[x][y] = "A";
						break;
					case 'B':
						matriz[x][y] = "B";
						break;
					default:
						matriz[x][y] = "" + (sTablero.charAt(contador) - 48);
						break;
				}
				contador++;
			}
		}
		return matriz;
	}
	
	//Ubica las imágenes del tablero a partir de un mensaje:
	public void pintarTablero(String sTablero){
		
		String[][] matriz = transformarMatriz(sTablero);
		
		//Inicialización de cada columna a pintar:
		String[] C0 = {matriz[0][0],matriz[1][0],matriz[2][0]}; 
		String[] C1 = {matriz[0][1],matriz[1][1],matriz[2][1]};
		String[] C2 = {matriz[0][2],matriz[1][2],matriz[2][2]};
		String[] C3 = {matriz[0][3],matriz[1][3],matriz[2][3]};
		String[] C4 = {matriz[0][4],matriz[1][4],matriz[2][4]};
		
		//Pintado de las columnas:
		pintarColumna(0,C0);
		pintarColumna(1,C1);
		pintarColumna(2,C2);
		pintarColumna(3,C3);
		pintarColumna(4,C4);
		
	}
	
	//Inicializa el tablero del juego:
	public void initTablero(String sTablero){
		tablero = new JLabel[FILAS][COLUMNAS];
		for(int x=0; x<FILAS; x++){
			for(int y=0; y<COLUMNAS; y++){
				tablero[x][y] = new JLabel();
				tablero[x][y].setPreferredSize(new Dimension(150,165));
				tablero[x][y].setHorizontalAlignment(JLabel.CENTER);
				tablero[x][y].setBackground(Color.BLACK);
				tablero[x][y].setBorder(BorderFactory.createRaisedBevelBorder());
				tablero[x][y].setOpaque(true);
				
			}
		}
		pintarTablero(sTablero);
	}
	
	//Inicializa los botones de la GUI:
	public void initBotones(JButton girar, JButton guardar){
		
		//Inicialización de los botones para apostar:
		lineas = new JButton[COLUMNAS];
		Escucha escucha = new Escucha();
		int contador = 1;
		for(int x=0; x<COLUMNAS; x++){
			if(x+contador == 1){
				lineas[x] = new JButton("<html><center>" + (x+contador) + "<br/> línea </center></html>");
			}else{
				lineas[x] = new JButton("<html><center>" + (x+contador) + "<br/> líneas </center></html>");
			}					
			lineas[x].setBorder(BorderFactory.createRaisedBevelBorder());
			lineas[x].setFont(new Font("Arial Black", Font.BOLD, 24));
			lineas[x].setForeground(new Color(45,49,146));
			lineas[x].setHorizontalAlignment(JLabel.CENTER);
			lineas[x].setOpaque(true);
			lineas[x].addActionListener(escucha);
			contador++;
		}
		
		//Inicialización del botón de ayuda:
		EscuchaAyuda escuchaAyuda = new EscuchaAyuda();
		ayuda = new JButton("    Ver Pagos    ");
		ayuda.setBorder(BorderFactory.createRaisedBevelBorder());
		ayuda.setFont(new Font("Arial Black", Font.BOLD, 20));
		ayuda.setForeground(Color.YELLOW);
		ayuda.setBackground(new Color(45,49,146));
		ayuda.setOpaque(true);
		ayuda.setFocusable(false);
		ayuda.addActionListener(escuchaAyuda);
		
		//Asignación del valor a los botones a partir de los parámetros:
		this.guardar = guardar;
		this.girar = girar;
		this.girar.setEnabled(false);
		
	}
	
	//Inicializa las etiquetas con el efecto giratorio:
	public void initCintas(){
		
		cintas = new JLabel[COLUMNAS];
		
		for(int x=0; x<COLUMNAS; x++){
			cintas[x] = new JLabel();					
			cintas[x].setBorder(null);
			cintas[x].setFocusable(false);
			if(x%2 == 0){
				cintas[x].setIcon(imagenes[19]);
			}else{
				cintas[x].setIcon(imagenes[20]);
			}
			cintas[x].setOpaque(false);
		}
		
		cintas[0].setBounds(0,0,170,362);
		cintas[1].setBounds(170,0,170,362);
		cintas[2].setBounds(340,0,170,362);
		cintas[3].setBounds(510,0,170,362);
		cintas[4].setBounds(680,0,170,362);
		
	}
	
	//Inicializa las imágenes de la GUI:
	public void initImagenes(){
		
		//Creación de los arreglos de imágenes:
		imagenes = new ImageIcon[26];
		
		//Carga de imágenes:
		for(int x=0; x<12; x++){
			imagenes[x] = new ImageIcon(getClass().getResource("/imagenes/" + x + ".png"));
		}
		imagenes[12] = new ImageIcon(getClass().getResource("/imagenes/luz.gif"));
		imagenes[13] = new ImageIcon(getClass().getResource("/imagenes/titulo.png"));	
		imagenes[14] = new ImageIcon(getClass().getResource("/imagenes/error.png"));
		imagenes[15] = new ImageIcon(getClass().getResource("/imagenes/comodin.png"));
		imagenes[16] = new ImageIcon(getClass().getResource("/imagenes/pago1.png"));
		imagenes[17] = new ImageIcon(getClass().getResource("/imagenes/pago2.png"));
		imagenes[18] = new ImageIcon(getClass().getResource("/imagenes/pago3.png"));
		imagenes[19] = new ImageIcon(getClass().getResource("/imagenes/giro1.gif"));
		imagenes[20] = new ImageIcon(getClass().getResource("/imagenes/giro2.gif"));
		imagenes[21] = new ImageIcon(getClass().getResource("/imagenes/l1.png"));
		imagenes[22] = new ImageIcon(getClass().getResource("/imagenes/l3.png"));
		imagenes[23] = new ImageIcon(getClass().getResource("/imagenes/l5.png"));
		imagenes[24] = new ImageIcon(getClass().getResource("/imagenes/l7.png"));
		imagenes[25] = new ImageIcon(getClass().getResource("/imagenes/l9.png"));
		
	}
	
	//Inicializa las etiquetas con los bombillos de la máquina:
	public void initLuces(){
		luces = new JLabel[14];
		for(int x=0; x<14; x++){
			luces[x] = new JLabel();
			luces[x].setBackground(Color.YELLOW);
			luces[x].setIcon(imagenes[12]);
			luces[x].setOpaque(true);
		}
	}
	
	//Inicializa las etiquetas de la GUI:
	public void initLabels(String nombre, String nCreditos){
		
		//Inicialización del título:
		titulo = new JLabel();
		titulo.setBackground(Color.YELLOW);
		titulo.setBorder(BorderFactory.createRaisedBevelBorder());
		titulo.setHorizontalAlignment(JLabel.CENTER);
		titulo.setIcon(imagenes[13]);
		titulo.setOpaque(true);
		
		//Inicialización del usuario:
		this.nombre = nombre;
		usuario = new JLabel("¡Hola " + nombre + "!");
		usuario.setBorder(BorderFactory.createRaisedBevelBorder());
		usuario.setFont(new Font("Arial Black", Font.BOLD, 24));
		usuario.setForeground(Color.RED);
		usuario.setBackground(Color.BLACK);
		usuario.setHorizontalAlignment(JLabel.CENTER);
		usuario.setOpaque(true);
		
		//Inicialización del pago:
		pago = new JLabel("0");
		pago.setBorder(BorderFactory.createRaisedBevelBorder());
		pago.setFont(new Font("Arial Black", Font.BOLD, 28));
		pago.setForeground(Color.RED);
		pago.setBackground(Color.BLACK);
		pago.setHorizontalAlignment(JLabel.CENTER);
		pago.setOpaque(true);
		
		//Inicialización de la apuesta:
		apuesta = new JLabel("0");
		apuesta.setBorder(BorderFactory.createRaisedBevelBorder());
		apuesta.setFont(new Font("Arial Black", Font.BOLD, 28));
		apuesta.setForeground(Color.RED);
		apuesta.setBackground(Color.BLACK);
		apuesta.setHorizontalAlignment(JLabel.CENTER);
		apuesta.setOpaque(true);
		
		//Inicialización de los créditos:
		creditos = new JLabel(nCreditos);
		creditos.setBorder(BorderFactory.createRaisedBevelBorder());
		creditos.setFont(new Font("Arial Black", Font.BOLD, 28));
		creditos.setForeground(Color.RED);
		creditos.setBackground(Color.BLACK);
		creditos.setHorizontalAlignment(JLabel.CENTER);
		creditos.setOpaque(true);	
		
		//Inicialización de la etiqueta del pago:
		ePago = new JLabel("Pago");
		ePago.setFont(new Font("Arial Black", Font.BOLD, 25));
		ePago.setForeground(Color.YELLOW);
		ePago.setBackground(new Color(45,49,146));
		ePago.setHorizontalAlignment(JLabel.CENTER);
		ePago.setOpaque(true);
		
		//Inicialización de la etiqueta de los créditos:
		eCreditos = new JLabel("Créditos");
		eCreditos.setFont(new Font("Arial Black", Font.BOLD, 25));
		eCreditos.setForeground(Color.YELLOW);
		eCreditos.setBackground(new Color(45,49,146));
		eCreditos.setHorizontalAlignment(JLabel.CENTER);
		eCreditos.setOpaque(true);
		
		//Inicialización de la etiqueta de la apuesta:
		eApuesta = new JLabel("Apuesta");
		eApuesta.setFont(new Font("Arial Black", Font.BOLD, 25));
		eApuesta.setForeground(Color.YELLOW);
		eApuesta.setBackground(new Color(45,49,146));
		eApuesta.setHorizontalAlignment(JLabel.CENTER);
		eApuesta.setOpaque(true);
		
		//Inicialización del botón con las líneas de apuesta:
		lApuestas = new JLabel();
		lApuestas.setBorder(null);
		lApuestas.setFocusable(false);
		lApuestas.setBackground(new Color(255,255,255,0));
		lApuestas.setOpaque(true);
		lApuestas.setBounds(0,0,850,362);
		
	}
	
	//Inicializa los paneles de la GUI:
	public void initPaneles(){
		
		//Inicialización de los paneles externos:
		superior = new JPanel(new BorderLayout());
		central = new JPanel();
		central.setLayout(null);
		inferior = new JPanel(new BorderLayout());
		
		//Inicialización de los paneles internos:
		
		matriz = new JPanel(new GridLayout(3,5));
		
		informacion = new JPanel(new BorderLayout());
		
		bombillos = new JPanel(new FlowLayout());
		bombillos.setBackground(Color.YELLOW);
		
		numeros = new JPanel(new BorderLayout());
		
		botones = new JPanel(new FlowLayout());
		botones.setBackground(Color.YELLOW);
		
		etiquetas = new JPanel(new BorderLayout());
		etiquetas.setBackground(new Color(45,49,146));
		
		botonesL = new JPanel(new BorderLayout());
		botonesL.setBackground(Color.YELLOW);
		
	}
	
	//Inicializa los componentes gráficos de la GUI:
	public void initGUI(String paquete, JButton girar, JButton guardar){
		
		//Procesamiento del mensaje del parámetro:
		String[] datos = paquete.split(" ");

		//Inicialización de las imágenes:
		initImagenes();
		
		//Inicialización de los botones con el efecto de giro:
		initCintas();
		
		//Inicialización del audio del giro:
		reproducirAudio("giro.wav",true);
		
		//Inicialización de los paneles:
		initPaneles();
		
		//Inicialización del tablero:
		initTablero(datos[0]);
		
		//Inicialización de los botones:
		initBotones(girar,guardar);
		
		//Inicialización de los bombillos:
		initLuces();
		
		//Inicialización de las etiquetas:
		initLabels(datos[2], datos[1]);
		
		//Inicialización del contenedor de la GUI:
		contenedor = this.getContentPane();
		contenedor.setLayout(new BorderLayout());
		
		//Inclusión de componentes en paneles internos:
		
		informacion.add(ayuda, BorderLayout.EAST);
		informacion.add(usuario, BorderLayout.CENTER);
		informacion.add(guardar, BorderLayout.WEST);
		
		for(int x=0; x<14; x++){
			bombillos.add(luces[x]);
		}
		
		superior.add(titulo, BorderLayout.NORTH);
		superior.add(informacion, BorderLayout.CENTER);
		superior.add(bombillos, BorderLayout.SOUTH);
		
		central.add(lApuestas);	
		
		for(int x=0; x<FILAS; x++){
			for(int y=0; y<COLUMNAS; y++){
				matriz.add(tablero[x][y]);
			}
		}
		
		matriz.setBounds(0,0,850,362);		
		central.add(matriz);
		
		for(int x=0; x<COLUMNAS; x++){
			central.add(cintas[x]);
		}
		
		numeros.add(pago, BorderLayout.WEST);
		numeros.add(creditos, BorderLayout.CENTER);
		numeros.add(apuesta, BorderLayout.EAST);
		
		for(int x=0; x<COLUMNAS; x++){
			botones.add(lineas[x]);
		}
		
		botonesL.add(botones, BorderLayout.CENTER);
		botonesL.add(this.girar, BorderLayout.EAST);
		
		etiquetas.add(ePago, BorderLayout.WEST);
		etiquetas.add(eCreditos, BorderLayout.CENTER);
		etiquetas.add(eApuesta, BorderLayout.EAST);
		
		inferior.add(etiquetas, BorderLayout.NORTH);
		inferior.add(numeros, BorderLayout.CENTER);
		inferior.add(botonesL, BorderLayout.SOUTH);
		
		//Inclusión de los paneles externos al contenedor:
		contenedor.add(superior, BorderLayout.NORTH);
		contenedor.add(central, BorderLayout.CENTER);
		contenedor.add(inferior, BorderLayout.SOUTH);
		
	}
	
	//Constructor:
	public TragamonedasGUI(String paquete, JButton girar, JButton guardar){
		initGUI(paquete, girar, guardar);
		setTitle("MEGA-TRAGAMONEDAS");
		ImageIcon icono = new ImageIcon(getClass().getResource("/imagenes/icono.png"));
		setIconImage(icono.getImage());
		setSize(850,725);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		reproducirAudio("saludo.wav",false);
		setVisible(true);		
	}
	
	//Extrae las etiquetas de una línea del tablero:
	public JLabel[] obtenerLinea(int L){
		JLabel[] linea = new JLabel[5];
		switch(L){
			case 1:
				linea[0] = tablero[1][0];
				linea[1] = tablero[1][1];
				linea[2] = tablero[1][2];
				linea[3] = tablero[1][3];
				linea[4] = tablero[1][4];
				break;
			case 2:
				linea[0] = tablero[0][0];
				linea[1] = tablero[0][1];
				linea[2] = tablero[0][2];
				linea[3] = tablero[0][3];
				linea[4] = tablero[0][4];
				break;
			case 3:
				linea[0] = tablero[2][0];
				linea[1] = tablero[2][1];
				linea[2] = tablero[2][2];
				linea[3] = tablero[2][3];
				linea[4] = tablero[2][4];
				break;
			case 4:
				linea[0] = tablero[0][0];
				linea[1] = tablero[1][1];
				linea[2] = tablero[2][2];
				linea[3] = tablero[1][3];
				linea[4] = tablero[0][4];
				break;
			case 5:
				linea[0] = tablero[2][0];
				linea[1] = tablero[1][1];
				linea[2] = tablero[0][2];
				linea[3] = tablero[1][3];
				linea[4] = tablero[2][4];
				break;
			case 6:
				linea[0] = tablero[0][0];
				linea[1] = tablero[0][1];
				linea[2] = tablero[1][2];
				linea[3] = tablero[2][3];
				linea[4] = tablero[2][4];
				break;
			case 7:
				linea[0] = tablero[2][0];
				linea[1] = tablero[2][1];
				linea[2] = tablero[1][2];
				linea[3] = tablero[0][3];
				linea[4] = tablero[0][4];
				break;
			case 8:
				linea[0] = tablero[1][0];
				linea[1] = tablero[2][1];
				linea[2] = tablero[1][2];
				linea[3] = tablero[0][3];
				linea[4] = tablero[1][4];
				break;
			case 9:
				linea[0] = tablero[1][0];
				linea[1] = tablero[0][1];
				linea[2] = tablero[1][2];
				linea[3] = tablero[2][3];
				linea[4] = tablero[1][4];
				break;
		}
		return linea;
	}
	
	//Pinta el fondo del tablero de negro:
	public void quitarMatch(){
		
		lApuestas.setIcon(null);
		lApuestas.setOpaque(false);
		
		for(int x=0; x<FILAS; x++){
			for(int y=0; y<COLUMNAS; y++){
				tablero[x][y].setBackground(Color.BLACK);
			}
		}
		
	}
	
	//Pinta una línea del tablero de color cyan:
	public void pintarMatch(String match){
		quitarMatch();
		String[] datos = match.split("_");
		JLabel[] linea;
		linea = obtenerLinea(Integer.parseInt(datos[0]));
		for(int x=0; x<Integer.parseInt(datos[1]); x++){
			linea[x].setBackground(Color.CYAN);
		}
		reproducirAudio("menu.wav",false);
	}
	
	//Muestra el decremento de pago e incremento de créditos:
	public void mostrarPago(String sPago){
		Thread hilo = new Thread(){
			public synchronized void run(){
				try{
					int nPago = Integer.parseInt(sPago);
					int nCreditos = Integer.parseInt(creditos.getText());
					pago.setText("" + nPago);
					reproducirAudio("dinero.wav",false);
					sleep(1150);
					
					//Ciclo del decremento/incremento:
					do{
						sleep(30);
						nPago--;
						nCreditos++;
						pago.setText("" + nPago);
						creditos.setText("" + nCreditos);
						reproducirAudio("suma.wav",false);
					}while(nPago > 0);
					
				}catch(InterruptedException E){
					E.printStackTrace();
				}
			}
		};
		hilo.start();
		
		//Validación de la finalización del hilo:
		while(hilo.getState() != Thread.State.TERMINATED){
		}
		
	}
	
	//Deshabilita o habilita los botones de apuestas:
	public void cambiarApuestas(boolean tipo){
		for(int x=0; x<COLUMNAS; x++){
			lineas[x].setEnabled(tipo);
		}
	}
	
	//Cambia las imágenes de las columas para simular un giro:
	public void efectoGiro(String sTablero){
		
		String matriz[][] = transformarMatriz(sTablero);
		
		//Extracción de las columnas del tablero:
		String[] C0 = {matriz[0][0],matriz[1][0],matriz[2][0]}; 
		String[] C1 = {matriz[0][1],matriz[1][1],matriz[2][1]};
		String[] C2 = {matriz[0][2],matriz[1][2],matriz[2][2]};
		String[] C3 = {matriz[0][3],matriz[1][3],matriz[2][3]};
		String[] C4 = {matriz[0][4],matriz[1][4],matriz[2][4]};
		
		//Sincronización del movimiento del tablero:
		Thread hilo = new Thread(){
			public synchronized void run(){
				try{
					
					//Demostración de cada columna girando:
					
					cintas[0].setOpaque(true);
					sleep(800);
					
					cintas[1].setOpaque(true);
					sleep(800);
					
					cintas[2].setOpaque(true);
					sleep(800);

					cintas[3].setOpaque(true);
					sleep(800);
					
					cintas[4].setOpaque(true);			
					sleep(800);
					
					//Ubicación de las columnas finales:
					
					cintas[0].setOpaque(false);
					pintarColumna(0,C0);
					sleep(800);
					
					cintas[1].setOpaque(false);
					pintarColumna(1,C1);
					sleep(800);
					
					cintas[2].setOpaque(false);
					pintarColumna(2,C2);
					sleep(800);

					cintas[3].setOpaque(false);
					pintarColumna(3,C3);
					sleep(800);
					
					cintas[4].setOpaque(false);	
					pintarColumna(4,C4);
					
					//Validación del estado del audio:
					if(!audioGiro.isRunning()){
						reproducirAudio("giro.wav",true);
					}
					
				}catch(InterruptedException E){
					E.printStackTrace();
				}
			}
		};		
		
		hilo.start();
		
		//Reproducción del audio de giro:
		audioGiro.start();
		
		//Validación de la finalización del hilo mayor:
		while(hilo.getState() != Thread.State.TERMINATED){
		}
		
		//Pausa del audio de giro:
		audioGiro.stop();
		
	}
	
	//Pinta un giro en el juego y su cambio de variables respectivo:
	public void actualizarJuego(String paquete){
		
		String[] datos = paquete.split(" ");
		
		//Deshabilitación de los botones:
		girar.setEnabled(false);
		cambiarApuestas(false);
		guardar.setEnabled(false);
		ayuda.setEnabled(false);
		
		efectoGiro(datos[0]);
		
		String[] matches = datos[2].split("-");
		
		Thread hilo = new Thread(){
			public synchronized void run(){
				try{
					if(!datos[1].equals("0")){
						sleep(800);
						for(int x=0; x<matches.length; x++){
							pintarMatch(matches[x]);
							sleep(1000);
						}
					}					
				}catch(InterruptedException E){
					E.printStackTrace();
				}
			}
		};
		
		hilo.start();
		
		//Validación de la finalización del hilo:
		while(hilo.getState() != Thread.State.TERMINATED){
		}
		
		quitarMatch();
		
		//Conteo del pago y créditos:
		if(!datos[1].equals("0")){
			mostrarPago(datos[1]);
		}
		
		//Habilitación de los botones:
		girar.setEnabled(true);
		cambiarApuestas(true);
		guardar.setEnabled(true);
		ayuda.setEnabled(true);
		
	}
	
	//Retorna la cantidad de líneas a apostar:
	public String getApuesta(){
		return apuesta.getText();
	}
	
	//Retorna la posicion de la líneas a apostar:
	public int getPosicion(JButton boton){
		int posicion = 0, contador = 1; 
		for(int x=0; x<COLUMNAS; x++){
			if(boton == lineas[x]){
				posicion = x + contador;
			}
			contador++;
		}
		return posicion;
	}
	
	//Actualiza el crédito nuevo de un jugador:
	public void descontar(){
		String nuevoCredito = "" + (Integer.parseInt(creditos.getText()) - Integer.parseInt(apuesta.getText()));
		creditos.setText(nuevoCredito);
		if(nuevoCredito.equals("0")){
			JOptionPane.showMessageDialog(null, "¡Qué mala suerte, Te has quedado sin créditos!", "Mega-Tragamonedas", JOptionPane.DEFAULT_OPTION, imagenes[14]);
			reproducirAudio("menu.wav",false);
		}
	}
	
	//Pinta la línea nueva a apostar:
	public void pintarLinea(int linea){
		switch(linea){
			case 1:
				lApuestas.setIcon(imagenes[21]);
				break;
			case 3:
				lApuestas.setIcon(imagenes[22]);
				break;
			case 5:
				lApuestas.setIcon(imagenes[23]);
				break;
			case 7:
				lApuestas.setIcon(imagenes[24]);
				break;
			case 9:
				lApuestas.setIcon(imagenes[25]);
				break;
		}
	}
	
	//Retorna el nombre de usuario:
	public String getUsuario(){
		return nombre;
	}
	
	//Retorna la cantidad de créditos:
	public String getCreditos(){
		return creditos.getText();
	}
	
	//Muestra mensajes de ayuda al usuario:
	public void mostrarAyudas(int orden){
		reproducirAudio("menu.wav",false);
		int opcion;
		String[] decidir;
		switch(orden){
			case 1:
				decidir = new String[1];
				decidir[0] = "Siguiente";
				opcion = JOptionPane.showOptionDialog(null, "", "Ayudas", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION, imagenes[16], decidir, decidir[0]);
				if(opcion == 0){
					mostrarAyudas(2);
				}
				break;
			case 2:
				decidir = new String[2];
				decidir[1] = "Siguiente";
				decidir[0] = "Anterior";
				opcion = JOptionPane.showOptionDialog(null, "", "Ayudas", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION, imagenes[17], decidir, decidir[0]);
				if(opcion == 0){
					mostrarAyudas(1);
				}
				if(opcion == 1){
					mostrarAyudas(3);
				}
				break;
			case 3:
				decidir = new String[2];
				decidir[1] = "Siguiente";
				decidir[0] = "Anterior";
				opcion = JOptionPane.showOptionDialog(null, "", "Ayudas", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION, imagenes[18], decidir, decidir[0]);
				if(opcion == 0){
					mostrarAyudas(2);
				}
				if(opcion == 1){
					mostrarAyudas(4);
				}
				break;
			case 4:
				decidir = new String[1];
				decidir[0] = "Anterior";
				opcion = JOptionPane.showOptionDialog(null, "", "Ayudas", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION, imagenes[15], decidir, decidir[0]);
				if(opcion == 0){
					mostrarAyudas(3);
				}
				break;
		}
	}
	
	//Escucha para el botón de ayuda:
	private class EscuchaAyuda implements ActionListener{
		public void actionPerformed(ActionEvent E){
			if(E.getSource() == ayuda){
				mostrarAyudas(1);
			}
		}
	}
	
	//Escucha para los botones de apuestas:
	private class Escucha implements ActionListener{
		public void actionPerformed(ActionEvent evento){
			JButton B = (JButton)evento.getSource();
			if(Integer.parseInt(creditos.getText()) >= getPosicion(B)){
				quitarMatch();
				apuesta.setText("" + (getPosicion(B)));
				pintarLinea(getPosicion(B));
				girar.setEnabled(true);
			}else{
				JOptionPane.showMessageDialog(null, "No tienes suficientes créditos para esta apuesta.", "Mega-Tragamonedas", JOptionPane.DEFAULT_OPTION, imagenes[14]);
				reproducirAudio("menu.wav",false);
			}
			reproducirAudio("menu.wav",false);
		}
	}

}

/********************************************
 * Christian Camilo Taborda Campiño         *
 * Código: 1632081-3743                     *
 * Fecha de creación: 12/05/2017            *
 * Fecha de última modificación: 20/05/2017 *
 * ****************************************** 
 */

package clienteTragamonedas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

public class Cliente{
	
	//ATRIBUTOS:
	private TragamonedasGUI vista;
	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private String mensaje, servidor, nombreUsuario;
	private Socket clienteConexion;
	private JButton girar, guardar;
	private ImageIcon[] imagenes;
	
	//Reproduce una pista de audio:
	public void reproducirAudio(String pista){
		
		try{			
			//Importación y reproducción del sonido:
			URL url = getClass().getResource("/audio/" + pista);
			AudioInputStream entrada = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(entrada);
			clip.start();
			
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
	
	//Carga las imágenes del cliente:
	public void initImagenes(){
		imagenes = new ImageIcon[6];
		imagenes[0] = new ImageIcon(getClass().getResource("/imagenes/titulo.png"));
		imagenes[1] = new ImageIcon(getClass().getResource("/imagenes/boton.png"));	
		imagenes[2] = new ImageIcon(getClass().getResource("/imagenes/botonP.png"));	
		imagenes[3] = new ImageIcon(getClass().getResource("/imagenes/botonR.png"));
		imagenes[4] = new ImageIcon(getClass().getResource("/imagenes/pokeball.png"));
		imagenes[5] = new ImageIcon(getClass().getResource("/imagenes/error.png"));
	}
	
	//Constructor:
	public Cliente(String host){
		
		servidor = host;
		editarJOptionPane();
		
		//Inicialización de las imágenes:
		initImagenes();
		
		//Inicialización del botón de giro:
		girar = new JButton();
		girar.setSize(new Dimension(70,70));
		girar.setIcon(imagenes[1]);
		girar.setBorderPainted(false);
		girar.setContentAreaFilled(false);
		girar.setFocusable(false);
		girar.setRolloverEnabled(true);
		girar.setRolloverIcon(imagenes[3]);
		girar.setPressedIcon(imagenes[2]);
		girar.setBackground(Color.YELLOW);
		girar.setOpaque(true);
		
		//Inicialización del botón de guardado:
		guardar = new JButton(" Guardar & Salir ");
		guardar.setBorder(BorderFactory.createRaisedBevelBorder());
		guardar.setFont(new Font("Arial Black", Font.BOLD, 20));
		guardar.setForeground(Color.YELLOW);
		guardar.setBackground(new Color(45,49,146));
		guardar.setOpaque(true);
		
		//Asignación de las escuchas de los botones:
		Escucha escucha = new Escucha();
		girar.addActionListener(escucha);
		guardar.addActionListener(escucha);
		guardar.setFocusable(false);
		
	}
	
	//Conecta con el servidor:
	public void conectar() throws IOException{
		System.out.println("Intentando realizar conexión.");
		clienteConexion = new Socket(InetAddress.getByName(servidor), 12345);
		System.out.println("Conectado a: " + clienteConexion.getInetAddress().getHostName() + ".");
	}
	
	//Obtiene los flujos de entrada y salida:
	public void obtenerFlujos() throws IOException{
		salida = new ObjectOutputStream(clienteConexion.getOutputStream());
		salida.flush();
		entrada = new ObjectInputStream(clienteConexion.getInputStream());
		System.out.println("Se obtuvieron los flujos.");
	}
	
	//Actualiza el juego con una nueva jugada:
	public void actualizarJuego(String paquete){
		vista.actualizarJuego(paquete);
	}
	
	//Inicia la GUI del juego:	
	public void cargarJuego(String paquete){
		vista = new TragamonedasGUI(paquete + " " + nombreUsuario, girar, guardar);
	}
	
	//Procesa el mensaje recibido por el servidor:
	public void usarMensaje(String paquete){
		if(paquete.equals("El nombre de usuario ya existe.") || paquete.equals("Clave incorrecta.") || paquete.equals("El nombre de usuario no existe.")){
			JOptionPane.showMessageDialog(null, paquete, "Login", JOptionPane.DEFAULT_OPTION, imagenes[5]);
			reproducirAudio("menu.wav");
			login();
		}else{
			String[] datos = paquete.split(" ");
			if(datos.length == 3){
				actualizarJuego(paquete);
			}else{
				cargarJuego(paquete);
			}
		}
	}
	
	//Pide las claves del login al usuario:
	public String pedirClave(String mensaje){
		char[] password = null;
		String salida;
		JPasswordField passwordField = new JPasswordField();
		Object[] componentes = {mensaje, passwordField};
		Object botones[] = {"Aceptar","Cancelar"};
		if(JOptionPane.showOptionDialog(null, componentes, "Login",	JOptionPane.OK_CANCEL_OPTION, JOptionPane.DEFAULT_OPTION, imagenes[4], null, botones) == JOptionPane.YES_OPTION){
			reproducirAudio("menu.wav");
			password = passwordField.getPassword();
		}		
		if(password == null || password.length == 0){
			salida = "";
		}else{
			salida = new String(password);			
		}		
		return salida;
	}
	
	//Inicia sesión o registra a un nuevo jugador desde el cliente:
	public void login(){
		String usuario = null, clave = null, confirmacion = null;
		boolean correcta = false;
		String[] botones = {"Iniciar Sesión", "Registrarse"};
		int opcion;
		
		//Ciclo de validación de los datos:
		do{
			opcion = JOptionPane.showOptionDialog(null, "", "Login", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION, imagenes[0], botones, botones[0]);
			reproducirAudio("menu.wav");
			switch(opcion){
				case 0:
					//Inicio de sesión:
					usuario = (String)JOptionPane.showInputDialog(null, "Ingrese su nombre de usuario:", "Login", JOptionPane.DEFAULT_OPTION, imagenes[4], null, "");
					reproducirAudio("menu.wav");
					if(!(usuario == null || usuario.equals(""))){
						clave = pedirClave("Ingrese su clave:");
					}	
					confirmacion = " ";
					correcta = true;
					break;
				case 1:
					//Registro de jugador:
					usuario = (String)JOptionPane.showInputDialog(null, "Ingrese un nombre de usuario:", "Login", JOptionPane.DEFAULT_OPTION, imagenes[4], null, "");
					reproducirAudio("menu.wav");
					if(!(usuario == null || usuario.equals(""))){
						do{
							clave = pedirClave("Ingrese una clave:");
							if(!(clave == null || clave.equals(""))){
								confirmacion = pedirClave("Ingrese nuevamente la clave:");
							}
							if(!(clave == null || clave.equals("")) && !(confirmacion == null || confirmacion.equals(""))){
								if(clave.equals(confirmacion)){
									correcta = true;
								}else{
									String[] decidir = {"Aceptar"};
									JOptionPane.showOptionDialog(null, "Las claves no coinciden.", "Login", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION, imagenes[4], decidir, decidir[0]);
									reproducirAudio("menu.wav");
									correcta = false;
								}
							}else{
								correcta = true;
							}
						}while(!correcta);		
					}
					break;
				default:
					enviarDatos("FINALIZAR");
					System.exit(-1);
					break;
			}
		}while(usuario == null || usuario.equals("") || clave == null || clave.equals("") || confirmacion == null || confirmacion.equals("") || !correcta);	
		nombreUsuario = usuario;
		
		//Envío de información al servidor:
		enviarDatos(usuario + " " + clave + " " + String.valueOf(opcion + 1));
		
	}
	
	//Procesa la conexión con el servidor:
	public void procesarConexion() throws IOException{
		System.out.println("Conexión exitosa.");
		login();
		do{
			try{
				mensaje = (String)entrada.readObject();
				usarMensaje(mensaje);
			}catch(ClassNotFoundException E){
				System.out.println("Se recibió un objeto desconocido.");
			}
		}while(!mensaje.equals("FINALIZAR"));
	}
	
	//Cierra la conexión Cliente-Servidor:
	public void cerrarConexion(){
		System.out.println("Cerrando conexión.");
		try{
			salida.close();
			entrada.close();
			clienteConexion.close();
		}catch(IOException E){
			E.printStackTrace();
		}
	}
	
	//Envía un mensaje al servidor:
	public void enviarDatos(String mensaje){
		try{
			salida.writeObject(mensaje);
			salida.flush();
		}catch(IOException E){
			System.out.println("Error al escribir objeto.");
		}
	}
	
	//Inicializa el cliente y prepara la conexión con el servidor:
	public void ejecutarCliente(){
		try{
			conectar();
			obtenerFlujos();
			procesarConexion();
		}catch(EOFException E){
			System.out.println("Cliente terminó la conexión.");
		}catch(IOException E){
			E.printStackTrace();
		}finally{
			cerrarConexion();
		}
	}
	
	//Escucha del botón de giro y guardado:
	private class Escucha implements ActionListener{
		public void actionPerformed(ActionEvent evento){
			if(evento.getSource() == girar){
				if(Integer.parseInt(vista.getCreditos()) >= Integer.parseInt(vista.getApuesta())){
					vista.descontar();
					vista.quitarMatch();
					enviarDatos(vista.getApuesta());				
				}else{
					JOptionPane.showMessageDialog(null, "No tienes suficientes créditos para esta apuesta.", "Mega-Tragamonedas", JOptionPane.DEFAULT_OPTION, imagenes[5]);
					reproducirAudio("menu.wav");
				}
			}
			if(evento.getSource() == guardar){
				String paquete = vista.getCreditos() + " " + vista.getUsuario();
				enviarDatos(paquete);
				enviarDatos("FINALIZAR");
				System.exit(-1);
			}
		}
	}
}



/********************************************
 * Christian Camilo Taborda Campiño         *
 * Código: 1632081-3743                     *
 * Fecha de creación: 12/05/2017            *
 * Fecha de última modificación: 20/05/2017 *
 * ****************************************** 
 */

package servidorTragamonedas;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Servidor extends JFrame{
	
	//ATRIBUTOS:
	private Controlador juego;
	private Administrador admin;
	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private Socket conexion;
	private ServerSocket servidorConexion;
	private JTextArea pantalla;
	private Container contenedor;
	
	//Inicializa el componente de la ventana:
	public void initGUI(){
		pantalla = new JTextArea();
		pantalla.setFont(new Font("Arial Black",Font.BOLD,22));
		pantalla.setForeground(new Color(45,49,146));
		pantalla.setBackground(Color.YELLOW);
		pantalla.setOpaque(true);
		contenedor.add(new JScrollPane(pantalla));
	}
	
	//Constructor:
	public Servidor(){
		contenedor = this.getContentPane();
		contenedor.setBackground(Color.YELLOW);
		initGUI();
		juego = new Controlador();
		admin = new Administrador();
		setTitle("MEGA-TRAGAMONEDAS");
		ImageIcon icono = new ImageIcon(getClass().getResource("/icono/icono.png"));
		setIconImage(icono.getImage());
		setLocationRelativeTo(null);
		setSize(500,200);
		setResizable(false);
		setVisible(true);
	}
	
	//Muestra mensajes del servidor en la ventana:
	public void mostrarMensaje(String envio){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				pantalla.append(envio);
			}
		});
	}
	
	//Espera que se conecte el cliente:
	public void esperarConexion() throws IOException{
		mostrarMensaje("Esperando al cliente.\n");
		conexion = servidorConexion.accept();
		mostrarMensaje("Conexión con cliente: " + conexion.getInetAddress().getHostName() + ".\n");
	}
	
	//Obtiene los flujos de entrada y salida:
	public void obtenerFlujos() throws IOException{
		salida = new ObjectOutputStream(conexion.getOutputStream());
		salida.flush();
		entrada = new ObjectInputStream(conexion.getInputStream());
		mostrarMensaje("Se obtuvieron los flujos.\n");
	}
	
	//Envia un mensaje al cliente:
	public void enviarDatos(String mensaje){
		try{
			salida.writeObject(mensaje);
			salida.flush();
		}catch(IOException E){
			mostrarMensaje("Error al escribir objeto.\n");
		}
	}
	
	//Registra o inicia sesión en la base de datos y notifica al cliente:
	public void login(String mensaje){
		String paquete = juego.getTablero() + " ";
		String[] datos = mensaje.split(" ");
		if(datos[2].equals("2")){
			if(admin.registrar(datos[0], datos[1]).equals("Registro Exitoso.")){
				paquete += admin.iniciarSesion(datos[0], datos[1]);
				enviarDatos(paquete);
			}else{
				enviarDatos(admin.registrar(datos[0], datos[1]));
			}
		}else{
			if(admin.iniciarSesion(datos[0], datos[1]).equals("Clave incorrecta.") || admin.iniciarSesion(datos[0], datos[1]).equals("El nombre de usuario no existe.")){
				enviarDatos(admin.iniciarSesion(datos[0], datos[1]));
			}else{
				paquete += admin.iniciarSesion(datos[0], datos[1]);
				enviarDatos(paquete);
			}
		}
	}
	
	//Genera un tablero aleatorio, calcula las ganancias y envía la información al cliente:
	public void jugar(String mensaje){
		juego.generarTablero();
		String paquete = juego.getTablero() + " ";
		paquete += juego.obtenerGanancias((int)mensaje.charAt(0) - 48);
		enviarDatos(paquete);
	}
	
	//Actualiza los créditos del cliente en la base de datos:
	public void guardar(String mensaje){
		String[] datos = mensaje.split(" ");
		admin.actualizarCreditos(Integer.parseInt(datos[0]), datos[1]);
	}
	
	//Procesa los mensajes enviados por el cliente:
	public void procesarConexion() throws IOException{
		mostrarMensaje("Conexión exitosa.\n");
		String mensaje = null;
		do{
			try{
				mensaje = (String)entrada.readObject();
				String[] datos = mensaje.split(" ");
				switch(datos.length){
					case 1:
						jugar(mensaje);
						break;
					case 2:
						guardar(mensaje);
						break;
					case 3:
						login(mensaje);
						break;
				}
			}catch(ClassNotFoundException E){
				mostrarMensaje("Se recibió un objeto desconocido.\n");
			}
		}while(!mensaje.equals("FINALIZAR"));
	}
	
	//Cierra la conexión Servidor-Cliente:
	public void cerrarConexion(){
		mostrarMensaje("Terminando conexión.\n");
		try{
			salida.close();
			entrada.close();
			conexion.close();
		}catch(IOException E){
			E.printStackTrace();
		}
	}
	
	//Inicia el servidor y procesa la conexión:
	public void ejecutarServidor(){
		try{
			servidorConexion = new ServerSocket(12345,1);
			while(true){
				esperarConexion();
				obtenerFlujos();
				procesarConexion();
			}
		}catch(EOFException E){
			mostrarMensaje("Servidor terminó la conexión.\n");
		}catch(IOException E){
			E.printStackTrace();
		}finally{
			cerrarConexion();
		}
	}
	
}

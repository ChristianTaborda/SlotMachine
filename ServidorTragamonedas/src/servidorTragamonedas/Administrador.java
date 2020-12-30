/********************************************
 * Christian Camilo Taborda Campiño         *
 * Código: 1632081-3743                     *
 * Fecha de creación: 12/05/2017            *
 * Fecha de última modificación: 20/05/2017 *
 * ****************************************** 
 */

package servidorTragamonedas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Administrador{
	
	//ATRIBUTOS:
	private static final String DIRECCION = "src\\baseDeDatos\\usuarios.s3db";
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String URL = "jdbc:sqlite:" + DIRECCION;
	private static Connection conexion = null;
	private static PreparedStatement psSql = null;
	private static ResultSet resultado = null;
	
	//Indica si un nombre de usuario ya existe:
	public boolean usuarioExistente(String usuario){
		boolean salida = false;
		try{
			
			//Conexión a la base de datos y preparación del comando SQL:
			Class.forName(DRIVER);
			conexion = DriverManager.getConnection(URL);
			String sql = "SELECT * FROM jugadores WHERE nombreUsuario = ?";
			psSql = conexion.prepareStatement(sql);
			psSql.setString(1, usuario);
			
			//Ejecución de la búsqueda en la base de datos:
			resultado = psSql.executeQuery();
			
			//Validación del resultado de la búsqueda:
			while(resultado.next()){
				if(resultado.getString("nombreUsuario").equals(usuario)){
					salida = true;
				}
			}
			
		}catch(Exception E){
			E.printStackTrace();
		}finally{
			try{
				
				//Cierre de la conexión:
				resultado.close();
				psSql.close();
				conexion.close();
				
			}catch(SQLException E){
				E.printStackTrace();
			}
		}
		return salida;
	}
	
	//Crea un nuevo registro en la base de datos:
	public String registrar(String usuario, String clave){
		String salida = null;
		
		//Validación de la existencia del usuario:
		if(!usuarioExistente(usuario)){
			try{
				
				//Conexión a la base de datos y preparación del comando SQL:
				Class.forName(DRIVER);
				conexion = DriverManager.getConnection(URL);
				String sql = "INSERT INTO jugadores (nombreUsuario,clave,creditos) VALUES (?,?,?)";
				psSql = conexion.prepareStatement(sql);
				psSql.setString(1, usuario);
				psSql.setString(2, clave);
				psSql.setInt(3, 270);
				
				//Ingreso del usuario:
				psSql.executeUpdate();				
				salida = "Registro Exitoso.";
				
			}catch(Exception E){
				E.printStackTrace();
			}finally{
				try{
					
					//Cierre de la conexión:
					psSql.close();
					conexion.close();
					
				}catch(SQLException E){
					E.printStackTrace();
				}
			}
		}else{
			salida = "El nombre de usuario ya existe.";
		}
		return salida;
	}
	
	//Valida los datos de un usuario para retornar sus créditos:
	public String iniciarSesion(String usuario, String clave){
		String salida = null;
		
		//Validación de la existencia del usuario:
		if(usuarioExistente(usuario)){
			try{
				
				//Conexión con la base de datos y preparación del comando SQL:
				Class.forName(DRIVER);
				conexion = DriverManager.getConnection(URL);
				String sql = "SELECT * FROM jugadores WHERE nombreUsuario = ?";
				psSql = conexion.prepareStatement(sql);
				psSql.setString(1, usuario);
				
				//Ejecución de la búsqueda en la base de datos:
				resultado = psSql.executeQuery();
				
				//Procesamiento de los resultados de la búsqueda:
				while(resultado.next()){
					if(resultado.getString("clave").equals(clave)){
						salida = String.valueOf(resultado.getInt("creditos"));
					}else{
						salida = "Clave incorrecta.";
					}
				}		
				
			}catch(Exception E){
				E.printStackTrace();
			}finally{
				try{
					
					//Cierre de la conexión:
					resultado.close();
					psSql.close();
					conexion.close();
					
				}catch(SQLException E){
					E.printStackTrace();
				}
			}
		}else{
			salida = "El nombre de usuario no existe.";
		}	
		return salida;
	}	
	
	//Actualiza los créditos de un usuario en la base de datos:
	public void actualizarCreditos(int creditos, String usuario){
		try{
			
			//Conexión con la base de datos y preparación del comando SQL:
			Class.forName(DRIVER);
			conexion = DriverManager.getConnection(URL);
			String sql = "UPDATE jugadores SET creditos = ? WHERE nombreUsuario = ?";
			psSql = conexion.prepareStatement(sql);
			psSql.setInt(1, creditos);
			psSql.setString(2, usuario);
			
			//Ejecución de la actualización:
			psSql.executeUpdate();		
			
		}catch(Exception E){
			E.printStackTrace();
		}finally{
			try{
				
				//Cierre de conexión:
				psSql.close();
				conexion.close();
				
			}catch(SQLException E){
				E.printStackTrace();
			}
		}
	}
	
}

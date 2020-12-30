/********************************************
 * Christian Camilo Taborda Campiño         *
 * Código: 1632081-3743                     *
 * Fecha de creación: 12/05/2017            *
 * Fecha de última modificación: 20/05/2017 *
 * ****************************************** 
 */

package servidorTragamonedas;

import javax.swing.JFrame;

public class PrincipalServidor{
	
	public static void main(String[] args){
		
		//Inicialización del servidor:
		Servidor juego = new Servidor();
		juego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		juego.ejecutarServidor();
		
	}
}
	



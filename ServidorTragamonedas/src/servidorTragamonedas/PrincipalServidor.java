/********************************************
 * Christian Camilo Taborda Campi�o         *
 * C�digo: 1632081-3743                     *
 * Fecha de creaci�n: 12/05/2017            *
 * Fecha de �ltima modificaci�n: 20/05/2017 *
 * ****************************************** 
 */

package servidorTragamonedas;

import javax.swing.JFrame;

public class PrincipalServidor{
	
	public static void main(String[] args){
		
		//Inicializaci�n del servidor:
		Servidor juego = new Servidor();
		juego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		juego.ejecutarServidor();
		
	}
}
	



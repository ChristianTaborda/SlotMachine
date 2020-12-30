/********************************************
 * Christian Camilo Taborda Campi�o         *
 * C�digo: 1632081-3743                     *
 * Fecha de creaci�n: 12/05/2017            *
 * Fecha de �ltima modificaci�n: 20/05/2017 *
 * ****************************************** 
 */

package clienteTragamonedas;

public class PrincipalCliente{

	public static void main(String[] args){
		
		//Inicializaci�n y ejecuci�n del cliente:
		Cliente jugador;
		if(args.length == 0){
			jugador = new Cliente("localHost");
		}else{
			jugador = new Cliente(args[0]);
		}
		jugador.ejecutarCliente();
		
	}
}

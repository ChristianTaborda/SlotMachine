/********************************************
 * Christian Camilo Taborda Campiño         *
 * Código: 1632081-3743                     *
 * Fecha de creación: 12/05/2017            *
 * Fecha de última modificación: 20/05/2017 *
 * ****************************************** 
 */

package clienteTragamonedas;

public class PrincipalCliente{

	public static void main(String[] args){
		
		//Inicialización y ejecución del cliente:
		Cliente jugador;
		if(args.length == 0){
			jugador = new Cliente("localHost");
		}else{
			jugador = new Cliente(args[0]);
		}
		jugador.ejecutarCliente();
		
	}
}

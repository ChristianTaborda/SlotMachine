/********************************************
 * Christian Camilo Taborda Campiño         *
 * Código: 1632081-3743                     *
 * Fecha de creación: 12/05/2017            *
 * Fecha de última modificación: 20/05/2017 *
 * ****************************************** 
 */

package servidorTragamonedas;

import java.util.Random;

public class Controlador{
	
	//ATRIBUTOS:
	private static final int FILAS = 3, COLUMNAS = 5;
	private int[][] tablero;
	private int[][] pagos;
	
	//Inicializa la matriz de pagos:
	public void initPagos(){
		
		pagos = new int[12][6];
		
		pagos[0][2] = 5;
		pagos[0][3] = 50;
		pagos[0][4] = 250;
		pagos[0][5] = 2500;
		pagos[1][2] = 2;
		pagos[1][3] = 25;
		pagos[1][4] = 100;
		pagos[1][5] = 500;
		pagos[2][2] = 2;
		pagos[2][3] = 20;
		pagos[2][4] = 80;
		pagos[2][5] = 400;
		pagos[3][2] = 2;
		pagos[3][3] = 5;
		pagos[3][4] = 25;
		pagos[3][5] = 100;
		pagos[4][3] = 10;
		pagos[4][4] = 75;
		pagos[4][5] = 350;
		pagos[5][3] = 10;
		pagos[5][4] = 50;
		pagos[5][5] = 250;
		pagos[6][3] = 10;
		pagos[6][4] = 50;
		pagos[6][5] = 200;
		pagos[7][3] = 5;
		pagos[7][4] = 50;
		pagos[7][5] = 125;
		pagos[8][3] = 5;
		pagos[8][4] = 25;
		pagos[8][5] = 125;
		pagos[9][3] = 5;
		pagos[9][4] = 25;
		pagos[9][5] = 100;
		pagos[10][3] = 5;
		pagos[10][4] = 25;
		pagos[10][5] = 100;
		pagos[11][3] = 10;
		pagos[11][4] = 25;
		pagos[11][5] = 75;
		
	}
	
	//Genera un tablero aleatorio:
	public void generarTablero(){
		Random aleatorio = new Random();
		int comodines = 0;
		for(int x=0; x<FILAS; x++){
			for(int y=0; y<COLUMNAS; y++){
				do{
					tablero[x][y] = aleatorio.nextInt(12);
					if(tablero[x][y] == 11){
						comodines++;
					}
					//Control de comodines:
				}while(!(comodines <= 3));					
			}
		}
	}
	
	//Constructor:
	public Controlador(){
		tablero = new int[FILAS][COLUMNAS];
		generarTablero();
		initPagos();
	}
	
	//Construye y retorna una cadena con la información del tablero:
	public String getTablero(){
		String salida = "";
		for(int x=0; x<FILAS; x++){
			for(int y=0; y<COLUMNAS; y++){
				switch(tablero[x][y]){
					case 10:
						salida += "A";
						break;
					case 11:
						salida += "B";
						break;
					default:
						salida += tablero[x][y];
						break;
				}
			}
		}
		return salida;
	}
	
	//Retorna el número que genera el match de una jugada:
	public int buscarMatch(int A, int B, int C, int D, int E){
		int match;
		if(A == 11){
			if(B == 11){
				if(C == 11){
					match = D;
				}else{
					match = C;
				}
			}else{
				match = B;
			}
		}else{
			match = A;
		}
		return match;
	}
	
	//Indica si una jugada es de combinación o simple:
	public boolean tipoMatch(int match, int[] fichas){
		boolean salida = false;
		if(match == 2 || match == 3){
			for(int x=0; x<fichas.length; x++){
				if(match != fichas[x] && fichas[x] != 11){
					salida = true;
				}
			}
		}
		return salida;
	}
	
	//Extrae los números participantes en una jugada:
	public int[] getMatch(int match, int[] fichas, int cantidad){
		int contador = 0;
		int[] salida = new int[cantidad];
		for(int x=0; x<COLUMNAS; x++){
			if(match == 3 || match == 2){
				if((fichas[x] == 3 || fichas[x] == 11 || fichas[x] == 2) && contador == x){
					salida[x] = fichas[x];
					contador++;
				}
			}else{
				if((fichas[x] == match || fichas[x] == 11) && contador == x){
					salida[x] = fichas[x];
					contador++;
				}
			}			
		}
		return salida;
	}
	
	//Retorna el tamaño de una jugada:
	public int contarMatch(int match, int[] fichas){
		int salida = 0;
		for(int x=0; x<COLUMNAS; x++){
			if(match == 3 || match == 2){
				if((fichas[x] == 3 || fichas[x] == 11 || fichas[x] == 2) && salida == x){
					salida++;
				}
			}else{
				if((fichas[x] == match || fichas[x] == 11) && salida == x){
					salida++;
				}
			}			
		}
		return salida;
	}
	
	//Retorna el pago acordado por tipo y cantidad:
	public int recibirPago(int match, int cantidad){		
		return pagos[match][cantidad];
	}
	
	//Retorna las ganancias de una jugada y su tamaño:
	public String revisarLinea(int A, int B, int C, int D, int E){
		int pago = 0;		
		int match = buscarMatch(A,B,C,D,E);
		int[] fichas = {A,B,C,D,E};
		int cantidad = contarMatch(match,fichas);
		boolean tipo = false;
		
		//Validación del tamaño necesario por jugada:
		if(match >= 0 && match < 4){
			if(cantidad >= 2){
				tipo = tipoMatch(match,getMatch(match,fichas,cantidad));	
				if(tipo){
					if(cantidad >= 3){
						pago += recibirPago(11,cantidad);
					}
				}else{
					pago += recibirPago(match,cantidad);
				}
			}
		}else{
			if(cantidad >= 3){
				pago += recibirPago(match,cantidad);
			}
		}
		String salida;
		
		//Validación de las ganancias:
		if(pago != 0){
			salida = pago + "-" + cantidad;
		}else{
			salida = String.valueOf(pago);
		}
		return salida;
		
	}
	
	//Retorna las ganancias obtenidas por una línea y los datos de la jugada:
	public String obtenerGanancias(int lineas){
		int ganancias = 0;
		String salida = "";
		if(lineas >= 1){
			String[] paquete = revisarLinea(tablero[1][0],tablero[1][1],tablero[1][2],tablero[1][3],tablero[1][4]).split("-");
			ganancias += Integer.parseInt(paquete[0]);
			if(paquete.length != 1){
				if(!salida.equals("")){
					salida += "-";
				}
				salida += 1 + "_" + paquete[1];			
			}			
		}
		if(lineas >= 2){
			String[] paquete = revisarLinea(tablero[0][0],tablero[0][1],tablero[0][2],tablero[0][3],tablero[0][4]).split("-");
			ganancias += Integer.parseInt(paquete[0]);
			if(paquete.length != 1){
				if(!salida.equals("")){
					salida += "-";
				}
				salida += 2 + "_" + paquete[1];
			}	
		}
		if(lineas >= 3){
			String[] paquete = revisarLinea(tablero[2][0],tablero[2][1],tablero[2][2],tablero[2][3],tablero[2][4]).split("-");
			ganancias += Integer.parseInt(paquete[0]);
			if(paquete.length != 1){
				if(!salida.equals("")){
					salida += "-";
				}
				salida += 3 + "_" + paquete[1];
			}
		}
		if(lineas >= 4){
			String[] paquete = revisarLinea(tablero[0][0],tablero[1][1],tablero[2][2],tablero[1][3],tablero[0][4]).split("-");
			ganancias += Integer.parseInt(paquete[0]);
			if(paquete.length != 1){
				if(!salida.equals("")){
					salida += "-";
				}
				salida += 4 + "_" + paquete[1];
			}
		}
		if(lineas >= 5){
			String[] paquete = revisarLinea(tablero[2][0],tablero[1][1],tablero[0][2],tablero[1][3],tablero[2][4]).split("-");
			ganancias += Integer.parseInt(paquete[0]);
			if(paquete.length != 1){
				if(!salida.equals("")){
					salida += "-";
				}
				salida += 5 + "_" + paquete[1];
			}
		}
		if(lineas >= 6){
			String[] paquete = revisarLinea(tablero[0][0],tablero[0][1],tablero[1][2],tablero[2][3],tablero[2][4]).split("-");
			ganancias += Integer.parseInt(paquete[0]);
			if(paquete.length != 1){
				if(!salida.equals("")){
					salida += "-";
				}
				salida += 6 + "_" + paquete[1];
			}
		}
		if(lineas >= 7){
			String[] paquete = revisarLinea(tablero[2][0],tablero[2][1],tablero[1][2],tablero[0][3],tablero[0][4]).split("-");
			ganancias += Integer.parseInt(paquete[0]);
			if(paquete.length != 1){
				if(!salida.equals("")){
					salida += "-";
				}
				salida += 7 + "_" + paquete[1];
			}
		}
		if(lineas >= 8){
			String[] paquete = revisarLinea(tablero[1][0],tablero[2][1],tablero[1][2],tablero[0][3],tablero[1][4]).split("-");
			ganancias += Integer.parseInt(paquete[0]);
			if(paquete.length != 1){
				if(!salida.equals("")){
					salida += "-";
				}
				salida += 8 + "_" + paquete[1];
			}
		}
		if(lineas >= 9){
			String[] paquete = revisarLinea(tablero[1][0],tablero[0][1],tablero[1][2],tablero[2][3],tablero[1][4]).split("-");
			ganancias += Integer.parseInt(paquete[0]);
			if(paquete.length != 1){
				if(!salida.equals("")){
					salida += "-";
				}
				salida += 9 + "_" + paquete[1];
			}
		}
		
		//Validación de la cantidad de ganancias:
		if(ganancias == 0){
			return ganancias + " Nel";
		}else{
			return ganancias + " " + salida;
		}
		
	}

}

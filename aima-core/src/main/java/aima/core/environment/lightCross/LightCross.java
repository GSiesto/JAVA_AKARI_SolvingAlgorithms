package aima.core.environment.lightCross;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.print.attribute.standard.PrinterLocation;

import aima.core.util.datastructure.Pair;
import aima.core.util.datastructure.XYLocation;

/**
@author Guillermo Siesto Sánchez /gsiestos/
*/
public class LightCross {
	
	/*
	 * Estructura tablero: matriz int[][]
	 * Casilla vacia: V						// Vacia
	 * Casilla vacia iluminada: I 			// Iluminada
	 * Casilla pared: B						// Bloque
	 * Casilla restriccion: 0, 1, 2, 3, 4	// Restriction
	 * Casilla bombilla: L 					// Light
	 */

	/* constantes útiles para la implementación del problema*/
	//public final static int FICHA_MIN = 1; //para indicar el valor de la ficha m�s peque�a en el puzzle-8 de 3x3
	//public final static int FICHA_MAX = 8; //para indicar el valor de la ficha m�s grande en el puzzle-8 de 3x3
	public final static int B = 5;//B de bloque para indicar que es un bloque donde no puede ir una fuente de luz ni pasar luz
	public final static int V = 6;//V de vac�a para indicar que esa casilla no tiene bombilla ni est� iluminada
	public final static int L = 7;//L para representar que esa casilla tiene una fuente de luz
	public final static int I = 8;//I de iluminada para indicar que esa casilla est� iluminada por una fuente de luz
	
	/* variable que va a representar el estado del problema */
	private int lightState [][];
	
	/* configuración inicial del problema para las pruebas internas */
	private static int[][] TABLERO_INI = {
		{ B,1,V,V,V,B},
		{ B,V,V,V,V,0},
		{ V,V,V,B,V,V},
		{ V,V,V,V,V,V},
		{ V,V,B,1,V,V}
		};
	
	/* Variables auxiliars para rapidez en heuristicaD */
	private List<Pair<Integer, Integer>> lista_coor_bloques_restricción = new ArrayList<Pair<Integer, Integer>>();
	private int cantidad_bombillas_restringidas;
	
	
	// ========================================
	// 		  C O N S T R U C T O R E S
	// ========================================
	/* Constructor por defecto */
	public LightCross(){
		lightState = new int[TABLERO_INI.length][TABLERO_INI[0].length];
		
		for( int fila=0 ; fila < TABLERO_INI.length ; fila++ ){
			for( int columna=0 ; columna < TABLERO_INI[0].length ; columna++ ){
				//Guardamos estado
				lightState[fila][columna] = TABLERO_INI[fila][columna];			
			}
		}
		
		// Volvemos a recorrer la matriz para obtener casillas restricciones que nos serán útiles
		// a lo largo de los distintos algoritmos
		//Guardamos una lista con las coordenadas de los bloques restricción
		for( int fila=0 ; fila < TABLERO_INI.length ; fila++ ){
			for( int columna=0 ; columna < TABLERO_INI[0].length ; columna++ ){
				Pair<Integer, Integer> coor = new Pair<Integer, Integer>(fila, columna);
				if (esBloqueRestriccionCasilla(coor)){
					
					// Se comprueba también si hay casillas a las que las restricciones le afectan adyacentemente
					// EJemplo:
					/*
					 * V V 2 V 3       A la fila 0 columna 3 le afectan las dos restricciones
					 */
					lista_coor_bloques_restricción.add(coor);
					
					this.cantidad_bombillas_restringidas = this.cantidad_bombillas_restringidas + lightState[fila][columna];
					
					Pair<Integer, Integer> auxiliar = new Pair<Integer, Integer>(coor.getFirst()+2, coor.getSecond());
					if (!esFueraLimiteCasilla(auxiliar) && esBloqueRestriccionCasilla(auxiliar) && lightState[auxiliar.getFirst()][auxiliar.getSecond()] != 0)
						this.cantidad_bombillas_restringidas--;
					auxiliar = new Pair<Integer, Integer>(coor.getFirst(), coor.getSecond()+2);
					if (!esFueraLimiteCasilla(auxiliar) && esBloqueRestriccionCasilla(auxiliar) && lightState[auxiliar.getFirst()][auxiliar.getSecond()] != 0)
						this.cantidad_bombillas_restringidas--;
					auxiliar = new Pair<Integer, Integer>(coor.getFirst()+1, coor.getSecond()+1);
					if (!esFueraLimiteCasilla(auxiliar) && esBloqueRestriccionCasilla(auxiliar) && lightState[auxiliar.getFirst()][auxiliar.getSecond()] != 0)
						this.cantidad_bombillas_restringidas--;
					
				}
			}
		}
		
		if (this.cantidad_bombillas_restringidas < 0)
			this.cantidad_bombillas_restringidas = 0;
		
	}
	
	/* Constructor parametrizado que recibe una variable que represente el estado inicial del problema */
	public LightCross( int[][] _estado ){
		lightState = new int[_estado.length][_estado[0].length];
		
		for( int fila=0 ; fila < _estado.length ; fila++ ){
			for( int columna=0 ; columna < _estado[0].length ; columna++ ){
				//Guardamos estado
				lightState[fila][columna] = _estado[fila][columna];		
			}
		}
		
		// Volvemos a recorrer la matriz para obtener casillas restricciones que nos serán útiles
		// a lo largo de los distintos algoritmos
		//Guardamos una lista con las coordenadas de los bloques restricción
		for( int fila=0 ; fila < lightState.length ; fila++ ){
			for( int columna=0 ; columna < lightState[0].length ; columna++ ){
				Pair<Integer, Integer> coor = new Pair<Integer, Integer>(fila, columna);
				if (esBloqueRestriccionCasilla(coor)){
					
					// Se comprueba también si hay casillas a las que las restricciones le afectan adyacentemente
					// EJemplo:
					/*
					 * V V 2 V 3       A la fila 0 columna 3 le afectan las dos restricciones
					 * 
					 * Solo miramos:
					 * 		coordenada + 2 derecha
					 * 		coordenada +2 abajo
					 * 		coordenada +1 +1 abajo derecha
					 * 
					 */
					lista_coor_bloques_restricción.add(coor);
					
					this.cantidad_bombillas_restringidas = this.cantidad_bombillas_restringidas + lightState[fila][columna];
					
					Pair<Integer, Integer> auxiliar = new Pair<Integer, Integer>(coor.getFirst()+2, coor.getSecond());
					if (!esFueraLimiteCasilla(auxiliar) && esBloqueRestriccionCasilla(auxiliar) && lightState[auxiliar.getFirst()][auxiliar.getSecond()] != 0)
						this.cantidad_bombillas_restringidas--;
					auxiliar = new Pair<Integer, Integer>(coor.getFirst(), coor.getSecond()+2);
					if (!esFueraLimiteCasilla(auxiliar) && esBloqueRestriccionCasilla(auxiliar) && lightState[auxiliar.getFirst()][auxiliar.getSecond()] != 0)
						this.cantidad_bombillas_restringidas--;
					auxiliar = new Pair<Integer, Integer>(coor.getFirst()+1, coor.getSecond()+1);
					if (!esFueraLimiteCasilla(auxiliar) && esBloqueRestriccionCasilla(auxiliar) && lightState[auxiliar.getFirst()][auxiliar.getSecond()] != 0)
						this.cantidad_bombillas_restringidas--;
					
				}
			}
		}
		
		if (this.cantidad_bombillas_restringidas < 0)
			this.cantidad_bombillas_restringidas = 0;
		
	}
	
	// ========================================
	// 		 			G E T
	// ========================================
	/* Método que devuelve el estado del problema en un momento dado */
	public int[][] getState(){ // No se puede poner directamente return lightstate  porque se pasariía por referencia
								// Siempre hay que quedarse con una copia
		int [][] retorno = new int[lightState.length][lightState[0].length];
		for (int fila = 0; fila < lightState.length; fila++) {
			for (int columna = 0 ; columna < lightState[0].length ; columna++) {
				retorno[fila][columna] = lightState[fila][columna];
			}
		}
		
		return retorno;
	}
	
	// ========================================
	// 		 			S E T
	// ========================================
	/* Método que fija un estado para el problema */
	public void setState( int[][] estadoLight){
		for (int fila = 0 ; fila < estadoLight.length ; fila++) {
			for (int columna = 0 ; columna < estadoLight[0].length ; columna++) {
				lightState[fila][columna] = estadoLight[fila][columna];
			}
		}
	}
	
	// ========================================
	// 			 es______Casilla();
	// ========================================
	
	/* Método que nos dice si el la casilla pasada como coordenadas es un Bloque de cualquier tipo */
	public boolean esBloqueCualquieraCasilla( Pair<Integer,Integer> coordenadas ){
		return (lightState[coordenadas.getFirst()][coordenadas.getSecond()] == B || esBloqueRestriccionCasilla(coordenadas));
	};
	
	/* Método que devuelve True si la casilla introducida como parámetro está fuera de los límites */
	public boolean esFueraLimiteCasilla( Pair<Integer,Integer> coordenadas ){
		return ( coordenadas.getFirst() < 0
				 || coordenadas.getSecond() < 0  
				 || coordenadas.getFirst() > (lightState.length - 1)
				 || coordenadas.getSecond() > (lightState[0].length - 1)) ; // Lenght devuelve el numero exacto de columnas o filas, y nosotros tratamos la matriz desde 0, no empezando desde uno
	}
	
	/* Método que nos dice si el la casilla pasada como coordenadas es un Bloque */
	public boolean esBloqueCasilla( Pair<Integer,Integer> coordenadas ){
		return (lightState[coordenadas.getFirst()][coordenadas.getSecond()] == B);
	};
	
	/* Método que nos dice si el la casilla pasada como coordenadas es un Bloque con reestricción */
	public boolean esBloqueRestriccionCasilla( Pair<Integer,Integer> coordenadas ){
		return (lightState[coordenadas.getFirst()][coordenadas.getSecond()] == 0
				|| lightState[coordenadas.getFirst()][coordenadas.getSecond()] == 1
				|| lightState[coordenadas.getFirst()][coordenadas.getSecond()] == 2
				|| lightState[coordenadas.getFirst()][coordenadas.getSecond()] == 3
				|| lightState[coordenadas.getFirst()][coordenadas.getSecond()] == 4);
	};
	
	/* Método que nos dice si el la casilla pasada como coordenadas es Vacía */
	public boolean esVacíaCasilla( Pair<Integer,Integer> coordenadas ){
		return (lightState[coordenadas.getFirst()][coordenadas.getSecond()] == V);
	};
	
	/* Método que nos dice si el la casilla pasada como coordenadas es una Bombilla */
	public boolean esBombillaCasilla( Pair<Integer,Integer> coordenadas ){
		return (lightState[coordenadas.getFirst()][coordenadas.getSecond()] == L);
	};
	
	/* Método que nos dice si el la casilla pasada como coordenadas está iluminada */
	public boolean esIluminadaCasilla( Pair<Integer,Integer> coordenadas ){
		return (lightState[coordenadas.getFirst()][coordenadas.getSecond()] == I);
	};
	
	// ========================================
	// 			    C O N T A R
	// ========================================
	/* Devuelve el número de bombillas adyacentes a una casilla */
	public int contarBombillasAdyacentes(Pair<Integer, Integer> coordenadas){
		int contador = 0;
		
		Pair<Integer, Integer> izq = new Pair<Integer, Integer>(coordenadas.getFirst()  , coordenadas.getSecond() - 1);
		Pair<Integer, Integer> der = new Pair<Integer, Integer>(coordenadas.getFirst() , coordenadas.getSecond() + 1);
		Pair<Integer, Integer> arriba = new Pair<Integer, Integer>(coordenadas.getFirst() - 1, coordenadas.getSecond() );
		Pair<Integer, Integer> abajo = new Pair<Integer, Integer>(coordenadas.getFirst() + 1, coordenadas.getSecond());
		
		if (!esFueraLimiteCasilla(coordenadas)){
			
			if ( !esFueraLimiteCasilla(izq) && esBombillaCasilla(izq))
				contador++;
			if ( !esFueraLimiteCasilla(der) && esBombillaCasilla(der))
				contador++;
			if ( !esFueraLimiteCasilla(arriba) && esBombillaCasilla(arriba))
				contador++;
			if ( !esFueraLimiteCasilla(abajo) && esBombillaCasilla(abajo))
				contador++;
		}else{
			contador = -1; // ïndicamos que estamos intentando ver coordenadas de fuera de límite
		}
		
		return contador;
	}
	
	public int cuantasBombillasQuedanARestricciones(){
		int contador = 0;
		
		for( int fila=0 ; fila < lightState.length ; fila++ ){
			for( int columna=0 ; columna < lightState[0].length ; columna++ ){
				
				/* Comprobamos las x:
				 * V X V
				 * x 3 X
				 * V X V
				 */
				if (!esFueraLimiteCasilla(new Pair<Integer, Integer>(fila, columna)) && esBloqueRestriccionCasilla(new Pair<Integer, Integer>(fila, columna))){
					
					Pair<Integer, Integer> izq = new Pair<Integer, Integer>(fila  , columna - 1);
					Pair<Integer, Integer> der = new Pair<Integer, Integer>(fila , columna + 1);
					Pair<Integer, Integer> arriba = new Pair<Integer, Integer>(fila - 1, columna );
					Pair<Integer, Integer> abajo = new Pair<Integer, Integer>(fila + 1, columna);
					
					int cuenta_bombillas = 0;
		
					if ( !esFueraLimiteCasilla(izq) && esBombillaCasilla(izq))
						cuenta_bombillas++;
					if ( !esFueraLimiteCasilla(der) && esBombillaCasilla(der))
						cuenta_bombillas++;
					if ( !esFueraLimiteCasilla(arriba) && esBombillaCasilla(arriba))
						cuenta_bombillas++;
					if ( !esFueraLimiteCasilla(abajo) && esBombillaCasilla(abajo))
						cuenta_bombillas++;
					
					contador = contador + (lightState[fila][columna] - cuenta_bombillas);
				}
			}
		}
		
		return contador;
	}
	
	public int cuantasRestriccionesTocanCasilla(Pair<Integer, Integer> coordenadas ){
		int contador = 0;
		
		Pair<Integer, Integer> izq = new Pair<Integer, Integer>(coordenadas.getFirst()  , coordenadas.getSecond() - 1);
		Pair<Integer, Integer> der = new Pair<Integer, Integer>(coordenadas.getFirst() , coordenadas.getSecond() + 1);
		Pair<Integer, Integer> arriba = new Pair<Integer, Integer>(coordenadas.getFirst() - 1, coordenadas.getSecond() );
		Pair<Integer, Integer> abajo = new Pair<Integer, Integer>(coordenadas.getFirst() + 1, coordenadas.getSecond());
		
		if (!esFueraLimiteCasilla(coordenadas)){
			
			if ( !esFueraLimiteCasilla(izq) && esBloqueRestriccionCasilla(izq))
				contador++;
			if ( !esFueraLimiteCasilla(der) && esBloqueRestriccionCasilla(der))
				contador++;
			if ( !esFueraLimiteCasilla(arriba) && esBloqueRestriccionCasilla(arriba))
				contador++;
			if ( !esFueraLimiteCasilla(abajo) && esBloqueRestriccionCasilla(abajo))
				contador++;
		}
		return contador;
		
	}
	
	public int contarBombillas(){
		int contador = 0;
		for( int fila=0 ; fila < lightState.length ; fila++ ){
			for( int columna=0 ; columna < lightState[0].length ; columna++ ){
				if (esBombillaCasilla(new Pair<Integer, Integer>(fila, columna)) )
						contador++;
			}
		}
		return contador;
	}
	
	public int contarCasillasVacias(){
		int contador = 0;
		for( int fila=0 ; fila < lightState.length ; fila++ ){
			for( int columna=0 ; columna < lightState[0].length ; columna++ ){
				if (esVacíaCasilla(new Pair<Integer, Integer>(fila, columna)) )
						contador++;
			}
		}
		return contador;
	}
	
	public int contarCasillasIluminadas(){
		int contador = 0;
		for( int fila=0 ; fila < lightState.length ; fila++ ){
			for( int columna=0 ; columna < lightState[0].length ; columna++ ){
				if (esIluminadaCasilla(new Pair<Integer, Integer>(fila, columna)) )
						contador++;
			}
		}
		return contador;
	}
	
	public int contarCasillasBloque(){
		int contador = 0;
		for( int fila=0 ; fila < lightState.length ; fila++ ){
			for( int columna=0 ; columna < lightState[0].length ; columna++ ){
				if (esBloqueCasilla(new Pair<Integer, Integer>(fila, columna)) )
						contador++;
			}
		}
		return contador;
	}
	
	public int contarCasillasBloqueCualquiera(){
		int contador = 0;
		for( int fila=0 ; fila < lightState.length ; fila++ ){
			for( int columna=0 ; columna < lightState[0].length ; columna++ ){
				if (esBloqueCualquieraCasilla(new Pair<Integer, Integer>(fila, columna)) )
						contador++;
			}
		}
		return contador;
	}
	
	// ========================================
	// 			 A L G O R I T M O S
	// ========================================
	public List<Pair<Integer, Integer>> getCasillasAdyacentes(Pair<Integer, Integer> coordenadas){
		List<Pair<Integer, Integer>> lista = new ArrayList<Pair<Integer, Integer>>();
		
		Pair<Integer, Integer> izq = new Pair<Integer, Integer>(coordenadas.getFirst()  , coordenadas.getSecond() - 1);
		Pair<Integer, Integer> der = new Pair<Integer, Integer>(coordenadas.getFirst() , coordenadas.getSecond() + 1);
		Pair<Integer, Integer> arriba = new Pair<Integer, Integer>(coordenadas.getFirst() - 1, coordenadas.getSecond() );
		Pair<Integer, Integer> abajo = new Pair<Integer, Integer>(coordenadas.getFirst() + 1, coordenadas.getSecond());
		
		if ( !esFueraLimiteCasilla(coordenadas) ){
			
			if ( !esFueraLimiteCasilla(izq))
				lista.add(izq);
			if ( !esFueraLimiteCasilla(der))
				lista.add(der);
			if ( !esFueraLimiteCasilla(arriba))
				lista.add(arriba);
			if ( !esFueraLimiteCasilla(abajo))
				lista.add(abajo);
		}
		
		return lista;
	}
	public List<XYLocation> getPosiblesFuentesLuz(){ // 90%
		List<XYLocation> lista = new ArrayList<XYLocation>();
		
		for (int fila = 0 ; fila < lightState.length ; fila++){
			for (int columna = 0 ; columna < lightState[0].length ; columna++){
				
				Pair<Integer, Integer> coordenada = new Pair<Integer, Integer>(fila, columna);
				
				if ( esVacíaCasilla(coordenada) ){
					List<Pair<Integer, Integer>> adyacentes = getCasillasAdyacentes(coordenada);
					
					List<Pair<Integer, Integer>> lista_restricciones = new ArrayList<Pair<Integer, Integer>>();
					for (int i = 0; i < adyacentes.size() ; i++){
						if (esBloqueRestriccionCasilla(new Pair<Integer, Integer>(adyacentes.get(i).getFirst(), adyacentes.get(i).getSecond())))
							lista_restricciones.add(new Pair<Integer, Integer>(adyacentes.get(i).getFirst(), adyacentes.get(i).getSecond()));
					}
					
					if (lista_restricciones.size() == 0){
						lista.add(new XYLocation(fila, columna));
					}else{
						int menor_valor = lightState[lista_restricciones.get(0).getFirst()][lista_restricciones.get(0).getSecond()];
						int menor_indice = 0;
						for (int i = 1 ; i < lista_restricciones.size() - 1 ; i++){
							if (lightState[lista_restricciones.get(i).getFirst()][lista_restricciones.get(i).getSecond()] < menor_valor ){
								menor_valor = lightState[lista_restricciones.get(i).getFirst()][lista_restricciones.get(i).getSecond()];
								menor_indice = i;
							}
						}
						
						if (menor_valor != 0 && menor_valor != 4){
							if (contarBombillasAdyacentes(new Pair<Integer, Integer>(lista_restricciones.get(menor_indice).getFirst(), lista_restricciones.get(menor_indice).getSecond()))  < menor_valor)
								lista.add(new XYLocation(fila, columna));
						}
					}
					
				}
			}
		}
		
		return lista;
	}
	
	public void incluirFuenteLuz(XYLocation coordenadas){
		int x = coordenadas.getXCoOrdinate();
		int y = coordenadas.getYCoOrdinate();
		
		lightState[x][y] = L;
		
		// Movimiento Vertical
		int i = x - 1;
		while (!esFueraLimiteCasilla(new Pair<Integer, Integer>(i, y)) && !esBloqueCualquieraCasilla(new Pair<Integer, Integer>(i, y)) ){
			lightState[i][y] = I;
			i--;
		}
		
		i = x + 1;
		while (!esFueraLimiteCasilla(new Pair<Integer, Integer>(i, y)) && !esBloqueCualquieraCasilla(new Pair<Integer, Integer>(i, y)) ){
			lightState[i][y] = I;
			i++;
		}
		
		// Moviemiento Horizontal
		int j = y - 1;
		while (!esFueraLimiteCasilla(new Pair<Integer, Integer>(x, j)) && !esBloqueCualquieraCasilla(new Pair<Integer, Integer>(x, j)) ){
			lightState[x][j] = I;
			j--;
		}
		
		j = y + 1;
		while (!esFueraLimiteCasilla(new Pair<Integer, Integer>(x, j)) && !esBloqueCualquieraCasilla(new Pair<Integer, Integer>(x, j)) ){
			lightState[x][j] = I;
			j++;
		}
	}
	
	public boolean checkFinish(){
		boolean certeza = true;
		for (int fila = 0 ; certeza && fila < lightState.length ; fila++){
			for (int columna = 0 ; certeza && columna < lightState[0].length ; columna++){
				if (lightState[fila][columna] == V)
					certeza = false;
				else{
					if (esBloqueRestriccionCasilla(new Pair<Integer, Integer>(fila, columna))){
						if (contarBombillasAdyacentes(new Pair<Integer, Integer>(fila, columna)) != lightState[fila][columna]){
							certeza = false;
						}
					}
				}
			}
		}
		return certeza;
	}
	
	// ========================================
	// 		 		  H A S H
	// ========================================
	/* Método que implementa el hashCode en función del estado del problema 
	 * Código cédido en los foros de la asignatura por Francisco Javier Rojo Martín
	 */	
	@Override
	public int hashCode() {
        int result = 17;
        for (int fila = 0; fila < lightState.length; fila++) {
            for (int columna = 0; columna < lightState[0].length; columna++) {
                    result += ((result * 37 * lightState[fila][columna])+fila+5)*(columna+7);
            }
        }
        return result;
       }   
	
	
	// ========================================
	// 		 		E Q U A L S
	// ========================================
	/* Método que implementa el método equals en función del estado del problema */
	@Override
	public boolean equals(Object o) {
		
		boolean iguales=true;
		int fila=0;
		int columna=0;	
		
		//parte común a cualquier equals
		if(this == o)
			return true;
		if((o == null) || (this.getClass() != o.getClass()))
			return false;
		
		//parte específica del equals
		LightCross aState = (LightCross) o;
		while(iguales && fila < lightState.length){
			while(iguales && columna<lightState[0].length){
				iguales = lightState[fila][columna] == aState.getState()[fila][columna];
				columna++;
			}
			columna=0;
			fila++;
		}
		
		return iguales;
	}
	
	// ========================================
	// 		 		 P R I N T 
	// ========================================
	/* Método que implementa el método toString en función del estado del problema */
	@Override
	public String toString() {
		for(int i = 0; i < lightState.length ; i++){
			for (int j = 0; j < lightState[0].length ; j++){
				
				switch (lightState[i][j]) {
				case V: // vacia
					System.out.print("[ ]");
					break;
				case I: // iluminada
					System.out.print("[░]");
					break;
				case B: // pared
					System.out.print("[█]");
					break;
				case L: // bombilla
					System.out.print("[§]");
					break;

				default: // restriccion
					System.out.print("[" + lightState[i][j] + "]");
					break;
				}
				
			}
			System.out.println();
		}
		return null;
	}
	
	public void printCurrentState(){
		this.toString();
	}
	
	// ========================================
	// 		 	H E U R Í S T I C A S 
	// ========================================
	// Tiene que devolver el número de pasos que te quedan de forma optimista 
	
	/*
	 * Devuelve el numero de casillas vacias
	 * La heurística más simple
	 * 99%
	 */
	public int evaluarEuristicaA(){
		int retVal = -1;
		
		boolean vacia = false;
		int filas = 0;
		int columnas = 0;
		
		for (int fila = 0; fila < lightState.length; fila++) {
            for (int columna = 0; !vacia && columna < lightState[0].length; columna++) {
            	if ( lightState[fila][columna] == V )
            		vacia = true;
            }
            if (vacia)
            	filas++;
            vacia = false;
         }
		
		vacia = false;
		for (int columna = 0; columna < lightState[0].length ; columna++) {
            for (int fila = 0; !vacia && fila < lightState.length; fila++) {
            	if ( lightState[fila][columna] == V )
            		vacia = true;
            }
            if (vacia)
            	columnas++;
            vacia = false;
		}
		
		int masGrande = filas;
		if (columnas > masGrande)
			masGrande = columnas;
		
		if (masGrande != 0){
			retVal = contarCasillasVacias() / masGrande;
			return retVal;
		}else{
			return 0;
		}

	}
	
	/*
	 * Contar el numero de bombillas que hay que tener por culpa de las restricciones (aproximadamente) indicado por lo bloques restricciones,
	 *  y le decrementamos las bombillas ya colocadas en el tablero
	 * 90%
	 */
	public int evaluarEuristicaB(){
		int retVal = -1;
		retVal = cantidad_bombillas_restringidas - contarBombillas();
		// Hay que hacer esta comprobación porque cuando hay dos casillas restrcciones tal que las dos restringen otra casilla,
		// no lo tendrá en cuenta, entonces puede salir negativo.
		if (retVal > 0)
			return retVal;
		else
			return 0;

	}
	
	/*
	 * Devuelve el bombillas aproximadas que vamos a tener y a diferencia de la herurística B
	 * le quita las bombillas colocadas adyacentes a los bloques restricción
	 * 100%
	 */
	public int evaluarEuristicaC(){
		int retVal = -1;
		
		retVal = cuantasBombillasQuedanARestricciones();
		return retVal;
	}
	
	/*
	 * Mirando una base teórica, en un tablero vacio de 2x2 para poder iluminarlo necesitaríamos 2 bombillas
	 * en caso de un 4x4 necesitamos 4
	 * en caso de un 8x8 necesitamos 8
	 * En caso de no ser una matriz cuadrada, sería el número más pequeño fijándonos en la fila o la columna
	 * Vamos a estimar a partir de ello
	 * 100%
	 */
	public int evaluarEuristicaD(){
		int retVal = -1;
		int masPeque = lightState.length + 1;
		if (lightState[0].length + 1 < masPeque)
			masPeque = lightState[0].length + 1;
		
		int numBombillas = contarBombillas();
		// Poner un < (Más optimo requiere más tiempo) o <= (Muy rápido, menos optimo)
		if (numBombillas <= masPeque){ // En caso de que haya más bombillas de las que nuestra "trampa" quisiera, se penaliza al no entrar y vales el numero inicial de masPeque
			masPeque = masPeque - numBombillas;
		}
		
		if (masPeque > 0)
			retVal = masPeque;
		else
			retVal = 0;
		
		return retVal;
	}

	/*
	 * Versión Refinada
	 * La fila o columna la tendremos en cuneta si no hay casilla vacias en ellas, de esta forma 
	 * el tablero se irá reduciendo
	 * Mirando una base teórica, en un tablero vacio de 2x2 para poder iluminarlo necesitaríamos 2 bombillas
	 * en caso de un 4x4 necesitamos 4
	 * en caso de un 8x8 necesitamos 8
	 * En caso de no ser una matriz cuadrada, sería el número más pequeño fijándonos en la fila o la columna
	 * Vamos a estimar a partir de ello
	 * 99%
	 */
	public int evaluarHeuristicaE(){
		int retVal = -1;
		
		boolean vacia = false;
		int filas = 0;
		int columnas = 0;
		
		for (int fila = 0; fila < lightState.length; fila++) {
            for (int columna = 0; !vacia && columna < lightState[0].length; columna++) {
            	if ( lightState[fila][columna] == V )
            		vacia = true;
            }
            if (vacia)
            	filas++;
            vacia = false;
         }
		
		vacia = false;
		for (int columna = 0; columna < lightState[0].length ; columna++) {
            for (int fila = 0; !vacia && fila < lightState.length; fila++) {
            	if ( lightState[fila][columna] == V )
            		vacia = true;
            }
            if (vacia)
            	columnas++;
            vacia = false;
		}
		
		
		int masPeque = filas;
		if (columnas < masPeque)
			masPeque = columnas;
		
		int numBombillas = contarBombillas();
		if (numBombillas <= masPeque){ // En caso de que haya más bombillas de las que nuestra "trampa" quisiera, se penaliza al no entrar y vales el numero inicial de masPeque
			masPeque = masPeque - numBombillas;
		}

		if (masPeque > 0)
			retVal = masPeque;
		else
			retVal = 0;
		
		return retVal;

	}
	
	// ========================================
	// 		 		  M A I N 
	// ========================================
//	public static void main(String[] args) {
//		LightCross juego = new LightCross();
//		
//		juego.toString();
//		
//		System.out.println("==============================================");
//		System.out.println("======== TAMAÑO");
//		System.out.println("==============================================");
//		System.out.println("- Filas: "+juego.getState().length);
//		System.out.println("- Columnas :"+juego.getState()[0].length);
//		System.out.println("- Máx coordenadas de Filas será : "+ (juego.getState().length - 1));
//		System.out.println("- Máx coordenadas de Columnas será : :"+ (juego.getState()[0].length - 1));
//		System.out.println();
//		
//		
//		
//		System.out.println("==============================================");
//		System.out.println("======== ES FUERA LÍMITE");
//		System.out.println("==============================================");
//		
//		System.out.print("- Es 0x0 fuera de límite: ");
//		System.out.println(juego.esFueraLimiteCasilla(new Pair<Integer, Integer>(0, 0)));
//		
//		System.out.print("- Es -1x0 fuera de límite: ");
//		System.out.println(juego.esFueraLimiteCasilla(new Pair<Integer, Integer>(-1, 0)));
//		
//		System.out.print("- Es 0x-2 fuera de límite: ");
//		System.out.println(juego.esFueraLimiteCasilla(new Pair<Integer, Integer>(0, -2)));
//		
//		System.out.print("- Es 5x5 fuera de límite: ");
//		System.out.println(juego.esFueraLimiteCasilla(new Pair<Integer, Integer>(5, 5)));
//		
//		System.out.print("- Es 6x6 fuera de límite: ");
//		System.out.println(juego.esFueraLimiteCasilla(new Pair<Integer, Integer>(6, 6)));
//		
//		System.out.print("- Es 9x0 fuera de límite: ");
//		System.out.println(juego.esFueraLimiteCasilla(new Pair<Integer, Integer>(9, 0)));
//		
//		
//		
//		System.out.println("==============================================");
//		System.out.println("======== CONTAR CASILLAS ADYACENTES");
//		System.out.println("==============================================");
//		List<Pair<Integer, Integer>> lista_temporal = new ArrayList<Pair<Integer, Integer>>();
//		
//		System.out.print("- Casillas adyacentes a 0x0: ");
//		lista_temporal = juego.getCasillasAdyacentes(new Pair<Integer, Integer>(0, 0));
//		for (int i = 0 ; i < lista_temporal.size() ; i++)
//			System.out.print("   [" + lista_temporal.get(i).getFirst() + "x" + lista_temporal.get(i).getSecond() + "]");
//		System.out.println();
//		
//		System.out.print("- Casillas adyacentes a 2x0: ");
//		lista_temporal = juego.getCasillasAdyacentes(new Pair<Integer, Integer>(2, 0));
//		for (int i = 0 ; i < lista_temporal.size() ; i++)
//			System.out.print("   [" + lista_temporal.get(i).getFirst() + "x" + lista_temporal.get(i).getSecond() + "]");
//		System.out.println();
//		
//		System.out.print("- Casillas adyacentes a 3x3: ");
//		lista_temporal = juego.getCasillasAdyacentes(new Pair<Integer, Integer>(3, 3));
//		for (int i = 0 ; i < lista_temporal.size() ; i++)
//			System.out.print("   [" + lista_temporal.get(i).getFirst() + "x" + lista_temporal.get(i).getSecond() + "]");
//		System.out.println();
//		
//		System.out.print("- Casillas adyacentes a 6x6: ");
//		lista_temporal = juego.getCasillasAdyacentes(new Pair<Integer, Integer>(6, 6));
//		for (int i = 0 ; i < lista_temporal.size() ; i++)
//			System.out.print("   [" + lista_temporal.get(i).getFirst() + "x" + lista_temporal.get(i).getSecond() + "]");
//		System.out.println();
//		
//		System.out.print("- Casillas adyacentes a 5x4: ");
//		lista_temporal = juego.getCasillasAdyacentes(new Pair<Integer, Integer>(5, 4));
//		for (int i = 0 ; i < lista_temporal.size() ; i++)
//			System.out.print("   [" + lista_temporal.get(i).getFirst() + "x" + lista_temporal.get(i).getSecond() + "]");
//		System.out.println();
//		
//		
//		System.out.println("==============================================");
//		System.out.println("======== CONTAR BOMBILLAS ADYACENTES");
//		System.out.println("==============================================");
//		
//		System.out.print("- Bombillas adyacentes a 0x0: ");
//		System.out.println(juego.contarBombillasAdyacentes(new Pair<Integer, Integer>(0, 0)));
//		
//		System.out.print("- Bombillas adyacentes a 1x0: ");
//		System.out.println(juego.contarBombillasAdyacentes(new Pair<Integer, Integer>(1, 0)));
//		
//		System.out.print("- Bombillas adyacentes a 2x1: ");
//		System.out.println(juego.contarBombillasAdyacentes(new Pair<Integer, Integer>(2, 1)));
//		
//		System.out.print("- Bombillas adyacentes a 5x5: ");
//		System.out.println(juego.contarBombillasAdyacentes(new Pair<Integer, Integer>(5, 5)));
//		
//		System.out.print("- Bombillas adyacentes a 6x6: ");
//		System.out.println(juego.contarBombillasAdyacentes(new Pair<Integer, Integer>(6, 6)));
//		
//		
//		System.out.println("==============================================");
//		System.out.println("======== GET POSIBLES FUENTES DE LUZ");
//		System.out.println("==============================================");
//		
//		List<XYLocation> lista_locations = new ArrayList<XYLocation>();
//
//		lista_locations = juego.getPosiblesFuentesLuz();
//		System.out.println(lista_locations.toString());
//		
//		
//		
//		System.out.println("==============================================");
//		System.out.println("======== INCLUIR FUENTES DE LUZ");
//		System.out.println("==============================================");
//		
//		System.out.println("- Incluyendo en 3x0:");
//		juego.incluirFuenteLuz(new XYLocation(3, 0));
//		System.out.println("- Nuevo estado:");
//		juego.toString();
//		
//		System.out.println("- Incluyendo en 4x4:");
//		juego.incluirFuenteLuz(new XYLocation(4, 4));
//		System.out.println("- Nuevo estado:");
//		juego.toString();
//		
//		
//		
//		
//	}
}

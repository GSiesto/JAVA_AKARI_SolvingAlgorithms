# Akari solving algorithms

*Year 2016/2017, Second year of Software Engineering, Artificial Intelligence and Intelligent Systems*

*Guillermo Siesto Sánchez*

## El Juego

El juego japonés AKARI consiste en un tablero donde podemos distinguir distintos tipos de casilla: Casilla **Vacía**, representada en **blanco**.

- Casilla **Bombilla**, representada con un **círculo**.
- Casilla **Iluminada**, representada con una **cruz**. 
- Casilla **Bloque**, representada con **bloque negro**. Casilla **Bloque de restricción**, representada con números de **0** al **4** 

### Normas

1. Una bombilla emite luz en vertical y horizontal hasta que se encuentra una celda no utilizable de cualquier tipo o hasta que se acaba el tablero.   

2. No se puede colocar 2 bombillas en una misma fila o columna de manera que se iluminen entre ellas. Solo se pueden colocar en la misma fila o columna si entre medias existe una celda no utilizable de cualquier tipo que detiene la luz emitida por cada una.   

3. Las celdas no utilizables con un número entre el 0 y el 4 dentro indican el número exacto de bombillas que debe haber al finalizar el juego en las casillas inmediatamente adyacentes superior, derecha, inferior e izquierda. Las casillas situadas en las posiciones diagonales no se tienen en cuenta en esta restricción: 

**El objetivo será situar bombillas en celdas vacías hasta que todas las celdas vacías tengan una bombilla o estén iluminadas** 

## Representación del problema

Para representar el problema haremos uso de una **matriz de enteros** que podrá contener las siguientes variables:

**B = 5**;
`B de bloque para indicar que es un bloque donde no puede ir una fuente de luz ni pasar luz.`

**V = 6**;

`V de vacía para indicar que esa casilla no tiene bombilla ni está iluminada.`

**L = 7**;

`L para representar que esa casilla tiene una fuente de luz (bombilla).`

**I = 8**;

`I de iluminada para indicar que esa casilla está iluminada por una fuente de luz`

Además:

- **0** en una casilla indica que las adyacentes **no pueden tener luces.**
- **1** en una casilla indica que las **adyacentes solo pueden tener una luz.**
- **2** en una casilla indica que las **adyacentes solo pueden tener 2 luces.**
- **3** en una casilla indica que las **adyacentes solo pueden tener 3 luces.**
- **4** en una casilla indica que las **adyacentes deben tener las 4 luces adyacentes.**

### **Ejemplo:**

```
private static int[][] LIGHTCROSS = {
	{ B,1,V,V,B,B},
	{ B,V,V,V,V,0},
	{ V,V,V,B,V,V},
	{ V,V,V,V,V,V},
	{ V,V,B,1,V,V}
};
```

## Heurísticas

### **Heurística A:**

Es la heurística más lógica y simple. Suponemos que todas las casillas del tablero están vacías. No se tiene en cuenta ninguna tipo de bloque. Además, haremos una medida del tablero tal que en el momento que una fila o columna no tenga casillas vacías, el supuesto numero de filas o columnas irá en decremento, ya que dividiremos lo anterior por la supuesta fila o columna, cogiendo siempre el mayor.

### **Heurística B:**

Nos fijamos en el número de restricciones ya que sabemos que sea como sea va a tener que haber ese numero. A esta estimación le restamos las bombillas ya colocadas para que decremento según avanza el algoritmo.

### **Heurística C:**

Parecida a la Heurística B, devuelve el número de bombillas que quedarían por poner en las casillas adyacentes a las casillas restricciones.

### **Heurística D:**

A partir de una estimación matemática sin tener en cuenta bloques o restricciones:

En un tablero vacío de 2x2 para poder iluminarlo necesitaríamos 2 bombillas.
En caso de un 4x4 necesitamos 4.
En caso de un 8x8 necesitamos 8.
…

### **Heurística E:**

Es una modificación del anterior mezclado con una parte de lo que hicimos en A. Hacemos una suposición de filas y columnas según el numero de filas y/o columnas con casillas varias.

## Análisis de rendimiento

![](https://user-images.githubusercontent.com/6242946/59555601-491cfd80-8fb5-11e9-9ebb-f3c790f08db4.png)

![](https://user-images.githubusercontent.com/6242946/59555602-491cfd80-8fb5-11e9-805b-30527a54c9a3.png)

![](https://user-images.githubusercontent.com/6242946/59555603-491cfd80-8fb5-11e9-9e7c-16977d7bb6fd.png)

![](https://user-images.githubusercontent.com/6242946/59555604-49b59400-8fb5-11e9-86b9-fd167521f3c4.png)

![](https://user-images.githubusercontent.com/6242946/59555605-49b59400-8fb5-11e9-8d7a-109933c64279.png)

## Conclusiones

Tras haber realizado un análisis de rendimiento para cada caso, podemos decidir que algoritmo usar según los problemas a los que nos enfrentamos. Por ello, para que sea más fácil he indicado con colores verde y rojo los mejores y peores casos del escenario. Ha simple vista y generalizando podemos decir que la *HeurísticaB* con el *algoritmo A* *, cuanto más grande es el escenario, mejores resultados obtenemos, tanto en tiempo como en costes (Encima es mucho más rápido que *profundidad*)

- Si nuestro enunciado nos pide un algoritmo **Rápido pero No certero** usaremos:
  - *HeurísticaB* o *HeurísticaE.* Los dos tienen una marca de tiempo bastante rápida, aunque no siempre llegan a la solución optima, sobretodo en los escenarios más pequeños.

- Si nuestro enunciado nos pide un algoritmo **Bastante Rápido y Certero** usaremos:
  - *HeurísticaB* es el más certero dentro de los algoritmos rápidos. En algunas ocasiones llega a ser más rápido que el profundidad.
- Si nuestro enunciado nos pide un escenario **Muy Certero pero sin importar mucho el tiempo**:
  - Podríamos decir que la *HeurísticaC* siempre nos ha dado el resultado más certero en uno de los casos (*Avariciosos* o A**) aunque cualquiera de los A* suelen dar la solución óptima.

Concluimos diciendo que gracias a ir dirigiendo nuestro algoritmo según estimaciones o jugadas “trampa”, hemos conseguido algoritmos mucho más rápido que anchura y tan precisos como profundidad.


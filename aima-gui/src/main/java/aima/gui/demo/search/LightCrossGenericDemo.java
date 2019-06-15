package aima.gui.demo.search;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.core.agent.Action;
import aima.core.environment.lightCross.HeuristicaA;
import aima.core.environment.lightCross.HeuristicaB;
import aima.core.environment.lightCross.HeuristicaC;
import aima.core.environment.lightCross.HeuristicaD;
import aima.core.environment.lightCross.HeuristicaE;
import aima.core.environment.lightCross.LightCross;
import aima.core.environment.lightCross.LightCrossAction;
import aima.core.environment.lightCross.LightCrossFunctionFactory;
import aima.core.environment.lightCross.LightCrossGoalTest;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;

public class LightCrossGenericDemo {

	public final static int B = 5;//B de bloque para indicar que es un bloque donde no puede ir una fuente de luz ni pasar luz
	public final static int V = 6;//V de vacáa para indicar que esa casilla no tiene bombilla ni está iluminada
	public final static int L = 7;//L para representar que esa casilla tiene una fuente de luz
	public final static int I = 8;//I de iluminada para indicar que esa casilla está iluminada por una fuente de luz
	/* Además:
	 * - 0 en una casilla indica que las adyacentes no pueden tener luces
	 * - 1 en una casilla indica que las adyacentes solo pueden tener una luz
	 * - 2 en una casilla indica que las adyacentes solo pueden tener 2 luces
	 * - 3 en una casilla indica que las adyacentes solo pueden tener 3 luces
	 * - 4 en una casilla indica que las adyacentes deben tener las 4 luces adyacentes

	*/
	
	
	// Escenario 1
//	{ B,1,V,V,B,B},
//	{ B,V,V,V,V,0},
//	{ V,V,V,B,V,V},
//	{ V,V,V,V,V,V},
//	{ V,V,B,1,V,V}
//	};
	
	// Escenario 2
//	{V,1,V,V,B},
//	{V,V,V,V,0},
//	{V,V,V,V,V},
//	{2,V,V,V,B},
//	{V,1,V,V,V}
//	};
	
	// Escenario 3 (7)
//	{B,V,V,1,V,0,B},
//	{B,V,V,V,V,V,V},
//	{V,V,B,B,B,V,V},
//	{B,V,V,V,V,V,B},
//	{V,V,B,V,B,V,V},
//	};
	
	// Escenario 4 (8)
//	{V,V,V,1,V,0,V},
//	{B,V,V,V,V,V,V},
//	{V,V,B,V,B,V,V},
//	{B,V,V,V,V,V,B},
//	{V,V,B,V,2,V,V},
//	};
	
	// Escenario 5 (9)
//	{V,V,V,B,B,B},
//	{V,B,V,V,V,V},
//	{1,V,V,B,V,0},
//	{V,B,V,V,V,B},
//	{V,1,V,3,V,V},
//	{V,V,V,V,V,1},
//	{V,B,B,B,V,V}
//	};
	
	// Escenario 6 8 (~8)
//	{ V,B,V,1,V},
//	{ 1,V,V,B,V},
//	{ V,V,2,V,V},
//	{ V,B,B,V,B},
//	{ V,V,V,V,V},
//	{ B,V,B,V,B},
//	};
	
	
	// Escenario 7
//	{ B,B,B,V,V,B,V},
//	{ V,V,V,B,V,V,V},
//	{ V,B,V,V,V,2,B},
//	{ 0,V,V,V,V,V,V},
//	{ B,B,B,0,V,V,B},
//	{ B,V,V,V,0,V,B}
//	};
	

	private static int[][] LIGHTCROSS = {
			{ B,1,V,V,B,B},
			{ B,V,V,V,V,0},
			{ V,V,V,B,V,V},
			{ V,V,V,V,V,V},
			{ V,V,B,1,V,V}
			};
	
	// RompePCs
//			{ B,V,3,V,B,B},
//			{ V,V,V,V,V,V},
//			{ V,1,V,1,V,V},
//			{ V,V,B,V,V,V},
//			{ B,V,2,V,V,1},
//			{ V,V,B,V,V,B}};
	




	/**
	 * @param args
	 */
	public static void main(String[] args) {
		newLightCrossDemo();
	}

	private static void newLightCrossDemo() {
		System.out.println("\nSignificado de los símbolos de representación:");
		System.out.println("█ = Bloque");
		System.out.println(" =Vacía");
		System.out.println("§=Fuente de luz");
		System.out.println("░=Iluminada");

		LightCross lightCross;


		System.out.println("\n<------------------------>");
		System.out.println("Profundidad (DFS (Graph))");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),new DepthFirstSearch(new GraphSearch()));

		System.out.println("\n<------------------------>");
		System.out.println("Anchura (BFS (Graph))");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),new BreadthFirstSearch(new GraphSearch()));


		System.out.println("\n<------------------------>");
		System.out.println("HeurísticaA con Avaricioso (Greedy Best First (Graph)");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),
					new GreedyBestFirstSearch(new GraphSearch(),new HeuristicaA()));

		System.out.println("\n<------------------------>");
		System.out.println("HeurísticaA con A* (Graph)");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),
					new AStarSearch(new GraphSearch(),new HeuristicaA()));


		System.out.println("\n<------------------------>");
		System.out.println("HeurísticaB con Avaricioso (Greedy Best First (Graph)");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),
					new GreedyBestFirstSearch(new GraphSearch(),new HeuristicaB()));


		System.out.println("\n<------------------------>");
		System.out.println("HeurísticaB con A* (Graph)");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),
					new AStarSearch(new GraphSearch(),new HeuristicaB()));


		System.out.println("\n<------------------------>");
		System.out.println("HeurísticaC con Avaricioso (Greedy Best First (Graph)");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),
					new GreedyBestFirstSearch(new GraphSearch(),new HeuristicaC()));


		System.out.println("\n<------------------------>");
		System.out.println("HeurísticaC con A* (Graph)");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),
					new AStarSearch(new GraphSearch(),new HeuristicaC()));


		System.out.println("\n<------------------------>");
		System.out.println("HeurísticaD con Avaricioso (Greedy Best First (Graph)");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),
					new GreedyBestFirstSearch(new GraphSearch(),new HeuristicaD()));

		System.out.println("\n<------------------------>");
		System.out.println("HeurísticaD con A* (Graph)");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),
					new AStarSearch(new GraphSearch(),new HeuristicaD()));
		
		System.out.println("\n<------------------------>");
		System.out.println("HeurísticaE con Avaricioso (Greedy Best First (Graph)");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),
					new GreedyBestFirstSearch(new GraphSearch(),new HeuristicaE()));

		System.out.println("\n<------------------------>");
		System.out.println("HeurísticaE con E* (Graph)");
		System.out.println("<------------------------>");
		lightCross= new LightCross(LIGHTCROSS);
		uninformedAndInformedSearchDemo(lightCross, LightCrossFunctionFactory.getActionsFunction(),
					LightCrossFunctionFactory.getResultFunction(),
					new LightCrossGoalTest(),
					new AStarSearch(new GraphSearch(),new HeuristicaE()));


	}


	/*******************search without and with heuristic********************/

	/**
	 *
	 */

	private static void uninformedAndInformedSearchDemo(Object initialState,
			ActionsFunction actionsFunction,ResultFunction resultFunction,
			GoalTest goalTest,SearchForActions searchType) {
		try {
			Problem problem = new Problem(initialState,actionsFunction,resultFunction,goalTest);

			SearchForActions search = searchType;
			long timeI = System.currentTimeMillis();
			SearchAgent agent = new SearchAgent(problem, search);
			long timeF = System.currentTimeMillis();
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			printTimeExecution(timeI,timeF);
			printGraphicActions(agent.getActions(),initialState);//-> Método opcional para ver el resultado al detalle de forma "gráfica"
			printLastActions(agent.getActions(),initialState);//-> Método opcional para ver el resultado final forma "gráfica"
		} catch (Exception e) {
			e.printStackTrace();
		}
	}








	/*******************otros metodos********************/
	private static void printInstrumentation(Properties properties) {
		double pathcost=0;
		double nodesexpanded=0;
		double branching=0;
		Iterator<Object> keys = properties.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String property = properties.getProperty(key);
			if (key=="pathCost")  pathcost= Double.parseDouble(property);
			if (key=="nodesExpanded")  nodesexpanded= Double.parseDouble(property);

			if (key!="maxQueueSize")  System.out.println(key + " : " + property);
		}

		if (pathcost>0 && nodesexpanded>0)
		{
			branching=Math.pow(nodesexpanded, 1.0/pathcost);
			System.out.println("branchingfactor: " + branching);
		}

	}

	private static void printActions(List<Action> actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = actions.get(i).toString();
			System.out.println(action);
		}
	}

		private static void printTimeExecution(long timeI,long timeF){
		long milisegundos = timeF - timeI;
		long hora = milisegundos/3600000;
		long restohora = milisegundos%3600000;
		long minuto = restohora/60000;
		long restominuto = restohora%60000;
		long segundo = restominuto/1000;
		long restosegundo = restominuto%1000;
		System.out.println("\nTimeExecution---> " + hora + ":" + minuto + ":" + segundo + "." + restosegundo);
	}

		//Método opcional para ver el resultado al detalle de forma "gráfica"
		private static void printGraphicActions(List<Action> actions, Object initialState) {
			System.out.println("\n Estado inicial:   ");
			LightCross lightCross = (LightCross) initialState;
			lightCross.printCurrentState();
			for (int i = 0; i < actions.size(); i++) {
				String action = actions.get(i).toString();
				System.out.println("\nAcción número " + (i+1));
				System.out.println(action);
				LightCrossAction lightCrossAction = (LightCrossAction) actions.get(i);
				lightCross.incluirFuenteLuz(lightCrossAction.getCoordenadas());//cambiar este m�todo por el m�todo que cambie la luz de una casilla en la implementaci�n de cada uno
				lightCross.printCurrentState();
			}
			System.out.println();
		}
		//M�todo opcional para ver el estado soluci�n de forma "gr�fica"
		private static void printLastActions(List<Action> actions, Object initialState) {
			System.out.println("\n Estado inicial:   ");
			LightCross lightCross = (LightCross) initialState;
			lightCross.printCurrentState();
			for (int i = 0; i < actions.size(); i++) {
				String action = actions.get(i).toString();
				System.out.print("\nAcciún número " + (i+1)+ " " + action);
				LightCrossAction lightCrossAction = (LightCrossAction) actions.get(i);
				lightCross.incluirFuenteLuz(lightCrossAction.getCoordenadas());//cambiar este m�todo por el m�todo que cambie la luz de una casilla en la implementaci�n de cada uno
				if (i == actions.size()-1) {
					System.out.println("\n Estado final:   ");
					lightCross.printCurrentState();
				}
			}
			System.out.println();
		}

}

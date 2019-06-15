package aima.gui.demo.search;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.core.agent.Action;
import aima.core.environment.ochopuzzle.HeuristicaManhattan;
import aima.core.environment.ochopuzzle.MisplacedTilleHeuristicFunction;
import aima.core.environment.ochopuzzle.Puzzle;
import aima.core.environment.ochopuzzle.PuzzleAction;
import aima.core.environment.ochopuzzle.PuzzleFunctionFactory;
import aima.core.environment.ochopuzzle.PuzzleGoalTest;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;

public class PuzzleDemoEjemplo3x3 {
	
	
		
	//Nivel simple de 3x3 con soluci�n optima en 10 pasos
	private static int[][] PUZZLE = {
			{ 1,4,2 },
			{ 6,0,7 },
			{ 8,3,5 }
			};



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		newPuzzleDemo();

	}
    
	private static void newPuzzleDemo() {
		
		
		
		Puzzle puzzle;
		puzzle= new Puzzle(PUZZLE);
		
		puzzle.printCurrentState();
		System.out.println("\n<------------------------>");
		System.out.println("==========================");
		System.out.println("Profundidad (DFS (Graph))");
		System.out.println("==========================");
		System.out.println("<------------------------>");
		uninformedAndInformedSearchDemo(puzzle, PuzzleFunctionFactory.getActionsFunction(),
					PuzzleFunctionFactory.getResultFunction(),
					new PuzzleGoalTest(),new DepthFirstSearch(new GraphSearch()));
		
		
		System.out.println("\n<------------------------>");
		System.out.println("==========================");
		System.out.println("Anchura (BFS (Graph))");
		System.out.println("==========================");
		System.out.println("<------------------------>");
		puzzle= new Puzzle(PUZZLE);
		uninformedAndInformedSearchDemo(puzzle, PuzzleFunctionFactory.getActionsFunction(),
					PuzzleFunctionFactory.getResultFunction(),
					new PuzzleGoalTest(),new BreadthFirstSearch(new GraphSearch()));
		
		System.out.println("\n<------------------------>");
		System.out.println("==========================");
		System.out.println("Profundidad Limitada a Soluci�n Optima");
		System.out.println("==========================");
		System.out.println("<------------------------>");
		puzzle= new Puzzle(PUZZLE);
		uninformedAndInformedSearchDemo(puzzle, PuzzleFunctionFactory.getActionsFunction(),
					PuzzleFunctionFactory.getResultFunction(),
					new PuzzleGoalTest(),new DepthLimitedSearch(10));
		
		System.out.println("\n<------------------------>");
		System.out.println("==========================");
		System.out.println("Profundidad Limitada a Soluci�n Optima + 2 niveles");
		System.out.println("==========================");
		System.out.println("<------------------------>");
		puzzle= new Puzzle(PUZZLE);
		uninformedAndInformedSearchDemo(puzzle, PuzzleFunctionFactory.getActionsFunction(),
					PuzzleFunctionFactory.getResultFunction(),
					new PuzzleGoalTest(),new DepthLimitedSearch(12));
		
		System.out.println("\n<------------------------>");
		System.out.println("==========================");
		System.out.println("Iterativa Profundidad");
		System.out.println("==========================");
		System.out.println("<------------------------>");
		puzzle= new Puzzle(PUZZLE);
		uninformedAndInformedSearchDemo(puzzle, PuzzleFunctionFactory.getActionsFunction(),
					PuzzleFunctionFactory.getResultFunction(),
					new PuzzleGoalTest(),new IterativeDeepeningSearch());
		
		System.out.println("\n<------------------------>");
		System.out.println("==========================");
		System.out.println("Heur�stica1 con Avaricioso (Greedy Best First (Graph)");
		System.out.println("==========================");
		System.out.println("<------------------------>");
		puzzle= new Puzzle(PUZZLE);
		uninformedAndInformedSearchDemo(puzzle, PuzzleFunctionFactory.getActionsFunction(),
					PuzzleFunctionFactory.getResultFunction(),
					new PuzzleGoalTest(),
					new GreedyBestFirstSearch(new GraphSearch(),new MisplacedTilleHeuristicFunction()));
		
		System.out.println("\n<------------------------>");
		System.out.println("==========================");
		System.out.println("Heur�stica1 con A* (Graph)");
		System.out.println("==========================");
		System.out.println("<------------------------>");
		puzzle= new Puzzle(PUZZLE);
		uninformedAndInformedSearchDemo(puzzle, PuzzleFunctionFactory.getActionsFunction(),
					PuzzleFunctionFactory.getResultFunction(),
					new PuzzleGoalTest(),
					new AStarSearch(new GraphSearch(),new MisplacedTilleHeuristicFunction()));
		
	
		
	System.out.println("\n<------------------------>");
	System.out.println("==========================");
	System.out.println("Heur�stica2 con Avaricioso (Greedy Best First (Graph)");
	System.out.println("==========================");
	System.out.println("<------------------------>");
	puzzle= new Puzzle(PUZZLE);
	uninformedAndInformedSearchDemo(puzzle, PuzzleFunctionFactory.getActionsFunction(),
				PuzzleFunctionFactory.getResultFunction(),
				new PuzzleGoalTest(),
				new GreedyBestFirstSearch(new GraphSearch(),new HeuristicaManhattan()));
	
	System.out.println("\n<------------------------>");
	System.out.println("==========================");
	System.out.println("Heur�stica2 con A* (Graph)");
	System.out.println("==========================");
	System.out.println("<------------------------>");
	puzzle= new Puzzle(PUZZLE);
	uninformedAndInformedSearchDemo(puzzle, PuzzleFunctionFactory.getActionsFunction(),
				PuzzleFunctionFactory.getResultFunction(),
				new PuzzleGoalTest(),
				new AStarSearch(new GraphSearch(),new HeuristicaManhattan()));
	
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
			//printGraphicActions(agent.getActions(),initialState);//-> M�todo opcional para ver el resultado al detalle de forma "gr�fica"
			
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
			
			System.out.println(key + " : " + property);
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

		//M�todo opcional para ver el resultado al detalle de forma "gr�fica"
		private static void printGraphicActions(List<Action> actions, Object initialState) {
			System.out.println("\n Estado inicial:   ");
			Puzzle puzzle = (Puzzle) initialState;
			puzzle.printCurrentState();
			for (int i = 0; i < actions.size(); i++) {
				String action = actions.get(i).toString();
				System.out.println("\nAcci�n n�mero " + (i+1));
				System.out.println(action);
							
				PuzzleAction puzzleAction = (PuzzleAction) actions.get(i);
				puzzle.moverHuecoACasilla(puzzleAction.getOrigen(),puzzleAction.getDestino());//cambiar este m�todo por el m�todo que mueva el almacenero en la implementaci�n de cada uno
				puzzle.printCurrentState();
			}
			System.out.println();
		}
		
}

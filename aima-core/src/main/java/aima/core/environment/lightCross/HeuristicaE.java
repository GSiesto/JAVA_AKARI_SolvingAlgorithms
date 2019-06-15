package aima.core.environment.lightCross;

import aima.core.search.framework.evalfunc.HeuristicFunction;



/**
 * 
 * @author Guillermo Siesto
 * GRUPO: C
 * AVARICIOSO
 */
public class HeuristicaE implements HeuristicFunction{


	@Override
	public double h(Object state) {
		LightCross lightState = (LightCross) state;
		int retVal = 0;
	
		retVal += lightState.evaluarHeuristicaE();
		
		return retVal;
	}

	
}
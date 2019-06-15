package aima.core.environment.lightCross;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.util.datastructure.XYLocation;

/**
@author aeprieto
*/


public class LightCrossFunctionFactory {
	

	private static ActionsFunction actionsFunction = null;
	private static ResultFunction resultFunction = null;

	public static ActionsFunction getActionsFunction() {
		if(null == actionsFunction)
			actionsFunction = new LightsOutActionsFunction();
		return actionsFunction;
	}

	public static ResultFunction getResultFunction() {
		if(null == resultFunction)
			resultFunction = new LightsOutResultFunction();
		return resultFunction;
	}
	
	private static class LightsOutActionsFunction implements ActionsFunction{

		@Override
		public Set<Action> actions(Object s) {
			System.gc();
			Set<Action> actions = new LinkedHashSet<Action>();
			
			LightCross lightCross = (LightCross) s;
			//obtener movimientos
			List<XYLocation> casillas = lightCross.getPosiblesFuentesLuz();
			//para cada movimiento valido crear accion y anadirla a conjunto
			for(XYLocation casilla: casillas)
					actions.add(new LightCrossAction(LightCrossAction.INCLUIR_FUENTE_LUZ,casilla));
			return actions;
		}
	}
	
	private static class LightsOutResultFunction implements ResultFunction{

		@Override
		public Object result(Object s, Action a) {
			if (a instanceof LightCrossAction){
				LightCrossAction sAction = (LightCrossAction) a;
				LightCross state = (LightCross) s;
				LightCross newState = new LightCross(state.getState());
				if (sAction.getName() == LightCrossAction.INCLUIR_FUENTE_LUZ)
					newState.incluirFuenteLuz(sAction.getCoordenadas());
				
				s = newState;
									
			}
				
			return s;
		}
		
	}
		
}
	

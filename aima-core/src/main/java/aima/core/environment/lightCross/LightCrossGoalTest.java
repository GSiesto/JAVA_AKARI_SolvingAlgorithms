package aima.core.environment.lightCross;

import aima.core.search.framework.problem.GoalTest;

/**
@author aeprieto
*/

public class LightCrossGoalTest implements GoalTest {
	
	@Override
	
	public boolean isGoalState(Object state) {
		LightCross lightCross = (LightCross) state;
		//return sArrays.equals(sokoban.getState(), sokoban.getGoal());
		return lightCross.checkFinish();
	} 
	
}

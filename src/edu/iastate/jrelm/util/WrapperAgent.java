package edu.iastate.jrelm.util;

import edu.iastate.jrelm.core.JReLMAgent;
import edu.iastate.jrelm.rl.ReinforcementLearner;

/**
 * Dummy agent used to wrap anonymous ReinforcementLearners.
 *
 */
public class WrapperAgent<RL extends ReinforcementLearner> implements
		JReLMAgent<RL> {
	String myID;
	RL myLearner;

	public WrapperAgent(String id, RL learner) {
		myID = id;
		myLearner = learner;
	}

	public String getID() {
		return myID;
	}

	public RL getLearner() {
		return myLearner;
	}
}
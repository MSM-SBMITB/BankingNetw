package edu.iastate.jrelm.core;

import edu.iastate.jrelm.rl.ReinforcementLearner;

/**
 * Interface for desiginating an agent that uses JReLM learning components
 * 
 * @author Charles Gieseler
 *
 */
public interface JReLMAgent<RL extends ReinforcementLearner> {

	/**
	 * Retreive a String identifier for this agent.
	 * 
	 * @return
	 */
	public String getID();

	/**
	 * Retrieve the leaner this agent is using.
	 * 
	 * @return
	 */
	public RL getLearner();

}

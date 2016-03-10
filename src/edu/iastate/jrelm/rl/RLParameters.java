/*
 * Created on Jun 18, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.iastate.jrelm.rl;

/**
 * Interface for a collection of parameters needed for JReLMLearners.
 * 
 * @author Charles Gieseler
 *
 */
public interface RLParameters {

	/**
	 * Get the name of the algorithm these parameters are for.
	 * 
	 * @return String name of the algorithm.
	 */
	public abstract String getName();

	/**
	 * Get a the names of the parameters
	 * 
	 * @return Array of parameter names as String
	 */
	public abstract String[] getParameterNames();

	/**
	 * Checks to make sure the current values for all parameters are valid.
	 * 
	 * @return true if all parameter values are valid, false otherwise.
	 */
	public abstract boolean validateParameters();

	/**
	 * Get the seed for the psuedo-random number generator used by the
	 * ReinforcementLearner. This parameter will be common to most (if not all)
	 * learners.
	 * 
	 * Random seeds need to be included as learner parameters to facilitate the
	 * assignment of separate seeds to individual learners. This is necessary
	 * for certain situations where variations in behavior accross learners
	 * needs to be forced. For example, learners embedded in separate agents
	 * will likely need different seeds to avoid identical starting behavior,
	 * unless they are intialized with policies.
	 * 
	 * @return
	 */
	public abstract int getRandomSeed();
}

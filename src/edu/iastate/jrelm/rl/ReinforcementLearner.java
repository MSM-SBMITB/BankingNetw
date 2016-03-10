/*
 * Created on Aug 20, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.iastate.jrelm.rl;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.core.Feedback;

/**
 * For classes that implement reinforcement learning algorithms. Classes
 * implementing this interface are responsible for driving the learning process
 * of specific algorithms.
 * 
 * Reinforcement learning algorithms make use of a policy to represent learned
 * knowledge. Policies themselves require access to the space of possible
 * actions, represented by ActionDomains. As such an ReinforcementLearner will
 * make use of with a StatelessPolicy and an ActionDomain.
 * <P>
 * Feedback is parameterized since required input will vary depending on the
 * specific reinforcement learning algorithm and the particular simulation
 * environment.
 * 
 * @author Charles Giseler
 *
 * @param <PA>
 *            - the type of ReinforcementLearner parameters (RLParameters) this
 *            learner accepts. This will usually be a specific set of paramters
 *            needed for a particular learning algorithm.
 * @param <I>
 *            - the type of identifier being used to distiguish Actions
 * @param <A>
 *            - the type of Actions this learner is working with
 * @param <F>
 *            - the type of reinforcement object (Feedback) that this learner
 *            accepts
 * @param <PO>
 *            - the type of Policy that this learner updates and uses make new
 *            Action selections
 *
 */

public interface ReinforcementLearner<PA extends RLParameters, I, A extends Action, F extends Feedback, PO extends Policy> {

	/**
	 * Initiate the learning process using given feedback. Feedback is
	 * associated with a the last Action chosen by this leaner and is treated as
	 * a reinforcement value for that Action. Feedback used to update the
	 * probability of choosing Actions in the learner's Policy according to the
	 * specific learning algorithm implemented.
	 * <P>
	 * Note: Most often feedback is for the last Action chosen, so given
	 * ActionID will usually point to this Action. As such, many RLEnigine
	 * implementations may also provide update() methods that simply accept
	 * feedback and associate it with the last Action chosen.
	 * 
	 * @param reward
	 *            - feedback for the specified action
	 */
	public void update(F reward);

	/**
	 * Elicits a new choice of action. The action will be chosen according to
	 * selection rule of the SimpleStatelessPolicy. Actions are chosen from a
	 * DiscreteFiniteDomain.
	 * 
	 * @return the next action chosen. Selected actions can be any object
	 *         implementing the Action interface.
	 * @see edu.iastate.jrelm.core.Action
	 */
	public A chooseAction();

	/**
	 * Retrieve the RLParameters that contain settings for this learning
	 * algorithm.
	 * 
	 * @return learning algorithm settings as RLParameters
	 */
	public PA getParameters();

	/**
	 * Create a default set of parameters that can be used with this learner.
	 * 
	 * @return learning parameters compatible with this learner, initialized to
	 *         default settings.
	 */
	public PA makeParameters();

	/**
	 * Sets the current settings for this learning algorithm.
	 *
	 * @param parameters
	 *            - new settings for this algorithm as RLParameters
	 */
	public void setParameters(PA newParams);

	/**
	 * Retrieve the StatelessPolicy being used to represent learned knowledge.
	 * 
	 * @return the Policy being used by this ReinforcementLearner. The policy
	 *         can be any object implementing the StatelessPolicy interface.
	 * @see edu.iastate.jrelm.rl.StatelessPolicy
	 */
	public PO getPolicy();

	/**
	 * Set the StatelessPolicy to be used to represent learned knowledge.
	 * 
	 * @param p
	 *            - The policy can be any object implementing the
	 *            StatelessPolicy interface.
	 * @see edu.iastate.jrelm.rl.StatelessPolicy
	 */
	public void setPolicy(PO newPolicy);

	/**
	 * Retrieves the name of the learning algorithm this learner implements.
	 * 
	 * @return - the algorithm name
	 */
	public String getName();

}

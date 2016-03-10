/*
 * Created on Jun 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.iastate.jrelm.rl;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.core.State;

/**
 * Interface for building a stateless reinforcement learning policy. This type
 * of policy simply maintains a distribution guiding action choice irrespective
 * of the current state of the world. That is, it simply maintains a likelihood
 * of selection for each action for all world states.
 * 
 * This type of policy can be useful in simple or static environments or with
 * learners that implement stateless algorithms (e.g. VRELearner)
 * 
 * @see edu.iastate.jrelm.rl.Policy
 * 
 * @author Charles Gieseler
 *
 * @param I
 *            - the type of action identifier used by the ActionDomain given to
 *            this policy
 * @param A
 *            - the type of Action managed by the ActionDomain given to this
 *            policy
 */
public interface StatelessPolicy<I, A extends Action> extends
		Policy<I, A, Object, State> {

	/**
	 * Choose a new Action based on the current policy.
	 * 
	 * @return an Action selected according to the policy.
	 */
	public A generateAction();

	/**
	 * Get the ActionDomain that this policy selects Actions from.
	 * 
	 * @return the associated ActionDomain
	 */
	public ActionDomain<I, A> getActionDomain();

	/**
	 * Retrieve the last Action chosen by this policy.
	 */
	public A getLastAction();

	/**
	 * Gets the current probability of choosing an action. Parameter actionIndex
	 * indicates which action in the policy's domain to lookup. Note, this does
	 * not retrieve probabilities for State-Action pairs as
	 * Policy.getProbability() does. This means the probability for actions does
	 * not depend on the state of the world.
	 * 
	 * @see Policy#getProbability(SI, AI)
	 * 
	 * @param actionID
	 *            - the identifier indicating which action to retrieve a
	 *            probability for.
	 * @return the probability of the Action specified by actionID. Double.NaN
	 *         should be returned if the ActionDomain does not contain an Action
	 *         with the given ID.
	 */
	public double getProbability(I actionID);

	/**
	 * Updates the probability of choosing the indicated Action. Note, this does
	 * not update probability values for State-Action pairs as
	 * Policy.setProbability(). This means the probability for actions is
	 * independent of the state of the world.
	 * 
	 * @see Policy#setProbability(SI, AI, double)
	 * 
	 * @param actionID
	 *            - the identifier of the desired Action in this policy's
	 *            ActionDomain.
	 * @param newValue
	 *            - new choice probability value to associate with this action.
	 */
	public void setProbability(I actionID, double newValue);

}

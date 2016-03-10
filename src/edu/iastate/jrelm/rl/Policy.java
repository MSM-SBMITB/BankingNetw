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
import edu.iastate.jrelm.core.StateDomain;

/**
 * Interface for building a reinforcement learning policy which is typically a
 * mapping from States to Actions. The policy essentially represents learned
 * knowledge and is the basis for choosing new actions. A ReinforcementLearner
 * updates the policy as dictated by the specific learning algorithm that it
 * implements and queriess the policy for a choice of the next action.
 * 
 * Since a policy maps between States and Actions and may assign values to
 * States, Actions, or State-Action pairs, it should operate over a given
 * ActionDomain and StateDomain.
 * 
 * @author Charles Gieseler
 *
 * @param AI
 *            - the type of Action identifier used in the ActionDomain given to
 *            this policy
 * @param A
 *            - the type of Action managed by the ActionDomain given to this
 *            policy
 * @param SI
 *            - the type of State identifier used in the StateDomain given to
 *            this policy
 * @param S
 *            - the type of State managed by the StateDomain given to this
 *            policy
 */
public interface Policy<AI, A extends Action, SI, S extends State> {

	/**
	 * Given the current State as indicated by the stateID, choose a new Action
	 * according to the current policy.
	 * 
	 * @return an Action selected according to the policy.
	 */
	public A generateAction(SI stateID);

	/**
	 * Get the ActionDomain that this policy selects Actions from.
	 * 
	 * @return the associated ActionDomain
	 */
	public ActionDomain<AI, A> getActionDomain();

	/**
	 * Get the StateDomain this policy is using
	 * 
	 * @return
	 */
	public StateDomain<SI, S> getStateDomain();

	/**
	 * Retrieve the last Action chosen by this policy.
	 */
	public A getLastAction();

	/**
	 * Gets the current probability of choosing a particular action from the
	 * current state.
	 * 
	 * @param actionID
	 *            - the identifier of the desired Action in the ActionDomain
	 * @param stateID
	 *            - the identifier of the desired State in the StateDomain
	 * @return the probability of choosing the specified Action from the given
	 *         State. Should return Double.NaN if actionID or stateID are do not
	 *         point to elements in the respective domains.
	 */
	public double getProbability(SI stateID, AI actionID);

	/**
	 * Updates the probability of choosing an Action from the given State.
	 * 
	 * @param actionID
	 *            - the identifier of the desired Action in this policy's
	 *            ActionDomain.
	 * @param stateID
	 *            - the identifier of the desired State in this policy's
	 *            StateDomain.
	 * @param newValue
	 *            - new choice probability value to associate with this
	 *            action-state pair.
	 */
	public void setProbability(SI stateID, AI actionID, double newValue);

	/**
	 * Should reset the psuedo-random number generator used by this Policy when
	 * generating new Action selections.
	 *
	 * @param seed
	 */
	public void setRandomSeed(int seed);
}

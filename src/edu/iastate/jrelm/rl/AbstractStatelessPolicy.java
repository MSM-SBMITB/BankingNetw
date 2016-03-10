package edu.iastate.jrelm.rl;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.State;
import edu.iastate.jrelm.core.StateDomain;

/**
 * An abstract implementation of the StatelessPolicy interface.
 * 
 * The StatelessPolicy is a specialization of the general Policy that doesn't
 * make use of States. This means, as an interface, StatelessPolicy inherits
 * extra State related methods from Policy that will never be used. This
 * abstract class is provided as a convenience to define away the extra methods
 * for those who want to implement a StatelessPolicy.
 * 
 * @author Charles Gieseler
 * 
 * @param I
 *            - the type of action identifier used by the ActionDomain given to
 *            this policy
 * @param A
 *            - the type of Action managed by the ActionDomain given to this
 *            policy
 *
 */
public abstract class AbstractStatelessPolicy<I, A extends Action> implements
		StatelessPolicy<I, A> {

	/* ** State related methods ** */
	public A generateAction(Object stateID) {
		return generateAction();
	}

	/**
	 * Defined away since this type of policy does not work with States. This
	 * method will return null.
	 */
	public StateDomain<Object, State> getStateDomain() {
		return null;
	}

	public double getProbability(Object stateID, I actionID) {
		return getProbability(actionID);
	}

	public void setProbability(Object stateID, I actionID, double newValue) {
		setProbability(actionID, newValue);
	}
}

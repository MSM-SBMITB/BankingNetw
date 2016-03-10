package edu.iastate.jrelm.core;

import java.util.ArrayList;
import java.util.Collection;

import edu.iastate.jrelm.core.SimpleAction;

/**
 * SimpleActionDomain is basic implementation of the ActionDomain interface.
 * This no-frills domain is essentially built around an ArrayList of
 * SimpleActions that wrap a given collection of Objects that represent action
 * choices.
 * <P>
 * This domain builds itself from a given collection of Objects. Each Object is
 * considered to represent a choice of action and the Collection the list of all
 * possible actions an ReinforcementLearner can choose from. Internally, each
 * Object is wrapped in SimpleAction and the space of possible actions is
 * organized by Integer index. The use of SimpleAction wrappers allow the domain
 * to be used with other learning components (i.e. ReinforcementLearner,
 * StatelessPolicy).
 * <P>
 * 
 * @param <O>
 *            - This is the original type of action Objects in the given
 *            collection.
 */
public class SimpleActionDomain<O> implements
		ActionDomain<Integer, SimpleAction<O>> {

	// List of object wrapped in SimpleActions
	private ArrayList<SimpleAction<O>> actionList;

	// List of identifiers for all actions in the domain
	private ArrayList<Integer> idList;

	/**
	 * Build a domain from the given collection of Objects.
	 * 
	 * @param actions
	 *            - list of objects specifying actions.
	 */
	public SimpleActionDomain(Collection<O> actions) {

		actionList = new ArrayList<SimpleAction<O>>();

		SimpleAction<O> newAction;

		int i = 0; // counter to provide each action
					// with an index id

		// Iterate through the given collection of action Ojects,
		// wrap them in SimpleActions, and add them to the list of
		// possible action choices
		idList = new ArrayList<Integer>();
		for (O act : actions) {
			newAction = new SimpleAction<O>(i, act);
			actionList.add(newAction);
			idList.add(i);
			i++;
		}
	}

	/**
	 * @see edu.iastate.jrelm.core.ActionDomain
	 */
	public boolean containsAction(SimpleAction<O> actionToCheck) {
		return actionList.contains(actionToCheck);
	}

	/**
	 * @see edu.iastate.jrelm.core.ActionDomain
	 */
	public SimpleAction<O> getAction(Integer id) {
		return actionList.get(id);
	}

	/**
	 * Convenience method to allow actions to be retrieved with an int id. This
	 * id should be the index of the desired object in the original collection
	 * given to the domain in the constructor.
	 * 
	 * @param actionIndex
	 *            - int index of the desired action in the domain
	 * @return - the desired action (note: the original object is wrapped in a
	 *         SimpleAction)
	 */
	public SimpleAction<O> getAction(int actionIndex) {
		return actionList.get(actionIndex);
	}

	/**
	 * 
	 * @see edu.iastate.jrelm.core.DiscreteFiniteDomain
	 */
	public int size() {
		return actionList.size();
	}

	public ArrayList<Integer> getIDList() {
		return idList;
	}
}
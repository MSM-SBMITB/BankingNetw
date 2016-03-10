package edu.iastate.jrelm.core;

import java.util.ArrayList;
import java.util.Collection;

import edu.iastate.jrelm.core.SimpleState;

/**
 * SimpleStateDomain is basic implementation of the StateDomain interface. This
 * no-frills domain is essentially built around an ArrayList of SimpleStates
 * that wrap a given collection of Objects that represent state choices.
 * <P>
 * This domain builds itself from a given collection of Objects. Each Object is
 * considered to represent a state of the environment external to the
 * ReinforcementLearning (or agent). The Collection is the list of all possible
 * states an ReinforcementLearner may encounter. Internally, each Object is
 * wrapped in SimpleState and the space of possible states is organized by
 * Integer index. The use of SimpleState wrappers allow the domain to be used
 * with other learning components (i.e. ReinforcementLearners that implement
 * stateful learning algorithms).
 * <P>
 * Note, this class is only appropriate for contexts where the set of all
 * possible states is discrete and finite.
 * 
 * @param <O>
 *            - This is the original type of state Objects in the given
 *            collection.
 */
public class SimpleStateDomain<O> implements
		StateDomain<Integer, SimpleState<O>> {

	// List of object wrapped in SimpleStates
	private ArrayList<SimpleState<O>> stateList;

	// List of identifiers for all states in the domain
	private ArrayList<Integer> idList;

	/**
	 * Build a domain from the given collection of Objects.
	 * 
	 * @param states
	 *            - list of objects specifying states.
	 */
	public SimpleStateDomain(Collection<O> states) {

		stateList = new ArrayList<SimpleState<O>>();

		SimpleState<O> newAction;

		int i = 0; // counter to provide each state
					// with an index id

		// Iterate through the given collection of state Ojects,
		// wrap them in SimpleActions, and add them to the list of
		// possible state choices
		idList = new ArrayList<Integer>();
		for (O act : states) {
			newAction = new SimpleState<O>(i, act);
			stateList.add(newAction);
			idList.add(i);
			i++;
		}
	}

	/**
	 * @see edu.iastate.jrelm.core.StateDomain
	 */
	public boolean containsState(SimpleState<O> stateToCheck) {
		return stateList.contains(stateToCheck);
	}

	/**
	 * @see edu.iastate.jrelm.core.StateDomain
	 */
	public SimpleState<O> getState(Integer id) {
		return stateList.get(id);
	}

	/**
	 * Convenience method to allow states to be retrieved with an int id. This
	 * id should be the index of the desired object in the original collection
	 * given to the domain in the constructor.
	 * 
	 * @param stateIndex
	 *            - int index of the desired state in the domain
	 * @return - the desired state (note: the original object is wrapped in a
	 *         SimpleState)
	 */
	public SimpleState<O> getState(int stateIndex) {
		return stateList.get(stateIndex);
	}

	/**
	 * 
	 * @see edu.iastate.jrelm.core.DiscreteFiniteDomain
	 */
	public int size() {
		return stateList.size();
	}

	public ArrayList<Integer> getIDList() {
		return idList;
	}
}
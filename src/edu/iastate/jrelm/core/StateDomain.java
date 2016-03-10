package edu.iastate.jrelm.core;

import java.util.ArrayList;

/**
 * Representation of an agent's state space. That is, the collection of world
 * states that an agent may encounter in an environment.
 * 
 * The type of State as well as state identifier may be paramertized. These are
 * similar to the Key/Value types that may be parameterized for Hasthtable.
 * 
 * @see java.util.Hashtable
 * 
 * @author Charles Gieseler
 * 
 * @param I
 *            - the type used to identify and organize States in this domain
 * @param S
 *            - the type of State this domain manages
 * 
 * @see edu.iastate.jrelm.core.Action
 */
public interface StateDomain<I, S extends State> {

	/**
	 * Determines if the given Action is in this domain.
	 */
	public boolean containsState(S stateToCheck);

	/**
	 * Retrieves the State indicated by the id object. Should return null if the
	 * id does not match an existing Action.
	 * 
	 * @param id
	 *            - identifier indicating the desired State
	 * @return the requested State associated with id in this domain
	 */
	public S getState(I id);

	/**
	 * Retrieve a list of the identifiers for all States in this domain.
	 * 
	 * @return list of action IDs.
	 */
	public ArrayList<I> getIDList();

	/**
	 * Reports the number of States in this domain.
	 * 
	 * @return size of this domain
	 */
	public int size();
}

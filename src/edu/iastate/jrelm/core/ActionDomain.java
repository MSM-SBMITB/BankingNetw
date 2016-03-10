package edu.iastate.jrelm.core;

import java.util.ArrayList;

/**
 * Representation of the space of possible operations an agent can perform in a
 * particular environment.
 * 
 * The type of Action as well as action identifier may be paramertized. These
 * are similar to the Key/Value types that may be parameterized for Hasthtable.
 * 
 * @see java.util.Hashtable
 * 
 * @author Charles Gieseler
 * 
 * @param I
 *            - the type used to identify and organize Actions in this domain
 * @param A
 *            - the type of Action this domain manages
 * 
 * @see edu.iastate.jrelm.core.Action
 */
public interface ActionDomain<I, A extends Action> {

	/**
	 * Determines if the given Action is in this domain.
	 */
	public boolean containsAction(A actionToCheck);

	/**
	 * Retrieves the Action indicated by the id object. Should return null if
	 * the id does not match an existing Action.
	 * 
	 * @param id
	 *            - identifier indicating the desired Action
	 * @return the requested Action associated with id in this domain
	 */
	public A getAction(I id);

	/**
	 * Retrieve a list of the identifiers for all Actions in this domain.
	 * 
	 * @return list of action IDs.
	 */
	public ArrayList<I> getIDList();

	/**
	 * Reports the number of Actions in this domain.
	 * 
	 * @return size of this domain
	 */
	public int size();
}

/*
 * Created on Jun 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.iastate.jrelm.core;

/**
 * For classes representing the operations that an agent can perform in a
 * particual simulation. This may simply indicate an operation or may fully
 * encapsulate data and methods actually used in performing the operation.
 * 
 * It should be noted that JReLM Action is distinct from the RePast BasicAction
 * class and all other BasicAction related classes. A BasicAction is an
 * operation on the simulation level that can be executed by a model's schedule.
 * JReLM Action is intended to be an internal representation of an agent level
 * operation.
 * 
 * This interface was given the name Action because the represented operation is
 * performed by an "actor" in a simulated environment (i.e. the agent).
 * 
 * @author Charles Gieseler
 *
 * @param <I>
 *            - The type this Action uses for identification
 *
 * @see edu.iastate.jrelm.core.ActionDomain
 */
public interface Action<I> {

	/**
	 * Retrieve the identifier for this Action.
	 * 
	 * @return the identifier for this Action
	 */
	public abstract I getID();
}

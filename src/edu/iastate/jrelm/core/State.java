package edu.iastate.jrelm.core;

/**
 * For classes representing a state of the environment that an agent is
 * operating in. Usually this will include only the salient features of the
 * world that an agent has access to and are relevant to learning.
 * 
 * @param <I>
 *            - The type this State uses for identification
 * 
 * @author Charles Gieseler
 * 
 * @see edu.iastate.jrelm.core.StateDomain
 *
 */
public interface State<I> {

	/**
	 * Retrieve the identifier for this State.
	 * 
	 * @return the identifier for this State
	 */
	public I getID();
}

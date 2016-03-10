package edu.iastate.jrelm.core;

/**
 * Simple class that implements the State interface. Acts as a wrapper around a
 * given object containing information about relavant, accessible, and salient
 * features in an agent's environment. In other words, the object is a full or
 * partial representation of a world state and SimpleState acts as a wrapper to
 * make it compatible with other JReLM components.
 *
 * @param <O>
 *            - the type of object being use to represent a course of state of
 *            the world.
 * 
 */
public class SimpleState<O> implements State<Integer> {
	private O worldState;
	Integer myID;

	/**
	 * Make a SimpleState with the given world state and an ID.
	 * 
	 * @param id
	 *            - identifier for this state
	 * @param wolrdInfo
	 *            - object containing information about relavant, accessible,
	 *            and salient features in an agent's environment
	 */
	public SimpleState(Integer id, O worldInfo) {
		worldState = worldInfo;
		myID = id;
	}

	/**
	 * Retrieve the object this is an State wrapper for.
	 * 
	 * @return - state of the world
	 */
	public O getWorldInfo() {
		return worldState;
	}

	/**
	 * Retrieve the ID for this action
	 * 
	 * @return state identifier
	 */
	public Integer getID() {
		return myID;
	}
}
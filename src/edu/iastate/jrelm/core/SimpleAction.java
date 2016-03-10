package edu.iastate.jrelm.core;

/**
 * Simple class that implements the Action interface. It acts as a wrapper
 * around a given object specifying or containing information required for an
 * operation the agent may peform in the simulation environment. In other words,
 * the object represents a course of action in the world and SimpleAction acts
 * as a wrapper to make it compatible with other JReLM components.
 * 
 * @param <O>
 *            - the type of object being use to represent a course of action.
 *            Note, this is different from the Action interface where the type
 *            parameter specificies the type of identifier being used.
 *            SimpleAction implements Action, specifying the identifier type as
 *            Integer.
 * 
 */
public class SimpleAction<O> implements Action<Integer> {
	private O doThis;
	Integer myID;

	/**
	 * Make a SimpleAction, given the action representation Object and an ID.
	 * 
	 * @param id
	 *            - identifier for this action
	 * @param activity
	 *            - object representing an action or operation an agent can
	 *            perform in the simulation model.
	 */
	public SimpleAction(Integer id, O activity) {
		doThis = activity;
		myID = id;
	}

	/**
	 * Retrieve the object this is an Action wrapper for.
	 * 
	 * @return - course of action
	 */
	public O getAct() {
		return doThis;
	}

	/**
	 * Retrieve the ID for this action
	 * 
	 * @return action identifier
	 */
	public Integer getID() {
		return myID;
	}
}
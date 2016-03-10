package edu.iastate.jrelm.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import edu.iastate.jrelm.gui.BasicSettingsEditor;
import edu.iastate.jrelm.rl.RLParameters;
import edu.iastate.jrelm.rl.ReinforcementLearner;
import edu.iastate.jrelm.rl.SimpleStatelessLearner;

/**
 * Manages the collection of ReinforcementLearners in a simulation. Responsible
 * for communicating parameter settings from a settings editor to individual
 * leaners.
 * 
 * 
 * @author Charles Gieseler
 *
 */
public class BasicLearnerManager {

	protected BasicSettingsEditor settings;

	protected Hashtable<String, JReLMAgent> agentTable;

	/*
	 * Agents are grouped according to their learning algorithm, as indicated by
	 * the Class of ReinforcementLearner they use. This Hashtable maintains the
	 * grouping.
	 */
	protected Hashtable<Class, Vector<JReLMAgent>> agentGrouping;

	protected int anonLearnersCount = 0;

	// Registration Events and Listeners
	public enum RegistrationEvent {
		REGISTERATION, UNREGISTERATION, BATCH_REGISTRATION
	}

	protected ArrayList<RegistrationListener> listenerList;

	/* *****************************************************************************
	 * CONSTRUCTORS
	 * *************************************************************
	 * ***************
	 */
	public BasicLearnerManager() {
		init();
	}

	public BasicLearnerManager(ReinforcementLearner[] learnerList) {
		init();

		// Build the registery from the given list of agents
		if ((learnerList != null) && (learnerList.length != 0)) {
			for (ReinforcementLearner learner : learnerList) {
				if (learner == null) {
					System.err
							.println("Warning: BasicLearnerManager recieved a null "
									+ "learner in the given list.");
				} else {
					register(learner);
				}
			}
		}
	}

	public BasicLearnerManager(ArrayList<JReLMAgent> agentList) {
		init();

		// Build the registery from the given list of agents
		if ((agentList != null) && (!agentList.isEmpty())) {
			for (JReLMAgent agent : agentList) {
				register(agent);
			}
		}
	}

	/* *****************************************************************************
	 * INITIALIZATION
	 * ***********************************************************
	 * *****************
	 */
	protected void init() {
		agentTable = new Hashtable<String, JReLMAgent>();
		agentGrouping = new Hashtable<Class, Vector<JReLMAgent>>();
		listenerList = new ArrayList<RegistrationListener>();
	}

	/* *****************************************************************************
	 * REGISTRATION METHODS
	 * *****************************************************
	 * ***********************
	 */

	/**
	 * Check to see if a given ReinforcementLearner is already regisitered with
	 * this manager. If it is, then this will return the String id that it is
	 * currently regisitered under. Otherwise, it will return null.
	 */
	public String isRegistered(ReinforcementLearner learner) {
		// Check to see if the learner is already embedded in a
		// registered agent (if there are any).

		if (agentTable.isEmpty()) {
			return null;
		}

		for (JReLMAgent agent : agentTable.values()) {
			if (learner.equals(agent.getLearner())) {
				return agent.getID();
			}
		}

		return null;
	}

	public String isRegistered(JReLMAgent agent) {
		String id = agent.getID();
		// First check that the agent is registered
		if (agentTable.containsValue(agent)) {
			// Next check that it is registered under it's own ID
			if (agent.equals(agentTable.get(id)))
				return id;
			else {
				// If not, then find which id it is registered under
				for (String tempID : agentTable.keySet()) {
					if (agent.equals(agentTable.get(tempID)))
						return tempID;
				}
			}
		}
		return null;
	}

	/**
	 * Register a bare ReinforcementLearner without a given ID. This learner to
	 * be registered under a generated generic ID. The ID will be returned upon
	 * successul registration.
	 * 
	 * @param learner
	 *            - the bare learner to register
	 * @return the auto-generated ID for this leaner. If the learner is already
	 *         registered, returns the existing ID assigined to this anonymous
	 *         learner. Returns null if the learner is not registered
	 *         successfully.
	 */
	public String register(ReinforcementLearner learner) {
		String existingID = isRegistered(learner);
		if (existingID != null)
			return existingID;

		String anonymousID = "Anonymous Learner " + anonLearnersCount;
		anonLearnersCount++;

		if (register(anonymousID, learner)) {
			String[] ids = { anonymousID };
			notifyListeners(ids, RegistrationEvent.REGISTERATION);
			return anonymousID;
		} else
			return null;
	}

	/**
	 * Register a ReinforcementLearner under a given id
	 * 
	 * @param id
	 *            - the String indentifier that the learner will be registered
	 *            under.
	 * 
	 * @param learner
	 * @return true if the learner was successfully registered, false if a the
	 *         learner has already been registered or another learner is
	 *         registered under the given id.
	 */
	public boolean register(String id, ReinforcementLearner learner) {

		if (isRegistered(learner) != null)
			return false;

		// Wrap the learner in a dummy agent so that it may be added to the
		// registery
		edu.iastate.jrelm.util.WrapperAgent wrapAgent = new edu.iastate.jrelm.util.WrapperAgent(
				id, learner);
		return register(wrapAgent);
	}

	/**
	 * Register a single JReLMAgent. The agent will be registered under it's
	 * given ID (JReLMAgent.getID()).
	 * 
	 * @param agent
	 *            - the agent to register
	 * @return true if the agent was successfully registered, false if a the
	 *         agent has already been registered or another agent is already
	 *         registered under the same ID.
	 */
	public boolean register(JReLMAgent agent) {
		if (isRegistered(agent) != null)
			return false;

		agentTable.put(agent.getID(), agent);
		groupAgent(agent);

		String[] ids = { agent.getID() };
		notifyListeners(ids, RegistrationEvent.REGISTERATION);
		return true;
	}

	/**
	 * Register a batch of JReLMAgents given as a Collection. Each agent will be
	 * registered under it's given ID (JReLMAgent.getID()).
	 * 
	 * @param agents
	 * @return true if all agents were successfully registered, false if one or
	 *         more agents were already registered or another agent is already
	 *         registered under the same ID.
	 */
	public boolean register(Collection<JReLMAgent> agents) {
		boolean success = true;
		ArrayList<String> ids = new ArrayList<String>();

		for (JReLMAgent<ReinforcementLearner> agent : agents) {
			// if an agent is registered successfully then add it's id to the
			// list of
			// newly registered agents. Otherewise, mark failure and move on to
			// the
			// next agent
			if (register(agent)) {
				ids.add(agent.getID());
			} else {
				success = false;
			}
		}

		notifyListeners(ids.toArray(new String[0]),
				RegistrationEvent.BATCH_REGISTRATION);

		return success;
	}

	/**
	 * Attempts to unregister the given ReinforcementLearner.
	 * 
	 * @param learner
	 *            - remove this ReinforcementLearner from the registry
	 * @return true if the learner was successfully unregistered. False if the
	 *         learner was not found in the registery.
	 */
	public boolean unregister(ReinforcementLearner learner) {

		// Look for an agent with this learner embedded in it
		for (JReLMAgent agent : agentTable.values()) {
			if (learner.equals(agent.getLearner())) {
				if (agentTable.remove(agent) != null) {
					ungroupAgent(agent);
					return true;
				}
			}
		}

		return false;
	}

	public boolean unregister(JReLMAgent agent) {

		if (agentTable.remove(agent) != null)
			return true;

		ungroupAgent(agent);

		return false;
	}

	public boolean unregister(String id) {
		JReLMAgent agent = agentTable.get(id);

		if (agent == null)
			return false;
		else
			return unregister(agent);

	}

	/* *****************************************************************************
	 * AGENT GROUPING
	 * ***********************************************************
	 * *****************
	 */

	/**
	 * Agents are grouped according to their learning algorithm as indicated by
	 * the type of RLParameters their learner uses. This method places the given
	 * agent in the appropriate algorithm group.
	 */
	protected void groupAgent(JReLMAgent agent) {
		Class learnerClass;

		// If the agent has a SimpleStatelessLearner, dig one more level to get
		// the
		// true learner claas
		if (SimpleStatelessLearner.class.isInstance(agent.getLearner())) {
			learnerClass = ((SimpleStatelessLearner) agent.getLearner())
					.getEngine().getClass();
		} else
			learnerClass = agent.getLearner().getClass();

		Vector<JReLMAgent> group;

		// If a group for this algorithm exists, then add the agent to the
		// group,
		// if it is not already in the group. Otherwise create a new group with
		// the agent in it. Add the group to the Grouping under the algorithm
		// designated by agentParamClass
		if (agentGrouping.containsKey(learnerClass)) {
			group = agentGrouping.get(learnerClass);
			if (!group.contains(agent))
				group.add(agent);
		} else {
			group = new Vector<JReLMAgent>();
			group.add(agent);
			agentGrouping.put(learnerClass, group);
		}
	}

	/**
	 * Agents are grouped according to their learning algorithm as indicated by
	 * the type of RLParameters their learner uses. This method removes the
	 * given agent from the algorithm group it is currently associated with.
	 */
	protected void ungroupAgent(JReLMAgent agent) {
		Class learnerClass;

		// If the agent has a SimpleStatelessLearner, dig one more level to get
		// the
		// true learner claas
		if (SimpleStatelessLearner.class.isInstance(agent.getLearner())) {
			learnerClass = ((SimpleStatelessLearner) agent.getLearner())
					.getEngine().getClass();
		} else
			learnerClass = agent.getLearner().getClass();

		Vector<JReLMAgent> group;
		// If a group exists for this algorithm and the agent is in the group,
		// then remove it. If the group is then empty, remove it from the
		// Grouping
		if (agentGrouping.containsKey(learnerClass)) {
			group = agentGrouping.get(learnerClass);
			if (group.contains(agent))
				group.remove(agent);

			if (group.isEmpty())
				agentGrouping.remove(learnerClass);
		}
	}

	/* *****************************************************************************
	 * SETTINGS UPDATE METHODS
	 * **************************************************
	 * **************************
	 */
	/**
	 * Update the learning parameter settings for the agent specified by 'id'.
	 * 
	 * @param id
	 *            - the String identifier for the desired agent
	 * @return true if the settings were accepted by the agent's learner. False
	 *         if the RLParameters given were incompatible with the agent's
	 *         learner.
	 */
	public boolean updateSettings(String id, RLParameters params) {

		JReLMAgent agent = agentTable.get(id);

		if (agent == null) {
			System.err
					.println("Tried to set parameters for agent '"
							+ id
							+ "', but no such agent is registered with this BasicLearnerManager.");
			return false;
		}

		try {
			agent.getLearner().setParameters(params);
		} catch (ClassCastException cce) {
			Class learnerClass = agent.getLearner().getClass();
			System.err
					.println("JReLM BasicLearnerManager update error: Attempted to set "
							+ "incompatible parameters for ID '" + id + "'.");
			System.err.println(params.getClass().getName()
					+ " not compatible with " + learnerClass);
			System.err.println("Using previous settings.");
		}

		return true;
	}

	/**
	 * Update the settings for all agents in the algortithm group indicated by
	 * the given Class of ReinforcementLearner. The given RLParameters must be
	 * compatible the indicated ReinforcementLearner type.
	 * 
	 * @param learnerType
	 *            - Class of ReinforcementLearner indicating which group of
	 *            agents should receive these settings
	 * @param params
	 *            - the new learning parameter settings to use for all agents in
	 *            the group
	 * @return true if the given parameters have been accepted by all agents in
	 *         the group. False if the parameter settings were rejected by any
	 *         agent in the group.
	 */
	public boolean updateSettings(Class learnerType, RLParameters params) {
		boolean settingsAccepted = true;
		Vector<JReLMAgent> group = agentGrouping.get(learnerType);

		if (group != null) {
			for (JReLMAgent agent : group) {
				try {
					agent.getLearner().setParameters(params);
				} catch (ClassCastException cce) {
					Class learnerClass = agent.getLearner().getClass();
					System.err
							.println("JReLM BasicLearnerManager update error: Attempted to set "
									+ "incompatible parameters for ID '"
									+ agent.getID() + "'.");
					System.err.println(params.getClass().getName()
							+ " not compatible with " + learnerClass);
					System.err.println("Using previous settings.");
					settingsAccepted = false;
				}
			}
		}

		return settingsAccepted;
	}

	/* *****************************************************************************
	 * ACCESSOR METHODS
	 * *********************************************************
	 * *******************
	 */

	/**
	 * Retrieve an agent with the given ID. If no such agent is registered, then
	 * null is returned.
	 * 
	 * @param agentID
	 *            - the ID of the desired agent
	 * @return an agent with the given ID or null if no such agent has been
	 *         registered.
	 */
	public JReLMAgent getAgent(String agentID) {
		return agentTable.get(agentID);
	}

	/**
	 * Get a list of the String identifiers for all registered agents.
	 * 
	 * @return an ArrayList containing the String IDs of all registered agents.
	 */
	public ArrayList<String> getAgentIDList() {
		Enumeration<String> idEnum = agentTable.keys();
		ArrayList<String> idList = new ArrayList<String>();

		String id;
		while (idEnum.hasMoreElements()) {
			id = idEnum.nextElement();
			idList.add(id);
		}

		return idList;
	}

	/**
	 * Retrieve the whole agent registery. Registry is returned as a Hashtable
	 * associating String IDs with JReLMAgents.
	 * 
	 * @return the Hashtable JReLMAgent registry
	 */
	public Hashtable<String, JReLMAgent> getAgentRegistery() {
		return agentTable;
	}

	/**
	 * Retrieves a group of agents that all use the same reinforcement learning
	 * algorithm as indicated by the given class of ReinforcementLearner.
	 * 
	 * @param learnerClass
	 * @return
	 */
	public Vector<JReLMAgent> getGroup(Class learnerClass) {
		return agentGrouping.get(learnerClass);
	}

	/**
	 * Retrieves a grouping of agents classified according to the class of their
	 * learners. The grouping is returned as a Hashtable keyed on Class objects
	 * representing the class of learners. Values in the Hashtable are groups of
	 * JReLMAgents stored as Vectors.
	 * 
	 * @return a collection of groups of agents.
	 */
	public Hashtable<Class, Vector<JReLMAgent>> getAgentGrouping() {
		return agentGrouping;
	}

	/* *****************************************************************************
	 * FOR SCREEN OUTPUT
	 * ********************************************************
	 * ********************
	 */
	public void printAgentGroups() {

		for (Class paramClass : agentGrouping.keySet()) {
			System.out.println(paramClass.getName());
			Vector<JReLMAgent> group = agentGrouping.get(paramClass);
			for (JReLMAgent agent : group)
				System.out.println("   " + agent.getID());
			System.out.println();
		}
	}

	/* *****************************************************************************
	 * FOR REGISTRATION EVENTS AND LISTENERS
	 * ************************************
	 * ****************************************
	 */
	public void addRegistrationListener(RegistrationListener listener) {
		listenerList.add(listener);
	}

	protected void notifyListeners(String[] ids, RegistrationEvent event) {
		// System.out.println("BasicLearnerManager.notifyListeners(): ids received:");
		// for(String id: ids)
		// System.out.println("	"+id);

		for (RegistrationListener listener : listenerList) {
			listener.registrationChange(ids, event);
		}
	}

	public interface RegistrationListener {
		public void registrationChange(String[] ids, RegistrationEvent event);
	}

}

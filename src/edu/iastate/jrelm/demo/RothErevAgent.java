/*
 * Created on Jan 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.iastate.jrelm.demo;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.core.FeedbackDoubleValue;
import edu.iastate.jrelm.core.JReLMAgent;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;
import edu.iastate.jrelm.rl.rotherev.RELearner;

/**
 * This agent is meant to demonstrate the use of the core components. It is a
 * simple agent with behavior driven by a modified version of the Roth-Erev
 * reinforcement learning algorithm. It must at least be supplied with a
 * learning domain and parameters for the learning algortithm.
 * <p>
 * Use nextAction() to elicit a choice of action from the agent in the form of
 * an Action object selected from the given action domain. Use activate(double)
 * to give the agent feedback in the for of a double. This will trigger the
 * learning process.
 * <p>
 * The type of Action and ActionDomain may be specified through parameters. This
 * agent has been set to use RothErevActionID internally as it is using a
 * ARELearner.
 * 
 * @author Charles Gieseler
 *
 */
public class RothErevAgent<A extends Action<Integer>> implements
		JReLMAgent<RELearner> {

	ActionDomain<Integer, A> domain;
	// REPolicy<Integer,A> policy;
	REParameters learningParams;
	RELearner<Integer, A> learner;

	String myID = "Anonymous";

	/**
	 * Build agent driven by Roth-Erev Reinforcement Learning.
	 * 
	 * @param dom
	 *            - A DiscreteFiniteAction
	 * @param agentID
	 *            - identifier for this agent ("Anonymous" by default)
	 * @param parameters
	 *            - parameters for Roth-Erev learning
	 */
	public RothErevAgent(REParameters params, ActionDomain<Integer, A> dom,
			String agentID) {
		domain = dom;
		learningParams = params;
		// policy = new REPolicy<Integer,A>(domain);
		learner = new RELearner<Integer, A>(learningParams, dom);
		myID = agentID;
	}

	/**
	 * Build agent driven by Roth-Erev Reinforcement Learning. This agent will
	 * have an "Anonymous" id.
	 * 
	 * @param dom
	 *            - A DiscreteFiniteAction
	 * @param parameters
	 *            - parameters for Roth-Erev learning
	 */
	public RothErevAgent(REParameters params, ActionDomain<Integer, A> dom) {
		this(params, dom, "Anonymous");
	}

	/**
	 * Use this to give the agent input from the environment. Input should be
	 * the resulting "reward" an agent received from it's last action. This
	 * simple agent can only receive feedback in the form of a double.
	 * 
	 * @param feedback
	 *            - double representing the result of the agent's last action
	 */
	public void receiveFeedback(double feedback) {
		learner.update(new FeedbackDoubleValue(feedback));
	}

	/**
	 * Elicits a choice of action from the agent. The action will be chosen from
	 * the given learning domain according to selection rule of the
	 * SimpleStatelessPolicy.
	 * 
	 * @see edu.iastate.jrelm.rl.SimpleStatelessPolicy#generateAction()
	 * 
	 * @return the next action to perform
	 */
	public A chooseAction() {
		return learner.chooseAction();
	}

	/**
	 * @see JReLMAgent#getID()
	 */
	public String getID() {
		return myID;
	}

	/**
	 * @see JReLMAgent#getLearner()
	 */
	public RELearner getLearner() {
		return learner;
	}
}

package edu.iastate.jrelm.test;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.State;
import edu.iastate.jrelm.core.StateDomain;
import edu.iastate.jrelm.rl.Policy;
import edu.iastate.jrelm.rl.StatelessPolicy;

public class FakeQLPolicy<AI, A extends Action, SI, S extends State> implements
		Policy<AI, A, SI, S> {

	public FakeQLPolicy() {

	}

	public A generateAction() {
		// TODO Auto-generated method stub
		return null;
	}

	public ActionDomain<AI, A> getActionDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	public A getLastAction() {
		// TODO Auto-generated method stub
		return null;
	}

	public A generateAction(SI stateID) {
		// TODO Auto-generated method stub
		return null;
	}

	public StateDomain<SI, S> getStateDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getProbability(SI stateID, AI actionID) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setProbability(SI stateID, AI actionID, double newValue) {
		// TODO Auto-generated method stub

	}

	public void setRandomSeed(int seed) {
		// TODO Auto-generated method stub

	}
}

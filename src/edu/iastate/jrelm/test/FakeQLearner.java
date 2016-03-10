package edu.iastate.jrelm.test;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.core.FeedbackDoubleValue;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.rl.RLParameters;
import edu.iastate.jrelm.rl.StatelessPolicy;
import edu.iastate.jrelm.rl.ReinforcementLearner;
import edu.iastate.jrelm.rl.SimpleStatelessPolicy;

public class FakeQLearner<I, A extends Action>
		implements
		ReinforcementLearner<FakeQLearningParameters, I, A, FeedbackDoubleValue, StatelessPolicy<I, A>> {
	FakeQLearningParameters parameters;
	ActionDomain<I, A> domain;

	public FakeQLearner(FakeQLearningParameters params, ActionDomain<I, A> dom) {
		parameters = new FakeQLearningParameters("blah");
		domain = dom;
	}

	public void update(FeedbackDoubleValue feedback) {
		// TODO Auto-generated method stub

	}

	public A chooseAction() {
		// TODO Auto-generated method stub
		return null;
	}

	public FakeQLearningParameters getParameters() {
		return parameters;
	}

	public FakeQLearningParameters makeParameters() {
		return new FakeQLearningParameters();
	}

	public void setParameters(FakeQLearningParameters params) {
		parameters = (FakeQLearningParameters) params;
	}

	public StatelessPolicy<I, A> getPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPolicy(StatelessPolicy<I, A> p) {
		// TODO Auto-generated method stub

	}

	public String getName() {
		return "Fake Q-Learning";
	}
}

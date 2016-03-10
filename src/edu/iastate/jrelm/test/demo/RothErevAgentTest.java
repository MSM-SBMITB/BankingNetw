package edu.iastate.jrelm.test.demo;

import java.util.ArrayList;

import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.demo.RothErevAgent;
import edu.iastate.jrelm.rl.rotherev.RELearner;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;
import edu.iastate.jrelm.util.SimpleEventGenerator;
import junit.framework.TestCase;

public class RothErevAgentTest extends TestCase {

	SimpleActionDomain<Integer> domain;
	REParameters params;
	RothErevAgent<SimpleAction<Integer>> agent;

	RELearner<Integer, SimpleAction<Integer>> benchmark_learner;

	// Rewards for corresponding actions
	double[] rewards = { 200, 500, 300, 800, 200 };

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(RothErevAgentTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		ArrayList<Integer> actionList = new ArrayList<Integer>();
		actionList.add(0);
		actionList.add(1);
		actionList.add(2);
		actionList.add(3);
		actionList.add(4);
		domain = new SimpleActionDomain<Integer>(actionList);

		params = new REParameters(1, 0.75, 500, 0.3333, 112358);

		agent = new RothErevAgent<SimpleAction<Integer>>(params, domain);

		benchmark_learner = new RELearner<Integer, SimpleAction<Integer>>(
				params, domain);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.demo.RothErevAgent.receiveFeedback(double)'
	 */
	public void testReceiveFeedback() {

	}

	/*
	 * Test method for 'edu.iastate.jrelm.demo.RothErevAgent.act()'
	 */
	public void testAct() {
		System.out.println("RothErevAgentTest.testAct()");
		System.out.print("	Initial: ");
		RELearner learner = agent.getLearner();
		REPolicy policy = learner.getPolicy();
		double[] probs = policy.getProbabilities();
		for (int j = 0; j < probs.length; j++)
			System.out.print(probs[j] + ", ");
		System.out.println();

		SimpleAction<Integer> nextAction;
		int index;

		SimpleAction<Integer> benchmark_nextAction;
		int benchmark_index;

		for (int i = 0; i < 100; i++) {
			nextAction = agent.chooseAction();
			index = nextAction.getAct();
			agent.receiveFeedback(rewards[index]);

			benchmark_nextAction = benchmark_learner.chooseAction();
			benchmark_index = benchmark_nextAction.getAct();
			benchmark_learner.update(rewards[benchmark_index]);

			assertEquals(index, benchmark_index);
			assertEquals(agent.getLearner().getUpdateCount(), i + 1);

			probs = agent.getLearner().getPolicy().getProbabilities();
			System.out.print("	" + index + "	");
			for (int j = 0; j < probs.length; j++)
				System.out.print(probs[j] + ", ");
			System.out.println();
		}
	}
}

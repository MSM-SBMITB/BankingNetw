package edu.iastate.jrelm.test;

import java.util.ArrayList;

import edu.iastate.jrelm.rl.SimpleStatelessLearner;
import edu.iastate.jrelm.rl.rotherev.variant.VREParameters;

import junit.framework.TestCase;

import static java.lang.System.out;

public class TestSimpleLearner_MultiAgent extends TestCase {
	ArrayList actionList;
	int numActions;
	int numLearners = 40;
	int numTrials = 3;

	ArrayList<SimpleStatelessLearner> learners;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestSimpleLearner_MultiAgent.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		actionList = new ArrayList();
		learners = new ArrayList<SimpleStatelessLearner>();

		double[] pair;
		for (double j = 0; j <= 100; j = j + 10) {
			for (double k = 5; k < 100; k = k + 5) {
				pair = new double[2];
				pair[0] = j / 100;
				pair[1] = k / 100;
				actionList.add(pair);
			}
		}
		numActions = actionList.size();

		VREParameters params = new VREParameters(7891509, 0.7, 1000, 0.43);

		for (int j = 0; j < numLearners; j++) {
			learners.add(new SimpleStatelessLearner(params, actionList));
			out.println(((VREParameters) learners.get(j).getParameters())
					.getRandomSeed());
		}
	}

	public void testMultiAgentActionChoice() {
		out.println("TestSimpleLearner_MultiAgent.testMultiAgentActionChoice():");
		SimpleStatelessLearner learner;
		for (int i = 0; i < numTrials; i++) {
			out.println("Trial " + i);
			for (int j = 0; j < learners.size(); j++) {
				learner = learners.get(j);
				out.println("	agent " + j + "  action: "
						+ learner.chooseActionIndex());
			}
			out.println();
		}
	}

}

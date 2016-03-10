package edu.iastate.jrelm.test;

import static java.lang.System.out;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import edu.iastate.jrelm.rl.SimpleStatelessLearner;
import edu.iastate.jrelm.rl.rotherev.variant.VREParameters;
import junit.framework.TestCase;

public class TestSimpleLearner_UniformChooseAction extends TestCase {
	ArrayList actionList;
	SimpleStatelessLearner learner;
	int numActions;
	int numTrials = 1000000;

	public static void main(String[] args) {
		junit.textui.TestRunner
				.run(TestSimpleLearner_UniformChooseAction.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		double[] pair;

		actionList = new ArrayList();
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
		learner = new SimpleStatelessLearner(params, actionList);
	}

	public void testChooseActionDistribution() {
		out.println("TestModifiedEmpiricalWalker_UniformChooseAction.testChooseActionDistribution():");

		int action;
		double[] counts = new double[numActions];
		double[] frequencies = new double[numActions];

		for (int i = 0; i < numTrials; i++) {
			action = learner.chooseActionIndex();
			counts[action]++;
		}

		for (int j = 0; j < numActions; j++)
			frequencies[j] = counts[j] / ((double) numTrials);

		out.println("	Ran " + numTrials + " trials");
		for (int k = 0; k < numActions; k++)
			out.println("	aciton: " + k + "  probability: "
					+ learner.getPolicy().getProbability(k) + "  frequency: "
					+ frequencies[k]);
	}

}

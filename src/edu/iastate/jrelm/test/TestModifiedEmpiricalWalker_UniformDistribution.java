package edu.iastate.jrelm.test;

import cern.jet.random.EmpiricalWalker;
import cern.jet.random.engine.MersenneTwister;
import edu.iastate.jrelm.util.ModifiedEmpiricalWalker;
import junit.framework.TestCase;

import static java.lang.System.out;

public class TestModifiedEmpiricalWalker_UniformDistribution extends TestCase {
	EmpiricalWalker walker;
	int numActions = 209;
	int numTrials = 1000000;

	public static void main(String[] args) {
		junit.textui.TestRunner
				.run(TestModifiedEmpiricalWalker_UniformDistribution.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		double[] pdf = new double[numActions];

		for (int i = 0; i < numActions; i++)
			pdf[i] = 1.0 / numActions;

		MersenneTwister twister = new MersenneTwister(11235);

		walker = new ModifiedEmpiricalWalker(pdf,
				ModifiedEmpiricalWalker.NO_INTERPOLATION, twister);
	}

	public void testNextInt() {
		out.println("TestModifiedEmpiricalWalker_UniformDistribution.testNextInt():");

		int action;
		double[] counts = new double[numActions];
		double[] frequencies = new double[numActions];

		for (int i = 0; i < numTrials; i++) {
			action = walker.nextInt();
			counts[action]++;
		}

		for (int j = 0; j < numActions; j++)
			frequencies[j] = counts[j] / ((double) numTrials);
		out.println("	Ran " + numTrials + " trials");
		for (int k = 0; k < numActions; k++)
			out.println("	aciton: " + k + "  probability: " + walker.pdf(k)
					+ "  frequency: " + frequencies[k]);

	}

}

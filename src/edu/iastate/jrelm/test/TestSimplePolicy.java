package edu.iastate.jrelm.test;

import java.util.ArrayList;

import cern.jet.random.engine.MersenneTwister;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.rl.SimpleStatelessPolicy;
import junit.framework.TestCase;

public class TestSimplePolicy extends TestCase {

	private SimpleStatelessPolicy<Integer, SimpleAction<Integer>> policy;
	private SimpleActionDomain<Integer> domain;
	private double[] distrib;
	private MersenneTwister randomGen;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestSimplePolicy.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		randomGen = new MersenneTwister();

		ArrayList<Integer> actionList = new ArrayList<Integer>();
		int[] actions = new int[20];
		distrib = new double[20];

		for (int j = 0; j < 20; j++) {
			actionList.add(new Integer(j));
			distrib[j] = 40.0 / 1500.0;
		}
		distrib[2] = 60.0 / 500.0;
		distrib[3] = 60.0 / 500.0;
		distrib[5] = 60.0 / 500.0;
		distrib[8] = 60.0 / 500.0;
		distrib[13] = 60.0 / 500.0;

		domain = new SimpleActionDomain<Integer>(actionList);
		policy = new SimpleStatelessPolicy<Integer, SimpleAction<Integer>>(
				domain, distrib);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		policy = null;
		domain = null;
	}

	public TestSimplePolicy(String arg0) {
		super(arg0);
	}

	public void testSimplePolicydomain() {
		policy = null;
		policy = new SimpleStatelessPolicy<Integer, SimpleAction<Integer>>(
				domain);
		assertNotNull(policy);
	}

	public void testSimplePolicydomainRandomEngine() {
		policy = null;
		policy = new SimpleStatelessPolicy<Integer, SimpleAction<Integer>>(
				domain, randomGen);
		assertNotNull(policy);

	}

	public void testSimplePolicydomainDistrib() {
		policy = null;
		policy = new SimpleStatelessPolicy<Integer, SimpleAction<Integer>>(
				domain, distrib);
		assertNotNull(policy);

		// Test illegal length distribution
		double[] newDistrib = { 0.3, 0.8, 0.2, 0.6 };
		policy = null;
		try {
			policy = new SimpleStatelessPolicy<Integer, SimpleAction<Integer>>(
					domain, newDistrib);
		} catch (IllegalArgumentException iae) {
			assertNotNull(iae);
		}
	}

	public void testSimplePolicydomainDistribRandomEngine() {
		policy = null;
		policy = new SimpleStatelessPolicy<Integer, SimpleAction<Integer>>(
				domain, distrib, randomGen);
		assertNotNull(policy);

		// Test illegal length distribution
		double[] newDistrib = { 0.3, 0.8, 0.2, 0.6 };
		policy = null;
		try {
			policy = new SimpleStatelessPolicy<Integer, SimpleAction<Integer>>(
					domain, newDistrib);
		} catch (IllegalArgumentException iae) {
			assertNotNull(iae);
		}
	}

	public void testGenerateAction() {
		int numCalls = 100;
		SimpleAction<Integer> action;
		action = policy.generateAction();
		assertNotNull(action);

		System.out.println("Calls with non-uniform distribution");
		for (int i = 0; i < numCalls; i++) {
			action = policy.generateAction();
			System.out.println("	 " + action.getID().intValue() + ": "
					+ policy.getProbability(action.getID()));
		}

		System.out.println("");
		policy = new SimpleStatelessPolicy<Integer, SimpleAction<Integer>>(
				domain);
		System.out.println("TestSimplePolicy: testGenerateAction:");
		System.out.println("Calls with uniform distribution");
		for (int i = 0; i < numCalls; i++) {
			action = policy.generateAction();
			System.out.println("	 " + action.getID().intValue() + ": "
					+ policy.getProbability(action.getID()));
		}
	}

	public void testGetDomain() {
		assertNotNull(policy.getActionDomain());
		assertSame(domain, policy.getActionDomain());
	}

	public void testGetNumActions() {
		int size = policy.getNumActions();
		assert (size == domain.size());
	}

	public void testGetLastAction() {
		SimpleAction action = policy.generateAction();
		assertNotNull(policy.getLastAction());
		assert (policy.getLastAction().equals(action));
	}

	/*
	 * Class under test for double getProbability(int)
	 */
	public void testGetProbabilityint() {
		int numActs = policy.getActionDomain().size();
		double prob;

		for (int i = 0; i < numActs; i++) {
			prob = policy.getProbability(i);
			assertNotNull(prob);
			assert ((prob >= 0) && (prob < 1));
		}

		prob = policy.getProbability(numActs + 1);
		assert (prob == Double.NaN);
	}

	/*
	 * Class under test for double getProbability(I)
	 */
	public void testGetProbabilityI() {
		int numActs = policy.getActionDomain().size();
		double prob1;
		double prob2;

		for (int i = 0; i < numActs; i++) {
			prob1 = policy.getProbability(i);
			prob2 = policy.getProbability(i);

			assertNotNull(prob1);
			assertNotNull(prob2);
			assertEquals(prob1, prob2);
			assert ((prob1 >= 0) && (prob1 < 1));
		}

		prob1 = policy.getProbability(numActs + 1);
		assertEquals(prob1, Double.NaN);
	}

	public void testSetProbability() {
		double prob;

		for (int i = 0; i < policy.getNumActions(); i++) {
			prob = distrib[i];
			policy.setProbability(i, prob);
			assertEquals(prob, policy.getProbability(i));
		}

		// Test invalid indexes
		try {
			policy.setProbability(-1, 0.5);
		} catch (IllegalArgumentException iae) {
			assertNotNull(iae);
		}

		try {
			policy.setProbability(domain.size() + 2, 0.5);
		} catch (IllegalArgumentException iae) {
			assertNotNull(iae);
		}
	}

	public void testGetDistribution() {
		double[] currentDistrib = policy.getDistribution();
		assertNotNull(currentDistrib);
		assertEquals(currentDistrib.length, policy.getNumActions());

		for (int i = 0; i < policy.getNumActions(); i++)
			assertEquals(distrib[i], currentDistrib[i]);

	}

	public void testSetDistribution() {
		// generic test with correctly sized distribution
		policy.setDistribution(distrib);
		assertEquals(distrib, policy.getDistribution());

		// Test illegal length distribution
		double[] newDistrib = { 0.3, 0.8, 0.2, 0.6 };
		double[] oldDistrib = policy.getDistribution();
		double[] distribInUse;

		try {
			policy.setDistribution(newDistrib);
		} catch (IllegalArgumentException iae) {
			distribInUse = policy.getDistribution();
			assertNotNull(policy);
			assertNotNull(distribInUse);
			assert (distribInUse.length == domain.size());

			// Check that both the distribution in use and getProbability have
			// the same values
			// as the previously set distribution. That is the distribution has
			// not changed since
			// the argument was invalid
			int numActions = domain.size();
			for (int i = 0; i < numActions; i++) {
				assertEquals(distribInUse[i], oldDistrib[i]);
				assertEquals(policy.getProbability(i), oldDistrib[i]);
			}
		}
	}

}

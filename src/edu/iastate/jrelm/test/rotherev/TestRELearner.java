package edu.iastate.jrelm.test.rotherev;

import java.util.ArrayList;

import cern.jet.random.engine.MersenneTwister;

import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.rl.StatelessPolicy;
import edu.iastate.jrelm.rl.SimplePolicy;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;
import edu.iastate.jrelm.rl.rotherev.RELearner;

import junit.framework.TestCase;

/**
 * The following tests are for the verification of the RELearner. That is, these
 * tests check that the RELearner is properly operating as learning component.
 * 
 * These tests do not perform verification that RELearner correctly implements
 * the Roth-Erev learning algorithm.
 * 
 * @see edu.iastate.jrelm.test.rotherev.RELearnerValidationTest
 *
 * @author Charles Gieseler
 *
 */

public class TestRELearner extends TestCase {

	int DOMAIN_SIZE = 4;
	SimpleActionDomain<String> domain;
	REParameters params;
	REPolicy<Integer, SimpleAction<String>> policy;
	RELearner<Integer, SimpleAction<String>> learner;
	MersenneTwister randomGenerator;

	double[] rewards = { 300, 400, 200, 800, 500 };

	public TestRELearner(String arg0) {
		super(arg0);
		randomGenerator = new MersenneTwister(112358);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestRELearner.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		ArrayList<String> actionList = new ArrayList<String>();

		for (double j = 0; j < DOMAIN_SIZE; j++) {
			actionList.add("Action_" + j);
		}
		// System.out.println("TestRELearner: setup(): actionList");
		// for(String act: actionList){
		// System.out.println(act);
		// }
		domain = new SimpleActionDomain<String>(actionList);
		params = new REParameters(50, 0.6332, 500, 0.3333, 112358);
		policy = new REPolicy<Integer, SimpleAction<String>>(domain,
				randomGenerator);

		learner = new RELearner<Integer, SimpleAction<String>>(params, policy);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Class under test for void VRELearner(L, D)
	 */
	public void testRothErevLearnerLD() {
		learner = new RELearner<Integer, SimpleAction<String>>(params, domain);
		assertNotNull(learner);
	}

	/*
	 * Class under test for void VRELearner(L, P)
	 */
	public void testRothErevLearnerLP() {
		learner = new RELearner<Integer, SimpleAction<String>>(params, policy);
		assertNotNull(learner);
	}

	/**
	 * This test checks to see that the propensities and probabilities of a
	 * selected action actually INCREASE with a POSITIVE reward and are indeed
	 * GREATER THAN the propensities and probabilities of all unrewarded
	 * actions.
	 */
	public void testUpdatePositiveReward() {
		SimpleAction<String> selectedAction = learner.chooseAction();

		double oldProp = learner.getPolicy().getPropensity(
				selectedAction.getID());
		double oldProb = learner.getPolicy().getProbability(
				selectedAction.getID());

		learner.update(500.0);

		double newProp = learner.getPolicy().getPropensity(
				selectedAction.getID());
		double newProb = learner.getPolicy().getProbability(
				selectedAction.getID());

		// Confirm that the probability and propensity of the rewarded
		// action have increased
		assertTrue(newProp > oldProp);
		assertTrue(newProb > oldProb);

		// Now confirm that the propensity and probabilities for the rewarded
		// action are greater than those of all other actions
		SimpleActionDomain<String> domain = (SimpleActionDomain<String>) learner
				.getPolicy().getActionDomain();
		for (Integer otherID : domain.getIDList()) {
			if (otherID == selectedAction.getID())
				continue;

			double otherProp = learner.getPolicy().getPropensity(otherID);
			double otherProb = learner.getPolicy().getProbability(otherID);
			assertTrue(newProp > otherProp);
			assertTrue(newProb > otherProb);
		}
	}

	/**
	 * This test checks to see that the propensities and probabilities of a
	 * selected action actually DECREASE with a NEGATIVE reward and are indeed
	 * LESS THAN the propensities and probabilities of all unrewarded actions.
	 */
	public void testUpdateNegativeReward() {
		SimpleAction<String> selectedAction = learner.chooseAction();

		double oldProp = learner.getPolicy().getPropensity(
				selectedAction.getID());
		double oldProb = learner.getPolicy().getProbability(
				selectedAction.getID());

		learner.update(-500.0);

		double newProp = learner.getPolicy().getPropensity(
				selectedAction.getID());
		double newProb = learner.getPolicy().getProbability(
				selectedAction.getID());

		// Confirm that the probability and propensity of the rewarded
		// action have increased
		assertTrue(newProp < oldProp);
		assertTrue(newProb < oldProb);

		// Now confirm that the propensity and probabilities for the rewarded
		// action are greater than those of all other actions
		SimpleActionDomain<String> domain = (SimpleActionDomain<String>) learner
				.getPolicy().getActionDomain();
		for (Integer otherID : domain.getIDList()) {
			if (otherID == selectedAction.getID())
				continue;

			double otherProp = learner.getPolicy().getPropensity(otherID);
			double otherProb = learner.getPolicy().getProbability(otherID);
			assertTrue(newProp < otherProp);
			assertTrue(newProb < otherProb);
		}
	}

	/**
	 * This test checks to see that given a zero-valued reward, the propensity
	 * of a rewarded action changes while the probability of the action stays
	 * the same. In addition, the propensities and probabilities of all actions
	 * should be equal before and after the zero-valued reward.
	 */
	public void testUpdateZeroReward() {
		SimpleAction<String> selectedAction = learner.chooseAction();

		double oldProp = learner.getPolicy().getPropensity(
				selectedAction.getID());
		double oldProb = learner.getPolicy().getProbability(
				selectedAction.getID());

		learner.update(0.0);

		double newProp = learner.getPolicy().getPropensity(
				selectedAction.getID());
		double newProb = learner.getPolicy().getProbability(
				selectedAction.getID());

		// Confirm that the probability and propensity of the rewarded
		// action have increased
		assertTrue(newProp != oldProp);
		assertTrue(newProb == oldProb);

		// Now confirm that the propensity and probabilities for the rewarded
		// action are greater than those of all other actions
		SimpleActionDomain<String> domain = (SimpleActionDomain<String>) learner
				.getPolicy().getActionDomain();
		for (Integer otherID : domain.getIDList()) {
			if (otherID == selectedAction.getID())
				continue;

			double otherProp = learner.getPolicy().getPropensity(otherID);
			double otherProb = learner.getPolicy().getProbability(otherID);
			assertTrue(newProp == otherProp);
			assertTrue(newProb == otherProb);
		}
	}

	public void testChooseAction() {
		SimpleAction<String> action;

		for (int i = 0; i < 1000; i++) {
			action = learner.chooseAction();
			assertNotNull(action);
			assertEquals(action, domain.getAction(action.getID()));
		}
	}

	public void testReset() {
	}

	public void testGetInitialPropensity() {
		double initProp = ((REParameters) learner.getParameters())
				.getInitialPropensity();
		assertNotNull(initProp);
	}

	public void testSetInitialPropensityValue() {
	}

	public void testGetName() {
	}

	public void testGetLastAction() {
		SimpleAction action = learner.chooseAction();

		assertNotNull(learner.getLastSelectedAction());
		assertEquals(learner.getLastSelectedAction(), action);
	}

	public void testGetParameters() {
		assertEquals(params, learner.getParameters());
	}

	public void testSetParameters() {
		REParameters newParams = new REParameters();
		learner.setParameters(newParams);
		assertEquals(newParams, learner.getParameters());
	}

	public void testGetPeriod() {
		for (int i = 1; i <= 1000; i++) {
			learner.update(500.0);
			assertEquals(learner.getUpdateCount(), i);
		}
	}

	public void testGetPolicy() {
		assertSame(policy, learner.getPolicy());
	}

	public void testSetPolicy() {
		REPolicy<Integer, SimpleAction<String>> newPolicy = new REPolicy<Integer, SimpleAction<String>>(
				domain);
		learner.setPolicy(newPolicy);
		assertEquals(newPolicy, learner.getPolicy());

	}

	public void testLearningCycle() {
		// System.out.println("TestRELearner: testLearningCycle");
		SimpleAction<String> action;
		int numCycles = 10;
		int[] counts = new int[domain.size()];

		// System.out.println("   Policy before cycles");
		printPolicy(learner.getPolicy());

		// System.out.println("");
		// System.out.println("Performing "+numCycles+" cycles:");

		for (int i = 0; i <= numCycles; i++) {
			action = learner.chooseAction();
			int index = action.getID();
			counts[index]++;
			// System.out.println("	"+ index +": "+action.getAct());
			learner.update(rewards[index]);
		}

		// System.out.println("");
		// System.out.println("   Policy after cycles");

		// printPolicy(learner.getPolicy());

		// System.out.println("   counts:");
		for (int i = 0; i < policy.getNumActions(); i++) {
			// System.out.println("	id: "+i+" was chosen: "
			// + counts[i]+" times, " + (double)counts[i] / (double)numCycles);
		}

		assertEquals(policy.getNumActions(), domain.size());
	}

	/**
	 * Test that using different random seeds will yield different initial
	 * results. In particular, the first selected action.
	 *
	 */
	public void No_testRandomSeeds() {

		int seed;
		System.out.println("TestRELearner.testRandomSeeds");
		seed = 11235;

		System.out.println(" First round of first actions");
		learner.getPolicy().reset();
		REPolicy p = (REPolicy) learner.getPolicy();
		((REParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 81321;
		learner.getPolicy().reset();
		((REParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 5589144;
		learner.getPolicy().reset();
		((REParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 233377;
		learner.getPolicy().reset();
		((REParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		System.out.println();
		System.out.println(" Second round of first actions");
		learner.getPolicy().reset();

		seed = 11235;
		learner.getPolicy().reset();
		((REParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 81321;
		learner.getPolicy().reset();
		((REParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 5589144;
		learner.getPolicy().reset();
		((REParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 233377;
		learner.getPolicy().reset();
		((REParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

	}

	public void printPolicy(StatelessPolicy p) {
		int numActs = p.getActionDomain().size();

		for (int i = 0; i < numActs; i++) {
			// System.out.println("	id: "+i+" prob: "
			// + p.getProbability(i));
		}
	}
}

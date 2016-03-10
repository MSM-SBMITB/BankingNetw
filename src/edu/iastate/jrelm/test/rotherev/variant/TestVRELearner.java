package edu.iastate.jrelm.test.rotherev.variant;

import java.util.ArrayList;

import cern.jet.random.engine.MersenneTwister;

import edu.iastate.jrelm.core.FeedbackDoubleValue;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.rl.StatelessPolicy;
import edu.iastate.jrelm.rl.SimplePolicy;
import edu.iastate.jrelm.rl.rotherev.RELearner;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;
import edu.iastate.jrelm.rl.rotherev.variant.VRELearner;
import edu.iastate.jrelm.rl.rotherev.variant.VREParameters;
import junit.framework.TestCase;

public class TestVRELearner extends TestCase {

	int DOMAIN_SIZE = 20;
	SimpleActionDomain<String> domain;
	VREParameters params;
	REPolicy<Integer, SimpleAction<String>> policy;
	VRELearner<Integer, SimpleAction<String>> learner;
	MersenneTwister randomGenerator;

	public TestVRELearner(String arg0) {
		super(arg0);
		randomGenerator = new MersenneTwister(112358);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestVRELearner.class);
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
		params = new VREParameters(700, 0.621, 150, 0.47);
		assertNotNull(params);
		policy = new REPolicy<Integer, SimpleAction<String>>(domain,
				randomGenerator);

		learner = new VRELearner<Integer, SimpleAction<String>>(params, policy);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Class under test for void VRELearner(L, D)
	 */
	public void testRothErevLearnerLD() {
		learner = new VRELearner<Integer, SimpleAction<String>>(params, domain);
		assertNotNull(learner);
	}

	/*
	 * Class under test for void VRELearner(L, P)
	 */
	public void testRothErevLearnerLP() {
		learner = new VRELearner<Integer, SimpleAction<String>>(params, policy);
		assertNotNull(learner);
	}

	public void testUpdate() {
		// System.out.println("TestRELearner.testUpdate()");
		SimpleAction<String> action = learner.chooseAction();
		// System.out.println("	Action chosen: "+action.getID().intValue());
		double oldProb = learner.getPolicy().getProbability(action.getID());
		learner.update(500.0);
		double newProb = learner.getPolicy().getProbability(action.getID());

		// System.out.println("	oldProb: "+ oldProb);
		// System.out.println("	newProb: "+newProb);

		assertTrue(newProb > oldProb);
	}

	public void testUpdateActionSuccess() {
		SimpleAction<String> actionToUpdate = domain.getAction(0);
		FeedbackDoubleValue reward = new FeedbackDoubleValue(500.0);

		double oldProb = learner.getPolicy().getProbability(
				actionToUpdate.getID());
		try {
			learner.update(reward, actionToUpdate);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		double newProb = learner.getPolicy().getProbability(
				actionToUpdate.getID());

		// System.out.println("	oldProb: "+ oldProb);
		// System.out.println("	newProb: "+newProb);

		assertTrue(newProb > oldProb);
	}

	public void testUpdateActionFailure() {
		SimpleAction<String> actionToUpdate = new SimpleAction<String>(
				domain.size() + 99, "Bad Action");
		FeedbackDoubleValue reward = new FeedbackDoubleValue(500.0);

		try {
			learner.update(reward, actionToUpdate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			learner.update(reward.getValue().doubleValue(), actionToUpdate);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		fail("No exception occurred when we tried to update on an unknown Action");
	}

	public void testChooseAction() {
		SimpleAction<String> action;
		action = learner.chooseAction();
		assertNotNull(action);

		assertTrue(domain.containsAction(action));
		assertEquals(action, domain.getAction(action.getID()));
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
		VREParameters newParams = new VREParameters();
		learner.setParameters(newParams);
		assertEquals(newParams, learner.getParameters());
	}

	// public void testGetPeriod() {
	// learner.update(500.0);
	// learner.update(500.0);
	// learner.update(500.0);
	//
	// assertEquals(learner.getPeriod(), 3);
	// }

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
		System.out.println("TestRELearner: testLearningCycle");
		SimpleAction<String> action;
		int numCycles = 1000;
		int[] counts = new int[domain.size()];

		System.out.println("   Policy before cycles");
		printPolicy(learner.getPolicy());

		System.out.println("");
		System.out.println("Performing " + numCycles + " cycles:");

		for (int i = 0; i <= numCycles; i++) {
			action = learner.chooseAction();
			int id = action.getID();
			counts[id]++;
			// System.out.println("	"+ id +": "+action.getAct());
			switch (id) {
			case 0:
				learner.update(-5000.0);
				break;
			case 1:
				learner.update(-5000.0);
				break;
			case 4:
				learner.update(150.0);
				break;
			case 7:
				learner.update(250.0);
				break;
			case 10:
				learner.update(-10.0);
				break;
			case 12:
				learner.update(5000.0);
				break;
			case 13:
				learner.update(5000.0);
				break;
			case 14:
				learner.update(100.0);
				break;
			case 15:
				learner.update(-100.0);
				break;
			case 16:
				learner.update(500.0);
				break;
			case 19:
				learner.update(-150.0);
				break;
			default:
				learner.update(100.0);
				break;
			}
		}

		System.out.println("");
		System.out.println("   Policy after cycles");

		printPolicy(learner.getPolicy());

		System.out.println("   counts:");
		for (int i = 0; i < policy.getNumActions(); i++) {
			System.out.println("	id: " + i + " was chosen: " + counts[i]
					+ " times, " + (double) counts[i] / (double) numCycles);
		}

		assertEquals(policy.getNumActions(), domain.size());
	}

	/**
	 * Test that using different random seeds will yield different initial
	 * results. In particular, the first selected action.
	 *
	 */
	public void testRandomSeeds() {
		int seed;
		System.out.println("TestRELearner.testRandomSeeds");
		seed = 11235;

		System.out.println(" First round of first actions");
		learner.getPolicy().reset();
		REPolicy p = (REPolicy) learner.getPolicy();
		((VREParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 81321;
		learner.getPolicy().reset();
		((VREParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 5589144;
		learner.getPolicy().reset();
		((VREParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 233377;
		learner.getPolicy().reset();
		((VREParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		System.out.println();
		System.out.println(" Second round of first actions");
		learner.getPolicy().reset();

		seed = 11235;
		learner.getPolicy().reset();
		((VREParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 81321;
		learner.getPolicy().reset();
		((VREParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 5589144;
		learner.getPolicy().reset();
		((VREParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

		seed = 233377;
		learner.getPolicy().reset();
		((VREParameters) learner.getParameters()).setRandomSeed(seed);
		System.out.println("  	seed: " + seed + " first action: "
				+ learner.chooseAction().getID());

	}

	public void testRewardSequenceA() {
		System.out.println("testRewardSequenceA()");
		ArrayList<double[]> actionList = new ArrayList<double[]>();
		double[] pair;
		for (double j = 10; j <= 100; j = j + 10) {
			for (double k = 5; k < 100; k = k + 5) {
				pair = new double[2];
				pair[0] = j / 100;
				pair[1] = k / 100;
				actionList.add(pair);
			}

			// Need to add a last action for almost 100% markup. Can't use %100
			// (1.0), since it
			// will result in an infinite price. The price calculation divides
			// by
			// (1 - markup), so markup == 1.0 results in x/0
			pair = new double[2];
			pair[0] = j / 100;
			pair[1] = 0.9999;
			actionList.add(pair);
		}

		VREParameters params = new VREParameters(100, 0.2757, 250.0, 0.505,
				370115310);
		SimpleActionDomain<double[]> domain = new SimpleActionDomain<double[]>(
				actionList);

		assertEquals(domain.size(), 200);

		REPolicy<Integer, SimpleAction<double[]>> policy = new REPolicy<Integer, SimpleAction<double[]>>(
				domain, 370115310);

		for (int i = 0; i < domain.size(); i++)
			assertEquals(policy.getProbability(i), 1.0 / (double) domain.size());

		RELearner<Integer, SimpleAction<double[]>> learner = new RELearner<Integer, SimpleAction<double[]>>(
				params, policy);

		int selectedAction = learner.chooseAction().getID();
		double reward = 160.0;
		System.out.println("	First action choice: " + selectedAction);
		System.out.println("		Rewarding with value " + reward);
		// System.out.println("		Policy before");
		learner.update(reward);
		System.out.println("		Policy after");
		printPolicy(learner.getPolicy());

		selectedAction = learner.chooseAction().getID();
		reward = 320;
		System.out.println("	Second action choice: " + selectedAction);
		System.out.println("		Rewarding with value " + reward);
		// System.out.println("		Policy before");
		learner.update(reward);
		// System.out.println("		Policy after");
		// printPolicy(learner.getPolicy());

		selectedAction = learner.chooseAction().getID();
		reward = 480;
		System.out.println("	Third action choice: " + selectedAction);
		System.out.println("		Rewarding with value " + reward);
		// System.out.println("		Policy before");
		learner.update(reward);
		// System.out.println("		Policy after");
		// printPolicy(learner.getPolicy());

		selectedAction = learner.chooseAction().getID();
		reward = 640;
		System.out.println("	Fourth action choice: " + selectedAction);
		System.out.println("		Rewarding with value " + reward);
		// System.out.println("		Policy before");
		learner.update(reward);
		// System.out.println("		Policy after");
		// printPolicy(learner.getPolicy());

		selectedAction = learner.chooseAction().getID();
		reward = 5199.999999999995;
		// System.out.println("	Five action choice: " + selectedAction);
		// System.out.println("		Rewarding with value " + reward);
		// System.out.println("		Policy before");
		// learner.update(reward);
		// System.out.println("		Policy after");
		// printPolicy(learner.getPolicy());

	}

	public void printPolicy(StatelessPolicy p) {
		int numActs = p.getActionDomain().size();

		for (int i = 0; i < numActs; i++) {
			System.out.println("	id: " + i + " prob: " + p.getProbability(i));
		}
	}

}

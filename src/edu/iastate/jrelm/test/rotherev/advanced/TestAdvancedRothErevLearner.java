package edu.iastate.jrelm.test.rotherev.advanced;

import java.util.ArrayList;

import cern.jet.random.engine.MersenneTwister;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;
import edu.iastate.jrelm.rl.rotherev.advanced.ARELearner;
import edu.iastate.jrelm.rl.rotherev.advanced.AREParameters;
import junit.framework.TestCase;

public class TestAdvancedRothErevLearner extends TestCase {

	int DOMAIN_SIZE = 20;
	SimpleActionDomain<String> domain;
	AREParameters params;
	REPolicy<Integer, SimpleAction<String>> policy;
	ARELearner<Integer, SimpleAction<String>> learner;
	MersenneTwister randomGenerator;

	public TestAdvancedRothErevLearner(String arg0) {
		super(arg0);
		randomGenerator = new MersenneTwister(112358);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestAdvancedRothErevLearner.class);
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
		params = new AREParameters(10, 0.77, 50, actionList.size(), 0.2);

		policy = new REPolicy<Integer, SimpleAction<String>>(domain,
				randomGenerator);

		learner = new ARELearner<Integer, SimpleAction<String>>(params, policy);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Class under test for void VRELearner(L, D)
	 */
	public void testRothErevLearnerLD() {
		learner = new ARELearner<Integer, SimpleAction<String>>(params, domain);
		assertNotNull(learner);
	}

	/*
	 * Class under test for void VRELearner(L, P)
	 */
	public void testRothErevLearnerLP() {
		learner = new ARELearner<Integer, SimpleAction<String>>(params, policy);
		assertNotNull(learner);
	}

	public void testUpdate() {
		// System.out.println("TestAdvancedRothErevLearner.testUpdate()");
		SimpleAction<String> action = learner.chooseAction();
		// System.out.println("	Action chosen: "+action.getID().intValue());
		double oldProb = learner.getPolicy().getProbability(action.getID());
		learner.update(500.0);
		double newProb = learner.getPolicy().getProbability(action.getID());

		// System.out.println("	oldProb: "+ oldProb);
		// System.out.println("	newProb: "+newProb);

		assertTrue(newProb > oldProb);
	}

	public void testChooseAction() {
		SimpleAction<String> action;
		action = learner.chooseAction();
		assertNotNull(action);

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
		REParameters newParams = new REParameters();
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
		assertEquals(policy, learner.getPolicy());
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
		int numCycles = 1000;
		int[] counts = new int[domain.size()];

		// System.out.println("   Policy before cycles");
		printPolicy();

		// System.out.println("");
		// System.out.println("Performing "+numCycles+" cycles:");

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

		// System.out.println("");
		// System.out.println("   Policy after cycles");

		// printPolicy();

		// System.out.println("   counts:");
		// for(int i=0; i < policy.getNumActions(); i++){
		// System.out.println("	id: "+i+" was chosen: "
		// + counts[i]+" times");
		// }

		assertEquals(policy.getNumActions(), domain.size());
	}

	public void printPolicy() {
		policy = learner.getPolicy();
		for (int i = 0; i < policy.getNumActions(); i++) {
			// System.out.println("	id: "+i+" prob: "
			// + policy.getProbability(i));
		}
	}
}

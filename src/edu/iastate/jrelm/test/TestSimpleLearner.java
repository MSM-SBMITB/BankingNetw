package edu.iastate.jrelm.test;

import java.util.ArrayList;

import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.rl.SimpleStatelessLearner;
import edu.iastate.jrelm.rl.SimpleStatelessPolicy;
import edu.iastate.jrelm.rl.rotherev.variant.VREParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;
//import edu.iastate.jrelm.rl.rotherev.advanced.AREParameters;
import junit.framework.TestCase;

import static java.lang.System.out;

public class TestSimpleLearner extends TestCase {

	int numActions = 20;
	SimpleActionDomain<String> domain;
	SimpleStatelessLearner<String> learner;

	public TestSimpleLearner(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestSimpleLearner.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		ArrayList<String> actionList = new ArrayList<String>();
		for (int i = 0; i < numActions; i++)
			actionList.add("Action" + i);

		domain = new SimpleActionDomain<String>(actionList);
		VREParameters reParams = new VREParameters();

		learner = new SimpleStatelessLearner<String>(reParams, actionList);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testChooseAction() {
		SimpleAction<String> action = learner.chooseAction();
		assertNotNull(action);

		int index = action.getID();
		assertTrue((index >= 0) && (index < numActions));
	}

	public void testSetParameters() {
		VREParameters oldParams = (VREParameters) learner.getParameters();
		VREParameters newParams1 = new VREParameters(128486803, 0.452, 500,
				0.23);
		learner.setParameters(newParams1);

		assertNotSame(oldParams, learner.getParameters());
		assertSame(newParams1, learner.getParameters());

		AREParameters newParams2 = new AREParameters(128486803, 0.452, 500,
				domain.size(), 0.23);
		learner.setParameters(newParams2);

		assertNotSame(oldParams, learner.getParameters());
		assertSame(newParams2, learner.getParameters());
	}

	public void testSetParametersIncompatibleClass() {
		IllegalArgumentException exception = null;

		FakeQLearningParameters qParams = new FakeQLearningParameters();

		try {
			learner.setParameters(qParams);
		} catch (IllegalArgumentException iae) {
			assertTrue(ClassCastException.class.isInstance(iae.getCause()));
			assertTrue(learner.getPolicy() != null);
			assertNotSame(qParams, learner.getParameters());
			assertFalse(learner.getParameters().equals(qParams));
			exception = iae;
		}
		// yell if no exception was thrown
		assertNotNull(exception);
	}

	public void testSetPolicy() {
		REPolicy oldPolicy = (REPolicy) learner.getPolicy();

		REPolicy<Integer, SimpleAction<String>> newPolicy = new REPolicy<Integer, SimpleAction<String>>(
				domain);
		newPolicy.setProbability(0, 0.75);

		learner.setPolicy(newPolicy);

		assertNotSame(oldPolicy, learner.getPolicy());
		assertSame(newPolicy, learner.getPolicy());
	}

	public void testSetPolicyIncompitibleClass() {
		// out.println("TestSimpleLearner.testSetPolicyIncompatible:");
		IllegalArgumentException exception = null;

		SimpleStatelessPolicy<Integer, SimpleAction<String>> newPolicy = new SimpleStatelessPolicy<Integer, SimpleAction<String>>(
				domain);

		try {
			learner.setPolicy(newPolicy);
		} catch (IllegalArgumentException iae) {
			assertTrue(ClassCastException.class.isInstance(iae.getCause()));
			assertTrue(learner.getPolicy() != null);
			assertNotSame(newPolicy, learner.getPolicy());
			assertFalse(learner.getPolicy().equals(newPolicy));
			exception = iae;
		}
		// yell if no exception was thrown
		assertNotNull(exception);
	}
}

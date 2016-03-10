package edu.iastate.jrelm.test.rl;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import cern.jet.random.engine.MersenneTwister;

import edu.iastate.jrelm.core.Feedback;
import edu.iastate.jrelm.core.FeedbackDoubleValue;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.demo.RothErevAgent;
import edu.iastate.jrelm.rl.RLParameters;
import edu.iastate.jrelm.rl.ReinforcementLearner;
import edu.iastate.jrelm.rl.SimpleStatelessLearner;
import edu.iastate.jrelm.rl.SimpleStatelessPolicy;
import edu.iastate.jrelm.rl.rotherev.RELearner;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;
import edu.iastate.jrelm.rl.rotherev.variant.VRELearner;
import edu.iastate.jrelm.rl.rotherev.variant.VREParameters;

import junit.framework.TestCase;

public class SimpleStatelessLearnerTest extends TestCase {

	ArrayList<Integer> actionList;
	SimpleActionDomain<Integer> simpleDomain;
	ReinforcementLearner<RLParameters, Integer, SimpleAction, Feedback<Double>, SimpleStatelessPolicy> genericLearner;

	REPolicy<Integer, SimpleAction<Integer>> policy;
	REParameters reParams;
	RELearner<Integer, SimpleAction<Integer>> reLearner;
	VREParameters vreParams;
	VRELearner<Integer, SimpleAction<Integer>> vreLearner;
	SimpleStatelessLearner<Integer> simpleLearner;

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(SimpleStatelessLearnerTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		actionList = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++)
			actionList.add(i);

		simpleDomain = new SimpleActionDomain<Integer>(actionList);

		policy = new REPolicy<Integer, SimpleAction<Integer>>(simpleDomain);

		reParams = new REParameters(1.0, 0.65, 500, 0.45, 11235);
		reLearner = new RELearner<Integer, SimpleAction<Integer>>(reParams,
				policy);

		vreParams = new VREParameters(1.0, 0.65, 500, 0.45, 11235);
		vreLearner = new VRELearner<Integer, SimpleAction<Integer>>(vreParams,
				policy);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessLearner.SimpleStatelessLearner(REParameters,
	 * Collection<O>)'
	 */
	public void testSimpleStatelessLearnerREParametersCollectionOfO() {
		simpleLearner = new SimpleStatelessLearner<Integer>(reParams,
				actionList);
		assertEquals(simpleLearner.getName(), reLearner.getName());
		comparePropensities_RELearner();
		compareProbabilities_RELearner();
		compareChooseAction_RELearner();
		compareUpdate_RELearner();
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessLearner.SimpleStatelessLearner(REParameters,
	 * SimpleActionDomain<O>)'
	 */
	public void testSimpleStatelessLearnerREParametersSimpleActionDomainOfO() {
		simpleLearner = new SimpleStatelessLearner<Integer>(reParams,
				simpleDomain);
		assertEquals(simpleLearner.getName(), reLearner.getName());
		comparePropensities_RELearner();
		compareProbabilities_RELearner();
		compareChooseAction_RELearner();
		compareUpdate_RELearner();
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessLearner.SimpleStatelessLearner(REParameters,
	 * REPolicy<Integer, SimpleAction<O>>)'
	 */
	public void testSimpleStatelessLearnerREParametersREPolicyOfIntegerSimpleActionOfO() {

		// Need to create two policies with the exact same information. Need
		// separate policies,
		// otherwise the SimpleStatless learner and RELearner will request new
		// choices from the
		// same policy resulting in different choices for the same comparison
		// iteration
		REPolicy<Integer, SimpleAction<Integer>> policy1 = new REPolicy<Integer, SimpleAction<Integer>>(
				simpleDomain);
		REPolicy<Integer, SimpleAction<Integer>> policy2 = new REPolicy<Integer, SimpleAction<Integer>>(
				simpleDomain);

		reLearner = new RELearner<Integer, SimpleAction<Integer>>(reParams,
				policy1);
		simpleLearner = new SimpleStatelessLearner<Integer>(reParams, policy2);

		assertEquals(simpleLearner.getName(), reLearner.getName());
		comparePropensities_RELearner();
		compareProbabilities_RELearner();
		compareChooseAction_RELearner();
		compareUpdate_RELearner();
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessLearner.SimpleStatelessLearner(VREParameters,
	 * Collection<O>)'
	 */
	public void testSimpleStatelessLearnerVREParametersCollectionOfO() {
		simpleLearner = new SimpleStatelessLearner<Integer>(vreParams,
				actionList);
		assertEquals(simpleLearner.getName(), vreLearner.getName());
		comparePropensities_VRELearner();
		compareProbabilities_VRELearner();
		compareChooseAction_VRELearner();
		compareUpdate_VRELearner();
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessLearner.SimpleStatelessLearner(VREParameters,
	 * SimpleActionDomain<O>)'
	 */
	public void testSimpleStatelessLearnerVREParametersSimpleActionDomainOfO() {
		simpleLearner = new SimpleStatelessLearner<Integer>(vreParams,
				simpleDomain);
		assertEquals(simpleLearner.getName(), vreLearner.getName());
		comparePropensities_VRELearner();
		compareProbabilities_VRELearner();
		compareChooseAction_VRELearner();
		compareUpdate_VRELearner();
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessLearner.SimpleStatelessLearner(VREParameters,
	 * REPolicy<Integer, SimpleAction<O>>)'
	 */
	public void testSimpleStatelessLearnerVREParametersREPolicyOfIntegerSimpleActionOfO() {
		// Need to create two policies with the exact same information. Need
		// separate policies,
		// otherwise the SimpleStatless learner and RELearner will request new
		// choices from the
		// same policy resulting in different choices for the same comparison
		// iteration
		REPolicy<Integer, SimpleAction<Integer>> policy1 = new REPolicy<Integer, SimpleAction<Integer>>(
				simpleDomain);
		REPolicy<Integer, SimpleAction<Integer>> policy2 = new REPolicy<Integer, SimpleAction<Integer>>(
				simpleDomain);

		vreLearner = new VRELearner<Integer, SimpleAction<Integer>>(vreParams,
				policy1);
		simpleLearner = new SimpleStatelessLearner<Integer>(vreParams, policy2);

		assertEquals(simpleLearner.getName(), vreLearner.getName());
		comparePropensities_VRELearner();
		compareProbabilities_VRELearner();
		compareChooseAction_VRELearner();
		compareUpdate_VRELearner();
	}

	public void compareUpdate_RELearner() {
		SimpleAction<Integer> reChoice;
		SimpleAction<Integer> simpleChoice;
		MersenneTwister twister = new MersenneTwister();
		double reward;

		reChoice = reLearner.chooseAction();
		simpleChoice = simpleLearner.chooseAction();
		assertEquals(simpleChoice.getAct(), reChoice.getAct());

		for (int i = 0; i < 1000; i++) {
			reward = 1000.0 * twister.nextDouble();
			reLearner.update(reward);
			simpleLearner.update(new FeedbackDoubleValue(reward));
			reChoice = reLearner.chooseAction();
			simpleChoice = simpleLearner.chooseAction();
			assertEquals(simpleChoice.getAct(), reChoice.getAct());
			comparePropensities_RELearner();
			compareProbabilities_RELearner();
		}
	}

	public void comparePropensities_RELearner() {
		for (int i = 0; i < actionList.size(); i++) {
			assertEquals(
					((REPolicy<Integer, SimpleAction<Integer>>) simpleLearner.getPolicy())
							.getPropensities()[i], reLearner.getPolicy()
							.getPropensities()[i]);
		}
	}

	public void compareProbabilities_RELearner() {
		for (int i = 0; i < actionList.size(); i++) {
			assertEquals(
					((REPolicy<Integer, SimpleAction<Integer>>) simpleLearner.getPolicy())
							.getProbabilities()[i], reLearner.getPolicy()
							.getProbabilities()[i]);
		}
	}

	public void compareChooseAction_RELearner() {
		SimpleAction<Integer> reChoice;
		SimpleAction<Integer> simpleChoice;

		for (int i = 0; i < 1000; i++) {
			reChoice = reLearner.chooseAction();
			simpleChoice = simpleLearner.chooseAction();
			assertEquals(simpleChoice.getAct(), reChoice.getAct());
		}
	}

	// -------------------------
	public void compareUpdate_VRELearner() {
		SimpleAction<Integer> vreChoice;
		SimpleAction<Integer> simpleChoice;
		MersenneTwister twister = new MersenneTwister();
		double reward;

		vreChoice = vreLearner.chooseAction();
		simpleChoice = simpleLearner.chooseAction();
		assertEquals(simpleChoice.getAct(), vreChoice.getAct());

		for (int i = 0; i < 1000; i++) {
			reward = 1000.0 * twister.nextDouble();
			vreLearner.update(reward);
			simpleLearner.update(new FeedbackDoubleValue(reward));
			vreChoice = vreLearner.chooseAction();
			simpleChoice = simpleLearner.chooseAction();
			assertEquals(simpleChoice.getAct(), vreChoice.getAct());
			comparePropensities_VRELearner();
			compareProbabilities_VRELearner();
		}
	}

	public void comparePropensities_VRELearner() {
		for (int i = 0; i < actionList.size(); i++) {
			assertEquals(
					((REPolicy<Integer, SimpleAction<Integer>>) simpleLearner.getPolicy())
							.getPropensities()[i], vreLearner.getPolicy()
							.getPropensities()[i]);
		}
	}

	public void compareProbabilities_VRELearner() {
		for (int i = 0; i < actionList.size(); i++) {
			assertEquals(
					((REPolicy<Integer, SimpleAction<Integer>>) simpleLearner.getPolicy())
							.getProbabilities()[i], vreLearner.getPolicy()
							.getProbabilities()[i]);
		}
	}

	public void compareChooseAction_VRELearner() {
		SimpleAction<Integer> vreChoice;
		SimpleAction<Integer> simpleChoice;

		for (int i = 0; i < 1000; i++) {
			vreChoice = vreLearner.chooseAction();
			simpleChoice = simpleLearner.chooseAction();
			assertEquals(simpleChoice.getAct(), vreChoice.getAct());
		}
	}

}

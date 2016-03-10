package edu.iastate.jrelm.test.rotherev;

import java.util.ArrayList;

import cern.jet.random.engine.MersenneTwister;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.rl.rotherev.REPolicy;
import edu.iastate.jrelm.rl.rotherev.RELearner;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import junit.framework.TestCase;

/**
 * These tests validates that RELearner fulfills its intended use. The intended
 * use being a correct implementation of the Roth-Erev learning algorithm. In
 * other words, these test check to see that the RELearner behaves in accordance
 * with the Roth-Erev algorithm.
 * 
 * Validatation the RELearner is made against calculations done by hand.
 * 
 * Note, these tests do not verify that RELearner is a properly operating
 * ReinforcementLearner component, which is handled by TestRELearner.
 * 
 * @see edu.iastate.jrelm.test.rotherev.TestRELearner
 * 
 * @author Charles Gieseler
 *
 */
public class RELearnerValidationTest extends TestCase {

	double BOLTZMANN_TEMP = 50;
	double EXPERIMENTATION = 0.65;
	double INIT_PROPENSITY = 500;
	double RECENCY = 0.43;

	int DOMAIN_SIZE = 4;
	SimpleActionDomain<String> domain;
	REParameters params;
	REPolicy<Integer, SimpleAction<String>> policy;
	RELearner<Integer, SimpleAction<String>> learner;
	MersenneTwister randomGenerator;

	public RELearnerValidationTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(RELearnerValidationTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		randomGenerator = new MersenneTwister();

		ArrayList<String> actionList = new ArrayList<String>();

		for (double j = 0; j < DOMAIN_SIZE; j++) {
			actionList.add("Action_" + j);
		}
		System.out.println("TestRELearner: setup(): actionList");
		// for(String act: actionList){
		// System.out.println(act);
		// }
		domain = new SimpleActionDomain<String>(actionList);
		params = new REParameters(BOLTZMANN_TEMP, EXPERIMENTATION,
				INIT_PROPENSITY, RECENCY, 112358);
		policy = new REPolicy<Integer, SimpleAction<String>>(domain,
				randomGenerator);
		learner = new RELearner<Integer, SimpleAction<String>>(params, policy);
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		learner = null;
		policy = null;
		params = null;
		domain = null;
		randomGenerator = null;
	}

	public void testLearningProcess() {
		SimpleAction<String> newChoice;

		/*
		 * First 6 random numbers generated from a MersenneTwister with seed
		 * 112358
		 * 
		 * 0.47964709435068315 -- First choice start from uniform, so ignore
		 * 
		 * 0.8121422842891705 0.011851526708644866 0.7568290174136303
		 * 0.5973060739047057 0.9757808769921001
		 */
		System.out.println("Before initial selection:");
		double[] props;
		double[] probs;

		// ---- INITIAL SELECTION ----
		// Starts from uniform propensities and probabilities
		// Initial choice should be 1, using SimpleEventGenerator
		// and rand value 0.47964709435068315
		newChoice = learner.chooseAction();
		assertEquals(1, newChoice.getID().intValue());
		learner.update(300.0);

		props = policy.getPropensities();
		assertEquals(350, props[0]);
		assertEquals(390, props[1]);
		assertEquals(350, props[2]);
		assertEquals(350, props[3]);

		// probs = policy.getProbabilities();
		// assertEquals(0.1913677, probs[0]);
		// assertEquals(0.4258968, probs[1]);
		// assertEquals(0.1913677, probs[2]);
		// assertEquals(0.1913677, probs[3]);

		// ----------------------------

		System.out.println("Before first informed choice:");
		printStatus();
		System.out.println();
		/*
		 * FIRST INFORMED CHOICE Values after last update according to hand
		 * calculation of the Roth-Erev algorithm
		 * 
		 * Propensities after update choice 0:
		 * 393.333333333333333333333333333333 choice 1: 460.0 choice 2:
		 * 393.333333333333333333333333333333 choice 3:
		 * 393.333333333333333333333333333333
		 * 
		 * Probabilities after update choice 0:
		 * 0.23983739837398373983739837398387 choice 1:
		 * 0.28048780487804878048780487804895 choice 2:
		 * 0.23983739837398373983739837398387 choice 3:
		 * 0.23983739837398373983739837398387
		 * 
		 * With random value 0.8121422842891705, should choose 3 using simple,
		 * subtractive probability method used in SimpleEventGenerator.
		 */
		newChoice = learner.chooseAction();
		assertEquals(3, newChoice.getID().intValue());
		learner.update(200.0);

		/*
		 * SECOND INFORMED CHOICE
		 * 
		 * Propensities 224.199999999999999999999999999999
		 * 305.533333333333333333333333333333 224.199999999999999999999999999999
		 * 294.199999999999999999999999999999
		 * 
		 * Probabilities 0.21390408344994275537463427044902
		 * 0.29150235339015392443709451723705 0.21390408344994275537463427044902
		 * 0.2806894797099605648136369418649
		 * 
		 * Rand value: 0.011851526708644866 Choosen Next: 0
		 */
		System.out.println("Before second informed choice:");
		printStatus();
		System.out.println();
		newChoice = learner.chooseAction();
		assertEquals(0, newChoice.getID().intValue());
		learner.update(50.0);

		/*
		 * THIRD INFORMED CHOICE
		 * 
		 * Propensities 145.293999999999999999999999999999
		 * 184.987333333333333333333333333333 138.627333333333333333333333333333
		 * 178.527333333333333333333333333333
		 * 
		 * Probabilities 0.22441445949869948535453697353865
		 * 0.28572296463794619596891945046829 0.21411743142694155612807031634534
		 * 0.27574514443641276254847325964788
		 * 
		 * Rand value: 0.7568290174136303 Choosen Next: 3
		 */
		System.out.println("Before third informed choice:");
		printStatus();
		System.out.println();
		newChoice = learner.chooseAction();
		assertEquals(3, newChoice.getID().intValue());
		learner.update(-25.0);

		/*
		 * FOURTH INFORMED CHOICE
		 * 
		 * Propensities 77.400913333333333333333333333276
		 * 100.02611333333333333333333333331 73.600913333333333333333333333314
		 * 93.010579999999999999999999999999
		 * 
		 * Probabilities 0.22558223087390787963811557939355
		 * 0.29074102903748491108884885376754 0.21393218798096600733353233811455
		 * 0.27034932018658840876308096833574
		 * 
		 * Rand value: 0.5973060739047057 Choosen Next: 2
		 */
		System.out.println("Before fourth informed choice:");
		printStatus();
		System.out.println();
		newChoice = learner.chooseAction();
		assertEquals(2, newChoice.getID().intValue());
		learner.update(1500.0);

		/*
		 * FIFTH INFORMED CHOICE
		 * 
		 * Propensities 369.11852059999999999999999999996
		 * 382.01488459999999999999999999998 1091.9525206
		 * 378.01603059999999999999999999999
		 * 
		 * Probabilities 0.16618711245397919726041082334534
		 * 0.17199340331912374340025591451956 0.49162647282066029159434763172247
		 * 0.17019301140623676774498563041294
		 * 
		 * Rand value: 0.9757808769921001 Choosen Next: 3
		 */
		System.out.println("Before fifth informed choice");
		printStatus();

		newChoice = learner.chooseAction();
		assertEquals(3, newChoice.getID().intValue());

	}

	public void printStatus() {
		REPolicy temp = learner.getPolicy();
		double[] props = temp.getPropensities();
		double[] probs = temp.getProbabilities();
		System.out.println("  Propensities: ");
		for (double pp : props)
			System.out.println("	" + pp);

		System.out.println();
		System.out.println("  Propbabilities: ");
		for (double pb : probs)
			System.out.println("	" + pb);
	}
}

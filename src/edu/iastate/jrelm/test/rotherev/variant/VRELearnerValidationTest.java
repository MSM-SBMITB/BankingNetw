package edu.iastate.jrelm.test.rotherev.variant;

import java.util.ArrayList;

import cern.jet.random.engine.MersenneTwister;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.rl.rotherev.variant.VRELearner;
import edu.iastate.jrelm.rl.rotherev.variant.VREParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;
import edu.iastate.jrelm.test.rotherev.RELearnerValidationTest;
import junit.framework.TestCase;

public class VRELearnerValidationTest extends TestCase {

	double BOLTZMANN_TEMP = 50;
	double EXPERIMENTATION = 0.65;
	double INIT_PROPENSITY = 500;
	double RECENCY = 0.43;

	int DOMAIN_SIZE = 4;
	SimpleActionDomain<String> domain;
	VREParameters params;
	REPolicy<Integer, SimpleAction<String>> policy;
	VRELearner<Integer, SimpleAction<String>> learner;
	MersenneTwister randomGenerator;

	public VRELearnerValidationTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(RELearnerValidationTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		randomGenerator = new MersenneTwister(112358);

		ArrayList<String> actionList = new ArrayList<String>();

		for (double j = 0; j < DOMAIN_SIZE; j++) {
			actionList.add("Action_" + j);
		}

		domain = new SimpleActionDomain<String>(actionList);
		params = new VREParameters(BOLTZMANN_TEMP, EXPERIMENTATION,
				INIT_PROPENSITY, RECENCY, 112358);
		policy = new REPolicy<Integer, SimpleAction<String>>(domain,
				randomGenerator);
		learner = new VRELearner<Integer, SimpleAction<String>>(params, policy);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testLearningProcess() {
		System.out.println("VRELearnerValidationTest.testLearningProcess()");
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

		// ---- INITIAL SELECTION ----
		// Starts from uniform propensities and probabilities
		// Initial choice should be 1, using SimpleEventGenerator
		// and rand value 0.47964709435068315
		newChoice = learner.chooseAction();
		assertEquals(1, newChoice.getID().intValue());
		learner.update(300.0);
		// ----------------------------
		// System.out.println("Propensities:");
		// for(double prop : learner.getPolicy().getPropensities())
		// System.out.println("	"+ prop);
		//
		// System.out.println("Probabilities:");
		// for(double prop : learner.getPolicy().getProbabilities())
		// System.out.println("	"+ prop);
		/*
		 * FIRST INFORMED CHOICE Values after last update according to hand
		 * calculation of the Roth-Erev algorithm
		 * 
		 * Propensities after update choice 0: 393.33333 choice 1: 390 choice 2:
		 * 393.33333 choice 3: 393.33333
		 * 
		 * Probabilities after update choice 0: 0.2540969 choice 1: 0.2377094
		 * choice 2: 0.2540969 choice 3: 0.2540969
		 * 
		 * With random value 0.8121422842891705, should choose 3 using simple,
		 * subtractive probability method used in SimpleEventGenerator.
		 */
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.2540969,
				0.000001));
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.2377094,
				0.000001));
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.2540969,
				0.000001));
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.2540969,
				0.000001));

		newChoice = learner.chooseAction();
		assertEquals(3, newChoice.getID().intValue());
		learner.update(200.0);

		/*
		 * SECOND INFORMED CHOICE
		 * 
		 * Propensities 309.42222 306.8 309.42222 294.2 Probabilities 0.2712644
		 * 0.2574047 0.2712644 0.2000665 Rand value: 0.011851526708644866
		 * Choosen Next: 0
		 */
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.2712644,
				0.000001));
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.2574047,
				0.000001));
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.2712644,
				0.000001));
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.2000665,
				0.000001));

		newChoice = learner.chooseAction();
		assertEquals(0, newChoice.getID().intValue());
		learner.update(50.0);

		/*
		 * THIRD INFORMED CHOICE
		 * 
		 * Propensities 193.87067 241.34933 243.41215 231.43733 Probabilities
		 * 0.1190774 0.3077682 0.3207312 0.2524232 Rand value:
		 * 0.7568290174136303 Choosen Next: 3
		 */
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.1190774,
				0.000001));
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.3077682,
				0.000001));
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.3207312,
				0.000001));
		assertTrue(within(learner.getPolicy().getPropensity(0), 0.2524232,
				0.000001));

		newChoice = learner.chooseAction();
		assertEquals(3, newChoice.getID().intValue());
		learner.update(-25.0);

		/*
		 * FOURTH INFORMED CHOICE
		 * 
		 * Propensities
		 * 
		 * 
		 * Probabilities
		 * 
		 * 
		 * Rand value: 0.5973060739047057 Choosen Next:
		 */
		newChoice = learner.chooseAction();
		// assertEquals(2, newChoice.getID().intValue());
		learner.update(1500.0);

		/*
		 * FOURTH INFORMED CHOICE
		 * 
		 * Propensities
		 * 
		 * Probabilities
		 * 
		 * Rand value: 0.9757808769921001 Choosen Next:
		 */
		newChoice = learner.chooseAction();
		// assertEquals(3, newChoice.getID().intValue());
	}

	boolean within(double x, double y, double e) {
		if ((x >= (y - e)) || (x <= (y + e)))
			return true;
		else
			return false;
	}
}

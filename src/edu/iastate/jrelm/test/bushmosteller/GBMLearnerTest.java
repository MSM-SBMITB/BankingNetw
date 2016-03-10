package edu.iastate.jrelm.test.bushmosteller;

import edu.iastate.jrelm.core.FeedbackDoubleValue;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.rl.AbstractStatlessLearner;
import edu.iastate.jrelm.rl.bushmosteller.GBMLearner;
import edu.iastate.jrelm.rl.bushmosteller.LinearGBMParameters;
import edu.iastate.jrelm.test.rl.AbstractStatlessLearnerTest;

/**
 * 
 * Tests the GBMLeaner. Note, this tests uses LinearGBMParameters. This means
 * that Generalised Bush-Mosteller learnning with linear reinforcement stregth
 * modification is being used for this test.
 * 
 * @author Charles Gieseler
 *
 */
public class GBMLearnerTest extends
		AbstractStatlessLearnerTest<LinearGBMParameters, Double> {

	protected void setUp() throws Exception {
		super.setUp();

		setParameters(new LinearGBMParameters(0.1, 0, 10));
		setFeedback(new FeedbackDoubleValue(5.0));

		GBMLearner<Integer, SimpleAction<Integer>> newLearner = new GBMLearner<Integer, SimpleAction<Integer>>(
				getParameters(), getPolicy());
		setLearner((AbstractStatlessLearner) newLearner);
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		setParameters(null);
		setFeedback(null);
		setLearner(null);
	}

	public void testUpdate() {
		getLearner().update(getFeedback());

		fail("Not yet implemented");
	}

	public void testMakeParameters() {
		assertNotNull(getLearner().makeParameters());
		assertTrue(getLearner().makeParameters().validateParameters());
	}

	public void testGetName() {
		assertEquals("Generalised Bush-Mosteller", getLearner().getName());
	}
}

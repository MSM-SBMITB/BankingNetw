package edu.iastate.jrelm.test.rl;

import java.util.ArrayList;

import edu.iastate.jrelm.core.Feedback;
import edu.iastate.jrelm.core.FeedbackDoubleValue;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.rl.AbstractStatlessLearner;
import edu.iastate.jrelm.rl.RLParameters;
import edu.iastate.jrelm.rl.SimpleStatelessPolicy;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import junit.framework.TestCase;

/**
 * Base test to check functionality implemented by the AbstractStatelessLearner.
 * This test should not be run itself. Rather, tests for classes that extend
 * AbstractStatelessLearner should extend this class and then run.
 * 
 * @author Charles Gieseler
 *
 * @param <PA>
 *            - specific type of parameters used for the test
 * @param <R>
 *            - reward feedback type that should be used
 */
public abstract class AbstractStatlessLearnerTest<PA extends RLParameters, R>
		extends TestCase {

	private PA parameters;
	private Feedback<R> feedback;
	private SimpleActionDomain<Integer> actionDomain;
	private SimpleStatelessPolicy<Integer, SimpleAction<Integer>> policy;
	private AbstractStatlessLearner<PA, Integer, SimpleAction<Integer>, Feedback<R>, SimpleStatelessPolicy<Integer, SimpleAction<Integer>>> learner;

	private int numActions = 5;

	/*************************************************************************
	 * TESTS
	 *************************************************************************/

	protected void setUp() throws Exception {
		super.setUp();

		ArrayList<Integer> actionList = new ArrayList<Integer>();
		for (int i = 0; i <= numActions; i++) {
			actionList.add(i);
		}
		actionDomain = new SimpleActionDomain<Integer>(actionList);

		policy = new SimpleStatelessPolicy<Integer, SimpleAction<Integer>>(
				actionDomain);

	}

	protected void tearDown() throws Exception {
		super.tearDown();

		setParameters(null);
		setActionDomain(null);
		setPolicy(null);
		setLearner(null);
	}

	/*************************************************************************
	 * ABSTRACT TESTS
	 *************************************************************************/

	/**
	 * Should be overriden to test functionality specific to individual learning
	 * implementations.
	 */
	public abstract void testUpdate();

	/**
	 * Should be overriden to test functionality specific to individual learning
	 * implementations.
	 */
	public abstract void testMakeParameters();

	public abstract void testGetName();

	/*************************************************************************
	 * IMPLEMENTED TESTS
	 *************************************************************************/

	public void testChooseAction() {
		SimpleAction<Integer> nextChoice;

		for (int i = 0; i < (1000 * numActions); i++) {
			nextChoice = learner.chooseAction();
			assertNotNull(nextChoice);

			int id = nextChoice.getID();
			assertTrue((id >= 0) && (id < numActions));
		}
	}

	public void testGetUpdateCount() {
		for (int i = 0; i < 100; i++) {
			getLearner().update(getFeedback());
			assertEquals((i + 1), getLearner().getUpdateCount());
		}
	}

	public void testGetLastSelectedAction() {

		assertNull(getLearner().getLastSelectedAction());

		for (int i = 0; i < 100; i++) {
			SimpleAction<Integer> newAction = this.getLearner().chooseAction();
			assertEquals(newAction, getLearner().getLastSelectedAction());
		}
	}

	public void testGetParameters() {
		assertNotNull(getLearner().getParameters());
		assertEquals(this.getParameters(), getLearner().getParameters());
	}

	public void testSetParameters() {
		getLearner().setParameters(this.getParameters());
		assertEquals(this.getParameters(), getLearner().getParameters());

		PA newParams = getLearner().makeParameters();
		getLearner().setParameters(newParams);
		assertEquals(newParams, getLearner().getParameters());

		getLearner().setParameters(null);
		assertNotNull(getLearner().getParameters());
	}

	public void testGetPolicy() {
		assertNotNull(getLearner().getPolicy());
		assertEquals(this.getPolicy(), getLearner().getPolicy());
	}

	public void testSetPolicy() {
		getLearner().setPolicy(this.getPolicy());
		assertEquals(this.getPolicy(), getLearner().getPolicy());
	}

	/*************************************************************************
	 * ACCESSORS
	 *************************************************************************/

	public SimpleActionDomain<Integer> getActionDomain() {
		return actionDomain;
	}

	public void setActionDomain(SimpleActionDomain<Integer> actionDomain) {
		this.actionDomain = actionDomain;
	}

	public AbstractStatlessLearner<PA, Integer, SimpleAction<Integer>, Feedback<R>, SimpleStatelessPolicy<Integer, SimpleAction<Integer>>> getLearner() {
		return learner;
	}

	public void setLearner(
			AbstractStatlessLearner<PA, Integer, SimpleAction<Integer>, Feedback<R>, SimpleStatelessPolicy<Integer, SimpleAction<Integer>>> learner) {
		this.learner = learner;
	}

	public int getNumActions() {
		return numActions;
	}

	public void setNumActions(int numActions) {
		this.numActions = numActions;
	}

	public PA getParameters() {
		return parameters;
	}

	public void setParameters(PA parameters) {
		this.parameters = parameters;
	}

	public SimpleStatelessPolicy<Integer, SimpleAction<Integer>> getPolicy() {
		return policy;
	}

	public void setPolicy(
			SimpleStatelessPolicy<Integer, SimpleAction<Integer>> policy) {
		this.policy = policy;
	}

	public Feedback<R> getFeedback() {
		return feedback;
	}

	public void setFeedback(Feedback<R> feedback) {
		this.feedback = feedback;
	}
}

package edu.iastate.jrelm.rl;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.Feedback;

/**
 * Base abstract class for ReinforcementLearner classes that implement
 * algorithms that do not make use of external environmental states.
 * 
 * This class takes care of most of the ho-hum functionality that will likely be
 * common to most statless ReinforcementLearner classes. Child classes will be
 * free to focus on implementing the specifics of thier particular learning
 * algorithms.
 * 
 * @author Charles Gieseler
 *
 * @param <PA>
 *            - the type of ReinforcementLearner parameters this learner accepts
 * @param <I>
 *            - the type of identifier being used to distiguish Actions
 * @param <A>
 *            - the type of Actions this learner is working with
 * @param <F>
 *            - the type of Feedback that this learner accepts
 * @param <PO>
 *            - the type of Policy that this learner updates and uses make new
 *            Action selections
 */
public abstract class AbstractStatlessLearner<PA extends RLParameters, I, A extends Action<I>, F extends Feedback, PO extends StatelessPolicy<I, A>>
		implements ReinforcementLearner<PA, I, A, F, PO> {

	// Collects and manages parameter settings for the ReinforcementLearner
	private PA parameters = null;

	// Represents learned knowledge.
	private PO policy = null;

	// Last action chosen from the policy
	private A lastSelectedAction = null;

	// How many times this LearningEngine has been updated
	private int updateCount = 0;

	// KLUDGE: use this to keep track of the last random seed set and constantly
	// poll to see if it changes. Ugly, but better than overhaul for now
	private int lastRandSeed = 0;

	/* ************************************************************************************
	 * CONSTRUCTORS
	 * *************************************************************
	 * **********************
	 */

	/**
	 * Construct an learning component with parameters specified in a PA and the
	 * given SimpleStatelessPolicy.
	 * 
	 * Note: any random seed that is already set in the given policy will be
	 * overwritten with the seed in the given parameters.
	 * 
	 * @param learningParams
	 *            - the collection of parameter settings for the this
	 *            AbstractStatlessLearner
	 * @param aPolicy
	 *            - a SimpleStatelessPolicy
	 */
	public AbstractStatlessLearner(PA learningParams, PO aPolicy) {

		this.setParameters(learningParams);
		this.setPolicy(aPolicy);
		init();
	}

	/* ************************************************************************************
	 * INITIALIZATION
	 * ************************************************************
	 * ************************
	 */
	protected void init() {

		// KLUDGE: get the initial seed
		lastRandSeed = parameters.getRandomSeed();

		// Set the random seed
		getPolicy().setRandomSeed(lastRandSeed);

		this.updateCount = 0;

		// Initialize lastSelected action.
		this.lastSelectedAction = chooseAction();

	}

	/* ************************************************************************************
	 * CORE
	 * **********************************************************************
	 * **************
	 */

	/**
	 * This is a basic implementation of ReinforcementLearner.chooseAction().
	 * Essentially, this method simply asks the learner's StatlessPolicy for a
	 * new Action choice, updates the last Action chosen and then forwards the
	 * choice on.
	 * 
	 * IMPORTANT: It should be noted that this method also checks to see if this
	 * learner's parameters have changed and takes appropriate action.
	 * Specifically, it checks to see if the random seed has changed and, if so,
	 * updates the learner's policy with the new setting.
	 * 
	 * Child classes overriding this method should keep this in mind. If we do
	 * not poll to see if the random seed has changed here, it will not get
	 * updated in the policy and any new random seed settings will not be used.
	 * 
	 * NOTE: This will change in the future. Ideally we would like to have some
	 * kind of ActionListener structure that would allow the learner to take
	 * appropriate action whenever ANY parameter setting is changed.
	 * 
	 * @see edu.iastate.jrelm.ReinforcementLearner#chooseAction()
	 */
	public A chooseAction() {

		// KLUDGE: poll the random seed to see if it's changed
		// TODO: Random seed polling not sound, rebuild better solution.
		// Possibly make some ActionListener structure to allow learners to
		// register with thier parameter objects and be notified of changes.
		if (parameters.getRandomSeed() != lastRandSeed) {
			this.lastRandSeed = parameters.getRandomSeed();
			this.getPolicy().setRandomSeed(lastRandSeed);
		}

		A nextAction = this.getPolicy().generateAction();
		this.lastSelectedAction = nextAction;
		return nextAction;
	}

	/* ************************************************************************
	 * ACCESSOR METHODS
	 * **********************************************************************
	 */

	/**
	 * @return the number of times this ReinforcementLearner has been updated
	 *         (i.e. the number of times update(Feedback) has been called).
	 */
	public int getUpdateCount() {
		return this.updateCount;
	}

	/**
	 * Allow child classes to mark each time update(Feedback) is called.
	 */
	protected void incrementUpdateCount() {
		this.updateCount++;
	}

	/**
	 * Sets the number of times update(Feedback) has been called back to zero.
	 *
	 */
	protected void resetUpdateCount() {
		this.updateCount = 0;
	}

	/**
	 * Retrieve the Action chosen from the last call to chooseAction().
	 * 
	 * @return the last Action choice selected.
	 */
	public A getLastSelectedAction() {
		return this.lastSelectedAction;
	}

	/**
	 * @see edu.iastate.jrelm.rl.ReinforcementLearner#getParameters()
	 */
	public PA getParameters() {
		return this.parameters;
	}

	/**
	 * Note, this method will check that these parameters are valid before
	 * accepting them (PA.validate()). If the parameters fail validation, the
	 * old settings will be used and the new settings will be ignored.
	 * 
	 * @see edu.iastate.jrelm.rl.PA#validateParameters()
	 * @see edu.iastate.jrelm.rl.ReinforcementLearner#setParameters(PA)
	 */
	public void setParameters(PA newParams) {
		if (newParams.validateParameters()) {
			this.parameters = newParams;
		}
	}

	public PO getPolicy() {
		return this.policy;
	}

	public void setPolicy(PO newPolicy) {
		this.policy = newPolicy;
	}

	public int getLastRandSeed() {
		return lastRandSeed;
	}

	public void setLastRandSeed(int lastRandSeed) {
		this.lastRandSeed = lastRandSeed;
	}

	public void setLastSelectedAction(A lastSelectedAction) {
		this.lastSelectedAction = lastSelectedAction;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}
}

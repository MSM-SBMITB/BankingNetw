package edu.iastate.jrelm.rl;

import java.util.Collection;

import edu.iastate.jrelm.core.Feedback;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;
import edu.iastate.jrelm.rl.rotherev.RELearner;
import edu.iastate.jrelm.rl.rotherev.variant.VRELearner;
import edu.iastate.jrelm.rl.rotherev.variant.VREParameters;

import static java.lang.System.out;

/**
 * The SimpleStatelessLearner packages together all the core learning components
 * and a few pre-implemented reinforcement learning algorithms. It is meant to
 * provide an easy way to drop reinforcement learning capabilities into an
 * agent.
 * <p>
 * SimpleStatelessLearner is initialized with a collection of objects that
 * represent or specify actions that an agent can take. It makes no difference
 * what specific types are in the collection. The SimpleStatelessLearner just
 * chooses objects from the collection and learns which ones are best to pick
 * based on given feedback.
 * <p>
 * The desired reinforcement learning algorithm is specified by the type of
 * parameters SimpleStatelessLearner is constructed with. For example, passing
 * VREParameters to the constructor will build a SimpleStatelessLearner that
 * uses a modified version of the Roth-Erev reinforcement learning algorithm.
 * Once the learning algorithm is set through the constructor, it may not be
 * changed for the life of the SimpleStatelessLearner object, though new
 * parameters or policies may be given.
 * 
 * <p>
 * The nextAction() method is used to elicit a choice of action from
 * SimpleStatelessLearner. This will return an object from the original
 * collection given to the learner. Also, nextIndex() may be used to elicit a
 * choice in the form of an int. This int is the index of the chosen action in
 * the original collection.
 * 
 * @author Charles Gieseler
 *
 * @param <O>
 *            - The type of object being used to specify actions. This will
 *            default to the most general Oject type.
 */
public class SimpleStatelessLearner<O>
		implements
		ReinforcementLearner<RLParameters, Integer, SimpleAction<O>, Feedback<Double>, StatelessPolicy<Integer, SimpleAction<O>>> {

	// The learner that is actually used
	private ReinforcementLearner<RLParameters, Integer, SimpleAction<O>, Feedback<Double>, StatelessPolicy<Integer, SimpleAction<O>>> learner;

	/* ************************************************************************************
	 * ROTH-EREV CONSTRUCTORS
	 * ***************************************************
	 * ********************************
	 */

	/**
	 * Build a SimpleStatelessLearner that uses a the Roth-Erev algorithm.
	 * 
	 * @param actionList
	 *            - list of action choices
	 * @param parameters
	 *            - parameters for the Roth-Erev learnining algorithm
	 * 
	 * @see RELearner
	 * @see REParameters
	 */
	public SimpleStatelessLearner(REParameters params, Collection<O> actionList) {

		SimpleActionDomain<O> newDomain = new SimpleActionDomain<O>(actionList);

		REPolicy<Integer, SimpleAction<O>> rePolicy = new REPolicy<Integer, SimpleAction<O>>(
				newDomain, params.getRandomSeed());

		learner = (ReinforcementLearner) new RELearner<Integer, SimpleAction<O>>(
				params, rePolicy);
	}

	/**
	 * Build a SimpleStatelessLearner that uses the Roth-Erev algorithm with the
	 * given SimpleActionDomain.
	 * 
	 * @param actionList
	 *            - list of action choices
	 * @param parameters
	 *            - parameters for the Roth-Erev learnining algorithm
	 */
	public SimpleStatelessLearner(REParameters params,
			SimpleActionDomain<O> aDomain) {

		REPolicy<Integer, SimpleAction<O>> policy = new REPolicy<Integer, SimpleAction<O>>(
				aDomain, params.getRandomSeed());

		learner = (ReinforcementLearner) new RELearner<Integer, SimpleAction<O>>(
				params, policy);
	}

	/**
	 * Build a SimpleStatelessLearner that uses the Roth-Erev algorithm with the
	 * given REPolicy. Learning will proceed over the domain included in the
	 * REPolicy.
	 * 
	 * This can be used to start a learner with known or previously learned
	 * knowledge.
	 * 
	 * @param params
	 *            - parameters for the modified Roth-Erev learnining algorithm
	 * @param aPolicy
	 *            - an existing policy containing and ActionDomain and possibly
	 *            previously learned knowledge
	 */
	public SimpleStatelessLearner(REParameters params,
			REPolicy<Integer, SimpleAction<O>> aPolicy) {

		learner = (ReinforcementLearner) new RELearner<Integer, SimpleAction<O>>(
				params, aPolicy);
	}

	/* ************************************************************************************
	 * VARIANT ROTH-EREV CONSTRUCTORS
	 * *******************************************
	 * ****************************************
	 */

	/**
	 * Build a SimpleStatelessLearner that uses a modified version of the
	 * Roth-Erev algorithm.
	 * 
	 * @param actionList
	 *            - list of action choices
	 * @param parameters
	 *            - parameters for the modified Roth-Erev learnining algorithm
	 * 
	 * @see VRELearner
	 * @see VREParameters
	 */
	public SimpleStatelessLearner(VREParameters params, Collection<O> actionList) {

		SimpleActionDomain<O> newDomain = new SimpleActionDomain<O>(actionList);

		REPolicy<Integer, SimpleAction<O>> rePolicy = new REPolicy<Integer, SimpleAction<O>>(
				newDomain, params.getRandomSeed());

		learner = (ReinforcementLearner) new VRELearner<Integer, SimpleAction<O>>(
				params, rePolicy);
	}

	/**
	 * Build a SimpleStatelessLearner that uses a modified version of the
	 * Roth-Erev algorithm with the given SimpleActionDomain.
	 * 
	 * @param actionList
	 *            - list of action choices
	 * @param parameters
	 *            - parameters for the modified Roth-Erev learnining algorithm
	 * 
	 * @see VRELearner
	 * @see VREParameters
	 */
	public SimpleStatelessLearner(VREParameters params,
			SimpleActionDomain<O> aDomain) {

		REPolicy<Integer, SimpleAction<O>> policy = new REPolicy<Integer, SimpleAction<O>>(
				aDomain, params.getRandomSeed());

		learner = (ReinforcementLearner) new VRELearner<Integer, SimpleAction<O>>(
				params, policy);
	}

	/**
	 * Build a SimpleStatelessLearner that uses a modified version of the
	 * Roth-Erev algorithm with the given REPolicy. Learning will proceed over
	 * the domain included in the REPolicy.
	 * 
	 * This can be used to start a learner with known or previously learned
	 * knowledge.
	 * 
	 * @param params
	 *            - parameters for the modified Roth-Erev learnining algorithm
	 * @param aPolicy
	 *            - an existing policy containing and ActionDomain and possibly
	 *            previously learned knowledge
	 * 
	 * @see VRELearner
	 * @see VREParameters
	 */
	public SimpleStatelessLearner(VREParameters params,
			REPolicy<Integer, SimpleAction<O>> aPolicy) {

		learner = (ReinforcementLearner) new VRELearner<Integer, SimpleAction<O>>(
				params, aPolicy);
	}

	/* ************************************************************************************
	 * LEARNER FUNCTIONALITY
	 * ****************************************************
	 * *******************************
	 */

	/**
	 * Elicits a choice of action. The returned choice is a SimpleAction object
	 * which is wrapped around an original Object from the Collection given to
	 * the learner during its construction. This choice is made according to the
	 * reinforcement learning algorithm specified in the constructor.
	 * 
	 * @return an object from the Original action choice Collection
	 */
	public SimpleAction<O> chooseAction() {
		SimpleAction<O> newAction = (SimpleAction<O>) learner.chooseAction();
		return newAction;
	}

	/**
	 * Elicits a choice of action. The returned choice is an Object from the
	 * original Collection given to the learner during its construction. The
	 * action choice is referred to as "Raw" since it is not wrapped in a
	 * SimpleAction. This choice is made according to the reinforcement learning
	 * algorithm specified in the constructor.
	 * 
	 * @return an object from the original action collection
	 */
	public O chooseActionRaw() {
		SimpleAction<O> newAction = chooseAction();
		return newAction.getAct();
	}

	/**
	 * Elicits a choice of action. The returned choice is the index an Object
	 * from the original collection given to the learner during its
	 * construction. This choice is made according to the reinforcement learning
	 * algorithm specified in the constructor.
	 * 
	 * @return the index of an object from the original action collection
	 */
	public int chooseActionIndex() {
		SimpleAction<O> newAction = learner.chooseAction();
		int index = newAction.getID();
		return index;
	}

	/**
	 * Give the learner feedback resulting from its last choice of action. This
	 * is the "reward" value for a reinforcement learning algorithm.
	 * SimpleStatelessLearner requires feedback in the form of a Double value.
	 * 
	 * @param reward
	 *            - "reward" resulting from the learners last choice of action.
	 */
	public void update(Feedback<Double> reward) {
		learner.update(reward);
	}

	public ReinforcementLearner getEngine() {
		return learner;
	}

	public StatelessPolicy<Integer, SimpleAction<O>> getPolicy() {
		return learner.getPolicy();
	}

	/**
	 * Set the policy for this SimpleStatelessLearner. Note: the given policy
	 * must be compatible with ReinforcementLearner set through the constructor.
	 * For example, if SimpleStatelessLearner were given VREParameters through
	 * its constructor, then the internal leaner is set to VRELearner and the
	 * given policy must be of type MREPolicy.
	 * 
	 * @param newPolicy
	 *            - new policy to use
	 */
	public void setPolicy(StatelessPolicy<Integer, SimpleAction<O>> newPolicy) {
		try {
			learner.setPolicy(newPolicy);
		} catch (ClassCastException cce) {
			IllegalArgumentException iae = new IllegalArgumentException(
					"SimpleStatelessLearner.setPolicy(StatelessPolicy): Illegal policy type.\n"
							+ newPolicy.getClass().getName()
							+ " incompatible with "
							+ learner.getClass().getName());
			iae.initCause(cce);
			throw iae;
		}
	}

	/**
	 * Returns the learning parameters currently in use. Note: These will be of
	 * the same type given to SimpleStatelessLearner durring construction.
	 * 
	 * @see ReinforcementLearner#getParameters()
	 */
	public RLParameters getParameters() {
		return (RLParameters) learner.getParameters();
	}

	/**
	 * Creates an RLParameters object of the same type as given to the
	 * SimpleStatelessLearner constructor. This is intialized with default
	 * values.
	 * 
	 * @see ReinforcementLearner#makeParameters()
	 */
	public RLParameters makeParameters() {
		return learner.makeParameters();
	}

	/**
	 * Set the learning parameters to use. Note: These must be compatible with
	 * the type of ReinforcementLeaner being used internally as chosen when the
	 * SimpleStatelessLearner was created. In other words, the RLParameters
	 * given here must be of the same type as those given to the
	 * SimpleStatelessLearner constructor.
	 * 
	 * @see ReinforcementLearner#setParameters(RLParameters)
	 */
	public void setParameters(RLParameters learnParams) {
		try {
			learner.setParameters(learnParams);
		} catch (ClassCastException cce) {
			IllegalArgumentException iae = new IllegalArgumentException(
					"SimpleStatelessLearner.setParameters(RLParameters): Illegal parameters type.\n"
							+ learnParams.getClass().getName()
							+ " incompatible with "
							+ learner.getClass().getName());
			iae.initCause(cce);
			throw iae;
		}
	}

	/**
	 * @see ReinforcementLearner#getName()
	 */
	public String getName() {
		return learner.getName();
	}

}

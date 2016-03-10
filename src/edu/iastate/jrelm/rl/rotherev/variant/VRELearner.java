/*
/* Created on Jun 17, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package edu.iastate.jrelm.rl.rotherev.variant;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.core.Feedback;
import edu.iastate.jrelm.core.FeedbackDoubleValue;
import edu.iastate.jrelm.rl.rotherev.RELearner;
import edu.iastate.jrelm.rl.rotherev.variant.VREParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;

import static java.lang.System.out;

/**
 * Variant Roth-Erev Learner
 * <p>
 * This ReinforcementLearner implements a variation of the Roth-Ere'ev algorithm
 * as presented in <br>
 * James Nicolaisen, Valentin Petrov, and Leigh Tesfatsian, "Market Power and <br>
 * Efficiency in a Computational Electricity Market with Discriminatory <br>
 * Double-Auction Pricing," IEEE Transactions on Evolutionary Computation, <br>
 * Volume 5, Number 5, 2001, 504-523.
 * <p>
 * See VRELearner.experience(int, double) for more details.
 * <p>
 * In addition, this variation allows updating with given reward-action pairs.
 * See VRELearner.update(FeedbackDoubleValue, Action).
 * <p>
 * See RELearner for details on the original Roth-Erev algorithm
 * 
 * @author Charles Gieseler
 *
 */
public class VRELearner<I, A extends Action<I>> extends RELearner<I, A> {

	/* ***********************************************************************
	 * CONSTRUCTORS
	 * **********************************************************************
	 */
	/**
	 * Construct a learner using Variant Roth-Erev with parameters specified in
	 * a VREParameters object.
	 * 
	 * @param learningParams
	 *            - the collection of parameter settings for the VRELearner
	 * @param aDomain
	 *            - the ActionDomain to learn over
	 */
	public VRELearner(VREParameters learningParams, ActionDomain<I, A> aDomain) {
		super(learningParams, aDomain);
	}

	/**
	 * Construct a RothErev learning component with parameters specified in a
	 * AMREParameters object and the given policy.
	 * 
	 * Note: any random seed that is already set in the given policy will be
	 * overwritten with the seed in the given parmaters.
	 * 
	 * @param learningParams
	 *            - the collection of parameter settings for the VRELearner
	 * @param aPolicy
	 *            - a SimpleStatelessPolicy
	 */
	public VRELearner(VREParameters learningParams, REPolicy<I, A> aPolicy) {
		super(learningParams, aPolicy);
	}

	/*
	 * /***********************************************************************
	 * MODIFIED UPDATE METHODS
	 * ***********************************************************************
	 */

	/**
	 * Update the Policy according to the Variant Roth-Erev algorithm, but
	 * associate the given feedback with the given Action. Learning will proceed
	 * in accordance with the Variant Roth-Erev algorithm, however, the given
	 * Action will be treated as if it were the last one selected by the last
	 * call to chooseAction().
	 * <P>
	 * Note, the actual record of the last Action chosen will not be changed.
	 * Thus a call to getLastSelectedAction() will yield the same Action before
	 * and after a call to this update method.
	 * <P>
	 * This update method will throw an Exception if this learner does not know
	 * about the given Action. That is, an Exception will be thrown if the given
	 * Action is not in the ActionDomain that this learner is using.
	 * 
	 * @param feedback
	 *            - reward for the specified action, given as a primitive
	 *            double.
	 * 
	 * @param actionToReinforce
	 *            - update will proceed associating the given feedback with this
	 *            Action
	 */
	public void update(Feedback<Double> feedback, A actionToReinforce)
			throws Exception {

		// If we don't know about this action then complain
		if (!getPolicy().getActionDomain().containsAction(actionToReinforce)) {
			Exception e = new Exception(
					"VRELearner.update(Feedback, Action) "
							+ "received an Action that is not in the ActionDomain "
							+ "associated with this learner's Policy. This learner does"
							+ " not know about Action "
							+ actionToReinforce.getID().toString() + ".");
			throw e;
		}

		this.update(feedback.getValue().doubleValue(), actionToReinforce);
	}

	/**
	 * Convenience version of the update(Feedback, Action) method.
	 * 
	 * @see edu.iastate.jrelm.rl.rotherev.variant.VRELearner#update(Feedback,
	 *      Action)
	 * 
	 * @param feedback
	 *            - reward for the specified action, given as a primitive
	 *            double.
	 * 
	 * @param actionToReinforce
	 *            - update will proceed associating the given feedback with this
	 *            Action
	 */
	public void update(double feedback, A actionToReinforce) throws Exception {

		// If we don't know about this action then complain
		if (!getPolicy().getActionDomain().containsAction(actionToReinforce)) {
			Exception e = new Exception(
					"VRELearner.update(double, Action) "
							+ "received an Action that is not in the ActionDomain "
							+ "associated with this learner's Policy. This learner does"
							+ " not know about Action "
							+ actionToReinforce.getID().toString() + ".");

			throw e;
		}

		// Fool the standard VRELearner.update(Feedback) method to update on
		// the specified actionToReinforce by temporarily making it the last
		// action selected
		A oldLastSelectedAction = getLastSelectedAction();
		this.setLastSelectedAction(actionToReinforce);

		this.update(feedback);

		this.setLastSelectedAction(oldLastSelectedAction);
	}

	/*
	 * /***********************************************************************
	 * ACTION PROBABILITY UPDATING
	 * ***********************************************************************
	 */
	/*
	 * Updates the probability for each action to be chosen in the policy.
	 */
	protected void updateProbabilities() {
		generateBoltzmanProbs();
	}

	/*
	 * Generate action probabilities using a Boltzmann distribution with a
	 * constant temperature
	 */
	protected void generateBoltzmanProbs() {

		// Do this because the super class will try to generate probs before
		// the new parameters are in place. Check for policy just in case
		if (this.getParameters() == null || getPolicy() == null)
			return;

		double[] propensities = getPolicy().getPropensities();
		double coolingParam = this.getParameters().getBoltzmannTemp();
		double summedExps = 0;
		double newProb;

		// Sum all of the exponentials of the the propensities
		for (double prop : propensities)
			summedExps = summedExps + Math.exp((prop / coolingParam));

		// For each action calculate the associated choice probability
		// p(i) = [ e ^(q(i)/T) ] / [ Sum_over_all_j(e ^ (q(j)/T)) ]
		int index;
		for (I actID : actionIDList) {
			index = actionIDList.indexOf(actID);
			newProb = Math.exp((propensities[index] / coolingParam))
					/ summedExps;
			getPolicy().setProbability(actID, newProb);
		}
	}

	/*
	 * /***********************************************************************
	 * RESPONSE GENERATION
	 * ***********************************************************************
	 */

	/*
	 * This is an altered version of the experience function for used in the
	 * standard Roth-Erev algorithm. Like in RELearner, propensities for all
	 * actions are updated and similarity does not come into play. If the
	 * actionIndex points to the action the reward is associated with (usually
	 * the last action taken) then simply adjust the weight by the
	 * experimentation. Otherwise increase the weight of the action by a small
	 * portion of its current propensity. This puts the "Modified" in Modified
	 * Roth-Erev algorithm.
	 * 
	 * If j is the index of the last action chosen, r_j is the reward received
	 * for performing j, i is the current action being updated, q_i is the
	 * propensity for i, n is the size of the action domain and e is the
	 * experimentation parameter, then this experience function can be expressed
	 * as _ | r_j * (1-e) if i = j E(i, r_j) = | |_ q_i * (e /(n-1)) if i != j
	 */

	protected double experience(int actionIndex, double reward) {
		// System.out.println("VRELearner.exeperience(): actionIndex: "+actionIndex+" reward: "+reward);
		double experience = 0;
		double e = this.getParameters().getExperimentation();
		// out.println("	lastSelectedAction: "+actionIndex;
		// out.println("	actionIDList contains lastAction: "+
		// actionIDList.contains(actionIndex));
		int rewardedIndex = actionIDList.indexOf(getLastSelectedAction()
				.getID());
		// out.println("	rewardedIndex: "+ rewardedIndex);
		// if this action is the last a
		if (actionIndex == rewardedIndex) {
			experience = reward * (1 - e);
		} else {
			// out.println("	prop: "+policy.getPropensity(actionIndex)+
			// " experiment update: " + (e /(domainSize - 1)));
			experience = getPolicy().getPropensity(actionIndex)
					* (e / (domainSize - 1));
		}
		// out.println("	experience: "+ experience);
		return experience;
	}

	/* ***********************************************************************************
	 * ACCESSOR METHODS
	 * *********************************************************
	 * *************************
	 */

	public String getName() {
		return "Modified Roth-Erev";
	}

	@Override
	public VREParameters getParameters() {
		return (VREParameters) super.getParameters();
	}

	public VREParameters makeParameters() {
		return new VREParameters();
	}
}

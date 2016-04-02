/*
/* Created on Jun 17, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package edu.iastate.jrelm.rl.rotherev;

import java.util.ArrayList;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.core.Feedback;
import edu.iastate.jrelm.rl.AbstractStatlessLearner;
import edu.iastate.jrelm.rl.RLParameters;
import edu.iastate.jrelm.rl.ReinforcementLearner;

import static java.lang.System.out;

/**
 * 
 * Roth-Erev Learner
 * 
 * The original Roth-Erev reinforcement learning algorithm was presented by A.
 * Roth and I. Erev in <br>
 * "Learning in Extensive-Form Games: Experimental Data and Simple Dynamic <br>
 * Models in the Intermediate Term," Games and Economic Behavior, <br>
 * Special Issue: Nobel Symposium, vol. 8, January 1995, 164-212.<br>
 * and <br>
 * "Predicting How People Play Games with Unique Mixed-Strategy Equilibria," <br>
 * American Economics Review, Volume 88, 1998, 848-881.
 * <P>
 * 
 * This ReinforcementLearner implements a this later version of the algorithm
 * 
 * Implementation adapted, in part, from the RothErevLearner in the Java Auction
 * Simulator API (JASA) by Steve Phelps, Department of Computer Science,
 * University of Liverpool.
 * <P>
 * 
 * @author Charles Gieseler\
 *
 */
public class RELearner<I, A extends Action<I>>
		extends
		AbstractStatlessLearner<REParameters, I, A, Feedback<Double>, REPolicy<I, A>> {

	// Number of possible Actions according to the ActionDomain of the
	// LearingPolicy
	protected int domainSize = -1;

	/*
	 * List of action ID's in the domain. Allows us to map from propensities to
	 * Actions in the domain
	 */
	protected ArrayList<I> actionIDList;

	/* ************************************************************************************
	 * CONSTRUCTORS
	 * *************************************************************
	 * **********************
	 */

	/**
	 * Construct a RothErev learning component with parameters specified in a
	 * AREParameters object.
	 * 
	 * @param learningParams
	 *            - the collection of parameter settings for the RELearner
	 * @param aDomain
	 *            - the ActionDomain to learn over
	 */
	public RELearner(REParameters learningParams, ActionDomain<I, A> aDomain) {
		super(learningParams, new REPolicy<I, A>(aDomain));

		init();

		// Initialize the choice probabilities since we made a new policy
		updateProbabilities();
	}

	/**
	 * Construct a RothErev learning component with parameters specified in a
	 * AREParameters object and the given policy.
	 * 
	 * Note: any random seed that is already set in the given policy will be
	 * overwritten with the seed in the given parmaters.
	 * 
	 * @param learningParams
	 *            - the collection of parameter settings for the RELearner
	 * @param aPolicy
	 *            - a Roth-Erev specific policy type (REPolicy).
	 */
	public RELearner(REParameters learningParams, REPolicy<I, A> aPolicy) {
		super(learningParams, aPolicy);

		init();
	}

	/* ************************************************************************************
	 * INITIALIZATION
	 * ************************************************************
	 * ************************
	 */
	protected void init() {

		domainSize = getPolicy().getActionDomain().size();
		actionIDList = getPolicy().getActionDomain().getIDList();

		// Initialize propensity values.
		double initProp = getParameters().getInitialPropensity();

		for (I id : actionIDList)
			getPolicy().setPropensity(id, initProp);
	}

	/*
	 * /*************************************************************************
	 * ********** ACTION PROPENSITY UPDATING
	 * *************************************
	 * *********************************************
	 */
	/*
	 * Update the propensities for all Actions. The propensity for last Action
	 * chosen will be updated using the feedback value that resulted from
	 * performing the Action.
	 * 
	 * If j is the index of the last action chosen, r_j is the reward received
	 * for performing j, i is the current action being updated, q_i is the
	 * propensity for i, and phi is the recency parameter, then this update
	 * function can be expressed as
	 * 
	 * q_i = (1-phi) * q_i + E(i, r_j)
	 */
	protected void updatePropensities(double reward) {
		// System.out.println("RELearner.updatePropensities(): ");

		double phi = getParameters().getRecency();

		for (int i = 0; i < domainSize; i++) {

			// System.out.println("	"+i+ " previous: "+
			// policy.getPropensity(i));
			double carryOver = (1 - phi) * getPolicy().getPropensity(i);

			double experience = experience(i, reward);
			getPolicy().setPropensity(i, carryOver + experience);
			// propensities[i] = (1 - r) * propensities[i] + experience(i,
			// reward);

			// System.out.println(" carry over: "+carryOver+" experience: "+experience);
			// System.out.println(" new: "+policy.getPropensity(i));
		}
	}

	/*
	 * /*************************************************************************
	 * *********** RESPONSE GENERATION
	 * *******************************************
	 * *****************************************
	 */

	/*
	 * This is the standard experience function for the Roth-Erev algorithm.
	 * Here propensities for all actions are updated and similarity does not
	 * come into play. That is, all action choices are assumed to be equally
	 * similar. If the actionIndex points to the action the reward is associated
	 * with (usually the last action taken) then simply adjust the weight by the
	 * experimentation. Otherwise, adjust the weight by a smaller portion of the
	 * reward.
	 * 
	 * If j is the index of the last action chosen, r_j is the reward received
	 * for performing j, i is the current action being updated, n is the size of
	 * the action domain and e is the experimentation parameter, then this
	 * experience function can be expressed as _ | r_j * (1-e) if i = j E(i,
	 * r_j) = | |_ r_j * (e /(n-1)) if i != j
	 */

	protected double experience(int actionIndex, double reward) {
		// System.out.println("RELearner.experience(): actionIndex: "+actionIndex+" reward: "+reward);
		double experience = 0.0;
		double e = getParameters().getExperimentation();
		// out.println("	experimentation: " + e);
		// out.println("	lastSelectedAction: "+this.lastSelectedAction.getID());
		// out.println("	actionIDList contains lastAction: "+
		// actionIDList.contains(lastSelectedAction.getID()));
		int rewardedIndex = actionIDList.indexOf(getLastSelectedAction()
				.getID());
		// out.println("	rewardedIndex: "+ rewardedIndex);
		// out.println("	domainSize: " + domainSize);
		// out.println("	reward * (1-e) == " + reward * (1-e));
		// out.println("	reward * (e /(domainSize - 1)) == " + reward * (e
		// /(domainSize - 1)));
		if (actionIndex == rewardedIndex) {
			// out.println("	this action was last selected");
			experience = reward * (1 - e);
		} else {
			// out.println("	prop: "+policy.getPropensity(actionIndex)+
			// " experiment update: " + (e /(domainSize - 1)));
			experience = reward * (e / (domainSize - 1));
		}
		// out.println("	experience: "+ experience);

		return experience;
	}

	/*
	 * /*************************************************************************
	 * ********** ACTION PROBABILITY UPDATING
	 * ************************************
	 * **********************************************
	 */
	/**
	 * Updates the probability for each action to be chosen in the policy. Uses
	 * a proportional probability unless the given parameters say to use a
	 * Gibbs-Bolztmann distribution.
	 * 
	 */
	protected void updateProbabilities() {

		if (getParameters().useBoltzmann()) {
			System.out.println("RELearner. use Boltzmann");
			generateBoltzmanProbs();
		} else {
			// Proportional Probability method
			//System.out.println("RELearner. use proportional probability");
			
			double[] propensities = getPolicy().getPropensities();
			double summedProps = 0.0;
			double newProb = 0.0;

			for (double prop : propensities)
				summedProps += prop;

			int index;
			for (I actID : actionIDList) {
				index = actionIDList.indexOf(actID);
				newProb = propensities[index] / summedProps;
				getPolicy().setProbability(actID, newProb);
				//System.out.println("RE.updateProbabilities agent no " + index + " " + newProb); // tr
				
				
			}
		}
	}

	/*
	 * Generate action probabilities using a Boltzmann distribution with a
	 * constant temperature
	 */
	protected void generateBoltzmanProbs() {
		 //System.out.println("RELearner.generateBoltzmannProbs()");

		// Do this because the super class will try to generate probs before
		// the new parameters are in place. Check for policy just in case
		// TODO: What was I thinking here? Perhaps when this was in VRELearner.
		// See if this is still needed.
		if (getParameters() == null || getPolicy() == null)
			return;

		double[] propensities = getPolicy().getPropensities();
		double coolingParam = getParameters().getBoltzmannTemp();
		double summedExps = 0.0;
		double newProb;

		// Sum all of the exponentials of the the propensities
		// System.out.println("  Summing exponents");
		for (double prop : propensities) {
			summedExps = summedExps + Math.exp((prop / coolingParam));
			// System.out.println("	prop: " + prop + "  exp:" +
			// Math.exp((prop/coolingParam)) + "  sum: " + summedExps);
		}

		// System.out.println("	propensities.length: " + propensities.length);

		// For each action calculate the associated choice probability
		// p(i) = [ e ^(q(i)/T) ] / [ Sum_over_all_j(e ^ (q(j)/T)) ]
		int index;
		for (I actID : actionIDList) {

			index = actionIDList.indexOf(actID);
			newProb = Math.exp((propensities[index] / coolingParam))
					/ summedExps;
			// System.out.println("  for index: " + index);
			// System.out.println("	propensity: " + propensities[index]);
			// System.out.println("	top: " +
			// Math.exp(propensities[index]/coolingParam));
			// System.out.println("	summedExps: " + summedExps);
			// System.out.print("	previous: " + policy.getProbability(actID));
			getPolicy().setProbability(actID, newProb);
			// System.out.println("  new: " + policy.getProbability(actID));
		}
	}

	/* **********************************************************************************
	 * MISCELLANEOUS METHODS
	 * ****************************************************
	 * *****************************
	 */

	/**
	 * This activates the learning process according to the modified Roth-Erev
	 * learning algorithm. Feedback is interpreted as reward for the last Action
	 * chosen by this engine. Entries in the policy associated with this Action
	 * are updated accordingly.
	 * <P>
	 * This algorithm expects feedback to be given as a Double.
	 * 
	 * @param feedback
	 *            - reward for the specified action
	 *
	 * @see edu.iastate.jrelm.rl.ReinforcementLearner
	 */
	public void update(Feedback<Double> feedback) {
		this.update(feedback.getValue().doubleValue());
	}

	/**
	 * Convenience version of the update(FeedbackDoubleValue) method
	 * 
	 * @param feedback
	 *            - reward for the specified action, given as a primitive
	 *            double.
	 *
	 * @see edu.iastate.jrelm.rl.ReinforcementLearner
	 */
	public void update(double feedback) {
		updatePropensities(feedback);
		updateProbabilities();
		incrementUpdateCount();
	}

	/**
	 * Clear all learned knowledge. The Action propensities are set to the
	 * current initial value and the probability values in the policy.
	 *
	 */
	public void reset() {
		init();
	}

	/* ************************************************************************
	 * ACCESSOR METHODS
	 * **********************************************************************
	 */

	/**
	 * Retreive the initial propensity value.
	 * 
	 * @return initial propesisty
	 */
	public double getInitialPropensity() {
		return getParameters().getInitialPropensity();
	}

	/**
	 * Set the initial propensity value.
	 * 
	 * @param initProp
	 */
	public void setInitialPropensityValue(double initProp) {
		getParameters().setInitialPropensity(initProp);
	}

	public String getName() {
		return "Roth-Erev";
	}

	/**
	 * Returns the parameters currently being used by this RELearner. Returned
	 * parameters are of type REParameters
	 * 
	 * @return the REParameters for this learner
	 * 
	 * @see ReinforcementLearner#getParameters();
	 */
	public REParameters getParameters() {
		return super.getParameters();
	}

	/**
	 * @see ReinforcementLearner#makeParameters()
	 */
	public REParameters makeParameters() {
		return new REParameters();
	}

	/**
	 * Set the parameters for this RELearner. Requires REParamters
	 * 
	 * @param params
	 *            - learning parameters of type REParameters
	 * 
	 * @see ReinforcementLearner#setParameters(RLParameters)
	 */
	// public void setParameters(RLParameters params){
	// parameters = (REParameters) params;
	// }

	/**
	 * @see edu.iastate.jrelm.rl.ReinforcementLearner#getPolicy()
	 */
	public REPolicy<I, A> getPolicy() {
		return super.getPolicy();
	}

	/**
	 * @see edu.iastate.jrelm.rl.ReinforcementLearner#setPolicy(edu.iastate.jrelm.rl.StatelessPolicy)
	 */
	public void setPolicy(REPolicy<I, A> newPolicy) {
		super.setPolicy(newPolicy);
		domainSize = getPolicy().getActionDomain().size();
		actionIDList = getPolicy().getActionDomain().getIDList();
	}

}

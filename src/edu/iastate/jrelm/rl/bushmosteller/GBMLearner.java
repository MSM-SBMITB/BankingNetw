package edu.iastate.jrelm.rl.bushmosteller;

/**
 * 
 * A ReinforcementLearner class that implements Generarlized Bush-Mosteller 
 * learning.
 * 
 * 
 * Note, classes in JReLM implementing Bush-Mosteller learning models are 
 * based on Th. Brenner's descriptions in "Agent Learning Representations," 
 * Chapter 18 of The Handbook of Computational Economics, vol 2. 
 * 
 * See:
 *    Leigh Tesfatsion , Kenneth L. Judd, Handbook of Computational Economics, 
 *    Volume 2: Agent-Based Computational Economics (Handbook of Computational 
 *    Economics), North-Holland Publishing Co., Amsterdam, The Netherlands, 2006
 * 
 * @author Charles Gieseler
 */
import java.util.ArrayList;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.Feedback;
import edu.iastate.jrelm.rl.AbstractStatlessLearner;
import edu.iastate.jrelm.rl.SimpleStatelessPolicy;

/**
 * An implementation of the Generalized Bush-Mosteller reinforcement learning
 * module.
 * 
 * @author Charles Gieseler
 *
 * @param <I>
 * @param <A>
 */
public class GBMLearner<I, A extends Action<I>>
		extends
		AbstractStatlessLearner<GBMParameters, I, A, Feedback<Double>, SimpleStatelessPolicy<I, A>> {

	private GBMParameters parameters;

	private SimpleStatelessPolicy<I, A> policy;

	public GBMLearner(GBMParameters learningParams,
			SimpleStatelessPolicy<I, A> aPolicy) {
		super(learningParams, aPolicy);
	}

	/**
	 * Implements the core learning function according to the Generalised
	 * Bush-Mosteller model.
	 * 
	 */
	public void update(Feedback<Double> reward) {

		ArrayList<I> idList = getPolicy().getActionDomain().getIDList();
		I lastChosen = getLastSelectedAction().getID();

		double currentProbability = 0.0;
		double modifiedReward = 0.0;
		double adjustmentValue = 0.0;
		double newProbability = 0.0;

		if (reward.getValue() >= 0.0) {
			// If the reward is positive...
			modifiedReward = parameters.feedbackModifier(reward.getValue());

			for (I actionID : idList) {
				currentProbability = getPolicy().getProbability(actionID);

				if (actionID == lastChosen) {
					adjustmentValue = modifiedReward * (1 - currentProbability);
				} else {
					adjustmentValue = -modifiedReward * (currentProbability);
				}

				newProbability = currentProbability + adjustmentValue;
				getPolicy().setProbability(actionID, newProbability);
			}
		} else {
			modifiedReward = parameters
					.feedbackModifier(-1 * reward.getValue());

			for (I actionID : idList) {
				currentProbability = getPolicy().getProbability(actionID);

				if (actionID == lastChosen) {
					adjustmentValue = -modifiedReward * currentProbability;
				} else {
					adjustmentValue = modifiedReward
							* ((currentProbability * getPolicy()
									.getProbability(lastChosen)) / (1 - getPolicy()
									.getProbability(lastChosen)));
				}

				newProbability = currentProbability + adjustmentValue;
				getPolicy().setProbability(actionID, newProbability);
			}
		}
	}

	/* ************************************************************************
	 * ACCESSOR METHODS
	 * **********************************************************************
	 */

	public GBMParameters getParameters() {
		return super.getParameters();
	}

	/**
	 * The implementation of Generalised Bush-Mosteller learning is partially
	 * distributed between the GBMLearnerTest and a specific child of
	 * GBMParameters. As such, the parameters returned here will determine a
	 * specific variation of Generalised Bush-Mosteller learning.
	 * 
	 * In particular a new LinearGBMParameters is returned. Using these
	 * parameters will result in Generalised Bush-Mosteller learning based on a
	 * linear feedback modificatier function.
	 * 
	 * Certain assumptions will be made in intializing these parameters. A
	 * multiplier value of v = 1.0 will be used for the feedback modifier
	 * function v(r) = v * r. The minimum and maximum expected feedback values
	 * will be set to 0.0 an 1.0 respectively.
	 * 
	 */
	public GBMParameters makeParameters() {
		LinearGBMParameters newParams = new LinearGBMParameters();
		newParams.setMultiplierValue(1.0);
		newParams.setFeedbackRange(0.0, 1.0);

		return newParams;
	}

	public void setParameters(GBMParameters params) {
		this.parameters = params;
	}

	public SimpleStatelessPolicy<I, A> getPolicy() {
		return policy;
	}

	public void setPolicy(SimpleStatelessPolicy<I, A> newPolicy) {
		policy = newPolicy;
	}

	public String getName() {
		return "Generalised Bush-Mosteller";
	}

}

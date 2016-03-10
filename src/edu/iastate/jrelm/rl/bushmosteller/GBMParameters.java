package edu.iastate.jrelm.rl.bushmosteller;

import edu.iastate.jrelm.rl.RLParameters;

/**
 * This is an abstract class to guide the implementation of Generalized
 * Bush-Mosteller learning parameters. Specifically, GeneralizedBMParameters
 * child classes should specifiy a reinforcement strength modifier function that
 * will be used in updating Policies. This is done by implementing the abstract
 * feedbackModifier(double) function. In effect, we are distributing the
 * implementation of Generalized Bush-Mosteller learning algorithms accross the
 * parameters and learner objects.
 * 
 * See the documentation of feedbackModifier() for guidance and constraints on
 * implemention.
 * 
 * @see this{@link #feedbackModifier(double)}
 * 
 *      Note, classes in JReLM implementing Bush-Mosteller learning models are
 *      based on Th. Brenner's descriptions in "Agent Learning Representations,"
 *      Chapter 18 of The Handbook of Computational Economics, vol 2.
 * 
 *      See: Leigh Tesfatsion , Kenneth L. Judd, Handbook of Computational
 *      Economics, Volume 2: Agent-Based Computational Economics (Handbook of
 *      Computational Economics), North-Holland Publishing Co., Amsterdam, The
 *      Netherlands, 2006
 * 
 * @author Charles Gieseler
 * 
 */
public abstract class GBMParameters implements RLParameters {

	private int randomSeed = 0;

	public GBMParameters() {
		super();
	}

	/**
	 * Bush-Mosteller learning uses a function v(r) in updating action choice
	 * selection probabilities. Here r is a reinforcement strength value
	 * resulting from the last action chosen. Essentially v(r) yields a modified
	 * feedback value, associated with the last selected action choice, that is
	 * then used in updating a Policy according to generalize Bush-Mosteller
	 * learning.
	 * 
	 * Note, v(r) must be monotonically increasing in r, for r > 0, where v(0) =
	 * 0 and 0 <= v(r) <= 1.
	 * 
	 * @param reinforcementStrength
	 * @return modified reinforcement strength
	 */
	public abstract double feedbackModifier(double reinforcementStrength);

	/**
	 * This method should be implememted in child classes to validate current
	 * parameter settings.
	 */
	public abstract boolean validateParameters();

	public abstract String getName();

	public String[] getParameterNames() {

		return new String[] { "ReinforcementStrengthModifierFunction" };
	}

	public void setRandomSeed(int newSeed) {
		this.randomSeed = newSeed;
	}

	public int getRandomSeed() {
		return randomSeed;
	}

}

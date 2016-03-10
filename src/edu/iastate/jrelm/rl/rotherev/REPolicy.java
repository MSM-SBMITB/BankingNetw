package edu.iastate.jrelm.rl.rotherev;

import cern.jet.random.engine.RandomEngine;
import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.rl.SimpleStatelessPolicy;

public class REPolicy<I, A extends Action> extends SimpleStatelessPolicy<I, A> {

	// Each possible action has a propensity associated with it. Essentially the
	// likelyhood that each action will be chosen
	protected double[] propensities;

	public REPolicy(ActionDomain<I, A> actionDomain) {
		super(actionDomain);
		init();
	}

	public REPolicy(ActionDomain<I, A> actionDomain, int randSeed) {
		super(actionDomain, randSeed);
		init();
	}

	public REPolicy(ActionDomain<I, A> actionDomain, RandomEngine randomGen) {
		super(actionDomain, randomGen);
		init();
	}

	public REPolicy(ActionDomain<I, A> actionDomain, double[] initProbs) {
		super(actionDomain, initProbs);
		init();
	}

	public REPolicy(ActionDomain<I, A> actionDomain, double[] initProbs,
			int randSeed) throws IllegalArgumentException {
		super(actionDomain, initProbs, randSeed);
		init();
	}

	public REPolicy(ActionDomain<I, A> actionDomain, double[] initProbs,
			RandomEngine randomGen) throws IllegalArgumentException {
		super(actionDomain, initProbs, randomGen);
		init();
	}

	protected void init() {
		super.init();
		propensities = new double[getActionDomain().size()];
	}

	public double getPropensity(I id) {
		int index = super.actionIDList.indexOf(id);

		try {
			return propensities[index];
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new IllegalArgumentException(
					"REPolicy.getPropesity(I): illegal index " + index
							+ ". No such propensity value in this policy.",
					aioobe);
		}
	}

	public double getPropensity(int i) {
		double prop;
		try {
			return propensities[i];
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new IllegalArgumentException(
					"REPolicy.getPropesity(int): illegal index " + i
							+ ". No such propensity value in this policy.",
					aioobe);
		}
	}

	public void setPropensity(I id, double prop) {
		int index = super.actionIDList.indexOf(id);
		try {
			propensities[index] = prop;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new IllegalArgumentException(
					"REPolicy.setPropesity(I, double): illegal index " + index
							+ ". No such propensity value in this policy.",
					aioobe);
		}
	}

	public void setPropensity(int i, double prop) {
		try {
			propensities[i] = prop;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new IllegalArgumentException(
					"REPolicy.setPropesity(int, double): illegal index " + i
							+ ". No such propensity value in this policy.",
					aioobe);
		}
	}

	public double[] getPropensities() {
		return propensities;
	}

	public void setPropensities(double[] props) {
		if (props.length != getActionDomain().size()) {
			throw new IllegalArgumentException(
					"REPolicy.setPropensities(double[]): \n"
							+ "Number of propensity values does not match the number of Actions in "
							+ "this policy's ActionDomain.");
		}
		propensities = props;
	}

	public double[] getProbabilities() {
		return probDistFunction;
	}
}

/*
 * Created on Jun 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.iastate.jrelm.rl;

import java.util.ArrayList;

import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.util.SimpleEventGenerator;

/**
 * A simple implementation of the StatelessPolicy interface. This is essentially
 * a discrete probability distribution governing the choice of Action from a
 * given ActionDomain, irrespective of the state of the world. To be used with a
 * ReinforcementLearner implementing a stateless algorithm.
 * 
 * 
 * @author Charles Gieseler
 *
 * @param <I>
 *            - the type used to identify Actions.
 * @param <A>
 *            - the type of Action used.
 * 
 */

public class SimpleStatelessPolicy<I, A extends Action> extends
		AbstractStatelessPolicy<I, A> {

	private int randSeed;

	/**
	 * Here a probability distribution function (pdf) is an array of probability
	 * values. When used in conjuction with the eventGenerator, a value
	 * indicates the likelihood that its index will be selected.
	 * 
	 * Each Action has an ID and each Action ID has an index in the list of IDs
	 * kept by the ActionDomain. The corresponding index in this probability
	 * distribution function contains a probability value for that ID. So we
	 * have a mapping from probabilities to Action IDs and from Action IDs to
	 * Actions. This allows the evenGenerator to use the pdf to select Actions
	 * from the ActionDomain according to the specified probability
	 * distribution.
	 * 
	 * The probability values are modified by a ReinforcementLearner according
	 * to the implemented learning algorithm.
	 */
	protected double[] probDistFunction;

	// Random number eventGenerator needed for the randomEngine event
	// eventGenerator
	protected RandomEngine randomEngine;

	/*
	 * Generates randomEngine events (action selections) according to the
	 * probabilities over all actions in the ActionDomain. Probabilities are
	 * maintained in probDistFunction.
	 */
	// protected ModifiedEmpiricalWalker eventGenerator;
	protected SimpleEventGenerator eventGenerator;

	/*
	 * The collection of possible Actions an agent is allowed to perform.
	 */
	protected ActionDomain<I, A> domain;

	/*
	 * List of action ID's in the domain. Allows us to map from int values
	 * chosen given by the event generator to actions in the domain.
	 */
	protected ArrayList<I> actionIDList;

	// Records the last action choosen by this policy.
	protected A lastAction;

	/* ***************************************************************
	 * CONSTRUCTORS
	 * **************************************************************
	 */

	/**
	 * Construct a SimpleStatelessPolicy using a given ActionDomain. Note, this
	 * policy requires a finite domain of discrete actions.
	 * 
	 * A new MersenneTwister seeded with the current time
	 * ((int)System.currentTimeMillis()) is created as the RandomEngine for this
	 * policy. Note: If creating multiple SimpleStatelessPolicies and
	 * MersenneTwister is the desired RandomEngine, it will be more efficient to
	 * create a single MersenneTwister and pass it to each new policy as it is
	 * constructed.
	 * 
	 * @see cern.jet.random.engine.MersenneTwister
	 *
	 * @param actionList
	 *            - the collection of possible actions this policy learns over
	 */
	public SimpleStatelessPolicy(ActionDomain<I, A> actionDomain) {
		domain = actionDomain;
		randSeed = (int) System.currentTimeMillis();
		randomEngine = new MersenneTwister(randSeed);

		// Initialize to uniform distribution
		int numActions = actionDomain.size();
		probDistFunction = new double[numActions];
		for (int i = 0; i < numActions; i++)
			probDistFunction[i] = 1.0 / ((double) numActions);

		// System.out.println("SimpleStatelessLearner: ");
		// System.out.println("	probDistFunction[5]: " + probDistFunction[5]);
		// System.out.println("	1.0/((double)numActions): "
		// +1.0/((double)numActions) );

		init();
	}

	/**
	 * Construct a SimplePolicy using the given ActionDomain and psuedo-random
	 * generator seed.
	 * 
	 * A new MersenneTwister seeded with the given seed value is created as the
	 * RandomEngine for this policy. Note: If creating multiple
	 * SimpleStatelessPolicies and MersenneTwister is the desired RandomEngine,
	 * it will be more efficient to create a single MersenneTwister and pass it
	 * to each new policy as it is constructed.
	 * 
	 * @see cern.jet.random.engine.MersenneTwister
	 * 
	 * @param aDomain
	 *            - the collection of possible Actions
	 * @param sDomain
	 *            - the collection of possible States
	 * @param randSeed
	 *            - seed value for the random generator used in this policy
	 */
	public SimpleStatelessPolicy(ActionDomain<I, A> actionDomain, int randSeed) {
		domain = actionDomain;
		this.randSeed = randSeed;
		randomEngine = new MersenneTwister(this.randSeed);

		// Initialize to uniform distribution
		int numActions = actionDomain.size();
		probDistFunction = new double[numActions];
		for (int i = 0; i < numActions; i++)
			probDistFunction[i] = 1.0 / ((double) numActions);

		init();
	}

	/**
	 * Construct a SimpleStatelessPolicy using a given ActionDomain and
	 * RandomEngine. Note, this policy requires a finite domain of discrete
	 * actions. The policy will use the given RandomEngine in selecting new
	 * choices.
	 * 
	 * Note: RandomGenerator does not reveal the seed value being used. As such,
	 * SimpleStatelessPolicy has no way of knowing what seed is being used when
	 * a pre-constructed generator is supplied. SimpleStatlessPolicy will set
	 * the seed -1 and report this value when getRandomSeed() is called.
	 * 
	 *
	 * @param actionList
	 *            - the collection of possible actions this policy learns over
	 * @param randomGen
	 *            - the RandomEngine this policy will use
	 */
	public SimpleStatelessPolicy(ActionDomain<I, A> actionDomain,
			RandomEngine randomGen) {
		domain = actionDomain;
		randSeed = -1;
		randomEngine = randomGen;

		// Initialize to uniform distribution
		int numActions = actionDomain.size();
		probDistFunction = new double[numActions];
		for (int i = 0; i < numActions; i++)
			probDistFunction[i] = 1.0 / ((double) numActions);

		init();
	}

	/**
	 * 
	 * A new MersenneTwister seeded with the current time
	 * ((int)System.currentTimeMillis()) is created as the RandomEngine for this
	 * policy. Note: If creating multiple SimplePolicies and MersenneTwister is
	 * the desired RandomEngine, it will be more efficient to create a single
	 * MersenneTwister and pass it to each new policy as it is constructed.
	 * 
	 * @see cern.jet.random.engine.MersenneTwister
	 */
	public SimpleStatelessPolicy(ActionDomain<I, A> actionDomain,
			double[] initProbs) {
		domain = actionDomain;
		randSeed = (int) System.currentTimeMillis();
		randomEngine = new MersenneTwister(randSeed);

		if (initProbs.length != domain.size())
			throw new IllegalArgumentException(
					"Cannot initialize policy with given initial"
							+ " probabilities.\n Expected " + domain.size()
							+ " values, but received " + initProbs.length
							+ ". Using default uniform distribution.");

		probDistFunction = initProbs;

		init();
	}

	public SimpleStatelessPolicy(ActionDomain<I, A> actionDomain,
			double[] initProbs, int randSeed) throws IllegalArgumentException {
		domain = actionDomain;
		this.randSeed = randSeed;
		randomEngine = new MersenneTwister(this.randSeed);

		if (initProbs.length != domain.size())
			throw new IllegalArgumentException(
					"Cannot initialize policy with given initial"
							+ " probabilities.\n Expected " + domain.size()
							+ " values, but received " + initProbs.length
							+ ". Using default uniform distribution.");

		probDistFunction = initProbs;
		init();
	}

	/**
	 * 
	 * Note: RandomGenerator does not reveal the seed value being used. As such,
	 * SimpleStatelessPolicy has no way of knowing what seed is being used when
	 * a pre-constructed generator is supplied. SimpleStatlessPolicy will set
	 * the seed -1 and report this value when getRandomSeed() is called.
	 * 
	 * @param actionDomain
	 * @param initProbs
	 * @param randomGen
	 * @throws IllegalArgumentException
	 */
	public SimpleStatelessPolicy(ActionDomain<I, A> actionDomain,
			double[] initProbs, RandomEngine randomGen)
			throws IllegalArgumentException {
		domain = actionDomain;
		this.randSeed = -1;
		randomEngine = randomGen;

		if (initProbs.length != domain.size())
			throw new IllegalArgumentException(
					"Cannot initialize policy with given initial"
							+ " probabilities.\n Expected " + domain.size()
							+ " values, but received " + initProbs.length
							+ ". Using default uniform distribution.");

		probDistFunction = initProbs;

		init();
	}

	/* ***************************************************************
	 * INITIALIZATION
	 * **************************************************************
	 */
	protected void init() {
		actionIDList = domain.getIDList();
		// eventGenerator = new ModifiedEmpiricalWalker(probDistFunction,
		// ModifiedEmpiricalWalker.NO_INTERPOLATION, randomEngine);
		eventGenerator = new SimpleEventGenerator(probDistFunction,
				randomEngine);

		// Need to init the lastAction to something. Choose a random action
		lastAction = generateAction();
	}

	/* ***************************************************************
	 * MISCELLANEOUS
	 * **************************************************************
	 */

	/**
	 * Choose an Action according to the current probability distribution
	 * function.
	 * 
	 * @return a new Action
	 */
	public A generateAction() {
		int chosenIndex;
		I chosenID;
		A chosenAction;

		// Pick the index of an action. Note: indexes start at 0
		chosenIndex = eventGenerator.nextEvent();
		chosenID = actionIDList.get(chosenIndex);
		chosenAction = domain.getAction(chosenID);

		lastAction = chosenAction;

		return chosenAction;
	}

	/**
	 * Reset this policy. Reverts to a uniform probability distribution over the
	 * domain of actions. This only modifies the probability distribution. It
	 * does not reset the RandomEngine.
	 * 
	 * WARNING: This will effectivlely erase all learned Action probabilities in
	 * this policy.
	 *
	 */
	public void reset() {
		int numActions = domain.size();
		for (int i = 0; i < numActions; i++)
			probDistFunction[i] = 1.0 / ((double) numActions);
		init();
	}

	/* ***************************************************************
	 * ACCESSOR METHODS
	 * **************************************************************
	 */
	/**
	 * Retrieve the probability distribution used in selecting actions from the
	 * action domain.
	 * 
	 * @return the current probability distribution over the domain of actions.
	 */
	public double[] getDistribution() {
		return probDistFunction;
	}

	/**
	 * Set the probability distribution used in selecting actions from the
	 * action domain. The distribution is given as an array of doubles.
	 * 
	 * Note: there should be a value for each Action in this policy's
	 * ActionDomain. Each value is associated with the Action
	 * 
	 * 
	 * @param distrib
	 *            - the new collection of action choice probabilities
	 */
	public void setDistribution(double[] distrib)
			throws IllegalArgumentException {
		if (distrib.length == domain.size()) {
			probDistFunction = distrib;
			// eventGenerator.setState(probDistFunction,
			// ModifiedEmpiricalWalker.NO_INTERPOLATION);
			// eventGenerator.setState2(probDistFunction);
			eventGenerator.setState(probDistFunction);
		} else {
			throw new IllegalArgumentException(
					"Cannot set given probability values." + " Expected "
							+ domain.size() + " values, but received "
							+ distrib.length
							+ ". Previous values will be used.");
		}
	}

	/**
	 * Get the set of actions this policy uses.
	 * 
	 * @see edu.iastate.jrelm.rl.StatelessPolicy#getDomain()
	 */
	public ActionDomain<I, A> getActionDomain() {
		return domain;
	}

	/**
	 * Retrieve the number of possible actions in the DiscreteFiniteDomain for
	 * this policy.
	 * 
	 * @return size of the ActionDomain
	 */
	public int getNumActions() {
		return domain.size();
	}

	/**
	 * Get the last action chosen by this policy.
	 * 
	 * @see edu.iastate.jrelm.rl.StatelessPolicy#getLastAction()
	 */
	public A getLastAction() {
		return lastAction;
	}

	/**
	 * Gets the current probability of choosing an action. Parameter actionIndex
	 * indicates which action in the policy's domain to lookup. If the given
	 * actionIndex is not within the bounds of the domain, Not A Number is
	 * returned (Double.NaN).
	 * 
	 * @param actionIndex
	 *            - int indicator of which action to look up
	 * @return the probability of the specified Action. Double.NaN if action
	 *         index is out of bounds for the domain of actions.
	 */
	public double getProbability(I actionID) {
		int index = actionIDList.indexOf(actionID);
		if ((index >= 0) && (index < probDistFunction.length))
			return probDistFunction[index];
		else
			return Double.NaN;
	}

	/**
	 * Updates the probability of choosing the indicated Action
	 * 
	 * @param actionID
	 *            - the index of the desired Action in this policy's
	 *            ActionDomain.
	 * @param newValue
	 *            - new choice probability value to associate with this action.
	 */
	public void setProbability(I actionID, double newValue) {
		int index = actionIDList.indexOf(actionID);
		if (index >= 0 && index < domain.size()) {
			probDistFunction[index] = newValue;
			// eventGenerator.setState(probDistFunction,
			// ModifiedEmpiricalWalker.NO_INTERPOLATION);
			// eventGenerator.setState2(probDistFunction);
			eventGenerator.setState(probDistFunction);
			
			
			//TR
			//System.out.println("SimpleStatelessPolicy.setProbability index |  "+ index + " new value | " + newValue);
			
		} else {
			throw new IllegalArgumentException(
					"Cannot set probability for action "
							+ actionID
							+ ". Action ID is not valid for this policy's ActionDomain.");
		}
	}

	/**
	 * Sets the RandomEngine to be used by this policy.
	 * 
	 * @param engine
	 */
	public void setRandomEngine(RandomEngine engine) {
		randomEngine = engine;
		eventGenerator.setRandomEngine(randomEngine);
	}

	public int getRandomSeed() {
		return randSeed;
	}

	/**
	 * Resets the RandomEngine, initializing it with the given seed. The
	 * RandomEngine will be set to a MersenneTwister. If you wish to use a
	 * different RandomEngine with this seed, use setRandomEngine(RandomEngine).
	 * 
	 * Note: Calling this method will create a new MersenneTwister. Repeated
	 * calls can lead to perfomance issues.
	 * 
	 * @see MersenneTwister cern.jet.random.engine.MersenneTwister
	 * 
	 * @param seed
	 *            - seed value
	 */
	public void setRandomSeed(int seed) {
		randSeed = seed;
		setRandomEngine(new MersenneTwister(seed));
	}

}

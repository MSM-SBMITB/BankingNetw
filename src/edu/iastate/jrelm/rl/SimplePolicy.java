package edu.iastate.jrelm.rl;

import java.util.ArrayList;

import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.core.State;
import edu.iastate.jrelm.core.StateDomain;
import edu.iastate.jrelm.util.ModifiedEmpiricalWalker;

/**
 * A simple implementation of the Policy interface. This is essentially manages
 * discrete probability distributions governing the choice of action based on
 * the current state of the world. An action choice is represented as one Action
 * object from a given ActionDomain. Similarly, the state of the world is
 * represented as one State object from a given StateDomain.
 * 
 * This is to be used with a ReinforcementLearner, which is responsible for
 * updating this policy according to the reinforcement learning algorithm
 * implemented.
 * 
 * @author Charles Gieseler
 *
 * @parma <SI> - the type of State identifier used.
 * @param <S>
 *            - the type of State managed by the given StateDomain.
 * @param <AI>
 *            - the type of Action identifier used.
 * @param <A>
 *            - the type of Action contained in the given ActionDomain
 * 
 */

public class SimplePolicy<AI, A extends Action, SI, S extends State> implements
		Policy<AI, A, SI, S> {

	/*
	 * pdfs maintains probability values for State-Action pairs.
	 * 
	 * Here a probability distribution function (pdf) is an array of probability
	 * values. When used in conjuction with the eventGenerator, a value
	 * indicates the likelihood that its index will be selected.
	 * 
	 * Each Action has an ID and each Action ID has an index in the list of IDs
	 * kept by the ActionDomain. The corresponding index in this probability
	 * distribution function contains a probability value for that ID. So we
	 * have a mapping from probabilities to Action IDs and from Action IDs to
	 * Actions. This allows the eventGenerator to use the pdf to select Actions
	 * from the domain according to the specified probability distribution.
	 * 
	 * Since the likelihood for choosing an action depends on the current State
	 * of the world, a separate pdf is maintained for each State. Like Actions,
	 * States have IDs and each State ID has an index in a list maintained by
	 * the StateDomain. The corresponding index in the pdfs array contains the
	 * action pdf for that State kept by the StateDomain.
	 * 
	 * The probability values are modified by a ReinforcementLearner according
	 * to the implemented learning algorithm.
	 * 
	 * Elements are accessed by [stateIndex][actionIndex].
	 */
	protected double[][] pdfs;

	// Random number eventGenerator needed for the randomEngine event
	// eventGenerator
	protected RandomEngine randomEngine;

	/*
	 * Generates randomEngine events (action selections) according to the
	 * probability probDistFunction over the domain of actions.
	 */
	protected ModifiedEmpiricalWalker eventGenerator;

	/*
	 * The collection of possible Actions that an agent may perform.
	 */
	protected ActionDomain<AI, A> actionDomain;

	/*
	 * The collection of possible States that an agent may encounter in a
	 * simulation environment.
	 */
	protected StateDomain<SI, S> stateDomain;

	/*
	 * List of action ID's in the ActionDomain. Allows us to map from int values
	 * chosen given by the event generator to actions in the domain.
	 */
	protected ArrayList<AI> actionIDList;

	/*
	 * List of action ID's in the domain. Allows us to map from int values
	 * chosen given by the event generator to actions in the domain.
	 */
	protected ArrayList<SI> stateIDList;

	// Records the last action choosen by this policy.
	protected A lastAction;

	// The seed for the random number generator
	protected int randSeed;

	/* ***************************************************************
	 * CONSTRUCTORS
	 * **************************************************************
	 */

	/**
	 * Construct a SimplePolicy using a given ActionDomain and StateDomain.
	 * Note, this policy requires a the finite, disctrete action and state
	 * domains.
	 * 
	 * A new MersenneTwister seeded with the current time
	 * ((int)System.currentTimeMillis()) is created as the RandomEngine for this
	 * policy. Note: If creating multiple SimplePolicies and MersenneTwister is
	 * the desired RandomEngine, it will be more efficient to create a single
	 * MersenneTwister and pass it to each new policy as it is constructed.
	 * 
	 * @see cern.jet.random.engine.MersenneTwister
	 *
	 * @param aDomain
	 *            - the collection of possible Actions
	 * @param sDomain
	 *            - the collection of possible States
	 */
	public SimplePolicy(ActionDomain<AI, A> aDomain, StateDomain<SI, S> sDomain) {
		this(aDomain, sDomain, ((int) System.currentTimeMillis()));
	}

	/**
	 * Construct a SimplePolicy using a given ActionDomain, StateDomain and
	 * psuedo-random generator seed. A new MersenneTwister seeded with the given
	 * seed value is created as the RandomEngine for this policy.
	 * 
	 * Note: If creating multiple SimplePolicies and MersenneTwister is the
	 * desired RandomEngine, it will be more efficient to create a single
	 * MersenneTwister and pass it to each new policy as it is constructed.
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
	public SimplePolicy(ActionDomain<AI, A> aDomain,
			StateDomain<SI, S> sDomain, int randSeed) {
		this(aDomain, sDomain, ((RandomEngine) new MersenneTwister(randSeed)));
		actionDomain = aDomain;
		stateDomain = sDomain;

		randomEngine = new MersenneTwister(randSeed);

		// Initialize to uniform distribution
		int numActions = actionDomain.size();
		int numStates = stateDomain.size();
		pdfs = new double[numStates][numActions];

		for (int i = 0; i < numStates; i++) {
			for (int j = 0; j < numActions; j++)
				pdfs[i][j] = 1.0 / ((double) numActions);
		}

		init();
	}

	/**
	 * Construct a SimplePolicy using a given ActionDomain and RandomEngine.
	 * Note, this policy requires a finite domain of discrete actions. A new
	 * MersenneTwister seeded with the current time
	 * ((int)System.currentTimeMillis()) is created as the RandomEngine for this
	 * policy.
	 *
	 * @param aDomain
	 *            - the collection of possible Actions
	 * @param sDomain
	 *            - the collection of possible States
	 * @param randomGen
	 *            - the RandomEngine to use. This policy employs a
	 *            ModifiedEmpiricalWalker in selecting Actions, which in turn
	 *            uses a RandomEngine.
	 * 
	 * @see RandomEngine
	 * @see ModifiedEmpiricalWalker
	 * 
	 */
	public SimplePolicy(ActionDomain<AI, A> aDomain,
			StateDomain<SI, S> sDomain, RandomEngine randomGen) {
		actionDomain = aDomain;
		stateDomain = sDomain;
		randomEngine = randomGen;

		// Initialize to uniform distribution
		int numActions = actionDomain.size();
		int numStates = stateDomain.size();
		pdfs = new double[numStates][numActions];

		for (int i = 0; i < numStates; i++) {
			for (int j = 0; j < numActions; j++)
				pdfs[i][j] = 1.0 / ((double) numActions);
		}

		init();
	}

	/**
	 * Construct a SimplePolicy using the given ActionDomain, StateDomain, and
	 * initial probability distribution functions. This policy requires a
	 * discrete, finite ActionDomain and ActionDomain.
	 * 
	 * The pdfs should be organized as State-Action pairs. The first dimension
	 * should be the index of a State's ID in the ID list maintained by the
	 * StateDomian. The second dimension should be the index of an Action's ID
	 * in the ID list maintained by the ActionDomain. For example,
	 * 
	 * probValue = initPDFs[4][2];
	 * 
	 * should retrieve a State-Action probability value for the fourth State and
	 * second Action in the respective State ID and Action ID lists.
	 * 
	 * @param aDomain
	 *            - the collection of possible Actions
	 * @param sDomain
	 *            - the collection of possible States
	 * @param initPDFs
	 *            - an initial collection of pdfs used in selecting Actions for
	 *            each State in the StateDomain.
	 */
	public SimplePolicy(ActionDomain<AI, A> aDomain,
			StateDomain<SI, S> sDomain, double[][] initPDFs) {
		this(aDomain, sDomain, initPDFs, ((int) System.currentTimeMillis()));
	}

	public SimplePolicy(ActionDomain<AI, A> aDomain,
			StateDomain<SI, S> sDomain, double[][] initPDFs, int randSeed) {
		this(aDomain, sDomain, initPDFs, new MersenneTwister(randSeed));
	}

	public SimplePolicy(ActionDomain<AI, A> aDomain,
			StateDomain<SI, S> sDomain, double[][] initPDFs,
			RandomEngine randomGen) throws IllegalArgumentException {
		actionDomain = aDomain;
		stateDomain = sDomain;
		randomEngine = randomGen;

		if (initPDFs.length != stateDomain.size())
			throw new IllegalArgumentException(
					"Cannot initialize policy with given initial"
							+ " probabilities.\n Expected pdfs for "
							+ stateDomain.size() + " States, but received "
							+ initPDFs.length + ".");

		for (int i = 0; i < initPDFs.length; i++) {
			if (initPDFs[i].length != actionDomain.size())
				throw new IllegalArgumentException(
						"Cannot initialize policy with given initial"
								+ " probabilities.\n Expected probabilities for "
								+ actionDomain.size()
								+ " Actions for each State, but received "
								+ initPDFs[i].length + " for State " + i
								+ " (ID " + stateDomain.getIDList().get(i)
								+ ").");
		}

		pdfs = initPDFs;

		init();
	}

	/* ***************************************************************
	 * INITIALIZATION
	 * **************************************************************
	 */
	protected void init() {
		actionIDList = actionDomain.getIDList();
		stateIDList = stateDomain.getIDList();

		// Create a new event generator initialized with a uniform pdf
		double[] uniformPDF = new double[actionDomain.size()];
		for (int i = 0; i < uniformPDF.length; i++)
			uniformPDF[i] = 1.0 / uniformPDF.length;

		eventGenerator = new ModifiedEmpiricalWalker(uniformPDF,
				ModifiedEmpiricalWalker.NO_INTERPOLATION, randomEngine);

		lastAction = null;
	}

	/* ***************************************************************
	 * MISCELLANEOUS
	 * **************************************************************
	 */

	/**
	 * Given the current State, choose an Action according to the current
	 * probability distribution function.
	 * 
	 * @return a new Action
	 */
	public A generateAction(State<SI> currentState) {
		return generateAction(currentState.getID());
	}

	/**
	 * Given the indentifier of the current State, choose an Action according to
	 * the current probability distribution function.
	 * 
	 * @return a new Action
	 */
	public A generateAction(SI stateID) {
		int chosenIndex;
		AI chosenID;
		A chosenAction;

		// First retrieve the pdf for the current state of the world
		int currentStateIndex = stateIDList.indexOf(stateID);
		double[] currentPDF = pdfs[currentStateIndex];
		eventGenerator.setState(currentPDF,
				ModifiedEmpiricalWalker.NO_INTERPOLATION);
		eventGenerator.setState2(currentPDF);

		// Pick the index of an action. Note: indexes start at 0
		chosenIndex = eventGenerator.nextInt();
		chosenID = actionIDList.get(chosenIndex);
		chosenAction = actionDomain.getAction(chosenID);

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
		// Initialize to uniform distribution
		int numActions = actionDomain.size();
		int numStates = stateDomain.size();
		pdfs = new double[numStates][numActions];

		for (int i = 0; i < numStates; i++) {
			for (int j = 0; j < numActions; j++)
				pdfs[i][j] = 1.0 / ((double) numActions);
		}
		init();
	}

	/* ***************************************************************
	 * ACCESSOR METHODS
	 * **************************************************************
	 */
	/**
	 * Retrieve the probability distribution function used in selecting Actions
	 * from the ActionDomain in the given State.
	 * 
	 * @param aState
	 *            - the State for which to retrieve a an action choice pdf.
	 * @return the current pdf used in choosing Actions for the given world
	 *         State.
	 */
	public double[] getDistribution(State<SI> aState) {
		int stateIndex = stateIDList.indexOf(aState.getID());
		return pdfs[stateIndex];
	}

	/**
	 * Retrieve the collection of probability distribution functions used in
	 * selecting Actions from the ActionDomain for all States in the
	 * StateDomain.
	 * 
	 * @return collection of all the pdf used in selecting Actions for each
	 *         State.
	 */
	public double[][] getDistribution() {
		return pdfs;
	}

	/**
	 * Set the probability distribution function used in selecting Actions from
	 * the ActionDomain for the given State. The distribution is given as an
	 * array of doubles.
	 * 
	 * Note: there should be a value for each Action in this policy's
	 * ActionDomain. Each value is associated with the Action.
	 * 
	 * @param distrib
	 *            - the new collection of Action choice probabilities
	 */
	public void setDistribution(State<SI> aState, double[] pdf)
			throws IllegalArgumentException {
		int stateIndex = stateIDList.indexOf(aState);
		if (pdf.length == actionDomain.size()) {
			pdfs[stateIndex] = pdf;
			eventGenerator.setState(pdf,
					ModifiedEmpiricalWalker.NO_INTERPOLATION);
		} else {
			throw new IllegalArgumentException(
					"Cannot set given probability values for State "
							+ aState.getID() + "." + " Expected "
							+ actionDomain.size() + " values, but received "
							+ pdf.length + ". Previous values will be used.");
		}
	}

	/**
	 * Get the domain of action being used by this policy.
	 * 
	 * @see edu.iastate.jrelm.rl.Policy#getActionDomain()
	 */
	public ActionDomain<AI, A> getActionDomain() {
		return actionDomain;
	}

	/**
	 * Get the domain of world state being used by this policy.
	 * 
	 * @see Policy#getStateDomain()
	 */
	public StateDomain<SI, S> getStateDomain() {
		return stateDomain;
	}

	/**
	 * Retrieve the number of Actions in this policy's ActionDomain.
	 * 
	 * @return size of the ActionDomain
	 */
	public int getNumActions() {
		return actionDomain.size();
	}

	/**
	 * Retrieve the number of States in this policy's StateDomain.
	 * 
	 * @return size of the StateDomain
	 */
	public int getNumStates() {
		return stateDomain.size();
	}

	/**
	 * Get the last action chosen by this policy. Note: this will be null if
	 * called before any Actions have been chosen.
	 * 
	 * @see edu.iastate.jrelm.rl.Policy#getLastAction()
	 */
	public A getLastAction() {
		return lastAction;
	}

	/**
	 * Look up the probability for a State-Action pair. Gets the current
	 * probability of choosing an Action given a world State. The desired State
	 * and Action are indicated by Action and State identifiers respectively.
	 * 
	 * @param stateID
	 *            - the identifier indicating the desired state to look up an
	 *            Action probability for
	 * @param actionID
	 *            - the identifier indicating the Action to look up a
	 *            probability for.
	 * 
	 * @return the probability of choosing the specified Action from the
	 *         indicated State of the wolrd. Double.NaN if either the Action ID
	 *         or State ID do not belong to this policy's ActionDomain or
	 *         StateDomain.
	 * 
	 */
	public double getProbability(SI stateID, AI actionID) {
		int actionIndex = actionIDList.indexOf(actionID);
		int stateIndex = stateIDList.indexOf(stateID);

		if ((stateIndex >= 0) && (stateIndex < pdfs.length)
				&& (actionIndex >= 0)
				&& (actionIndex < pdfs[stateIndex].length))
			return pdfs[stateIndex][actionIndex];
		else
			return Double.NaN;
	}

	/**
	 * Set a State-Action pair probability value. Updates the probability of
	 * choosing the indicated Action given the indicated State of the wold.
	 * 
	 * @param stateID
	 *            - indicator of the desired State in this policy's StateDomain.
	 * @param actionID
	 *            - indicator of the desired Action in this policy's
	 *            ActionDomain.
	 * @param newValue
	 *            - new choice probability value to associate with this
	 *            State-Action pair.
	 */
	public void setProbability(SI stateID, AI actionID, double newValue) {
		int actionIndex = actionIDList.indexOf(actionID);
		int stateIndex = stateIDList.indexOf(stateID);

		if ((stateIndex >= 0) && (stateIndex < pdfs.length)) {
			if ((actionIndex >= 0) && (actionIndex < pdfs[stateIndex].length)) {
				pdfs[stateIndex][actionIndex] = newValue;
				eventGenerator.setState(pdfs[stateIndex],
						ModifiedEmpiricalWalker.NO_INTERPOLATION);
				eventGenerator.setState2(pdfs[stateIndex]);
			} else {
				throw new IllegalArgumentException(
						"Cannot set probability for State "
								+ stateID
								+ " and Action "
								+ actionID
								+ ". Action ID is not valid for this policy's ActionDomain.");
			}
		} else {
			throw new IllegalArgumentException(
					"Cannot set probability for State "
							+ stateID
							+ " and Action "
							+ actionID
							+ ". State ID is not valid for this policy's StateDomain.");
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
		randomEngine = new MersenneTwister(seed);
		setRandomEngine(randomEngine);
		randSeed = seed;
	}

}

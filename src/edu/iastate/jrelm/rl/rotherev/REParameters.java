/*
 * Created on Jun 18, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.iastate.jrelm.rl.rotherev;

import java.util.Hashtable;

//import com.sun.java_cup.internal.production;

import uchicago.src.reflector.DescriptorContainer;

import edu.iastate.jrelm.rl.RLParameters;

/**
 * Collects and manages parameters settings required for the modified Roth-Erev
 * reinforcement learning algorithm.
 * 
 * Roth-Erev learning parameters:
 * 
 * Experiementation: --explaination pending-- Note: Be careful not to choose
 * value e where (1-e) == e / (N - 1), where N is the size of the action domain
 * (i.e. e == 0.75 and N == 4). This will result in all action propensities
 * receiving the same experience update value, regardless of the last action
 * chosen. Action choice probabilities will then remain uniform and no learning
 * will occur.
 * 
 * 
 * Initial Propensity: --explaination pending--
 * 
 * Recency: --explaination pending--
 * 
 * 
 * 
 * Additional parameters:
 * 
 * @author Charles Gieseler
 *
 */
public class REParameters implements RLParameters, DescriptorContainer {

	/**
	 * Cooling parameter for Gibbs-Boltmann cooling. For the Gibbs-Boltzmann
	 * probability method used in VRELearner
	 */
	private double boltzmannTemp = Double.NaN;
	public static final double DEFAULT_BOLTZMANN = 10;
	private boolean useBoltz = false;

	/**
	 * The tendency for experimentation among action choices. The algorithm will
	 * sometimes choose non-optimal Actions in favor of exploring the domain.
	 * This allows the algortithm to get a more accurate picture of the domain
	 * and find Actions that yield better rewards than previously know.
	 * 
	 */
	private double experimentation = Double.NaN;
	public static final double DEFAULT_EXPERIMENTATION = 0.5;

	/**
	 * The initial propesinty value assigned to all actions. Used instead of a
	 * scaling parameter.
	 */
	private double initialPropensity = Double.NaN;
	public static final double DEFAULT_INIT_PROPENSITY = 100;

	/**
	 * Number of Actions available to choose from in the ActionDomain in use.
	 */
	// private int numberOfActions = -1;

	/**
	 * The degree to which actions are "forgotten". Used to degrade the
	 * propensity for choosing actions. Meant to make recent experience more
	 * prominent than past experience in the action choice process.
	 * 
	 */
	private double recency = Double.NaN;
	public static final double DEFAULT_RECENCY = 0.5;

	/**
	 * Seed value for the random event generator used in the policy Setting the
	 * seed to null forces VREParameters.getSeed() to return
	 * (int)System.currentTimeMillis().
	 */
	private Integer seed;

	/* ********************************************************************************
	 * DESCRIPTOR CONTAINER RELATED FIELDS
	 * **************************************
	 * *****************************************
	 */
	// Array of names of the parameters as Strings
	private String[] paramNames = new String[] { "BoltzmannTemp",
			"Experimentation", "InitialPropensity", "Recency", "RandomSeed" };

	// Collection of parameter descriptors. Allow parameters to be displayed and
	// edited in the RePast interface.
	Hashtable paramDescriptors = new Hashtable();

	/* ********************************************************************************
	 * CONSTRUCTORS
	 * *************************************************************
	 * ******************
	 */
	/**
	 * This is meant solely as a convenience constructor. Parameters are set to
	 * the
	 *
	 */
	public REParameters() {
		useBoltz = false;
		experimentation = DEFAULT_EXPERIMENTATION;
		initialPropensity = DEFAULT_INIT_PROPENSITY;
		recency = DEFAULT_RECENCY;

		seed = (int) System.currentTimeMillis();
	}

	/**
	 * Build a VREParameters object with the given learning parameter values.
	 * <P>
	 * 
	 * @param experimentation
	 *            -
	 * @param recency
	 *            -
	 * @param initialPropensity
	 */
	public REParameters(double experimentation, double initialPropensity,
			double recency) {

		useBoltz = false;
		this.experimentation = experimentation;
		this.recency = recency;
		this.initialPropensity = initialPropensity;

		seed = (int) System.currentTimeMillis();
		// TODO: Actually handle case of invalid parameters
		validateParameters();
	}

	/**
	 * Build a VREParameters object with the given learning parameter values.
	 * Includes a temperture value for use in a Gibb-Boltzmann probabilty
	 * distribution. The learner will use this distribution in selecting new
	 * action choices, rather than the default proportional distribution.
	 * <P>
	 * 
	 * @param boltzmannCooling
	 * @param experimentation
	 *            -
	 * @param initialPropensity
	 * @param recency
	 *            -
	 */
	public REParameters(double boltzmannTemp, double experimentation,
			double initialPropensity, double recency) {

		useBoltz = true;
		this.boltzmannTemp = boltzmannTemp;
		this.experimentation = experimentation;
		this.recency = recency;
		this.initialPropensity = initialPropensity;

		seed = (int) System.currentTimeMillis();
		// TODO: Actually handle case of invalid parameters
		validateParameters();
	}

	/**
	 * Additional parameters. Specify a seed for the psuedo-random number
	 * generator used by REPolicy and temperture value for use in a
	 * Gibb-Boltzmann probabilty distribution. The learner will use this
	 * distribution in selecting new action choices, rather than the default
	 * proportional distribution.
	 * 
	 * @param boltzmannCooling
	 * @param experimentation
	 * @param initialPropensity
	 * @param numberOfActions
	 * @param recency
	 * @param randSeed
	 */
	public REParameters(double boltzmannTemp, double experimentation,
			double initialPropensity, double recency, int randSeed) {

		useBoltz = true;
		this.boltzmannTemp = boltzmannTemp;
		this.experimentation = experimentation;
		this.recency = recency;
		this.initialPropensity = initialPropensity;
		seed = randSeed;

		validateParameters();
	}

	/* *******************************************************
	 * VALIDATION ******************************************************
	 */
	/**
	 * @see edu.iastate.jrelm.core.ai.learning.rl.RLParameters#validateParameters()
	 */
	public boolean validateParameters() {
		boolean valid = true;

		if (boltzmannTemp <= 0) {
			System.err
					.println("Parameters validation failure: "
							+ "Cooling parameter for Gibbs/Bolzmann probability generation must be a positive value."
							+ boltzmannTemp + " not valid.");
			valid = false;
		}

		if (experimentation < 0 || experimentation > 1) {
			System.err.println("Parameters validation failure: "
					+ "Invalid experimentation value: " + experimentation);
			valid = false;
		}

		if (initialPropensity < 0) {
			System.err.println("Parameters validation failure: "
					+ "Invalid initial propensity value: " + initialPropensity);
			valid = false;
		}

		if (recency < 0 || recency > 1) {
			System.err.println("Parameters validation failure:"
					+ "Invalid recency value: " + recency);
			valid = false;
		}

		return valid;
	}

	/* *******************************************************
	 * ACCESSOR METHODS ******************************************************
	 */

	/**
	 * Indicate whether to use the Gibbs-Boltzmann probability distribution or
	 * default to the proportional distribution.
	 */
	public boolean useBoltzmann() {
		return useBoltz;
	}

	/**
	 * @return Returns the value of the boltzmann temperature parameter.
	 */
	public double getBoltzmannTemp() {
		return boltzmannTemp;
	}

	/**
	 * @param bTemp
	 *            Sets the value of the scaling paremeter.
	 */
	public void setBoltzmannTemp(double bTemp) {
		boltzmannTemp = bTemp;
	}

	/**
	 * @return Returns the experimentation value.
	 */
	public double getExperimentation() {
		return experimentation;
	}

	public void setExperimentation(double e) {
		this.experimentation = e;
	}

	public double getInitialPropensity() {
		return initialPropensity;
	}

	public void setInitialPropensity(double initProp) {
		initialPropensity = initProp;
	}

	public String getName() {
		return "Roth-Erev";
	}

	/**
	 * @return Returns the value of the recency parameter.
	 */
	public double getRecency() {
		return recency;
	}

	/**
	 * @param recency
	 *            Sets the value of the recency parameter.
	 */
	public void setRecency(double r) {
		this.recency = r;
	}

	/**
	 * Get the seed for the RandomEngine used in the REPolicy. If the seed has
	 * not been set, this will return (int)System.currentTimeMillis().
	 * 
	 * @return - seed value for use in the policy
	 * 
	 * @see REPolicy
	 * @see cern.jet.random.engine.RandomEngine
	 */
	public int getRandomSeed() {
		return seed;
	}

	/**
	 * Set the seed to be used with the RandomEngine used in MREPolicy
	 * 
	 * @param randSeed
	 *            - seed value
	 */
	public void setRandomSeed(int randSeed) {
		seed = randSeed;
	}

	/* *****************************************************
	 * DESCRIPTOR CONTAINER AND GUI RELATED
	 * ****************************************************
	 */

	/**
	 * Get the names of the parameters
	 * 
	 * @return Parameter names as an array of Strings
	 */
	public String[] getParameterNames() {
		return paramNames;
	}

	public Hashtable getParameterDescriptors() {
		return paramDescriptors;
	}
}

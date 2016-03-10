package edu.iastate.jrelm.rl.rotherev.variant;

import edu.iastate.jrelm.rl.rotherev.REParameters;

/**
 * Parameters for the Varian Roth-Erev learner. These are exactly the same as
 * for the standard Roth-Erev leanrer, but a unique parameters class is needed
 * to identify the VRELearner (see SimpleStatelessLearner, BasicLearnerManager,
 * and BasicSettingsEditor).
 * 
 * @author Charles Gieseler
 *
 */
public class VREParameters extends REParameters {

	/* ********************************************************************************
	 * DESCRIPTOR CONTAINER RELATED FIELDS
	 * **************************************
	 * *****************************************
	 */
	// private String[] paramNames = new String[] {
	// "BoltzmannCooling",
	// "Experimentation",
	// "InitialPropensity",
	// "Recency",
	// "RandomSeed"};

	public VREParameters() {
		super();
	}

	public VREParameters(double experimentation, double initialPropensity,
			double recency) {
		super(experimentation, initialPropensity, recency);

	}

	public VREParameters(double experimentation, double initialPropensity,
			double recency, int randSeed) {
		super(experimentation, initialPropensity, recency, randSeed);
	}

	public VREParameters(double boltzmannTemp, double experimentation,
			double initialPropensity, double recency) {
		super(boltzmannTemp, experimentation, initialPropensity, recency);

	}

	public VREParameters(double boltzmannTemp, double experimentation,
			double initialPropensity, double recency, int randSeed) {
		super(boltzmannTemp, experimentation, initialPropensity, recency,
				randSeed);
	}

	public boolean validateParameters() {
		boolean valid = true;

		if (!super.validateParameters())
			valid = false;

		return valid;
	}

	/* *****************************************************
	 * DESCRIPTOR CONTAINER AND GUI RELATED
	 * ****************************************************
	 */
	// //Collection of parameter descriptors. Allow parameters to be displayed
	// and
	// //edited in the RePast interface.
	// Hashtable paramDescriptors = new Hashtable();
	//
	// /**
	// * Get the names of the parameters
	// *
	// * @return Parameter names as an array of Strings
	// */
	// public String[] getParameterNames(){
	// return paramNames;
	// }
	//
	// public Hashtable getParameterDescriptors(){
	// return paramDescriptors;
	// }
	//
	public String getName() {
		return "Variant Roth-Erev";
	}
}

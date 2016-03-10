package edu.iastate.jrelm.rl.bushmosteller;

/**
 * An implementation of AbstractGBMParmaters that specifies a linear function
 * for the reinforcement strength modifier used in Generalised Bush-Mosteller
 * learning. Specifically, v(r) = v * r. In this class, v is designated as the
 * 'multiplier value' and is accessed via getMultiplierValue() and
 * setMultiplierValue(double).
 * <p>
 * Note, in Generalised Bush-Mosteller learning, the feedback modifier function
 * is constrained so that v(0) = 0 and 0 <= v(r) <= 1. This means the multiplier
 * value v in v(r) = v * r must be chosen such that (v * r_min) >= 0 and (v *
 * r_max) <= 1, where r_min and r_max are the minimum and maximum expected
 * values for reinforcement strengths.
 * <p>
 * If you wish to use LinearGMBParameters to check that the current setting for
 * the multiplier value v is valid, you must first set the min and max expected
 * reinforcement strengths through setFeedbackRange(min, max).
 * 
 * @author Charles Gieseler
 *
 */
public class LinearGBMParameters extends GBMParameters {

	/**
	 * The linear multiplier value v used in v(r) = v * r.
	 */
	private double multiplier = Double.NaN;
	public static final double DEFAULT_LINEAR_MULTIPLIER = 1.0;

	/**
	 * The minimum expected feedback value
	 */
	private double minExpectedStregth = Double.NaN;
	public static final double DEFAULT_MIN_EXPECTED_STRENGH = 0.0;

	/**
	 * The maximum expected feedback value
	 */
	private double maxExpectedStrength = Double.NaN;
	public static final double DEFAULT_MAX_EXPECTED_STRENGH = 1.0;

	/****************
	 * Contstructors
	 ****************/

	/**
	 * Default constructor. Note, the defualt settings for the feedback
	 * multiplier value v is v = 1.0. The default settings for the minimum and
	 * maximum expected reinforcement strength are 0.0 and 1.0 respectively.
	 * 
	 * Note, the default settings are only appropriate in situations where
	 * feedback values are garaunteed to be between 0 and 1 and you want to use
	 * the values directly (without modification).
	 */
	public LinearGBMParameters() {
		this(DEFAULT_LINEAR_MULTIPLIER, DEFAULT_MIN_EXPECTED_STRENGH,
				DEFAULT_MAX_EXPECTED_STRENGH);
	}

	/**
	 * Construct a new LinearGBMParameters with the given linear multiplier,
	 * minimum expected strength and maximum expected stregth.
	 * 
	 * Note, completion of construction does not garauntee that the given
	 * parameter values are valid. Please use validateParameters() after
	 * construction if you wish to confirm that the settings are appropriate for
	 * Linear Generalized Bush-Mosteller learning.
	 * 
	 * @param multiplierValue
	 * @param minExpectedStrength
	 * @param maxExpectedStrength
	 */
	public LinearGBMParameters(double multiplierValue,
			double minExpectedStrength, double maxExpectedStrength) {
		this.setMultiplierValue(multiplierValue);
		this.setFeedbackRange(minExpectedStrength, maxExpectedStrength);
	}

	@Override
	public double feedbackModifier(double reinforcementStrength) {
		return multiplier * reinforcementStrength;
	}

	/****************
	 * Other methods
	 ****************/

	@Override
	public boolean validateParameters() {
		boolean valid = false;

		double scaledMin = multiplier * minExpectedStregth;
		double scaledMax = multiplier * maxExpectedStrength;

		if ((scaledMin >= 0.0) && (scaledMin <= 1.0) && (scaledMax >= 0.0)
				&& (scaledMax <= 1.0)) {
			valid = true;
		}

		return valid;
	}

	/**
	 * Retrieve the current setting for the multiplier value v used in the
	 * feedback modifier function v(r) = v * r.
	 */
	public double getMultiplierValue() {
		return multiplier;
	}

	/**
	 * Set the current multiplier value v used in the feedback modifier function
	 * v(r) = v * r.
	 * 
	 * Note, in Generalised Bush-Mosteller learning, the feedback modifier
	 * function is constrained so that v(0) = 0 and 0 <= v(r) <= 1. This means
	 * the multiplier value v in v(r) = v * r must be chosen such that (v *
	 * r_min) >= 0 and (v * r_max) <= 1, where r_min and r_max are the minimum
	 * and maximum expected values for reinforcement strengths.
	 */
	public void setMultiplierValue(double newValue) {
		this.multiplier = newValue;
	}

	/**
	 * Set the range of expected reinforcement strength values. This range will
	 * be used in validating the current feedback multiplier setting.
	 * 
	 */
	public void setFeedbackRange(double minimum, double maximum) {
		minExpectedStregth = minimum;
		maxExpectedStrength = maximum;
	}

	public double getMinExpectedFeedback() {
		return minExpectedStregth;
	}

	public double getMaxExpectedFeedback() {
		return maxExpectedStrength;
	}

	public String getName() {
		return "Linear Generalized Bush-Mosteller";
	}

	@Override
	public String[] getParameterNames() {

		return new String[] { "LinearMultiplier" };
	}

}

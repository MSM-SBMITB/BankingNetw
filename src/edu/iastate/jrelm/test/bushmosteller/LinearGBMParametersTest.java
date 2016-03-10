package edu.iastate.jrelm.test.bushmosteller;

import edu.iastate.jrelm.rl.bushmosteller.LinearGBMParameters;
import junit.framework.TestCase;

public class LinearGBMParametersTest extends TestCase {

	double newLinearMultiplier;
	double newMinExpected;
	double newMaxExpected;

	LinearGBMParameters parameters;
	String expectedName = "Linear Generalized Bush-Mosteller";
	String[] expectedParamNames = { "LinearMultiplier" };

	protected void setUp() throws Exception {
		super.setUp();

		newLinearMultiplier = 0.1;
		newMinExpected = 0.0;
		newMaxExpected = 10.0;

		parameters = new LinearGBMParameters();
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		newLinearMultiplier = Double.NaN;
		newMinExpected = Double.NaN;
		newMaxExpected = Double.NaN;

		parameters = null;
	}

	public void testLinearGBMParameters() {
		assertEquals(LinearGBMParameters.DEFAULT_LINEAR_MULTIPLIER,
				parameters.getMultiplierValue());
		assertEquals(LinearGBMParameters.DEFAULT_MIN_EXPECTED_STRENGH,
				parameters.getMinExpectedFeedback());
		assertEquals(LinearGBMParameters.DEFAULT_MAX_EXPECTED_STRENGH,
				parameters.getMaxExpectedFeedback());

		assertTrue(parameters.validateParameters());
	}

	public void testLinearGBMParametersDoubleDoubleDouble() {
		parameters = new LinearGBMParameters(newLinearMultiplier,
				newMinExpected, newMaxExpected);

		assertEquals(newLinearMultiplier, parameters.getMultiplierValue());
		assertEquals(newMinExpected, parameters.getMinExpectedFeedback());
		assertEquals(newMaxExpected, parameters.getMaxExpectedFeedback());

		assertTrue(parameters.validateParameters());
	}

	public void testFeedbackModifier() {

	}

	public void testValidateParameters() {

		// ******* VALIDATION PASSES ********
		// Default case should pass
		assertTrue(parameters.validateParameters());

		parameters = new LinearGBMParameters(0.1, 0.0, 10.0);
		assertTrue(parameters.validateParameters());

		parameters = new LinearGBMParameters(-0.1, -10.0, 0.0);
		assertTrue(parameters.validateParameters());

		parameters = new LinearGBMParameters(0.01, 50.0, 100.0);
		assertTrue(parameters.validateParameters());

		parameters = new LinearGBMParameters(-0.01, -100.0, -50.0);
		assertTrue(parameters.validateParameters());

		// ******* VALIDATION FAILURES ********

		// *** Test the multiplier
		parameters = new LinearGBMParameters(999,
				LinearGBMParameters.DEFAULT_MIN_EXPECTED_STRENGH,
				LinearGBMParameters.DEFAULT_MAX_EXPECTED_STRENGH);
		assertFalse(parameters.validateParameters());

		parameters = new LinearGBMParameters(-999,
				LinearGBMParameters.DEFAULT_MIN_EXPECTED_STRENGH,
				LinearGBMParameters.DEFAULT_MAX_EXPECTED_STRENGH);
		assertFalse(parameters.validateParameters());

		// *** Test the min
		parameters = new LinearGBMParameters(
				LinearGBMParameters.DEFAULT_LINEAR_MULTIPLIER, -1,
				LinearGBMParameters.DEFAULT_MAX_EXPECTED_STRENGH);
		assertFalse(parameters.validateParameters());

		parameters = new LinearGBMParameters(
				LinearGBMParameters.DEFAULT_LINEAR_MULTIPLIER, 999,
				LinearGBMParameters.DEFAULT_MAX_EXPECTED_STRENGH);
		assertFalse(parameters.validateParameters());

		// *** Test the max
		parameters = new LinearGBMParameters(
				LinearGBMParameters.DEFAULT_LINEAR_MULTIPLIER,
				LinearGBMParameters.DEFAULT_MIN_EXPECTED_STRENGH, 999);
		assertFalse(parameters.validateParameters());

		parameters = new LinearGBMParameters(
				LinearGBMParameters.DEFAULT_LINEAR_MULTIPLIER,
				LinearGBMParameters.DEFAULT_MIN_EXPECTED_STRENGH, -1);
		assertFalse(parameters.validateParameters());
	}

	public void testGetName() {
		assertEquals(expectedName, parameters.getName());
	}

	public void testGetParameterNames() {
		assertTrue(parameters.getParameterNames().length == 1);
		assertEquals(expectedParamNames[0], parameters.getParameterNames()[0]);
	}

	public void testGetMultiplierValue() {
		assertEquals(LinearGBMParameters.DEFAULT_LINEAR_MULTIPLIER,
				parameters.getMultiplierValue());
	}

	public void testSetMultiplierValue() {
		double newMultiplier = 0.75;
		parameters.setMultiplierValue(newMultiplier);

		assertEquals(newMultiplier, parameters.getMultiplierValue());
	}

	public void testSetFeedbackRange() {
		double newMin = 5.0;
		double newMax = 10.0;
		parameters.setFeedbackRange(newMin, newMax);

		assertEquals(newMin, parameters.getMinExpectedFeedback());
		assertEquals(newMax, parameters.getMaxExpectedFeedback());
	}

	public void testGetMinExpectedFeedback() {
		assertEquals(LinearGBMParameters.DEFAULT_MIN_EXPECTED_STRENGH,
				parameters.getMinExpectedFeedback());
	}

	public void testGetMaxExpectedFeedback() {
		assertEquals(LinearGBMParameters.DEFAULT_MAX_EXPECTED_STRENGH,
				parameters.getMaxExpectedFeedback());
	}

}

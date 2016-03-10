package edu.iastate.jrelm.test.bushmosteller;

import edu.iastate.jrelm.rl.bushmosteller.GBMParameters;
import junit.framework.TestCase;

public class GBMParametersTest extends TestCase {

	String paramObjectName = "Dummy Generalized Bush-Mosteller";
	String[] defaultParamLabelNames = { "ReinforcementStrengthModifierFunction" };
	DummyGBMParameters parameters;
	int defaultRandSeed = 0;
	int newRandomSeed = 12345;

	/**
	 * Dummy implementation of the GBMParameters class to allow us to test
	 * non-abstract methods.
	 */
	public class DummyGBMParameters extends GBMParameters {

		public DummyGBMParameters() {
			super();
		}

		@Override
		public double feedbackModifier(double reinforcementStrength) {
			return 0;
		}

		@Override
		public boolean validateParameters() {
			return false;
		}

		@Override
		public String getName() {
			return paramObjectName;
		}

	}

	protected void setUp() throws Exception {
		super.setUp();

		parameters = new DummyGBMParameters();
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		parameters = null;
	}

	public void testGetName() {
		assertTrue(parameters.getName().equals(paramObjectName));
	}

	public void testGetParameterNames() {
		// There should only be one parameter name in the abstract GBMParameters
		String[] paramNames = parameters.getParameterNames();
		assertTrue(paramNames.length == 1);
		assertEquals(defaultParamLabelNames[0], paramNames[0]);
	}

	public void testSetRandomSeed() {
		int newSeed = 777;
		parameters.setRandomSeed(newSeed);
		assertEquals(newSeed, parameters.getRandomSeed());
	}

	public void testGetRandomSeed() {
		assertEquals(defaultRandSeed, parameters.getRandomSeed());
	}

}

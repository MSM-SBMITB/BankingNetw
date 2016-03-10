package edu.iastate.jrelm.test.rotherev;

import edu.iastate.jrelm.rl.rotherev.REParameters;
import junit.framework.TestCase;

public class TestREParameters extends TestCase {

	REParameters params;
	double testBoltz = 2;
	double testExp = 0.234;
	double testInitProp = 300;
	double testRecency = 0.6;
	int testSeed = 1138;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestREParameters.class);
	}

	public TestREParameters(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		params = new REParameters(1, 0.2575, 500, 0.0505);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.REParameters()'
	 */
	public void testREParameters() {
		params = new REParameters();
		assertNotNull(params);

		assertEquals(params.getBoltzmannTemp(), REParameters.DEFAULT_BOLTZMANN);
		assertEquals(params.getExperimentation(),
				REParameters.DEFAULT_EXPERIMENTATION);
		assertEquals(params.getInitialPropensity(),
				REParameters.DEFAULT_INIT_PROPENSITY);
		assertEquals(params.getRecency(), REParameters.DEFAULT_RECENCY);
		assertNotNull(params.getRandomSeed());
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.REParameters(double, double,
	 * double, double)'
	 */
	public void testREParametersDoubleDoubleDoubleDouble() {
		params = new REParameters(testBoltz, testExp, testInitProp, testRecency);

		assertEquals(params.getBoltzmannTemp(), testBoltz);
		assertEquals(params.getExperimentation(), testExp);
		assertEquals(params.getInitialPropensity(), testInitProp);
		assertEquals(params.getRecency(), testRecency);
		assertNotNull(params.getRandomSeed());
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.REParameters(double, double,
	 * double, double, int)'
	 */
	public void testREParametersDoubleDoubleDoubleDoubleInt() {
		params = new REParameters(testBoltz, testExp, testInitProp,
				testRecency, testSeed);

		assertEquals(params.getBoltzmannTemp(), testBoltz);
		assertEquals(params.getExperimentation(), testExp);
		assertEquals(params.getInitialPropensity(), testInitProp);
		assertEquals(params.getRecency(), testRecency);
		assertEquals(params.getRandomSeed(), testSeed);
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.validateParameters()'
	 */
	public void testValidateParameters() {

		// Test true positives

		// Make sure defaults are good
		params = new REParameters();
		assertTrue(params.validateParameters());

		// Check settings via the accessor methods. Check at boundry values
		params.setBoltzmannTemp(testBoltz);
		assertTrue(params.validateParameters());
		params.setBoltzmannTemp(0.00000001);
		assertTrue(params.validateParameters());

		params.setExperimentation(0);
		assertTrue(params.validateParameters());
		params.setExperimentation(1);
		assertTrue(params.validateParameters());
		params.setExperimentation(testExp);
		assertTrue(params.validateParameters());

		params.setInitialPropensity(0);
		assertTrue(params.validateParameters());
		params.setInitialPropensity(999999);
		assertTrue(params.validateParameters());
		params.setInitialPropensity(testInitProp);
		assertTrue(params.validateParameters());

		params.setRecency(0);
		assertTrue(params.validateParameters());
		params.setRecency(1);
		assertTrue(params.validateParameters());
		params.setRecency(testRecency);
		assertTrue(params.validateParameters());

		params.setRandomSeed(-555567);
		assertTrue(params.validateParameters());
		params.setRandomSeed(0);
		assertTrue(params.validateParameters());
		params.setRandomSeed(testSeed);
		assertTrue(params.validateParameters());

		// Test false positives
		params = new REParameters(1, -3.2, 700, 5);
		assertFalse(params.validateParameters());

		// reset params
		params = new REParameters();

		// Check settings via the accessor methods
		params.setBoltzmannTemp(0);
		assertFalse(params.validateParameters());
		params.setBoltzmannTemp(-0.00000001);
		assertFalse(params.validateParameters());
		params.setExperimentation(-10);
		assertFalse(params.validateParameters());
		params = new REParameters();

		params.setExperimentation(-0.00000001);
		assertFalse(params.validateParameters());
		params.setExperimentation(-10);
		assertFalse(params.validateParameters());
		params.setExperimentation(1.00000001);
		assertFalse(params.validateParameters());
		params.setExperimentation(10);
		assertFalse(params.validateParameters());
		params = new REParameters();

		params.setRecency(-0.00000001);
		assertFalse(params.validateParameters());
		params.setRecency(-10);
		assertFalse(params.validateParameters());
		params.setRecency(1.00000001);
		assertFalse(params.validateParameters());
		params.setRecency(10);
		assertFalse(params.validateParameters());
		params = new REParameters();

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.getBoltzmannTemp()'
	 */
	public void testGetBoltzmannTemp() {
		params = new REParameters();
		assertEquals(params.getBoltzmannTemp(), REParameters.DEFAULT_BOLTZMANN);

		params.setBoltzmannTemp(testBoltz);
		params.getBoltzmannTemp();
		assertFalse(params.getBoltzmannTemp() == REParameters.DEFAULT_BOLTZMANN);
		assertEquals(params.getBoltzmannTemp(), testBoltz);
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.setBoltzmannTemp(double)'
	 */
	public void testSetBoltzmannTemp() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.getExperimentation()'
	 */
	public void testGetExperimentation() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.setExperimentation(double)'
	 */
	public void testSetExperimentation() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.getInitialPropensity()'
	 */
	public void testGetInitialPropensity() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.setInitialPropensity(double)'
	 */
	public void testSetInitialPropensity() {

	}

	/*
	 * Test method for 'edu.iastate.jrelm.rl.rotherev.REParameters.getName()'
	 */
	public void testGetName() {

	}

	/*
	 * Test method for 'edu.iastate.jrelm.rl.rotherev.REParameters.getRecency()'
	 */
	public void testGetRecency() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.setRecency(double)'
	 */
	public void testSetRecency() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.getRandomSeed()'
	 */
	public void testGetRandomSeed() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.setRandomSeed(int)'
	 */
	public void testSetRandomSeed() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.getParameterNames()'
	 */
	public void testGetParameterNames() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REParameters.getParameterDescriptors()'
	 */
	public void testGetParameterDescriptors() {

	}

}

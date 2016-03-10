package edu.iastate.jrelm.test.rotherev;

import java.util.ArrayList;

import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.rl.SimplePolicy;
import edu.iastate.jrelm.rl.SimpleStatelessPolicy;
import edu.iastate.jrelm.rl.rotherev.REPolicy;

import junit.framework.TestCase;

public class testREPolicy extends TestCase {
	SimpleActionDomain<Integer> domain;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(testREPolicy.class);
	}

	public testREPolicy(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		ArrayList<Integer> actionList = new ArrayList<Integer>();
		for (int i = 0; i < 200; i++)
			actionList.add(i);

		domain = new SimpleActionDomain<Integer>(actionList);

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'edu.iastate.jrelm.rl.rotherev.REPolicy.init()'
	 */
	public void testInit() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REPolicy.REPolicy(ActionDomain<I, A>)'
	 */
	public void testREPolicyActionDomainOfIA() {
		REPolicy<Integer, SimpleAction<Integer>> policy = new REPolicy<Integer, SimpleAction<Integer>>(
				domain);

		assertNotNull(policy);

		// Make sure all intial prob values are 1/200
		for (int i = 0; i < 200; i++)
			assertEquals(policy.getProbability(i), 0.0050);
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REPolicy.REPolicy(ActionDomain<I, A>,
	 * int)'
	 */
	public void testREPolicyActionDomainOfIAInt() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REPolicy.REPolicy(ActionDomain<I, A>,
	 * RandomEngine)'
	 */
	public void testREPolicyActionDomainOfIARandomEngine() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REPolicy.REPolicy(ActionDomain<I, A>,
	 * double[])'
	 */
	public void testREPolicyActionDomainOfIADoubleArray() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REPolicy.REPolicy(ActionDomain<I, A>,
	 * double[], int)'
	 */
	public void testREPolicyActionDomainOfIADoubleArrayInt() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REPolicy.REPolicy(ActionDomain<I, A>,
	 * double[], RandomEngine)'
	 */
	public void testREPolicyActionDomainOfIADoubleArrayRandomEngine() {

	}

	/*
	 * Test method for 'edu.iastate.jrelm.rl.rotherev.REPolicy.getPropensity(I)'
	 */
	public void testGetPropensityI() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REPolicy.getPropensity(int)'
	 */
	public void testGetPropensityInt() {

	}

	/*
	 * Test method for 'edu.iastate.jrelm.rl.rotherev.REPolicy.setPropensity(I,
	 * double)'
	 */
	public void testSetPropensityIDouble() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REPolicy.setPropensity(int, double)'
	 */
	public void testSetPropensityIntDouble() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REPolicy.getPropensities()'
	 */
	public void testGetPropensities() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.rotherev.REPolicy.setPropensities(double[])'
	 */
	public void testSetPropensities() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.SimpleStatelessPolicy(ActionDomain<I,
	 * A>)'
	 */
	public void testSimpleStatelessPolicyActionDomainOfIA() {
		SimpleStatelessPolicy<Integer, SimpleAction<Integer>> policy = new SimpleStatelessPolicy<Integer, SimpleAction<Integer>>(
				domain);

		assertNotNull(policy);
	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.SimpleStatelessPolicy(ActionDomain<I,
	 * A>, int)'
	 */
	public void testSimpleStatelessPolicyActionDomainOfIAInt() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.SimpleStatelessPolicy(ActionDomain<I,
	 * A>, RandomEngine)'
	 */
	public void testSimpleStatelessPolicyActionDomainOfIARandomEngine() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.SimpleStatelessPolicy(ActionDomain<I,
	 * A>, double[])'
	 */
	public void testSimpleStatelessPolicyActionDomainOfIADoubleArray() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.SimpleStatelessPolicy(ActionDomain<I,
	 * A>, double[], int)'
	 */
	public void testSimpleStatelessPolicyActionDomainOfIADoubleArrayInt() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.SimpleStatelessPolicy(ActionDomain<I,
	 * A>, double[], RandomEngine)'
	 */
	public void testSimpleStatelessPolicyActionDomainOfIADoubleArrayRandomEngine() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.generateAction()'
	 */
	public void testGenerateAction() {

	}

	/*
	 * Test method for 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.reset()'
	 */
	public void testReset() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.getDistribution()'
	 */
	public void testGetDistribution() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.setDistribution(double[])'
	 */
	public void testSetDistribution() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.getActionDomain()'
	 */
	public void testGetActionDomain() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.getNumActions()'
	 */
	public void testGetNumActions() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.getLastAction()'
	 */
	public void testGetLastAction() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.getProbability(I)'
	 */
	public void testGetProbabilityI() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.setProbability(I, double)'
	 */
	public void testSetProbabilityIDouble() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.setRandomEngine(RandomEngine)'
	 */
	public void testSetRandomEngine() {

	}

	/*
	 * Test method for
	 * 'edu.iastate.jrelm.rl.SimpleStatelessPolicy.setRandomSeed(int)'
	 */
	public void testSetRandomSeed() {

	}

}

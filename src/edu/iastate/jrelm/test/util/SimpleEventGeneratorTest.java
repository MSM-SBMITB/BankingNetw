package edu.iastate.jrelm.test.util;

import edu.iastate.jrelm.util.SimpleEventGenerator;
import junit.framework.TestCase;

public class SimpleEventGeneratorTest extends TestCase {

	SimpleEventGenerator eventGen;
	double[] pdf = { 0.2, 0.1, 0.5, 0.2 };

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SimpleEventGeneratorTest.class);
	}

	protected void setUp() throws Exception {
		eventGen = new SimpleEventGenerator(pdf, 112358);
	}

	protected void tearDown() throws Exception {

	}

	/*
	 * Test method for 'edu.iastate.jrelm.util.SimpleEventGenerator.nextEvent()'
	 */
	public void testNextEvent() {
		int nextEvent = -1;

		for (int i = 0; i < 1000; i++) {
			nextEvent = eventGen.nextEvent();
			assertTrue(nextEvent >= 0);
			assertTrue(nextEvent < pdf.length);
		}
	}

	public void testDistribution() {
		// System.out.println("SimpleEventGeneratorTest.testDistribution():");
		int numTrials = 10000;
		int[] counts = new int[pdf.length];
		int nextEvent = -1;

		for (int i = 0; i < numTrials; i++) {
			nextEvent = eventGen.nextEvent();
			counts[nextEvent]++;
		}

		double error = 0.01;
		double selectionRatio;

		// Check that the selection counts for each choice are within
		// +/- error of the pdf
		for (int j = 0; j < pdf.length; j++) {
			selectionRatio = ((double) counts[j]) / numTrials;
			// System.out.println("	selection ratio for choice " + j + " : " +
			// selectionRatio);
			assertTrue(selectionRatio >= (pdf[j] - error));
			assertTrue(selectionRatio <= (pdf[j] + error));
		}
	}
}

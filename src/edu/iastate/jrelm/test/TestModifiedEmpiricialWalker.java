package edu.iastate.jrelm.test;

import cern.jet.random.Empirical;
import cern.jet.random.EmpiricalWalker;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import edu.iastate.jrelm.util.ModifiedEmpiricalWalker;
import junit.framework.TestCase;

public class TestModifiedEmpiricialWalker extends TestCase {

	double[] pdf;
	ModifiedEmpiricalWalker eventGen;
	RandomEngine randEngine;
	int numTrials;

	public TestModifiedEmpiricialWalker(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestModifiedEmpiricialWalker.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		numTrials = 10000;

		pdf = new double[20];
		for (int j = 0; j < 20; j++) {
			pdf[j] = 40.0 / 1500.0;
		}
		pdf[2] = 60.0 / 500.0;
		pdf[3] = 60.0 / 500.0;
		pdf[5] = 60.0 / 500.0;
		pdf[8] = 60.0 / 500.0;
		pdf[13] = 60.0 / 500.0;

		randEngine = new MersenneTwister(1);
		eventGen = new ModifiedEmpiricalWalker(pdf, Empirical.NO_INTERPOLATION,
				randEngine);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testNextInt() {
		System.out.println("TestEmpiricalWalker.testNextInt()");
		int numEvents = pdf.length;
		int nextEvent;
		for (int j = 1; j <= numTrials; j++) {
			nextEvent = eventGen.nextInt();
			assert ((nextEvent >= 0) && (nextEvent < numEvents));
			// System.out.println("	trial: "+j+ "  event: "+nextEvent);
			// System.out.println("		  pdf: "+eventGen.pdf(nextEvent));
		}
		// System.out.println("");
	}

	/**
	 * 
	 */
	public void testNextIntAbsoluteDist() {
		System.out.println("TestEmpiricalWalker.testNextIntAbsoluteDist()");
		// Uniform
		pdf = new double[5];
		pdf[0] = 10;
		pdf[1] = 10;
		pdf[2] = 10;
		pdf[3] = 10;
		pdf[4] = 10;
		eventGen.setState(pdf, Empirical.NO_INTERPOLATION);
		eventGen.setState2(pdf);
		testAccurracy();

		pdf = new double[10];
		pdf[0] = 50;
		pdf[1] = 50;
		pdf[2] = 50;
		pdf[3] = 50;
		pdf[4] = 50;
		pdf[5] = 50;
		pdf[6] = 50;
		pdf[7] = 50;
		pdf[8] = 50;
		pdf[9] = 50;
		eventGen.setState(pdf, Empirical.NO_INTERPOLATION);
		eventGen.setState2(pdf);
		testAccurracy();

		// Non-uniform
		pdf = new double[5];
		pdf[0] = 3.234;
		pdf[1] = 100.947;
		pdf[2] = 72;
		pdf[3] = 57;
		pdf[4] = 23;
		eventGen.setState(pdf, Empirical.NO_INTERPOLATION);
		eventGen.setState2(pdf);
		testAccurracy();

		pdf = new double[20];
		pdf[0] = 80;
		pdf[1] = 23;
		pdf[2] = 200;
		pdf[3] = 75;
		pdf[4] = 4.321;
		pdf[5] = 67.8;
		pdf[6] = 20;
		pdf[7] = 12;
		pdf[8] = 157.32;
		pdf[9] = 912;
		pdf[10] = 73.1;
		pdf[11] = 9;
		pdf[12] = 45;
		pdf[13] = 31.23;
		pdf[14] = 102.57;
		pdf[15] = 0.134;
		pdf[16] = 0;
		pdf[17] = 3.234;
		pdf[18] = 3.234;
		pdf[19] = 42;
		eventGen.setState(pdf, Empirical.NO_INTERPOLATION);
		eventGen.setState2(pdf);
		testAccurracy();

		pdf = new double[50];
		pdf[0] = 80;
		pdf[2] = 23;
		pdf[4] = 200;
		pdf[6] = 75;
		pdf[8] = 4.321;
		pdf[10] = 67.8;
		pdf[12] = 20;
		pdf[14] = 12;
		pdf[15] = 157.32;
		pdf[16] = 912;
		pdf[18] = 73.1;
		pdf[20] = 9;
		pdf[22] = 45;
		pdf[24] = 31.23;
		pdf[26] = 102.57;
		pdf[28] = 0.134;
		pdf[30] = 0;
		pdf[32] = 3.234;
		pdf[34] = 71.7;
		pdf[36] = 3.234;
		pdf[38] = 42;
		pdf[40] = 31.54;
		pdf[42] = 173.2;
		pdf[44] = 782;
		pdf[46] = 23;
		pdf[48] = 89.124;
		eventGen.setState(pdf, Empirical.NO_INTERPOLATION);
		eventGen.setState2(pdf);
		testAccurracy();
	}

	public void testNextIntUniformAbsolute() {

	}

	public void testNextIntUniformRelative() {
		System.out.println("TestEmpiricalWalker.testNextIntUniform:");

		int numEvents;
		for (int i = 5; i <= 5; i += 10) {
			numEvents = i;
			// System.out.println(" numEvents=="+numEvents);
			// Make EmpiricalWalker with uniform pdf
			pdf = new double[numEvents];
			// System.out.println("Input pdf:");
			for (int j = 0; j < numEvents; j++)
				pdf[j] = 1.0 / (double) numEvents;

			eventGen = new ModifiedEmpiricalWalker(pdf,
					Empirical.NO_INTERPOLATION, randEngine);
			testAccurracy();
		}
		// int nextEvent;
		// double[] counts = new double[numEvents];
		// for(int j=1; j <= numTrials; j++){
		// nextEvent = eventGen.nextInt();
		// System.out.println("	trial: "+j+ "  event: "+nextEvent);
		// System.out.println("		  pdf: "+eventGen.pdf(nextEvent));
		// counts[nextEvent]++;
		// }
		// System.out.println("");
		// System.out.println("Frequencies:");
		// for(int k=0; k < numEvents; k++)
		// System.out.println("	event "+k+": " +
		// (double)counts[k]/(double)numTrials);
	}

	public void testModifiedEmpiricalWalker() {
	}

	public void testCdf() {
	}

	public void testPdf() {
		System.out.println("TestEmpiricalWalker.testPDF()");
		eventGen.setState(pdf, Empirical.NO_INTERPOLATION);
		double cumulitiveSum = 0.0;
		for (int i = 0; i < pdf.length; i++) {
			double internalValue = eventGen.pdf(i);
			cumulitiveSum += internalValue;
			assertTrue((internalValue >= 0.0 && internalValue < 1));
			// System.out.println("	EmpiricalWalker.pdf["+i+"]: "+internalValue+"  original: "+pdf[i]);
		}
		// System.out.println("	cumulitiveSum: "+cumulitiveSum);
		// assertTrue(cumulitiveSum == 1.0);
	}

	public void testSetState() {
	}

	public void testSetState2() {

	}

	public void testNextDouble() {
		System.out.println("TestEmpiricalWalker.testNextDouble()");
		eventGen = new ModifiedEmpiricalWalker(pdf,
				Empirical.LINEAR_INTERPOLATION, this.randEngine);
		int numEvents = pdf.length;
		int nextEvent;
		for (int j = 1; j <= numTrials; j++) {
			nextEvent = eventGen.nextInt();
			assert ((nextEvent >= 0) && (nextEvent < numEvents));
			// System.out.println("	trial: "+j+ "  event: "+nextEvent);
			// System.out.println("		  pdf: "+eventGen.pdf(nextEvent));
		}
	}

	public void testAccurracy() {
		System.out.println("TestEmpiricalWalker.testAccurracy()");
		int numEvents = pdf.length;
		int nextEvent;
		double pdfVal;
		double[] counts = new double[numEvents];
		for (int j = 1; j <= numTrials; j++) {
			nextEvent = eventGen.nextInt();
			counts[nextEvent]++;
		}

		System.out.println("Frequencies:");
		for (int k = 0; k < pdf.length; k++) {
			double freq = (double) counts[k] / (double) numTrials;
			pdfVal = eventGen.pdf(k);
			System.out.print("	event " + k + ": ");
			System.out.print("pdf: " + pdfVal + "  frequency: " + freq);
			double error = Math.abs(pdfVal - freq);
			System.out.print("	error: " + error);
			System.out.println("	error < 0.01: " + (error < 0.01));
			// assertTrue((error < 0.01));
		}
	}
}

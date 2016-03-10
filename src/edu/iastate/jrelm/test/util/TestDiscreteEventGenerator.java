package edu.iastate.jrelm.test.util;

import cern.jet.random.Empirical;
import cern.jet.random.EmpiricalWalker;
import cern.jet.random.engine.MersenneTwister;
import edu.iastate.jrelm.util.DiscreteEventGenerator;
import junit.framework.TestCase;

public class TestDiscreteEventGenerator extends TestCase {

	double[] pdf;
	int numTrials = 100000;
	DiscreteEventGenerator eventGen;
	MersenneTwister twister;

	public TestDiscreteEventGenerator(String arg0) {
		super(arg0);
		twister = new MersenneTwister(new java.util.Date());
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestDiscreteEventGenerator.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		pdf = new double[4];
		pdf[0] = .10;
		pdf[1] = 2.0 / 3.0;
		pdf[2] = 0.133333333333333333;
		pdf[3] = 0.10;

		eventGen = new DiscreteEventGenerator(pdf, twister);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Class under test for void DiscreteEventGenerator(double[])
	 */
	public void testDiscreteEventGeneratordoubleArray() {
		DiscreteEventGenerator newDistrib = new DiscreteEventGenerator(pdf);
		assertNotNull(newDistrib);
	}

	/*
	 * Class under test for void DiscreteEventGenerator(double[], int)
	 */
	public void testDiscreteEventGeneratordoubleArrayint() {
		DiscreteEventGenerator newDistrib = new DiscreteEventGenerator(pdf,
				(int) System.currentTimeMillis());
		assertNotNull(newDistrib);
	}

	/*
	 * Class under test for void DiscreteEventGenerator(double[], RandomEngine)
	 */
	public void testDiscreteEventGeneratordoubleArrayRandomEngine() {
		DiscreteEventGenerator newDistrib = new DiscreteEventGenerator(pdf,
				twister);
		assertNotNull(newDistrib);
	}

	public void testNextEvent() {
		int eventIndex;

		for (int i = 0; i <= 100; i++) {
			eventIndex = eventGen.nextEvent();
			assertNotNull(eventIndex);
			assert ((eventIndex >= 0) && (eventIndex < pdf.length));
		}
	}

	public void testGetPDF() {
		double[] tempPdf = eventGen.getPDF();
		assertNotNull(tempPdf);
		assert (pdf.length == tempPdf.length);

		for (int i = 0; i < tempPdf.length; i++) {
			assertEquals(tempPdf[i], pdf[i]);
			double prob = tempPdf[i];
			assert ((prob >= 0) && (prob <= 1));
		}

	}

	/*
	 * Class under test for void setState(double[])
	 */
	public void testSetStatedoubleArray() {
		double[] newPdf = { 0.25, 0.25, 0.35, 0.15 };
		eventGen.setState(newPdf);

		double[] tempPdf = eventGen.getPDF();
		for (int i = 0; i < newPdf.length; i++)
			assertEquals(newPdf[i], tempPdf[i]);

		testNextEvent();
	}

	/*
	 * Class under test for void setState(double[], int)
	 */
	public void testSetStatedoubleArrayint() {
		double[] newPdf = { 0.25, 0.25, 0.35, 0.15 };
		eventGen.setState(newPdf, (int) System.currentTimeMillis());

		double[] tempPdf = eventGen.getPDF();
		for (int i = 0; i < newPdf.length; i++)
			assertEquals(newPdf[i], tempPdf[i]);

		testNextEvent();
	}

	/*
	 * Class under test for void setState(double[], RandomEngine)
	 */
	public void testSetStatedoubleArrayRandomEngine() {
		double[] newPdf = { 0.25, 0.25, 0.35, 0.15 };
		eventGen.setState(newPdf, new MersenneTwister());

		double[] tempPdf = eventGen.getPDF();
		for (int i = 0; i < newPdf.length; i++)
			assertEquals(newPdf[i], tempPdf[i]);

		testNextEvent();
	}

	/**
	 * 
	 */
	/**
	 * 
	 */
	public void testOutput() {
		double[] counts = new double[eventGen.getPDF().length];

		for (int i = 0; i < numTrials; i++) {
			int event = eventGen.nextEvent();
			assert ((event >= 0) && (event < pdf.length));
			// System.out.println("	trial: " + i+"  event chosen: "+event);
			counts[event]++;

		}
		// System.out.println();
		// System.out.println("Frequencies:");
		// for(int j=0; j < counts.length; j++){
		// System.out.println("	event: "+ j+"  pdf: "+pdf[j]);
		// System.out.println("		  frequency: "+(double)counts[j]/(double)numTrials);
		//
		// }

	}

	public void testDiscreteEventGenertorUniformDistribs() {
		// System.out.println("TestDiscreteEventGeneratorUniformDistribs");
		int numEvents = 50;
		for (int i = 5; i <= 30; i += 5) {
			numEvents = i;
			// System.out.println(" numEvents=="+numEvents);
			// Make EmpiricalWalker with uniform pdf
			pdf = new double[numEvents];
			// System.out.println("Input pdf:");
			for (int j = 0; j < numEvents; j++)
				pdf[j] = 1.0 / (double) numEvents;

			eventGen = new DiscreteEventGenerator(pdf, twister);
			testAccurracy();
		}
	}

	public void testAccurracy() {
		System.out.println("TestDiscreteEventGenerator.testAccurracy()");
		System.out.println("	numEvents: " + pdf.length);
		int numEvents = pdf.length;
		int nextEvent;
		double pdfVal;
		double[] counts = new double[numEvents];
		for (int j = 1; j <= numTrials; j++) {
			nextEvent = eventGen.nextEvent();
			counts[nextEvent]++;
		}

		// System.out.println("Frequencies:");
		for (int k = 0; k < pdf.length; k++) {
			double freq = (double) counts[k] / (double) numTrials;
			pdfVal = pdf[k];
			// System.out.print("	event "+k+": ");
			// System.out.print("pdf: "+pdfVal+"  frequency: " + freq);
			double offset = Math.abs(pdfVal - freq);
			// System.out.print("	offset: "+ offset);
			// System.out.println("	offset check: "+ (offset < 0.01));
			assertTrue((offset < 0.01));
		}
	}
}

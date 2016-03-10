package edu.iastate.jrelm.test;

import cern.jet.random.engine.MersenneTwister;
import edu.iastate.jrelm.util.DiscreteEventGenerator;

public class GeneralDistribDriver {

	static DiscreteEventGenerator distrib;

	public GeneralDistribDriver() {
	}

	public static void main(String[] arg) {
		MersenneTwister twister = new MersenneTwister(
				(int) System.currentTimeMillis());

		double[] pdf = new double[4];
		pdf[0] = .10;
		pdf[1] = 2.0 / 3.0;
		pdf[2] = 0.1333;
		pdf[3] = 0.10;

		distrib = new DiscreteEventGenerator(pdf, twister);

		// System.out.println("GeneralDistribDriver: generate events");
		int trials = 10000;
		int[] counts = new int[pdf.length];
		for (int i = 0; i < trials; i++) {
			int event = distrib.nextEvent();
			// System.out.println("	choice: "+i+ " event: "+event);
			counts[event]++;
		}

		// System.out.println("");
		// System.out.println("	counts:");
		// for(int j=0; j < counts.length; j++){
		// System.out.println("	event: "+j+" "+counts[j]+"  "+(double)counts[j]/(double)trials);
		// }
	}
}

package edu.iastate.jrelm.util;

import java.util.Vector;

import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

/**
 * Class for generating discrete random events based on a given probability
 * density function. Uses a bin-method for selecting events with likelihood
 * corresponding to the pdf. Simple method, but less accurate.
 * 
 * @author Charles Gieseler
 *
 */
public class DiscreteEventGenerator {

	private final int FINENESS = 10000;
	private double[] distrib;
	private Integer[] bins;
	private RandomEngine engine;
	private Uniform generator;

	public DiscreteEventGenerator(double[] pdf) {
		distrib = pdf.clone();
		engine = new MersenneTwister((int) System.currentTimeMillis());
		init();
	}

	public DiscreteEventGenerator(double[] pdf, int seed) {
		distrib = pdf.clone();
		engine = new MersenneTwister(seed);
		init();
	}

	/**
	 * 
	 * @param pdf
	 *            - probability distribution function
	 * @param engine
	 *            -
	 */
	public DiscreteEventGenerator(double[] pdf, RandomEngine randEng) {
		distrib = pdf.clone();
		engine = randEng;
		init();
	}

	private void init() {
		Vector<Integer> protoBins = new Vector<Integer>();
		// System.out.println("DiscreteEventGenerator: init(): ");
		int numEvents = distrib.length;
		long totalNumBins = 0;
		// long startBin =0;
		// long endBin;
		// long[] counts = new long[numEvents];
		for (int i = 0; i < numEvents; i++) {
			long numBins = Math.round(distrib[i] * (numEvents * FINENESS));
			totalNumBins += numBins;
			// System.out.println("	event "+i+" gets "+ numBins +" bins");
			// counts[i] = numBins;
			// endBin = (numBins-1) + startBin ;
			// System.out.println("	   startBin: "+ startBin+" endBin: "+
			// endBin);
			// startBin = endBin+1;

			for (int j = 0; j < numBins; j++)
				protoBins.add(new Integer(i));
		}
		bins = new Integer[protoBins.size()];
		protoBins.toArray(bins);

		// System.out.println("");
		// System.out.println("	Total Bins:" + bins.length);
		// double sum = 0;
		// for(int k=0; k < counts.length; k++){
		// System.out.println("	event: "+k+" percentage of bins: "+(double)counts[k]/(double)bins.length);
		// sum += (double)counts[k]/(double)bins.length;
		// }
		// System.out.println("	percentage total: " + sum);

		generator = new Uniform(0, totalNumBins - 1, engine);
	}

	public int nextEvent() {
		// choose a bin number
		int binNumber = generator.nextInt();

		// Now get the index of the event stored in the bin
		int eventIndex = bins[binNumber];

		return eventIndex;
	}

	/* ***************************************************************
	 * ACCESSOR METHODS
	 * **************************************************************
	 */
	/**
	 * Retrieve the probability distribution function current being used to
	 * generate new events.
	 *
	 * @return the current probability distribution function.
	 */
	public double[] getPDF() {
		return distrib;
	}

	public void setState(double[] pdf) {
		distrib = pdf.clone();
		init();
	}

	public void setState(double[] pdf, int seed) {
		distrib = pdf.clone();
		engine = new MersenneTwister(seed);
		init();
	}

	public void setState(double[] pdf, RandomEngine randEng) {
		distrib = pdf.clone();
		engine = randEng;
		init();
	}
}

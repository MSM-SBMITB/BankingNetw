package edu.iastate.jrelm.util;

import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

/**
 * Generate discrete random events from a given distribution.
 * 
 * @author Charles Gieseler
 *
 */
public class SimpleEventGenerator {

	private double[] distrib;
	private RandomEngine engine;

	public SimpleEventGenerator(double[] pdf) {
		distrib = pdf.clone();
		engine = new MersenneTwister((int) System.currentTimeMillis());
	}

	public SimpleEventGenerator(double[] pdf, int seed) {
		distrib = pdf.clone();
		engine = new MersenneTwister(seed);
	}

	public SimpleEventGenerator(double[] pdf, RandomEngine rng) {
		distrib = pdf.clone();
		engine = rng;
	}

	public int nextEvent() {
		int eventIndex = 0;

		double randValue = engine.nextDouble();

		while (randValue > 0 && eventIndex < distrib.length) {
			randValue -= distrib[eventIndex];
			eventIndex++;
		}

		return eventIndex - 1;
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
	}

	public void setState(double[] pdf, int seed) {
		distrib = pdf.clone();
		engine = new MersenneTwister(seed);
	}

	public void setState(double[] pdf, RandomEngine randEng) {
		distrib = pdf.clone();
		engine = randEng;
	}

	public void setRandomEngine(RandomEngine engine) {
		this.engine = engine;
	}
}

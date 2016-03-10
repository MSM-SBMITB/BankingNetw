package edu.iastate.jrelm.core;

/**
 * Simple implementation of the Feedback interface for passing 'double' valued
 * feedback to JReLM learners. Just acts as a wrapper around a given Double
 * object.
 * 
 * @author Charles Gieseler
 *
 */
public class FeedbackDoubleValue implements Feedback<Double> {

	private double value;

	public FeedbackDoubleValue(Double someValue) {
		value = someValue.doubleValue();
	}

	public Double getValue() {
		return new Double(value);
	}
}

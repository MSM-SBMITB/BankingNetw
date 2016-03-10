package edu.iastate.jrelm.core;

/**
 * Simple implementation of the Feedback interface. Just acts as a wrapper to
 * allow other types to be used as Feedback .
 * 
 * @author Charles Gieseler
 *
 * @param <O>
 */
public class SimpleFeedback<O> implements Feedback<O> {

	private O feedBackValue;

	public SimpleFeedback(O value) {
		feedBackValue = value;
	}

	public O getValue() {
		return feedBackValue;
	}

}

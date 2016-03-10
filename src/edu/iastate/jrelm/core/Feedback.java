package edu.iastate.jrelm.core;

/**
 * Interface for classes representing feedback for reinforcement learning
 * algorithms. Such classes will encapulate data for passing learners
 * information regarding their past choices. In addition, Feedback classes may
 * contain methods for special processing of feedback information or for
 * comparison of Feedback objects.
 * 
 * Feedback may be simple, such as a single reward value resulting from the last
 * action chosen, or more complicated, such as a weighted reward distribution
 * over a recent history of multiple action choices. The Feedback interface
 * allows us to build appropriately complex classes for different types of
 * feedback.
 *
 * 
 * @author Charles Gieseler
 *
 */
public interface Feedback<O> {

	/**
	 * Retrieve the
	 * 
	 * @return
	 */
	public O getValue();
}

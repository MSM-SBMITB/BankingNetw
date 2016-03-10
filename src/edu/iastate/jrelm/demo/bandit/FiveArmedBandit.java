package edu.iastate.jrelm.demo.bandit;

import edu.iastate.jrelm.util.SimpleEventGenerator;

/**
 * Payout mechanism. All you have to do is pull one of the arm and receive a
 * payout. Each arm has a distribution of three different payouts that are
 * delivered with different probabilities.
 * 
 * The expected values for each arm are as follows: arm 0 : 210 arm 1 : 510 arm
 * 2 : 595 arm 3 : 147.5 arm 4 : 790
 *
 * The GamblerAgent, using a RothErevLearner, should eventually learn that the
 * last arm is the best to pull. We expect to see the history of arm choices to
 * converge to 4.
 *
 * 
 * @author Charlie Gieseler
 *
 */
public class FiveArmedBandit {

	private double[][] payouts;

	// Used to select payouts from a given distribution. The distribution is
	// selected by the arm choice
	private SimpleEventGenerator[] eventGens = new SimpleEventGenerator[5];

	private int randSeed = 11235813;

	public FiveArmedBandit() {

		payouts = new double[5][3];

		// Choice 0
		// Expected value: 210
		payouts[0][0] = 200;
		payouts[0][1] = 300;
		payouts[0][2] = 100;

		double[] distrib0 = { 0.7, 0.2, 0.1 };
		eventGens[0] = new SimpleEventGenerator(distrib0, randSeed);

		// Choice 1
		// Expected value: 510
		payouts[1][0] = 900;
		payouts[1][1] = 400;
		payouts[1][2] = 600;

		double[] distrib1 = { 0.1, 0.6, 0.3 };
		eventGens[1] = new SimpleEventGenerator(distrib1, randSeed);

		// Choice 2
		// Expected value: 595
		payouts[2][0] = 700;
		payouts[2][1] = 600;
		payouts[2][2] = 550;

		double[] distrib2 = { 0.4, 0.2, 03 };
		eventGens[2] = new SimpleEventGenerator(distrib2, randSeed);

		// Choice 3
		// Expected value: 147.5
		payouts[3][0] = 150;
		payouts[3][1] = 50;
		payouts[3][2] = 1000;

		double[] distrib3 = { 0.5, 0.45, 0.05 };
		eventGens[3] = new SimpleEventGenerator(distrib3, randSeed);

		// Choice 4
		// Expected value: 790
		payouts[4][0] = 700;
		payouts[4][1] = 800;
		payouts[4][2] = 900;

		double[] distrib4 = { 0.3, 0.5, 0.2 };
		eventGens[4] = new SimpleEventGenerator(distrib4, randSeed);

	}

	public double pullArm(int armChoice) {

		int payoutSelection = eventGens[armChoice].nextEvent();

		return payouts[armChoice][payoutSelection];
	}

}

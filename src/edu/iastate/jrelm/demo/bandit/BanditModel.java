package edu.iastate.jrelm.demo.bandit;

import javax.swing.JFrame;
import edu.iastate.jrelm.core.BasicLearnerManager;
import edu.iastate.jrelm.gui.BasicSettingsEditor;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimpleModel;

/**
 * This is a demonstration model showing the use of an agent with behavior
 * driven by the core components. A GamblerAgent, an agent powered by a
 * RothErevLearner, is placed before a FiveArmedBandit, a mechanism with five
 * levers. Each of the levers yields one of three reward values with different
 * probabilities. The reward distribution also differs over each arm and thus
 * each arm has a different expected return.
 * 
 * The task for the GamblerAgent is to learn, by repeatedly pulling arms, which
 * arm yields the highest expected value and so is the best to keep pulling to
 * maximize reward.
 * 
 * The last arm has the highest expected reward, so we expect to see the history
 * of arm choices converge to arm 4.
 * 
 * @author Charles Gieseler
 *
 */

public class BanditModel extends SimpleModel {

	private FiveArmedBandit bandit;
	private GamblerAgent gambler;

	private REParameters gamblerParams;
	private BanditActionDomain gamblerDomain;

	// GUI Components
	private BasicLearnerManager learnManager;
	private BasicSettingsEditor learnEditor;
	JFrame learnWindow;

	public BanditModel() {

	}

	public void setup() {
		super.setup();

		if (learnWindow != null)
			learnWindow.dispose();

		bandit = new FiveArmedBandit();

		gamblerDomain = new BanditActionDomain();
		gamblerParams = new REParameters(100, 0.55, 500, 0.3, 8780632);
		gambler = new GamblerAgent(gamblerDomain, gamblerParams);

	}

	public void buildModel() {

		// *** JReLM initialization
		learnManager = new BasicLearnerManager();
		learnManager.register(gambler);
		learnEditor = new BasicSettingsEditor(learnManager);

		getController().addSimEventListener(learnEditor);
		System.out.println(getController().getClass().toString());

		// Display JReLM settings GUI before the model is run
		learnWindow = new JFrame("JReLM Settings");
		learnWindow.add(learnEditor);
		learnWindow.setLocation(250, 0);
		learnEditor.display();
		learnWindow.setSize(200, 300);
		learnWindow.setVisible(true);

		System.out.println("BanditModel built...\n");
	}

	protected void step() {

		int armChoice = gambler.chooseBanditArm();

		double payoff = bandit.pullArm(armChoice);

		gambler.receivePayoff(payoff);

		System.out.println("*** New round ***");
		System.out.println("  Gambler chose arm " + armChoice);
		System.out.println("  received payoff " + payoff);
		System.out.println();
	}

	public static void main(String[] args) {
		SimInit init = new SimInit();
		BanditModel model = new BanditModel();
		init.loadModel(model, "", false);
	}
}

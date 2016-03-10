package edu.iastate.jrelm.test.gui;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import edu.iastate.jrelm.core.BasicLearnerManager;
import edu.iastate.jrelm.core.FeedbackDoubleValue;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.gui.BasicSettingsEditor;
import edu.iastate.jrelm.rl.Policy;
import edu.iastate.jrelm.rl.RLParameters;
import edu.iastate.jrelm.rl.ReinforcementLearner;
import edu.iastate.jrelm.rl.rotherev.RELearner;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;
import edu.iastate.jrelm.rl.rotherev.advanced.ARELearner;
import edu.iastate.jrelm.rl.rotherev.advanced.AREParameters;
import edu.iastate.jrelm.rl.rotherev.advanced.LinearSpillover;
import edu.iastate.jrelm.rl.rotherev.advanced.LogarithmicSpillover;
import edu.iastate.jrelm.rl.rotherev.variant.VRELearner;
import edu.iastate.jrelm.rl.rotherev.variant.VREParameters;
import edu.iastate.jrelm.similarity.SimpleDissimilarity;

public class BasicSettingsEditorDriver {

	public BasicSettingsEditorDriver() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<SimpleAction> actions = new ArrayList();
		actions.add(new SimpleAction(1, 1));
		actions.add(new SimpleAction(2, 2));
		actions.add(new SimpleAction(3, 3));

		SimpleActionDomain<Object> domain = new SimpleActionDomain(actions);

		LinearSpillover linSpill = new LinearSpillover(
				new SimpleDissimilarity(), -5);
		LogarithmicSpillover logSpill = new LogarithmicSpillover(
				new SimpleDissimilarity(), 0.5, 0.6, 0.7);
		AREParameters areParams = new AREParameters();
		areParams.addSpilloverMethod(linSpill);
		areParams.addSpilloverMethod(logSpill);

		ReinforcementLearner<RLParameters, Integer, SimpleAction, FeedbackDoubleValue, Policy>[] learners = new ReinforcementLearner[5];
		learners[0] = new RELearner(new REParameters(), domain);
		learners[1] = new RELearner(new REParameters(), domain);
		learners[2] = new VRELearner(new VREParameters(), domain);
		learners[3] = new VRELearner(new VREParameters(), domain);
		learners[4] = new ARELearner(areParams, domain);

		BasicLearnerManager manager = new BasicLearnerManager(learners);
		BasicSettingsEditor editor = new BasicSettingsEditor(manager);

		// Display JReLM settings GUI before the model is run
		JFrame learnWindow = new JFrame("JReLM Settings");
		learnWindow.add(editor);
		learnWindow.setLocation(250, 0);
		editor.display();
		learnWindow.setSize(225, 300);
		learnWindow.setVisible(true);

		// try{
		// System.in.read();
		// }catch(IOException ioe){
		//
		// }
	}

}

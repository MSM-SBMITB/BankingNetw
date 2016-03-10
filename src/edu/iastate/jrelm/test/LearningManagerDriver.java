package edu.iastate.jrelm.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.iastate.jrelm.core.JReLMAgent;
import edu.iastate.jrelm.core.BasicLearnerManager;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.gui.BasicSettingsEditor;
import edu.iastate.jrelm.rl.ReinforcementLearner;
import edu.iastate.jrelm.rl.SimpleStatelessLearner;
import edu.iastate.jrelm.rl.rotherev.advanced.ARELearner;
import edu.iastate.jrelm.rl.rotherev.advanced.AREParameters;
import edu.iastate.jrelm.rl.rotherev.variant.VRELearner;
import edu.iastate.jrelm.rl.rotherev.variant.VREParameters;
import edu.iastate.jrelm.util.WrapperAgent;

import static java.lang.System.out;

public class LearningManagerDriver {

	private static BasicSettingsEditor editor;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ArrayList temp = new ArrayList();
		temp.add(1);
		temp.add(2);
		temp.add(3);
		temp.add(4);
		temp.add(5);
		SimpleActionDomain domain = new SimpleActionDomain(temp);

		VRELearner reLearner1 = new VRELearner(new VREParameters(), domain);
		WrapperAgent reAgent1 = new WrapperAgent("RothErevAgent1", reLearner1);

		VRELearner reLearner2 = new VRELearner(new VREParameters(), domain);
		WrapperAgent reAgent2 = new WrapperAgent("RothErevAgent2", reLearner2);

		ARELearner areLearner1 = new ARELearner(new AREParameters(), domain);
		WrapperAgent areAgent1 = new WrapperAgent("AdvancedRothErevAgent1",
				areLearner1);

		FakeQLearner qLearner1 = new FakeQLearner(
				new FakeQLearningParameters(), domain);
		WrapperAgent qAgent1 = new WrapperAgent("Curly", qLearner1);

		FakeQLearner qLearner2 = new FakeQLearner(
				new FakeQLearningParameters(), domain);
		WrapperAgent qAgent2 = new WrapperAgent("Moe", qLearner2);

		FakeQLearner qLearner3 = new FakeQLearner(
				new FakeQLearningParameters(), domain);
		WrapperAgent qAgent3 = new WrapperAgent("Jack", qLearner3);

		SimpleStatelessLearner sLeaner1 = new SimpleStatelessLearner(
				new VREParameters(), temp);
		WrapperAgent sAgent1 = new WrapperAgent("SimpleAgent1", sLeaner1);

		ArrayList<JReLMAgent> agentList = new ArrayList<JReLMAgent>();
		agentList.add(reAgent1);
		agentList.add(reAgent2);
		agentList.add(qAgent1);
		agentList.add(qAgent2);
		agentList.add(qAgent3);
		agentList.add(areAgent1);
		agentList.add(sAgent1);
		BasicLearnerManager manager = new BasicLearnerManager(agentList);

		// test registration
		VRELearner rawLearner1 = new VRELearner(new VREParameters(), domain);
		ARELearner rawLearner2 = new ARELearner(new AREParameters(), domain);
		ARELearner rawLearner3 = new ARELearner(new AREParameters(), domain);

		manager.register(rawLearner1);
		manager.register(rawLearner2);
		manager.register("Calvin", rawLearner3);

		// manager.printAgentGroups();

		VREParameters newREParams = new VREParameters();
		FakeQLearningParameters newQParams = new FakeQLearningParameters("Bah4");
		// manager.updateSettings("RothErevAgent1", newREParams);
		// manager.updateSettings("RothErevAgent1", newQParams);

		editor = new BasicSettingsEditor(manager);
		editor.display();

		makeEnablePanel();

		JReLMAgent<VRELearner> agent = manager.getAgent("RothErevAgent1");
		VREParameters params = (VREParameters) agent.getLearner()
				.getParameters();

		// System.out.println("*** TestManager ****");
		// System.out.println("Params for RothErevAgent1");
		// System.out.println("Experimentation: " +
		// params.getExperimentation());
		// System.out.println("Initiail Propensity: " +
		// params.getInitialPropensity());
		// System.out.println("Number Of Actions: " +
		// params.getNumberOfActions());
		// System.out.println("Recency: " + params.getRecency());

	}

	private static void makeEnablePanel() {
		JFrame enableFrame = new JFrame();
		JPanel enablePanel = new JPanel();
		final JButton enableButton = new JButton("Disable");

		enableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (editor.isEnabled()) {
					out.println("LearningManagerDriver.makeEnablePanel: Disabling editor");
					editor.setEnabled(false);
					enableButton.setText("Enable");
					enableButton.repaint();
				} else {
					out.println("LearningManagerDriver.makeEnablePanel: Enabling editor");
					editor.setEnabled(true);
					enableButton.setText("Disable");
					enableButton.repaint();
				}
			}
		});
		enablePanel.add(enableButton);
		enableFrame.add(enablePanel);

		enableFrame.setPreferredSize(new Dimension(50, 100));
		enableFrame.setSize(new Dimension(50, 100));
		enableFrame.setLocation(350, 350);
		enableFrame.setVisible(true);
	}
}

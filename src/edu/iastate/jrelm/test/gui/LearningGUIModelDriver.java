package edu.iastate.jrelm.test.gui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.core.SimpleActionDomain;
import edu.iastate.jrelm.gui.JReLMSettingsController_Mark2;
import edu.iastate.jrelm.rl.RLParameters;
import edu.iastate.jrelm.rl.rotherev.advanced.AREParameters;
import edu.iastate.jrelm.rl.rotherev.advanced.ExponentialSpillover;
import edu.iastate.jrelm.rl.rotherev.advanced.LinearSpillover;
import edu.iastate.jrelm.rl.rotherev.advanced.LogarithmicSpillover;
import edu.iastate.jrelm.rl.rotherev.advanced.NoSpillover;
import edu.iastate.jrelm.spillover.SpilloverByDissimilarity;
import edu.iastate.jrelm.test.FakeDissimilarity;
import edu.iastate.jrelm.test.FakeQLearningParameters;
import uchicago.src.reflector.IntrospectPanel;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.engine.SimpleModel;

public class LearningGUIModelDriver extends SimpleModel {

	public LearningGUIModelDriver() {
		initRLGUI();
	}

	public String getName() {
		return "LearningGUIModelDriver";
	}

	public void initRLGUI() {

		ArrayList actions = new ArrayList();
		actions.add(1);
		actions.add(2);
		actions.add(3);
		actions.add(4);
		actions.add(5);
		SimpleActionDomain domain = new SimpleActionDomain(actions);

		AREParameters params;
		// Spillover objects
		FakeDissimilarity measure = new FakeDissimilarity();
		ExponentialSpillover<Action, FakeDissimilarity> expSpill = new ExponentialSpillover<Action, FakeDissimilarity>(
				measure, -1);

		LogarithmicSpillover<Action, FakeDissimilarity> logSpill = new LogarithmicSpillover<Action, FakeDissimilarity>(
				measure, -1, 0.4, 0.5);

		LinearSpillover<Action, FakeDissimilarity> linSpill = new LinearSpillover<Action, FakeDissimilarity>(
				measure, -1);

		// List
		Vector<SpilloverByDissimilarity> spillList = new Vector<SpilloverByDissimilarity>();
		spillList.add(expSpill);
		spillList.add(logSpill);
		spillList.add(linSpill);

		// parameters
		// parameters = new AREParameters(0.2, 0.3, 0.4, 10, 0.5, spillList);
		params = new AREParameters();

		ArrayList descripts = new ArrayList();
		descripts.add(params);
		descripts.add(new FakeQLearningParameters("blah"));

		// JReLMController_Mark1 controller = new
		// JReLMController_Mark1(descripts);
		// JReLMController_Mark1 controller = new
		// JReLMController_Mark1(parameters);

		// JReLMSettingsController_Mark2 controller = new
		// JReLMSettingsController_Mark2(parameters);
		JReLMSettingsController_Mark2 controller = new JReLMSettingsController_Mark2(
				descripts);

		// this.setController(controller);
		// controller.setExitOnExit(true);
		// controller.setModel(this);
		// this.addSimEventListener(controller);

		controller.display();

		// controller.setParameterList(new Vector<RLParameters>());
	}

	public static void main(String[] args) {
		LearningGUIModelDriver test = new LearningGUIModelDriver();
	}

}

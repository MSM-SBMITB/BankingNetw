package edu.iastate.jrelm.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import uchicago.src.reflector.ComboPair;
import uchicago.src.reflector.PairComboBox;
import uchicago.src.sim.engine.SimEvent;
import uchicago.src.sim.engine.SimEventListener;
import edu.iastate.jrelm.core.BasicLearnerManager;
import edu.iastate.jrelm.core.JReLMAgent;
import edu.iastate.jrelm.rl.RLParameters;
import edu.iastate.jrelm.rl.ReinforcementLearner;

/**
 * A component of JReLM providing a simple GUI to modify the learning settings.
 * Used in conjunction with a BasicLearnerManager. All ReinforcementLearners
 * registered with the manager may be accessed and have their learning settin
 * 
 * @author Charles Gieseler
 *
 */
public class BasicSettingsEditor extends JPanel implements SimEventListener {

	private static final long serialVersionUID = 1L;

	private final String RL_PANEL_TITLE = "JReLM Parameters";

	private BasicLearnerManager manager;

	// To select from the list of availwable RL Algorithms
	private JComboBox algorithmSelector;

	// *** NOT SUPPORTED AT THIS TIME ***
	// List of agents used to capture and transmit settings to all
	// agents for a particular algorithm group. There will be a
	// representative for each algorithm group
	// private Hashtable<Class, RLParameters> representatives;

	// To select from the list of agents using a particular algorithm
	private JComboBox agentSelector;

	// Algorithm that has the focus. The one currently selected
	// from the list of available algorithms
	private Class<ReinforcementLearner> selectedAlgorithm;

	// The currently selected agent
	private String selectedAgentID;

	// Settings for the currently selected algorithm
	private JPanel algorithmSettingsPanel;

	// Panel for all settings related to RL Algorithms
	private JPanel rlPanel;

	// Frame to display JReLM settings
	// private JFrame settingsFrame;

	/* *****************************************************************************
	 * CONSTRUCTORS
	 * *************************************************************
	 * ***************
	 */

	/**
	 * For use when only one type of RL algorithm is in use in the model.
	 * 
	 * @param parameters
	 */
	public BasicSettingsEditor(BasicLearnerManager learningManager) {
		manager = learningManager;
		setupAlgorithmSelector();
		setupAgentSelector();
		setupAlgorithmSettingsPanel();
	}

	/* *****************************************************************************
	 * SETUP PANELS
	 * *************************************************************
	 * ***************
	 */

	/*
	 * Constructs a panel for selecting among the learining alogorithms
	 * available in the model.
	 * 
	 * If there are more than one algorithms in the list, then set up a
	 * selection box to choose between them. Otherwise, if there is only one
	 * then designate it as the currently selected algorithm. If there are no
	 * algorithms given then the selection panel and the current selection
	 * remain unitialized.
	 */
	private void setupAlgorithmSelector() {
		algorithmSelector = new JComboBox();
		updateAlgorithmSelector();
	}

	private void setupAgentSelector() {
		if (agentSelector == null)
			agentSelector = new JComboBox();

		agentSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				selectedAgentID = (String) agentSelector.getSelectedItem();
				updateAlgorithmSettingsPanel();
			}
		});
		updateAgentSelector();
	}

	/*
	 * Builds the panel displaying the settings for the selected algorithm.
	 */
	private void setupAlgorithmSettingsPanel() {
		algorithmSettingsPanel = new JPanel(new BorderLayout());
		updateAlgorithmSettingsPanel();
	}

	/*
	 * 
	 */
	private void setupRLPanel() {
		rlPanel = new JPanel(new BorderLayout());

		Border b = BorderFactory.createEtchedBorder();

		/* *********************
		 * Add Selection Panels ********************
		 */
		// Sub-panel of the rlPanel. Used for layout tweaking
		JPanel selectionSubpanel = new JPanel(new BorderLayout());

		// Algorithm selection: add this panel if there are more than one
		// algorithms
		// to choose from.
		if ((algorithmSelector != null)
				&& (algorithmSelector.getItemCount() > 0)) {
			algorithmSelector.setBorder(BorderFactory.createTitledBorder(b,
					"Learning Method Selection"));
			selectionSubpanel.add(algorithmSelector, BorderLayout.NORTH);
		} else if (algorithmSelector.getItemCount() == 1) {
			JLabel outOfOrder = new JLabel(
					"No reinforcement learning algorithms available.");
			selectionSubpanel.add(outOfOrder);
		}

		if ((agentSelector != null) && (agentSelector.getItemCount() > 0)) {
			agentSelector.setBorder(BorderFactory.createTitledBorder(b,
					"Agent Selection"));
			selectionSubpanel.add(agentSelector, BorderLayout.CENTER);
		} else {
			JLabel outOfOrder = new JLabel("No agents available.");
			selectionSubpanel.add(outOfOrder);
		}

		rlPanel.add(selectionSubpanel, BorderLayout.NORTH);

		/* *********************
		 * Add Settings Panel ********************
		 */
		// Sub-panel of the rlPanel. Used for layout tweaking
		JPanel settingsSubpanel = new JPanel(new BorderLayout());
		algorithmSettingsPanel.setBorder(BorderFactory.createTitledBorder(b,
				"Parameters Settings"));

		JScrollPane sp = new JScrollPane(algorithmSettingsPanel);

		settingsSubpanel.add(sp, BorderLayout.NORTH);
		rlPanel.add(settingsSubpanel, BorderLayout.CENTER);

		// Add the whole learning panel
		this.add(rlPanel);
	}

	/* *****************************************************************************
	 * UPDATE PANELS
	 * ************************************************************
	 * ****************
	 */
	protected void updateAlgorithmSelector() {
		Hashtable<Class, Vector<JReLMAgent>> agentGrouping = manager
				.getAgentGrouping();
		ArrayList<Class> algList = new ArrayList<Class>(agentGrouping.keySet());

		// *** NOT IMPLEMENTED AT THIS TIME ***
		// Make new list of representatives
		// representatives = new Hashtable<Class,RLParameters>();

		if (algList == null || algList.isEmpty()) {
			selectedAlgorithm = null;
		} else {

			// Make listing of available algorithms with their display names in
			// the
			// form of ComboPairs. Each is a combo of the String name of
			// an algorithm and the Class of the ReinforcementLearner
			Vector<ComboPair> displayPairs = new Vector<ComboPair>();

			int numAlgs = algList.size();
			// add the algorithms in reverse order so they are displayed in the
			// same order as the grouping
			for (int i = numAlgs - 1; i >= 0; i--) {
				Class learnerClass = algList.get(i);
				JReLMAgent agentInGroup = agentGrouping.get(learnerClass)
						.firstElement();
				String algName = agentInGroup.getLearner().getParameters()
						.getName();
				displayPairs.add(new ComboPair(learnerClass, algName));

				// *** NOT IMPLEMENTED AT THIS TIME ***
				// Build the list of representative parameteters
				// representatives.put(learnerClass,
				// agentInGroup.getLearner().getParameters());
			}

			algorithmSelector = new PairComboBox(displayPairs);

			// Intialize the selected algorithm
			selectedAlgorithm = (Class) ((ComboPair) algorithmSelector
					.getSelectedItem()).first;

			algorithmSelector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					selectedAlgorithm = (Class) ((ComboPair) algorithmSelector
							.getSelectedItem()).first;
					updateAgentSelector();
				}
			});
		}
	}

	protected void updateAgentSelector() {
		if (agentSelector == null || selectedAlgorithm == null)
			return;

		Hashtable<Class, Vector<JReLMAgent>> agentGrouping = manager
				.getAgentGrouping();

		if (agentGrouping == null)
			return;

		Vector<JReLMAgent> agentList = agentGrouping.get(selectedAlgorithm);

		if (agentList == null)
			return;

		agentSelector.removeAllItems();

		JReLMAgent agent;
		int numAgents = agentList.size();

		for (int i = numAgents - 1; i >= 0; i--) {
			agent = agentList.elementAt(i);
			agentSelector.addItem(agent.getID());
		}

		// *** NOT SUPPORTED AT THIS TIME ***
		// Add an option to edit settings for all agents.
		// agentSelector.insertItemAt("All", 0);

		selectedAgentID = (String) agentSelector.getSelectedItem();
	}

	protected void updateAlgorithmSettingsPanel() {
		// System.out.println("BasicSettingsEditor.updateAlgorithmSettingsPanel: ");
		if (algorithmSettingsPanel == null)
			return;

		algorithmSettingsPanel.removeAll();

		if (selectedAgentID != null) {
			RLParameters params = null;
			JReLMAgent selectedAgent = manager.getAgent(selectedAgentID);

			// *** NOT SUPPORTED AT THIS TIME ***
			// Check to see if we are editing settings for all agents using the
			// selected algorithm
			// if(selectedAgentID.equals("All") && (selectedAgent != null)){

			// params = representatives.get(selectedAlgorithm);
			// algorithmSettingsPanel.add(new
			// JLabel("Edit settings for all agents."));
			// }else{
			params = selectedAgent.getLearner().getParameters();
			try {
				DelayedIntrospectPanel introspect = new DelayedIntrospectPanel(
						params, params.getParameterNames(), false, false);
				algorithmSettingsPanel.add(introspect, BorderLayout.NORTH, 0);
				introspect.reset();
			} catch (Exception e) {
				System.err
						.println("Could not initialize reinforcement learning algorithm "
								+ "parameter settings.");
				e.printStackTrace();
				algorithmSettingsPanel.add(new JLabel(
						"No parameter settings available."));
			}
			// }
		} else if (selectedAlgorithm == null) {
			algorithmSettingsPanel
					.add(new JLabel("No reinforcement learning "));
			algorithmSettingsPanel.add(new JLabel("algorithm selected."));
		} else {
			algorithmSettingsPanel.add(new JLabel(
					"No parameter settings available."));
		}

		// Carry over enabled status
		boolean enabled = algorithmSettingsPanel.isEnabled();
		Component[] comps = algorithmSettingsPanel.getComponents();
		for (Component comp : comps)
			comp.setEnabled(enabled);

		algorithmSettingsPanel.updateUI();

	}

	/*
	 * Rebuild the rlPanel and display new settings whenever a new agent or new
	 * algorithm is selected.
	 */
	protected void updateRLPanel() {
		rlPanel.updateUI();
	}

	/* *****************************************************************************
	 * MISCELLANEOUS
	 * ************************************************************
	 * ****************
	 */
	public void display() {
		// if(settingsFrame != null)
		// settingsFrame.dispose();
		//
		// settingsFrame = new JFrame("JReLM Parameter Settings");
		//
		// setupRLPanel();
		//
		// settingsFrame.setLocation(0, 0);
		// settingsFrame.setPreferredSize(new Dimension(300, 400));
		// settingsFrame.pack();
		// settingsFrame.setVisible(true);

		setupRLPanel();

		// setPreferredSize(new Dimension(300, 400));
		// pack();
		setVisible(true);
	}

	/**
	 * Rebuilds the JReLM settings display with the current list of algorithms
	 *
	 */
	public void reset() {
		System.out.println("BasicSettingsEditor.reset()");
		System.out.println("	reseting");
		algorithmSelector = null;
		agentSelector = null;
		algorithmSettingsPanel = null;

		setupAlgorithmSelector();
		setupAgentSelector();
		setupAlgorithmSettingsPanel();
		display();
	}

	/* *****************************************************************************
	 * ACCESSOR METHODS
	 * *********************************************************
	 * *******************
	 */

	/**
	 * Enables or disables the subpanel for editing the parameter settings. This
	 * does not disable the selection of algorithms or agents. Thus the user may
	 * still view the parameter settings for any agent, but may not make changes
	 * while 'enabled' is set to false.
	 * 
	 * @param enabled
	 *            - true allows settings to be changed, false prevents them from
	 *            being changed
	 */
	public void setEditorEnabled(boolean enabled) {
		algorithmSettingsPanel.setEnabled(enabled);
		Component[] comps = algorithmSettingsPanel.getComponents();
		for (Component comp : comps) {
			comp.setEnabled(enabled);
		}
	}

	/**
	 * Indicates whether the parameter settings editor subpanel is enabled or
	 * not.
	 *
	 */
	public boolean isEditorEnabled() {
		return algorithmSettingsPanel.isEnabled();
	}

	public BasicLearnerManager getSettingsManager() {
		return manager;
	}

	public void setSettingsManager(BasicLearnerManager newManager) {
		manager = newManager;
		reset();
	}

	public void simEventPerformed(SimEvent event) {
		// JFrame loudFrame = new JFrame();
		// JPanel panel = new JPanel();
		// panel.add(new JLabel("BasicSettingsEditor.actionPerformed \n"));
		// panel.add(new
		// JLabel("event source "+event.getSource().getClass().getCanonicalName()+
		// "\n"));
		// panel.add(new JLabel("event action command: "+event.getId()+"\n"));
		int id = event.getId();
		if (id == SimEvent.START_EVENT) {
			setEditorEnabled(false);
			// panel.add(new JLabel("id == START_EVENT"));
		} else if (id == SimEvent.PAUSE_EVENT || id == SimEvent.STOP_EVENT) {
			setEditorEnabled(true);
			// panel.add(new JLabel("id == PAUSE_EVENT"));
		}

		// loudFrame.add(panel);
		// loudFrame.setSize(new Dimension(300, 200));
		// loudFrame.setLocation(500, 500);
		// loudFrame.setVisible(true);
	}

	// *** NOT SUPPORTED AT THIS TIME ***
	/*
	 * protected class AllGroupUpdater implements ActionListener{ RLParameters
	 * params; // public AllGroupUpdater(RLParameters p){ // params = p; // }
	 * public void actionPerformed(ActionEvent arg0) {
	 * if(selectedAgentID.equalsIgnoreCase("All")){ System.out.println(
	 * "AllGroupUpdater.actionPerformed: Updating all agents in group now.");
	 * Vector<JReLMAgent> group = manager.getGroup(selectedAlgorithm);
	 * RLParameters params = representatives.get(selectedAlgorithm);
	 * for(JReLMAgent agent: group) agent.getLearner().setParameters(params); }
	 * } }
	 */
}

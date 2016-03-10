package edu.iastate.jrelm.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import uchicago.src.reflector.ComboPair;
import uchicago.src.reflector.IntrospectPanel;
import uchicago.src.reflector.PairComboBox;
import uchicago.src.sim.engine.Controller;
import edu.iastate.jrelm.rl.RLParameters;

public class JReLMSettingsController_Mark2 {

	private final String RL_TAB_TITLE = "JReLM Parameters";

	// List of availble RL Algorithms. Each is represented by appropriate
	// RLParameters rather than the actual JReLMLearners
	private Vector<RLParameters> rlAlgorithmList;

	// To select from the list of availwable RL Algorithms
	private JComboBox algorithmSelector;

	// Algorithm that has the focus. The one currently selected
	// from the list of available algorithms
	private RLParameters selectedAlgorithm;

	// Settings for the currently selected algorithm
	private JPanel algorithmSettingsPanel;

	// Panel for all settings related to RL Algorithms
	private JPanel rlPanel;

	// Frame to display JReLM settings
	private JFrame settingsFrame;

	/* *****************************************************************************
	 * CONSTRUCTORS
	 * *************************************************************
	 * ***************
	 */
	public JReLMSettingsController_Mark2() {
		selectedAlgorithm = null;
		rlAlgorithmList = new Vector<RLParameters>();

		buildAlgorithmSelectionPanel();
		buildAlgorithmSettingsPanel();
	}

	/**
	 * For use when only one type of RL algorithm is in use in the model.
	 * 
	 * @param parameters
	 */
	public JReLMSettingsController_Mark2(RLParameters params) {
		rlAlgorithmList = new Vector<RLParameters>();
		rlAlgorithmList.add(params);

		buildAlgorithmSelectionPanel();
		buildAlgorithmSettingsPanel();
	}

	public <P extends RLParameters> JReLMSettingsController_Mark2(
			Collection<P> paramsList) {

		if ((paramsList != null) && (!paramsList.isEmpty()))
			rlAlgorithmList = new Vector<RLParameters>(paramsList);
		else
			rlAlgorithmList = new Vector<RLParameters>();

		buildAlgorithmSelectionPanel();
		buildAlgorithmSettingsPanel();
	}

	/* *****************************************************************************
	 * BUILD PANELS
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
	private void buildAlgorithmSelectionPanel() {

		if (rlAlgorithmList == null || rlAlgorithmList.isEmpty()) {
			selectedAlgorithm = null;

		} else if (rlAlgorithmList.size() == 1) {
			if ((selectedAlgorithm == null)
					|| (!rlAlgorithmList.contains(selectedAlgorithm))) {

				selectedAlgorithm = rlAlgorithmList.get(0);
			}
		} else if (rlAlgorithmList.size() > 1) {

			// Make listing of available algorithms with their display names in
			// the
			// form of ComboPairs
			Vector algVector = new Vector();
			// Perform black magic to iterate over the list
			// @see java.util.Vector <T> T[] toArray(T[] a)
			for (RLParameters params : rlAlgorithmList
					.toArray(new RLParameters[1])) {
				algVector.add(new ComboPair(params, params.getName()));
			}

			algorithmSelector = new PairComboBox(algVector);

			algorithmSelector.addActionListener(new ActionListener() {
				// When a new algorithm is selected call for the algorithm
				// settings panel
				// to be updated
				public void actionPerformed(ActionEvent evt) {
					ComboPair selectedPair = (ComboPair) algorithmSelector
							.getSelectedItem();
					selectedAlgorithm = (RLParameters) selectedPair.first;
					updateRLParamsPanel();
				}
			});

			if ((selectedAlgorithm == null)
					|| (!rlAlgorithmList.contains(selectedAlgorithm))) {
				ComboPair selectedPair = (ComboPair) algorithmSelector
						.getSelectedItem();
				selectedAlgorithm = (RLParameters) selectedPair.first;
			}
		}

	}

	/*
	 * Builds the panel displaying the settings for the selected algorithm.
	 */
	private void buildAlgorithmSettingsPanel() {
		algorithmSettingsPanel = new JPanel(new BorderLayout());
		try {
			if (selectedAlgorithm != null) {

				IntrospectPanel introspect = new IntrospectPanel(
						selectedAlgorithm,
						selectedAlgorithm.getParameterNames(), false, false);

				algorithmSettingsPanel.add(introspect, BorderLayout.NORTH, 0);

			} else {
				algorithmSettingsPanel.add(new JLabel(
						"No Reinforcement Learining algorithms available."));
			}
		} catch (Exception e) {
			System.err
					.println("Could not initialize learning algorithm parameter settings panel.");
			e.printStackTrace();
		}
	}

	/*
	 * Adds a panel for managing RL settings as a new tab
	 */
	private void addLearningPanel() {

		rlPanel = new JPanel(new BorderLayout());

		Border b = BorderFactory.createEtchedBorder();
		JPanel p; // extra panel used for layout tweaking
		JScrollPane sp;

		// Algorithm selection: add this panel if there are more than one
		// algorithms
		// to choose from.
		if ((algorithmSelector != null)
				&& (algorithmSelector.getItemCount() > 1)) {
			algorithmSelector.setBorder(BorderFactory.createTitledBorder(b,
					"Algorithm Selection"));
			rlPanel.add(algorithmSelector, BorderLayout.NORTH);

		}

		if (selectedAlgorithm != null) {
			algorithmSettingsPanel.setBorder(BorderFactory.createTitledBorder(
					b, selectedAlgorithm.getName() + " Parameters"));
		} else {
			algorithmSettingsPanel.setBorder(BorderFactory.createTitledBorder(
					b, "Algorithm Parameters"));
		}

		// Algorithm specific parameter settings
		p = new JPanel(new BorderLayout());

		p.add(algorithmSettingsPanel, BorderLayout.NORTH);
		sp = new JScrollPane(p);

		rlPanel.add(sp, BorderLayout.CENTER);
		JButton updateButton = new JButton("Update");
		rlPanel.add(updateButton, BorderLayout.SOUTH, 1);

		// Add the whole learning panel
		settingsFrame.add(rlPanel);
	}

	/*
	 * Rebuild the RLParamsPanel and display new settings whenever a new
	 * algorithm is selected.
	 */
	private void updateRLParamsPanel() {

		Border b = BorderFactory.createEtchedBorder();
		try {
			IntrospectPanel rlSettings = new IntrospectPanel(selectedAlgorithm,
					selectedAlgorithm.getParameterNames(), false, false);

			algorithmSettingsPanel.remove(0);
			algorithmSettingsPanel.add(rlSettings, BorderLayout.NORTH, 0);

			algorithmSettingsPanel.setBorder(BorderFactory.createTitledBorder(
					b, selectedAlgorithm.getName() + " Parameters"));
		} catch (Exception e) {
			System.err
					.println("Could not update learning algorithm parameter settings panel");
			e.printStackTrace();
		}

		rlPanel.updateUI();
	}

	public void display() {

		if (settingsFrame != null)
			settingsFrame.dispose();

		settingsFrame = new JFrame("JReLM Parameter Settings");

		addLearningPanel();

		settingsFrame.setLocation(0, 0);
		settingsFrame.pack();
		settingsFrame.setVisible(true);
	}

	/**
	 * Rebuilds the JReLM settings display with the current list of algorithms
	 *
	 */
	public void reset() {

		buildAlgorithmSelectionPanel();
		buildAlgorithmSettingsPanel();
		display();
	}

	// private resetLearningParamPanel()

	/* *****************************************************************************
	 * ACCESSOR METHODS
	 * *********************************************************
	 * *******************
	 */

	/**
	 * Returns a list of the JReLM reinforcement learning algorithms this
	 * controller knows about.
	 * 
	 * Note: Each algorithm is represent by its corresponding RLParameters. Each
	 * of these RLParameter objects contain settings for their JReLMLearners as
	 * last set through the user interface.
	 */
	public Vector<RLParameters> getAlgorithmList() {
		return rlAlgorithmList;
	}

	/**
	 * Set the list of available reinforcement algorithms that can be set
	 * through this JReLM parameters settings window.
	 * 
	 * @param <P>
	 *            - accepts any Collection of objects extending from
	 *            RLParameters.
	 * @param algorithmList
	 *            - new list of algorithms as represented by their parameters
	 */
	public <P extends RLParameters> void setParameterList(
			Collection<P> algorithmList) {
		rlAlgorithmList = new Vector<RLParameters>(algorithmList);
		reset();
	}

}

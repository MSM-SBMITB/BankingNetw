package edu.iastate.jrelm.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.RepaintManager;
import javax.swing.border.Border;
import javax.swing.plaf.TabbedPaneUI;

import edu.iastate.jrelm.rl.RLParameters;

import uchicago.src.reflector.ComboPair;
import uchicago.src.reflector.DescriptorContainer;
import uchicago.src.reflector.IntrospectPanel;
import uchicago.src.reflector.PairComboBox;
import uchicago.src.reflector.PropertyComboBox;
//import uchicago.src.repastdemos.neuralfromfile.AgentSelectorDialog;
import uchicago.src.sim.engine.Controller;
import uchicago.src.sim.parameter.ParameterLexer;
import uchicago.src.sim.util.ProbeUtilities;
import uchicago.src.sim.util.SimUtilities;

public class JReLMController_Mark1 extends Controller {

	private final String RL_TAB_TITLE = "JReLM Parameters";

	// List of availble RL Algorithms. Each is represented by appropriate
	// RLParameters rather than the actual JReLMLearners
	private Vector<RLParameters> rlAlgorithmList = new Vector<RLParameters>();

	// To select from the list of availwable RL Algorithms
	private JComboBox algorithmSelector;

	// Algorithm that has the focus. The one currently selected
	// from the list of available algorithms
	private RLParameters selectedAlgorithm;

	// Settings for the currently selected algorithm
	private JPanel algorithmSettingsPanel;

	// Panel for all settings related to RL Algorithms
	private JPanel rlPanel;

	public JReLMController_Mark1() {
		selectedAlgorithm = null;
	}

	/**
	 * For use when only one type of RL algorithm is in use in the model.
	 * 
	 * @param parameters
	 */
	public JReLMController_Mark1(RLParameters params) {
		super();

		rlAlgorithmList.add(params);

		buildAlgorithmSelectionPanel();
		buildAlgorithmSettingsPanel();
		rlPanel = new JPanel(new BorderLayout());
	}

	public <P extends RLParameters> JReLMController_Mark1(
			Collection<P> paramsList) {
		super();

		rlAlgorithmList = new Vector<RLParameters>(paramsList);

		buildAlgorithmSelectionPanel();
		buildAlgorithmSettingsPanel();
		rlPanel = new JPanel(new BorderLayout());
	}

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
		algorithmSelector = null;

		if (rlAlgorithmList.size() > 1) {

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

			ComboPair selectedPair = (ComboPair) algorithmSelector
					.getSelectedItem();
			selectedAlgorithm = (RLParameters) selectedPair.first;
		} else if (rlAlgorithmList.size() == 1) {
			// If there is only one algorithm in the given list, then just set
			// it to be selected
			selectedAlgorithm = rlAlgorithmList.get(0);
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
				JButton updateButton = new JButton("Update");

				algorithmSettingsPanel.add(introspect, BorderLayout.NORTH, 0);
				algorithmSettingsPanel
						.add(updateButton, BorderLayout.CENTER, 1);

			} else {
				algorithmSettingsPanel.add(new JLabel(
						"No Reinfornce Learining algorithms available."));
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

		// Algorithm specific parameter settings
		p = new JPanel(new BorderLayout());

		// algorithmSettingsPanel.setBorder(
		// BorderFactory.createTitledBorder(b,
		// selectedAlgorithm.getAlgorithmName()
		// + " Parameters"));

		algorithmSettingsPanel.setBorder(BorderFactory.createTitledBorder(b,
				"Algorithm Parameters"));

		p.add(algorithmSettingsPanel, BorderLayout.NORTH);
		sp = new JScrollPane(p);

		rlPanel.add(sp, BorderLayout.CENTER);

		// Add the whole learning panel
		tabPane.insertTab(RL_TAB_TITLE, null, rlPanel, null, 1);
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

		} catch (Exception e) {
			System.err
					.println("Could not update learning algorithm parameter settings panel");
			e.printStackTrace();
		}

		tabPane.updateUI();
	}

	public void display() {
		// Must initialize by calling super.display() first since most of the
		// construction is private. The learning panel can then be tacked on,
		super.display();
		addLearningPanel();

		// Taken from Controller. Have to reposition where and how big the
		// display
		// is since the learning panel is added afterwards this is done by super
		if (settingsFrame == null) {
			Dimension sfSize = settingsFrame.getSize();

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int myWidth = settingsFrame.getWidth();
			int toolBarHeight = tbFrame.getHeight();
			int x = screenSize.width - myWidth;
			int toolBarY = tbFrame.getLocation().y;
			int y = toolBarY + toolBarHeight + 10;

			if (sfSize.height > screenSize.height - 150) {
				settingsFrame.setSize(settingsFrame.getSize().width,
						screenSize.height - 150);
			}
			settingsFrame.setLocation(x, y);
		}
	}

	// TODO: private resetLearningParamPanel()

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

	public <P extends RLParameters> void setParameterList(
			Collection<P> paramsList) {
		rlAlgorithmList = new Vector<RLParameters>(paramsList);
	}

	public <P extends RLParameters> boolean addParameters(P params) {
		if (rlAlgorithmList.contains(params))
			return false;
		else {
			rlAlgorithmList.add(params);
			return true;
		}

	}

	public <P extends RLParameters> boolean removeParameters(P params) {
		return rlAlgorithmList.remove(params);

	}
}

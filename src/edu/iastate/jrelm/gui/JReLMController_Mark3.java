package edu.iastate.jrelm.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import edu.iastate.jrelm.core.BasicLearnerManager;
import edu.iastate.jrelm.core.BasicLearnerManager.RegistrationEvent;

import uchicago.src.sim.engine.Controller;
import uchicago.src.sim.parameter.ParameterSetter;

public class JReLMController_Mark3 extends Controller {

	BasicLearnerManager manager;
	BasicSettingsEditor editor;

	public JReLMController_Mark3() {
		super();
		init();
	}

	public JReLMController_Mark3(ParameterSetter arg0) {
		super(arg0);
		init();
	}

	private void init() {
		manager = new BasicLearnerManager();
		editor = new BasicSettingsEditor(manager);

		manager.addRegistrationListener(new BasicLearnerManager.RegistrationListener() {
			public void registrationChange(String[] ids, RegistrationEvent event) {
				System.out
						.println("JReLMController_Mark3: registration change listerner");
				System.out.println("	event: " + event.toString());

				editor.reset();
				tabPane.repaint();

			}
		});
	}

	/*
	 * Adds a panel for managing RL settings as a new tab
	 */
	private void addLearningPanel() {

		Border b = BorderFactory.createEtchedBorder();
		JPanel p; // extra panel used for layout tweaking
		JScrollPane sp;

		// Add the whole learning panel
		tabPane.insertTab("JReLM settings", null, editor, null, 1);
		editor.display();
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

	public BasicLearnerManager getManager() {
		return manager;
	}

	public BasicSettingsEditor getEditor() {
		return editor;
	}

}

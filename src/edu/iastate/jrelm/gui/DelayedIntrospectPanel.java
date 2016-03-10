package edu.iastate.jrelm.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JButton;

import uchicago.src.reflector.IntrospectPanel;
import uchicago.src.reflector.PropertyWidget;

/**
 * A bit of a hack to get around ItrospectPanel directly settings values in the
 * introspected object whenever a change is made through the widgets. Now new
 * values are not set until the update button is pressed.
 * 
 * @author Charles Gieseler
 *
 */
public class DelayedIntrospectPanel extends IntrospectPanel {

	ArrayList<PropertyWidget> widgetList;
	JButton updateButton;

	public DelayedIntrospectPanel(Object o) throws InvocationTargetException,
			IllegalAccessException, IntrospectionException {
		super(o);
		init();
	}

	public DelayedIntrospectPanel(Object o, String[] propsToIntrospect,
			boolean alphaOrder, boolean showBeanBowlButton)
			throws InvocationTargetException, IllegalAccessException,
			IntrospectionException {
		super(o, propsToIntrospect, alphaOrder, showBeanBowlButton);
		init();
	}

	public DelayedIntrospectPanel(Object o, String[] propsToIntrospect,
			boolean alphaOrder) throws InvocationTargetException,
			IllegalAccessException, IntrospectionException {
		super(o, propsToIntrospect, alphaOrder);
		init();
	}

	private void init() {
		widgetList = new ArrayList<PropertyWidget>();
		updateButton = new JButton("Update");

		updateButton.addActionListener(this);

		GridBagConstraints constraints = ((GridBagLayout) this.getLayout())
				.getConstraints(this);

		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		add(updateButton, constraints);
		constraints.gridy++;

	}

	/**
	 * Overrides the IntrospectPanel's actionPerformed. This catches all actions
	 * and records all of the widgets within this panel that have had an action
	 * performed at least once. Only when the source of the action is this
	 * panel's "update" button will actions events for all of the recorded
	 * widgets get passed to IntrospectPanel.actionPerformed(ActionEvent).
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource().equals(updateButton)) {
			for (PropertyWidget widget : widgetList) {
				ActionEvent newEvent = new ActionEvent(widget, 007,
						widget.toString());
				super.actionPerformed(newEvent);
			}
		} else {
			PropertyWidget widget = (PropertyWidget) evt.getSource();
			// Compile a list of all widgets that have had an actioned performed
			// at least once.
			if (!widgetList.contains(widget))
				widgetList.add(widget);
		}
	}

	public void redraw() throws IllegalAccessException,
			InvocationTargetException, IntrospectionException {
		super.redraw();
		init();
	}

	public JButton getUpdateButton() {
		return updateButton;
	}
}

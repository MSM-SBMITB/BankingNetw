package edu.iastate.jrelm.demo.bandit;

import java.util.ArrayList;

import edu.iastate.jrelm.core.ActionDomain;

public class BanditActionDomain implements ActionDomain<Integer, BanditAction> {

	private int numberOfArmChoices = 5;
	private ArrayList<BanditAction> actionList;
	private ArrayList<Integer> idList;

	public BanditActionDomain() {
		actionList = new ArrayList<BanditAction>(numberOfArmChoices);
		idList = new ArrayList<Integer>();

		for (int i = 0; i < numberOfArmChoices; i++) {
			actionList.add(new BanditAction(new Integer(i), new Integer(i)));
			idList.add(i);
		}
	}

	public int size() {
		return numberOfArmChoices;
	}

	public BanditAction getAction(Integer index) {
		return actionList.get(index);
	}

	public ArrayList<Integer> getIDList() {
		return idList;
	}

	public boolean containsAction(BanditAction actionToCheck) {
		return actionList.contains(actionToCheck);
	}
}

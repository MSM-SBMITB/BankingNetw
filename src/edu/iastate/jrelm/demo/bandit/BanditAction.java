package edu.iastate.jrelm.demo.bandit;

import edu.iastate.jrelm.core.SimpleAction;

public class BanditAction extends SimpleAction<Integer> {

	private int armChoice;

	/**
	 * Construct a BanditAction with the given ID and a choice of arm to pull.
	 * 
	 * @param id
	 *            - ID for this action
	 * @param armNumber
	 *            - which arm to pull
	 */
	public BanditAction(Integer id, Integer armNumber) {
		super(id, armNumber);
		armChoice = armNumber;
	}

	/**
	 * Here the plan of action is which arm to pull.
	 */
	public Integer getAct() {
		return armChoice;
	}
}

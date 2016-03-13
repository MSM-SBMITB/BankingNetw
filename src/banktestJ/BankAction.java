package banktestJ;

import edu.iastate.jrelm.core.SimpleAction;

public class BankAction extends SimpleAction<Integer> {
	
	private int choice;

	/**
	 * Construct a Bank Action with the given ID and a choice of takeback lending amt.
	 * 
	 * @param id
	 *            - ID for this action
	 * @param takebackLending
	 *            - how much fraction the bank will takeback
	 */
	public BankAction(Integer id, Integer takebackLending) {
		super(id, takebackLending);
		choice = takebackLending;
	}

	/**
	 * Here the plan of action is which arm to pull.
	 */
	public Integer getAct() {
		return choice;
	}

}

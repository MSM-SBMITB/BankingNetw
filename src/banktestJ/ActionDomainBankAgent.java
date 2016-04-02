package banktestJ;

import java.util.ArrayList;

import edu.iastate.jrelm.core.ActionDomain;



public class ActionDomainBankAgent implements ActionDomain <Integer, BankAction> {
	
	private int numberOfChoices = 10;
	private ArrayList<BankAction> actionList;
	private ArrayList<Integer> idList;
	private ArrayList<Double> actionConverter;
	
	public ActionDomainBankAgent(){
		actionList = new ArrayList<BankAction>(numberOfChoices);
		idList = new ArrayList<Integer>();
		
		for (int i = 0; i < numberOfChoices; i++) {
			actionList.add(new BankAction(new Integer(i), new Integer(i+1)));
			idList.add(i);
		}

	}

	public boolean containsAction(BankAction actionToCheck) {
		return actionList.contains(actionToCheck);
	}

	public BankAction getAction(Integer index) {
		return actionList.get(index);
	}

	public ArrayList<Integer> getIDList() {
		return idList;
	}

	public int size() {
		return numberOfChoices;
	}

}

package edu.iastate.jrelm.demo.bandit;

import edu.iastate.jrelm.demo.RothErevAgent;
import edu.iastate.jrelm.rl.rotherev.REParameters;

public class GamblerAgent extends RothErevAgent<BanditAction> {

	public GamblerAgent(BanditActionDomain domain, REParameters parameters,
			String agentID) {
		super(parameters, domain, agentID);
	}

	public GamblerAgent(BanditActionDomain domain, REParameters parameters) {
		super(parameters, domain);
	}

	public int chooseBanditArm() {
		BanditAction choice = this.chooseAction();
		return choice.getAct();
	}

	public void receivePayoff(double payoff) {
		this.receiveFeedback(payoff);
	}
}

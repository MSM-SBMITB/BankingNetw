package banktestJ;

import java.util.ArrayList;
import java.util.Random;

import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.demo.RothErevAgent;
import edu.iastate.jrelm.rl.rotherev.REParameters;

import uchicago.src.sim.engine.Stepable;


/*public class BankAgentsPlus {
	
	
	
	
	public BankAgentsPlus(double e, double i, double d, double b, double c){
		this.e = e; //external assets
		this.i = i; // interbak assets
		this.d = d; //cust deposit
		this.b = b; // interbank borrowing
		this.c = c; // networth
		this.netWorth = (e+i)/d; //networth ratio
	}
	
	public BankAgentsPlus(){
		this.e = 0;
		this.i = 0;
		this.d = 0;
		this.b = 0;
		this.c = 0;
	}
	
	public void operate(boolean inShock, int time,ArrayList<Boolean> list, ArrayList<BankAgentsPlus> agentList ){
		regularOperation();
		shock(inShock, time);
		interbankInit(list,agentList);
		interbankOperation();
	}
	
	public void operate(boolean inShock, int time){
		regularOperation();
		shock(inShock, time);
		interbankOperation();
	}
	
	
	// this where the JReLM Roth-Erev implemented
	private void shock(boolean param, int time){
		if (param) { 
			if (time== 0){
				ShockMatrix X = new ShockMatrix();
				shockMtrx = X.get();}
			//bank respond to shock
			if(shockMtrx.get(time)< this.c){
			}
		}
		residualShock = 0;
	}
	
	private void regularOperation(){
		this.e = this.e + (this.e * Constants.drift* Constants.dT)  + (Constants.volatility* randNum.nextDouble()* Math.sqrt(Constants.dT));
		this.i = this.i*Math.exp(Constants.r_i);		
		this.d = this.d*Math.exp(Constants.r_d);
		this.b = this.b*Math.exp(Constants.r_b);
		this.c = (this.e+this.i)-(this.d-this.b);
	}
	

	private void interbankInit(ArrayList<Boolean> list, ArrayList<BankAgentsPlus> agentList){
		interbNetw = list;
		listAgent = agentList;
	}
	
	private void interbankOperation(){ 
		for (int i=0; i<listAgent.size();i++){
			for (int j=0; j<Constants.N;j++){
			//for (int j=0; j<list.size();j++){
				if (interbNetw.get(j)){
					System.out.println(interbNetw.get(j)); System.out.println(j);
					if (listAgent.get(j).netWorth<Constants.netWorthTreshold){	
						//bank take back lending ??
						listAgent.get(j).b = listAgent.get(j).b - Constants.w;
					}
					//else{
						// bank extend lending ???
					//}
					
				}
			}
		}
	}
	
	public double netWorth(){
		return (e+i)/d;
	}
	
	public void getNetwork(ArrayList<Boolean> in){
		interbNetw = in;
	}
}

}
*/

public class BankAgentsPlus extends RothErevAgent <BankAction> implements Stepable {

	private double e,i,d,b,c,a;
	private double netWorth, residualShock; 
	private ArrayList<Boolean> interbNetw = new ArrayList<Boolean>();
	private ArrayList<Integer> shockMtrx = new ArrayList<Integer>();
	//private ArrayList<BankAgentsPlus> listAgent = new ArrayList<BankAgentsPlus>();
	
	private String ID;
	private boolean failed = false; 
	
	//----------JRELM------------------------------------------------------------------------------
	public BankAgentsPlus(ActionDomainBankAgent domain, REParameters params, String agentID){
		super(params, domain, agentID);
		this.ID = agentID; 
	}
	
	public BankAgentsPlus(ActionDomainBankAgent domain, REParameters params){
		super(params, domain);
	}
	
	public int chooseFraction(){
		BankAction choice = this.chooseAction();
		return choice.getAct();
	}
	
	public void receivePayoff(double payoff) {
		this.receiveFeedback(payoff);
	}
	//----------------------------------------------------------------------------------------------
	

	
	private static double[] balanceSheet; 
	
	private double currentShock;
	
	private Random randNum = new Random();
	
	public void addBalanceSheet(double[] bal){
		this.e = bal[3]; //external assets
		this.i = bal[4]; // interbak assets
		this.d = bal[0]; //cust deposit
		this.b = bal[1]; // interbank borrowing
		this.c = bal[2]; // networth
		this.netWorth = (bal[3]+bal[4])/bal[0]; //networth ratio ??
	}
	
	public void getNetwork(ArrayList<Boolean> in){
		interbNetw = in;
	}
	
	private void regularOperation(){
		this.e = this.e + (this.e * Constants.drift* Constants.dT)  + (Constants.volatility* randNum.nextDouble()* Math.sqrt(Constants.dT));
		this.i = this.i*Math.exp(Constants.r_i* Constants.dT);		
		this.d = this.d*Math.exp(Constants.r_d* Constants.dT);
		this.b = this.b*Math.exp(Constants.r_b* Constants.dT);
		this.c = (this.e+this.i)-(this.d-this.b);
		
		//System.out.println(e+" | "+i+" | "+d+" | "+b+" | "+c);
	}
	
	// shock absorb
	
	// interbank RL
	private void interbankOperation(){ 
		for (int i=0; i<BankModel.agentList.size();i++){
			for (int j=0; j<Constants.N;j++){
			//for (int j=0; j<interbNetw.size();j++){
				if (interbNetw.get(j)){
					//System.out.println(interbNetw.get(j)); System.out.println(j);
					//if (BankModel.agentList.get(j).netWorth<Constants.netWorthTreshold){	
						//bank take back lending ??
						int fractionChoice = this.chooseFraction();
						double delta = fractionChoice*0.2*Constants.w; 
						this.i = this.i - delta;
						this.c = this.c + delta; 		
						BankModel.agentList.get(j).b = BankModel.agentList.get(j).b - delta;
						updateNetworth();
						receivePayoff(payoff(this.netWorth)); 
						
						System.out.println("bank "+ID+" taking "+ fractionChoice +" from bank " + j + " resulting nw " + netWorth );
						
					}
					//else{
						// bank extend lending ???
					//}
					
				}
			
		}
	}
	
	public void step(){
		if (!failed){
			regularOperation(); 
			interbankOperation();
		}
	}
	
	private int payoff(double a){
		
		if (a<=0){
			return -100;
		}
		else {
			return 1; 
		}
	}
	
	private void updateNetworth(){
		netWorth = (e+i)/d;
		
		if (netWorth <= 0){
			failed = true;
		}
	}
	
	
}




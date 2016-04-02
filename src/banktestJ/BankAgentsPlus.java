package banktestJ;

import java.util.ArrayList;
import java.util.Random;

import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.demo.RothErevAgent;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import uchicago.src.sim.engine.Stepable;
import umontreal.iro.lecuyer.stochprocess.GeometricBrownianMotion;

public class BankAgentsPlus extends RothErevAgent <BankAction> implements Stepable {

	private double e,i,d,b,c,a;
	private double netWorth, residualShock; 
	private ArrayList<Boolean> interbNetw = new ArrayList<Boolean>();
	private ArrayList<Integer> shockMtrx = new ArrayList<Integer>();
	
	public double[] assetList = new double[Constants.N];
	public double[] lenderList = new double[Constants.N];
	
	private GeometricBrownianMotion GBM;
	//private ArrayList<BankAgentsPlus> listAgent = new ArrayList<BankAgentsPlus>();
	
	private String ID;
	private boolean failed = false; 
	private boolean inShock = false;
	private boolean lowNetworth = false;
	
	private int t = 0; 
	
	private double sigma, mu;	
	
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
	
	//GBM = new GeometricBrownianMotion();
	
	private static double[] balanceSheet; 
	
	private double currentShock;
	
	private Random randNum = new Random();
	
	public void addBalanceSheet(double[] bal){
		this.e = bal[3]; //external assets
		this.i = bal[4]; // interbank assets
		this.d = bal[0]; //cust deposit
		this.b = bal[1]; // interbank borrowing
		this.c = bal[2]; // networth
		this.netWorth = (bal[2])/(bal[3]+bal[4]); //networth ratio ??
		//System.out.println(ID+" | "+ e+" | "+i+" | "+d+" | "+b+" | "+c + " | " + netWorth );
	}
	
	public void getNetwork(ArrayList<Boolean> borrower, ArrayList<Boolean> lender){
		assetList = networkSlot(borrower);
		lenderList = networkSlot(lender);
		
		checkConsistency();
	}
	
	private void regularOperation(){
		this.e = this.e + (this.e * Constants.drift* Constants.dT)  + (this.e*Constants.volatility* randNum.nextDouble()* Math.sqrt(Constants.dT));
		this.i = this.i*Math.exp(Constants.r_i* Constants.dT);		
		this.d = this.d*Math.exp(Constants.r_d* Constants.dT);
		this.b = this.b*Math.exp(Constants.r_b* Constants.dT);
		
		double deltaC = (this.e+this.i) - (this.d+this.b) ;
		this.c = this.c+deltaC; 
		this.a = this.e+ this.i;
		
		updateBalanceSheet();
		
		
		//--Print
		if (Integer.parseInt(ID)== 0){ // only print output of bank with number 0
		System.out.println( t + " | BankAgentPlus | "+ ID+" | "+ e+" | "+i+" | "+d+" | "+b+" | "+c +  " | " + netWorth  );}
	}
	
	private int randomBankNum = -1;
	
	private void shockAbsorb(){
		
		if (ShockMatrix.systemic) {
			// double[][] systemicShockMatrix = new double[52][2];
			if (ShockMatrix.systemicShockMatrix[t][1] != 0){
				mu = ShockMatrix.systemicShockMatrix[t][1] ; 
				sigma = ShockMatrix.systemicShockMatrix[t][2] ;
			}
			else{
				mu = Constants.drift; sigma = Constants.volatility; 
			}}
		
		else { //idiosynchratic
			//ArrayList<Double> shockMatrix = new ArrayList<Double>(52);
			
			if (randomBankNum < 0){
				randomBankNum = 0; //add random interger number generator
			}
			
			if (Integer.parseInt(ID)==randomBankNum && !inShock){
				inShock = true;
			}
			
			if (inShock){
				//idioSyncShock(ShockMatrix.shockMatrix.get(t)); // benerin
			}
		}
		updateNetworthRatio(); 
	}
	
	private void idioSyncShock(double param){
		double residualShock = param; 
		
		if(residualShock < this.c ){
			this.c = this.c - residualShock;
		}
		else {
			residualShock = residualShock- this.c;
			this.c = 0;
			
			failed = true;
			
			if(residualShock<this.b){
				this.b = this.b - residualShock;
				transmitShocktoLender(); // transmit to bank that give lending 
			}
			else{
				residualShock = residualShock - this.b;
				this.b = 0;
				
				this.d = this.d - residualShock; 
			}
		}
		
	}
	
	private void transmitShocktoLender(){
		double fraction = this.b/arraysum(lenderList);
		
		for(int i=0; i < lenderList.length; i++){
			lenderList[i] = lenderList[i]*fraction;
			BankModel.agentList.get(i).assetList[Integer.parseInt(ID)] = BankModel.agentList.get(i).assetList[Integer.parseInt(ID)]*fraction;
			
		}
		
	}
	
	// interbank RL
	private void interbankOperation(){ 
		//for (int i=0; i<BankModel.agentList.size();i++){
			for (int j=0; j<Constants.N;j++){
				if (assetList[j]>1E-3 && lowNetworth){
						if (!(BankModel.agentList.get(j).failed)){
							int fractionChoice = this.chooseFraction();
							double delta = fractionChoice*0.1*assetList[j];
							assetList[j] = assetList[j]-delta;  //update assetList
							//this.i = this.i - delta;
							this.c = this.c + delta;
							BankModel.agentList.get(j).lenderList[j] = assetList[j]; //update lenderList in another bank	
							updateBalanceSheet();		
							receivePayoff(payoff(this.netWorth)); 
							
							if (Integer.parseInt(ID)== 0){ // only print output of bank with number 0
							System.out.println(ID + " taking " + delta + " from " + j + " resulting networth " + netWorth );
							System.out.println( t + " | BankAgentPlus | "+ ID+" | "+ e+" | "+i+" | "+d+" | "+b+" | "+c +  " | " + netWorth  );}
						}					
				}}}
			
		//}
	
	public void step(){
		//updateBalanceSheet();
		if(!failed){
			if (ShockMatrix.systemic) {
				shockAbsorb();
				regularOperation(); 
			}
			else{
				regularOperation(); 
				shockAbsorb();
			}
			if(netWorth<0.03){
				interbankOperation();
				if (Integer.parseInt(ID)== 0){
				System.out.println("step | lowNetworth " + lowNetworth);}
			}
		}	
		t = t+1;		
	}
	
	private double payoff(double a){
		//return (0.1*Math.exp(2.3026*a));
		return ((110*a) - 100);
	}
	private void updateNetworthRatio(){
		netWorth = c/(e+i);
		
		if (netWorth <= 0 || a <= 0 ){
			failed = true;
		}
	}
	private void checkConsistency(){
		if(i == arraysum(assetList)){
			System.out.println(ID + " assetList checked");
		}
		
		if(b == arraysum(lenderList)){
			System.out.println(ID + " lenderList checked");
		}
	}
	private void updateBalanceSheet(){
		
		round (i, 5); round (b, 5); round (d, 5); round (e, 5); // rounding   
		
		i = arraysum(assetList);
		b = arraysum (lenderList);
		a= e+i;
		c = a - (d+b);
		netWorth = c/a;
		if (netWorth < 0) {failed = true;}
		if (netWorth < Constants.netWorthTreshold){ lowNetworth = true; }
		else {lowNetworth = false;}
		
		
		
		
	}
	private double[] networkSlot(ArrayList<Boolean> A){
		//interbNetw
		//interbAssetSlot
		
		double[] B = new double[A.size()];
		
		for (int i=0; i< A.size(); i++){
			if(A.get(i)){
				B[i] = Constants.w; 
			}
			else {
				B[i]=0;
			}}
		
		return B;
	}
	private double arraysum(double[] A){
		double sum = 0;
		
		for(int i =0; i < A.length ; i++){
			sum = sum + A[i];
		}
		
		return sum;
	}
	
	private static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
	
}




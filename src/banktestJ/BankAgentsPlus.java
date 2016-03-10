package banktestJ;

import java.util.ArrayList;
import java.util.Random;


public class BankAgentsPlus {
	
	private double e,i,d,b,c,a;
	private double netWorth, residualShock; 
	private ArrayList<Boolean> interbNetw = new ArrayList<Boolean>();
	private ArrayList<Integer> shockMtrx = new ArrayList<Integer>();
	private ArrayList<BankAgentsPlus> listAgent = new ArrayList<BankAgentsPlus>();
	
	private double currentShock;
	
	private Random randNum = new Random();
	
	
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

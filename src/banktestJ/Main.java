package banktestJ;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
	
	static ArrayList<BankAgents> agentList = new ArrayList <BankAgents>();
	static ArrayList<Integer> list = new ArrayList <Integer>();

	static Network netw;
	
	static int currentBankNum;
	
	


	//---------------MISC FUNCTION------------------------------------------------------------------------------------------------------------------------
	//shuffle list
	private static ArrayList<Integer> ShuffleList(int a) {
		for (int i=0; i<a;i++ ){
			list.add(i);
		}
		Collections.shuffle(list);
		return list;
	}
	
	private static ArrayList<Boolean> passingNetwork (int a){
		ArrayList<Boolean> pass = new ArrayList<Boolean>();
		pass = netw.getNetwork(a);
		return pass;
	}
	
	//----------------------------------------------------------------------------------------
	
	
	private static void init(){
		netw = new Network();
		new BalanceSheet();
		//adding bank agents
		for(int i=0; i<Constants.N; i++){
			agentList.add(new BankAgents(Constants.bsG[i][0], Constants.bsG[i][1], Constants.bsG[i][2], Constants.bsG[i][3], Constants.bsG[i][4]));
			//agentList.getNetw(netw.getNetwork(i));
		}
	}
	
	private static void run() {
		for (int t=0; t<Constants.T; t++){	
			int a = agentList.size();
			ArrayList<Integer> sequence = ShuffleList(a);
			for(int i=0; i<a;i++)
			{
				currentBankNum = sequence.get(i);
				System.out.println(t);
				if (t==0){
					agentList.get(currentBankNum).operate(Constants.inShock, t,passingNetwork(currentBankNum), agentList);
				}
				else{
					agentList.get(currentBankNum).operate(Constants.inShock, t);
				}
			}			
		}
	}
	
	public static void main1(String[] args) {
		init(); 
		run(); 
	}	
		
}

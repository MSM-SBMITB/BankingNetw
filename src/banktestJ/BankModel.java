package banktestJ;

import java.util.ArrayList;

import javax.swing.JFrame;

import cern.colt.Arrays;
import edu.iastate.jrelm.core.BasicLearnerManager;
import edu.iastate.jrelm.gui.BasicSettingsEditor;
import edu.iastate.jrelm.rl.rotherev.REParameters;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimpleModel;

public class BankModel extends SimpleModel {
	
	//private BankAgentsPlus bank; 
	
	private REParameters bankParams;
	private ActionDomainBankAgent bankDomain; 
	
	// GUI Components
	private BasicLearnerManager learnManager;
	private BasicSettingsEditor learnEditor;
	JFrame learnWindow;
	
	// Bank Related
	public static ArrayList<BankAgentsPlus> agentList = new ArrayList <BankAgentsPlus>();
	static ArrayList<Integer> list = new ArrayList <Integer>();

	private static Network netw;
	private static  BalanceSheet balanceSheet; 
	//private static int currentBankNum;
	public static ShockMatrix Shock;
	//private ArrayList <BankAgentsPlus> banks;
	
	
	public BankModel(){
		
	}
	
	public void setup(){
		
		init();
		
		super.setup();
		if (learnWindow != null)
			learnWindow.dispose();
		shuffle = true; 
		autoStep = true;
		
		bankDomain = new ActionDomainBankAgent(); 
		//bankParams =  new REParameters(100, 0.55, 500, 0.3, 8780632); //if using boltzmann dist
		bankParams =  new REParameters(); // using proportional probability at initial
		
		for (int i=0; i< Constants.N; i++){
			agentList.add(new BankAgentsPlus(bankDomain,bankParams, String.valueOf(i)));
			agentList.get(i).addBalanceSheet(balanceSheet.getBalanceSheet(i));
			agentList.get(i).getNetwork(netw.getBorrowerList(i), netw.getLenderList(i));
			super.agentList = BankModel.agentList;
		}
		
		//super.stoppingTime = 52; // set stopping time
		
		
	}
	
	public void buildModel(){
		// *** JReLM initialization
		learnManager = new BasicLearnerManager();
		
		for (int i=0; i< Constants.N; i++){
			learnManager.register(BankModel.agentList.get(i));
		}	
		
		learnEditor = new BasicSettingsEditor(learnManager);
		
		getController().addSimEventListener(learnEditor);
		System.out.println(getController().getClass().toString());
		
		// Display JReLM settings GUI before the model is run
		learnWindow = new JFrame("JReLM Settings");
		learnWindow.add(learnEditor);
		learnWindow.setLocation(250, 0);
		learnEditor.display();
		learnWindow.setSize(200, 300);
		learnWindow.setVisible(true);
		
		System.out.println("BankModel built...\n");
		
	}
	
	protected void preStep(){
		super.agentList = agentList; 
	}
	
	
	protected void step(){ }// step disini ga dipake, lihat di agent
	
	protected void postStep(){
		super.agentList = agentList; 
	}


	public static void main(String[] args) {
		SimInit init = new SimInit();
		BankModel model = new BankModel(); 
		init.loadModel(model, "", false);

	}
	
	private static void init(){
		netw = new Network(); // create network
		balanceSheet = new BalanceSheet(); // create balance sheet for entire bank systen
		Shock = new ShockMatrix(); // creating matrix shock setting
		Shock.shock = false; 
		
		//printArray(Constants.bsG);
		//printArray1(Constants.nw);
		
	}
	
	//------------------------------------------misc

	private static void printArray(double[][] A){
	
		for(int i=0; i < 5; i++){
			for(int j=0; j< Constants.N; j++)
			{
				if (j==4){
					System.out.println(A[i][j]);
				}
				else {
					System.out.print(A[i][j] + " | ");
				}
			}
		}
		
	}
	
	private static void printArray1(boolean[][] A){
		
		for(int i=0; i < Constants.N; i++){
			for(int j=0; j< Constants.N; j++)
			{
				if (j==Constants.N-1){
					System.out.println(A[i][j]);
				}
				else {
					System.out.print(A[i][j] + " | ");
				}
			}
		}
		
	}
}

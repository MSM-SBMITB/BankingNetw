package banktestJ;

import java.util.ArrayList;

import javax.swing.JFrame;

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
	private static int currentBankNum;
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
		bankParams =  new REParameters(100, 0.55, 500, 0.3, 8780632);
		
		for (int i=0; i< Constants.N; i++){
			agentList.add(new BankAgentsPlus(bankDomain,bankParams, String.valueOf(i)));
			agentList.get(i).addBalanceSheet(balanceSheet.getBalanceSheet(i));
			agentList.get(i).getNetwork(netw.getNetwork(i));
			super.agentList = BankModel.agentList;
		}
		
		
		
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
	
	
	protected void step(){
		System.out.println("step");
		//int fractionChoice = bank.chooseFraction();
		//int payoff = 0; // bikin mekanisme payoffnya input pake fraction choice 
		//bank.receivePayoff(payoff);
		
	}
	
	protected void postStep(){
		
	}


	public static void main(String[] args) {
		SimInit init = new SimInit();
		BankModel model = new BankModel(); 
		init.loadModel(model, "", false);

	}
	
	private static void init(){
		netw = new Network();
		balanceSheet = new BalanceSheet();
	}

}

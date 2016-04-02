package banktestJ;

import java.util.ArrayList;

public class ShockMatrix {
	
	private ArrayList<Integer> Shock = new ArrayList<Integer>();
	public boolean shock = false; 
	public static boolean systemic = false; 
	public static ArrayList<Double> shockMatrix = new ArrayList<Double>(52);
	public static double[][] systemicShockMatrix = new double[52][2];
	//private static double initMu,initSigma, shockMu, shockSigma;
	
	
	public ArrayList<Integer> get(){
		return Shock;
	}
	
	public void addShock(){
		if (shock) {
			if (systemic){
				
			}
			else{
				//idiosynchratic();
			}
		}
	}
	
	public static void addShock(int startT, int endT, double param){idiosynchratic(startT,endT,param);}//specified time range for idiosynchratic shock
	public static void addShock(int T, double param){idiosynchratic(T-1,T,param);} //specified time for idiosynchratic shock
	public static void addShock(int startT, int endT, double mu, double sigma){setSystemicShock(startT,endT,mu,sigma);} //specified time for systemic shock
	public static void addShock(double mu, double sigma){setSystemicShock(0,52,mu,sigma);}
	
	private static void setSystemicShock (int startT, int endT, double mu, double sigma){
		for (int i=startT; i <= endT; i++){
			systemicShockMatrix[i][1]=	mu;
			systemicShockMatrix[i][2]=	sigma;
			}
	}
	
	private static void idiosynchratic(int startT, int endT, double param ){
		// create shock matrix; 
		for (int i = startT; i <= endT; i++){
			shockMatrix. set(i,param);
		}
	}
	
	//add shock setup, systemic or idiosychratic
	public void setup( boolean A){ 
		shock = true;
		systemic = true;
	}
	
	
	public ShockMatrix(){
		// creating shock matrix
		for(int i=0; i<Constants.T; i++){
			Shock.add(i);
		}
	}

}

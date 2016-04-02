package banktestJ;
//initializing banking network

import java.util.ArrayList;
import uchicago.src.sim.util.Random;
//import java.util.Random;

public class Network {
	
	 
	
	static double rand; 

	private static int N = Constants.N;
	private static boolean[][] netw = new boolean[N][N];
	private static double p = Constants.p;
	public static int[][] inOutDegree = new int[2][N]; // 0=out; 1=in
	
	private static ArrayList<Boolean> connectionList = new ArrayList<Boolean>();
	private static ArrayList<Boolean> connectionList1 = new ArrayList<Boolean>();

	private static void Initialize() {
		//Random random = new Random();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				 rand = Random.uniform.nextDoubleFromTo(0.00,1.00);
				if (rand < p && i != j) {
					netw[i][j] = true;
					inOutDegree[0][i]++;//i
					inOutDegree[1][j]++;//b
					Constants.Z++;
				}
			}
		}
	}

	public ArrayList<Boolean> getBorrowerList (int a){	
		for (int i=0; i < N; i++){
			connectionList.add(netw[a][i]);
		}		
		return connectionList;
	}
	
	public ArrayList<Boolean> getLenderList (int a){	
		for (int i=0; i < N; i++){
			connectionList1.add(netw[i][a]);
		}		
		return connectionList1;
	}
	
	public Network() {
		Initialize();
		Constants.nw = netw;
	}

}

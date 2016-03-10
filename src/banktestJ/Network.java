package banktestJ;
//initializing banking network

import java.util.ArrayList;
//import repast.simphony.random.RandomHelper;
import java.util.Random;

public class Network {

	private static int N = Constants.N;
	private static boolean[][] netw = new boolean[N][N];
	private static double p = Constants.p;
	public static int[][] inOutDegree = new int[2][N]; // 0=out; 1=in
	
	private static ArrayList<Boolean> connectionList = new ArrayList<Boolean>();

	private static void Initialize() {
		Random random = new Random();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (random.nextDouble() < p && i != j) {
					netw[i][j] = true;
					inOutDegree[0][i]++;
					inOutDegree[1][j]++;
					Constants.Z++;
				}
			}
		}
	}

	public ArrayList<Boolean> getNetwork (int a){	
		for (int i=0; i < N; i++){
			connectionList.add(netw[a][i]);
		}		
		return connectionList;
	}
	
	public Network() {
		Initialize();
		Constants.nw = netw;
	}

}

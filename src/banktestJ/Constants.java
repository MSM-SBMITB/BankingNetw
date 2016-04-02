package banktestJ;

public class Constants {
	
	// detail of simulation
	public static final boolean systemic = true;
	

	public static final int N = 20; // number of bank
	
	public static double drift = 0.12/100;
	public static  double volatility = 0.19/100;
	public static final double dT = 1.0/52.0;

	public static final int E = 100000;
	public static final double p = 0.4;

	public static final double theta = 0.2;
	public static final double gamma = 0.05;
	public static final double threshold_nw = 0.03;

	public static double r_b = 0.2;
	public static double r_i = 0.08;
	public static double r_d = 0.0004; //per tick

	public static double Z = 0.00;

	public static boolean[][] nw = new boolean[N][N];

	public static double[][] bsG = new double[N][5]; // balanceSheetGlobal
	
	public static boolean inShock = true;
	
	public static double netWorthTreshold = 0.2;
	
	public static final int T = 52;
	
	public static double w = 0; 
}

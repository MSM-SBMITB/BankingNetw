package banktestJ;


public class BalanceSheet {

	private static double E = Constants.E;
	private static int N = Constants.N;
	private static double gamma = Constants.gamma;
	private static double I, w;

	private static double[] e = new double[N];
	private static double[] i = new double[N];
	private static double[] d = new double[N];
	private static double[] b = new double[N];
	private static double[] c = new double[N];
	private static double[] a = new double[N];

	private static double sumE = 0;
	private static double e_ = 0;

	private static void initialize() {
		I = (E * Constants.theta) / (1.00 - Constants.theta);
		w = I / Constants.Z;
		
		Constants.w = w;

		for (int i_ = 0; i_ < Constants.N; i_++) {
			i[i_] = Network.inOutDegree[0][i_] * w;
			b[i_] = Network.inOutDegree[1][i_] * w;
			sumE = sumE + b[i_];
			
			//System.out.println(i[i_]+" | "+b[i_]);
		}

		e_ = (E - sumE) / N;

		for (int i_ = 0; i_ < Constants.N; i_++) {
			e[i_] = b[i_] - i[i_] + e_;

			a[i_] = (e[i_] + i[i_]);

			c[i_] = gamma * a[i_];
			d[i_] = a[i_] - c[i_] - b[i_];
			
			//System.out.println(e[i_]+" | "+d[i_]);
		}
	}

	public double[] getBalanceSheet(int x) {
		double dats[] = { d[x], b[x], c[x], e[x], i[x] };
		return (dats);
	}

	private static void putToGlobal() {
		for (int k = 0; k < 5; k++) {
			for (int j = 0; j < N; j++) {
				if (k == 0) {
					Constants.bsG[j][k] = e[j];
				} else if (k == 1) {
					Constants.bsG[j][k] = i[j];
				} else if (k == 2) {
					Constants.bsG[j][k] = d[j];
				} else if (k == 3) {
					Constants.bsG[j][k] = b[j];
				} else if (k == 4) {
					Constants.bsG[j][k] = c[j];
				}
			}
		}
	}
	
	private static void checkValue(){
		boolean check = true;
		
		for (int k = 0; k < 5; k++) {
			for (int j = 0; j < N; j++) {
			if(Constants.bsG[j][k]<0){if (!check){check=!check;}}
			}}
		if (!check){init();System.out.println("BalanceSheet | balancesheet contain negative; recalculating ");}
	}
	
	public static void init(){
		initialize();
		putToGlobal();	
		checkValue();
	}

	public BalanceSheet() {
		init();
		
	}

}

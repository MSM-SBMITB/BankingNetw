package banktestJ;

import java.util.ArrayList;

public class ShockMatrix {
	
	private ArrayList<Integer> Shock = new ArrayList<Integer>();
	
	public ArrayList<Integer> get(){
		return Shock;
	}
	
	public ShockMatrix(){
		// creating shock matrix
		for(int i=0; i<Constants.T; i++){
			Shock.add(i);
		}
	}

}

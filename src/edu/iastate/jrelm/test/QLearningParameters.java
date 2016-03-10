package edu.iastate.jrelm.test;

import java.util.Hashtable;

import edu.iastate.jrelm.rl.RLParameters;

public class QLearningParameters implements RLParameters {

	private String ALGORITHM_NAME = "Q-Learning";
	private String[] parameterNames = { "Spin", "Color", "Flavor" };
	private Hashtable descriptors = new Hashtable();

	private double spin = 0;
	private String color = "Blue";
	private String flavor = "partched";

	protected void setAlgorithmName(String algName) {
		ALGORITHM_NAME = algName;
	}

	public QLearningParameters() {

	}

	public QLearningParameters(String bah) {

	}

	public String getName() {
		return ALGORITHM_NAME;
	}

	public String[] getParameterNames() {
		return parameterNames;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFlavor() {
		return flavor;
	}

	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	public double getSpin() {
		return spin;
	}

	public void setSpin(double spin) {
		this.spin = spin;
	}

	// public Hashtable getDescriptors() {
	// return descriptors;
	// }

	public String toString() {
		return getName();
	}

	public boolean validateParameters() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getRandomSeed() {
		// TODO Auto-generated method stub
		return 0;
	}

}

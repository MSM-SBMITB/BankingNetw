package edu.iastate.jrelm.demo;

import java.util.ArrayList;

import edu.iastate.jrelm.core.ActionDomain;
import edu.iastate.jrelm.rl.rotherev.RELearner;
import edu.iastate.jrelm.rl.rotherev.REParameters;



public class Demo {
	
	private static ArrayList<Integer> A = new ArrayList<Integer>();//actionDomain

	ActionDomain<Integer, A> domain;
	// REPolicy<Integer,A> policy;
	REParameters learningParams;
	RELearner<Integer, A> learner;
	
	
	private RothErevAgent agent1(A);
	
	
	private static void createAgent(){
		
	}
	
	private static void createActionDomain(){
		for(int i = 0; i<4; i++){
			A.add(i);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		createActionDomain();
		createAgent();

	}

}

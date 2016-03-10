package edu.iastate.jrelm.test;

import edu.iastate.jrelm.core.Action;
import edu.iastate.jrelm.similarity.DissimilarityMeasure;

public class FakeDissimilarity implements DissimilarityMeasure<Action, Double> {

	public Double dissimilarity(Action action1, Action action2) {
		// TODO Auto-generated method stub
		return new Double(0.5);
	}

}

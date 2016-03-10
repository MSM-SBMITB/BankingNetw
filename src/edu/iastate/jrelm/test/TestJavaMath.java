package edu.iastate.jrelm.test;

import java.math.BigDecimal;
import java.math.MathContext;

public class TestJavaMath {

	public TestJavaMath() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("TestJavaMath");
		double doubleSum = 0.0;
		for (int k = 0; k < 20; k++) {
			doubleSum = doubleSum + 0.05;
		}
		System.out.println("doubleSum = " + doubleSum);

		int intSum = 0;
		for (int k = 0; k < 20; k++) {
			intSum = intSum + 1;
		}
		System.out.println("intSum = " + intSum);

		float floatSum = 0;
		floatSum = (float) 0.5;
		System.out.println(floatSum);
		floatSum = 0;
		for (int k = 0; k < 20; k++) {
			floatSum = floatSum + (float) 0.0500;
		}
		System.out.println("floatSum = " + floatSum);

		MathContext mc = new MathContext(50);
		BigDecimal bdSum = new BigDecimal(0.0 + "", mc);
		BigDecimal bd = new BigDecimal(0.05 + "", mc);
		System.out.println("bd = " + bd.toString());
		for (int k = 0; k < 20; k++) {
			bdSum = bdSum.add(bd);
		}
		System.out.println("BigDecimalSum = " + bdSum.toString());

	}

}

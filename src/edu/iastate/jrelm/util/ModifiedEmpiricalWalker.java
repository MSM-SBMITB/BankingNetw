/*
EmpiricalWalker is part of Cern's Open Source Libraries for High Performance Scientific and Technical 
Computing in Java (Colt 1.2.0), Copyright � 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
 */
package edu.iastate.jrelm.util;

import java.util.Stack;

import cern.jet.random.Empirical;
import cern.jet.random.EmpiricalWalker;
import cern.jet.random.engine.RandomEngine;

/**
 * 
 * A modified version of the EmpricialWalker from Cern's Open Source Libraries
 * for High Performance Scientific and Technical Computing in Java (Colt 1.2.0).
 * Changes have been made to the original EmpiricalWalker to avoid problems
 * encountered when probability distribution functions with uniform relative
 * values are used.
 * 
 * @author Charles Gieseler
 *
 * @see cern.jet.random.EmpiricalWalker
 */
public class ModifiedEmpiricalWalker extends EmpiricalWalker {

	/**
	 * Flag to use linear interpolation.
	 * 
	 * @see cern.jet.random.Empirical#LINEAR_INTERPOLATION
	 *      cern.jet.random.EmpiricalWalker
	 */
	public static int LINEAR_INTERPOLATION = Empirical.LINEAR_INTERPOLATION;
	/**
	 * Flag to use no interpolation.
	 * 
	 * @see cern.jet.random.Empirical#NO_INTERPOLATION
	 *      cern.jet.random.EmpiricalWalker
	 */
	public static int NO_INTERPOLATION = Empirical.NO_INTERPOLATION;

	public ModifiedEmpiricalWalker(double[] pdf, int interpolationType,
			RandomEngine randomGenerator) {
		super(pdf, interpolationType, randomGenerator);
		setState(pdf, interpolationType);
		setState2(pdf);
	}

	/**
	 * Modified from EmpricalWalker so that pdf values returned are not negative
	 * and there is a value for each index in the original pdf (i.e. no
	 * ArrayIndexOutOfBoundsException when k=0).
	 * 
	 * Returns the probability distribution function.
	 * 
	 * @see cern.jet.random.EmpiricalWalker#pdf(int)
	 *      cern.jet.random.EmpiricalWalker
	 */
	public double pdf(int k) {
		if (k < 0 || k >= cdf.length - 1)
			return 0.0;
		return cdf[k + 1] - cdf[k];
	}

	/**
	 * Sets the distribution parameters. The <tt>pdf</tt> must satisfy all of
	 * the following conditions
	 * <ul>
	 * <li><tt>pdf != null && pdf.length &gt; 0</tt>
	 * <li><tt>0.0 &lt;= pdf[i] : 0 &lt; =i &lt;= pdf.length-1</tt>
	 * <li><tt>0.0 &lt; Sum(pdf[i]) : 0 &lt;=i &lt;= pdf.length-1</tt>
	 * </ul>
	 * 
	 * @param pdf
	 *            probability distribution function.
	 * @throws IllegalArgumentException
	 *             if at least one of the three conditions above is violated.
	 * 
	 * @see EmpiricalWalker#setState(double[], int)
	 */
	public void setState(double[] pdf, int interpolationType) {
		// System.err.println("EmpiricalWalker.setState():");
		if (pdf == null || pdf.length == 0) {
			throw new IllegalArgumentException(
					"Non-existing probability distribution function (pdf == null or pdf.length == 0)");
		}

		// compute cumulative distribution function (cdf) from probability
		// distribution function (pdf)
		int nBins = pdf.length;
		this.cdf = new double[nBins + 1];

		cdf[0] = 0;
		for (int i = 0; i < nBins; i++) {
			if (pdf[i] < 0.0)
				throw new IllegalArgumentException("Negative probability");
			cdf[i + 1] = cdf[i] + pdf[i];
			// System.err.println("pdf["+i+"]: "+pdf[i]+"  cdf["+i+"]: "+cdf[i]);
		}
		if (cdf[nBins] <= 0.0)
			throw new IllegalArgumentException(
					"At leat one probability must be > 0.0");
		// now normalize to 1 (relative probabilities).

		int cTotal = (int) Math.round(cdf[nBins]);
		for (int i = 0; i < nBins + 1; i++) {
			cdf[i] /= cTotal;
			// System.out.println("normalized cdf["+i+"]: "+cdf[i]);
		}
		// cdf is now cached...
	}

	/**
	 * Modified from EmpiricalWalker to allow nextInt to work properly when the
	 * given pdf contains uniform values.
	 * 
	 * Sets the distribution parameters. The <tt>pdf</tt> must satisfy both of
	 * the following conditions
	 * <ul>
	 * <li><tt>0.0 &lt;= pdf[i] : 0 &lt; =i &lt;= pdf.length-1</tt>
	 * <li><tt>0.0 &lt; Sum(pdf[i]) : 0 &lt;=i &lt;= pdf.length-1</tt>
	 * </ul>
	 *
	 * @param pdf
	 *            probability distribution function.
	 * @throws IllegalArgumentException
	 *             if at least one of the three conditions above is violated.
	 * 
	 * @see EmpiricalWalker#setState2(double[])
	 */
	public void setState2(double[] pdf) {
		int size = pdf.length;
		int k, s, b;
		int numBigs, numSmalls;
		Stack<Integer> stackOfBigs;
		Stack<Integer> stackOfSmalls;
		double[] E;
		int pTotal = 0;
		double mean = 0.0;
		double d;

		// System.out.println("EmpiricalWalker.setState2");

		// if (size < 1) {
		// throw new
		// IllegalArgumentException("must have size greater than zero");
		// }
		/*
		 * Make sure elements of ProbArray[] are positive. Won't enforce that
		 * sum is unity; instead will just normalize
		 */
		/*
		 * >>>>>O-:-O Changed from EmpiricalWalker O-:-O<<<<< When the pdf
		 * values are relative, their sum may be slightly above 1 due to loss of
		 * precission in adding doubles. In the case of a relative pdf
		 * containing uniform values, this will result a slight error when the
		 * values are re-normalized into in the array E[]. Each value in E[]
		 * will then be less than the mean and classified as a small. Then A[]
		 * will be improperly initialized with pdf.length-1 as it's last value
		 * and 0 for all other values. The final result is that nextInt will
		 * almost always return 0, sometimes pdf.length-1, and NEVER any value
		 * in between.
		 */
		double tempTotal = 0.0;
		for (k = 0; k < size; ++k) {
			// if (pdf[k] < 0) {
			// throw new
			// IllegalArgumentException("Probabilities must be >= 0: "+pdf[k]);
			// }
			tempTotal += pdf[k];
		}

		// >>>>>O-:-O Here we round pTotal to compensate for floating point
		// error accrued
		// over repeated summations. If pdf values are relative probabilities,
		// this should
		// make pTotal be 1.
		pTotal = (int) Math.round(tempTotal);

		/* Begin setting up the internal state */
		this.K = size;
		this.F = new double[size];
		this.A = new int[size];

		// normalize to relative probability
		E = new double[size];
		for (k = 0; k < size; ++k) {
			E[k] = pdf[k] / (double) pTotal;
			// System.out.println("	E["+k+"]: "+ E[k]);
		}
		// System.out.println("");
		/* Now create the Bigs and the Smalls */
		mean = 1.0 / (double) pdf.length;
		// System.out.println("	mean: "+mean);
		numSmalls = 0;
		numBigs = 0;
		for (k = 0; k < size; ++k) {
			if (E[k] < mean)
				++numSmalls;
			else
				++numBigs;
		}
		// System.out.print("	nSmalls: "+nSmalls);
		// System.out.println("	nBigs: "+nBigs);
		stackOfBigs = new Stack<Integer>();
		stackOfSmalls = new Stack<Integer>();
		for (k = 0; k < size; ++k) {
			if (E[k] < mean) {
				stackOfSmalls.push(k);
			} else {
				stackOfBigs.push(k);
			}
		}
		/* Now work through the smalls */
		while (!stackOfSmalls.empty()) {
			s = stackOfSmalls.pop();
			// System.out.println("	s: "+s);
			if (stackOfBigs.size() == 0) {
				/* Then we are on our last value */
				this.A[s] = s;
				// System.out.println(" original A["+s+"]: "+A[s]);
				this.F[s] = 1.0;
				break;
			}
			b = stackOfBigs.pop();
			this.A[s] = b;
			this.F[s] = size * E[s];

			d = mean - E[s];
			E[s] = E[s] + d; /* now E[s] == mean */
			E[b] = E[b] - d;
			if (E[b] < mean) {
				stackOfSmalls.push(b); /*
										 * no longer big, join ranks of the
										 * small
										 */
			} else if (E[b] > mean) {
				stackOfBigs.push(b); /* still big, put it back where you found it */
			} else {
				/* E[b]==mean implies it is finished too */
				this.A[b] = b;
				this.F[b] = 1.0;
			}
		}

		while (stackOfBigs.size() > 0) {
			b = stackOfBigs.pop();
			this.A[b] = b;
			this.F[b] = 1.0;
		}
	}

	public void setRandomEngine(RandomEngine engine) {
		setRandomGenerator(engine);
	}
}

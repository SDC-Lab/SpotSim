package org.cloudbus.spotsim.random;

import java.util.Random;

import umontreal.iro.lecuyer.randvar.NormalGen;
import umontreal.iro.lecuyer.randvarmulti.MultinormalCholeskyGen;
import umontreal.iro.lecuyer.rng.LFSR258;
import umontreal.iro.lecuyer.rng.RandomStream;
/*************************************************************************
 * Generate random variables with mixture of gaussians distribution. The
 * approach is composition method.
 * This class needs SSJ library. To download visit
 * http://www.iro.umontreal.ca/~simardr/ssj/
 * 
 *  @author Bahman Javadi
 * 
 * 
 *************************************************************************/

public class MixtureOfGaussians {

	static NormalGen ngen;

	public static void init(){
		
		RandomStream stream = new LFSR258();	
		ngen = new NormalGen(stream);
		
	}
	
    public static double nextGaussian(Random random,MixtureModel mixtureModel ) {

	int k = mixtureModel.getK();
	double[] p = mixtureModel.getP();
	double[] mu = mixtureModel.getMu();
	double[] sigma = mixtureModel.getSigma();
	
	double rnd;
	int i;
	double pr;
	
	rnd = random.nextDouble();
	// this is for exponential case
	if (k==0){
		return -(Math.log(rnd)*mu[0]); 
	}
		

	i = 0;
	pr = p[i];
	while (i <= k) {
	    if (rnd <= pr) {
		break;
	    }
	    i++;
	    pr += p[i];
	}

	double[] value = new double[1];
	double[] mui = {mu[i]};
	double[][] sigma2 = {{sigma[i]}};
	
	do {
	
		MultinormalCholeskyGen.nextPoint(ngen, mui,sigma2,value);
		
	} while (value[0] < 0);
	return value[0];
    }
}
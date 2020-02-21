package ch.epfl.rigel.math;

import java.util.Locale;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */

public final class Polynomial {
	
	private double[] coeffs;
	
	private Polynomial(double coefficientN, double...others) {
		coeffs= new double [others.length+1];
		coeffs[0]=coefficientN;
		for(int i = 0; i<others.length; ++i) {
			coeffs[i+1]=others[i];
		}
		
	}
    /**
     * Creates a polynome [-size/2,size/2[
     * @param double : highest degree coeficient
     * * @param double... : other coefficients
     * @throws :IllegalArgumentException if highest degree coefficient is 0
     * @return polynome
     */
	public static Polynomial of(double coefficientN, double... coefficients) {
		if(coefficientN==0)
			throw new IllegalArgumentException();
		//Copying array to ensure immutability
		double [] others = new double [coefficients.length]; 
		for(int i=0;i<coefficients.length;++i) {
			others[i]=coefficients[i];
		}
		return new Polynomial(coefficientN,others);
	}
	
	
    /**Evaluates the polynome at a specified value
     * @param double : value to calculate
     * @return double : evaluation  
     */
	public double at(double x) {
		double result = coeffs[0];
		for(int i=1 ; i<coeffs.length;++i) {
			result = result * x + coeffs[i];
		}
		return result;
	}
	
    @Override
    public String toString() {
    	String out="";
    	for(int i=0;i<coeffs.length;++i) { //Building up the string
    		if(coeffs[i]==0) // No need to print if the coefficient is 0 
    			continue;
    		if(i!=0) {
    			if(coeffs[i]>0)	//Adding the plus symbol
    				out+="+";
    			else
        			out+="-";
    		}
    		else if(coeffs[i]<0)
    			out+="-";
    		if(Math.abs(coeffs[i])!=1) {
    			if(coeffs[i]-Math.floor(coeffs[i])==0)//Checking if the number is natural and printing it appropriately 
    				out+= String.format(Locale.ROOT,"%.1f", Math.abs(coeffs[i]));
    			else
    				out+= String.format(Locale.ROOT,"%s", Math.abs(coeffs[i]));		
    		} 
    		if(i!=coeffs.length-1)out +="x";
    		if(i!=coeffs.length-2 && i!=coeffs.length-1) out+="^"+(coeffs.length-i-1); // printing the power (except 0)

    	}
        return out;
    }
    
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException(); 
    }
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException(); 
    }
}

package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */

public final class Polynomial {
	
	private final double[] coeffs;
	
	private Polynomial(double coefficientN, double...others) {
		coeffs= new double [others.length+1];
		coeffs[0]=coefficientN;
		System.arraycopy(others, 0, coeffs, 1, others.length);
		
	}
    /**
     * Creates a polynom [-size/2,size/2[
     * @param   highest degree coefficient
     * * @param  other coefficients
     * @throws IllegalArgumentException if highest degree coefficient is 0
     * @return polynom
     */
	public static Polynomial of(double coefficientN, double... coefficients) {
		
	    Preconditions.checkArgument(coefficientN !=0);
		return new Polynomial(coefficientN,coefficients);
	}
	
	
    /**Evaluates the polynom at a specified value
     * @param x : value to calculate
     * @return evaluation  
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
        StringBuilder sb = new StringBuilder(); 
        
        for(int i =0 ; i<coeffs.length;i++) {
            if(coeffs[i]==0) 
                continue; 
            if(coeffs[i]>0) {
                sb.append("+");
               
            }
           
            if(coeffs[i]==-1.0)
                sb.append("-");
            
            if(coeffs[i]!=-1.0&&coeffs[i]!=1.0)
                sb.append(coeffs[i]);
            
             sb.append("x^");
             
            if(i==0&&coeffs[i]>0)
                sb.deleteCharAt(0);
            if(i==coeffs.length-1)
            {
                sb.replace(sb.lastIndexOf("x^"), sb.length(), "");
                break; 
            }
            if(i==coeffs.length-2)
            {
                sb.replace(sb.lastIndexOf("^"), sb.length(), "");
                continue; 
            }
            
            sb.append(coeffs.length-i-1);
        }
        return sb.toString() ;   
    }
    
    @Override
    public  int hashCode() {
        throw new UnsupportedOperationException(); 
    }
    
    @Override
    public  boolean equals(Object obj) {
        throw new UnsupportedOperationException(); 
    }
}

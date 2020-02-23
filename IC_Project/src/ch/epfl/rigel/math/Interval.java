package ch.epfl.rigel.math;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */

public abstract class Interval {
    
    private final double  borneMin , borneMax ; 
    
    
    protected Interval(double borneMi, double borneMa) {
        this.borneMin=borneMi; 
        this.borneMax=borneMa; 
    }
    
    /**Getter for BorneMin
     * @return value of BorneMin
     */
   public double low() {
        return borneMin; 
    }
    /**Getter for BorneMax
     * @return value of BorneMax
     */
    public double high() {
        return borneMax; 
    }
    
    /**Size of the interval
     * @return size of the interval 
     */
    public double size() {
        return borneMax-borneMin ;
    }
    
    /**Verifies if v is in the interval
     * @param v : double to verify
     * @return true if v is in the interval 
     */
    public abstract boolean contains(double v ); 
    
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException(); 
    }
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException(); 
    }
}

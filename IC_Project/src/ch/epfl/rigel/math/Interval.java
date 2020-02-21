package ch.epfl.rigel.math;

/**
 * @author Mohamed Hichem Hadhri (300434)
 *
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
    double low() {
        return borneMin; 
    }
    /**Getter for BorneMax
     * @return value of BorneMax
     */
    double high() {
        return borneMax; 
    }
    
    /**Size of the interval
     * @return size of the interval 
     */
    double size() {
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

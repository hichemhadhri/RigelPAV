package ch.epfl.rigel.math;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */

public final class ClosedInterval extends Interval {

    
    private ClosedInterval(double x, double y ) {
        super(x,y); 
    }
    
    
    /**
     * Creates a CLosedInterval Object [low, high]
     * @param low : Minimum head
     * @param high : Maximum head
     * @throws :IllegalArgumentException if low=>high
     * @return : ClosedInterval
     */
    public static ClosedInterval of(double low, double high) {
    	Preconditions.checkArgument(high>low);
        return new ClosedInterval(low, high); 
    }
    
    /**
     * Creates a symmetric CLosedInterval Object [-size/2, size/2]
     * @param size : size of interval
     * @throws :IllegalArgumentException if size<=0
     * @return : symmetric ClosedInterval
     */
    public static ClosedInterval symmetric(double size) {
    	Preconditions.checkArgument(size>0);
        
        return new ClosedInterval(-size/2,size/2);
    }
    
    /**Clip v to the interval
     * @param v : value to clip
     * @return clipped value of v
     */
    public double clip(double v) {
        if(v<=super.low())
            return super.low(); 
        if(v>=super.high())
            return super.high(); 
        
        return v; 
    }
    
    @Override
    public boolean contains(double v) {
        return (v>=super.low() && v<=super.high());
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[ %f , %f]", super.low(),super.high()) ;
    }
}

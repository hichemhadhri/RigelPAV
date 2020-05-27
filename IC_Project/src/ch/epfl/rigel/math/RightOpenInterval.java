package ch.epfl.rigel.math;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */

public final class RightOpenInterval extends Interval{

    private RightOpenInterval(double x, double y ) {
        super(x,y); 
    }
    
    
    /**
     * Creates a RightOpenInterval Object [low, high[
     * @param low : Minimum head
     * @param high : Maximum head
     * @throws IllegalArgumentException if low>high
     * @return : RightOpenInterval
     */
    public static RightOpenInterval of(double low, double high) {
    	Preconditions.checkArgument(low<high);
        return new RightOpenInterval(low, high); 
    }
    
    /**
     * Creates a symmetric RightOpenInterval [-size/2,size/2[
     * @param size : size
     * @throws :IllegalArgumentException if size<=0
     * @return symmetric RightOpenInterval
     */
    public static RightOpenInterval symmetric(double size) {
    	Preconditions.checkArgument(size>0); 
        return new RightOpenInterval(-size/2,size/2);
    }
    
   private static double floorMod(double x, double y ) {
       return x-y*Math.floor(x/y);
   }
    
    /**
     * reduce a double in the interval
     * 
     * @param v : double to reduce
     * @return  reduced double in [low,high[
     */
    public double reduce(double v) {
        return super.low()+ floorMod(v-super.low(),super.size());
    }

    @Override
    public boolean contains(double v) {
        return (v>=super.low() && v<super.high());
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[ %f , %f[", super.low(),super.high()) ;
    }
}

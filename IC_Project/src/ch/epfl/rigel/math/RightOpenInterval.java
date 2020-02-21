package ch.epfl.rigel.math;

import java.util.Locale;

/**
 * RightOpenInterval class extends Interval
 * 
 * @author Mohamed Hichem Hadhri (300434)
 *
 */
public final class RightOpenInterval extends Interval{

    private RightOpenInterval(double x, double y ) {
        super(x,y); 
    }
    
    
    /**
     * Creates a RightOpenInterval Object [low, high[
     * @param low : Minimum head
     * @param high : Maximum head
     * @return : RightOpenInterval
     */
    public static RightOpenInterval of(double low, double high) {
        if(!(low<high))
            throw new IllegalArgumentException(); 
        return new RightOpenInterval(low, high); 
    }
    
    /**
     * Creates a symmetric RightOpenInterval [-size/2,size/2[
     * @param size : size
     * @throws :IllegalArgumentException if size<=0
     * @return symmetric RightOpenInterval
     */
    public static RightOpenInterval symmetric(double size) {
        if(size<=0)
            throw new IllegalArgumentException(); 
        
        return new RightOpenInterval(-size/2,size/2);
    }
    
   private double floorMod(double x, double y ) {
       return x-y*Math.floor(x/y);
   }
    
    /**
     * reduce a double in the interval
     * 
     * @param v : double to reduce
     * @return  reduced double in [low,high[
     */
    public double reduce(double v) {
        return super.low()+ floorMod(v-super.low(),super.high()-super.low());
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

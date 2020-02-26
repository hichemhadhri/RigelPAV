package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {
    private static final Interval raInterval = RightOpenInterval.of(0,Angle.TAU);
    private static final Interval decInterval = ClosedInterval.of(-Angle.TAU/4,Angle.TAU/4);
    private EquatorialCoordinates (double ra , double dec){
        super(ra,dec);
    }
    
    /**
     * @param ra : right ascention value
     * @param dec : declination value
     * @return EclipticCoordinates Object
     * @throws IllegalArgumentException if ra or dec are false
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        if(!raInterval.contains(ra) || !decInterval.contains(dec))
            throw new IllegalArgumentException();
        return new EquatorialCoordinates(ra,dec);
    }
    
   
    
    
    /**Getter for right ascention in radian 
     * @return right ascention in radian
     */
    public double ra() {
        return super.lon();
    }
    /**Getter for declination in radian 
     * @return declination in radian 
     */
    public double dec() {
        return super.lat();
    }
    /**Getter for right ascention in degrees 
     * @return right ascention in degrees
     */
    public double raDeg() {
        return super.lonDeg();
    }
    /**Getter for right ascention in Hours 
     * @return right ascention in hours
     */
    public double raHr() {
        return Angle.toHr(super.lon());
    }
    
    /**Getter for declination in degrees
     * @return declination in degrees
     */
    public double decDeg() {
        return super.latDeg();
    }
    
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)",raHr() , decDeg()) ; 
    }
}

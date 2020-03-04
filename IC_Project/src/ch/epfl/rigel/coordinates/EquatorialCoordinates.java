package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * EquatorialCoordinates: 
 * right ascension , declination
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
     * @param ra : right ascension value
     * @param dec : declination value
     * @return EclipticCoordinates Object
     * @throws IllegalArgumentException if ra is not in [0, 2pi[  or dec is not in [-pi/2 , pi/2]
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        Preconditions.checkInInterval(raInterval, ra);
        Preconditions.checkInInterval(decInterval, dec);
        return new EquatorialCoordinates(ra,dec);
    }
    
   
    
    
    /**Getter for right ascension in radian 
     * @return right ascension in radian
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
    /**Getter for right ascension in degrees 
     * @return right ascension in degrees
     */
    public double raDeg() {
        return super.lonDeg();
    }
    /**Getter for right ascension in Hours 
     * @return right ascension in hours
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

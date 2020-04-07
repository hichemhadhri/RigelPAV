package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;
/**
 * Ecliptic coordinates:
 * longtitude , latitude
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public final  class EclipticCoordinates extends SphericalCoordinates {
    private static final Interval lonInterval = RightOpenInterval.of(0,Angle.TAU);
    private static final Interval latInterval = ClosedInterval.of(-Angle.TAU/4,Angle.TAU/4);

    private EclipticCoordinates (double lon , double lat){
        super(lon,lat);
    }
    
    /**Creates a new EclipticCoordinates object from given lon and lat
     * @param lon : longitude value
     * @param lat : latitude value
     * @return EclipticCoordinates Object
     * @throws IllegalArgumentException if lon is not in [0, 2pi[  or lat is not in [-pi/2 , pi/2]
     */
    public static EclipticCoordinates of(double lon, double lat) {
        Preconditions.checkInInterval(lonInterval, lon);
        Preconditions.checkInInterval(latInterval, lat);

        return new EclipticCoordinates(lon,lat);
    }
 
    
    
    @Override
    public double lon() {
        return super.lon();
    }
    @Override
    public double lat() {
        return super.lat();
    }
    @Override
    public double lonDeg() {
        return super.lonDeg();
    } 
    @Override
    public double latDeg() {
        return super.latDeg();
    }
    
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)",lonDeg() , latDeg()) ; 
    }
}

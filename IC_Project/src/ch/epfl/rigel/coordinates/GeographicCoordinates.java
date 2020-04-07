package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;


/**
 * GeographicCoordinates: 
 * longitude , latitude
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public final class GeographicCoordinates extends SphericalCoordinates {
    private static final Interval lonInterval = RightOpenInterval.of(-180,180);
    private static final Interval latInterval = ClosedInterval.of(-90,90);
	private GeographicCoordinates (double longtitude , double latitude){
		super(longtitude,latitude);
	}
	
	/**Creates a GeographicCoordinates object from the given lonDeg and latDeg
	 * @param lonDeg : longitude in degrees
	 * @param latDeg : latitude in degrees
	 * @throws IllegalArgumentException if lon is not in [-180, 180[  or lat is not in [-90 , 90]
	 * @return new GeographicCorrdinates object
	 */
	public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
	    Preconditions.checkInInterval(lonInterval, lonDeg);
        Preconditions.checkInInterval(latInterval, latDeg);
		return new GeographicCoordinates(Angle.ofDeg(lonDeg),Angle.ofDeg(latDeg));
	}
	
	
	/**Verifies validity of longitude in degrees value
	 * @param lonDeg : longitude in degrees
	 * @return true if valid
	 */
	public static boolean isValidLonDeg(double lonDeg) {
		return lonInterval.contains(lonDeg);
	}
	/**Verifies validity of latitude in degrees value
     * @param lonDeg : latitude in degrees
     * @return true if valid
     */
	public static boolean isValidLatDeg(double latDeg) {
		return latInterval.contains(latDeg);
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
		return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)",lonDeg() , latDeg()) ; 
	}
}

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
public final class GeographicCoordinates extends SphericalCoordinates {
    private static final Interval lonInterval = RightOpenInterval.of(-180,180);
    private static final Interval latInterval = ClosedInterval.of(-90,90);
	private GeographicCoordinates (double longtitude , double latitude){
		super(longtitude,latitude);
	}
	
	/**Creates a GeographicCoordinates object from the given lonDeg and latDeg
	 * @param lonDeg : longitude in degrees
	 * @param latDeg : latitude in degrees
	 * @throws IllegalArgumentException if lonDeg or latDeg is not valid
	 * @return new GeographicCorrdinates object
	 */
	public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
		if(!isValidLatDeg(latDeg)||!isValidLonDeg(lonDeg))
			throw new IllegalArgumentException();
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
	
	
	public double lon() {
		return super.lon();
	}
	public double lat() {
		return super.lat();
	}
	public double lonDeg() {
		return super.lonDeg();
	}
	public double latDeg() {
		return super.latDeg();
	}
	
	
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)",lonDeg() , latDeg()) ; 
	}
}

package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;
/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public final class GeographicCoordinates extends SphericalCoordinates {
	
	
	//angles in radians
	private GeographicCoordinates (double longtitude , double latitude){
		super(longtitude,latitude);
	}
	
	public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
		if(!isValidLatDeg(latDeg)||!isValidLonDeg(lonDeg))
			throw new IllegalArgumentException();
		return new GeographicCoordinates(Angle.ofDeg(lonDeg),Angle.ofDeg(latDeg));
	}
	
	public static boolean isValidLonDeg(double lonDeg) {
		return lonDeg<=180 && lonDeg>=-180;
	}
	public static boolean isValidLatDeg(double latDeg) {
		return latDeg<=90 && latDeg>=-90;
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

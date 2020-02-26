package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */

abstract class SphericalCoordinates {
	private final double latitude;
	private final double longtitude;
	
	SphericalCoordinates(double longtitude, double latitude){
		this.latitude=latitude;
		this.longtitude=longtitude;
		
	}
	
	/**getter for longitude in radian 
	 * @return longitude in radian 
	 */
	double lon() {
		return longtitude;
	}
	
	/**getter for latitude in radian 
	 * @return latitude in radian 
	 */
	double lat() {
		return latitude;
	}
	
	/**Getter for longitude in degrees
	 * @return longitude in degrees
	 */
	double lonDeg() {
		return Angle.toDeg(longtitude);
	}
	/**Getter for latitude in degrees 
	 * @return latitude in degrees
	 */
	double latDeg() {
		return Angle.toDeg(latitude);
	}
	
	
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException(); 
    }
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException(); 
    }

}

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
	
	double lon() {
		return longtitude;
	}
	
	double lat() {
		return latitude;
	}
	
	double lonDeg() {
		return Angle.toDeg(longtitude);
	}
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

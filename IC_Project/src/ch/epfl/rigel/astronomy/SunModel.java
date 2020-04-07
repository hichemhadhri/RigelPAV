package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;


/**SunModel enum 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public enum SunModel implements CelestialObjectModel<Sun> {
	SUN;
	
	private final static double EPSILON_G_RAD = Angle.ofDeg(279.557208);
	private final static double OMEGA_G_RAD = Angle.ofDeg(283.112438);
	private final static double THETA0_RAD = Angle.ofDeg(0.533128);
	private final static double TROPICAL_YEAR = 365.242191;
	private final static double EX = 0.016705;

	@Override
	public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
		double meanAnomaly = getMeanAnomaly(daysSinceJ2010);
		double trueAnomaly = getTrueAnomaly(meanAnomaly);
		
		double longitudeRad = getEclipticLongitude(trueAnomaly);
		EclipticCoordinates ecliptCoord = EclipticCoordinates.of(Angle.normalizePositive(longitudeRad), 0);
		EquatorialCoordinates equaCoord = eclipticToEquatorialConversion.apply(ecliptCoord);
		double angularSize = getAngularSize(trueAnomaly);
		
		return new Sun(ecliptCoord,equaCoord,(float)angularSize, (float)meanAnomaly);
	}
	private double getMeanAnomaly(double D) {
	  
		return (Angle.TAU / TROPICAL_YEAR) * D + EPSILON_G_RAD - OMEGA_G_RAD;
	}
	private double getTrueAnomaly(double M0) {
		return M0 + 2 * EX * Math.sin(M0);
	}
	private double getEclipticLongitude(double v0) {
		return v0 + OMEGA_G_RAD; 
	}
	
	private double getAngularSize(double v0) {
		return THETA0_RAD * ( ( 1 + EX * Math.cos(v0) ) / ( 1 - EX * EX ) );
	}

}

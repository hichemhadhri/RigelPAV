package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;


/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 * @param <O>
 */
public interface CelestialObjectModel<O> {
	
	
	/**
	 * @param daysSinceJ2010
	 * @param eclipticToEquatorialConversion
	 * @return
	 */
	O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
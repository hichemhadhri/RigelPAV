package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;


/**CelestialObjectModel interface
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 * @param <O>
 */
public interface CelestialObjectModel<O> {
	
	
	/** Get a celestial object with corresponding position at a specific day
	 * @param daysSinceJ2010
	 * @param eclipticToEquatorialConversion
	 * @return
	 */
	O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}

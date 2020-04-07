package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;


/**Planet Class
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public final class Planet extends CelestialObject {

	/** Planet constructor
	 * @param name
	 * @param equatorialPos
	 * @param angularSize
	 * @param magnitude
	 */
	public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
		super(name, equatorialPos, angularSize, magnitude);
	}

}

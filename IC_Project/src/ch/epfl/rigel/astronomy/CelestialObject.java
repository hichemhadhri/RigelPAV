package ch.epfl.rigel.astronomy;

import java.util.Objects;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * CelestialObject class
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public abstract  class CelestialObject {
	
	private final String name;
	private final EquatorialCoordinates equatorialPos;
	private final float angularSize;
	private final float magnitude;
	
	
	
	/** CelestialObject constructor
	 * @param name: the name of the celestial object
	 * @param equatorialPos: the equatorial coordinates
	 * @param angularSize: the angular size of the object
	 * @param magnitude: the magnitude of the object
	 * @throw IllegalArgumentException: if angularSize <0
	 * @throw NullPointerException: if equatorialPos or name is null
	 */
	 CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){
		Preconditions.checkArgument(angularSize>=0);
		this.name = Objects.requireNonNull(name, "Name cannot be null");
		Objects.requireNonNull(equatorialPos,"Equatorial position cannot be null");
		this.equatorialPos = equatorialPos;
		this.angularSize = angularSize;
		this.magnitude = magnitude;
	}

	/** name getter
	 * @return name 
	 */
	public String name() {
		return name;
	}

	/**angularSize getter
	 * @return angularSize
	 */
	public double angularSize() {
		return angularSize;
	}

	/**magnitude Getter
	 * @return magnitude
	 */
	public double magnitude() {
		return magnitude;
	}

	/**equatorialPos getter 
	 * @return equatorialPos
	 */
	public EquatorialCoordinates equatorialPos() {
		return equatorialPos;
	}
	
	/**Returns information about the celestialObject 
	 * @return info 
	 */
	public String info() {
		return name();	
	}
	
	@Override
	public String toString() {
		return info();
	}
	
}

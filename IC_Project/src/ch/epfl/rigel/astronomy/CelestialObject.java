package ch.epfl.rigel.astronomy;

import java.util.Objects;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 * CelestialObject class
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
		if(angularSize<0)
			throw new IllegalArgumentException();
		this.name = Objects.requireNonNull(name, "Name cannot be null");
		Objects.requireNonNull(equatorialPos,"Equatorial position cannot be null");
		this.equatorialPos = EquatorialCoordinates.of(equatorialPos.ra(), equatorialPos.dec());
		this.angularSize = angularSize;
		this.magnitude = magnitude;
	}
	
	/**Getter for name
	 * @return name
	 */
	public String name() {
		return name;
	}
	/**Getter for angularSize
	 * @return angularSize
	 */
	public double angularSize() {
		return angularSize;
	}
	/**Getter for magnitude
	 * @return
	 */
	public double magnitude() {
		return magnitude;
	}
	/**Getter for equatorialPos
	 * @return
	 */
	public EquatorialCoordinates equatorialPos() {
		return equatorialPos;
	}
	
	/**Returns information of the celestialObject 
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

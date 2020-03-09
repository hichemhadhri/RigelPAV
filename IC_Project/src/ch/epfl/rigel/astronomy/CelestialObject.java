package ch.epfl.rigel.astronomy;

import java.util.Objects;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 * CelestialObject class
 */
public abstract class CelestialObject {
	
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
		this.equatorialPos = Objects.requireNonNull(equatorialPos,"Equatorial position cannot be null");
		this.angularSize = angularSize;
		this.magnitude = magnitude;
	}
	
	public String name() {
		return name;
	}
	public double angularSize() {
		return angularSize;
	}
	public double magnitude() {
		return magnitude;
	}
	public EquatorialCoordinates equatorialPos() {
		return equatorialPos;
	}
	
	public String info() {
		return name();	
	}
	
	@Override
	public String toString() {
		return info();
	}
	
}

package ch.epfl.rigel.astronomy;

import java.util.Objects;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public final class Sun extends CelestialObject {
	private final static float defaultMagnitude = -26.7f;
	private final static String defaultName = "Soleil";
	private final EclipticCoordinates eclipticPos;
	private final float meanAnomaly;
	
	
	
	/** Sun constructor
	 * @param eclipticPos
	 * @param equatorialPos
	 * @param angularSize
	 * @param meanAnomaly
	 */
	public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
		super(defaultName, equatorialPos, angularSize, defaultMagnitude);
		Objects.requireNonNull(eclipticPos);
		this.eclipticPos = EclipticCoordinates.of(eclipticPos.lon(),eclipticPos.lat());
		this.meanAnomaly = meanAnomaly;
	}
	
	public EclipticCoordinates eclipticPos() {
	    
	   return  EclipticCoordinates.of(eclipticPos.lon(),eclipticPos.lat());
	}
	
	public double meanAnomaly() {
		return meanAnomaly;
	}

}

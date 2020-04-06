package ch.epfl.rigel.astronomy;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public final class Moon extends CelestialObject{
	private final static String defaultName = "Lune";
	private final float phase;
	
	/**Moon Constructor
	 * @param equatorialPos
	 * @param angularSize
	 * @param magnitude
	 * @param phase: Moon phase
	 */
	public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude,float phase) {
		super(defaultName, equatorialPos, angularSize, magnitude);
		this.phase=(float) Preconditions.checkInInterval(ClosedInterval.of(0, 1), phase);
	}
	
	@Override
	public String info() {
		return String.format(Locale.ROOT,"%s (%.1f%%)",super.info(), phase*100); 
	}
	
	
	

}

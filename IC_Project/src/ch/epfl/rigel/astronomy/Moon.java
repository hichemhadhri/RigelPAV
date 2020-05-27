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
	private final static String DEFAULTNAME = "Lune";
	private final static ClosedInterval MOONPHASEINTERVAL = ClosedInterval.of(0, 1);
	private final float PHASE;
	
	/**Moon Constructor
	 * @param equatorialPos
	 * @param angularSize
	 * @param magnitude
	 * @param phase: Moon phase
	 */
	public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
		super(DEFAULTNAME, equatorialPos, angularSize, magnitude);
		this.PHASE=(float) Preconditions.checkInInterval(MOONPHASEINTERVAL, phase);
	}
	
	@Override
	public String info() {
		return String.format(Locale.ROOT,"%s (%.1f%%)",super.info(), PHASE*100); 
	}
	
	
	

}

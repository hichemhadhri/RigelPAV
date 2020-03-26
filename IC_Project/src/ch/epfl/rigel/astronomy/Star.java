package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public final class Star extends CelestialObject{
	private final int hipparcosId;
	private final int colorTemperature;
	public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex){
		super(name,equatorialPos,0,magnitude);
		Preconditions.checkArgument(hipparcosId>=0);
		Preconditions.checkInInterval(ClosedInterval.of(-0.5, 5.5), colorIndex);
		this.hipparcosId=hipparcosId;
		this.colorTemperature= (int)(4600 * (1/(0.92f*colorIndex+1.7f) + 1/(0.92f*colorIndex+0.62f)));//Calculating the color temperature once and for all
	}

	public int hipparcosId() {
		return hipparcosId;
	}
	public int  colorTemperature() { 
		return colorTemperature;
	}
}

package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**Star class 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public final class Star extends CelestialObject{
	private final int hipparcosId;
	private final int colorTemperature;
	
	
	/** Star constructor
	 * @param hipparcosId
	 * @param name
	 * @param equatorialPos
	 * @param magnitude
	 * @param colorIndex
	 */
	public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex){
		super(name,equatorialPos,0,magnitude);
		Preconditions.checkArgument(hipparcosId>=0);
		Preconditions.checkInInterval(ClosedInterval.of(-0.5, 5.5), colorIndex);
		this.hipparcosId=hipparcosId;
		this.colorTemperature= (int)calculateColorTemperature(colorIndex);//Calculating the color temperature once and for all
	}
	
	private double calculateColorTemperature(float colorIndex) {
		return(4600 * (1 / ( 0.92f * colorIndex + 1.7f ) + 1 / ( 0.92f * colorIndex + 0.62f ) ) );
	}

	/**HipparCosId getter
	 * @return hipparcosId
	 */
	public int hipparcosId() {
		return hipparcosId;
	}
	
	/**colorTemperature getter
	 * @return colorTemperature in Kelvin degrees
	 */
	public int  colorTemperature() { 
		return colorTemperature;
	}
}

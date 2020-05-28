package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * HorizontalCoordinates: 
 * right azimuth , altitude
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public class HorizontalCoordinates extends SphericalCoordinates {
	private static final RightOpenInterval AZ_INTERVAL_DEG = RightOpenInterval.of(0,360);
    private static final RightOpenInterval AZ_INTERVAL = RightOpenInterval.of(0,Angle.TAU);
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.of(-Angle.TAU/4,Angle.TAU/4);
	private HorizontalCoordinates(double longtitude, double latitude) {
		super(longtitude, latitude);
	}

	/**Creates a HorizontalCoordinates Object from the given azimuth and altitude
	 * @param az : azimuth in radian
	 * @param alt : altitude in radian
	 * @throws IllegalArgumentException if az or alt are not valid
	 * @return a HorziontalCoordinates Object
	 */
	public static HorizontalCoordinates of(double az, double alt) {
	    Preconditions.checkInInterval(AZ_INTERVAL, az);
        Preconditions.checkInInterval(ALT_INTERVAL, alt);
		return new HorizontalCoordinates(az,alt);
	}
	
	/**Creates a HorizontalCoordinates Object from the given azimuth and altitude in degrees
     * @param az : azimuth in degrees
     * @param alt : altitude in degrees
     * @throws IllegalArgumentException if az is not in [0,TAU[ or alt is not in [-pi/2 , pi/2]
     * @return a HorziontalCoordinates Object
     */
	public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
	    Preconditions.checkInInterval(AZ_INTERVAL, Angle.ofDeg(azDeg));
        Preconditions.checkInInterval(ALT_INTERVAL, Angle.ofDeg(altDeg));
		return new HorizontalCoordinates(Angle.ofDeg(azDeg),Angle.ofDeg(altDeg));
	}
	
	/**Getter for azimuth in radian
	 * @return azimuth
	 */
	public double az() {
		return lon();
	}
	/**Getter for azimuth in degrees
	 * @return azimuth in degrees
	 */
	public double azDeg() {
		return lonDeg();
	}
	/**Getter for altitude in radian 
	 * @return altitude
	 */
	public double alt() {
	    return lat();
	}
	/**Getter for altitude in degrees
	 * @return altitude in degrees
	 */
	public double altDeg() {
	    return latDeg();
	}
	
	/**Returns a String for the Octant where the receiver's Azimuth is
	 * @param n : north
	 * @param e : east
	 * @param s : south
	 * @param w : west
	 * @return Octant string
	 */
	public String azOctantName(String n, String e, String s, String w) {
		String octants[] = {n,n+e,e,s+e,s,s+w,w,n+w};
		for (int i = 0 ; i<8 ; ++i) {
			if (RightOpenInterval.of(45*i, 45*(i+1)).contains(AZ_INTERVAL_DEG.reduce(azDeg()+22.5))){
				return octants[i];
			}
		}
		return "";
	}
	/**Calculates the angularDistance between the current Object and that(parameter)
	 * @param that : second coordinates
	 * @return angularDistance
	 */
	public double angularDistanceTo(HorizontalCoordinates that) {
		return Math.acos( Math.sin(this.lat()) * Math.sin(that.lat())  +  Math.cos(this.lat()) * Math.cos(that.lat()) * Math.cos(this.lon()-that.lon())  );
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg() , altDeg() ) ;
	}

}

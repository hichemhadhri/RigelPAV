package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public class HorizontalCoordinates extends SphericalCoordinates {
    private static final Interval azInterval = RightOpenInterval.of(0,Angle.TAU);
    private static final Interval altInterval = ClosedInterval.of(-Angle.TAU/4,Angle.TAU/4);
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
	    Preconditions.checkInInterval(azInterval, az);
        Preconditions.checkInInterval(altInterval, alt);
		return new HorizontalCoordinates(az,alt);
	}
	
	/**Creates a HorizontalCoordinates Object from the given azimuth and altitude in degrees
     * @param az : azimuth in degrees
     * @param alt : altitude in degrees
     * @throws IllegalArgumentException if az or alt are not valid
     * @return a HorziontalCoordinates Object
     */
	public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
	    Preconditions.checkInInterval(azInterval, Angle.ofDeg(azDeg));
        Preconditions.checkInInterval(altInterval, Angle.ofDeg(altDeg));
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
		String out="";
		if(azDeg()>292.5 || azDeg()<76.5)
			out+=n;
		else if(azDeg()>112.5 && azDeg()<247.5)
			out+=s;
		
		if(azDeg()>22.5 && azDeg()<157.5)
			out+=e;
		else if(azDeg()>202.5 && azDeg()<=337.5)
			out+=w;
		return out;
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

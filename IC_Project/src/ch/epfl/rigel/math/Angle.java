package ch.epfl.rigel.math;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */

public final class Angle {

    private Angle(){}

    public static final double TAU = 2 * Math.PI;
    private static final double HOURS_PER_RAD = 24.0 / TAU;
    private static final double RAD_PER_SEC = TAU/(3600.0 * 360.0);


    /**Returns the normalized angle value
     * @param double : the angle to normalize
     * @return double : the angle value in the [0,τ[ interval
     */
    public static double normalizePositive(double rad){
        return RightOpenInterval.of(0, TAU).reduce(rad);
    }

    /**Returns the angle value in radians corresponding to the given angle in seconds
     * @param double : the angle in seconds
     * @return double : the angle in radians
     */
    public static double ofArcsec(double sec){
        return sec * RAD_PER_SEC;
    }

    /**Returns the angle value in radians corresponding to the given angle in degrees, minutes and seconds
     * @param int : degrees arc
     * @param int : minutes arc
     * @param int : seconds arc
     * @return double : the angle in radians
     * @throws IllegalArgumentException : if the min or sec value is not in the [0,60[ range
     */
    public static double ofDMS(int deg, int min, double sec){
    	if((min<0|| min>=60)|| (sec<0|| sec>=60))
    		throw new IllegalArgumentException();
        return Math.toRadians(deg+ min/60.0 + sec/3600.0);
    }

    /**Returns the angle value in radians corresponding to the given angle in degrees
     * @param double : the angle in degrees
     * @return double : the angle in radians
     */
    public static double ofDeg(double deg){
        return Math.toRadians(deg);
    }

    /**Returns the angle value in degrees corresponding to the given angle in radians
     * @param double : the angle in radians
     * @return double : the angle in degrees
     */
    public static double  toDeg(double rad){
        return Math.toDegrees(rad);
    }

    /**Returns the angle value in radians corresponding to the given angle in hours
     * @param double : the angle in hours
     * @return double : the angle in radians
     */
    public static double ofHr(double hr){
        return hr / HOURS_PER_RAD;
    }

    /**Returns the angle value in hours corresponding to the given angle in radians
     * @param double : the angle in radians
     * @return double : the angle in hours
     */
    public static double  toHr(double rad){
        return rad * HOURS_PER_RAD;
    }


}
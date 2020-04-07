package ch.epfl.rigel.math;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */

public final class Angle {

    private Angle(){}

    /**
     * Constant equal to 2*PI
     */
    public static final double TAU = 2 * Math.PI;
    private static final double HOURS_PER_RAD = 24.0 / TAU;
    private static final double RAD_PER_SEC = TAU/(3600.0 * 360.0);


    /**Returns the normalized angle value
     * @param rad : the angle to normalize
     * @return the angle value in the [0,τ[ interval
     */
    public static double normalizePositive(double rad){
        return RightOpenInterval.of(0, TAU).reduce(rad);
    }

    /**Returns the angle value in radians corresponding to the given angle in seconds
     * @param sec : the angle in seconds
     * @return  the angle in radians
     */
    public static double ofArcsec(double sec){
        return sec * RAD_PER_SEC;
    }

    /**Returns the angle value in radians corresponding to the given angle in degrees, minutes and seconds
     * @param deg : degrees arc
     * @param min : minutes arc
     * @param sec : seconds arc
     * @return the angle in radians
     * @throws IllegalArgumentException : if the min or sec value is not in the [0,60[ range
     */
    public static double ofDMS(int deg, int min, double sec){
        RightOpenInterval lim = RightOpenInterval.of(0, 60);
        if(!lim.contains(min) || !lim.contains(sec))
    		throw new IllegalArgumentException();
        return Math.toRadians(deg+ min/60.0 + sec/3600.0);
    }

    /**Returns the angle value in radians corresponding to the given angle in degrees
     * @param deg : the angle in degrees
     * @return  the angle in radians
     */
    public static double ofDeg(double deg){
        return Math.toRadians(deg);
    }

    /**Returns the angle value in degrees corresponding to the given angle in radians
     * @param rad : the angle in radians
     * @return  the angle in degrees
     */
    public static double  toDeg(double rad){
        return Math.toDegrees(rad);
    }

    /**Returns the angle value in radians corresponding to the given angle in hours
     * @param hr : the angle in hours
     * @return  the angle in radians
     */
    public static double ofHr(double hr){
        return hr / HOURS_PER_RAD;
    }

    /**Returns the angle value in hours corresponding to the given angle in radians
     * @param rad : the angle in radians
     * @return the angle in hours
     */
    public static double  toHr(double rad){
        return rad * HOURS_PER_RAD;
    }


}
package ch.epfl.rigel.astronomy;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

/**
 * SiderealTime Class
 * 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public final class SiderealTime {
     private SiderealTime() {
         
     }
     
     private final  static  Polynomial s0p= Polynomial.of(0.000025862, 2400.051336,6.697374558);
     private final static Polynomial s1p= Polynomial.of(1.002737909,0);
     
     /**Returns the greenwich sidereal Time 
     * @param when : date to determine its greenwich sidereal Time
     * @return greenwich sidereal time 
     */
    public static double greenwich(ZonedDateTime when) {
        ZonedDateTime wg = when.withZoneSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS);
        double julDiff=Epoch.J2000.julianCenturiesUntil(wg);
        double hourDiff=-when.withZoneSameInstant(ZoneOffset.UTC).until(wg, ChronoUnit.MILLIS)/3.6e+6;
        double s0 = s0p.at(julDiff);
        double s1 = s1p.at(hourDiff);
        return Angle.normalizePositive(Angle.ofHr(s1+s0));
        
     }
     
     /**Returns the local Time 
     * @param when : date to determine its local  Time
     * @param where : location 
     * @return the local time at the specified location
     */
    public static  double local(ZonedDateTime when, GeographicCoordinates where) {
        return  Angle.normalizePositive(SiderealTime.greenwich(when)+ where.lon());
     }
}

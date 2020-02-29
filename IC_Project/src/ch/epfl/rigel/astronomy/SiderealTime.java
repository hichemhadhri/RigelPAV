package ch.epfl.rigel.astronomy;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

public final class SiderealTime {
     private SiderealTime() {
         
     }
     
     
     public static double greenwich(ZonedDateTime when) {
        ZonedDateTime wg = when.withZoneSameInstant(ZoneOffset.UTC).withHour(0);
        double julDiff=Epoch.J2000.julianCenturiesUntil(wg);
        System.out.println(julDiff);
        double hourDiff=-when.withZoneSameInstant(ZoneOffset.UTC).until(wg, ChronoUnit.MILLIS)/3.6e+6;
        Polynomial s0p= Polynomial.of(0.000025862, 2400.051336,6.697374558);
        double s0 = s0p.at(julDiff);
        Polynomial s1p= Polynomial.of(1.002737909,0);
        double s1 = s1p.at(hourDiff);
        return Angle.normalizePositive(Angle.ofHr(s1+s0));
        
     }
     
     public static  double local(ZonedDateTime when, GeographicCoordinates where) {
        return  Angle.normalizePositive(SiderealTime.greenwich(when)+ where.lon());
     }
}

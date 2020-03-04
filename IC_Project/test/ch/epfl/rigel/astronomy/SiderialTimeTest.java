package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;

class SiderialTimeTest {

    @Test
    void greenwichtest() {
        assertEquals(2.9257399567031235 ,SiderealTime.greenwich(ZonedDateTime.of(2004,9,23, 11,0,0,0,ZoneId.of("UTC"))),1e-6);
        assertEquals(1.9883078130455532  ,SiderealTime.greenwich(ZonedDateTime.of(2001,9,11,8,14,0,0, ZoneId.of("UTC"))),1e-6);
       assertEquals(5.355270290366605   ,SiderealTime.greenwich(ZonedDateTime.of(2001,1,27, 12, 0 , 0,0, ZoneId.of("UTC"))),1e-6);
        System.out.println(Angle.toHr(SiderealTime.greenwich(ZonedDateTime.of(1980,4,22,14,36,51,670000000, ZoneId.of("UTC")))));
       System.out.println(Angle.toHr(SiderealTime.local(ZonedDateTime.of(1980,4,22,14,36,51,670000000,ZoneOffset.UTC),GeographicCoordinates.ofDeg(-64,45))));

    }
    @Test
    void greenwichTest2() {
        ZonedDateTime zdt =ZonedDateTime.of(LocalDate.of(1980, Month.APRIL,22),
                                            LocalTime.of(14,36,51,(int)(67*1E7)),
                                            ZoneOffset.UTC);
        assertEquals(Angle.ofHr(4.668119327),SiderealTime.greenwich(zdt),1E-10);
        }
    @Test
    void localTest() {
       assertEquals(   0.401453   ,Angle.toHr(SiderealTime.local(ZonedDateTime.of(1980,4,22,14,36,51,670000000,ZoneOffset.UTC),GeographicCoordinates.ofDeg(-64,0))),1e-6);
        
        }
    void daysUntil() {
    ZonedDateTime a = ZonedDateTime.of(
            LocalDate.of(2003, Month.JULY, 30),
            LocalTime.of(15, 0),
            ZoneOffset.UTC);
    ZonedDateTime b = ZonedDateTime.of(
            LocalDate.of(2020, Month.MARCH, 20),
            LocalTime.of(0, 0),
            ZoneOffset.UTC);
    ZonedDateTime c = ZonedDateTime.of(
            LocalDate.of(2006, Month.JUNE, 16),
            LocalTime.of(18, 13),
            ZoneOffset.UTC);
    ZonedDateTime d = ZonedDateTime.of(
            LocalDate.of(2000, Month.JANUARY, 3),
            LocalTime.of(18, 0),
            ZoneOffset.UTC);
    ZonedDateTime e = ZonedDateTime.of(
            LocalDate.of(1999, Month.DECEMBER, 6),
            LocalTime.of(23, 3),
            ZoneOffset.UTC);

    assertEquals(1306.125, Epoch.J2000.daysUntil(a));
    assertEquals(7383.5, Epoch.J2000.daysUntil(b));
    assertEquals(2358.259028, Epoch.J2000.daysUntil(c), 1e-6);
    assertEquals(2.25, Epoch.J2000.daysUntil(d));
    assertEquals(-25.539583, Epoch.J2000.daysUntil(e), 1e-6);

    assertEquals(-2345.375, Epoch.J2010.daysUntil(a), 1e-6);
    assertEquals(3732, Epoch.J2010.daysUntil(b));
    assertEquals(-1293.240972, Epoch.J2010.daysUntil(c), 1e-6);
    assertEquals(-3649.25, Epoch.J2010.daysUntil(d), 1e-6);
    assertEquals(-3677.039583, Epoch.J2010.daysUntil(e), 1e-6);

    }

    
    void julianCenturiesUntil() {
        ZonedDateTime a = ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1).minusDays(36525) ,
                LocalTime.of(12, 0), ZoneOffset.UTC);


        assertEquals(-1, Epoch.J2000.julianCenturiesUntil(a));
    }
}

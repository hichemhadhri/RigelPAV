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
       assertEquals(1.2220619247737088    ,SiderealTime.greenwich(ZonedDateTime.of(1980,4,22,14,36,51,67, ZoneId.of("UTC"))),1e-6);
        
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
        ZonedDateTime zdt =ZonedDateTime.of(LocalDate.of(2020, Month.FEBRUARY,27),
                                            LocalTime.of(00,00,00),
                                            ZoneOffset.UTC);
        GeographicCoordinates where=GeographicCoordinates.ofDeg(-76.0000000000,0);
        assertEquals(5.3535814050,Angle.toHr(SiderealTime.local(zdt,where)),1E-6);
        
        assertEquals(1.74570958832716     ,SiderealTime.local(ZonedDateTime.of(1980,4,22,14,36,51,27,ZoneOffset.UTC),
                GeographicCoordinates.ofDeg(30,45)),1e-6);
        
        }
    
    



}

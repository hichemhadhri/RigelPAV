package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

class SiderialTimeTest {

    @Test
    void greenwichtest() {
        assertEquals(2.9257399567031235 ,SiderealTime.greenwich(ZonedDateTime.of(2004,9,23, 11,0,0,0,ZoneId.of("UTC"))),1e-6);
     //   assertEquals(1.9883078130455532  ,SiderealTime.greenwich(ZonedDateTime.of(2001,9,11,8,14,0,0, ZoneId.of("UTC"))),1e-6);
       assertEquals(5.355270290366605   ,SiderealTime.greenwich(ZonedDateTime.of(2001,1,27, 12, 0 , 0,0, ZoneId.of("UTC"))),1e-6);
       assertEquals(1.2220619247737088    ,SiderealTime.greenwich(ZonedDateTime.of(1980,4,22,14,36,51,67, ZoneId.of("UTC"))),1e-6);
        
    }

}
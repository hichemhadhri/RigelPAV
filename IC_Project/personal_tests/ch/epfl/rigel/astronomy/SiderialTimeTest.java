package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.gui.TimeAccelerator;

class SiderialTimeTest {

    @Test
    void test() {
       
        ZonedDateTime initialTime = ZonedDateTime.parse("2020-04-17T21:00:00+00:00");
        ZonedDateTime laterTime = TimeAccelerator.continuous(300).adjust(initialTime, (long) (2.34 * 1e9));
        assertEquals(ZonedDateTime.parse("2020-04-17T21:11:42+00:00"), laterTime); 

        
              ZonedDateTime initialTime2 = ZonedDateTime.parse("2020-04-20T21:00:00+00:00");
              ZonedDateTime laterTime2 = TimeAccelerator.discrete(Duration.parse("PT23H56M4S"), 10)
        .adjust(initialTime2, (long) (2.34 * 1e9));
              System.out.println(laterTime2);
        assertEquals(ZonedDateTime.parse("2020-05-13T19:29:32+00:00"), laterTime2);
    }
   

}

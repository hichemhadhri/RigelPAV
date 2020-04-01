package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

class MyMoonModelTest {
    ZonedDateTime zdt =  ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC);
    @Test
    void at() {
//        assertEquals(0 , MoonModel.MOON.at(Epoch.J2010.daysUntil(zdt), new EclipticToEquatorialConversion(zdt))
//                .equatorialPos().raDeg());
        
        assertEquals(0.009225908666849136 ,MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),
                ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of(
                LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),ZoneOffset.UTC))).
                angularSize());
        
        assertEquals("Lune (22.5%)" ,MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, 9, 1),LocalTime.of(0, 0),
        ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of( LocalDate.of(2003, 9, 1),
        LocalTime.of(0, 0),ZoneOffset.UTC))).
        info());
    }

}

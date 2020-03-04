package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import ch.epfl.rigel.math.Angle;

class EclipticToEquatorialConversionTest {

    @Test
    void applyTest() {
	    ZonedDateTime dt = ZonedDateTime.of(LocalDate.of(2009,Month.JULY,6),LocalTime.of(0,0),ZoneOffset.UTC);
	    EclipticToEquatorialConversion a = new EclipticToEquatorialConversion(dt);
	    EquatorialCoordinates equ =a.apply(EclipticCoordinates.of(Angle.ofDeg(139.686111),Angle.ofDeg(4.875278)));
	    assertEquals(19.535003,equ.decDeg(),1e-6);
	    assertEquals(9.581478,equ.raHr(),1e-6);
    }

}

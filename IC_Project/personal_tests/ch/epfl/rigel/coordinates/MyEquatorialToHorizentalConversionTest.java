package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

class MyEquatorialToHorizentalConversionTest {

	@Test
	void applyTest() {
        ZonedDateTime dt = ZonedDateTime.of(2020, 3, 4, 22, 17, 0, 0, ZoneId.of("UTC"));
        EquatorialToHorizontalConversion a = new EquatorialToHorizontalConversion(dt, GeographicCoordinates.ofDeg(6.1, 52));
        //(az=326.4525°, alt=-8.2817°)
	}
    @Test
    void applyWorks1Terme() {

        GeographicCoordinates lena = GeographicCoordinates.ofDeg(52,0);

        EquatorialCoordinates a = EquatorialCoordinates.of(Angle.ofDeg(87.933333), Angle.ofDeg(23.219444) );
        EquatorialToHorizontalConversion zab = new EquatorialToHorizontalConversion(ZonedDateTime.of
                (2009,7,6, 0,0,0,0, ZoneId.of("UTC")), lena);
        HorizontalCoordinates errrr = zab.apply(a);
        assertEquals(283.271027, errrr.lonDeg(), 1e-6);
        //assertEquals(19.334345, errrr.latDeg());

    }

    @Test
    void applyWorks2Terme() {

        GeographicCoordinates lena = GeographicCoordinates.ofDeg(52, 0);

        EquatorialCoordinates a = EquatorialCoordinates.of(Angle.ofDeg(87.933333), Angle.ofDeg(23.219444));
        EquatorialToHorizontalConversion zab = new EquatorialToHorizontalConversion(ZonedDateTime.of
                (2009, 7, 6, 0, 0, 0, 0, ZoneId.of("UTC")), lena);
        HorizontalCoordinates errrr = zab.apply(a);
        assertEquals(19.334345, errrr.latDeg());
    }

}

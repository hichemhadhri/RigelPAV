package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

class EquatorialToHorizentalConversionTest {

	@Test
	void applyTest() {
        ZonedDateTime dt = ZonedDateTime.of(2020, 3, 4, 22, 17, 0, 0, ZoneId.of("UTC"));
        EquatorialToHorizontalConversion a = new EquatorialToHorizontalConversion(dt, GeographicCoordinates.ofDeg(6.1, 52));
        //(az=326.4525°, alt=-8.2817°)
	}

}

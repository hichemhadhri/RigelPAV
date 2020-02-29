package ch.epfl.rigel.coordinates;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ZonedDateTime dt = ZonedDateTime.of(2009, 7, 5, 1, 40, 0, 0, ZoneId.of("UTC"));
		EclipticToEquatorialConversion a = new EclipticToEquatorialConversion(ZonedDateTime.now());
	}

}

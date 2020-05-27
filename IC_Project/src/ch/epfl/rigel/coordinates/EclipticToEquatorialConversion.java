package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

/**
 * Ecliptic to Equatorial coordinates converter
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {
	
	
	
	
	private final double cosEclipObliq;
	private final double sinEclipObliq;
	private final double eclipObliq;
	private final static Polynomial POLYNOM = Polynomial.of(Angle.ofDMS(0, 0,0.00181),-Angle.ofDMS(0, 0,0.0006),-Angle.ofDMS(0, 0,46.815), Angle.ofDMS(23, 26, 21.45));
	
	/**
	 * EclipticToEquatorialConversion constructor
	 * @param ZonedDateTime: the zoned date time reference
	 */
	public EclipticToEquatorialConversion (ZonedDateTime when) {
		eclipObliq =POLYNOM.at(Epoch.J2000.julianCenturiesUntil(when));
		cosEclipObliq=Math.cos(eclipObliq);
		sinEclipObliq=Math.sin(eclipObliq);
	}

	/**
	 * Applies the conversion
	 * @param EclipticCoordinates: the EclipticCoordinates to convert
	 * @return EquatorialCoordinates: the equatorial coordinates
	 */
	@Override
	public EquatorialCoordinates apply(EclipticCoordinates ecl) {
		return EquatorialCoordinates.of(Angle.normalizePositive(getAlpha(ecl)),getDelta(ecl));
	}
	
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException(); 
    }
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException(); 
    }
    
    
    private double getAlpha(EclipticCoordinates ecl) {
    	return Math.atan2( Math.sin(ecl.lon()) * cosEclipObliq - Math.tan(ecl.lat()) * sinEclipObliq, 
    		   Math.cos(ecl.lon()));
    }
    
    private double getDelta(EclipticCoordinates ecl) {
    	
    	return Math.asin( Math.sin(ecl.lat()) * cosEclipObliq + Math.sin(ecl.lon()) * sinEclipObliq * Math.cos(ecl.lat()) );
    }




}

package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

/**
 * Equatorial to Horizontal coordinates converter
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

	private final double cosPhi;
	private final double sinPhi;
	private final double siderealTime;
	
	
	/**
	 * EquatorialToHorizontalConversion constructor
	 * @param ZonedDateTime: the zoned date time reference
	 * @param GeographicCoordinates: the geographical coordinates reference
	 */
	public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
		
		siderealTime = SiderealTime.local(when, where);
		cosPhi = Math.cos(where.lat());
		sinPhi = Math.sin(where.lat());

	}
	
	/**
	 * Applies the conversion
	 * @param EquatorialCoordinates: the EquatorialCoordinates to convert
	 * @return HorizontalCoordinates: the horizontal coordinates
	 */
	@Override
	public HorizontalCoordinates apply(EquatorialCoordinates equ) {
		double dec = equ.dec();
		double angularHour = getAngularHour(equ.ra());
		double h = getAlt(dec,angularHour);
		return HorizontalCoordinates.of(getAz(dec,h,angularHour), h);
	}
	
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException(); 
    }
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException(); 
    }
	
	private double getAz(double dec , double h, double H) {
		return Angle.normalizePositive(Math.atan2( -Math.cos(dec) * cosPhi * Math.sin(H),
			   Math.sin(dec) - sinPhi * Math.sin(h) ));
	}
	
	private double getAlt(double dec, double H) {
		return Math.asin( Math.sin(dec) * sinPhi + Math.cos(dec) * cosPhi * Math.cos(H) );
	}
	
	private double getAngularHour(double ra) {
		return siderealTime-ra;
	}

}

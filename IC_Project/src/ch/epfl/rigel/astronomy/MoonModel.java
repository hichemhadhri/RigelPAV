package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**MoonModel enum
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public enum MoonModel implements CelestialObjectModel<Moon> {
	MOON;
	private final static double L0_RAD = Angle.ofDeg(91.929336);
	private final static double P0_RAD = Angle.ofDeg(130.143076);
	private final static double N0_RAD = Angle.ofDeg(291.682547);
	private final static double I_RAD = Angle.ofDeg(5.145396);
	private final static double THETA0 = Angle.ofDeg(0.5181);
	private final static double E = 0.0549;
	

	@Override
	public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

		Sun sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
		double M0 = sun.meanAnomaly();
		double lambda0 = sun.eclipticPos().lon();
		
		
		double l = getOrbitalLongitude(daysSinceJ2010);
		
		double Mm = getMeanAnomaly(l,daysSinceJ2010);
		
		double Ev = getEv(l,Mm,lambda0);
		double Ae = getAe(M0);
		double A3 = getA3(M0);
		
		double Mmp = Mm + Ev - Ae - A3;
		
		double Ec = getEc(Mmp);
		double A4 = getA4(Mmp);
		
		double lp = l + Ev + Ec - Ae + A4;
		
		double V = getV(lp,lambda0);
		
		double ls = lp + V;
		
		
		double N = getN(daysSinceJ2010);
		double Np = getNp(N,M0);
		
		double lon = getMoonLongitude(ls,Np);
		double lat = getMoonLatitude(ls,Np);
		
		EquatorialCoordinates coord = eclipticToEquatorialConversion.apply(EclipticCoordinates.of(Angle.normalizePositive(lon), lat));
		
		
		float angularSize = getAngularSize(Mmp,Ec);
		double moonPhase = getMoonPhase(ls,lambda0);
		
		return new Moon(coord, angularSize, 0, (float)moonPhase);
	}
	
	
	private float getAngularSize(double Mmp, double Ec) {
		return (float)(THETA0
						/
					( ( 1 - E * E ) / ( 1 + E * Math.cos(Mmp+Ec) ) )
					);
	}
	
	private double getMoonPhase(double ls, double lambda0) {
		return  ( 1 - Math.cos(ls-lambda0) ) / 2 ;
	}
	
	
	private double getOrbitalLongitude(double D) {
		return Angle.ofDeg(13.1763966) * D + L0_RAD;
	}
	private double getMeanAnomaly(double l , double D) {
		return l - Angle.ofDeg(0.1114041) * D - P0_RAD;
	}
	private double getEv(double l, double Mm, double lambda0) {
		return Angle.ofDeg(1.2739) * Math.sin( 2 * ( l - lambda0 ) - Mm );
	}
	private double getAe(double M0) {
		return Angle.ofDeg(0.1858) * Math.sin(M0);
	}
	private double getA3(double M0) {
		return Angle.ofDeg(0.37) * Math.sin(M0);
	}
	private double getEc(double Mmp) {
		return Angle.ofDeg(6.2886) * Math.sin(Mmp);
	}
	private double getA4(double Mmp) {
		return Angle.ofDeg(0.214) * Math.sin(2*Mmp);
	}
	private double getV(double lp, double lambda0) {
		return Angle.ofDeg(0.6583) * Math.sin( 2 * ( lp - lambda0 ) );
	}
	
	private double getN(double D) {
		return N0_RAD - Angle.ofDeg(0.0529539) * D;
	}
	private double getNp(double N , double M0) {
		return N - Angle.ofDeg(0.16) * Math.sin(M0);
	}
	
	private double getMoonLongitude(double ls,double Np) {
		return Math.atan2( Math.sin(ls-Np) * Math.cos(I_RAD) , Math.cos(ls-Np) ) + Np;
	}
	
	private double getMoonLatitude(double ls, double Np) {
		return Math.asin( Math.sin(ls-Np) * Math.sin(I_RAD) );
	}

}

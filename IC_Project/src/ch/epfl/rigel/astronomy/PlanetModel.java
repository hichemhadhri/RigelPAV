package ch.epfl.rigel.astronomy;

import java.util.List;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**Planet Model enum
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public enum PlanetModel implements CelestialObjectModel<Planet> {
    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
          0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
          0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
         1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
           9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
           19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);
    
    public static final List<PlanetModel> ALL=List.of(PlanetModel.MERCURY,PlanetModel.VENUS,PlanetModel.EARTH,PlanetModel.MARS,PlanetModel.JUPITER,PlanetModel.SATURN,PlanetModel.URANUS,PlanetModel.NEPTUNE);
	private final static double EARTH_TROPICAL_YEAR = 365.242191;
	private final String name;
	private final double tropicalYear;
	private final double epsilon;
	private final double omega;
	private final double e;
	private final double a;
	private final double cosi;
	private final double sini;
	private final double bigOmega;
	private final double angularSize;
	private final double magnitude;
	
	
	private PlanetModel(String name, double tropicalYear, double epsilon, double omega, double e, double a , double i, double bigOmega, double angularSize, double magnitude) {
		this.name = name;
		this.tropicalYear = tropicalYear;
		this.epsilon = Angle.ofDeg(epsilon);
		this.omega = Angle.ofDeg(omega);
		this.e = e;
		this.a = a;
		double irad = Angle.ofDeg(i);
		this.cosi = Math.cos(irad);
		this.sini = Math.sin(irad);
		this.bigOmega = Angle.ofDeg(bigOmega);
		this.angularSize = Angle.ofArcsec(angularSize);
		this.magnitude = magnitude;
	}
	
	

	@Override
	public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
	    double meanAnomalyTerre = ( ( Angle.TAU / 365.242191 ) * ( daysSinceJ2010 / EARTH.tropicalYear ) ) + EARTH.epsilon - EARTH.omega;
	    double trueAnomalyTerre = meanAnomalyTerre + 2.0 * EARTH.e * Math.sin(meanAnomalyTerre);
	    double R = (EARTH.a * (1.0 - Math.pow(EARTH.e,2))) / (1.0 + (EARTH.e * Math.cos(trueAnomalyTerre)));
        double L = trueAnomalyTerre + EARTH.omega;
		
        double meanAnomaly = getMeanAnomaly(daysSinceJ2010);
		double trueAnomaly = getTrueAnomaly(meanAnomaly);
		double r = getRadius(trueAnomaly);
		double l = getLongInOrbPlane(trueAnomaly);
		double psi = getLatHelio(l);
		double rprime = getRp(r,psi);
		double lprime = getLp(l);
		double lambda = getLon(rprime,lprime,R,L);
		double beta = getLat(rprime,lprime,lambda,psi,R,L);
		double rho = getRho(R,r,l,L,psi);
		double angularSize = getAngularSize(rho);
		double magnitude = getMagnitude(lambda,l,r,rho);
		EquatorialCoordinates coord = eclipticToEquatorialConversion.apply(EclipticCoordinates.of(Angle.normalizePositive(lambda), beta));
		return new Planet(name,coord,(float)angularSize,(float)magnitude);
	}
	
	
	
	private double getMeanAnomaly(double D) {
		return (Angle.TAU / EARTH_TROPICAL_YEAR ) * ( D / tropicalYear ) + epsilon - omega;
	}
	private double getTrueAnomaly(double M) {
		return M + 2 * e * Math.sin(M);
	}
	private double getRadius(double v) {
		return ( a * ( 1 - e * e ) ) / ( 1 + e * Math.cos(v) );
	}
	private double getLongInOrbPlane(double v) {
		return v + omega;
	}
	
	private double getLatHelio(double l) {
		return Math.asin(Math.sin(l-bigOmega) * sini);
	}
	
	private double getRp(double r , double psi) {
		return r * Math.cos(psi);
	}
	private double getLp(double l) {
		return Math.atan2(Math.sin(l-bigOmega) * cosi ,Math.cos(l-bigOmega) )+ bigOmega;
	}
	
	
	private double getLonforSup(double rp,double lp , double R, double L) {
		return lp + Math.atan2(R * Math.sin(lp-L) , rp - R * Math.cos(lp-L) );
	}
	private double getLonforInf(double rp,double lp , double R, double L) {
		return Angle.TAU/2 + L + Math.atan2(rp * Math.sin(L-lp) , R - rp * Math.cos(L-lp) );
	}
	
	private double getLon(double rp,double lp , double R, double L) {
		switch (this) {
		case MARS:
		case JUPITER:
		case SATURN:
		case URANUS:
		case NEPTUNE:
			return getLonforSup(rp,lp,R,L);
		case MERCURY:
		case VENUS:
			return getLonforInf(rp,lp,R,L);
		case EARTH:
			break;
			
		}
		return 0;
	}
	
	private double getLat(double rp,double lp , double lambda, double psi, double R, double L) {
		return Math.atan( (rp * Math.tan(psi) * Math.sin(lambda-lp) ) / (R * Math.sin(lp-L) ) );
	}
	
	private double getAngularSize(double rho) {
		return angularSize/rho;
	}
	private double getRho(double R, double r, double l, double L,double psi) {
		return Math.sqrt(R * R + r * r - 2 * R * r * Math.cos(l-L) *Math.cos(psi) );
	}
	
	private double getMagnitude(double lambda, double l,double r,double rho ) {
		double F = ( 1 + Math.cos(lambda-l) ) / 2;
		return magnitude + 5 * Math.log10( ( r * rho ) / ( Math.sqrt(F) ) );
	}
	
	
}

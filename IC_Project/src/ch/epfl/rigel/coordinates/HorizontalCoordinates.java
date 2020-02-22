package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;

public class HorizontalCoordinates extends SphericalCoordinates {

	private HorizontalCoordinates(double longtitude, double latitude) {
		super(longtitude, latitude);
	}

	public static HorizontalCoordinates of(double az, double alt) {
		if(az<0 || az >= Angle.TAU || alt>Angle.TAU/4 || alt<-Angle.TAU/4)
			throw new IllegalArgumentException();
		return new HorizontalCoordinates(az,alt);
	}
	
	public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
		if(azDeg<0 || azDeg >= 360 || altDeg>90 || altDeg<-90)
			throw new IllegalArgumentException();
		return new HorizontalCoordinates(Angle.ofDeg(azDeg),Angle.ofDeg(altDeg));
	}
	public double az() {
		return lon();
	}
	public double azDeg() {
		return lonDeg();
	}
	public String azOctantName(String n, String e, String s, String w) {
		String out="";
		if(azDeg()>292.5 || azDeg()<76.5)
			out+=n;
		else if(azDeg()>112.5 && azDeg()<247.5)
			out+=s;
		
		if(azDeg()>22.5 && azDeg()<157.5)
			out+=e;
		else if(azDeg()>202.5 && azDeg()<=337.5)
			out+=w;
		return out;
	}
	public double alt() {
		return lat();
	}
	public double altDeg() {
		return latDeg();
	}
	public double angularDistanceTo(HorizontalCoordinates that) {
		return Math.acos( Math.sin(this.lat()) * Math.sin(that.lat())  +  Math.cos(this.lat()) * Math.cos(that.lat()) * Math.cos(this.lon()-that.lon())  );
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg() , altDeg() ) ;
	}

}

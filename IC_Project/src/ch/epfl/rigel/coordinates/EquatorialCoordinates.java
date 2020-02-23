package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;

public final class EquatorialCoordinates extends SphericalCoordinates {

    private EquatorialCoordinates (double ra , double dec){
        super(ra,dec);
    }
    
    public static EquatorialCoordinates of(double ra, double dec) {
        if(ra<0 || ra >= Angle.TAU || dec>Angle.TAU/4 || dec<-Angle.TAU/4)
            throw new IllegalArgumentException();
        return new EquatorialCoordinates(ra,dec);
    }
    
   
    
    
    public double ra() {
        return super.lon();
    }
    public double dec() {
        return super.lat();
    }
    public double raDeg() {
        return super.lonDeg();
    }
    public double raHr() {
        return Angle.toHr(super.lon());
    }
    
    public double decDeg() {
        return super.latDeg();
    }
    
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(lon=%.4fh, dec=%.4f°)",raHr() , decDeg()) ; 
    }
}

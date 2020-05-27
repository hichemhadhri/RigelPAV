package ch.epfl.rigel.coordinates;

import java.util.function.Function;

import ch.epfl.rigel.math.Angle;

/**
 * StereographicProjection
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates>{
     private final double cosC,sinC,alpha; 
     private final HorizontalCoordinates centre; 
  
    /**StereographicProjection constructor
     * @param center : center of the projection 
     */
    public StereographicProjection(HorizontalCoordinates center) {
        cosC= Math.cos(center.alt());
        sinC=Math.sin(center.alt()); 
        alpha = center.az() ; 
        centre= center; 
   }

    
    
    /**Calculates the center of the circle of the projection of the parallel passing by hor
     * @param hor : coordinates of the parallel
     * @return Coordinates of  the center
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
 
        double yx =cosC/(sinC+Math.sin(hor.alt()));
        return CartesianCoordinates.of(0, yx); 
    }
    
    /**Calculates the radius of the circle of the projection of the parallel passing by hor
     * @param parallel 
     * @return radius
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return Math.abs(Math.cos(parallel.alt())/(sinC+Math.sin(parallel.alt()))); 
    }
    
    /**Calculates the diameter of the projected sphere of angular size rad
     * @param rad : angular size of the sphere
     * @return diameter
     */
    public double applyToAngle(double rad) {
        return 2*Math.tan(rad/4); 
    }
    
  
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double cosD = Math.cos(azAlt.az()-alpha); 
        double sinD = Math.sin(azAlt.az()-alpha);
        double cosA=Math.cos(azAlt.alt());
        double sinA = Math.sin(azAlt.alt()); 
        double d =1/(1+sinA*sinC+Math.cos(azAlt.alt())*cosC*cosD);
        return CartesianCoordinates.of(d*cosA*sinD,d*(sinA*cosC-cosA*sinC*cosD)) ; 
    }
    
    /**Calculates the horizontalCoordinates of the point which its projected coordinates is xy
     * @param xy : coordinates of the projection
     * @return HorizontalCoordinates
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        if(xy.x()==0 && xy.y()==0)
            return HorizontalCoordinates.of(Angle.normalizePositive(alpha), centre.alt()); 
        double p =Math.sqrt( Math.pow( xy.x() , 2 ) + Math.pow( xy.y() , 2 ) );
        double psqr =  Math.pow( p , 2 );
        double sinc = 2 * p / ( psqr + 1 );
        double cosc = ( 1 - psqr) / ( psqr + 1 ); 
        double alp = Math.atan2( ( xy.x() * sinc ) ,( p * cosC * cosc - xy.y() * sinC * sinc ) ) + alpha;
        double ang = Math.asin( cosc * sinC + ( ( xy.y() * sinc * cosC ) / p ) );
        return HorizontalCoordinates.of(Angle.normalizePositive(alp), ang); 
    }
    
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException(); 
    }
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException(); 
    }
    
    @Override 
    public String toString() {
        return "StereographicProjection : " + centre.toString() ; 
    } 
}

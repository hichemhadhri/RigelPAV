package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */

public final class Preconditions {
    
   
    private Preconditions() {
        
    }
    
    /**Verifies if Argument is true or not
     * 
     * @param isTrue : boolean to verify 
     * @throws IllegalArgumentException if FALSE
     */
    public static void checkArgument(boolean isTrue) {
        if(!isTrue)
            throw new IllegalArgumentException("boolean equals to FALSE"); 
    }
    
    /**
    * Check if value in Interval 
	* @param interval : Interval
	* @param value : double to verify
	* @throws IllegalArgumentException if value is NOT in interval 
	* @return value if in interval
	*/
    public static  double checkInInterval(Interval interval, double value) {
       if(!interval.contains(value))
           throw new IllegalArgumentException("value not in interval"); 
       return value; 
    }
}

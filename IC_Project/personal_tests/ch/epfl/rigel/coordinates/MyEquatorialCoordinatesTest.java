package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

class MyEquatorialCoordinatesTest {


   
    @Test
    void ofFailsOnUnvalid() {
        assertThrows(IllegalArgumentException.class,() -> {
           EclipticCoordinates.of(-20,-1)  ;
        });
        assertThrows(IllegalArgumentException.class,() -> {
            EclipticCoordinates.of(0,Math.PI) ;
        });
        assertThrows(IllegalArgumentException.class,() -> {
            EclipticCoordinates.of(3*Math.PI,0)  ;
        });
    }
    
    
    
    @Test
    void ofWorksOnValid() {
        EclipticCoordinates coord = EclipticCoordinates.of(0,0);
        assertEquals(0,coord.lat(),1e-8);
        assertEquals(0, coord.lon(),1e-8);
        coord = EclipticCoordinates.of(Math.PI,Math.PI/2);
        assertEquals(Math.PI,coord.lon(),1e-8);
        assertEquals(Math.PI/2,coord.lat(),1e-8);
          }
    
    @Test 
    void ToStringTest() {
        EclipticCoordinates coord = EclipticCoordinates.of(Angle.ofDeg(22.5), Angle.ofDeg(18));
        String str = coord.toString();
        System.out.println(str);
        assertEquals("(λ=22.5000°, β=18.0000°)",str);
        
    }
}

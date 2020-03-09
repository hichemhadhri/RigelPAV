package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

class MyEclipticCoordinatesTest {

  
    @Test
    void ofFailsOnUnvalid() {
        assertThrows(IllegalArgumentException.class,() -> {
            EquatorialCoordinates.of(-1,-20)  ;
        });
        assertThrows(IllegalArgumentException.class,() -> {
            EquatorialCoordinates.of(3*Math.PI,0) ;
        });
        assertThrows(IllegalArgumentException.class,() -> {
            EquatorialCoordinates.of(0,3*Math.PI)  ;
        });
    }
    
    
    
    @Test
    void ofWorksOnValid() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(0,0);
        assertEquals(0,coord.ra(),1e-8);
        assertEquals(0, coord.dec(),1e-8);
        coord = EquatorialCoordinates.of(Math.PI,Math.PI/2);
        assertEquals(Math.PI/2,coord.dec(),1e-8);
        assertEquals(Math.PI,coord.ra(),1e-8);
          }
    @Test 
    void ToStringTest() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(Angle.ofHr(1.5), Angle.ofDeg(45));
        String str = coord.toString();
        System.out.println(str);
        assertEquals("(ra=1.5000h, dec=45.0000°)",str);
        
    }

}

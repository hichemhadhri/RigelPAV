package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class MyGeographicCoordinatesTest {

    @Test
    void isValidLonDegReturnsTrueOnValid() {
        assertTrue(GeographicCoordinates.isValidLonDeg(-90));
        assertTrue(GeographicCoordinates.isValidLonDeg(0));
        assertTrue(GeographicCoordinates.isValidLonDeg(-180));
        assertTrue(GeographicCoordinates.isValidLonDeg(179));
    }
    
    @Test
    void isValidLonDegReturnsFalseOnUnvalid() {
        assertFalse(GeographicCoordinates.isValidLonDeg(200));
        assertFalse(GeographicCoordinates.isValidLonDeg(180));
        assertFalse(GeographicCoordinates.isValidLonDeg(-200));
        assertFalse(GeographicCoordinates.isValidLonDeg(200));
    }
    
    @Test
    void isValidLatDegReturnsTrueOnValid() {
        assertTrue(GeographicCoordinates.isValidLatDeg(50));
        assertTrue(GeographicCoordinates.isValidLonDeg(0));
        assertTrue(GeographicCoordinates.isValidLonDeg(-90));
        assertTrue(GeographicCoordinates.isValidLonDeg(90));
    }
    
    @Test
    void isValidLatDegReturnsFalseUnvalid() {
        assertFalse(GeographicCoordinates.isValidLatDeg(150));
        assertFalse(GeographicCoordinates.isValidLonDeg(300));
        assertFalse(GeographicCoordinates.isValidLonDeg(-200));
    }
    
    @Test
    void ofDegFailsOnUnvalid() {
        assertThrows(IllegalArgumentException.class,() -> {
            GeographicCoordinates.ofDeg(-200,-200)  ;
        });
        assertThrows(IllegalArgumentException.class,() -> {
            GeographicCoordinates.ofDeg(180,90)  ;
        });
        assertThrows(IllegalArgumentException.class,() -> {
            GeographicCoordinates.ofDeg(150,91)  ;
        });
    }
    
    @Test
    void ofDegWorksNormalCase() {
        GeographicCoordinates coord = GeographicCoordinates.ofDeg(0,0);
        assertEquals(0,coord.lonDeg(),1e-8);
        assertEquals(0,coord.latDeg(),1e-8);
        coord = GeographicCoordinates.ofDeg(50,12);
        assertEquals(50,coord.lonDeg(),1e-8);
        assertEquals(12,coord.latDeg(),1e-8); 
    }
    
    @Test 
    void ToStringTest() {
        GeographicCoordinates coord = GeographicCoordinates.ofDeg(6.57, 46.52);
        String str = coord.toString();
        assertEquals("(lon=6.5700°, lat=46.5200°)",str);
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}

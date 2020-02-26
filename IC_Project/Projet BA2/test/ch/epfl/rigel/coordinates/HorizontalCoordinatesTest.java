package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HorizontalCoordinatesTest {

   

    @Test
    void ofDegFailsOnUnvalid() {
        assertThrows(IllegalArgumentException.class,() -> {
            HorizontalCoordinates.ofDeg(-10,-200)  ;
        });
        assertThrows(IllegalArgumentException.class,() -> {
            HorizontalCoordinates.ofDeg(360,1)  ;
        });
        assertThrows(IllegalArgumentException.class,() -> {
            HorizontalCoordinates.ofDeg(1,100)  ;
        });
    }

    @Test
    void ofDegWorksOnValid() {
        HorizontalCoordinates coord = HorizontalCoordinates.ofDeg(0,0);
        assertEquals(0,coord.azDeg(),1e-8);
        assertEquals(0, coord.altDeg(),1e-8);
        coord = HorizontalCoordinates.ofDeg(180,90);
        assertEquals(180,coord.azDeg(),1e-8);
        assertEquals(90, coord.altDeg(),1e-8);    
    }
    
    
    @Test
    void ofFailsOnUnvalid() {
        assertThrows(IllegalArgumentException.class,() -> {
            HorizontalCoordinates.of(-1,-20)  ;
        });
        assertThrows(IllegalArgumentException.class,() -> {
            HorizontalCoordinates.of(0,Math.PI) ;
        });
        assertThrows(IllegalArgumentException.class,() -> {
            HorizontalCoordinates.of(3*Math.PI,0)  ;
        });
    }
    
    
    
    @Test
    void ofWorksOnValid() {
        HorizontalCoordinates coord = HorizontalCoordinates.of(0,0);
        assertEquals(0,coord.az(),1e-8);
        assertEquals(0, coord.alt(),1e-8);
        coord = HorizontalCoordinates.of(Math.PI,Math.PI/2);
        assertEquals(Math.PI,coord.az(),1e-8);
        assertEquals(Math.PI/2,coord.alt(),1e-8);
        
    }
    
    @Test
    void angluarDistanceTest() {
        HorizontalCoordinates coord1 = HorizontalCoordinates.ofDeg(6.5682,46.5183);
        HorizontalCoordinates coord2 = HorizontalCoordinates.ofDeg(8.5476,47.3763);
        assertEquals(0.0279,coord1.angularDistanceTo(coord2),1e-4);   
    }
    
    @Test
    void azOctantNameTest() {
        HorizontalCoordinates coord = HorizontalCoordinates.ofDeg(0,0);
        assertEquals("N",coord.azOctantName("N", "E", "S", "W"));
        coord = HorizontalCoordinates.ofDeg(90,0);
        assertEquals("E",coord.azOctantName("N", "E", "S", "W"));
        coord = HorizontalCoordinates.ofDeg(180,0);
        assertEquals("S",coord.azOctantName("N", "E", "S", "W"));
        coord = HorizontalCoordinates.ofDeg(270,0);
        assertEquals("W",coord.azOctantName("N", "E", "S", "W"));
        coord = HorizontalCoordinates.ofDeg(23,0);
        assertEquals("NE",coord.azOctantName("N", "E", "S", "W"));
        coord = HorizontalCoordinates.ofDeg(315,0);
        assertEquals("NW",coord.azOctantName("N", "E", "S", "W"));
        coord = HorizontalCoordinates.ofDeg(225,0);
        assertEquals("SW",coord.azOctantName("N", "E", "S", "W")); 
        coord = HorizontalCoordinates.ofDeg(22.5,0);
        assertEquals("N",coord.azOctantName("N", "E", "S", "W")); 
    }
    
    @Test 
    void ToStringTest() {
        HorizontalCoordinates coord = HorizontalCoordinates.ofDeg(350, 7.2);
        String str = coord.toString();
        System.out.println(str);
        assertEquals("(az=350.0000°, alt=7.2000°)",str);
        
    }
    
    
    



}

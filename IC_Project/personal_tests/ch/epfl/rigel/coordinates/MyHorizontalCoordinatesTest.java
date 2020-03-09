package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyHorizontalCoordinatesTest {

    @Test
    void ofWorksOnValidParam(){
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var az = rng.nextDouble(0,360);
            var alt = rng.nextDouble(-90,90);

            var horCoordinates1= HorizontalCoordinates.ofDeg(az,alt);
            assertEquals(az, horCoordinates1.azDeg(), 1e-6);
            assertEquals(alt, horCoordinates1.altDeg(), 1e-6);

            var horCoordinates2= HorizontalCoordinates.of(Angle.ofDeg(az),Angle.ofDeg(alt));
            assertEquals(az, horCoordinates2.azDeg(), 1e-6);
            assertEquals(alt, horCoordinates2.altDeg(), 1e-6);

        }
        HorizontalCoordinates horCoordinates1=HorizontalCoordinates.ofDeg(0,-90);
        assertEquals(0, horCoordinates1.azDeg(), 1e-6);
        assertEquals(-90, horCoordinates1.altDeg(), 1e-6);

        HorizontalCoordinates horCoordinates2=HorizontalCoordinates.ofDeg(0,90);
        assertEquals(0, horCoordinates2.azDeg(), 1e-6);
        assertEquals(90, horCoordinates2.altDeg(), 1e-6);
    }

    @Test
    void ofFailsOnInvalidParam() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var az = rng.nextDouble(360, 1000);
            var alt = rng.nextDouble(90, 1000);
            assertThrows(IllegalArgumentException.class, () -> {
                HorizontalCoordinates.ofDeg(az, alt);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                HorizontalCoordinates.of(Angle.ofDeg(az),Angle.ofDeg(alt));
            });
        }
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(360, 0);
        });
        
        

    }

    @Test
    void angularDistanceWorks() {
       double distance= HorizontalCoordinates.ofDeg(6.5682, 46.5183)
                .angularDistanceTo(HorizontalCoordinates.ofDeg(8.5476,47.3763));
       assertEquals(0.0279,distance,1e-4);

    }


    @Test
    void azOctantNameWorks(){
        double[] values = new double[] { 10, 22.5, 30, 22.4999, 44.9999, 45, 55, 67.4999, 67.5, 85, 89.9999, 90, 105,
                112.4999, 112.5, 125, 134.9999, 135, 145, 157.4999, 157.5, 179.9999, 180, 195,202.4999, 202.5, 215,
        224.9999, 225, 240, 247.4999, 247.5, 265, 269.9999, 270, 285, 292.4999, 292.5, 305, 314.9999, 315, 337.4999,
        345, 359.9999};
        String[] octant = new String[] { "N", "NE", "NE", "N", "NE", "NE", "NE", "NE" , "E", "E", "E", "E", "E", "E",
                "SE", "SE", "SE", "SE", "SE", "SE", "S", "S","S","S", "S", "SO", "SO", "SO", "SO", "SO", "SO", "O", "O",
                "O", "O", "O", "O", "NO" , "NO", "NO", "NO", "NO", "N","N"};
        for(int i=0; i<values.length;++i) {
        	System.out.println(values[i]);
            assertEquals(octant[i],HorizontalCoordinates.ofDeg(values[i],0).azOctantName("N","E", "S",
                    "O") );

        }

    }





}




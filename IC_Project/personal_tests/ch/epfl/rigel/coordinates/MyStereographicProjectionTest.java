package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.Moon;
import ch.epfl.rigel.astronomy.Sun;
import ch.epfl.rigel.math.Angle;

class MyStereographicProjectionTest {

    @Test
    void applyWorks(){
        HorizontalCoordinates h1 = HorizontalCoordinates.of(Math.PI/4, Math.PI/6);
        HorizontalCoordinates center1 = HorizontalCoordinates.of(0,0);
        StereographicProjection e = new StereographicProjection(center1);
        double p = Math.sqrt(6);
        CartesianCoordinates a1 = CartesianCoordinates.of(p/(4+p), 2/(4+p));
        CartesianCoordinates c1 = e.apply(h1);
        assertEquals(a1.x(), c1.x(), 1e-8);
        assertEquals(a1.y(), c1.y(), 1e-8);

        HorizontalCoordinates h2 = HorizontalCoordinates.of(Math.PI/2, Math.PI/2);
        HorizontalCoordinates center2 = HorizontalCoordinates.of(Math.PI/4, Math.PI/4);
        StereographicProjection e2 = new StereographicProjection(center2);
        double p2 = Math.sqrt(2);
        CartesianCoordinates a2 = CartesianCoordinates.of(0, p2/(2+p2));
        CartesianCoordinates c2 = e2.apply(h2);
        assertEquals(a2.x(), c2.x(), 1e-8);
        assertEquals(a2.y(), c2.y(), 1e-8);
    }

    @Test
    void circleCenterForParallelWorks(){
        HorizontalCoordinates h1 = HorizontalCoordinates.of(Math.PI/4, Math.PI/6);
        HorizontalCoordinates center1 = HorizontalCoordinates.of(0,0);
        StereographicProjection s = new StereographicProjection(center1);
        CartesianCoordinates a1 = s.circleCenterForParallel(h1);
        assertEquals(0, a1.x(), 1e-10);
        assertEquals(2, a1.y(), 1e-10);
    }

    @Test
    void circleRadiusForParallelWorks(){
        HorizontalCoordinates h2 = HorizontalCoordinates.of(Math.PI/2, Math.PI/2);
        HorizontalCoordinates center2 = HorizontalCoordinates.of(Math.PI/4, Math.PI/4);
        StereographicProjection e2 = new StereographicProjection(center2);
        double rho1 = e2.circleRadiusForParallel(h2);
        assertEquals(0, rho1, 1e-10);
    }

    @Test
    void applyToAngle(){
        HorizontalCoordinates center2 = HorizontalCoordinates.of(Math.PI/4, Math.PI/4);
        StereographicProjection e2 = new StereographicProjection(center2);
        double z = e2.applyToAngle(Math.PI);
        System.out.println(z);
    }
    
 

    @Test
    void moonTest(){
        Moon moon = new Moon(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(19.7)), 37.5f, -1, 0.3752f);
        assertEquals("Lune", moon.name());
        assertEquals("Lune (37.5%)", moon.info());
        assertEquals(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(19.7)).dec(), moon.equatorialPos().dec());
        assertEquals(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(19.7)).ra(), moon.equatorialPos().ra()); //checking equatorial position
        assertThrows(IllegalArgumentException.class, () -> {new Moon(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(19.7)), 37.5f, -1, -0.1f); });
    }

    @Test
    void sunTest(){

        //tests variés pour sun et moon
        Sun sun = new Sun(EclipticCoordinates.of(Angle.ofDeg(53), Angle.ofDeg(38)),
                EquatorialCoordinates.of(Angle.ofDeg(55.8),Angle.ofDeg(24)),
                0.4f, 5.f);
        assertEquals("Soleil", sun.info());
        assertEquals(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(24)).dec(), sun.equatorialPos().dec());
        assertEquals(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(19.7)).ra(), sun.equatorialPos().ra()); //checking equatorial position
        assertEquals(5.f, sun.meanAnomaly());
        assertEquals(-26.7f, sun.magnitude());

        //test pour eclipticPos throws un null
        assertThrows(NullPointerException.class, () -> { new Sun(null,
                EquatorialCoordinates.of(Angle.ofDeg(55.8),Angle.ofDeg(24)),
                0.4f, 5.f); });

    }
}

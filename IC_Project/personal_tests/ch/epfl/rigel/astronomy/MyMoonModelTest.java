package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

class MyMoonModelTest {
    ZonedDateTime zdt =  ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC);
    @Test
    void at() {
//        assertEquals(0 , MoonModel.MOON.at(Epoch.J2010.daysUntil(zdt), new EclipticToEquatorialConversion(zdt))
//                .equatorialPos().raDeg());
        
        assertEquals(0.009225908666849136 ,MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),
                ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of(
                LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),ZoneOffset.UTC))).
                angularSize());
        //assertEquals(14.211456457836 ,MoonModel.MOON.at(-2313, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,  Month.SEPTEMBER, 1),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().raHr());
      // assertEquals(0,MoonModel.MOON.at(-2313, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,  Month.SEPTEMBER, 1),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().dec());
        assertEquals("Lune (22.5%)" ,MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, 9, 1),LocalTime.of(0, 0),
        ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of( LocalDate.of(2003, 9, 1),
        LocalTime.of(0, 0),ZoneOffset.UTC))).
        info());
    }
    
    
    ZonedDateTime d1= ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1),
            LocalTime.of(0, 0),
            ZoneOffset.UTC);
    ZonedDateTime a = ZonedDateTime.of(
            LocalDate.of(2003, Month.JULY, 30),
            LocalTime.of(15, 0),
            ZoneOffset.UTC);
    ZonedDateTime b = ZonedDateTime.of(
            LocalDate.of(2020, Month.MARCH, 20),
            LocalTime.of(0, 0),
            ZoneOffset.UTC);
    ZonedDateTime c = ZonedDateTime.of(
            LocalDate.of(2006, Month.JUNE, 16),
            LocalTime.of(18, 13),
            ZoneOffset.UTC);
    ZonedDateTime d = ZonedDateTime.of(
            LocalDate.of(2000, Month.JANUARY, 3),
            LocalTime.of(18, 0),
            ZoneOffset.UTC);
    ZonedDateTime e = ZonedDateTime.of(
            LocalDate.of(1999, Month.DECEMBER, 6),
            LocalTime.of(23, 3),
            ZoneOffset.UTC);
    EclipticToEquatorialConversion ae= new EclipticToEquatorialConversion(a);
    EclipticToEquatorialConversion be= new EclipticToEquatorialConversion(b);
    EclipticToEquatorialConversion ce= new EclipticToEquatorialConversion(c);
    EclipticToEquatorialConversion de= new EclipticToEquatorialConversion(d);
    EclipticToEquatorialConversion ee= new EclipticToEquatorialConversion(e);
    EclipticToEquatorialConversion ed1= new EclipticToEquatorialConversion(d1);
    Moon luned1 = MoonModel.MOON.at(Epoch.J2010.daysUntil(d1),ed1);
    Moon lunea = MoonModel.MOON.at(Epoch.J2010.daysUntil(a),ae);
    Moon luneb = MoonModel.MOON.at(Epoch.J2010.daysUntil(b),be);
    Moon lunec = MoonModel.MOON.at(Epoch.J2010.daysUntil(c),ce);
    Moon luned = MoonModel.MOON.at(Epoch.J2010.daysUntil(d),de);
    Moon lunee = MoonModel.MOON.at(Epoch.J2010.daysUntil(e),ee);

    @Test
    public void atWorksEquatorialCoordinates(){
        assertEquals(3.7205506003957067,luned1.equatorialPos().ra(), 1e-10);
        assertEquals(-0.20114171346014936,luned1.equatorialPos().dec(), 1e-10);

        assertEquals(2.5774730714106253,lunea.equatorialPos().ra(), 1e-10);
        assertEquals(0.32306783345874074,lunea.equatorialPos().dec(), 1e-10);

        assertEquals(5.494870752028857,luneb.equatorialPos().ra(), 1e-10);
        assertEquals(-0.35536693112418194,luneb.equatorialPos().dec(), 1e-10);

        assertEquals(5.833597896802137,lunec.equatorialPos().ra(), 1e-10);
        assertEquals(-0.23268121611547493,lunec.equatorialPos().dec(), 1e-10);

        assertEquals(4.3458584045705075,luned.equatorialPos().ra(), 1e-10);
        assertEquals(-0.31026704508957587,luned.equatorialPos().dec(), 1e-10);

        assertEquals(4.234563123966964,lunee.equatorialPos().ra(), 1e-10);
        assertEquals(-0.28625647573032476,lunee.equatorialPos().dec(), 1e-10);


    }

    @Test
    public void atWorksPhase(){
//        assertEquals(0.22500607,luned1.getPhase(), 1e-8);
//        assertEquals(0.020678004,lunea.getPhase(), 1e-8);
//        assertEquals(0.16724092,luneb.getPhase(), 1e-8);
//        assertEquals(0.70552105,lunec.getPhase(), 1e-8);
//        assertEquals(0.07966227,luned.getPhase(), 1e-8);
//        assertEquals(0.008791369,lunee.getPhase(), 1e-8);

    }
   

}

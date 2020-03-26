package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

class MySunModelTest {

   @Test
   void sunAt() {
//       EclipticToEquatorialConversion eclipticToEquatorialConversion= new EclipticToEquatorialConversion(ZonedDateTime.of(2003,7,27, 0,0,0,0,ZoneId.of("UTC")));
//  Sun sun =  SunModel.SUN.at(Epoch.J2010.daysUntil(ZonedDateTime.of(1988,7,27, 0,0,0,0,ZoneId.of("UTC"))), 
//               eclipticToEquatorialConversion);
//   System.out.println(Angle.toDeg(sun.equatorialPos().dec()));
//    assertEquals(Angle.ofDMS(19, 21, 10),sun.equatorialPos().dec(),1e-8);
      assertEquals(19.35288373097352 , SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().decDeg());
   assertEquals(19.35288373097352 ,SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 27), LocalTime.of(0, 0, 0, 0), 
           ZoneOffset.UTC))).equatorialPos().decDeg());
   assertEquals(5.9325494700300885  ,SunModel.SUN.at(27 + 31, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2010,  Month.FEBRUARY, 27),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().ra() 
           );
   assertEquals(8.392682808297806  ,SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().raHr()
           );
  ZonedDateTime zdt = ZonedDateTime.of(LocalDate.of(1988, Month.JULY, 27), LocalTime.of(0, 0), ZoneOffset.UTC);
   
       assertEquals(0.3353207024580374,SunModel.SUN.at(Epoch.J2010.daysUntil(zdt), new EclipticToEquatorialConversion(zdt)).equatorialPos().dec());
   
       assertEquals(0.009162353351712227 ,SunModel.SUN.at(Epoch.J2010.daysUntil(zdt),new EclipticToEquatorialConversion(zdt)).angularSize());
   
       
       assertEquals(10515 ,new Star(24436, "Rigel", EquatorialCoordinates.of(0, 0), 0, -0.03f)
.colorTemperature());
       assertEquals(3793  ,new Star(27989, "Betelgeuse", EquatorialCoordinates.of(0, 0), 0, 1.50f)
       .colorTemperature());
   ZonedDateTime e= ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 27),
           LocalTime.of(0,0),
           ZoneOffset.UTC);
   EclipticToEquatorialConversion h= new EclipticToEquatorialConversion(e);
   EquatorialCoordinates h1= h.apply(EclipticCoordinates.of(Angle.ofDeg(123.580601),0.0));
   System.out.println(h1.decDeg());
   
   assertEquals(11.18715493470968 ,PlanetModel.JUPITER.at(-2231.0,
           new EclipticToEquatorialConversion(
                   ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                           LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
           
           .equatorialPos().raHr());
   assertEquals(6.35663550668575  ,PlanetModel.JUPITER.at(-2231.0,
           new EclipticToEquatorialConversion(
                   ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                           LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
           
           .equatorialPos().decDeg(),1e-10);
   
   assertEquals(35.11141185362771 ,Angle.toDeg(PlanetModel.JUPITER.at(-2231.0,new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).angularSize())*3600);
   assertEquals(-1.9885659217834473  ,PlanetModel.JUPITER.at(-2231.0,new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).magnitude());

   
   assertEquals(16.8200745658971 ,PlanetModel.MERCURY.at(-2231.0,
        new EclipticToEquatorialConversion(
                ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                        LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                        
        .equatorialPos().raHr(),1e-10);
   assertEquals(-24.500872462861  ,PlanetModel.MERCURY.at(-2231.0,
           new EclipticToEquatorialConversion(
                   ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                           LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                           
           .equatorialPos().decDeg(),1e-10);
   }
}

package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

class MySunModelTest {

   @Test
   void sunAt() {
       EclipticToEquatorialConversion eclipticToEquatorialConversion= new EclipticToEquatorialConversion(ZonedDateTime.of(2003,7,27, 0,0,0,0,ZoneId.of("UTC")));
  Sun sun =  SunModel.SUN.at(Epoch.J2010.daysUntil(ZonedDateTime.of(1988,7,27, 0,0,0,0,ZoneId.of("UTC"))), 
               eclipticToEquatorialConversion);
    
    assertEquals(Angle.ofDMS(19, 21, 10),sun.equatorialPos().dec(),1e-4);
       
   }

}

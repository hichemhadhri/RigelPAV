package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        ZonedDateTime e0= ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0,0),
                ZoneOffset.UTC);
        ZonedDateTime e1= ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 27),
                LocalTime.of(0,0),
                ZoneOffset.UTC);
        ZonedDateTime e2= ZonedDateTime.of(LocalDate.of(2007, Month.JANUARY, 3),
                LocalTime.of(0,0),
                ZoneOffset.UTC);
        ZonedDateTime e3= ZonedDateTime.of(LocalDate.of(2015, Month.OCTOBER, 15),
                LocalTime.of(6,0),
                ZoneOffset.UTC);
        ZonedDateTime e4= ZonedDateTime.of(LocalDate.of(2020, Month.FEBRUARY, 26),
                LocalTime.of(12,47,12),
                ZoneOffset.UTC);
        ZonedDateTime e5= ZonedDateTime.of(LocalDate.of(2030, Month.MARCH, 7),
                LocalTime.of(0,0),
                ZoneOffset.UTC);
        EclipticToEquatorialConversion h= new EclipticToEquatorialConversion(e0);
        EclipticToEquatorialConversion h1= new EclipticToEquatorialConversion(e1);
        EclipticToEquatorialConversion h2= new EclipticToEquatorialConversion(e2);
        EclipticToEquatorialConversion h3= new EclipticToEquatorialConversion(e3);
        EclipticToEquatorialConversion h4= new EclipticToEquatorialConversion(e4);
        EclipticToEquatorialConversion h5= new EclipticToEquatorialConversion(e5);


        Sun soleil0 = SunModel.SUN.at(Epoch.J2010.daysUntil(e1),h1);
        System.out.println("1.");
        System.out.println(soleil0.equatorialPos());
        System.out.println("sunLon "+soleil0.equatorialPos().raHr());
        System.out.println("sunlat "+soleil0.equatorialPos().decDeg());
        System.out.println("BookSunLon "+(8+23/60.0+34/3600.0));
        System.out.println("BookSunLat "+(19+21/60.0+10/3600.0));
        System.out.println("Sun AngularSize "+ soleil0.angularSize());
        System.out.println("**************************************************************");
        System.out.println("2.");
        Sun soleil1 = SunModel.SUN.at(Epoch.J2010.daysUntil(e0),h);
        System.out.println(soleil1.equatorialPos());
        System.out.println("sunLon "+soleil1.equatorialPos().raHr());
        System.out.println("sunlat "+soleil1.equatorialPos().decDeg());
        System.out.println("sunMeanAnomaly "+soleil1.meanAnomaly());
        System.out.println("**************************************************************");
        System.out.println("3.");
        Sun soleil2 = SunModel.SUN.at(Epoch.J2010.daysUntil(e2),h2);
        System.out.println(soleil2.equatorialPos());
        System.out.println("sunLon "+soleil2.equatorialPos().raHr());
        System.out.println("sunlat "+soleil2.equatorialPos().decDeg());
        System.out.println("sunMeanAnomaly "+soleil2.meanAnomaly());
        System.out.println("**************************************************************");
        System.out.println("4.");
        Sun soleil3 = SunModel.SUN.at(Epoch.J2010.daysUntil(e3),h3);
        System.out.println(soleil3.equatorialPos());
        System.out.println("sunLon "+soleil3.equatorialPos().raHr());
        System.out.println("sunlat "+soleil3.equatorialPos().decDeg());
        System.out.println("sunMeanAnomaly "+soleil3.meanAnomaly());
        System.out.println("**************************************************************");
        System.out.println("5.");
        Sun soleil4 = SunModel.SUN.at(Epoch.J2010.daysUntil(e4),h4);
        System.out.println(soleil4.equatorialPos());
        System.out.println("sunLon "+soleil4.equatorialPos().raHr());
        System.out.println("sunlat "+soleil4.equatorialPos().decDeg());
        System.out.println("sunMeanAnomaly "+soleil4.meanAnomaly());
        System.out.println("**************************************************************");
        System.out.println("6.");
        Sun soleil5 = SunModel.SUN.at(Epoch.J2010.daysUntil(e5),h5);
        System.out.println(soleil5.equatorialPos());
        System.out.println("sunLon "+soleil5.equatorialPos().raHr());
        System.out.println("sunlat "+soleil5.equatorialPos().decDeg());
        System.out.println("sunMeanAnomaly "+soleil5.meanAnomaly());
        System.out.println("**************************************************************");
        System.out.println("**************************************************************");
        System.out.println("**************************************************************");
        System.out.println("Jupiter");
        System.out.println("1.");
        Planet jupiter0= PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(e0),h);
        System.out.println(jupiter0.equatorialPos());
        System.out.println("lon: "+jupiter0.equatorialPos().raHr());
        System.out.println("lat: "+jupiter0.equatorialPos().decDeg());
        System.out.println("bookJupiterLon "+(11+11/60.0+14/3600.0));
        System.out.println("BookJupiterLat "+(6+21/60.0+25/3600.0));
        System.out.println("angularSize "+ jupiter0.angularSize());
        System.out.println("Magnitude "+jupiter0.magnitude());
        System.out.println("**************************************************************");
        System.out.println("2.");
        Planet jupiter1= PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(e1),h1);
        System.out.println("lon: "+jupiter1.equatorialPos().raHr());
        System.out.println("lat: "+jupiter1.equatorialPos().decDeg());
        System.out.println("angularSize "+ jupiter1.angularSize());
        System.out.println("Magnitude "+jupiter1.magnitude());
        System.out.println("**************************************************************");
        System.out.println("3.");
        Planet jupiter2= PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(e2),h2);
        System.out.println("lon: "+jupiter2.equatorialPos().raHr());
        System.out.println("lat: "+jupiter2.equatorialPos().decDeg());
        System.out.println("angularSize "+ jupiter2.angularSize());
        System.out.println("Magnitude "+jupiter2.magnitude());
        System.out.println("**************************************************************");
        System.out.println("4.");
        Planet jupiter3= PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(e3),h3);
        System.out.println("lon: "+jupiter3.equatorialPos().raHr());
        System.out.println("lat: "+jupiter3.equatorialPos().decDeg());
        System.out.println("angularSize "+ jupiter3.angularSize());
        System.out.println("Magnitude "+jupiter3.magnitude());
        System.out.println("**************************************************************");
        System.out.println("4.");
        Planet jupiter4= PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(e4),h4);
        System.out.println("lon: "+jupiter4.equatorialPos().raHr());
        System.out.println("lat: "+jupiter4.equatorialPos().decDeg());
        System.out.println("angularSize "+ jupiter4.angularSize());
        System.out.println("Magnitude "+jupiter4.magnitude());
        System.out.println("**************************************************************");
        System.out.println("**************************************************************");
        System.out.println("**************************************************************");
        System.out.println("Mercure");
        System.out.println("1.");
        Planet mercurry0= PlanetModel.MERCURY.at(Epoch.J2010.daysUntil(e0),h);
        System.out.println(mercurry0.equatorialPos());
        System.out.println("lon: "+mercurry0.equatorialPos().raHr());
        System.out.println("lat: "+mercurry0.equatorialPos().decDeg());
        System.out.println("BookLon "+(16+49/60.0+12/3600.0));
        System.out.println("BookLat "+(-24-30/60.0-0.9/3600.0));
        System.out.println("angularSize "+ mercurry0.angularSize());
        System.out.println("Magnitude "+mercurry0.magnitude());
        System.out.println("**************************************************************");
        System.out.println("2.");
        Planet mercurry1= PlanetModel.MERCURY.at(Epoch.J2010.daysUntil(e1),h1);
        System.out.println(mercurry1.equatorialPos());
        System.out.println("lon: "+mercurry1.equatorialPos().raHr());
        System.out.println("lat: "+mercurry1.equatorialPos().decDeg());
        System.out.println("angularSize "+ mercurry1.angularSize());
        System.out.println("Magnitude "+mercurry1.magnitude());
        System.out.println("**************************************************************");
        System.out.println("3.");
        Planet mercurry2= PlanetModel.MERCURY.at(Epoch.J2010.daysUntil(e2),h2);
        System.out.println(mercurry2.equatorialPos());
        System.out.println("lon: "+mercurry2.equatorialPos().raHr());
        System.out.println("lat: "+mercurry2.equatorialPos().decDeg());
        System.out.println("angularSize "+ mercurry2.angularSize());
        System.out.println("Magnitude "+mercurry2.magnitude());
        System.out.println("**************************************************************");
        System.out.println("4.");
        Planet mercurry3= PlanetModel.MERCURY.at(Epoch.J2010.daysUntil(e3),h3);
        System.out.println(mercurry3.equatorialPos());
        System.out.println("lon: "+mercurry3.equatorialPos().raHr());
        System.out.println("lat: "+mercurry3.equatorialPos().decDeg());
        System.out.println("angularSize "+ mercurry3.angularSize());
        System.out.println("Magnitude "+mercurry3.magnitude());
        System.out.println("**************************************************************");
        System.out.println("5.");
        Planet mercurry4= PlanetModel.MERCURY.at(Epoch.J2010.daysUntil(e4),h4);
        System.out.println(mercurry4.equatorialPos());
        System.out.println("lon: "+mercurry4.equatorialPos().raHr());
        System.out.println("lat: "+mercurry4.equatorialPos().decDeg());
        System.out.println("angularSize "+ mercurry4.angularSize());
        System.out.println("Magnitude "+mercurry4.magnitude());
        System.out.println("**************************************************************");
        System.out.println("**************************************************************");
        System.out.println("**************************************************************");
        System.out.println("Stars colorTemperature");
        Star star0 = new Star(0,"star0",EquatorialCoordinates.of(0,0),2f,0.31f);
        Star star1 = new Star(1,"star1",EquatorialCoordinates.of(0,0),1f,5f);
        Star star2 = new Star(5,"star2",EquatorialCoordinates.of(0,0),3f,-0.5f);
        Star star3 = new Star(7,"star3",EquatorialCoordinates.of(0,0),2.5f,0f);
        Star star4 = new Star(0,"star4",EquatorialCoordinates.of(0,0),2f,2.22f);
        Star star5 = new Star(0,"star5",EquatorialCoordinates.of(0,0),2f,-0.33f);

        System.out.println("1. "+star0.colorTemperature());
        System.out.println("2. "+star1.colorTemperature());
        System.out.println("3. "+star2.colorTemperature());
        System.out.println("4. "+star3.colorTemperature());
        System.out.println("5. "+star4.colorTemperature());
        System.out.println("6. "+star5.colorTemperature());









    }
}



package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.gui.BlackBodyColor;
import javafx.scene.paint.Color;

class MyObservedSkyTest {

    @Test
    void test() throws IOException {
        String HYG_CATALOGUE_NAME ="/hygdata_v3.csv";String AST_CATALOGUE_NAME ="/asterisms.txt";StarCatalogue catalogue;ObservedSky sky;
        StereographicProjection stereo;
        GeographicCoordinates geoCoords;
        ZonedDateTime time;
        EquatorialToHorizontalConversion convEquToHor;
        EclipticToEquatorialConversion convEcltoEqu;
        StarCatalogue.Builder builder;
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) 
        {builder = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE);}
        try (InputStream astStream = getClass().getResourceAsStream(AST_CATALOGUE_NAME)) 
        {catalogue = builder.loadFrom(astStream, AsterismLoader.INSTANCE).build();}
        time = ZonedDateTime.of(LocalDate.of(2020, Month.APRIL, 4),LocalTime.of(0, 0), ZoneOffset.UTC);
        geoCoords = GeographicCoordinates.ofDeg(30, 45);
        stereo = new StereographicProjection(HorizontalCoordinates.ofDeg(20, 22));
        convEquToHor = new EquatorialToHorizontalConversion(time, geoCoords);
        convEcltoEqu = new EclipticToEquatorialConversion(time);
        sky = new ObservedSky(time, geoCoords, stereo, catalogue); 
        
    assertEquals("Tau Phe",sky.ObjectClosestTo(stereo.apply(new EquatorialToHorizontalConversion(time,geoCoords).apply(EquatorialCoordinates.of(0.004696959812148989,-0.861893035343076))),0.1).get().name());
    assertEquals(Optional.empty(),
            sky.ObjectClosestTo(stereo.apply(new EquatorialToHorizontalConversion(time,geoCoords)
                    .apply(EquatorialCoordinates.of(0.04696959812148989,-0.8618930353430763))),0.001));
    int i = 0;for (Star star : sky.stars()) {assertEquals(stereo.apply(convEquToHor.apply(star.equatorialPos())).x(),
            sky.starsPositions()[i]);i += 2;}
            assertEquals(catalogue.stars().size(),sky.stars().size());
            //Si fail: Cloner/Copier le tableau
            double memory = sky.starsPositions()[0];
            sky.starsPositions()[0] = Double.MAX_VALUE;
            assertEquals(memory, sky.starsPositions()[0]);
    }
    
    @Test
    void backTest() {
        assertEquals(Color.web("#ffcc99"),BlackBodyColor.colorForTemperature(3798.1409));
        assertEquals(Color.web("#c8d9ff"),BlackBodyColor.colorForTemperature(10500d));
        assertThrows(IllegalArgumentException.class, ()-> BlackBodyColor.colorForTemperature(40_000.00001d));
        assertEquals(Color.web("#ff3800"), BlackBodyColor.colorForTemperature(1000));
        assertEquals(Color.web("#ff8912"), BlackBodyColor.colorForTemperature(2000));
        assertEquals(Color.web("#ffdbba"), BlackBodyColor.colorForTemperature(4500));
        assertEquals(Color.web("#ccdbff"), BlackBodyColor.colorForTemperature(10000));
        assertEquals(Color.web("#9bbcff"), BlackBodyColor.colorForTemperature(40000));

        assertEquals(Color.web("#ffcc99"), BlackBodyColor.colorForTemperature(3798));
        assertEquals(Color.web("#ffcc99"), BlackBodyColor.colorForTemperature(3802));

        assertEquals(Color.web("#ff3800"), BlackBodyColor.colorForTemperature(1049));
        assertEquals(Color.web("#ff8912"), BlackBodyColor.colorForTemperature(1951));

        assertThrows(IllegalArgumentException.class, () -> BlackBodyColor.colorForTemperature(999));
        assertThrows(IllegalArgumentException.class, () -> BlackBodyColor.colorForTemperature(40001));
    }

}

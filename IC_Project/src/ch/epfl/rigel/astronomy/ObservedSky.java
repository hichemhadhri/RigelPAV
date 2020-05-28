package ch.epfl.rigel.astronomy;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;

/**
 * ObservedSky class
 * 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public class ObservedSky {

    private static final int NB_PLANETS = 7;
    private final StarCatalogue catalogue;
    private final List<Planet> planets;
    private final Sun sun;
    private final Moon moon;
    private final CartesianCoordinates sunPos, moonPos;
    private final double[] planetsPos, starsPos;
    private final Map<CelestialObject, CartesianCoordinates> map;

    /**
     * ObservedSky Constructor
     * 
     * @param time
     *            : observation time
     * @param coords
     *            : observation coordinates
     * @param proj
     *            : projection
     * @param catalogue
     *            : star catalogue
     */
    public ObservedSky(ZonedDateTime time, GeographicCoordinates coords,
            StereographicProjection proj, StarCatalogue catalogue) {
        map = new HashMap<>();

        this.catalogue = catalogue;

        EclipticToEquatorialConversion ecToEq = new EclipticToEquatorialConversion(
                time);
        EquatorialToHorizontalConversion eqToHo = new EquatorialToHorizontalConversion(
                time, coords);

        sun = SunModel.SUN.at(Epoch.J2010.daysUntil(time), ecToEq);
        sunPos = proj.apply(eqToHo.apply(sun.equatorialPos()));
        map.put(sun, sunPos);

        moon = MoonModel.MOON.at(Epoch.J2010.daysUntil(time), ecToEq);
        moonPos = proj.apply(eqToHo.apply(moon.equatorialPos()));
        map.put(moon, moonPos);

        planetsPos = new double[2 * NB_PLANETS];
        this.planets = new ArrayList<>();
        int i = 0;
        for (PlanetModel planet : PlanetModel.values()) {
            if (planet.equals(PlanetModel.EARTH))
                continue;
            planets.add(planet.at(Epoch.J2010.daysUntil(time), ecToEq));
            planetsPos[2 * i] = proj
                    .apply(eqToHo.apply(planets.get(i).equatorialPos())).x();
            planetsPos[2 * i + 1] = proj
                    .apply(eqToHo.apply(planets.get(i).equatorialPos())).y();
            map.put(planets.get(i), CartesianCoordinates.of(planetsPos[2 * i],
                    planetsPos[2 * i + 1]));
            i++;
        }
        starsPos = new double[catalogue.stars().size() * 2];
        i = 0;
        for (Star star : catalogue.stars()) {
            starsPos[2 * i] = proj.apply(eqToHo.apply(star.equatorialPos()))
                    .x();
            starsPos[2 * i + 1] = proj.apply(eqToHo.apply(star.equatorialPos()))
                    .y();
            map.put(star, CartesianCoordinates.of(starsPos[2 * i],
                    starsPos[2 * i + 1]));

            i++;
        }
    }

    /**
     * Sun Getter
     * 
     * @return sun
     */
    public Sun sun() {
        return sun;
    }

    /**
     * sunPosition Getter
     * 
     * @return sun position
     */
    public CartesianCoordinates sunPosition() {
        return CartesianCoordinates.of(sunPos.x(), sunPos.y());
    }

    /**
     * Moon Getter
     * 
     * @return moon
     */
    public Moon moon() {
        return moon;
    }

    /**
     * moonPosition Getter
     * 
     * @return moon position
     */
    public CartesianCoordinates moonPosition() {
        return CartesianCoordinates.of(moonPos.x(), moonPos.y());
    }

    /**
     * Planets getter
     * 
     * @return planets
     */
    public List<Planet> planets() {
        return List.copyOf(planets);
    }

    /**
     * planetsPositions getter
     * 
     * @return planets Positions
     */
    public double[] planetsPositions() {
        return planetsPos.clone();
    }

    /**
     * returns list of stars
     * 
     * @return stars
     */
    public List<Star> stars() {
        return catalogue.stars();
    }

    /**
     * starsPositions getter
     * 
     * @return stars positions
     */
    public double[] starsPositions() {
        return starsPos.clone();
    }

    /**
     * returns set of asterisms
     * 
     * @return asterisms positions
     */
    public Set<Asterism> asterisms() {
        return catalogue.asterisms();
    }

    /**
     * returns asterims' indices
     * 
     * @param asterism
     * @return
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        return catalogue.asterismIndices(asterism);
    }

    /**
     * returns the closet object to the given coordinates within a mamximum
     * radius
     * 
     * @param coords
     *            : given coordiantes
     * @param max
     *            : max radius
     * @return closest object
     * 
     */
    public Optional<CelestialObject> ObjectClosestTo(
            CartesianCoordinates coords, double max) {
        Iterator<CartesianCoordinates> iterator = map.values().iterator();

        CartesianCoordinates nextCoordinates, minCoordinates;
        minCoordinates = iterator.next();
        double min = distance(minCoordinates, coords);

        while (iterator.hasNext()) {
            nextCoordinates = iterator.next();
            if (distance(nextCoordinates, coords) < min) {
                min = distance(nextCoordinates, coords);
                minCoordinates = nextCoordinates;
            }

        }
        if (min <= max) {
            for (CelestialObject cel : map.keySet()) {
                if (map.get(cel) == minCoordinates) {
                    return Optional.of(cel);
                }
            }
        }

        return Optional.empty();

    }

    private double distance(CartesianCoordinates coord1,
            CartesianCoordinates coord2) {
        return Math.sqrt(Math.pow(coord1.x() - coord2.x(), 2)
                + Math.pow(coord1.y() - coord2.y(), 2));
    }

}

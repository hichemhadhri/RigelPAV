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

public class ObservedSky {
    private final ZonedDateTime time;
    private final GeographicCoordinates coords; 
    private final StereographicProjection proj ; 
    private final StarCatalogue catalogue; 
    private final  List<Planet>   planets ; 
    private final Sun sun ; 
    private final Moon moon  ;
    private final CartesianCoordinates sunPos, moonPos; 
    private final double[] planetsPos, starsPos; 
    private final Map<CelestialObject,CartesianCoordinates> map; 
    
    public ObservedSky(ZonedDateTime time,GeographicCoordinates coords,StereographicProjection proj,StarCatalogue catalogue)
    {
        map=new HashMap<>(); 
        this.time = time; 
        this.coords = coords; 
        this.proj = proj; 
        this.catalogue = catalogue; 
        
        EclipticToEquatorialConversion conversion = new EclipticToEquatorialConversion(time); 
        EquatorialToHorizontalConversion conversion2 = new EquatorialToHorizontalConversion(time,coords); 
        
        sun = SunModel.SUN.at(Epoch.J2010.daysUntil(time), conversion);
        sunPos= proj.apply(conversion2.apply(sun.equatorialPos()));
        map.put(sun, sunPos);
        
        moon = MoonModel.MOON.at(Epoch.J2010.daysUntil(time), conversion ); 
        moonPos= proj.apply(conversion2.apply(moon.equatorialPos()));
        map.put(moon, moonPos); 
        
        planetsPos=new double[14];
        this.planets = new ArrayList<>(); 
        int i=0;
        for(PlanetModel planet : PlanetModel.values()) {
            if(planet.equals(PlanetModel.EARTH))
                continue; 
            planets.add(planet.at(Epoch.J2010.daysUntil(time),conversion));
            planetsPos[2*i]= proj.apply(conversion2.apply(planets.get(i).equatorialPos())).x(); 
            planetsPos[2*i+1]= proj.apply(conversion2.apply(planets.get(i).equatorialPos())).y(); 
            map.put(planets.get(i), CartesianCoordinates.of(planetsPos[2*i], planetsPos[2*i+1]));
            i++;
        }
        starsPos=new double[catalogue.stars().size()*2];
        i =0;
        for(Star star : catalogue.stars()) {
            starsPos[2*i]= proj.apply(conversion2.apply(star.equatorialPos())).x(); 
            starsPos[2*i+1]= proj.apply(conversion2.apply(star.equatorialPos())).y(); 
            map.put(star, CartesianCoordinates.of(starsPos[2*i], starsPos[2*i+1]));

            i++;
        }    
    }
    
    public Sun sun() {
        return sun ; 
    }
    
    public CartesianCoordinates sunPosition() {
        return CartesianCoordinates.of(sunPos.x(), sunPos.y());
    }
    
    public Moon moon() {
        return moon ; 
    }
    
    public CartesianCoordinates moonPosition() {
        return CartesianCoordinates.of(moonPos.x(), moonPos.y()); 
    }
    
    public List<Planet> planets(){
        return List.copyOf(planets); 
    }
    
    public double[] planetsPositions() {
        return planetsPos.clone();
    }
    
    public List<Star> stars(){
        return catalogue.stars();  //TODO: should we make a copy even if catalogue.stars() returns an unmodifiable list
    }
    
    public double[] starsPositions() {
        return starsPos.clone(); //TODO : is this the right way to copy an array
    }
    
    
    public Set<Asterism> asterisms(){
        return catalogue.asterisms(); 
    }
    
    public List<Integer> asterismIndices(Asterism asterism){
        return catalogue.asterismIndices(asterism); 
    }
    
    public Optional<CelestialObject> ObjectClosestTo(CartesianCoordinates coords, double max) {
        Iterator<CartesianCoordinates> iterator = map.values().iterator();
        
        CartesianCoordinates nextCoordinates,minCoordinates; 
        minCoordinates = iterator.next();
        double min = distance(minCoordinates,coords) ;
        
        while(iterator.hasNext()) {
            nextCoordinates = iterator.next();
           if(distance(nextCoordinates,coords)<min) {
               min = distance(nextCoordinates,coords); 
               minCoordinates = nextCoordinates; 
           }
           
        }
        if(min<=max)
        {
            for(CelestialObject cel : map.keySet()) {
                if(map.get(cel)==minCoordinates) {
                    return Optional.of(cel); 
                }
            }
        }
        
        return Optional.empty();
            
    }
    
    
    
    private double distance(CartesianCoordinates coord1, CartesianCoordinates coord2) {
        return Math.sqrt(Math.pow( coord1.x()-coord2.x() , 2 ) + Math.pow( coord1.y()-coord2.y() , 2 ));
    }
        
    
    
}

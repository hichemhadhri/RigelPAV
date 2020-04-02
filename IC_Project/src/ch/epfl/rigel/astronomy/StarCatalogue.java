package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.rigel.Preconditions;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public final class StarCatalogue {
	private final List<Star> stars;
	private final List<Asterism> asterisms;
	private final Map<Asterism,List<Integer>> map;
	
	
	/**
	 * @author Mohamed Hichem Hadhri (300434)
	 * @author Khalil Haroun Achache (300350)
	 *
	 */
	public interface Loader{
		void load(InputStream inputStream, Builder builder) throws IOException;
	}
	
	/**
	 * @author Mohamed Hichem Hadhri (300434)
	 * @author Khalil Haroun Achache (300350)
	 *
	 */
	public final static class Builder{
		private List<Star> stars;
		private List<Asterism> asterisms;
		public Builder() {
			this.stars = new ArrayList<Star>();
			this.asterisms = new ArrayList<Asterism>();
		}
		public Builder addStar(Star star) {
			stars.add(star);
			return this;
		}
		public List<Star> stars(){
			return Collections.unmodifiableList(stars);
		}
		public Builder addAsterism(Asterism asterism) {
			asterisms.add(asterism);
			return this;
		}
		public List<Asterism> asterisms(){
			return Collections.unmodifiableList(asterisms);
		}
		public Builder loadFrom(InputStream inputStream, Loader loader)throws IOException {
			loader.load(inputStream, this);
			return this;
		}
		public StarCatalogue build() {
			return new StarCatalogue(stars(),asterisms());
		}
		
	}
	
	/**
	 * @param stars
	 * @param asterisms
	 */
	public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
		
		Iterator<Asterism> iterator=asterisms.iterator();
		while(iterator.hasNext()) {
			Preconditions.checkArgument(stars.containsAll(iterator.next().stars()));	
		}
		
		this.stars = List.copyOf(stars);
		this.map = intializeMap(asterisms,this.stars);
		//System.out.println(this.map);
		//TODO: Check
		this.asterisms = List.copyOf(asterisms);
		
	}
	public List<Star> stars(){
		return stars;
	}
	public Set<Asterism> asterisms(){
		return this.map.keySet();
	}
	
	/**
	 * @param asterism
	 * @return
	 * @throws IllegalArgumentException if the asterism doesn't belong to the catalog  
	 */
	public List<Integer> asterismIndices(Asterism asterism){
		Preconditions.checkArgument(asterisms.contains(asterism));
		
		return map.get(asterism);
	}
	private Map<Asterism,List<Integer>> intializeMap(List<Asterism> asterisms,List<Star>stars){
        Map<Asterism,List<Integer>> result = new HashMap<Asterism,List<Integer>>();
        Asterism nextAsterism;
        
        Iterator<Asterism> asterismsIterator = asterisms.iterator();
        while(asterismsIterator.hasNext()) {
            List<Integer> container = new ArrayList<Integer>();
            nextAsterism = asterismsIterator.next();
            container.clear();
            for(Star star : nextAsterism.stars()) {
             //  if(star.hipparcosId()==97886) System.out.println(stars.indexOf(star));
                container.add(stars.indexOf(star));
            }
            result.put(nextAsterism, container);
            
        }
        return result;
    }
}

package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.rigel.Preconditions;

/**
 * StarCatalogue Class
 * 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public final class StarCatalogue {
	private final List<Star> stars;
	private final List<Asterism> asterisms;
	private final Map<Asterism, List<Integer>> map;

	/**
	 * StarCatalogue constructor
	 * 
	 * @param stars
	 * @param asterisms
	 */
	public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {

		Iterator<Asterism> iterator = asterisms.iterator();
		while (iterator.hasNext()) {
			Preconditions.checkArgument(stars.containsAll(iterator.next().stars()));
		}

		this.stars = List.copyOf(stars);
		this.map = intializeMap(asterisms, this.stars);
		this.asterisms = List.copyOf(asterisms);

	}

	/**
	 * Getter for stars
	 * 
	 * @return stars list
	 */
	public List<Star> stars() {
		return stars;
	}

	/**
	 * Getter for asterisms
	 * 
	 * @return astersims set
	 */
	public Set<Asterism> asterisms() {
		return Collections.unmodifiableSet(this.map.keySet());
	}

	/**
	 * Returns the asterism's stars indices
	 * 
	 * @param asterism
	 * @return A list of the index of the stars in the hygdatabase
	 * @throws IllegalArgumentException if the asterism doesn't belong to the
	 *                                  catalog
	 */
	public List<Integer> asterismIndices(Asterism asterism) {
		Preconditions.checkArgument(asterisms.contains(asterism));

		return List.copyOf(map.get(asterism));
	}

	private Map<Asterism, List<Integer>> intializeMap(List<Asterism> asterisms, List<Star> stars) {
		Map<Asterism, List<Integer>> result = new HashMap<Asterism, List<Integer>>();
		Asterism nextAsterism;

		Iterator<Asterism> asterismsIterator = asterisms.iterator();
		while (asterismsIterator.hasNext()) {
			List<Integer> container = new ArrayList<Integer>();
			nextAsterism = asterismsIterator.next();
			container.clear();
			for (Star star : nextAsterism.stars()) {

				container.add(stars.indexOf(star));
			}
			result.put(nextAsterism, Collections.unmodifiableList(container));

		}
		return result;
	}

	/**
	 * Loader Interface
	 * 
	 * @author Mohamed Hichem Hadhri (300434)
	 * @author Khalil Haroun Achache (300350)
	 *
	 */
	public interface Loader {
		/**
		 * Loads from a file
		 * 
		 * @param inputStream
		 * @param builder
		 * @throws IOException when the file cannot be correctly loaded
		 */
		void load(InputStream inputStream, Builder builder) throws IOException;
	}

	/**
	 * @author Mohamed Hichem Hadhri (300434)
	 * @author Khalil Haroun Achache (300350)
	 *
	 */
	public final static class Builder {
		private List<Star> stars;
		private List<Asterism> asterisms;

		/**
		 * Builder Constructor
		 * 
		 */
		public Builder() {
			this.stars = new ArrayList<Star>();
			this.asterisms = new ArrayList<Asterism>();
		}

		/**
		 * Add star
		 * 
		 * @param star to add
		 * @return builder
		 */
		public Builder addStar(Star star) {
			stars.add(star);
			return this;
		}

		/**
		 * Returns current loaded stars
		 * 
		 * @return stars
		 */
		public List<Star> stars() {
			return Collections.unmodifiableList(stars);
		}

		/**
		 * Add asterism
		 * 
		 * @param asterism to add
		 * @return builder
		 */
		public Builder addAsterism(Asterism asterism) {
			asterisms.add(asterism);
			return this;
		}

		/**
		 * Returns current loaded asterisms
		 * 
		 * @return asterisms
		 */
		public List<Asterism> asterisms() {
			return Collections.unmodifiableList(asterisms);
		}

		/**
		 * Loads a specified object from file
		 * 
		 * @param inputStream
		 * @param loader
		 * @return the same builder to allow cascading operations
		 * @throws IOException
		 */
		public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
			loader.load(inputStream, this);
			return this;
		}

		/**
		 * Builds the starCatalogue
		 * 
		 * @return the built catalogue
		 */
		public StarCatalogue build() {
			return new StarCatalogue(stars(), asterisms());
		}

	}
}

package ch.epfl.rigel.astronomy;

import java.util.List;

import ch.epfl.rigel.Preconditions;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public final class Asterism {
	
	private final List<Star> stars;
	
	/** constructor for Asterism
	 * @param stars: list of stars
	 */
	public Asterism(List<Star> stars){
		Preconditions.checkArgument(!stars.isEmpty());
		this.stars=List.copyOf(stars);
	}
	
	
	/**stars Getter
	 * @return stars list of the asterism
	 */
	public List<Star> stars(){
		return stars;
	}
}

package ch.epfl.rigel.astronomy;

import java.util.List;

import ch.epfl.rigel.Preconditions;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
//TODO: Check about immuability
public final class Asterism {
	
	private final List<Star> stars;
	
	
	
	//////No acces modifiers specified( not even package private)
	public Asterism(List<Star> stars){
		Preconditions.checkArgument(stars.isEmpty());
		this.stars=List.copyOf(stars);
	}
	
	
	public List<Star> stars(){
		return List.copyOf(stars);
	}
}

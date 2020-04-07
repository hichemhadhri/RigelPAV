package ch.epfl.rigel.coordinates;

/**CartesianCoordinates class
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 * CartesianCoordinates class
 *  x , y
 *
 */
public final class CartesianCoordinates {
	private final double x;
	private final double y;
	
	private CartesianCoordinates (double x, double y) {
		this.x=x;
		this.y=y;
	}

    /**Creates a new CartesianCoordiantes object from given x and y
     * @param x : abscissa
     * @param y : ordinate
     * @return CartisianCoordinates Object
     */
	public static CartesianCoordinates of(double x, double y) {
		return new CartesianCoordinates(x,y);
	}
	
	/** x getter
	 * @return x
	 */
	public double x() {
		return x;
	}
	
	/**y getter 
	 * @return y
	 */
	public double y() {
		return y;
	}
	
    /** Get the textual representation of the coordinates
     * @return textual representation
     */
	@Override
	public String toString() {
		return "(x: "+x+" ,y: "+y+")";
	}
	
    /**
     * @throws UnsupportedOperationException()
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException(); 
    }
    /**
     * @throws UnsupportedOperationException()
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException(); 
    }
    
	
}

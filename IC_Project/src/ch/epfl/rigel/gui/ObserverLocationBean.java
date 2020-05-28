package ch.epfl.rigel.gui;


import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public class ObserverLocationBean {
	private DoubleProperty lonDeg;
	private DoubleProperty latDeg;
	private ObjectBinding<GeographicCoordinates> coordinates;
	
	/** ObserverLocationBean constructor
	 * 
	 */
	public ObserverLocationBean() {
		lonDeg= new SimpleDoubleProperty();
		latDeg=new SimpleDoubleProperty();
		coordinates=Bindings.createObjectBinding(()->GeographicCoordinates.ofDeg(lonDeg.doubleValue(),latDeg.doubleValue()), lonDeg,latDeg);
	}
	/** Getter for lonDeg property
	 * @return longitude in degrees property
	 */
	public DoubleProperty lonDegProperty() {
		return lonDeg;
	}
	/** lonDeg Setter
	 * @param lonDeg
	 */
	public void setLonDeg(double lonDeg) {
		this.lonDeg.set(lonDeg);
	}
	/** Getter for lonDeg
	 * @return longitutde in degrees
	 */
	public double getLonDeg() {
		return this.lonDeg.doubleValue();
	}
	
	/** Getter for latDeg property
	 * @return latitude in degrees property
	 */
	public DoubleProperty latDegProperty() {
		return latDeg;
	}
	/** latDeg setter
	 * @param latDeg
	 */
	public void setLatDeg(double latDeg) {
		this.latDeg.set(latDeg);
	}
	/** Getter for latDeg
	 * @return latitude in degrees
	 */
	public double getLatDeg() {
		return this.latDeg.doubleValue();
	}
	
	/** Getter for coordinates binding
	 * @return coordinates binding
	 */
	public ObjectBinding<GeographicCoordinates>coordinates(){
		return coordinates;
	}
	
	/** coordinates Setter
	 * @param coords
	 */
	public void setCoordinates(GeographicCoordinates coords) {
		this.lonDeg.set(coords.lonDeg());
		this.latDeg.set(coords.latDeg());
	}
	
	/** Getter for Coordinates
	 * @return coordinates
	 */
	public GeographicCoordinates getCoordinates() {
		return coordinates.get();
	}
	
}

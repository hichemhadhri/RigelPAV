package ch.epfl.rigel.gui;


import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ObserverLocationBean {
	private DoubleProperty lonDeg;
	private DoubleProperty latDeg;
	private ObjectBinding<GeographicCoordinates> coordinates;
	
	public ObserverLocationBean() {
		lonDeg= new SimpleDoubleProperty();
		latDeg=new SimpleDoubleProperty();
		coordinates=Bindings.createObjectBinding(()->GeographicCoordinates.ofDeg(lonDeg.doubleValue(),latDeg.doubleValue()), lonDeg,latDeg);
	}
	public DoubleProperty lonDeg() {
		return lonDeg;
	}
	public void setLonDeg(double lonDeg) {
	    System.out.println("hi");
		this.lonDeg.set(lonDeg);
	}
	public double getLonDeg() {
		return this.lonDeg.doubleValue();
	}
	
	public DoubleProperty latDeg() {
		return latDeg;
	}
	public void setLatDeg(double latDeg) {
		this.latDeg.set(latDeg);
	}
	public double getLatDeg() {
		return this.latDeg.doubleValue();
	}
	
	public ObjectBinding<GeographicCoordinates>coordinates(){
		return coordinates;
	}
	////A verifier: non optimisee, creer 2 fois un objet
	public void setCoordinates(GeographicCoordinates coords) {
		this.lonDeg.set(Angle.toDeg(coords.lon()));
		this.latDeg.set(Angle.toDeg(coords.lat()));
	}
	
	public GeographicCoordinates getCoordinates() {
		return coordinates.get();
	}
	
}

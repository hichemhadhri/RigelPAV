package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public class ViewingParametersBean {
	private DoubleProperty fieldOfViewDeg;
	private ObjectProperty<HorizontalCoordinates> center;

	/**
	 * ViewingParametersBean constructor
	 */
	public ViewingParametersBean() {
		fieldOfViewDeg = new SimpleDoubleProperty();
		center = new SimpleObjectProperty<>();
	}


	/** Getter for center property
	 * @return center
	 */
	public ObjectProperty<HorizontalCoordinates> centerProperty() {
		return center;
	}

	/** Getter for center coordinates
	 * @return center
	 */
	public HorizontalCoordinates getCenter() {
		return center.get();
	}

	/** center Setter
	 * @param center
	 */
	public void setCenter(HorizontalCoordinates center) {
		this.center.set(center);
	}

	/** Getter for fieldOfView property
	 * @return
	 */
	public DoubleProperty fieldOfViewDegProperty() {
		return fieldOfViewDeg;
	}
	/** Getter for fieldOfView
	 * @return fieldOfView in degrees
	 */
	public double getFieldOfViewDeg() {
		return fieldOfViewDeg.get();
	}


	/** fieldOfView Setter
	 * @param fieldDeg
	 */
	public void setFieldOfViewDeg(double fieldDeg) {
		this.fieldOfViewDeg.set(fieldDeg);
	}

}

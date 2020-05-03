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

	public ViewingParametersBean() {
		fieldOfViewDeg = new SimpleDoubleProperty();
		center = new SimpleObjectProperty<>();
	}

	public HorizontalCoordinates getCenter() {
		return center.get();
	}

	public ObjectProperty<HorizontalCoordinates> center() {
		return center;
	}

	public double getFieldOfViewDeg() {
		return fieldOfViewDeg.get();
	}

	public DoubleProperty fieldOfViewDeg() {
		return fieldOfViewDeg;
	}

	public void setCenter(HorizontalCoordinates center) {
		this.center.set(center);
	}

	public void setFieldOfViewDeg(double fieldDeg) {
		this.setFieldOfViewDeg(fieldDeg);
	}

}

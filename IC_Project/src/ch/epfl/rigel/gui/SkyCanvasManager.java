package ch.epfl.rigel.gui;

import java.util.Optional;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Transform;

public class SkyCanvasManager {
	private final static int MAX_OBJECTS = 10;
	private final static double HORIZONTAL_INC_DEG = 10;
	private final static double VERTICAL_INC_DEG = 5;
	private DoubleBinding mouseAzDeg;
	private DoubleBinding mouseAltDeg;
	///// VERIFY
	private ObjectBinding<CelestialObject> objectUnderMouse;

	private ObjectBinding<StereographicProjection> projection;
	private ObjectBinding<Transform> planeToCanvas;
	private ObjectBinding<ObservedSky> observedSky;
	private ObjectProperty<CartesianCoordinates> mousePosition;
	private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;

	private Canvas canvas;
	private SkyCanvasPainter painter;

	public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dtBean, ObserverLocationBean olBean,
			ViewingParametersBean vpBean) {
		canvas = new Canvas(600, 800);
		painter = new SkyCanvasPainter(canvas);
		mousePosition = new SimpleObjectProperty<CartesianCoordinates>(CartesianCoordinates.of(0,0));
		projection = Bindings.createObjectBinding(() -> {
			return new StereographicProjection(vpBean.getCenter());
		}, vpBean.center());
		planeToCanvas = Bindings.createObjectBinding(() -> {
			double scale = calculateScale(canvas.widthProperty().get(),vpBean.getFieldOfViewDeg(),projection.get());
			return Transform.affine(scale, 0, 0, -scale, canvas.widthProperty().get()/2, canvas.heightProperty().get()/2);
		}, canvas.widthProperty(),canvas.heightProperty(), projection,vpBean.fieldOfViewDeg());
		observedSky = Bindings.createObjectBinding(() -> {
			return new ObservedSky(dtBean.getZonedDateTime(), olBean.getCoordinates(), projection.get(), catalogue);
		}, olBean.coordinates(), dtBean.dateProperty(), dtBean.timeProperty(), dtBean.zoneProperty(), projection);

		objectUnderMouse = Bindings.createObjectBinding(() -> {
			Point2D coordMouse = planeToCanvas.get().inverseTransform(mousePosition.get().x(),mousePosition.get().y());
			CartesianCoordinates coords = CartesianCoordinates.of(coordMouse.getX(), coordMouse.getY());
			return observedSky.get().ObjectClosestTo(coords, MAX_OBJECTS).get();
		}, observedSky, mousePosition, planeToCanvas);
		mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
			Point2D coords = planeToCanvas.get().inverseTransform(mousePosition.get().x(), mousePosition.get().y());
			return projection.get().inverseApply(CartesianCoordinates.of(coords.getX(), coords.getY()));
		}, mousePosition, projection, planeToCanvas);
		mouseAzDeg = Bindings.createDoubleBinding(() -> {
			return Angle.toDeg(mouseHorizontalPosition.get().az());
		}, mouseHorizontalPosition);
		mouseAltDeg = Bindings.createDoubleBinding(() -> {
			return Angle.toDeg(mouseHorizontalPosition.get().alt());
		}, mouseHorizontalPosition);

		canvas.setOnKeyPressed(key -> {
			if (key.getCode() == KeyCode.LEFT) {
				double val=(vpBean.getCenter().azDeg() - HORIZONTAL_INC_DEG);
				if (val<0) val+=360;
				vpBean.setCenter(HorizontalCoordinates.ofDeg(val%360,
						vpBean.getCenter().altDeg()));
			} else if (key.getCode() == KeyCode.RIGHT) {
				vpBean.setCenter(HorizontalCoordinates.ofDeg((vpBean.getCenter().azDeg() + HORIZONTAL_INC_DEG) % 360,
						vpBean.getCenter().altDeg()));
			} else if (key.getCode() == KeyCode.UP) {
				double clampedValue = Math.min(90, Math.max(5, vpBean.getCenter().altDeg() + VERTICAL_INC_DEG));
				vpBean.setCenter(HorizontalCoordinates.ofDeg(vpBean.getCenter().azDeg(), clampedValue));

			} else if (key.getCode() == KeyCode.DOWN) {
				double clampedValue = Math.min(90, Math.max(5, vpBean.getCenter().altDeg() - VERTICAL_INC_DEG));
				vpBean.setCenter(HorizontalCoordinates.ofDeg(vpBean.getCenter().azDeg(), clampedValue));
			}
		});
		canvas.setOnMousePressed(event -> {
			if (event.isPrimaryButtonDown()) {
				canvas.requestFocus();
			}
			event.consume();
		});
		canvas.setOnMouseMoved(event -> {
			mousePosition.set(CartesianCoordinates.of(event.getX(), event.getY()));
		});
		canvas.setOnScroll(event -> {
			double value = Math.abs(event.getDeltaX()) > Math.abs(event.getDeltaY())
					? event.getDeltaX()
					: -event.getDeltaY();
			vpBean.setFieldOfViewDeg(Math.min(150, Math.max(30, vpBean.getFieldOfViewDeg()+value)));
		});
		projection.addListener((o)->{
			painter.drawAll(observedSky.get(), planeToCanvas.get(), projection.get());
		});
		
		planeToCanvas.addListener((o)->{
			
			painter.drawAll(observedSky.get(), planeToCanvas.get(), projection.get());
		});
		
		//ajoutiha bech nupdati changement naarech ken fama makhir
		observedSky.addListener((o)->{
            
            painter.drawAll(observedSky.get(), planeToCanvas.get(), projection.get());
        });
		
		
	}
	public ObjectBinding<CelestialObject> objectUnderMouseProperty(){
		return objectUnderMouse;
	}
	public Canvas canvas() {
		return canvas;
	}
	public DoubleBinding mouseAzDegProperty() {
		return mouseAzDeg;
	}
	public DoubleBinding mouseAltDegProperty() {
		return mouseAltDeg;
	}

	private double calculateScale(double width, double fieldOfviewDeg, StereographicProjection projection) {
		return width / (projection.applyToAngle(Angle.ofDeg(fieldOfviewDeg)));
	}

}

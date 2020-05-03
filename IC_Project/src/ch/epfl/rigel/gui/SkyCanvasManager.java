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
	public DoubleBinding mouseAzDeg;
	public DoubleBinding mouseAltDeg;
	///// VERIFY
	public ObjectBinding<Optional<CelestialObject>> objectUnderMouse;

	private ObjectBinding<StereographicProjection> projection;
	private ObjectBinding<Transform> planeToCanvas;
	private ObjectBinding<ObservedSky> observedSky;
	private ObjectProperty<CartesianCoordinates> mousePosition;
	private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;

	private Canvas canvas;
	private SkyCanvasPainter painter;

	public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dtBean, ViewingParametersBean vpBean,
			ObserverLocationBean olBean) {
		canvas=new Canvas(600,800);
		painter= new SkyCanvasPainter(canvas);
		
		mousePosition=new SimpleObjectProperty<CartesianCoordinates>();
		projection = Bindings.createObjectBinding(() -> {
			return new StereographicProjection(vpBean.getCenter());
		}, vpBean.center());
		// planeToCanvas=Bindings.createObjectBinding(()->{return Transform.affine(1, 1,
		// 1, 1, 1, 1);}, canvas.widthProperty(),canvas.heightProperty(),projection);
		observedSky = Bindings.createObjectBinding(() -> {
			return new ObservedSky(dtBean.getZonedDateTime(), olBean.getCoordinates(), projection.get(), catalogue);
		}, olBean.coordinates(), dtBean.dateProperty(), dtBean.timeProperty(), dtBean.zoneProperty(), projection);

		objectUnderMouse = Bindings.createObjectBinding(() -> {
			return observedSky.get().ObjectClosestTo(mousePosition.get(), MAX_OBJECTS);
		}, observedSky, mousePosition, planeToCanvas);
		mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
			Point2D coords= planeToCanvas.get().inverseTransform(mousePosition.get().x(), mousePosition.get().y());
			return projection.get().inverseApply(CartesianCoordinates.of(coords.getX(),coords.getY()));
		}, mousePosition, projection, planeToCanvas);
		mouseAzDeg=Bindings.createDoubleBinding(()->{return Angle.toDeg(mouseHorizontalPosition.get().az());}, mouseHorizontalPosition);
		mouseAltDeg=Bindings.createDoubleBinding(()->{return Angle.toDeg(mouseHorizontalPosition.get().alt());}, mouseHorizontalPosition);
		
		canvas.setOnKeyPressed(key->{
			if(key.getCode()==KeyCode.LEFT) {
				
			}else if(key.getCode()==KeyCode.RIGHT) {
				
			}
		});
		canvas.setOnMousePressed(event->{
			if(event.isPrimaryButtonDown()) {
				canvas.requestFocus();
			}
			event.consume();
		});
		canvas.setOnMouseMoved(event->{
			mousePosition.set(CartesianCoordinates.of(event.getX(),event.getY()));
		});
		canvas.setOnScroll(event->{
			double value = Math.abs(event.getDeltaX())>Math.abs(event.getDeltaY())?event.getDeltaX():event.getDeltaY();
		});
		objectUnderMouse.addListener((event)->{
			System.out.println(objectUnderMouse.get());
		});
		

	}


}

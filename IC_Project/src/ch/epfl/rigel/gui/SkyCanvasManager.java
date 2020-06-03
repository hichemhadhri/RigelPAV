package ch.epfl.rigel.gui;


import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public class SkyCanvasManager {
	
	private final static RightOpenInterval AZ_INTERVAL = RightOpenInterval.of(0, 360);
	private final static ClosedInterval ALT_INTERVAL = ClosedInterval.of(5, 90);
	private final static ClosedInterval FOV_DEG = ClosedInterval.of(30, 150);
	private final static int MAX_OBJECTS = 10;
	private final static double HORIZONTAL_INC_DEG = 10;
	private final static double VERTICAL_INC_DEG = 5;
	
	private DoubleBinding mouseAzDeg;
	private DoubleBinding mouseAltDeg;
	private ObjectBinding<CelestialObject> objectUnderMouse;

	private ObjectBinding<StereographicProjection> projection;
	private ObjectBinding<Transform> planeToCanvas;
	private ObjectBinding<ObservedSky> observedSky;
	private ObjectProperty<CartesianCoordinates> mousePosition;
	private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;
	// mode property 
    private SimpleBooleanProperty mode; 
	private Canvas canvas;
	private SkyCanvasPainter painter;

	/** SkyCanvasManager constructor
	 * @param catalogue
	 * @param dtBean
	 * @param olBean
	 * @param vpBean
	 */
	public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dtBean, ObserverLocationBean olBean,
			ViewingParametersBean vpBean) {
		
		canvas = new Canvas(600, 800);
		
		mode = new SimpleBooleanProperty(true); //Initialized to true
		
		painter = new SkyCanvasPainter(canvas);
		
		mousePosition = new SimpleObjectProperty<CartesianCoordinates>(CartesianCoordinates.of(0,0));
		
		//Creating bindings
		projection = Bindings.createObjectBinding(() -> {
			return new StereographicProjection(vpBean.getCenter());
		}, vpBean.centerProperty());
		
		planeToCanvas = Bindings.createObjectBinding(() -> {
			double scale = calculateScale(canvas.widthProperty().get(),vpBean.getFieldOfViewDeg(),projection.get());
			return Transform.affine(scale, 0, 0, -scale, canvas.widthProperty().get()/2, canvas.heightProperty().get()/2);
		}, canvas.widthProperty(),canvas.heightProperty(), projection,vpBean.fieldOfViewDegProperty());
		
		observedSky = Bindings.createObjectBinding(() -> {
			return new ObservedSky(dtBean.getZonedDateTime(), olBean.getCoordinates(), projection.get(), catalogue);
		}, olBean.coordinates(), dtBean.dateProperty(), dtBean.timeProperty(), dtBean.zoneProperty(), projection);
		 DoubleBinding newR =Bindings.createDoubleBinding(
	                ()->{
	                    try {
	                        return planeToCanvas.get().inverseDeltaTransform(MAX_OBJECTS,0).magnitude();
	                    }
	                    catch(NonInvertibleTransformException e) {
	                        return 0.0;
	                    }
	                },
	                planeToCanvas);
		objectUnderMouse = Bindings.createObjectBinding(() -> {
			try {
				Point2D coordMouse = planeToCanvas.get().inverseTransform(mousePosition.get().x(),mousePosition.get().y());
				CartesianCoordinates coords = CartesianCoordinates.of(coordMouse.getX(), coordMouse.getY());
				return observedSky.get().ObjectClosestTo(coords, newR.get()).orElse(null);
				
			} catch(NonInvertibleTransformException e) {
				return null;
			}
		}, newR,observedSky, mousePosition, planeToCanvas);
		
		mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
			try {
				Point2D coords = planeToCanvas.get().inverseTransform(mousePosition.get().x(), mousePosition.get().y());
				return projection.get().inverseApply(CartesianCoordinates.of(coords.getX(), coords.getY()));
				
			}catch(NonInvertibleTransformException e) {
				return null;
			}
		}, mousePosition, projection, planeToCanvas);
		
		mouseAzDeg = Bindings.createDoubleBinding(() -> {
			try {
				return Angle.toDeg(mouseHorizontalPosition.get().az());
				
			}catch(NullPointerException e) {
				return 0.0;
			}
		}, mouseHorizontalPosition);
		
		mouseAltDeg = Bindings.createDoubleBinding(() -> {
			try {
				return Angle.toDeg(mouseHorizontalPosition.get().alt());
				
			}catch (NullPointerException e) {
				return 0.0;
			}
		}, mouseHorizontalPosition);

		//Managing user inputs
		canvas.setOnKeyPressed(key -> {
		  if(mode.get()) { // if navigation mode is "clavier" then take inputs into consideration 
			if (key.getCode() == KeyCode.LEFT) {
				vpBean.setCenter(HorizontalCoordinates.ofDeg(AZ_INTERVAL.reduce(vpBean.getCenter().azDeg() - HORIZONTAL_INC_DEG),
						vpBean.getCenter().altDeg()));
			} else if (key.getCode() == KeyCode.RIGHT) {
				vpBean.setCenter(HorizontalCoordinates.ofDeg(AZ_INTERVAL.reduce((vpBean.getCenter().azDeg() + HORIZONTAL_INC_DEG)),
						vpBean.getCenter().altDeg()));
			} else if (key.getCode() == KeyCode.UP) {
				vpBean.setCenter(HorizontalCoordinates.ofDeg(vpBean.getCenter().azDeg(), ALT_INTERVAL.clip(vpBean.getCenter().altDeg() + VERTICAL_INC_DEG)));

			} else if (key.getCode() == KeyCode.DOWN) {
				vpBean.setCenter(HorizontalCoordinates.ofDeg(vpBean.getCenter().azDeg(), ALT_INTERVAL.clip(vpBean.getCenter().altDeg() - VERTICAL_INC_DEG)));
			}
		  }
			key.consume();
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
			vpBean.setFieldOfViewDeg(FOV_DEG.clip(vpBean.getFieldOfViewDeg()+value));
		});
		
		//Redrawing the sky when the properties and links that impact the sky change
		projection.addListener((o)->{
			painter.drawAll(observedSky.get(), planeToCanvas.get(), projection.get());
		});
		
		planeToCanvas.addListener((o)->{
			
			painter.drawAll(observedSky.get(), planeToCanvas.get(), projection.get());
		});
		
		observedSky.addListener((o)->{
            
            painter.drawAll(observedSky.get(), planeToCanvas.get(), projection.get());
        });
		
		
	}
	
	
	/**modeProperty getter 
	 * @return modeProperty
	 */
	public SimpleBooleanProperty modeProperty() {
	    return mode; 
	}
	
	/**  Getter for objectUnderMouseProperty
	 * @return objectUnderMouseProperty
	 */
	public ObjectBinding<CelestialObject> objectUnderMouseProperty(){
		return objectUnderMouse;
	}
	
	/**Object Under mouse getter
	 * @return objectUnderMouse
	 */
	public CelestialObject getObjectUnderMouse() {
	    return objectUnderMouse.get(); 
	}
	/** Getter for the canvas 
	 * @return canvas
	 */
	public Canvas canvas() {
		return canvas;
	}
	/** Getter for mouseAzDegProperty
	 * @return mouseAzDegProperty
	 */
	public DoubleBinding mouseAzDegProperty() {
		return mouseAzDeg;
	}
	/** Getter for mouseAltDegProperty
	 * @return mouseAltDegProperty
	 */
	public DoubleBinding mouseAltDegProperty() {
		return mouseAltDeg;
	}
	
	/**
     * MouseAzDeg getter
     * 
     * @return mouseAzDeg
     */
    public double getMouseAzDeg() {
        return mouseAzDeg.get();
    }

    /**
     * MouseAltDeg getter
     * 
     * @return mouseAltDeg
     */
    public double getMouseAltDeg() {
        return mouseAltDeg.get();
    }


	private double calculateScale(double width, double fieldOfviewDeg, StereographicProjection projection) {
		return width / (projection.applyToAngle(Angle.ofDeg(fieldOfviewDeg)));
	}

}

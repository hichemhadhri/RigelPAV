package ch.epfl.rigel.gui;




import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

public class SkyCanvasPainter {
	private final static Color BACKGROUND_COLOR = Color.BLACK;
	private final static Color ASTERISM_COLOR = Color.BLUE;
	private final static Color PLANET_COLOR = Color.LIGHTGRAY;
	private final static Color MOON_COLOR = Color.WHITE;
	private final static Color HORIZON_COLOR = Color.RED;
	private final static Color SUN_OUTER_COLOR = Color.YELLOW.deriveColor(0, 1, 1, 0.25);
	private final static Color SUN_MID_COLOR = Color.YELLOW;
	private final static Color SUN_INNER_COLOR = Color.WHITE;
	private Canvas canvas;
	private GraphicsContext ctx ;
	
	public SkyCanvasPainter(Canvas canvas) {
		this.canvas=canvas;
		this.ctx = canvas.getGraphicsContext2D();
	}
	public void clear() {
		ctx.setFill(BACKGROUND_COLOR);
		ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	public void drawStars(ObservedSky obs, StereographicProjection projection, Transform transform ) {
		double[] centers = new double[obs.starsPositions().length]; 
		double diameter;
		transform.transform2DPoints(obs.starsPositions(), 0, centers, 0, centers.length/2);
		//Drawing Asterism
		ctx.setStroke(ASTERISM_COLOR);
		int indexStart;//The indexes of the star in the obs.stars list (to map the coordinates)
		int indexEnd;
		for(Asterism asterism: obs.asterisms()) {
			ctx.beginPath();
			
			for(int i =0;i<asterism.stars().size()-1;++i) {
				indexStart=obs.stars().indexOf(asterism.stars().get(i));
				indexEnd=obs.stars().indexOf(asterism.stars().get(i+1));
				ctx.moveTo(centers[2*indexStart], centers[2*indexStart+1]);
				if(canvas.getBoundsInLocal().contains(new Point2D(centers[2*indexStart],centers[2*indexStart+1]))|| canvas.getBoundsInLocal().contains(new Point2D(centers[2*indexEnd],centers[2*indexEnd+1])))
					ctx.lineTo(centers[2*indexEnd],centers[2*indexEnd+1]);
			}
			ctx.stroke();
			ctx.closePath();
		}
		
		//Drawing the stars
		for(int i =0 ; i<centers.length/2;++i) {
			ctx.setFill(BlackBodyColor.colorForTemperature(obs.stars().get(i).colorTemperature()));
			diameter=getDiameter(obs.stars().get(i))*transform.getMxx();
			ctx.fillOval(centers[2*i], centers[2*i+1], diameter, diameter);
		}
	}
	public void drawPlanets(ObservedSky obs, Transform transform ) {

		double[] centers = new double[obs.planetsPositions().length]; 
		double diameter;
		transform.transform2DPoints(obs.planetsPositions(), 0, centers, 0, centers.length/2);
		ctx.setFill(PLANET_COLOR);
		for(int i =0 ; i<centers.length/2;++i) {
			diameter=getDiameter(obs.stars().get(i))*transform.getMxx();
			ctx.fillOval(2*i, 2*i+1, diameter, diameter);
		}
		
	}
	public void drawSun(ObservedSky obs, Transform transform ) {
		Point2D center = transform.transform(obs.sunPosition().x(),obs.sunPosition().x());
		//CHECK
		double diameter = getDiameter(obs.sun())*transform.getMxx();
		ctx.setFill(SUN_OUTER_COLOR);
		ctx.fillOval(center.getX(),center.getY(),diameter*2.2,diameter*2.2);
		ctx.setFill(SUN_MID_COLOR);
		ctx.fillOval(center.getX(),center.getY(),diameter+2,diameter+2);
		ctx.setFill(SUN_INNER_COLOR);
		ctx.fillOval(center.getX(),center.getY(),diameter,diameter);
	}
	public void drawMoon(ObservedSky obs, Transform transform ) {
		Point2D center = transform.transform(obs.moonPosition().x(),obs.moonPosition().x());
		double diameter = getDiameter(obs.moon())*transform.getMxx();
		ctx.setFill(MOON_COLOR);
		ctx.fillOval(center.getX(),center.getY(),diameter,diameter);
	}
	public void drawHorizon(ObservedSky obs, StereographicProjection projection, Transform transform ) {
		
	}
	private double getDiameter(CelestialObject obj) {
		double mp = Math.min(5, Math.max(-2, obj.magnitude()));
		double f = (99-17*mp)/140;
		return f* 2* Math.tan(Angle.ofDeg(0.5/4));
	}
}

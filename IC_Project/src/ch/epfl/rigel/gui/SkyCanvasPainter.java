package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public class SkyCanvasPainter {
	private final static Color BACKGROUND_COLOR = Color.BLACK;
	private final static Color PLANET_COLOR = Color.LIGHTGRAY;
	private final static Color SUN_MID_COLOR = Color.YELLOW;
	private final static Color ASTERISM_COLOR = Color.BLUE;
	private final static Color MOON_COLOR = Color.WHITE;
	private final static Color HORIZON_COLOR = Color.RED;
	private final static Color SUN_INNER_COLOR = Color.WHITE;
	private final static Color SUN_OUTER_COLOR = Color.YELLOW.deriveColor(0, 1, 1, 0.25);
	private Canvas canvas;
	private GraphicsContext ctx;

	/**
	 * Sky Canvas Painter constructor
	 * 
	 * @param canvas
	 */
	public SkyCanvasPainter(Canvas canvas) {
		this.canvas = canvas;
		this.ctx = canvas.getGraphicsContext2D();
	}

	/**
	 * Clear the canvas
	 */
	public void clear() {
		ctx.setFill(BACKGROUND_COLOR);
		ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	/**
	 * Draw the stars with their asterisms
	 * 
	 * @param obs
	 * @param projection
	 * @param transform
	 */
	public void drawStars(ObservedSky obs, StereographicProjection projection, Transform transform) {
		double[] centers = new double[obs.starsPositions().length];
		double diameter;
		transform.transform2DPoints(obs.starsPositions(), 0, centers, 0, centers.length / 2);
		// Drawing Asterism
		ctx.setStroke(ASTERISM_COLOR);

		ctx.setLineWidth(1);
		int indexStart;// The indexes of the star in the obs.stars list (to map the coordinates)
		int indexEnd;
		for (Asterism asterism : obs.asterisms()) {
			ctx.beginPath();

			for (int i = 0; i < asterism.stars().size() - 1; ++i) {
				indexStart = obs.stars().indexOf(asterism.stars().get(i));
				indexEnd = obs.stars().indexOf(asterism.stars().get(i + 1));
				ctx.moveTo(centers[2 * indexStart], centers[2 * indexStart + 1]);
				if (canvas.getBoundsInLocal()
						.contains(new Point2D(centers[2 * indexStart], centers[2 * indexStart + 1]))
						|| canvas.getBoundsInLocal()
								.contains(new Point2D(centers[2 * indexEnd], centers[2 * indexEnd + 1])))
					ctx.lineTo(centers[2 * indexEnd], centers[2 * indexEnd + 1]);
			}
			ctx.stroke();
			ctx.closePath();
		}

		// Drawing the stars
		for (int i = 0; i < centers.length / 2; ++i) {
			ctx.setFill(BlackBodyColor.colorForTemperature(obs.stars().get(i).colorTemperature()));
			diameter = getDiameter(obs.stars().get(i)) * transform.getMxx();
			ctx.fillOval(getCorrCoord(centers[2 * i], diameter), getCorrCoord(centers[2 * i + 1], diameter), diameter,
					diameter);
		}
	}

	/**
	 * Draw the planets
	 * 
	 * @param obs
	 * @param transform
	 */
	public void drawPlanets(ObservedSky obs, Transform transform) {

		double[] centers = new double[obs.planetsPositions().length];
		double diameter;
		transform.transform2DPoints(obs.planetsPositions(), 0, centers, 0, centers.length / 2);
		ctx.setFill(PLANET_COLOR);
		for (int i = 0; i < centers.length / 2; ++i) {
			diameter = getDiameter(obs.planets().get(i)) * transform.getMxx();
			ctx.fillOval(getCorrCoord(centers[2 * i], diameter), getCorrCoord(centers[2 * i + 1], diameter), diameter,
					diameter);
		}
	}

	/**
	 * Draw the sun
	 * 
	 * @param obs
	 * @param transform
	 */
	public void drawSun(ObservedSky obs, Transform transform) {
		Point2D center = transform.transform(obs.sunPosition().x(), obs.sunPosition().y());
		double diameter = 2 * Math.tan(obs.sun().angularSize() / 4) * transform.getMxx();
		ctx.setFill(SUN_OUTER_COLOR);
		ctx.fillOval(getCorrCoord(center.getX(), diameter * 2.2), getCorrCoord(center.getY(), diameter * 2.2),
				diameter * 2.2, diameter * 2.2);
		ctx.setFill(SUN_MID_COLOR);
		ctx.fillOval(getCorrCoord(center.getX(), diameter + 2), getCorrCoord(center.getY(), diameter + 2), diameter + 2,
				diameter + 2);
		ctx.setFill(SUN_INNER_COLOR);
		ctx.fillOval(getCorrCoord(center.getX(), diameter), getCorrCoord(center.getY(), diameter), diameter, diameter);
	}

	/**
	 * Draw the moon
	 * 
	 * @param obs
	 * @param transform
	 */
	public void drawMoon(ObservedSky obs, Transform transform) {
		Point2D center = transform.transform(obs.moonPosition().x(), obs.moonPosition().y());
		double diameter = 2 * Math.tan(obs.moon().angularSize() / 4) * transform.getMxx();
		ctx.setFill(MOON_COLOR);
		ctx.fillOval(getCorrCoord(center.getX(), diameter), getCorrCoord(center.getY(), diameter), diameter, diameter);
	}

	/**
	 * Draw the horizon, with octants
	 * 
	 * @param obs
	 * @param projection
	 * @param transform
	 */
	public void drawHorizon(ObservedSky obs, StereographicProjection projection, Transform transform) {

		ctx.setStroke(HORIZON_COLOR);
		ctx.setLineWidth(2);
		// Drawing the horizon
		HorizontalCoordinates parallel = HorizontalCoordinates.of(0, 0);
		CartesianCoordinates centerCart = projection.circleCenterForParallel(parallel);
		Point2D center = transform.transform(centerCart.x(), centerCart.y());
		double diameter = 2 * projection.circleRadiusForParallel(parallel) * transform.getMxx();

		ctx.strokeOval(getCorrCoord(center.getX(), diameter), getCorrCoord(center.getY(), diameter), diameter,
				diameter);
		// Drawing the text
		ctx.setTextBaseline(VPos.TOP);

		ctx.setTextAlign(TextAlignment.CENTER);
		ctx.setFill(HORIZON_COLOR);
		HorizontalCoordinates textHorCoord;
		CartesianCoordinates centerTextCartCoord;
		Point2D centerText;
		for (int i = 0; i < 360; i += 45) {
			textHorCoord = HorizontalCoordinates.of(Angle.ofDeg(i), Angle.ofDeg(-0.5));
			centerTextCartCoord = projection.apply(textHorCoord);
			centerText = transform.transform(centerTextCartCoord.x(), centerTextCartCoord.y());
			ctx.fillText(textHorCoord.azOctantName("N", "E", "S", "O"), centerText.getX(), centerText.getY());
		}

	}

	/**
	 * Draw the celestial objects, with the following order: Stars, Planets, Sun,
	 * Moon, Horizon
	 * 
	 * @param sky
	 * @param planeToCanvas
	 * @param projection
	 */
	public void drawAll(ObservedSky sky, Transform planeToCanvas, StereographicProjection projection) {
		clear();
		drawStars(sky, projection, planeToCanvas);
		drawPlanets(sky, planeToCanvas);
		drawSun(sky, planeToCanvas);
		drawMoon(sky, planeToCanvas);
		drawHorizon(sky, projection, planeToCanvas);

	}

	private double getDiameter(CelestialObject obj) {
		double mp = Math.min(5, Math.max(-2, obj.magnitude()));
		double f = (99 - 17 * mp) / 140;
		return f * 2 * Math.tan(Angle.ofDeg(0.5) / 4);
	}

	private double getCorrCoord(double coord, double diameter) {
		return coord - diameter / 2;
	}
}

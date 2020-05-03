package GUI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.ZonedDateTime;

import javax.imageio.ImageIO;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.gui.SkyCanvasPainter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public final class DrawSky extends Application {
  public static void main(String[] args) { launch(args); }

  private InputStream resourceStream(String resourceName) {
    return getClass().getResourceAsStream(resourceName);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    try (InputStream hs = resourceStream("/hygdata_v3.csv")){
    
      StarCatalogue catalogue = new StarCatalogue.Builder()
	.loadFrom(hs, HygDatabaseLoader.INSTANCE)//.loadFrom(resourceStream("/asterisms.txt"), AsterismLoader.INSTANCE)
	.build();

      ZonedDateTime when =
	ZonedDateTime.parse("2020-02-17T20:15:00+01:00");
      GeographicCoordinates where =
	GeographicCoordinates.ofDeg(6.57, 46.52);
      
     //HorizontalCoordinates projCenter = HorizontalCoordinates.ofDeg(0, 90);//transform
      //HorizontalCoordinates projCenter = HorizontalCoordinates.ofDeg(277, -23);
     // HorizontalCoordinates projCenter = HorizontalCoordinates.ofDeg(0, 23);//Sun
      HorizontalCoordinates projCenter =HorizontalCoordinates.ofDeg(180, 45);
     // HorizontalCoordinates projCenter = HorizontalCoordinates.ofDeg(3.7, -65);//lune
      StereographicProjection projection =
	new StereographicProjection(projCenter);
      ObservedSky sky =
	new ObservedSky(when, where, projection, catalogue);
   // Transform planeToCanvas = Transform.affine(260, 0, 0, -260, 400, 300);//other transform
      Canvas canvas =
	new Canvas(800, 600);
      Transform planeToCanvas =Transform.affine(1300, 0, 0, -1300, 400, 300); //prof transfrom
      SkyCanvasPainter painter =
	new SkyCanvasPainter(canvas);

      painter.drawAll(sky, planeToCanvas, projection);

      WritableImage fxImage =
	canvas.snapshot(null, null);
      BufferedImage swingImage =
	SwingFXUtils.fromFXImage(fxImage, null);
      ImageIO.write(swingImage, "png", new File("sky.png"));
    }
    Platform.exit();
  }
}
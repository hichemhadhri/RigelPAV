package ch.epfl.rigel.gui;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.UnaryOperator;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.bonus.UDPServer;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

/**
 * Main class
 * 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try (InputStream hs = resourceStream("/hygdata_v3.csv")) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(resourceStream("/asterisms.txt"),
                            AsterismLoader.INSTANCE)
                    .build();
            hs.close();

            ZonedDateTime when = ZonedDateTime
                    .parse("2020-02-17T20:15:00+01:00");
            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(when);

            ObserverLocationBean observerLocationBean = new ObserverLocationBean();
            observerLocationBean
                    .setCoordinates(GeographicCoordinates.ofDeg(6.57, 46.52));

            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            viewingParametersBean.setCenter(
                    HorizontalCoordinates.ofDeg(180.000000000001, 15));
            viewingParametersBean.setFieldOfViewDeg(100);
            UDPServer server = null;
            try {
            	server=new UDPServer(2900,viewingParametersBean);
            	server.setDaemon(true);
            	server.start();
            } catch (SocketException e) {
            	e.printStackTrace();
            }

            SkyCanvasManager canvasManager = new SkyCanvasManager(catalogue,
                    dateTimeBean, observerLocationBean, viewingParametersBean);

            TimeAnimator ta = new TimeAnimator(dateTimeBean);
            ta.setAccelerator(NamedTimeAccelerator.TIMES_3000.getAccelerator());

            Canvas sky = canvasManager.canvas();
            Pane ciel = new Pane(sky);
            BorderPane root = new BorderPane();
            root.setTop(drawControlBar(observerLocationBean, dateTimeBean, ta));
            root.setCenter(ciel);
            root.setBottom(drawInfos(viewingParametersBean, canvasManager));

            sky.widthProperty().bind(ciel.widthProperty());
            sky.heightProperty().bind(ciel.heightProperty());

            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.setTitle("Rigel");

            primaryStage.setScene(new Scene(root));
            primaryStage.show();

            ciel.requestFocus();

        }

    }

    private HBox drawControlBar(ObserverLocationBean obs, DateTimeBean db,
            TimeAnimator ta) throws IOException {

        // creating the hbox postion
        Label longitude = new Label("Longitude (°) :");
        Label latitude = new Label("Latitude (°) :");

        TextField lonTextField = new TextField();

        TextFormatter<Number> lonTF = createTextFormatter(true);
        lonTextField.setTextFormatter(lonTF);
        lonTextField
                .setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        lonTF.valueProperty().bindBidirectional(obs.lonDeg());
        TextField latTextField = new TextField();
        TextFormatter<Number> latTF = createTextFormatter(false);
        latTextField.setTextFormatter(latTF);
        latTextField
                .setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        latTF.valueProperty().bindBidirectional(obs.latDeg());

        HBox position = new HBox(longitude, lonTextField, latitude,
                latTextField);
        position.setStyle(
                "-fx-spacing: inherit;\n" + "-fx-alignment: baseline-left;");

        // creating the time hbox
        Label date = new Label("Date :");

        DatePicker dp = new DatePicker();
        dp.setStyle("-fx-pref-width: 120;");
        dp.valueProperty().bindBidirectional(db.dateProperty());
        dp.setFocusTraversable(false);

        Label hour = new Label("Heure :");

        TextField hourField = new TextField();
        hourField.setStyle(
                "-fx-pref-width: 75;" + "-fx-alignment: baseline-right;");
        hourField.setFocusTraversable(false);

        DateTimeFormatter hmsFormatter = DateTimeFormatter
                .ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(
                hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<LocalTime>(
                stringConverter);
        hourField.setTextFormatter(timeFormatter);
        timeFormatter.valueProperty().bindBidirectional(db.timeProperty());

        ObservableList<String> list = FXCollections
                .observableArrayList(ZoneId.getAvailableZoneIds());
        list.sort(null);

        ObservableList<ZoneId> zoneList = FXCollections.observableArrayList();
        for (String nom : list) {
            zoneList.add(ZoneId.of(nom));
        }
        ComboBox<ZoneId> zone = new ComboBox<ZoneId>(zoneList);

        zone.setStyle("-fx-pref-width: 180");
        zone.valueProperty().bindBidirectional(db.zoneProperty());
        zone.setFocusTraversable(false);
        HBox time = new HBox(date, dp, hour, hourField, zone);
        time.setStyle(
                "-fx-spacing: inherit;\n" + "-fx-alignment: baseline-left;");
        time.disableProperty().bind(ta.runningProperty());

        // creating animation hbox

        ChoiceBox<NamedTimeAccelerator> cb = new ChoiceBox<>();
        cb.setItems(FXCollections
                .observableList(Arrays.asList(NamedTimeAccelerator.values())));
        cb.setValue(NamedTimeAccelerator.TIMES_300);
        ta.acceleratorProperty()
                .bind(Bindings.select(cb.valueProperty(), "accelerator"));
        cb.disableProperty().bind(ta.runningProperty());
        String playString = "\uf04b";
        String pauseString = "\uf04c";
        Button resetButton, playButton;
        try (InputStream fontStream = getClass()
                .getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf");) {
            Font fontAwesome = Font.loadFont(fontStream, 15);

            resetButton = new Button("\uf0e2");
            resetButton.setFont(fontAwesome);
            resetButton.disableProperty().bind(ta.runningProperty());
            playButton = new Button(playString);
            playButton.setFont(fontAwesome);
            fontStream.close();
        }

        playButton.setOnMouseClicked((o) -> {
            if (playButton.getText() == playString) {
                ta.start();
                playButton.setText(pauseString);
            } else {
                ta.stop();
                playButton.setText(playString);
            }

        });
        resetButton.setOnMouseClicked((o) -> {
            ta.stop();
            ta.getDateTimeBean().setZonedDateTime(ZonedDateTime.now());
            ;
            playButton.setText(playString);
        });

        HBox animation = new HBox(cb, resetButton, playButton);
        animation.setStyle("-fx-spacing: inherit;");

        // creating the whole control bar

        HBox controlBar = new HBox(position, time, animation);
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        return controlBar;

    }

    private BorderPane drawInfos(ViewingParametersBean vp,
            SkyCanvasManager sm) {

        BorderPane infos = new BorderPane();
        infos.setStyle("-fx-padding: 4;-fx-background-color: white;");

        Text gauche = new Text();
        gauche.textProperty().bind(Bindings.format(Locale.ROOT,
                "Champ de vue : %.1f°", vp.fieldOfViewDeg()));

        Text center = new Text();
        center.textProperty().bind(sm.objectUnderMouseProperty().asString());

        Text droite = new Text();
        droite.textProperty()
                .bind(Bindings.format(Locale.ROOT,
                        "Azimut : %.2f°, hauteur : %.2f°",
                        sm.mouseAzDegProperty(), sm.mouseAltDegProperty()));

        infos.setRight(droite);
        infos.setLeft(gauche);
        infos.setCenter(center);
        return infos;
    }

    private TextFormatter<Number> createTextFormatter(boolean bool) {
        NumberStringConverter stringConverter = new NumberStringConverter(
                "#0.00");

        UnaryOperator<TextFormatter.Change> Filter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newDeg = stringConverter.fromString(newText)
                        .doubleValue();
                boolean choice = bool
                        ? GeographicCoordinates.isValidLonDeg(newDeg)
                        : GeographicCoordinates.isValidLatDeg(newDeg);
                return choice ? change : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> TextFormatterr = new TextFormatter<>(
                stringConverter, 0.0, Filter);
        return TextFormatterr;
    }

}

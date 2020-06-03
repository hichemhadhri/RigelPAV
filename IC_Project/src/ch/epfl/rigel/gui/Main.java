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
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    
  private   ZonedDateTime when;
  private   DateTimeBean dateTimeBean;
  private   ObserverLocationBean observerLocationBean ;
  private   ViewingParametersBean viewingParametersBean;
  private   SkyCanvasManager canvasManager;
  private   TimeAnimator ta;
  private   Canvas sky;
  private   UDPServer server ;

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

         
             dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(ZonedDateTime.now());

            observerLocationBean = new ObserverLocationBean();
            observerLocationBean
                    .setCoordinates(GeographicCoordinates.ofDeg(6.57, 46.52));

             viewingParametersBean = new ViewingParametersBean();
            viewingParametersBean.setCenter(
                    HorizontalCoordinates.ofDeg(180.000000000001, 15));
            viewingParametersBean.setFieldOfViewDeg(100);
          
            try {
            	server=new UDPServer(2900,viewingParametersBean,observerLocationBean);
            	server.setDaemon(true);
            	server.start();
            } catch (SocketException e) {
            	e.printStackTrace();
            }

             canvasManager = new SkyCanvasManager(catalogue,
                    dateTimeBean, observerLocationBean, viewingParametersBean);

             ta = new TimeAnimator(dateTimeBean);
            ta.setAccelerator(NamedTimeAccelerator.TIMES_3000.getAccelerator());

             sky = canvasManager.canvas();
            Pane ciel = new Pane(sky);
            BorderPane root = new BorderPane();
            VBox top = new VBox(drawHelpMenu(),drawControlBar());
            
            root.setTop(top);
            root.setCenter(ciel);
            root.setBottom(drawInfos(viewingParametersBean, canvasManager));
            
            /////////////////////////////////////////////////
       
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

    private HBox drawControlBar() throws IOException {

        // creating the hbox postion
        Label longitude = new Label("Longitude (°) :");
        Label latitude = new Label("Latitude (°) :");

        TextField lonTextField = new TextField();

        TextFormatter<Number> lonTF = createTextFormatter(true);
        lonTextField.setTextFormatter(lonTF);
        lonTextField
                .setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        lonTF.valueProperty().bindBidirectional(observerLocationBean.lonDegProperty());
        TextField latTextField = new TextField();
        TextFormatter<Number> latTF = createTextFormatter(false);
        latTextField.setTextFormatter(latTF);
        latTextField
                .setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        latTF.valueProperty().bindBidirectional(observerLocationBean.latDegProperty());

        HBox position = new HBox(longitude, lonTextField, latitude,
                latTextField);
        position.setStyle(
                "-fx-spacing: inherit;\n" + "-fx-alignment: baseline-left;");

        // creating the time hbox
        Label date = new Label("Date :");

        DatePicker dp = new DatePicker();
        dp.setStyle("-fx-pref-width: 120;");
        dp.valueProperty().bindBidirectional(dateTimeBean.dateProperty());
      
        Label hour = new Label("Heure :");

        TextField hourField = new TextField();
        hourField.setStyle(
                "-fx-pref-width: 75;" + "-fx-alignment: baseline-right;");
        

        DateTimeFormatter hmsFormatter = DateTimeFormatter
                .ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(
                hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<LocalTime>(
                stringConverter);
        hourField.setTextFormatter(timeFormatter);
        timeFormatter.valueProperty().bindBidirectional(dateTimeBean.timeProperty());

        ObservableList<String> list = FXCollections
                .observableArrayList(ZoneId.getAvailableZoneIds());
        list.sort(null);

        ObservableList<ZoneId> zoneList = FXCollections.observableArrayList();
        for (String nom : list) {
            zoneList.add(ZoneId.of(nom));
        }
        ComboBox<ZoneId> zone = new ComboBox<ZoneId>(zoneList);

        zone.setStyle("-fx-pref-width: 180");
        zone.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());
      
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

        HBox controlBar = new HBox(position,new Separator(Orientation.VERTICAL), time, new Separator(Orientation.VERTICAL),animation);
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        return controlBar;

    }
    
    private  MenuBar drawHelpMenu() {
        Menu aide = new Menu("aide");

         Menu subMenu = new Menu("Point And View Setup");
      CustomMenuItem customMenuItem = new CustomMenuItem();
      ScrollPane setup = new ScrollPane();
      VBox setupH = new VBox(); 
      setupH.setSpacing(2);
      Label l1 = new Label("1.Installez l'application Rigel PAV : "); 
      l1.setStyle("-fx-font-weight: bold;");
      
      Label l11 = new Label("scannez ce QR Code "); 
      l11.setStyle("-fx-font-weight: normal;"); 
      
      Label l2 = new Label ("2.Entrez l'adresse IP de votre PC ainsi que le port  : ");
      l2.setStyle("-fx-font-weight: bold;"); 
      Label l21 = new Label("L'adresse IP de votre PC et le port sont indiqués en bas à droite de la fenêtre "); 
      l21.setStyle("-fx-font-weight: normal");
      
      InputStream input = resourceStream("/tuto2.jpg");
      Image image = new Image(input,389,200,true,true); 
      ImageView tuto2 = new ImageView(image);
      HBox imCon = new HBox(tuto2); 
      imCon.setAlignment(Pos.BASELINE_CENTER);
      
      //
      Label l3 =new Label("3.Checkez synchroniser pour envoyer les données de votre sensor et Coords géo pour envoyer vos coordonnées au PC");
      l3.setStyle("-fx-font-weight: bold;");
      Label l31 = new Label("vous devez voir le status basculer de non connecté à connecté dans les deux applications");
 
      Image image2 = new Image(input,389,200,true,true); 
      ImageView tuto3 = new ImageView(image2);
      HBox imCon2 = new HBox(tuto3); 
      imCon2.setAlignment(Pos.BASELINE_CENTER);
      Label l4 = new Label("4.pointez votre telephone vers le ciel et vous verrez le ciel de l'application mis à jour ");
      l4.setStyle("-fx-font-weight: bold;");
      Label notice = new Label("NB: l'application est disponible seulement sur Android , et vous devez avoir l'orientation sensor activé");
      notice.setStyle("-fx-text-decoration: underline;");
      
      setupH.getChildren().addAll(l1,l11,l2,l21,imCon,l3,l31,imCon2,l4,notice); 
     
      setup.setContent(setupH);
    
      customMenuItem.setContent(setup);
      
      customMenuItem.setHideOnClick(false);
      subMenu.getItems().add(customMenuItem);
      aide.getItems().add(subMenu); 
      
        MenuBar menuBar = new MenuBar(aide);

   
        
        
        return menuBar ; 
    }

    private BorderPane drawInfos(ViewingParametersBean vp,
            SkyCanvasManager sm) {

        BorderPane infos = new BorderPane();
        infos.setStyle("-fx-padding: 4;-fx-background-color: white;");

        Text gauche1 = new Text();
        gauche1.textProperty().bind(Bindings.format(Locale.ROOT,
                "Champ de vue : %.1f°", vp.fieldOfViewDegProperty()));

        Text center = new Text();
        center.textProperty().bind(sm.objectUnderMouseProperty().asString());

        Text gauche2 = new Text();
        gauche2.textProperty()
                .bind(Bindings.format(Locale.ROOT,
                        "Azimut : %.2f°, hauteur : %.2f°",
                        sm.mouseAzDegProperty(), sm.mouseAltDegProperty()));

        HBox gauche = new HBox(gauche1,new Separator(Orientation.VERTICAL),gauche2); 
        gauche.setSpacing(2); 
        Text droite4 = new Text("statut : ");
        Text droite1 = new Text("port : " + server.getPort() );
        Text droite2 = new Text("Adresse IP : " + server.getIp());
        Text droite3 = new Text(); 
        droite3.textProperty().bind(Bindings.format(Locale.ROOT, "%s",server.statusProperty()));
        if(droite3.getText()=="connecté")
            droite3.setFill(Color.GREEN);
        else 
            droite3.setFill(Color.RED);
        
        droite3.setStyle("-fx-font-weight: bold;");
        HBox droite = new HBox(droite1,new Separator(Orientation.VERTICAL),droite2,new Separator(Orientation.VERTICAL),droite4,droite3); 
        droite.setSpacing(2); 
        gauche.setSpacing(2);
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

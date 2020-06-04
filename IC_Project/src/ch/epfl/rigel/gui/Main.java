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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
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
            	server=new UDPServer(3000,viewingParametersBean,observerLocationBean);
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
            root.setBottom(drawInfos());
            
          
       
            sky.widthProperty().bind(ciel.widthProperty());
            sky.heightProperty().bind(ciel.heightProperty());

            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.setTitle("Rigel");
     Scene scene =new Scene(root); 
     
     scene.getStylesheets().add("/style.css") ;
            primaryStage.setScene(scene);
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
    
    private  MenuBar drawHelpMenu() throws IOException{
      Menu aide = new Menu("Aide");
      
      Menu subMenu = new Menu("Point And View Setup");
      CustomMenuItem customMenuItem = new CustomMenuItem();
      ScrollPane setup = new ScrollPane();
      VBox setupH = new VBox(); 
      setupH.setSpacing(2);
      Label l1 = new Label("1. Installez l'application Rigel PAV en Scannant ce QR code: "); 
      l1.setStyle("-fx-font-weight: bold;");
      
      Label l11 = new Label(" NB: l'application n'est disponible que sur Android , et vous devez avoir le capteur d'orientation et la géolocalisation activés"); 
      l11.setStyle("-fx-font-weight: normal;"); 
     InputStream input = resourceStream("/qrcode.png");
     Image image = new Image(input,229,217,false,false); 
     ImageView tuto1 = new ImageView(image);
     HBox imCon = new HBox(tuto1); 
     imCon.setAlignment(Pos.BASELINE_CENTER);
     input.close();
      Label l0 = new Label("2. Assurez vous que le smartphone et l'ordinateur sont connectés sur le même réseau WIFI \n(Le partage de connexion est aussi possible)");
      l0.setStyle("-fx-font-weight: bold;");
      
      Label l2 = new Label ("3. Entrez l'adresse IP de votre ordinateur ainsi que le port dans la fenêtre de l'application sur smartphone: ");
      l2.setStyle("-fx-font-weight: bold;"); 
      Label l21 = new Label("L'adresse IP de votre ordinateur et le port sont indiqués en bas à droite de la fenêtre "); 
      l21.setStyle("-fx-font-weight: normal");
      
       input = resourceStream("/tuto1.png");
      Image image2 = new Image(input,375,200,false,false); 
      ImageView tuto2 = new ImageView(image2);
      HBox imCon1 = new HBox(tuto2); 
      imCon1.setAlignment(Pos.BASELINE_CENTER);
      input.close();
     
      Label l3 =new Label("4. Checkez synchroniser pour envoyer les données des capteurs \n Une fois que les données du GPS sont disponibles, vous pouvez cliquer sur Envoyer Géolocalisation pour envoyer vos coordonnées géographiques au PC");
      l3.setStyle("-fx-font-weight: bold;");
      Label l31 = new Label("Vous devriez voir le statut basculer de Non connecté à Connecté dans les deux applications");
        
      input = resourceStream("/tuto2.png");
      Image image3 = new Image(input,389,200,false,false); 
      ImageView tuto3 = new ImageView(image3);
      HBox imCon2 = new HBox(tuto3); 
      imCon2.setAlignment(Pos.BASELINE_CENTER);
      input.close(); 
      
      Label l4 = new Label("5. Dans le menu du Mode de Navigation, sélectionnez le mode Smartphone");
      l4.setStyle("-fx-font-weight: bold;");
      
      Label l5 = new Label("6. Pointez votre smartphone vers le ciel et vous devriez voir le ciel du programme se mettre à jour en temps réel");
      l5.setStyle("-fx-font-weight: bold;");
      setupH.getChildren().addAll(l1,l11,imCon,l0,l2,imCon1,l21,l3,l31,imCon2,l4,l5); 
     setupH.setPrefHeight(400);
     setupH.setPrefWidth(700);
     setupH.getStyleClass().add("custom-menu");
      setup.setContent(setupH);
       setup.setPrefHeight(400);
       setup.setPrefWidth(700);
       setup.getStyleClass().add("custom-menu");
      customMenuItem.setContent(setup);
      customMenuItem.getStyleClass().add("custom-menu");
      customMenuItem.setHideOnClick(false);
      
      subMenu.getItems().add(customMenuItem);
      
      aide.getItems().add(subMenu); 
      
      Menu navig = new Menu("Mode de Navigation"); 
      CustomMenuItem navigItem = new CustomMenuItem();
      RadioButton clavier= new RadioButton("Clavier");
      clavier.setTextFill(Color.BLACK);
    
     
      clavier.setStyle("-fx-focus-color: transparent;");
      RadioButton telephone = new RadioButton("Smartphone"); 
      telephone.setTextFill(Color.BLACK);
      telephone.setStyle("-fx-focus-color: transparent;");
      ToggleGroup tg = new ToggleGroup(); 
      clavier.setToggleGroup(tg);
      telephone.setToggleGroup(tg);
      tg.selectToggle(clavier);
      //binding with udp server and skycanvasmanger mode properties
      clavier.selectedProperty().bindBidirectional(canvasManager.modeProperty());
      telephone.selectedProperty().bindBidirectional(server.modeProperty());
      
      VBox rCon = new VBox(clavier,telephone); 
      navigItem.setContent(rCon);
      navigItem.setHideOnClick(false);
      
      navig.getItems().add(navigItem);
      
        MenuBar menuBar = new MenuBar(aide,navig);
          
   
        
        
        return menuBar ; 
    }

    private BorderPane drawInfos() {

        BorderPane infos = new BorderPane();
        infos.setStyle("-fx-padding: 4;-fx-background-color: white;");

        Text gauche1 = new Text();
        gauche1.textProperty().bind(Bindings.format(Locale.ROOT,
                "Champ de vue : %.1f°", viewingParametersBean.fieldOfViewDegProperty()));

        Text center = new Text();
        
        
        canvasManager.objectUnderMouseProperty().addListener(e->{
        if(canvasManager.objectUnderMouseProperty().getValue()!=null) 
            center.setText(canvasManager.objectUnderMouseProperty().get().info());
        else 
            center.setText("");});
      
        Text gauche2 = new Text();
        gauche2.textProperty()
                .bind(Bindings.format(Locale.ROOT,
                        "Azimut : %.2f°, hauteur : %.2f°",
                        canvasManager.mouseAzDegProperty(), canvasManager.mouseAltDegProperty()));

        HBox gauche = new HBox(gauche1,new Separator(Orientation.VERTICAL),gauche2); 
        gauche.setSpacing(2); 
        Text droite4 = new Text("Statut : ");
        Text droite1 = new Text("Port : " + server.getPort() );
        Text droite2 = new Text("Adresse IP : " + server.getIP());
        Text droite3 = new Text();
        
        droite3.textProperty().bind(Bindings.format(Locale.ROOT, "%s",server.statusProperty()));
        droite3.fillProperty().bind(server.colorProperty());
        
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

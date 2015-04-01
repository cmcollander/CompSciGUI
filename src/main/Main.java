package main;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static Map map;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize Map
        map = new Map();
        
        // MenuItem Listener
        EventHandler<ActionEvent> menuAction = menuItemSelected();
        
        // MenuBar setup
        final Menu menu1 = new Menu("File");
        final Menu menu2 = new Menu("Ship");
        final Menu menu3 = new Menu("Port");
        final Menu menu4 = new Menu("Sea Monster");
        final MenuItem menu5 = new MenuItem("About");
        
        // File Menu
        MenuItem menu11 = new MenuItem("Open");
        menu11.setOnAction(menuAction);
        MenuItem menu12 = new MenuItem("Close");
        menu12.setOnAction(menuAction);
        MenuItem menu13 = new MenuItem("Snap Shot");
        menu13.setOnAction(menuAction);
        MenuItem menu14 = new MenuItem("Exit");
        menu14.setOnAction(menuAction);
        menu1.getItems().addAll(menu11, menu12, menu13, menu14);
        
        // Ship Menu
        MenuItem menu21 = new MenuItem("Generate Ships");
        menu21.setOnAction(menuAction);
        MenuItem menu22 = new MenuItem("Update Ships");
        menu22.setOnAction(menuAction);
        MenuItem menu23 = new MenuItem("Display All Ships");
        menu23.setOnAction(menuAction);
        MenuItem menu24 = new MenuItem("Remove All Ships");
        menu24.setOnAction(menuAction);
        menu2.getItems().addAll(menu21, menu22, menu23, menu24);
        
        // Port Menu
        MenuItem menu31 = new MenuItem("Unload Ship");
        menu31.setOnAction(menuAction);
        MenuItem menu32 = new MenuItem("Update Dock");
        menu32.setOnAction(menuAction);
        MenuItem menu33 = new MenuItem("Display All Docks");
        menu33.setOnAction(menuAction);
        MenuItem menu34 = new MenuItem("Display All Cargos");
        menu34.setOnAction(menuAction);
        menu3.getItems().addAll(menu31, menu32, menu33, menu34);
        
        // Monster Menu
        MenuItem menu41 = new MenuItem("Generate Monsters");
        menu41.setOnAction(menuAction);
        MenuItem menu42 = new MenuItem("Update Monsters");
        menu42.setOnAction(menuAction);
        MenuItem menu43 = new MenuItem("Display All Monsters");
        menu43.setOnAction(menuAction);
        MenuItem menu44 = new MenuItem("Remove All Monsters");
        menu44.setOnAction(menuAction);
        MenuItem menu45 = new MenuItem("Summon Godzilla");
        menu45.setOnAction(menuAction);
        menu4.getItems().addAll(menu41, menu42, menu43, menu44, menu45);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2, menu3, menu4);
        //TODO: Find a way to add a MenuItem to menuBar without a Menu in between, for the About button
        
        // MapView Setup
        ImageView mapView = new ImageView();
        Image image = new Image("image.png");
        mapView.setImage(image);
        
        // TextLabel Setup
        Label textLabel = new Label("Woof!");
        
        // Create Scene
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mapView);
        root.setBottom(textLabel);
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setTitle("CompSci GUI - JavaFX!");
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private EventHandler<ActionEvent> menuItemSelected() {
        return new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                MenuItem mItem = (MenuItem) event.getSource();
                String text = mItem.getText();
                
                // File Menu
                // Open MenuItem
                if("Open".equalsIgnoreCase(text)) {
                    TextInputDialog openDialog = new TextInputDialog("complex");
                    openDialog.setTitle("Open File");
                    openDialog.setHeaderText("Open File");
                    openDialog.setContentText("Please enter the tag for your file:");
                    
                    Optional<String> result = openDialog.showAndWait();
                    if(result.isPresent()) {
                        try {
                            map = FileHandler.getMapFile(result.get()+".map.txt");
                            map.setPort(FileHandler.getPortFile(result.get()+".port.txt"));
                        } catch(Exception ex) {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Exception");
                            alert.setHeaderText("Exception Stacktrace");
                            alert.setContentText("Could not find file");

                            // Create expandable Exception.
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            ex.printStackTrace(pw);
                            String exceptionText = sw.toString();

                            Label label = new Label("The exception stacktrace was:");

                            TextArea textArea = new TextArea(exceptionText);
                            textArea.setEditable(false);
                            textArea.setWrapText(true);

                            textArea.setMaxWidth(Double.MAX_VALUE);
                            textArea.setMaxHeight(Double.MAX_VALUE);
                            GridPane.setVgrow(textArea, Priority.ALWAYS);
                            GridPane.setHgrow(textArea, Priority.ALWAYS);

                            GridPane expContent = new GridPane();
                            expContent.setMaxWidth(Double.MAX_VALUE);
                            expContent.add(label, 0, 0);
                            expContent.add(textArea, 0, 1);

                            // Set expandable Exception into the dialog pane.
                            alert.getDialogPane().setExpandableContent(expContent);

                            alert.showAndWait();
                        }
                    }
                }
                // Exit MenuItem
                if("Exit".equalsIgnoreCase(text)) {
                   System.exit(0); 
                }
                // Place other MenuItems here
            }
        };
    }
}

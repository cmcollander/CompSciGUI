package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // MenuBar setup
        final Menu menu1 = new Menu("File");
        final Menu menu2 = new Menu("Ship");
        final Menu menu3 = new Menu("Port");
        final Menu menu4 = new Menu("Sea Monster");
        final Menu menu5 = new Menu("About");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2, menu3, menu4, menu5);
        
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
    
}

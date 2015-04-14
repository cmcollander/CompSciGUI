/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Chris
 */
public class MapGenerator extends Application {

    private static Canvas mapView;
    private static char[][] matrix;
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        matrix = new char[36][54];

        for (int row = 0; row < 36; row++) {
            for (int col = 0; col < 54; col++) {
                matrix[row][col] = '.';
            }
        }

        // MENU
        EventHandler<ActionEvent> menuAction = menuItemSelected();
        Menu menu1 = new Menu("File");
        MenuItem menu11 = new MenuItem("Open");
        menu11.setOnAction(menuAction);
        MenuItem menu12 = new MenuItem("Save");
        menu12.setOnAction(menuAction);
        MenuItem menu13 = new MenuItem("Exit");
        menu13.setOnAction(menuAction);
        menu1.getItems().addAll(menu11, menu12, menu13);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu1);

        // MAIN VIEW
        GridPane mainView = new GridPane();
        mainView.setHgap(0);
        mainView.setVgap(0);
        mainView.setPadding(new Insets(0, 0, 0, 0));

        // CANVAS
        EventHandler<MouseEvent> mouseAction = mouseEvent();
        mapView = new Canvas(540, 360);
        mapView.setOnMouseClicked(mouseAction);
        refreshMap();

        // VIEWS
        mainView.add(mapView, 0, 0);
        //mainView.add(buttonView, 0, 1);

        // ROOT
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mainView);

        Scene scene = new Scene(root, 540, 400);

        primaryStage.setTitle("Map Generator!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private EventHandler<ActionEvent> menuItemSelected() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MenuItem mItem = (MenuItem) event.getSource();
                String text = mItem.getText();

                if ("Open".equalsIgnoreCase(text)) {
                    openMap();
                }
                if ("Save".equalsIgnoreCase(text)) {
                    saveMap();
                }
                if ("Exit".equalsIgnoreCase(text)) {
                    System.exit(0);
                }

            }
        };
    }

    private EventHandler<MouseEvent> mouseEvent() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int col = (int) (event.getX() / 10);
                int row = (int) (event.getY() / 10);
                if (event.getButton() == MouseButton.PRIMARY) {
                    matrix[row][col] = matrix[row][col] == '.' ? '*' : '.';
                    refreshMap();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Coordinates");
                    String str = String.format("%d,%d:%2.6f,%2.6f", row, col, MapConverter.row2lat(row), MapConverter.col2lon(col));
                    alert.setContentText(str);
                    alert.showAndWait();
                }
            }
        };
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static void displayStackTrace(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText("Exception Stacktrace");
        alert.setContentText("The program threw an exception");

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

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public static void refreshMap() {

        // Base ocean
        GraphicsContext gc = mapView.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, 540, 360);

        // Iterate through the matrix, getting all land placed
        char currentChar;
        gc.setFill(Color.GREEN);

        for (int row = 0; row < 36; row++) {
            for (int col = 0; col < 54; col++) {
                currentChar = matrix[row][col];
                if (currentChar == '*') {
                    gc.fillRect(col * 10, row * 10, 10, 10);
                }
            }
        }
    }

    public void openMap() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        File file = fileChooser.showOpenDialog(stage);
        matrix = FileHandler.open(file);
        refreshMap();
    }

    public void saveMap() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map File");
        File file = fileChooser.showSaveDialog(stage);
        FileHandler.save(matrix, file);
    }

}

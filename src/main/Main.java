/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

    private static Stage stage;
    private static Map map;
    private static Canvas canvas;
    private static TextArea textArea;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

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
        Canvas mapView = new Canvas(300, 200);
        GraphicsContext gc = mapView.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, 300, 200);

        // TextArea Setup
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 1);

        // Create Scene
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mapView);
        root.setBottom(expContent);
        Scene scene = new Scene(root, 300, 500);
        primaryStage.setTitle("CompSci GUI - JavaFX!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private EventHandler<ActionEvent> menuItemSelected() {
        return new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                MenuItem mItem = (MenuItem) event.getSource();
                String text = mItem.getText();

                // File Menu
                // Open MenuItem
                if ("Open".equalsIgnoreCase(text)) {
                    TextInputDialog openDialog = new TextInputDialog("complex");
                    openDialog.setTitle("Open File");
                    openDialog.setHeaderText("Open File");
                    openDialog.setContentText("Please enter the tag for your file:");

                    Optional<String> result = openDialog.showAndWait();
                    if (result.isPresent()) {
                        try {
                            map = FileHandler.getMapFile(result.get() + ".map.txt");
                            map.setPort(FileHandler.getPortFile(result.get() + ".port.txt"));
                        } catch (Exception ex) {
                            displayStackTrace(ex);
                        }
                    }
                }
                // Close MenuItem
                if ("Close".equalsIgnoreCase(text)) {
                    map = new Map(); // This should reset absolutely everything
                }
                // Snap Shot MenuItem
                if ("Snap Shot".equalsIgnoreCase(text)) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Snap Shot");
                    File snapShotFile = fileChooser.showSaveDialog(stage);
                    try {
                        FileHandler.setSnapShot(snapShotFile, map);
                    } catch (Exception ex) {
                        displayStackTrace(ex);
                    }
                }
                // Exit MenuItem
                if ("Exit".equalsIgnoreCase(text)) {
                    System.exit(0);
                }

                // Ship Menu
                // Generate Ships MenuItem
                if ("Generate Ships".equalsIgnoreCase(text)) {
                    int numShips = 0;

                    TextInputDialog openDialog = new TextInputDialog("10");
                    openDialog.setTitle("Generate Ships");
                    openDialog.setHeaderText("Generate a Number of Ships");
                    openDialog.setContentText("Please enter the number of ships:");

                    Optional<String> result = openDialog.showAndWait();
                    if (result.isPresent()) {
                        try {
                            numShips = Integer.parseInt(result.get());
                        } catch (Exception ex) {
                            if (ex instanceof NumberFormatException) {
                                incorrectInput();
                            } else {
                                displayStackTrace(ex);
                            }
                        }
                    }
                    map.generateShips(numShips);
                }
                // Update Ships MenuItem
                if ("Update Ships".equalsIgnoreCase(text)) {
                    ArrayList<String> choices = new ArrayList<>();
                    for (CargoShip ship : map.getShips()) {
                        choices.add(ship.getName());
                    }

                    ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
                    dialog.setTitle("Update Ship");
                    dialog.setHeaderText("Update Ship");
                    dialog.setContentText("Choose a ship:");

                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        // Find which ship is being updated and pass it to the updateShip function
                        boolean found = false;
                        for (CargoShip ship : map.getShips()) {
                            if (ship.getName().equals(result.get())) {
                                found = true;
                                updateShip(ship);
                            }
                        }
                        if (!found) {
                            // This shouldn't happen anymore, just a precaution
                            incorrectInput();
                        }
                    }
                }

                // Display All Ships MenuItem
                if ("Display All Ships".equalsIgnoreCase(text)) {
                    String output = "";
                    for (CargoShip ship : map.getShips()) {
                        output += ship.display();
                    }
                    textArea.setText(output);
                }

                // Remove All Ships MenuItem
                if ("Remove All Ships".equalsIgnoreCase(text)) {
                    map.getShips().clear();
                }
            }
        };
    }

    public static void incorrectInput() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Input");
        alert.setHeaderText("Incorrect Input");
        alert.setContentText("Please Enter a Correct Value");
        alert.showAndWait();
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

    private static void updateShip(CargoShip ship) {
        if (map == null || map.getShips() == null || map.getShips().size() < 1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Ships Initialized");
            alert.setHeaderText("No Ships Initialized");
            alert.setContentText("Please run Generate Ships before calling this command!");
            alert.showAndWait();
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Update Ship");
        dialog.setHeaderText("Update Ship");

        // Can set a ship icon here, if we want
        ButtonType updateButtonType = new ButtonType("Update", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create the info fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField(ship.getName());
        TextField country = new TextField(ship.getCountryOfRegistration());
        TextField transponder = new TextField(Long.toString(ship.getTransponderNumber()));
        TextField length = new TextField(Double.toString(ship.getLength()));
        TextField beam = new TextField(Double.toString(ship.getBeam()));
        TextField draft = new TextField(Double.toString(ship.getDraft()));
        TextField longitude = new TextField(Double.toString(ship.getLongitude()));
        TextField latitude = new TextField(Double.toString(ship.getLatitude()));
        // ADD MORE FIELDS HERE!
        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Country of Registration:"), 0, 1);
        grid.add(country, 1, 1);
        grid.add(new Label("Transponder:"), 0, 2);
        grid.add(transponder, 1, 2);
        grid.add(new Label("Length:"), 0, 3);
        grid.add(length, 1, 3);
        grid.add(new Label("Beam:"), 0, 4);
        grid.add(beam, 1, 4);
        grid.add(new Label("Draft:"), 0, 5);
        grid.add(draft, 1, 5);
        grid.add(new Label("Longitude:"), 0, 6);
        grid.add(longitude, 1, 6);
        grid.add(new Label("Latitude:"), 0, 7);
        grid.add(latitude, 1, 7);

        dialog.getDialogPane().setContent(grid);

        Optional<Void> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                ship.setName(name.getText());
                ship.setCountryOfRegistration(country.getText());
                ship.setTransponderNumber(Long.parseLong(transponder.getText()));
                ship.setLength(Double.parseDouble(length.getText()));
                ship.setBeam(Double.parseDouble(beam.getText()));
                ship.setDraft(Double.parseDouble(draft.getText()));
                ship.setLongitude(Double.parseDouble(longitude.getText()));
                ship.setLatitude(Double.parseDouble(latitude.getText()));
            } catch (Exception ex) {
                incorrectInput();
            }
        }

    }
}

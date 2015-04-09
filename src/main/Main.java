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
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The Main class for the Port application.
 *
 * @author Collander, Khan, Wetzel
 */
public class Main extends Application {

    private static Stage stage;
    private static Map map;
    private static Canvas mapView;
    private static TextArea textArea;
    private static int canvasWidth;
    private static int canvasHeight;
    private static boolean mapLoaded;
    private static CargoShip draggedShip;
    private static SeaMonster draggedMonster;
    public static boolean threadStepComplete = false;

    /**
     * Main method for the application.
     *
     * @param primaryStage Primary GUI Stage
     */
    @Override
    public void start(Stage primaryStage) {
        draggedShip = null;
        draggedMonster = null;
        stage = primaryStage;
        canvasWidth = 54 * 10;
        canvasHeight = 36 * 10;
        mapLoaded = false;

        // Initialize Map
        map = new Map();

        // MenuItem Listener
        EventHandler<ActionEvent> menuAction = menuItemSelected();

        // MenuBar setup
        final Menu menu1 = new Menu("File");
        final Menu menu2 = new Menu("Ship");
        final Menu menu3 = new Menu("Port");
        final Menu menu4 = new Menu("Sea Monster");
        Label aboutLabel = new Label("About");

        // MouseHandler for About button
        aboutLabel.setOnMouseClicked((MouseEvent event) -> {
            aboutDialog();
        });

        Menu menu5 = new Menu();
        menu5.setGraphic(aboutLabel);

        // File Menu
        MenuItem menu11 = new MenuItem("Open");
        menu11.setOnAction(menuAction);
        MenuItem menu12 = new MenuItem("Close");
        menu12.setOnAction(menuAction);
        MenuItem menu13 = new MenuItem("Snap Shot");
        menu13.setOnAction(menuAction);
        MenuItem menu15 = new MenuItem("Map Generator");
        menu15.setOnAction(menuAction);
        MenuItem menu14 = new MenuItem("Exit");
        menu14.setOnAction(menuAction);
        menu1.getItems().addAll(menu11, menu12, menu13, menu15, menu14);

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
        MenuItem menu32 = new MenuItem("Update Docks");
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
        menuBar.getMenus().addAll(menu1, menu2, menu3, menu4, menu5);

        //Center Pane of mapView and another gridpane with buttons
        GridPane mainView = new GridPane();
        mainView.setHgap(0);
        mainView.setVgap(0);
        mainView.setPadding(new Insets(0, 0, 0, 0));

        GridPane buttonView = new GridPane();
        buttonView.setHgap(10);
        buttonView.setVgap(0);
        buttonView.setPadding(new Insets(0, 0, 0, 0));

        Button crankButton = new Button("Crank To 11!");
        buttonView.add(crankButton, 0, 0);

        // Start 3D Button
        crankButton.setOnMouseClicked((MouseEvent event) -> {
            start3D();
        });
        // MapView Setup
        mapView = new Canvas(canvasWidth, canvasHeight);
        refreshMap();
        mainView.add(mapView, 0, 0);
        mainView.add(buttonView, 0, 1);

        // mapView Mouse Event Handler
        mapView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    int col = (int) (event.getX() / 10);
                    int row = (int) (event.getY() / 10);
                    if (map.isShip(row, col)) {
                        CargoShip currentShip = map.getShipAt(row, col);
                        updateShip(currentShip);
                        row = currentShip.getRow();
                        col = currentShip.getCol();
                        try {
                            checkMonsterCollision(map.getShipAt(row, col));
                        } catch (Exception ex) {
                            displayStackTrace(ex);
                        }
                    }
                    if (map.isDock(row, col)) {
                        updateDock(map.getDockAt(row, col));
                    }
                    if (map.isMonster(new Position(row, col))) {
                        updateMonster(map.getMonsterAt(row, col));
                        try {
                            checkMonsterCollision(map.getMonsterAt(row, col));
                        } catch (Exception ex) {
                            displayStackTrace(ex);
                        }
                    }
                }
            }
        });
        mapView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    int col = (int) (event.getX() / 10);
                    int row = (int) (event.getY() / 10);

                    if (draggedShip != null) {
                        // If a ship is currently in the drag variable, erase it.
                        draggedShip = null;
                    } else if (map.isShip(row, col)) {
                        draggedShip = map.getShipAt(row, col);
                    } else if (draggedMonster != null) {
                        draggedMonster = null;
                    } else if (map.isMonster(new Position(row, col))) {
                        draggedMonster = map.getMonsterAt(row, col);
                    }
                }
            }
        });
        mapView.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    int col = (int) (event.getX() / 10);
                    int row = (int) (event.getY() / 10);

                    col = constrain(col, 0, 53);
                    row = constrain(row, 0, 35);

                    if (draggedShip != null) {
                        draggedShip.setRow(row);
                        draggedShip.setCol(col);
                        try {
                            checkMonsterCollision(draggedShip);
                        } catch (Exception ex) {
                            displayStackTrace(ex);
                        }
                        if (map.isDock(row, col)) {
                            draggedShip.setDirection(map.getDockAt(row, col).getDirection());
                        }

                        draggedShip = null;
                        refreshMap();
                    }
                    if (draggedMonster != null) {
                        draggedMonster.setRow(row);
                        draggedMonster.setCol(col);
                        try {
                            checkMonsterCollision(draggedMonster);
                        } catch (Exception ex) {
                            displayStackTrace(ex);
                        }

                        draggedMonster = null;
                        refreshMap();
                    }
                }
            }
        });

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
        root.setCenter(mainView);
        root.setBottom(expContent);
        Scene scene = new Scene(root, canvasWidth - 10, canvasHeight + 220);
        primaryStage.setTitle("CompSci GUI - JavaFX!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * This main method is only present in case we run the application from a
     * .jar file
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Menu Event Handler
     *
     * @return the actual Handle method containing instructions for the Handler
     */
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
                            mapLoaded = true;

                            // Set the directions for the docks
                            for (Dock dock : map.getPort().getDocks()) {
                                int row = dock.getRow();
                                int col = dock.getCol();

                                dock.setDirection(1); // Default Direction, East
                                // If land north, direction=0
                                if (row != 0) {
                                    if (map.getMatrix()[row - 1][col] == '.') {
                                        dock.setDirection(0);
                                    }
                                }
                                // If land south, direction=2
                                if (row != 35) {
                                    if (map.getMatrix()[row + 1][col] == '.') {
                                        dock.setDirection(2);
                                    }
                                }
                                // If land west, direction=1
                                if (col != 0) {
                                    if (map.getMatrix()[row][col - 1] == '.') {
                                        dock.setDirection(3);
                                    }
                                }
                            }

                            refreshMap();
                        } catch (Exception ex) {
                            displayStackTrace(ex);
                        }
                    }
                }
                // Close MenuItem
                if ("Close".equalsIgnoreCase(text)) {
                    map = new Map(); // This should reset absolutely everything
                    mapLoaded = false;
                    refreshMap();
                }
                // Snap Shot MenuItem
                if ("Snap Shot".equalsIgnoreCase(text)) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Snap Shot");
                    File snapShotFile = fileChooser.showSaveDialog(stage);
                    if (snapShotFile != null) {
                        try {
                            FileHandler.setSnapShot(snapShotFile, map);
                        } catch (Exception ex) {
                            displayStackTrace(ex);
                        }
                    }
                }
                // Map Generator MenuItem
                if ("Map Generator".equalsIgnoreCase(text)) {
                    Stage newStage = new Stage();
                    MapGenerator gen = new MapGenerator();
                    gen.start(newStage);
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
                    checkMonsterCollision();

                    refreshMap();
                }
                // Update Ships MenuItem
                if ("Update Ships".equalsIgnoreCase(text)) {

                    if (loadMapNotif()) {
                        return;
                    }
                    ArrayList<String> choices = new ArrayList<>();
                    map.getShips().stream().forEach(s -> choices.add(s.getName()));

                    if (choices.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("No Ships have been generated");
                        alert.setHeaderText("No Ships have been generated");
                        alert.setContentText("Please generate ships before attempting to update");
                        alert.showAndWait();
                    } else {
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
                                    checkMonsterCollision();
                                }
                            }
                            if (!found) {
                                // This shouldn't happen anymore, just a precaution
                                incorrectInput();
                            }
                        }
                        refreshMap();

                    }
                }

                // Display All Ships MenuItem
                if ("Display All Ships".equalsIgnoreCase(text)) {
                    if (loadMapNotif()) {
                        return;
                    }

                    String output = "";
                    output = map.getShips().stream().map((ship) -> ship.display()).reduce(output, String::concat);
                    textArea.setText(output);
                }

                // Remove All Ships MenuItem
                if ("Remove All Ships".equalsIgnoreCase(text)) {
                    map.getShips().clear();
                    refreshMap();
                }

                //Port Menu
                // Unload Ship MenuItem
                if ("Unload Ship".equalsIgnoreCase(text)) {
                    if (loadMapNotif()) {
                        return;
                    }
                    unloadShip();
                }

                // Update Docks MenuItem
                if ("Update Docks".equalsIgnoreCase(text)) {
                    if (loadMapNotif()) {
                        return;
                    }
                    /*
                     if (!mapLoaded) {
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setTitle("No Map Loaded");
                     alert.setHeaderText("No Map Loaded");
                     alert.setContentText("Please load a map in order to continue.");
                     alert.showAndWait();
                     return;
                     }*/

                    ArrayList<String> choices = new ArrayList<>();
                    map.getPort().getDocks().stream().forEach((dock) -> {
                        choices.add(dock.getName());
                    });

                    if (choices.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("No docks yet created");
                        alert.setHeaderText("No docks yet created");
                        alert.setContentText("Please create docks before attempting to update one");
                        alert.showAndWait();
                    }

                    ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
                    dialog.setTitle("Update Dock");
                    dialog.setHeaderText("Update Dock");
                    dialog.setContentText("Choose a dock:");

                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        // Find which dock is being updated and pass it to the updateDock function
                        boolean found = false;
                        for (Dock dock : map.getPort().getDocks()) {
                            if (dock.getName().equals(result.get())) {
                                found = true;
                                updateDock(dock);
                            }
                        }
                        if (!found) {
                            // This shouldn't happen anymore, just a precaution
                            incorrectInput();
                        }
                    }
                    refreshMap();
                }

                //Display All Docks MenuItem
                if ("Display All Docks".equalsIgnoreCase(text)) {
                    if (loadMapNotif()) {
                        return;
                    }
                    String output = "";
                    output = map.getPort().getDocks().stream().map((dock) -> dock.display()).reduce(output, String::concat);
                    textArea.setText(output);
                }

                //Display All Cargos MenuItem
                if ("Display All Cargos".equalsIgnoreCase(text)) {
                    String output = "";
                    output = map.getPort().getCargos().stream().map((cargo) -> cargo.display()).reduce(output, String::concat);
                    textArea.setText(output);
                }

                //Monster Menu
                //Generate Monsters
                if ("Generate Monsters".equalsIgnoreCase(text)) {

                    if (mapLoaded == true) {

                        int numMonsters = 0;

                        TextInputDialog openDialog = new TextInputDialog("10");
                        openDialog.setTitle("Generate Monsters");
                        openDialog.setHeaderText("Generate a Number of Monsters");
                        openDialog.setContentText("Please enter the number of monsters:");

                        Optional<String> result = openDialog.showAndWait();
                        if (result.isPresent()) {
                            try {
                                numMonsters = Integer.parseInt(result.get());
                            } catch (Exception ex) {
                                if (ex instanceof NumberFormatException) {
                                    incorrectInput();
                                } else {
                                    displayStackTrace(ex);
                                }
                            }
                        }
                        map.generateMonsters(numMonsters);
                        checkMonsterCollision();
                        refreshMap();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("ERROR!");
                        alert.setHeaderText("Map not loaded.");
                        alert.setContentText("Please load map first in order to generate monsters.");
                        alert.showAndWait();

                    }

                }

                // Update Monsters MenuItem
                if ("Update Monsters".equalsIgnoreCase(text)) {
                    if (mapLoaded == true) {
                        ArrayList<String> choices = new ArrayList<>();
                        map.getMonsters().stream().forEach((monster) -> {
                            choices.add(monster.getType());
                        });

                        if (choices.isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("No monsters yet created");
                            alert.setHeaderText("No monsters yet created");
                            alert.setContentText("Please generate monsters before attempting to update one");
                            alert.showAndWait();
                        } else {

                            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
                            dialog.setTitle("Update Monster");
                            dialog.setHeaderText("Update Monster");
                            dialog.setContentText("Choose a Monster:");

                            System.out.println("here3");

                            Optional<String> result = dialog.showAndWait();
                            if (result.isPresent()) {
                                // Find which dock is being updated and pass it to the updateDock function
                                boolean found = false;
                                for (SeaMonster monster : map.getMonsters()) {
                                    if (monster.getType().equals(result.get())) {
                                        found = true;
                                        updateMonster(monster);
                                        try {
                                            checkMonsterCollision(monster);
                                        } catch (Exception ex) {
                                            displayStackTrace(ex);
                                        }
                                    }
                                }
                                if (!found) {
                                    // This shouldn't happen anymore, just a precaution
                                    incorrectInput();
                                }
                            }
                            refreshMap();
                        }
                    } else {
                        //System.out.println("map not loaded!");

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("ERROR!");
                        alert.setHeaderText("Map not loaded.");
                        alert.setContentText("Please load map first in order to generate and update monsters.");
                        alert.showAndWait();

                    }
                }

                //Display All Monsters
                if ("Display All Monsters".equalsIgnoreCase(text)) {
                    if (loadMapNotif()) {
                        return;
                    }
                    String output = new String();
                    output = map.getMonsters().stream().map((monster) -> monster.display()).reduce(output, String::concat);
                    textArea.setText(output);
                }

                //Remove All Monsters
                if ("Remove All Monsters".equalsIgnoreCase(text)) {
                    map.getMonsters().clear();
                    refreshMap();
                }

                //Summon Godzilla
                if ("Summon Godzilla".equalsIgnoreCase(text)) {

                    if (mapLoaded == true) {

                        Godzilla g = new Godzilla();
                        Position pos = new Position(0, 0);

                        //added boolean check here
                        if (updateLocationGodzilla(pos)) {
                            // if updated, remove previous Godzilla
                            ArrayList toRemove = new ArrayList();
                            for (SeaMonster monst : map.getMonsters()) {
                                if (monst instanceof Godzilla) {
                                    toRemove.add(monst);
                                }
                            }
                            map.getMonsters().removeAll(toRemove);
                            toRemove.clear();

                            g.setPosition(pos);
                            // Random direction for Godzilla
                            Random random = new Random();
                            int direction = random.nextInt(4);
                            g.setDirection(direction);
                            
                            map.getMonsters().add(g);
                            try {
                                checkMonsterCollision(g);
                            } catch (Exception ex) {
                                displayStackTrace(ex);
                            }
                            try {
                                SoundManager.theme("thrill.mp3");
                            } catch (Exception ex) {
                                System.out.println("could not play sound.");
                            }
                        }
                        refreshMap();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR! Godzilla could not be summoned.");
                        alert.setHeaderText("Map not loaded.");
                        alert.setContentText("Please load map first in order to generate Godzilla.");
                        alert.showAndWait();

                    }

                }
            }
        };
    }

    /**
     * A dialog alert for incorrect input, usually being called on a number
     * parsing issue
     */
    public static void incorrectInput() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Input");
        alert.setHeaderText("Incorrect Input");
        alert.setContentText("Please Enter a Correct Value");
        alert.showAndWait();
    }

    /**
     * Easily dialog box to display the stack trace of an exception
     *
     * @param ex the exception to output
     */
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

    /**
     * Presents a dialog box to update a ship variable
     *
     * @param ship The ship to be edited
     */
    public static void updateShip(CargoShip ship) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Ship");
        dialog.setHeaderText("Update Ship");

        try {
            URL resource = new File("media\\images\\updateShip.png").toURI().toURL();
            dialog.setGraphic(new ImageView(resource.toString()));
        } catch (Exception ex) {
            displayStackTrace(ex);
        }

        ButtonType updateButtonType = new ButtonType("Update", ButtonData.OK_DONE);
        ButtonType cargoButtonType = new ButtonType("Cargo", ButtonData.OTHER);
        ButtonType locationButtonType = new ButtonType("Location", ButtonData.OTHER);
        dialog.getDialogPane().getButtonTypes().addAll(locationButtonType, cargoButtonType, updateButtonType, ButtonType.CANCEL);

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

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {
            ButtonType bt = result.get();

            // Update Button Hit
            if (bt.getText().equalsIgnoreCase("Update")) {
                try {
                    ship.setName(name.getText());
                    ship.setCountryOfRegistration(country.getText());
                    ship.setTransponderNumber(Long.parseLong(transponder.getText()));
                    ship.setLength(Double.parseDouble(length.getText()));
                    ship.setBeam(Double.parseDouble(beam.getText()));
                    ship.setDraft(Double.parseDouble(draft.getText()));
                } catch (Exception ex) {
                    incorrectInput();
                }
            }

            // Cargo Button Hit
            if (bt.getText().equalsIgnoreCase("Cargo")) {
                updateCargo(ship.getCargo());
            }

            // Location Button Hit
            if (bt.getText().equalsIgnoreCase("Location")) {
                updateLocation(ship.getPosition());
                if (map.isDock(ship.getRow(), ship.getCol())) {
                    ship.setDirection(map.getDockAt(ship.getRow(), ship.getCol()).getDirection());
                }
            }
        }

    }

    /**
     * Presents a dialog box to update a dock variable
     *
     * @param dock The dock to be edited
     */
    private static void updateDock(Dock dock) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Dock");
        dialog.setHeaderText("Update Dock");

        try {
            URL resource = new File("media\\images\\updateDock.png").toURI().toURL();
            dialog.setGraphic(new ImageView(resource.toString()));
        } catch (Exception ex) {
            displayStackTrace(ex);
        }

        ButtonType updateButtonType = new ButtonType("Update", ButtonData.OK_DONE);
        ButtonType locationButtonType = new ButtonType("Location", ButtonData.OTHER);
        dialog.getDialogPane().getButtonTypes().addAll(locationButtonType, updateButtonType, ButtonType.CANCEL);

        // Create the info fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField(dock.getName());
        TextField section = new TextField(Character.toString(dock.getSection()));
        TextField dockNumber = new TextField(Integer.toString(dock.getDockNumber()));
        TextField length = new TextField(Double.toString(dock.getLength()));
        TextField width = new TextField(Double.toString(dock.getWidth()));
        TextField depth = new TextField(Double.toString(dock.getDepth()));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Section Number:"), 0, 1);
        grid.add(section, 1, 1);
        grid.add(new Label("Dock Number:"), 0, 2);
        grid.add(dockNumber, 1, 2);
        grid.add(new Label("Length:"), 0, 3);
        grid.add(length, 1, 3);
        grid.add(new Label("Width:"), 0, 4);
        grid.add(width, 1, 4);
        grid.add(new Label("Depth:"), 0, 5);
        grid.add(depth, 1, 5);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {
            ButtonType bt = result.get();

            // If Update Button Hit
            if (bt.getText().equalsIgnoreCase("Update")) {
                try {
                    dock.setName(name.getText());
                    dock.setSection(section.getText().charAt(0));
                    dock.setDockNumber(Integer.parseInt(dockNumber.getText()));
                    dock.setLength(Double.parseDouble(length.getText()));
                    dock.setWidth(Double.parseDouble(width.getText()));
                    dock.setDepth(Double.parseDouble(depth.getText()));
                } catch (Exception ex) {
                    incorrectInput();
                }
            }

            // If Location Button Hit
            if (bt.getText().equalsIgnoreCase("Location")) {
                updateLocation(dock.getPosition());
            }
        }
        refreshMap();
    }

    /**
     * Presents a dialog box to update a SeaMonster variable
     *
     * @param monster The monster to be edited
     */
    private static void updateMonster(SeaMonster monster) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Monster");
        dialog.setHeaderText("Update Monster");

        try {
            URL resource = new File("media\\images\\updateMonster.png").toURI().toURL();
            dialog.setGraphic(new ImageView(resource.toString()));
        } catch (Exception ex) {
            displayStackTrace(ex);
        }

        ButtonType updateButtonType = new ButtonType("Update", ButtonData.OK_DONE);
        ButtonType locationButtonType = new ButtonType("Location", ButtonData.OTHER);
        dialog.getDialogPane().getButtonTypes().addAll(locationButtonType, updateButtonType, ButtonType.CANCEL);

        // Create the info fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField type = new TextField(monster.getType());

        grid.add(new Label("Type:"), 0, 0);
        grid.add(type, 1, 0);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {
            ButtonType bt = result.get();

            // If Update Button Hit
            if (bt.getText().equalsIgnoreCase("Update")) {
                try {
                    monster.setType(type.getText());
                } catch (Exception ex) {
                    incorrectInput();
                }
            }

            // If Location Button Hit
            if (bt.getText().equalsIgnoreCase("Location")) {
                updateLocation(monster.getPosition());
            }
        }
        refreshMap();
    }

    /**
     * Presents a dialog box to edit the location of either a CargoShip, Dock,
     * or SeaMonster
     *
     * @param pos The position object to be edited
     */
    private static void updateLocation(Position pos) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Location");
        dialog.setHeaderText("Update Location");

        try {
            URL resource = new File("media\\images\\updateLocation.png").toURI().toURL();
            dialog.setGraphic(new ImageView(resource.toString()));
        } catch (Exception ex) {
            displayStackTrace(ex);
        }

        ButtonType updateButtonType = new ButtonType("Update", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField row = new TextField(Integer.toString(pos.getRow()));
        TextField col = new TextField(Integer.toString(pos.getCol()));
        TextField latitude = new TextField(Double.toString(pos.getLatitude()));
        TextField longitude = new TextField(Double.toString(pos.getLongitude()));

        grid.add(new Label("Row:"), 0, 0);
        grid.add(row, 1, 0);
        grid.add(new Label("Column:"), 0, 1);
        grid.add(col, 1, 1);
        grid.add(new Label("Latitude:"), 0, 2);
        grid.add(latitude, 1, 2);
        grid.add(new Label("Longitude:"), 0, 3);
        grid.add(longitude, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {
            ButtonType bt = result.get();

            // If Update Button Hit
            if (bt.getText().equalsIgnoreCase("Update")) {
                try {
                    // Find out which values were changed
                    boolean rowChanged, colChanged, latChanged, lonChanged;

                    rowChanged = Integer.parseInt(row.getText()) != pos.getRow();
                    colChanged = Integer.parseInt(col.getText()) != pos.getCol();
                    lonChanged = Double.parseDouble(longitude.getText()) != pos.getLongitude();
                    latChanged = Double.parseDouble(latitude.getText()) != pos.getLatitude();

                    if ((rowChanged && latChanged) || (colChanged && lonChanged)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Incorrect Values");
                        alert.setHeaderText("Incorrect Values");
                        alert.setContentText("Cannot adjust row and latitude or column and longitude at the same time");
                        alert.showAndWait();
                    } else {
                        if (rowChanged) {
                            pos.setRow(constrain(Integer.parseInt(row.getText()), 0, 35));
                        }
                        if (colChanged) {
                            pos.setCol(constrain(Integer.parseInt(col.getText()), 0, 53));
                        }
                        if (latChanged) {
                            pos.setLatitude(constrain(Double.parseDouble(latitude.getText()), MapConverter.row2lat(0), MapConverter.row2lat(35)));
                        }
                        if (lonChanged) {
                            pos.setLongitude(constrain(Double.parseDouble(longitude.getText()), MapConverter.col2lon(0), MapConverter.col2lon(53)));
                        }
                    }
                } catch (Exception ex) {
                    incorrectInput();
                }
            }
        }
        refreshMap();
    }

    /**
     * Presents a dialog box to edit the location of Godzilla
     *
     * @param pos The position object to be edited
     */
    private static boolean updateLocationGodzilla(Position pos) {
        boolean check = false;
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Place Godzilla");
        dialog.setHeaderText("Place Godzilla");

        try {
            URL resource = new File("media\\images\\updateLocationGodzilla.png").toURI().toURL();
            dialog.setGraphic(new ImageView(resource.toString()));
        } catch (Exception ex) {
            displayStackTrace(ex);
        }

        ButtonType updateButtonType = new ButtonType("Place", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField row = new TextField(Integer.toString(pos.getRow()));
        TextField col = new TextField(Integer.toString(pos.getCol()));
        TextField latitude = new TextField(Double.toString(pos.getLatitude()));
        TextField longitude = new TextField(Double.toString(pos.getLongitude()));

        grid.add(new Label("Row:"), 0, 0);
        grid.add(row, 1, 0);
        grid.add(new Label("Column:"), 0, 1);
        grid.add(col, 1, 1);
        grid.add(new Label("Latitude:"), 0, 2);
        grid.add(latitude, 1, 2);
        grid.add(new Label("Longitude:"), 0, 3);
        grid.add(longitude, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {
            ButtonType bt = result.get();

            // If Update Button Hit
            if (bt.getText().equalsIgnoreCase("Place")) {
                try {
                    // Find out which values were changed
                    boolean rowChanged, colChanged, latChanged, lonChanged;

                    rowChanged = Integer.parseInt(row.getText()) != pos.getRow();
                    colChanged = Integer.parseInt(col.getText()) != pos.getCol();
                    lonChanged = Double.parseDouble(longitude.getText()) != pos.getLongitude();
                    latChanged = Double.parseDouble(latitude.getText()) != pos.getLatitude();

                    if ((rowChanged && latChanged) || (colChanged && lonChanged)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Incorrect Values");
                        alert.setHeaderText("Incorrect Values");
                        alert.setContentText("Cannot adjust row and latitude or column and longitude at the same time");
                        alert.showAndWait();
                    } else {
                        if (rowChanged) {
                            pos.setRow(constrain(Integer.parseInt(row.getText()), 0, 35));
                        }
                        if (colChanged) {
                            pos.setCol(constrain(Integer.parseInt(col.getText()), 0, 53));
                        }
                        if (latChanged) {
                            pos.setLatitude(constrain(Double.parseDouble(latitude.getText()), MapConverter.row2lat(0), MapConverter.row2lat(35)));
                        }
                        if (lonChanged) {
                            pos.setLongitude(constrain(Double.parseDouble(longitude.getText()), MapConverter.col2lon(0), MapConverter.col2lon(53)));
                        }
                    }
                    check = true;

                } catch (Exception ex) {
                    incorrectInput();
                }
            }
        }
        refreshMap();
        return check;
    }

    /**
     * Presents a dialog box to edit the location of either a Cargo
     *
     * @param cargo The Cargo object to be edited
     */
    private static void updateCargo(Cargo cargo) {
        if (cargo == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No Cargo Exists");
            alert.setHeaderText("No Cargo Exists");
            alert.setContentText("No cargo exists to edit!");
            alert.showAndWait();
            return;
        }

        int cargoType = 0; // Plain Cargo
        if (cargo instanceof Oil) {
            cargoType = 1; // Oil
        }
        if (cargo instanceof Box) {
            cargoType = 2; // Box
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Cargo");
        dialog.setHeaderText("Update Cargo");

        try {
            URL resource = new File("media\\images\\updateCargo.png").toURI().toURL();
            dialog.setGraphic(new ImageView(resource.toString()));
        } catch (Exception ex) {
            displayStackTrace(ex);
        }

        ButtonType updateButtonType = new ButtonType("Update", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create the info fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField description = new TextField(cargo.getDescription());

        String currTonString;
        switch (cargoType) {
            case 0:
                currTonString = Double.toString(cargo.getTonnage());
                break;
            case 1:
                currTonString = Integer.toString(((Oil) cargo).getBarrels());
                break;
            case 2:
                currTonString = Integer.toString(((Box) cargo).getTeus());
                break;
            default:
                currTonString = "ERROR";
                break;
        }

        TextField tonnage = new TextField(currTonString);

        String tonString;
        switch (cargoType) {
            case 0:
                tonString = "Tonnage:";
                break;
            case 1:
                tonString = "Barrels:";
                break;
            case 2:
                tonString = "TEUs:";
                break;
            default:
                tonString = "ERROR";
                break;
        }

        grid.add(new Label("Description:"), 0, 0);
        grid.add(description, 1, 0);
        grid.add(new Label(tonString), 0, 1);
        grid.add(tonnage, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get().getText().equalsIgnoreCase("Update")) {
            try {
                cargo.setDescription(description.getText());
                switch (cargoType) {
                    case 0:
                        cargo.setTonnage(Double.parseDouble(tonnage.getText()));
                        break;
                    case 1:
                        ((Oil) cargo).setBarrels(Integer.parseInt(tonnage.getText()));
                        break;
                    case 2:
                        ((Box) cargo).setTeus(Integer.parseInt(tonnage.getText()));
                        break;
                    default:
                        break;
                }
            } catch (Exception ex) {
                incorrectInput();
            }
        }
    }

    /**
     * Refresh the graphic viewport of the GUI
     */
    public static void refreshMap() {

        // Base ocean
        GraphicsContext gc = mapView.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        // If no map has been loaded, just exit here
        if (!mapLoaded) {
            return;
        }

        // Iterate through the matrix, getting all land placed
        char[][] matrix = map.getMatrix();
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

        //Iterate through the docks, ALSO CREATING LAND UNDERNEATH
        // *Not all docks are placed on land. Some are on land, some are on water, at least in 'complex'
        for (Dock dock : map.getPort().getDocks()) {
            int dockType = 0;
            if (dock instanceof Crane) {
                dockType = 1;
            }
            if (dock instanceof Pier) {
                dockType = 2;
            }

            int rowP = 10 * dock.getRow() + 10;
            int colP = 10 * dock.getCol();
            int row = dock.getRow();
            int col = dock.getCol();

            // Add LAND under Dock
            gc.setFill(Color.GREEN);
            gc.fillRect(col * 10, row * 10, 10, 10);

            gc.setFill(Color.BLACK);

            switch (dockType) {
                case 0:
                    gc.fillText("D", colP, rowP);
                    break;
                case 1:
                    gc.fillText("C", colP, rowP);
                    break;
                case 2:
                    gc.fillText("P", colP, rowP);
                    break;
            }
        }

        // Iterate through the ships
        for (CargoShip ship : map.getShips()) {
            int shipType = 0;
            if (ship instanceof ContainerShip) {
                shipType = 1;
            }
            if (ship instanceof OilTanker) {
                shipType = 2;
            }

            int row = 10 * ship.getRow() + 10;
            int col = 10 * ship.getCol();

            switch (shipType) {
                case 0:
                    gc.setFill(Color.WHITE);
                    gc.fillText("S", col, row);
                    break;
                case 1:
                    gc.setFill(Color.WHITE);
                    gc.fillText("B", col, row);
                    break;
                case 2:
                    gc.setFill(Color.RED);
                    gc.fillText("T", col, row);
                    break;
            }

            if (map.isDock(ship.getRow(), ship.getCol())) {
                if (map.isShipSafe(ship.getRow(), ship.getCol())) {
                    // Safely Docked
                    gc.setFill(Color.BLACK);
                    gc.fillRect(col, row - 10, 10, 10);
                    gc.setFill(Color.GREEN);
                    gc.fillText("$", col, row);
                }
            }
            if (!map.isShipSafe(ship.getRow(), ship.getCol())) {
                // Ship is Unsafe
                // Safely Docked
                gc.setFill(Color.YELLOW);
                gc.fillRect(col, row - 10, 10, 10);
                gc.setFill(Color.RED);
                gc.fillText("X", col, row);
            }
        }

        // Iterate through the Monsters
        for (SeaMonster monster : map.getMonsters()) {
            int monsterType = -1;

            if (monster instanceof Leviathan) {
                monsterType = 0;
            }
            if (monster instanceof Kraken) {
                monsterType = 1;
            }
            if (monster instanceof SeaSerpent) {
                monsterType = 2;
            }
            if (monster instanceof Godzilla) {
                monsterType = 3;
            }

            int row = 10 * monster.getRow() + 10;
            int col = 10 * monster.getCol();

            gc.setFill(Color.BLUE);
            gc.fillRect(col, row - 10, 10, 10);

            switch (monsterType) {
                case 0:
                    gc.setFill(Color.YELLOW);
                    gc.fillText("L", col, row);
                    break;
                case 1:
                    gc.setFill(Color.YELLOW);
                    gc.fillText("K", col, row);
                    break;
                case 2:
                    gc.setFill(Color.YELLOW);
                    gc.fillText("\u01A7", col, row);
                    break;
                case 3:
                    gc.setFill(Color.YELLOW);
                    gc.fillText("G", col, row);
                    break;
            }
        }
    }

    /**
     * Called upon hitting the About button, this presents a dialog with
     * information about our team and this project
     */
    private static void aboutDialog() {
        String aboutMessage = new String();

        aboutMessage += "Team ____\n";
        aboutMessage += "CSE 1325-002\n";
        aboutMessage += "April 2, 2015\n";
        aboutMessage += "\tName: Chris Collander\n";
        aboutMessage += "\tID: 1001101078\n";
        aboutMessage += "\tName: Abdul Rafey Khan\n";
        aboutMessage += "\tID: 1000955036\n";
        aboutMessage += "\tName: Clint Wetzel\n";
        aboutMessage += "\tID: 1000717927\n";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText(aboutMessage);
        alert.showAndWait();

    }

    /**
     * Constrain an integer between a minimum and a maximum value
     *
     * @param val the integer to constrain
     * @param min the minimum constraint
     * @param max the maximum constraint
     * @return the constrained value
     */
    private static int constrain(int val, int min, int max) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }

    /**
     * Constrain an integer between a minimum and a maximum value
     *
     * @param val the integer to constrain
     * @param min the minimum constraint
     * @param max the maximum constraint
     * @return the constrained value
     */
    private static double constrain(double val, double min, double max) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }

    /**
     * Presents a dialog box to unload a ship that is safely at the correct type
     * of Dock
     */
    private static void unloadShip() {
        ArrayList<String> choices = new ArrayList<>();
        for (CargoShip ship : map.getShips()) {
            if (map.isShipSafe(ship.getRow(), ship.getCol()) && map.isDock(ship.getRow(), ship.getCol()) && ship.getCargo() != null) {
                choices.add(ship.getName());
            }
        }

        // If no safe ships
        if (choices.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Safe Ships");
            alert.setHeaderText("No Safe Ships");
            alert.setContentText("There are no safe ships with cargo parked at a dock to unload");
            alert.showAndWait();
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Unload Ship");
        dialog.setHeaderText("Unload Ship");
        dialog.setContentText("Which Ship would you like to unload?");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            for (CargoShip ship : map.getShips()) {
                if (ship.getName().equalsIgnoreCase(result.get())) {
                    map.getPort().getCargos().add(ship.getCargo());
                    ship.setCargo(null);
                    return;
                }
            }
        }

    }

    /**
     * Checks to see if a monster has collided with a ship
     */
    public static void checkMonsterCollision() {
        map.getMonsters().stream().forEach((monster) -> {
            try {
                checkMonsterCollision(monster);
            } catch (Exception ex) {
                displayStackTrace(ex);
            }
        });
    }

    /**
     * Checks to see if a monster has collided with a ship
     *
     * @param monster The monster to check
     * @throws java.lang.Exception
     */
    public static void checkMonsterCollision(SeaMonster monster) throws Exception {
        if (map.isShip(monster.getRow(), monster.getCol())) {
            SoundManager.growl(monster);
            textArea.setText(monster.battleCry());

            // Remove ship
            /*
             for(CargoShip ship : map.getShips()) {
             if(ship.getRow() == monster.getRow() && ship.getCol() == monster.getCol())
             map.getShips().remove(ship);
             }
             */
            ArrayList toRemove = new ArrayList();
            for (CargoShip ship : map.getShips()) {
                if (ship.getRow() == monster.getRow() && ship.getCol() == monster.getCol()) {
                    toRemove.add(ship);
                    ship.removeModel();
                    //map.getShips().remove(ship);
                }
            }
            map.getShips().removeAll(toRemove);
            toRemove.clear();

            refreshMap();

        }
    }

    /**
     * Checks to see if a Ship has collided with a monster
     *
     * @param ship the ship to check
     * @throws java.lang.Exception
     */
    public static void checkMonsterCollision(CargoShip ship) throws Exception {
        if (map.isMonster(new Position(ship.getRow(), ship.getCol()))) {
            SoundManager.growl(map.getMonsterAt(ship.getRow(), ship.getCol()));
            textArea.setText(map.getMonsterAt(ship.getRow(), ship.getCol()).battleCry());

            // Remove ship
            ship.removeModel();

            map.getShips().remove(ship);
        }
    }

    public boolean loadMapNotif() {
        if (!mapLoaded) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Map Loaded");
            alert.setHeaderText("No Map Loaded");
            alert.setContentText("Please load a map in order to continue.");
            alert.showAndWait();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Load the map into a 3D Representation of the map
     */
    public void start3D() {
        if (!mapLoaded) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Map Loaded");
            alert.setHeaderText("No Map Loaded");
            alert.setContentText("Please load a map before running the simulation");
            alert.showAndWait();
            return;
        }

        try {
            SoundManager.theme("theme.mp3");
        } catch (Exception ex) {
            System.out.println("Error playing sound track.");
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        String text = new String();
        text += "Please be patient while the simulation loads...\n";
        text += "I'll leave this for you to ponder over meanwhile\n";
        text += "*Why is it that when you transport something by car\n";
        text += "*it is called a shipment, but when you transport\n";
        text += "*something by ship, it is called cargo???";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.setTitle("Please wait...");
        alert.setHeaderText(null);
        alert.show();

        (new PortSimulation(map)).run();

        alert.close();
    }
}

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
        aboutLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                aboutDialog();
            }
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
        Button runButton = new Button("Run");
        buttonView.add(crankButton, 0, 0);
        buttonView.add(runButton, 1, 0);

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
                        updateShip(map.getShipAt(row, col));
                        checkMonsterCollision(map.getShipAt(row, col));
                    }
                    if (map.isDock(row, col)) {
                        updateDock(map.getDockAt(row, col));
                    }
                    if (map.isMonster(new Position(row, col))) {
                        updateMonster(map.getMonsterAt(row, col));
                        checkMonsterCollision(map.getMonsterAt(row, col));
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
                        checkMonsterCollision(draggedShip);
                        draggedShip = null;
                        refreshMap();
                    }
                    if (draggedMonster != null) {
                        draggedMonster.setRow(row);
                        draggedMonster.setCol(col);
                        checkMonsterCollision(draggedMonster);
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
                            mapLoaded = true;
                            refreshMap();
                        } catch (Exception ex) {
                            displayStackTrace(ex);
                        }
                    }
                }
                // Close MenuItem
                if ("Close".equalsIgnoreCase(text)) {
                    map = new Map(); // This should reset absolutely everything
                    refreshMap();
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
                    checkMonsterCollision();
                    
                    refreshMap();
                }
                // Update Ships MenuItem
                if ("Update Ships".equalsIgnoreCase(text)) {
                    ArrayList<String> choices = new ArrayList<>();
                    for (CargoShip ship : map.getShips()) {
                        choices.add(ship.getName());
                    }

                    if (choices.size() == 0) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("No Ships have been generated");
                        alert.setHeaderText("No Ships have been generated");
                        alert.setContentText("Please generate ships before attempting to update");
                        alert.showAndWait();
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
                    refreshMap();
                }

                //Port Menu
                // Unload Ship MenuItem
                if ("Unload Ship".equalsIgnoreCase(text)) {
                    unloadShip();
                }

                // Update Docks MenuItem
                if ("Update Docks".equalsIgnoreCase(text)) {
                    ArrayList<String> choices = new ArrayList<>();
                    for (Dock dock : map.getPort().getDocks()) {
                        choices.add(dock.getName());
                    }

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
                    String output = "";
                    for (Dock dock : map.getPort().getDocks()) {
                        output += dock.display();
                    }
                    textArea.setText(output);
                }

                //Display All Cargos MenuItem
                if ("Display All Cargos".equalsIgnoreCase(text)) {
                    String output = "";
                    for (Cargo cargo : map.getPort().getCargos()) {
                        output += cargo.display();
                    }
                    textArea.setText(output);
                }

                //Monster Menu
                //Generate Monsters
                if ("Generate Monsters".equalsIgnoreCase(text)) {
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
                }

                // Update Monsters MenuItem
                if ("Update Monsters".equalsIgnoreCase(text)) {
                    ArrayList<String> choices = new ArrayList<>();
                    for (SeaMonster monster : map.getMonsters()) {
                        choices.add(monster.getType());
                    }

                    if (choices.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("No monsters yet created");
                        alert.setHeaderText("No monsters yet created");
                        alert.setContentText("Please generate monsters before attempting to update one");
                        alert.showAndWait();
                    }

                    ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
                    dialog.setTitle("Update Monster");
                    dialog.setHeaderText("Update Monster");
                    dialog.setContentText("Choose a Monster:");

                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        // Find which dock is being updated and pass it to the updateDock function
                        boolean found = false;
                        for (SeaMonster monster : map.getMonsters()) {
                            if (monster.getType().equals(result.get())) {
                                found = true;
                                updateMonster(monster);
                                checkMonsterCollision(monster);
                            }
                        }
                        if (!found) {
                            // This shouldn't happen anymore, just a precaution
                            incorrectInput();
                        }
                    }
                    refreshMap();
                }

                //Display All Monsters
                if ("Display All Monsters".equalsIgnoreCase(text)) {
                    String output = new String();
                    for (SeaMonster monster : map.getMonsters()) {
                        output += monster.display();
                    }
                    textArea.setText(output);
                }

                //Remove All Monsters
                if ("Remove All Monsters".equalsIgnoreCase(text)) {
                    map.getMonsters().clear();
                    refreshMap();
                }

                //Summon Godzilla
                if ("Summon Godzilla".equalsIgnoreCase(text)) {
                    // If there is already a Godzilla, remove him
                    for (SeaMonster monster : map.getMonsters()) {
                        if (monster instanceof Godzilla) {
                            map.getMonsters().remove(monster);
                        }
                    }

                    Godzilla g = new Godzilla();
                    Position pos = new Position(0, 0);
                    updateLocationGodzilla(pos);
                    g.setPosition(pos);
                    map.getMonsters().add(g);
                    checkMonsterCollision(g);
                    refreshMap();
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
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Ship");
        dialog.setHeaderText("Update Ship");

        // Ship Icon *DOESNT WORK YET*
        try {
            URL resource = new File("media\\ship_icon.png").toURI().toURL();
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
            }
        }

    }

    private static void updateDock(Dock dock) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Dock");
        dialog.setHeaderText("Update Dock");

        // Can set a ship icon here, if we want
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

    private static void updateMonster(SeaMonster monster) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Monster");
        dialog.setHeaderText("Update Monster");

        // Can set a ship icon here, if we want
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

    private static void updateLocation(Position pos) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Location");
        dialog.setHeaderText("Update Location");

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
                            pos.setRow(Integer.parseInt(row.getText()));
                        }
                        if (colChanged) {
                            pos.setCol(Integer.parseInt(col.getText()));
                        }
                        if (latChanged) {
                            pos.setLatitude(Integer.parseInt(latitude.getText()));
                        }
                        if (lonChanged) {
                            pos.setLongitude(Integer.parseInt(longitude.getText()));
                        }
                    }
                } catch (Exception ex) {
                    incorrectInput();
                }
            }
        }
        refreshMap();
    }

    private static void updateLocationGodzilla(Position pos) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Place Godzilla");
        dialog.setHeaderText("Place Godzilla");

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
                            pos.setRow(Integer.parseInt(row.getText()));
                        }
                        if (colChanged) {
                            pos.setCol(Integer.parseInt(col.getText()));
                        }
                        if (latChanged) {
                            pos.setLatitude(Integer.parseInt(latitude.getText()));
                        }
                        if (lonChanged) {
                            pos.setLongitude(Integer.parseInt(longitude.getText()));
                        }
                    }
                } catch (Exception ex) {
                    incorrectInput();
                }
            }
        }
        refreshMap();
    }

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

    private static void refreshMap() {

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
                    gc.fillText("S", col, row);
                    break;
                case 3:
                    gc.setFill(Color.YELLOW);
                    gc.fillText("G", col, row);
                    break;
            }
        }
    }

    private static void aboutDialog() {
        String aboutMessage = new String();

        aboutMessage += "Team ____\n";
        aboutMessage += "CSE 1325-002\n";
        aboutMessage += "April 2, 2015\n";
        aboutMessage += "\tName: Chris Collander\n";
        aboutMessage += "\tID: 1001101078\n";
        aboutMessage += "\tName: Abdul Rafey Khan\n";
        aboutMessage += "\tID: __________\n";
        aboutMessage += "\tName: Clint Wetzel\n";
        aboutMessage += "\tID: 1000717927\n";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText(aboutMessage);
        alert.showAndWait();
    }

    private static int constrain(int val, int min, int max) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }

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
    
    public void checkMonsterCollision() {
        for(SeaMonster monster : map.getMonsters())
            checkMonsterCollision(monster);
    }
    
    public void checkMonsterCollision(SeaMonster monster) {
        if(map.isShip(monster.getRow(), monster.getCol())) {
            try {
                SoundManager.growl();
            } catch(Exception ex) {
                displayStackTrace(ex);
            }
            textArea.setText(monster.battleCry());
        }
    }
    
    public void checkMonsterCollision(CargoShip ship) {
        if(map.isMonster(new Position(ship.getRow(),ship.getCol()))) {
            try {
                SoundManager.growl();
            } catch(Exception ex) {
                displayStackTrace(ex);
            }
            textArea.setText(map.getMonsterAt(ship.getRow(), ship.getCol()).battleCry());
        }
    }
}

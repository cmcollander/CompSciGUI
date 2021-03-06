/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

/*
 X axis is Columns
 Z axis is Rows
 Y axis is Height
 */
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import java.io.File;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import org.fxyz.cameras.AdvancedCamera;
import org.fxyz.cameras.controllers.FPSController;

public class PortSimulation {

    private final Map map;
    private final Stage stage = new Stage();
    private final Group root = new Group();
    private final FPSController controller = new FPSController();
    AdvancedCamera camera = new AdvancedCamera();
    private boolean fullscreen = true;

    private Xform world = new Xform();
    private Xform oceanGroup = new Xform();
    private Xform landGroup = new Xform();
    private Xform shipGroup = new Xform();
    private Xform monsterGroup = new Xform();
    private Xform dockGroup = new Xform();
    private Xform enterpriseGroup = new Xform();
    private Xform spillGroup = new Xform();

    TdsModelImporter importer = new TdsModelImporter();

    public PortSimulation(Map map) {
        this.map = map;
    }

    public void run() {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        buildOcean();
        buildLand();
        buildShips();
        buildMonsters();
        buildDocks();
        buildSpills();
        buildEnterprise();

        // Get yourself in the right position by repositioning the world... Kinda philosophical
        world.setRotateZ(180);

        Scene scene = new Scene(root, 1024, 768, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.SKYBLUE);
        handleKeyboard(scene, world);

        stage.setTitle("3D Port Simulation");
        stage.setScene(scene);
        stage.setFullScreen(fullscreen);
        stage.show();

        controller.setScene(scene);
        controller.setMouseLookEnabled(true);
        camera.setController(controller);
        root.getChildren().add(camera);
        scene.setCamera(camera);
        setInitialCamera();
    }

    public void setInitialCamera() {
        // Default camera position (41,-183,445),(-35,-150,0)
        controller.affine.setTx(17);
        controller.affine.setTy(-323);
        controller.affine.setTz(464);
        controller.setRotate(-45, -180, 0);
    }

    private void handleKeyboard(Scene scene, final Node root) {
        scene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case Z:
                    setInitialCamera();
                    break;
                case E:
                    PredatorPrey.step(map);
                    Main.checkCollisions();
                    buildSpills();
                    map.getMonsters().stream().forEach((mon) -> {
                        mon.updateXform();
                    });
                    map.getShips().stream().forEach((ship) -> {
                        ship.updateXform();
                    });
                    if (map.hasEnterprise()) {
                        map.getEnterprise().updateXform();
                    }
                    Main.refreshMap();
                    break;
                case Y:
                    if (!map.hasEnterprise()) {
                        map.setEnterprise(new Enterprise());
                        buildEnterprise();
                    }
                    break;
                case F:  // Fullscreen Toggle (messes up if you use ESC to exit fullscreen
                    stage.setFullScreen(!fullscreen);
                    fullscreen = !fullscreen;
                    break;
                case Q:  // Fully closes the 3D Display
                    stage.close();
                    Main.refreshMap();
                    break;
            }
        });
    }

    private void buildOcean() {
        final PhongMaterial oceanMaterial = new PhongMaterial();
        oceanMaterial.setDiffuseColor(Color.BLUE);
        oceanMaterial.setSpecularColor(Color.LIGHTBLUE);

        final Box ocean = new Box(540, 1, 360);
        ocean.setTranslateX(540 / 2);
        ocean.setTranslateZ(360 / 2);
        ocean.setTranslateY(-1);

        ocean.setMaterial(oceanMaterial);

        oceanGroup.getChildren().add(ocean);
        oceanGroup.setVisible(true);
        world.getChildren().add(ocean);
    }

    private void buildLand() {
        final PhongMaterial landMaterial = new PhongMaterial();
        landMaterial.setDiffuseColor(Color.GREEN);
        landMaterial.setSpecularColor(Color.LIGHTGREEN);

        for (int row = 0; row < 36; row++) {
            for (int col = 0; col < 54; col++) {
                if (map.getMatrix()[row][col] == '*' && !map.isDock(row, col)) {
                    Box land = new Box(10, 2, 10);
                    // Translation
                    land.setTranslateY(1);
                    land.setTranslateX(5 + col * 10);
                    land.setTranslateZ(5 + row * 10);
                    land.setMaterial(landMaterial);
                    landGroup.getChildren().add(land);
                }
            }
        }
        world.getChildren().add(landGroup);
        landGroup.setVisible(true);
    }

    private void buildShips() {
        for (CargoShip ship : map.getShips()) {
            int shipType = 0; // CargoShip
            if (ship instanceof ContainerShip) {
                shipType = 1; // ContainerShip
            }
            if (ship instanceof OilTanker) {
                shipType = 2; // OilTanker
            }

            // Read in 3D Model
            File modelFile;
            switch (shipType) {
                case 0:
                    modelFile = new File("media\\models\\cargoShip.3ds");
                    break;
                case 1:
                    modelFile = new File("media\\models\\containerShip.3ds");
                    break;
                default:
                    modelFile = new File("media\\models\\oilTanker.3ds");
                    break;

            }
            try {
                importer.read(modelFile.toURI().toURL());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error opening 3D model!");
                alert.showAndWait();
                return;
            }
            Node[] shipNodes = importer.getImport();
            ship.getModel().getChildren().addAll(shipNodes);

            // Translation
            ship.getModel().setRotateZ(180.0);
            ship.updateXform();

            shipGroup.getChildren().add(ship.getModel());
        }
        world.getChildren().add(shipGroup);
        shipGroup.setVisible(true);
    }

    private void buildMonsters() {
        for (SeaMonster monster : map.getMonsters()) {
            int mType = 0; // Godzilla
            if (monster instanceof Kraken) {
                mType = 1;
            }
            if (monster instanceof Leviathan) {
                mType = 2;
            }
            if (monster instanceof SeaSerpent) {
                mType = 3;
            }

            // Read in 3D Model
            File modelFile;
            switch (mType) {
                case 0:
                    modelFile = new File("media\\models\\godzilla.3ds");
                    break;
                case 1:
                    modelFile = new File("media\\models\\kraken.3ds");
                    break;
                case 2:
                    modelFile = new File("media\\models\\leviathan.3ds");
                    break;
                default:
                    modelFile = new File("media\\models\\seaSerpent.3ds");
                    break;

            }
            try {
                importer.read(modelFile.toURI().toURL());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error opening 3D model!");
                alert.showAndWait();
                return;
            }
            Node[] monsterNodes = importer.getImport();
            monster.getModel().getChildren().addAll(monsterNodes);

            // Translation
            monster.getModel().setRotateZ(180.0);
            monster.getModel().setTranslateY(mType == 0 ? ((map.getMatrix()[monster.getRow()][monster.getCol()] == '*') ? 2 : 0) : 0);
            monster.updateXform();

            monsterGroup.getChildren().add(monster.getModel());
        }
        world.getChildren().add(monsterGroup);
        monsterGroup.setVisible(true);
    }

    private void buildDocks() {
        for (Dock dock : map.getPort().getDocks()) {
            int dockType = 0; // Dock
            if (dock instanceof Crane) {
                dockType = 1; // Crane
            }
            if (dock instanceof Pier) {
                dockType = 2; // Pier
            }

            // Read in 3D Model
            Xform dockModel = new Xform();
            File modelFile;
            switch (dockType) {
                case 0:
                    modelFile = new File("media\\models\\dock.3ds");
                    break;
                case 1:
                    modelFile = new File("media\\models\\crane.3ds");
                    break;
                default:
                    modelFile = new File("media\\models\\pier.3ds");
                    break;
            }
            try {
                importer.read(modelFile.toURI().toURL());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error opening 3D model!");
                alert.showAndWait();
                return;
            }
            Node[] dockNodes = importer.getImport();
            dockModel.getChildren().addAll(dockNodes);

            // Translation
            dockModel.setRotateZ(180.0);
            dockModel.setRotateY(dock.getDirection() * 90);
            dockModel.setTranslateX(5 + dock.getCol() * 10);
            dockModel.setTranslateZ(5 + dock.getRow() * 10);

            dockGroup.getChildren().add(dockModel);
        }
        world.getChildren().add(dockGroup);
        dockGroup.setVisible(true);
    }

    public void buildSpills() {
        spillGroup = new Xform();
        for (OilSpill spill : map.getSpills()) {
            File modelFile;
            modelFile = new File("media\\models\\oilSpill.3ds");
            try {
                importer.read(modelFile.toURI().toURL());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error opening 3D model!");
                alert.showAndWait();
                return;
            }
            Node[] spillNodes = importer.getImport();
            spill.getModel().getChildren().addAll(spillNodes);

            // Translation
            spill.getModel().setRotateZ(180.0);
            spill.getModel().setRotateY(spill.getDirection() * 90);
            spill.getModel().setTranslateY(0.5);
            spill.getModel().setTranslateX(5 + spill.getPosition().getCol() * 10);
            spill.getModel().setTranslateZ(5 + spill.getPosition().getRow() * 10);

            spillGroup.getChildren().add(spill.getModel());
        }
        world.getChildren().add(spillGroup);
        spillGroup.setVisible(true);
    }

    public void buildEnterprise() {
        // Only build an Enterprise if there is not one currently. The choice to require a Godzilla or not is up to the client-code.
        if (!map.hasEnterprise()) {
            return;
        }

        File modelFile = new File("media\\models\\Enterprise.3ds");
        try {
            importer.read(modelFile.toURI().toURL());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error opening 3D model!");
            alert.showAndWait();
            return;
        }
        Node[] eNodes = importer.getImport();
        Xform model = new Xform();
        model.getChildren().addAll(eNodes);
        map.getEnterprise().setModel(model);
        map.getEnterprise().updateXform();
        enterpriseGroup.getChildren().clear();
        enterpriseGroup.getChildren().add(model);
        world.getChildren().add(enterpriseGroup);
        enterpriseGroup.setVisible(true);
    }

}

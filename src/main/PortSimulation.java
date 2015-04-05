/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

import javafx.event.EventHandler;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.stage.Stage;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import java.io.File;
import javafx.scene.control.Alert;

public class PortSimulation {

    private final Map map;
    private final Stage stage = new Stage();
    private final Group root = new Group();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private boolean fullscreen = true;

    private final Xform world = new Xform();
    private final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();
    private final Xform oceanGroup = new Xform();
    private final Xform landGroup = new Xform();
    private final Xform shipGroup = new Xform();
    private final Xform monsterGroup = new Xform();
    private final Xform dockGroup = new Xform();

    TdsModelImporter importer = new TdsModelImporter();

    /*
     X axis is Columns
     Z axis is Rows
     Y axis is Height
     */
    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 30.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 120.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;

    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    public PortSimulation(Map map) {
        this.map = map;
    }

    public void run() {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        buildCamera();
        buildOcean();
        buildLand();
        buildShips();
        buildMonsters();
        buildDocks();

        // Get yourself in the right position by repositioning the world... Kinda philosophical
        world.setTranslateX(-540 / 2);
        world.setTranslateZ(-360 / 2);

        Scene scene = new Scene(root, 1024, 768, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.SKYBLUE);
        handleKeyboard(scene, world);
        handleMouse(scene, world);

        stage.setTitle("3D Port Simulation");
        stage.setScene(scene);
        stage.setFullScreen(fullscreen);
        stage.show();

        scene.setCamera(camera);
    }

    private void handleMouse(Scene scene, final Node root) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * MOUSE_SPEED * ROTATION_SPEED);
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * MOUSE_SPEED * ROTATION_SPEED);
                } else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX * MOUSE_SPEED * 10;
                    camera.setTranslateZ(newZ);
                } else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * MOUSE_SPEED * 10 * TRACK_SPEED);
                    cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * MOUSE_SPEED * 10 * TRACK_SPEED);
                }
            }
        });
    }

    private void handleKeyboard(Scene scene, final Node root) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                double z, newZ;
                switch (event.getCode()) {
                    case F:
                        stage.setFullScreen(!fullscreen);
                        fullscreen = !fullscreen;
                    case Z:
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
                        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
                        break;
                    case V:
                        landGroup.setVisible(!landGroup.isVisible());
                        break;
                    case W:
                        z = camera.getTranslateZ();
                        newZ = z + 200 * MOUSE_SPEED;
                        camera.setTranslateZ(newZ);
                        break;
                    case S:
                        z = camera.getTranslateZ();
                        newZ = z - 200 * MOUSE_SPEED;
                        camera.setTranslateZ(newZ);
                        break;
                    case Q:
                        stage.close();
                        break;
                }
            }
        });
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(-180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);

        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
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
            ship.getModel().setRotateY(ship.getDirection() * 90);
            ship.getModel().setTranslateX(5 + ship.getCol() * 10);
            ship.getModel().setTranslateZ(5 + ship.getRow() * 10);

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
            monster.getModel().setTranslateY(mType == 0 ? ((map.getMatrix()[monster.getRow()][monster.getCol()] == '*') ? 10 : 0) : 0);
            monster.getModel().setTranslateX(5 + monster.getCol() * 10);
            monster.getModel().setTranslateZ(5 + monster.getRow() * 10);

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
}

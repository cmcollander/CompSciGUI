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
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;

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
                if (map.getMatrix()[row][col] == '.' && !map.isDock(row, col)) {
                    continue;
                }

                Box land = new Box(10, 10, 10);
                // Translation
                land.setTranslateY(5);
                land.setTranslateX(5 + col * 10);
                land.setTranslateZ(5 + row * 10);
                land.setMaterial(landMaterial);
                landGroup.getChildren().add(land);
            }
        }
        world.getChildren().add(landGroup);
        landGroup.setVisible(true);
    }

    private void buildShips() {
        final PhongMaterial cargoShipMaterial = new PhongMaterial();
        cargoShipMaterial.setDiffuseColor(Color.RED);
        cargoShipMaterial.setSpecularColor(Color.PINK);

        final PhongMaterial containerShipMaterial = new PhongMaterial();
        containerShipMaterial.setDiffuseColor(Color.BROWN);
        containerShipMaterial.setSpecularColor(Color.CHOCOLATE);

        final PhongMaterial tankerShipMaterial = new PhongMaterial();
        tankerShipMaterial.setDiffuseColor(Color.DARKSLATEGRAY);
        tankerShipMaterial.setSpecularColor(Color.LIGHTSLATEGRAY);

        for (CargoShip ship : map.getShips()) {
            int shipType = 0; // CargoShip
            if (ship instanceof ContainerShip) {
                shipType = 1;
            }
            if (ship instanceof OilTanker) {
                shipType = 2;
            }

            Cylinder shipModel = new Cylinder(5, 20);

            // Translation
            shipModel.setTranslateY(10);
            shipModel.setTranslateX(5 + ship.getCol() * 10);
            shipModel.setTranslateZ(5 + ship.getRow() * 10);
            switch (shipType) {
                case 0:
                    shipModel.setMaterial(cargoShipMaterial);
                    break;
                case 1:
                    shipModel.setMaterial(containerShipMaterial);
                    break;
                case 2:
                    shipModel.setMaterial(tankerShipMaterial);
                    break;
            }
            shipGroup.getChildren().add(shipModel);
        }
        world.getChildren().add(shipGroup);
        shipGroup.setVisible(true);
    }

    private void buildMonsters() {
        final PhongMaterial krakenMaterial = new PhongMaterial();
        krakenMaterial.setDiffuseColor(Color.RED);
        krakenMaterial.setSpecularColor(Color.PINK);

        final PhongMaterial leviathanMaterial = new PhongMaterial();
        leviathanMaterial.setDiffuseColor(Color.BROWN);
        leviathanMaterial.setSpecularColor(Color.CHOCOLATE);

        final PhongMaterial serpentMaterial = new PhongMaterial();
        serpentMaterial.setDiffuseColor(Color.DARKSLATEGRAY);
        serpentMaterial.setSpecularColor(Color.LIGHTSLATEGRAY);

        final PhongMaterial godzillaMaterial = new PhongMaterial();
        godzillaMaterial.setDiffuseColor(Color.DARKGREEN);
        godzillaMaterial.setSpecularColor(Color.GREEN);

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

            Sphere monsterModel = new Sphere(mType == 0 ? 10 : 5);

            // Translation
            monsterModel.setTranslateY(mType == 0 ? 10 + ((map.getMatrix()[monster.getRow()][monster.getCol()]=='*')?10:0) : 5);
            monsterModel.setTranslateX(5 + monster.getCol() * 10);
            monsterModel.setTranslateZ(5 + monster.getRow() * 10);
            switch (mType) {
                case 0:
                    monsterModel.setMaterial(godzillaMaterial);
                    break;
                case 1:
                    monsterModel.setMaterial(krakenMaterial);
                    break;
                case 2:
                    monsterModel.setMaterial(leviathanMaterial);
                    break;
                case 3:
                    monsterModel.setMaterial(serpentMaterial);
                    break;
            }
            monsterGroup.getChildren().add(monsterModel);
        }
        world.getChildren().add(monsterGroup);
        monsterGroup.setVisible(true);
    }

    private void buildDocks() {
        final PhongMaterial dockMaterial = new PhongMaterial();
        dockMaterial.setDiffuseColor(Color.RED);
        dockMaterial.setSpecularColor(Color.PINK);

        final PhongMaterial craneMaterial = new PhongMaterial();
        craneMaterial.setDiffuseColor(Color.BROWN);
        craneMaterial.setSpecularColor(Color.CHOCOLATE);

        final PhongMaterial pierMaterial = new PhongMaterial();
        pierMaterial.setDiffuseColor(Color.DARKSLATEGRAY);
        pierMaterial.setSpecularColor(Color.LIGHTSLATEGRAY);

        for (Dock dock : map.getPort().getDocks()) {
            int dockType = 0; // Dock
            if (dock instanceof Crane) {
                dockType = 1;
            }
            if (dock instanceof Pier) {
                dockType = 2;
            }

            Box dockModel = new Box(10, 10, 10);

            // Translation
            dockModel.setTranslateY(15);
            dockModel.setTranslateX(5 + dock.getCol() * 10);
            dockModel.setTranslateZ(5 + dock.getRow() * 10);
            switch (dockType) {
                case 0:
                    dockModel.setMaterial(dockMaterial);
                    break;
                case 1:
                    dockModel.setMaterial(craneMaterial);
                    break;
                case 2:
                    dockModel.setMaterial(pierMaterial);
                    break;
            }
            dockGroup.getChildren().add(dockModel);
        }
        world.getChildren().add(dockGroup);
        dockGroup.setVisible(true);
    }
}

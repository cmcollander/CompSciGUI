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

public class PortSimulation {

    private Map map;
    private Stage stage = new Stage();
    private Group root = new Group();
    final PerspectiveCamera camera = new PerspectiveCamera(true);

    final Xform world = new Xform();
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    final Xform oceanGroup = new Xform();
    final Xform landGroup = new Xform();
    final Xform shipGroup = new Xform();

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

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

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

        world.setTranslateX(-530 / 2);
        world.setTranslateZ(-350 / 2);

        Scene scene = new Scene(root, 1024, 768, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.SKYBLUE);
        handleKeyboard(scene, world);
        handleMouse(scene, world);

        stage.setTitle("3D Port Simulation");
        stage.setScene(scene);
        stage.setMaximized(true);
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

        final Box ocean = new Box(530, 1, 350);
        ocean.setTranslateX(530 / 2);
        ocean.setTranslateZ(350 / 2);
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

        for (int row = 1; row < 35; row++) {
            for (int col = 52; col > -1; col--) {
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
}

package main;

import javafx.scene.PerspectiveCamera;

public class FPSCamera extends PerspectiveCamera{
    
    public double CAMERA_INITIAL_DISTANCE = -450;
    public double CAMERA_INITIAL_X_ANGLE = 30.0;
    public double CAMERA_INITIAL_Y_ANGLE = 120.0;
    public double CAMERA_NEAR_CLIP = 0.1;
    public double CAMERA_FAR_CLIP = 10000.0;

    FPSCamera(boolean b) {
        super(b);
    }
    
}

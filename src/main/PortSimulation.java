/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Chris
 */
public class PortSimulation {
    private Map map;
    
    public PortSimulation(Map map) {
        this.map = map;
    }
    
    public void run() {
        Group root = new Group();
        Stage stage = new Stage();
        stage.setTitle("3D Port Simulation");
        stage.setScene(new Scene(root,800,600,Color.BLACK));
        stage.setMaximized(true);
        stage.show();
    }
}

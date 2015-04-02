/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

import java.io.File;
import java.net.URL;
import javafx.scene.media.AudioClip;

/**
 *
 */
public class SoundManager {

    public static void growl() throws Exception {
        URL resource = new File("media\\growl.mp3").toURI().toURL();
        AudioClip growlSound = new AudioClip(resource.toString());
        growlSound.play();
    }
}

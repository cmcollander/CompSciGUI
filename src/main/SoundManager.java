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

    public static void growl(SeaMonster monster) throws Exception {
        File file;
        if (monster instanceof Godzilla) {
            file = new File("media\\godzillaSound.mp3");
        } else if (monster instanceof Leviathan) {
            file = new File("media\\leviathanSound.mp3");
        } else if (monster instanceof Kraken) {
            file = new File("media\\krakenSound.mp3");
        } else {
            file = new File("media\\seaSerpentSound.mp3");
        }

        URL resource = file.toURI().toURL();
        AudioClip growlSound = new AudioClip(resource.toString());
        growlSound.play();
    }
}

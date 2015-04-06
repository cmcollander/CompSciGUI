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
            file = new File("media\\sounds\\godzillaSound.mp3");
        } else if (monster instanceof Leviathan) {
            file = new File("media\\sounds\\leviathanSound.mp3");
        } else if (monster instanceof Kraken) {
            file = new File("media\\sounds\\krakenSound.mp3");
        } else {
            file = new File("media\\sounds\\seaSerpentSound.mp3");
        }

        URL resource = file.toURI().toURL();
        AudioClip growlSound = new AudioClip(resource.toString());
        growlSound.play();
    }
}

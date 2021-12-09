package test;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/** This class is used to load and play audio
 * @author Sabrina Felicia Kusumawati
 * @version 0.2
 */

public class AudioPlayer {
    /**
     * Indicates if loaded audio is muted / unmuted
     */
    public boolean muteAudio;
    /**
     * Contains the audio clip specified by fileName in AudioPlayer
     */
    private Clip clip;

    /** loads the audio file that is going to be played
     * @param fileName it contains the file path for the audio that is going to be played
     */
    public AudioPlayer(String fileName) {
        // specify the sound to play
        // (assuming the sound can be played by the audio system)
        // from a wave File
        try {
            File file = new File(fileName);
            if (file.exists()) {
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                // load the sound into memory (a Clip)
                clip = AudioSystem.getClip();
                clip.open(sound);
            }
            else {
                throw new RuntimeException("Sound: file not found: " + fileName);
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Malformed URL: " + e);
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Unsupported Audio File: " + e);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Input/Output Error: " + e);
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Line Unavailable Exception Error: " + e);
        }

        // play, stop, loop the sound clip
    }

    /**
     * This method is used to Play the audio file loaded by AudioPlayer
     */
    public void play(){
        if (!muteAudio){
            clip.setFramePosition(0);  // Must always rewind!
            clip.start();
        }
    }

    /**
     * This method is used to loop the audio loaded by AudioPlayer
     */
    public void loop(){
        if (!muteAudio)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * This method is used to stop the audio loaded by AudioPlayer
     */
    public void stop(){
        clip.stop();
    }
}
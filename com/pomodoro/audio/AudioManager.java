package com.pomodoro.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.Player;
import javazoom.jl.decoder.JavaLayerException;

public class AudioManager {

    private Player backgroundMusicPlayer;
    private Thread backgroundMusicThread;
    private String customAlarmPath = null;
    private boolean isMusicEnabled = true;
    private String currentLofiTrack = "lofi1.mp3";

    public boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    public String getCurrentLofiTrack() {
        return currentLofiTrack;
    }

    public String getCustomAlarmPath() {
        return customAlarmPath;
    }

    public AudioManager() {
        System.out.println("AudioManager: Initializing for MP3 playback...");
    }

    public void playBackgroundMusic() {
        if (!isMusicEnabled || (backgroundMusicThread != null && backgroundMusicThread.isAlive())) {
            return;
        }

        backgroundMusicThread = new Thread(() -> {
            try {
                String resourcePath = "/resources/" + currentLofiTrack;
                var musicStream = getClass().getResourceAsStream(resourcePath);
                if (musicStream != null) {
                    System.out.println("AudioManager: Playing " + currentLofiTrack + "...");
                    this.backgroundMusicPlayer = new Player(musicStream);
                    this.backgroundMusicPlayer.play();
                } else {
                    System.out.println("❌ AudioManager: Could not find track: " + resourcePath);
                }
            } catch (JavaLayerException e) {
            }
        });
        backgroundMusicThread.start();
    }

    public void stopBackgroundMusic() {
        if (this.backgroundMusicPlayer != null) {
            System.out.println("AudioManager: Stopping music.");
            this.backgroundMusicPlayer.close();
        }
        if (backgroundMusicThread != null) {
            backgroundMusicThread.interrupt();
        }
    }

    public void setMusicEnabled(boolean enabled) {
        this.isMusicEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        }
    }

    public void setCustomAlarmSound(String filePath) {
        this.customAlarmPath = filePath;
    }

    public void setBackgroundMusicTrack(String trackName) {
        this.currentLofiTrack = trackName;
        stopBackgroundMusic();
    }

    public void playAlarmSound() {
        if (customAlarmPath != null && new File(customAlarmPath).exists()) {
            new Thread(() -> {
                try (FileInputStream fis = new FileInputStream(customAlarmPath)) {
                    Player alarmPlayer = new Player(fis);
                    alarmPlayer.play();
                } catch (Exception e) {
                    System.err.println("Error playing custom alarm sound.");
                }
            }).start();
        } else {
            playSynthesizedTone();
        }
    }

    private void playSynthesizedTone() {
        try {
            byte[] buf = new byte[1];
            AudioFormat af = new AudioFormat(8000f, 8, 1, true, false);
            SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
            for (int i = 0; i < 300 * 8; i++) {
                double angle = i / (8000f / 440) * 2.0 * Math.PI;
                buf[0] = (byte) (Math.sin(angle) * 100);
                sdl.write(buf, 0, 1);
            }
            sdl.drain();
            sdl.stop();
            sdl.close();
        } catch (LineUnavailableException e) {
            System.err.println("Could not play synthesized alarm sound.");
        }
    }
}

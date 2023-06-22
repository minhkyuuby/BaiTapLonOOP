package com.dbdc.game.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private static final float DEFAULT_VOLUME = 1.0f;
    private Music backgroundMusic;
    private Sound soundEffect;
    public static Sound click;

    public AudioManager() {
        // Initialize your audio resources here
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mp3/Ambient 1.mp3"));
        soundEffect = Gdx.audio.newSound(Gdx.files.internal("music/mp3/Death.mp3"));
        click = Gdx.audio.newSound(Gdx.files.internal("music/mp3/click.wav"));
    }

    public void playBackgroundMusic() {
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(DEFAULT_VOLUME);
            backgroundMusic.play();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
    }

    public void playSoundEffect() {
        soundEffect.play(DEFAULT_VOLUME);
    }

    public void playSoundEffect(Sound sfx) {
        sfx.play(DEFAULT_VOLUME);
    }

    public void dispose() {
        // Dispose of your audio resources here
        backgroundMusic.dispose();
        soundEffect.dispose();
        click.dispose();
    }
}

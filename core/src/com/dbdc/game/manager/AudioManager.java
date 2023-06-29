package com.dbdc.game.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private static final float DEFAULT_VOLUME = 1.0f;
    private Music backgroundMusic;
    private Music gameplayMusic;
    private Sound soundEffect;
    public static Sound click;

    public AudioManager() {
        // Initialize your audio resources here
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mp3/Nujabes.mp3"));
        gameplayMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mp3/8 Bit Space Groove.mp3"));
        soundEffect = Gdx.audio.newSound(Gdx.files.internal("music/mp3/Death.mp3"));
        click = Gdx.audio.newSound(Gdx.files.internal("music/mp3/click.wav"));
    }

    public void playGameplayMusic() {
        if (!gameplayMusic.isPlaying()) {
            gameplayMusic.setLooping(true);
            gameplayMusic.setVolume(DEFAULT_VOLUME);
            gameplayMusic.play();
        }
    }
    public void stopGameplayMusic() {
        if (gameplayMusic.isPlaying()) {
            gameplayMusic.stop();
        }
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

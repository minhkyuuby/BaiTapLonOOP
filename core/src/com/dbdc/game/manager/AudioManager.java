package com.dbdc.game.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.List;

public class AudioManager {
    private static final float DEFAULT_VOLUME = 1f;
    private Music backgroundMusic;
    private Music gameplayMusic;
    private List<Sound> soundEffects;
    public static Sound click;



    public AudioManager() {
        // Initialize your audio resources here
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mp3/Nujabes.mp3"));
        gameplayMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mp3/8 Bit Space Groove.mp3"));
        soundEffects = new ArrayList<>();
        soundEffects.add(Gdx.audio.newSound(Gdx.files.internal("music/mp3/sfx/Hit.mp3")));
        soundEffects.add(Gdx.audio.newSound(Gdx.files.internal("music/mp3/sfx/OOF.mp3")));
        soundEffects.add(Gdx.audio.newSound(Gdx.files.internal("music/mp3/sfx/WritingSfx.mp3")));
        soundEffects.add(Gdx.audio.newSound(Gdx.files.internal("music/mp3/sfx/Win.mp3")));
        soundEffects.add(Gdx.audio.newSound(Gdx.files.internal("music/mp3/sfx/Lose.mp3")));
        click = Gdx.audio.newSound(Gdx.files.internal("music/mp3/click.wav"));
    }

    public void playGameplayMusic() {
        if (!gameplayMusic.isPlaying()) {
            gameplayMusic.setLooping(true);
            gameplayMusic.setVolume(DEFAULT_VOLUME/3);
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
            backgroundMusic.setVolume(DEFAULT_VOLUME/3);
            backgroundMusic.play();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
    }

    public Sound getSoundEffect(int id) {
        return soundEffects.get(id);
    }

    public void playSoundEffect(Sound sfx) {
        sfx.play(DEFAULT_VOLUME);
    }

    public void playSoundEffect(int id) {
        getSoundEffect(id).play(DEFAULT_VOLUME);
    }
    public void dispose() {
        // Dispose of your audio resources here
        backgroundMusic.dispose();
        click.dispose();
    }
}

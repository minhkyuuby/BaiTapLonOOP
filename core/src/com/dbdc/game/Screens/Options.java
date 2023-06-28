package com.dbdc.game.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dbdc.game.GameClass;

public class Options implements Screen {
    protected GameClass game;
    private  Stage stage;
    private Slider volumeSlider;
    private ScreenViewport screenViewport;

    public Options(GameClass game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

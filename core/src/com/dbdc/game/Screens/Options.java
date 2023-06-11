package com.dbdc.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dbdc.game.GameClass;

public class Options extends DefaultScreen {
    private  Stage stage;
    private Slider volumeSlider;
    private ScreenViewport screenViewport;

    public Options(GameClass game) {
        super(game);
        stage = new Stage(new ScreenViewport());
        Label music;
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {

    }

    @Override
    public void dispose() {

    }
}

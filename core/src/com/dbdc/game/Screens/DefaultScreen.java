package com.dbdc.game.Screens;

import com.badlogic.gdx.Screen;
import com.dbdc.game.GameClass;

public abstract class DefaultScreen implements Screen {
    protected GameClass game;

    public DefaultScreen(GameClass game) {
        this.game = game;
    }

    public abstract void update(float delta);
    public abstract void draw(float delta);
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        draw(delta);
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

package com.dbdc.game.Screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dbdc.game.GameClass;
import com.dbdc.game.manager.Assets;


public class MainMenu implements Screen {
    protected GameClass game;
    private Stage stage;
    private LevelSelecting levelSelecting = new LevelSelecting(game);
    private Options options = new Options(game);
//    private GamePlay gamePlay = new GamePlay(game);
    private AboutUs aboutUs = new AboutUs(game);
    private Button ngButton, levelButton, optionsButton, exitButton, auButton;
    private Table table;
    private Texture bgTexture = new Texture(Assets.bg);

    public MainMenu(final GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        game.audioManager.playBackgroundMusic();

        stage = new Stage(new ScreenViewport());

        Texture playBtnTexture = new Texture(Gdx.files.internal(Assets.playbtn));
        ngButton = new ImageButton(new TextureRegionDrawable(playBtnTexture));
        Texture levelBtnTexture = new Texture(Gdx.files.internal(Assets.levelbtn));
        levelButton = new ImageButton(new TextureRegionDrawable(levelBtnTexture));
        Texture optionBtnTexture = new Texture(Gdx.files.internal(Assets.optionbtn));
        optionsButton = new ImageButton(new TextureRegionDrawable(optionBtnTexture));
        Texture exitBtnTexture = new Texture(Gdx.files.internal(Assets.exitbtn));
        exitButton = new ImageButton(new TextureRegionDrawable(exitBtnTexture));
        Texture auBtnTexture = new Texture(Gdx.files.internal(Assets.aubtn));
        auButton = new ImageButton(new TextureRegionDrawable(auBtnTexture));

        Image bgImage = new Image(bgTexture);
        stage.addActor(bgImage);

        table = new Table();
        table.setFillParent(true);
        table.defaults().pad(0f);
        table.pack();

        table.row();
        table.add(ngButton);
        table.row();
        table.add(levelButton);
        table.row();
        table.add(optionsButton).padTop(10f);
        table.row();
        table.add(exitButton).padTop(5f);
        table.row();
        table.add(auButton).padTop(5f);
        table.setPosition(0,-80);
        table.align(Align.center);

//        gamePlay = new GamePlay(game);
        levelSelecting = new LevelSelecting(game);
        options = new Options(game);
        aboutUs = new AboutUs(game);

        ngButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GamePlay(game));
            };
        });
        levelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {game.setScreen(levelSelecting);
            };
        });
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(options);
            };
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            };
        });
        auButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(
                        Actions.fadeOut(0.3f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                game.setScreen(aboutUs);
                            }
                        })
                ));
            }
        });

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
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
        game.audioManager.stopBackgroundMusic();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}

package com.dbdc.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dbdc.game.GameClass;
import com.dbdc.game.manager.Assets;
import com.dbdc.game.manager.AudioManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class GameOver implements Screen {
    protected GameClass game;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private Button menu, playagain;
    private Texture bgTexture;
    private Image bg;
    private Table table;
    public GameOver(GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        atlas = new TextureAtlas("screen/gameoverskin.pack");
        skin = new Skin(atlas);
        table = new Table();

        bgTexture = new Texture(Assets.overbg);
        bg = new Image(bgTexture);
        stage.addActor(bg);

        ImageButton.ImageButtonStyle menuStyle = new ImageButton.ImageButtonStyle();
        menuStyle.up = skin.getDrawable("menu_up");
        menuStyle.down = skin.getDrawable("menu_down");
        menu = new ImageButton(menuStyle);

        ImageButton.ImageButtonStyle pgStyle = new ImageButton.ImageButtonStyle();
        pgStyle.up = skin.getDrawable("playagain_up");
        pgStyle.down = skin.getDrawable("playagain_down");
        playagain = new ImageButton(pgStyle);

        table.setFillParent(true);
        table.defaults().pad(0f);
        table.pack();

        table.row();
        table.add(playagain).padTop(275f);
        table.row();
        table.add(menu);

        playagain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.playSoundEffect(AudioManager.click);
                switchScreen(game,new GamePlay(game));
            };
        });
        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.playSoundEffect(AudioManager.click);
                switchScreen(game,new MainMenu(game));
            };
        });

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    public void switchScreen(final Game game, final Screen newScreen){
        stage.getRoot().getColor().a = 1;
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(fadeOut(0.5f));
        sequenceAction.addAction(run(new Runnable() {
            @Override
            public void run() {
                game.setScreen(newScreen);
            }
        }));
        stage.getRoot().addAction(sequenceAction);
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        skin.dispose();
    }
}

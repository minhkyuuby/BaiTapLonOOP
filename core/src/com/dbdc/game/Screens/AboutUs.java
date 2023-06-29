package com.dbdc.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dbdc.game.GameClass;
import com.dbdc.game.manager.Assets;
import com.dbdc.game.manager.AudioManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class AboutUs implements Screen {
    GameClass game;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Texture auscreenTexture = new Texture(Assets.auscreen);
    private Image auImage;
    private Button backButton;

    public AboutUs(final GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        atlas = new TextureAtlas("screen/levelskin.pack");
        skin = new Skin(atlas);
        final MainMenu menu = new MainMenu(game);

        auImage = new Image(auscreenTexture);

        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(fadeIn(0.5f));

        ImageButton.ImageButtonStyle backStyle = new ImageButton.ImageButtonStyle();
        backStyle.up = skin.getDrawable("back_up");
        backStyle.down = skin.getDrawable("back_down");
        backButton = new ImageButton(backStyle);

        backButton.setPosition(50,600);

        stage.addActor(auImage);
        stage.addActor(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.playSoundEffect(AudioManager.click);
                switchScreen(game,menu);
            }
        });

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
    }
}

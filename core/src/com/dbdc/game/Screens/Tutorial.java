package com.dbdc.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dbdc.game.GameClass;
import com.dbdc.game.manager.Assets;
import com.dbdc.game.manager.AudioManager;

public class Tutorial implements Screen {
    GameClass game;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Texture tutuorialTexture = new Texture(Assets.tutorial);
    private Button backButton;

    public Tutorial(final GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        atlas = new TextureAtlas("screen/levelskin.pack");
        skin = new Skin(atlas);
        final MainMenu menu = new MainMenu(game);

        Image auImage = new Image(tutuorialTexture);

        stage.addAction(Actions.sequence(Actions.alpha(0,0f),Actions.fadeIn(0.3f)));

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
                stage.addAction(Actions.sequence(
                        Actions.fadeOut(0.3f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                game.setScreen(menu);
                            }
                        })
                ));
            }
        });

        Gdx.input.setInputProcessor(stage);
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

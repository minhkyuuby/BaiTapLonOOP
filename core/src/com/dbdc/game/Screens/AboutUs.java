package com.dbdc.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.dbdc.game.GameClass;
import com.dbdc.game.manager.Assets;

public class AboutUs implements Screen {
    GameClass game;
    private Stage stage;
    private Texture auscreenTexture = new Texture(Assets.auscreen);
    private Button backButton;

    public AboutUs(final GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        final MainMenu menu = new MainMenu(game);

        Image auImage = new Image(auscreenTexture);

        stage.addAction(Actions.sequence(Actions.alpha(0,0f),Actions.fadeIn(0.3f)));
        stage.addActor(auImage);

        Texture backBtnTexture = new Texture(Gdx.files.internal(Assets.backbtn));
        backButton = new ImageButton(new TextureRegionDrawable(backBtnTexture));

        backButton.setPosition(40,600);

        stage.addActor(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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

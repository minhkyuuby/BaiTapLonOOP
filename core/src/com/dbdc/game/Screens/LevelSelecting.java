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

public class LevelSelecting implements Screen {
    protected GameClass game;
    private Stage stage;
    private Texture levelbgTexture = new Texture(Assets.levelbg);
    private Button gt1Button;
    private Button gt2Button;
    private Button gt3Button;
    private Button dsButton;
    private Button vlButton;
    private Button backButton;

    public LevelSelecting(GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        final MainMenu menu = new MainMenu(game);

        Image levelImage = new Image(levelbgTexture);

        stage.addAction(Actions.sequence(Actions.alpha(0,0f),Actions.fadeIn(0.3f)));

        Texture gt1Texture = new Texture(Gdx.files.internal(Assets.gt1));
        gt1Button = new ImageButton(new TextureRegionDrawable(gt1Texture));
        Texture gt2Texture = new Texture(Gdx.files.internal(Assets.gt2));
        gt2Button = new ImageButton(new TextureRegionDrawable(gt2Texture));
        Texture gt3Texture = new Texture(Gdx.files.internal(Assets.gt3));
        gt3Button = new ImageButton(new TextureRegionDrawable(gt3Texture));
        Texture dsTexture = new Texture(Gdx.files.internal(Assets.daiso));
        dsButton = new ImageButton(new TextureRegionDrawable(dsTexture));
        Texture vlTexture = new Texture(Gdx.files.internal(Assets.vl));
        vlButton = new ImageButton(new TextureRegionDrawable(vlTexture));

        gt1Button.setPosition(35,125);
        dsButton.setPosition(300,230);
        gt2Button.setPosition(545,115);
        vlButton.setPosition(780,235);
        gt3Button.setPosition(1030,250);

        Texture backBtnTexture = new Texture(Gdx.files.internal(Assets.backbtn));
        backButton = new ImageButton(new TextureRegionDrawable(backBtnTexture));

        backButton.setPosition(50,600);
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

        stage.addActor(levelImage);
        stage.addActor(gt1Button);
        stage.addActor(gt2Button);
        stage.addActor(gt3Button);
        stage.addActor(dsButton);
        stage.addActor(vlButton);
        stage.addActor(backButton);

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

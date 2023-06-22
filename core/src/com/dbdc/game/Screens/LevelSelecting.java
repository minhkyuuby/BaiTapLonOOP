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

public class LevelSelecting implements Screen {
    protected GameClass game;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
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
        atlas = new TextureAtlas("screen/levelskin.pack");
        skin = new Skin(atlas);
        final MainMenu menu = new MainMenu(game);

        Image levelImage = new Image(levelbgTexture);

        stage.addAction(Actions.sequence(Actions.alpha(0,0f),Actions.fadeIn(0.3f)));

        ImageButton.ImageButtonStyle gt1Style = new ImageButton.ImageButtonStyle();
        gt1Style.up = skin.getDrawable("gt1_up");
        gt1Style.down = skin.getDrawable("gt1_down");
        gt1Button = new ImageButton(gt1Style);

        ImageButton.ImageButtonStyle gt2Style = new ImageButton.ImageButtonStyle();
        gt2Style.up = skin.getDrawable("gt2_up");
        gt2Style.down = skin.getDrawable("gt2_down");
        gt2Button = new ImageButton(gt2Style);

        ImageButton.ImageButtonStyle gt3Style = new ImageButton.ImageButtonStyle();
        gt3Style.up = skin.getDrawable("gt3_up");
        gt3Style.down = skin.getDrawable("gt3_down");
        gt3Button = new ImageButton(gt3Style);

        ImageButton.ImageButtonStyle dsStyle = new ImageButton.ImageButtonStyle();
        dsStyle.up = skin.getDrawable("daiso_up");
        dsStyle.down = skin.getDrawable("daiso_down");
        dsButton = new ImageButton(dsStyle);

        ImageButton.ImageButtonStyle vlStyle = new ImageButton.ImageButtonStyle();
        vlStyle.up = skin.getDrawable("vl_up");
        vlStyle.down = skin.getDrawable("vl_down");
        vlButton = new ImageButton(vlStyle);

        ImageButton.ImageButtonStyle backStyle = new ImageButton.ImageButtonStyle();
        backStyle.up = skin.getDrawable("back_up");
        backStyle.down = skin.getDrawable("back_down");
        backButton = new ImageButton(backStyle);

        gt1Button.setPosition(35,180);
        dsButton.setPosition(300,260);
        gt2Button.setPosition(545,177);
        vlButton.setPosition(780,295);
        gt3Button.setPosition(1035,290);
        backButton.setPosition(50,600);

        gt1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.playSoundEffect(AudioManager.click);
            }
        });

        gt2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.playSoundEffect(AudioManager.click);
            }
        });

        gt3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.playSoundEffect(AudioManager.click);
            }
        });

        dsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.playSoundEffect(AudioManager.click);
            }
        });

        vlButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.playSoundEffect(AudioManager.click);
            }
        });

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

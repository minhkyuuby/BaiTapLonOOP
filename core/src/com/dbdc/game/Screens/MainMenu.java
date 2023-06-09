    package com.dbdc.game.Screens;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.Screen;
    import com.badlogic.gdx.graphics.GL20;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.TextureAtlas;
    import com.badlogic.gdx.scenes.scene2d.InputEvent;
    import com.badlogic.gdx.scenes.scene2d.Stage;
    import com.badlogic.gdx.scenes.scene2d.actions.Actions;
    import com.badlogic.gdx.scenes.scene2d.ui.*;
    import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
    import com.badlogic.gdx.utils.Align;
    import com.badlogic.gdx.utils.viewport.ScreenViewport;
    import com.dbdc.game.GameClass;
    import com.dbdc.game.GameScreen;
    import com.dbdc.game.manager.Assets;
    import com.dbdc.game.manager.AudioManager;


    public class MainMenu implements Screen {
        protected GameClass game;
        private Stage stage;
        private TextureAtlas atlas;
        private Skin skin;
        private Button levelButton, optionsButton, exitButton, auButton, tutorialButton;
        private Table table;
        private Texture bgTexture = new Texture(Assets.bg);

        public MainMenu(final GameClass game) {
            this.game = game;
        }

        @Override
        public void show() {
            game.audioManager.playBackgroundMusic();

            stage = new Stage(new ScreenViewport());
            atlas = new TextureAtlas("screen/menuskin.pack");
            skin = new Skin(atlas);

            ImageButton.ImageButtonStyle levelStyle = new ImageButton.ImageButtonStyle();
            levelStyle.up = skin.getDrawable("level_up");
            levelStyle.down = skin.getDrawable("level_down");
            levelButton = new ImageButton(levelStyle);

            ImageButton.ImageButtonStyle optionStyle = new ImageButton.ImageButtonStyle();
            optionStyle.up = skin.getDrawable("option_up");
            optionStyle.down = skin.getDrawable("option_down");
            optionsButton = new ImageButton(optionStyle);

            ImageButton.ImageButtonStyle exitStyle = new ImageButton.ImageButtonStyle();
            exitStyle.up = skin.getDrawable("exit_up");
            exitStyle.down = skin.getDrawable("exit_down");
            exitButton = new ImageButton(exitStyle);

            ImageButton.ImageButtonStyle auStyle = new ImageButton.ImageButtonStyle();
            auStyle.up = skin.getDrawable("aboutus_up");
            auStyle.down = skin.getDrawable("aboutus_down");
            auButton = new ImageButton(auStyle);

            ImageButton.ImageButtonStyle tutorialStyle = new ImageButton.ImageButtonStyle();
            tutorialStyle.up = skin.getDrawable("tutorial_up");
            tutorialStyle.down = skin.getDrawable("tutorial_down");
            tutorialButton = new ImageButton(tutorialStyle);

            Image bgImage = new Image(bgTexture);
            stage.addActor(bgImage);

            table = new Table();
            table.setFillParent(true);
            table.defaults().pad(0f);
            table.pack();

            table.row();
            table.add(levelButton);
            table.row();
            table.add(optionsButton).padTop(10f);
            table.row();
            table.add(tutorialButton).padTop(10f);
            table.row();
            table.add(exitButton).padTop(8f);
            table.row();
            table.add(auButton).padTop(10f);
            table.setPosition(0,-80);
            table.align(Align.center);

    //        MainMenu menu = this;
            levelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.audioManager.playSoundEffect(AudioManager.click);
                    stage.addAction(Actions.sequence(
                            Actions.fadeOut(0.3f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    game.setScreen(game.getScreen(GameScreen.Levels));
                                }
                            })
                    ));
                }
            });
            optionsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.audioManager.playSoundEffect(AudioManager.click);
    //                game.setScreen(options);
                };
            });
            exitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.audioManager.playSoundEffect(AudioManager.click);
                    Gdx.app.exit();
                };
            });
            tutorialButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.audioManager.playSoundEffect(AudioManager.click);
                    stage.addAction(Actions.sequence(
                            Actions.fadeOut(0.3f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    game.setScreen(game.getScreen(GameScreen.Tutorial));
                                }
                            })
                    ));
                }
            });
            auButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.audioManager.playSoundEffect(AudioManager.click);
                    stage.addAction(Actions.sequence(
                            Actions.fadeOut(0.3f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    game.setScreen(game.getScreen(GameScreen.AboutUs));
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
    //        dispose();
        }

        @Override
        public void dispose() {
            stage.dispose();
            atlas.dispose();
            skin.dispose();
        }

    }

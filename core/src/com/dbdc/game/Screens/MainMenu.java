package com.dbdc.game.Screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.dbdc.game.GameClass;


public class MainMenu extends DefaultScreen {
    private Stage stage;
    private LevelSelecting levelSelecting = new LevelSelecting(game);
    private Options options = new Options(game);
    private GamePlay gamePlay = new GamePlay(game);

    private TextButton ngButton, levelButton, optionsButton, exitButton;
    private Label title;
    private Table table;

    private BitmapFont font;

    private Texture bg;


    public MainMenu(final GameClass game) {
        super(game);
        stage = new Stage();

        AssetManager manager = game.getManager();

        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter mySmallFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        mySmallFont.fontFileName = "fonts/futureFont.ttf";
        mySmallFont.fontParameters.size = 10;
        manager.load("fonts/futureFont.ttf", BitmapFont.class, mySmallFont);
        manager.finishLoading();

        font = manager.get("fonts/futureFont.ttf",BitmapFont.class);

        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font = font;
        ls.fontColor = Color.CHARTREUSE;
        title = new Label("DBDC", ls);
        title.getColor().a = 1.0f;

        TextButton.TextButtonStyle tbs =  new TextButton.TextButtonStyle();
        tbs.font = font;
        tbs.downFontColor = Color.RED;
        tbs.overFontColor = Color.GREEN;
        tbs.fontColor 	  = Color.BLUE;

        ngButton = new TextButton("NEW GAME", tbs);
        levelButton = new TextButton("LEVEL", tbs);
        optionsButton = new TextButton("OPTIONS", tbs);
        exitButton = new TextButton("EXIT", tbs);

        table = new Table();
        table.setFillParent(true);
        table.pack();

        table.row();
        table.add(title).padTop(30f).expand().colspan(2);
        table.row();
        table.add(ngButton).padTop(30f).expand().colspan(2);
        table.row();
        table.add(levelButton).padTop(30f).expand().colspan(2);
        table.row();
        table.add(optionsButton).padTop(30f).expand().colspan(2);
        table.row();
        table.add(exitButton).padTop(30f).expand().colspan(2);

        ngButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(gamePlay);
            };
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            };
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(options);
            };
        });

        levelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {game.setScreen(levelSelecting);
            };
        });

        stage.addActor(table);

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}

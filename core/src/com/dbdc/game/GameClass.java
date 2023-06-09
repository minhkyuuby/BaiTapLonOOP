package com.dbdc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.dbdc.game.Screens.MainMenu;

public class GameClass extends Game {
//	SpriteBatch batch;
//	Texture img;
private FPSLogger fps;

	@Override
	public void create () {
//		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
		setScreen(new MainMenu(this));

		fps = new FPSLogger();
	}

	@Override
	public void render () {

	}

}

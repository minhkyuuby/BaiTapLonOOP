package com.dbdc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.dbdc.game.Screens.MainMenu;

public class GameClass extends Game {
	private AssetManager manager;
	public static int HEIGHT;
	public static int WIDTH;

	@Override
	public void create () {
		
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();

		manager = new AssetManager();

		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public void dispose() {
		manager.dispose();
	}

	public AssetManager getManager() {
		return manager;
	}

}

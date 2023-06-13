package com.dbdc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dbdc.game.Screens.MainMenu;

public class GameClass extends Game {
	public static int HEIGHT;
	public static int WIDTH;

	@Override
	public void create () {
		
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();

		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public void dispose() {

	}

}

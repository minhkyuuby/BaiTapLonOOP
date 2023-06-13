package com.dbdc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.dbdc.game.Screens.GamePlay;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dbdc.game.Screens.MainMenu;
import com.kotcrab.vis.ui.VisUI;

public class GameClass extends Game {
	public static int HEIGHT;
	public static int WIDTH;

	@Override
	public void create () {
		Bullet.init();
		if (!VisUI.isLoaded())
			VisUI.load();
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();

		this.setScreen(new MainMenu(this));
//		this.setScreen(new GamePlay(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public void dispose() {

	}

}

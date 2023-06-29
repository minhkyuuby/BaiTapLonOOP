package com.dbdc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.dbdc.game.Screens.GameOver;
import com.dbdc.game.Screens.MainMenu;
import com.dbdc.game.manager.AudioManager;
import com.kotcrab.vis.ui.VisUI;

public class GameClass extends Game {
	public static int HEIGHT;
	public static int WIDTH;
	public AudioManager audioManager;
	@Override
	public void create () {
		Bullet.init();
		if (!VisUI.isLoaded())
			VisUI.load();
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		audioManager = new AudioManager();
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public void dispose() {
		audioManager.dispose();
	}
}

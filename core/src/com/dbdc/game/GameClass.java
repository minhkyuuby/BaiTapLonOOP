package com.dbdc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.dbdc.game.Screens.AboutUs;
import com.dbdc.game.Screens.Tutorial;
import com.dbdc.game.Screens.GamePlay;
import com.dbdc.game.Screens.LevelSelecting;
import com.dbdc.game.Screens.MainMenu;
import com.dbdc.game.manager.AudioManager;
import com.kotcrab.vis.ui.VisUI;
import net.mgsx.gltf.scene3d.scene.Scene;

public class GameClass extends Game {
	public static int HEIGHT;
	public static int WIDTH;
	public AudioManager audioManager;

	public Screen[] gameScreens;
	@Override
	public void create () {
		Bullet.init();
		if (!VisUI.isLoaded())
			VisUI.load();
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		audioManager = new AudioManager();
		gameScreens = new Screen[5];
		gameScreens[GameScreen.MainMenu.ordinal()] = new MainMenu(this);
		gameScreens[GameScreen.Levels.ordinal()] = new LevelSelecting(this);
		gameScreens[GameScreen.GamePlay.ordinal()] = new GamePlay(this);
		gameScreens[GameScreen.AboutUs.ordinal()] = new AboutUs(this);
		gameScreens[GameScreen.Tutorial.ordinal()] = new Tutorial(this);
		this.setScreen(getScreen(GameScreen.MainMenu));
	}

	@Override
	public void render () {
		super.render();
	}

	public void dispose() {
		audioManager.dispose();
	}

	public Screen getScreen(GameScreen screen) {
		return gameScreens[screen.ordinal()];
	}
}

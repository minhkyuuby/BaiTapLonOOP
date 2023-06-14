package com.dbdc.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.jpcodes.physics.BulletPhysics;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("danh-bai-dai-cuong");
		config.setBackBufferConfig(8,8,8,8,16, 0, 4);
		config.setWindowedMode(1280, 720);
		config.useVsync(true);
//		new Lwjgl3Application(new Drop(), config);
		new Lwjgl3Application(new GameClass(), config);
//		new Lwjgl3Application(new BulletPhysics(), config);
	}
}

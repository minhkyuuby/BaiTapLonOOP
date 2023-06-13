package com.dbdc.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.dbdc.game.testScreens.BasicCollisionDetection;

public class Basic3DTest extends Game {


    @Override
    public void create() {
        Bullet.init();
        setScreen(new BasicCollisionDetection());
    }
}

package com.dbdc.game.entities;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Enemy extends BulletEntity {
    private EnemyStates currentState;
    public Enemy(btRigidBody body, Scene modelScene) {
        super(body, modelScene);
        currentState = EnemyStates.IDLE;
    }

    private void updateState(EnemyStates states) {
        currentState = states;
    }

}

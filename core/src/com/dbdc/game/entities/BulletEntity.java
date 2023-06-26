package com.dbdc.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import net.mgsx.gltf.scene3d.scene.Scene;

public class BulletEntity {
    private final btRigidBody body;
    private final Scene modelScene;

    public BulletEntity(btRigidBody body, Scene modelScene) {
        this.body = body;
        this.modelScene = modelScene;
    }

    public Scene getModelScene() {
        return modelScene;
    }

    public ModelInstance getModelInstance() {
        return modelScene.modelInstance;
    }

    public AnimationController getAnimationController() {
        return modelScene.animationController;
    }

    public btRigidBody getBody() {
        return body;
    }

}

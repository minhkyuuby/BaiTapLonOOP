package com.jpcodes.physics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class BulletEntity {
    private final btRigidBody body;
    private final ModelInstance modelInstance;

    public BulletEntity(btRigidBody body, ModelInstance modelInstance) {
        this.body = body;
        this.modelInstance = modelInstance;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public btRigidBody getBody() {
        return body;
    }
}
package com.dbdc.game.entities;


import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import net.mgsx.gltf.scene3d.scene.Scene;

public class InteractableEntity extends BulletEntity {
    public static float INTERACTIVE_RADIUS = 3.3f;
    private final float ROTATE_SPEED = 20f;

    InteractableType type;
    boolean isInteracted;
    public InteractableEntity(btRigidBody body, Scene modelScene, InteractableType type) {
        super(body, modelScene);
        type = type;
        isInteracted = false;
    }

    public InteractableType getType() {
        return type;
    }

    public void update(float delta) {
        if (!isInteracted)
            getModelInstance().transform.rotate(Vector3.Y, delta * ROTATE_SPEED);
    }

    public void interact(Vector3 itrObjectPos) {
        isInteracted = true;

    }
}

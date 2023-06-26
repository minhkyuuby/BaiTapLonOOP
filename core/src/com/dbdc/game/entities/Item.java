package com.dbdc.game.entities;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.jpcodes.physics.utils.Utils3D;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Item extends BulletEntity {
    public static float COLLECT_RADIUS = 1f;
    private final float ROTATE_SPEED = 10f;
//    private final Vector3 currentDirection = new Vector3();
    public Item(btRigidBody body, Scene modelScene) {
        super(body, modelScene);
    }

    public void update(float delta) {
//        Utils3D.getDirection(getModelInstance().transform, currentDirection);
//        getModelInstance().transform.rotate(Vector3.Y, ROTATE_SPEED *delta);
        getBody().setAngularVelocity(new Vector3(0,1,0));
//        System.out.println("Rotate here!");
    }
}

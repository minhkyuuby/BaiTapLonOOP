package com.dbdc.game.controllers;

import com.dbdc.game.entities.BulletEntity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestNotMeRayResultCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.jpcodes.physics.BulletPhysicsSystem;
import com.jpcodes.physics.utils.Utils3D;

public class DynamicCharacterController implements AnimationController.AnimationListener {
    private final float MOVE_SPEED = 32f;
    private final float JUMP_FACTOR = 15f;

    private final Vector3 position = new Vector3();
    private final Vector3 normal = new Vector3();
    private final Vector3 tmpPosition = new Vector3();
    private final Vector3 currentDirection = new Vector3();
    private final Vector3 linearVelocity = new Vector3();
    private final Vector3 angularVelocity = new Vector3();

    private final BulletEntity character;

    private AnimationController animationController;
    private final BulletPhysicsSystem physicsSystem;
    private final ClosestNotMeRayResultCallback callback;

    public BulletEntity getCharacter() {
        return character;
    }

    public DynamicCharacterController(BulletEntity entity, BulletPhysicsSystem bulletPhysicsSystem) {
        character = entity;
        physicsSystem = bulletPhysicsSystem;
        callback = new ClosestNotMeRayResultCallback(character.getBody());
        animationController = character.getAnimationController();
        animationController.setAnimation("Idle", -1);
    }

    public void update(float delta) {
        Utils3D.getDirection(character.getModelInstance().transform, currentDirection);
        btRigidBody body = character.getBody();
        resetVelocity();
        boolean isOnGround = isGrounded();

        // A slightly hacky work around to allow climbing up and preventing sliding down slopes
        if (isOnGround) {
            callback.getHitNormalWorld(normal);

            // dot product returns 1 if same direction, -1 if opposite direction, zero if perpendicular
            // so we get the dot product of the normal and the Up (Y) vector.
            float dot = normal.dot(Vector3.Y);

            // If the dot product is NOT 1, meaning the ground is not flat, then we disable gravity
            if (dot != 1.0) {
                body.setGravity(Vector3.Zero);
            }
        } else {
            body.setGravity(BulletPhysicsSystem.DEFAULT_GRAVITY);
        }

        // Forward movement
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            linearVelocity.set(currentDirection).scl(delta * MOVE_SPEED * 1.5f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            linearVelocity.set(currentDirection).scl(-delta * MOVE_SPEED * 1.5f);
        }

        // Turning
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angularVelocity.set(0, 1f, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angularVelocity.set(0, -1f, 0);
        }

        // Jump
        if (isOnGround && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            linearVelocity.y += JUMP_FACTOR;
            animationController.action("Jump", 1, 1.5f, this, 0f);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.J)) {
            animationController.action("AttackSpinning", 1, 1.5f, this, 0.2f);
        }

        if (!linearVelocity.isZero()) {
            body.applyCentralImpulse(linearVelocity);
        }

        if (!angularVelocity.isZero()) {
            body.setAngularVelocity(angularVelocity);
        }

    }

    /**
     * Check if we are standing on something by casting a ray straight down.
     * @return
     */
    private boolean isGrounded() {
        // Reset out callback
        callback.setClosestHitFraction(1.0f);
        callback.setCollisionObject(null);

        Utils3D.getPosition(character.getModelInstance().transform, position);

        // The position we are casting a ray to, slightly below the players current position.
        tmpPosition.set(position).sub(0, 1.4f, 0);

        physicsSystem.raycast(position, tmpPosition, callback);

        return callback.hasHit();
    }

    private void resetVelocity() {
        angularVelocity.set(0,0,0);
        linearVelocity.set(0,0,0);
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {

    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {

    }
}
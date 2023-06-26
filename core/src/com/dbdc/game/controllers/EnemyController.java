package com.dbdc.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestNotMeRayResultCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.dbdc.game.entities.BulletEntity;
import com.jpcodes.physics.BulletPhysicsSystem;

public class EnemyController implements AnimationController.AnimationListener {
    public static float ATTACK_RADIUS = 2f;
    private final float MOVE_SPEED = 28f;

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

    public EnemyController(BulletEntity entity, BulletPhysicsSystem bulletPhysicsSystem) {
        character = entity;
        physicsSystem = bulletPhysicsSystem;
        callback = new ClosestNotMeRayResultCallback(character.getBody());
        animationController = character.getAnimationController();
        animationController.setAnimation("Idle", -1);
    }

    public void update(float delta) {

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

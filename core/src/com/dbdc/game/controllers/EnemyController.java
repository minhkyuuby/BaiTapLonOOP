package com.dbdc.game.controllers;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.dbdc.game.entities.BulletEntity;
import com.jpcodes.physics.BulletPhysicsSystem;

import java.util.Random;

public class EnemyController implements AnimationController.AnimationListener {
    public static float ATTACK_RADIUS = 2f;
    private final float MOVE_SPEED = 28f;
    private final float ATTACK_DELAY = 3.5f;

    private final Vector3 position = new Vector3();
    private final Vector3 normal = new Vector3();
    private final Vector3 tmpPosition = new Vector3();
    private final Vector3 currentDirection = new Vector3();
    private final Vector3 linearVelocity = new Vector3();
    private final Vector3 angularVelocity = new Vector3();

    private final BulletEntity character;

    private AnimationController animationController;
//    private final BulletPhysicsSystem physicsSystem;

    public boolean isDefeated, isAttacking, shouldBeDestroy;
    private float atkCountdown;

    public BulletEntity getCharacter() {
        return character;
    }

    public EnemyController(BulletEntity entity) {
        character = entity;
//        physicsSystem = bulletPhysicsSystem;
        animationController = character.getAnimationController();
        animationController.setAnimation("Run", -1);
        isDefeated = false;
        isAttacking = false;
        shouldBeDestroy = false;
        Random rand = new Random();

        atkCountdown = rand.nextFloat(ATTACK_DELAY);
    }

    public void update(float delta) {
        atkCountdown-=delta;
        if (atkCountdown <= 0) {
            atkCountdown = ATTACK_DELAY;
            Attack();
        }
    }

    private void resetVelocity() {
        angularVelocity.set(0,0,0);
        linearVelocity.set(0,0,0);
    }

    public void Death() {
        animationController.animate("Defeat", 1, 1f, this, 0.25f);
        isDefeated = true;
    }

    public void Attack() {
        if(!isDefeated) {
            animationController.action("AttackSpinning", 1, 1.5f, this, 0f);
            isAttacking = true;
        }
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {
        if(animation.animation.id.equals("Defeat")) {
//            System.out.println("Defeated!");
            shouldBeDestroy = true;
        }
        if(animation.animation.id.equals("AttackSpinning")) {
            isAttacking = false;
        }
    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {

    }
}

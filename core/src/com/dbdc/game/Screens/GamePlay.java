package com.dbdc.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.dbdc.game.GameClass;
import com.dbdc.game.controllers.DynamicCharacterController;
import com.dbdc.game.entities.BulletEntity;
import com.jpcodes.physics.MotionState;
import com.jpcodes.physics.controllers.camera.ThirdPersonCameraController;
import com.jpcodes.physics.utils.Utils3D;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class GamePlay extends PhysicScreen {


    private final DynamicCharacterController playerController;
    private SceneAsset levelAsset;
    private SceneAsset playerAsset;

    private Scene playerScene;

    public GamePlay(GameClass game) {
        super(game);

        BulletEntity player = createPlayer();

        playerController = new DynamicCharacterController(player, bulletPhysicsSystem);
//        playerScene = playerController.getCharacter().getModelScene();
        setCameraController(new ThirdPersonCameraController(camera, playerScene.modelInstance));

        addSceneToSceneManager(playerScene);
        camera.position.set(new Vector3(0, 10, -10));
        camera.lookAt(Vector3.Zero);

        // Load a walkable area
        levelAsset = new GLTFLoader().load(Gdx.files.internal("models/level.gltf"));
        Scene levelScene = new Scene(levelAsset.scene);
        Model sceneModel = Utils3D.loadOBJ(Gdx.files.internal("models/level.obj"));
        ModelInstance sceneInstance = new ModelInstance(sceneModel);
        sceneInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.FOREST));

        addSceneToSceneManager(levelScene);
//        renderInstances.add(sceneInstance);

        btCollisionShape shape = Bullet.obtainStaticNodeShape(sceneInstance.nodes);
        btRigidBody.btRigidBodyConstructionInfo sceneInfo = new btRigidBody.btRigidBodyConstructionInfo(0f, null, shape, Vector3.Zero);
        btRigidBody body = new btRigidBody(sceneInfo);
        bulletPhysicsSystem.addBody(body);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        playerController.update(delta);
    }

    @Override
    public void dispose() {
        levelAsset.dispose();
        super.dispose();
    }

    private BulletEntity createPlayer() {
        playerAsset = new GLTFLoader().load(Gdx.files.internal("models/character/skeleton-archer.gltf"));
        playerScene = new Scene(playerAsset.scene);
        ModelInstance playerModelInstance = playerScene.modelInstance;

        // Move him up above the ground
        playerModelInstance.transform.setToTranslation(0,10,0);

        // Calculate dimension
        BoundingBox boundingBox = new BoundingBox();
        playerModelInstance.calculateBoundingBox(boundingBox);
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);

        // Scale for half extents
        dimensions.scl(0.5f);
        MotionState motionState = new MotionState(playerModelInstance.transform);
        btCapsuleShape capsuleShape = new btCapsuleShape(dimensions.len() / 2.5f, dimensions.y);

        float mass = 2f;

        Vector3 intertia = new Vector3();
        capsuleShape.calculateLocalInertia(mass, intertia);

        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(mass, motionState, capsuleShape, intertia);
        btRigidBody body = new btRigidBody(info);

        // Prevent body from falling over
        body.setAngularFactor(Vector3.Y);

        // Prevent the body from sleeping
        body.setActivationState(Collision.DISABLE_DEACTIVATION);

        // Add damping so we dont slide forever!
        body.setDamping(0.75f, 0.99f);

        renderInstances.add(playerModelInstance);
        bulletPhysicsSystem.addBody(body);

        return new BulletEntity(body, playerScene);
    }
}

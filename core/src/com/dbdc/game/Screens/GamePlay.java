package com.dbdc.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dbdc.game.GameClass;
import com.dbdc.game.controllers.DynamicCharacterController;
import com.dbdc.game.controllers.EnemyController;
import com.dbdc.game.entities.BulletEntity;
import com.dbdc.game.entities.Item;
import com.jpcodes.physics.MotionState;
import com.jpcodes.physics.controllers.camera.ThirdPersonCameraController;
import com.jpcodes.physics.utils.Utils3D;
import com.kotcrab.vis.ui.widget.VisLabel;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GamePlay extends PhysicScreen {


    private final DynamicCharacterController playerController;
    private SceneAsset playerAsset;
    private SceneAsset levelAsset;
    private Scene playerScene;

    private List<Item> levelItems;

    private List<EnemyController> enemies;

    // UI components
    private final Stage stage;
    private final VisLabel bookCountLabel;

    // Gameplay params
    int bookCollected;

    public GamePlay(GameClass game) {
        super(game);
        BulletEntity player = createCharacter("models/character/dogwithpencilandsuitcase.gltf", new Vector3(0, 10, 5));

        playerController = new DynamicCharacterController(player, bulletPhysicsSystem);
        setCameraController(new ThirdPersonCameraController(camera, playerScene.modelInstance));

        addSceneToSceneManager(playerScene);
        camera.position.set(new Vector3(0, 10, -10));
        camera.lookAt(Vector3.Zero);

        /*   UI setup *///
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        bookCountLabel = new VisLabel();
        bookCountLabel.setPosition(10f,Gdx.graphics.getHeight() -10);
        stage.addActor(bookCountLabel);

        /*  gameplay setup *///
        bookCollected = 0;
        levelItems = new ArrayList<>();
        enemies = new ArrayList<>();
    }

    @Override
    public void show() {
        super.show();
        // Load a walkable area
        createLevel("models/level.gltf", "models/level/level.obj");
        levelItems.add(createLevelItem("models/item/bookItem.gltf", new Vector3(0, 3, 3)));
        levelItems.add(createLevelItem("models/item/bookItem.gltf", new Vector3(0, 3, 4)));
        enemies.add(createLevelEnemy("models/character/brokenminion.gltf", new Vector3(0, 10, -5)));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenu(game));
            this.hide();
            return;
        }
        playerController.update(delta);
        checkItemCollision(delta);

        for (EnemyController enemy: enemies) {
            enemy.update(delta);
        }

        /* UI update */
        stage.act();
        stage.draw();
        bookCountLabel.setText("Book count: " + bookCollected);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        playerAsset.dispose();
        levelAsset.dispose();
    }

    private BulletEntity createCharacter(String charModelPath, Vector3 position) {
        playerAsset = new GLTFLoader().load(Gdx.files.internal(charModelPath));
        playerScene = new Scene(playerAsset.scene);
        ModelInstance playerModelInstance = playerScene.modelInstance;

        // Move him up above the ground
        playerModelInstance.transform.setToTranslation(position);

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
        info.dispose();
        return new BulletEntity(body, playerScene);
    }

    private void createLevel(String levelScenePath, String levelBodyPath) {
        levelAsset = new GLTFLoader().load(Gdx.files.internal(levelScenePath));
        Scene levelScene = new Scene(levelAsset.scene);
        Model sceneModel = Utils3D.loadOBJ(Gdx.files.internal(levelBodyPath));
        ModelInstance sceneInstance = new ModelInstance(sceneModel);
        sceneInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.FOREST));

        addSceneToSceneManager(levelScene);
        renderInstances.add(sceneInstance);

        btCollisionShape shape = Bullet.obtainStaticNodeShape(sceneInstance.nodes);
        btRigidBody.btRigidBodyConstructionInfo sceneInfo = new btRigidBody.btRigidBodyConstructionInfo(0f, null, shape, Vector3.Zero);
        btRigidBody body = new btRigidBody(sceneInfo);
        sceneInfo.dispose();
        bulletPhysicsSystem.addBody(body);
    }

    private Item createLevelItem(String itemPath, Vector3 position) {
        levelAsset = new GLTFLoader().load(Gdx.files.internal(itemPath));
        Scene itemScene = new Scene(levelAsset.scene);
        ModelInstance itemInstance = itemScene.modelInstance;
//        sceneInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.FOREST));
        addSceneToSceneManager(itemScene);

        renderInstances.add(itemInstance);
        itemInstance.transform.setToTranslation(position);

        BoundingBox boundingBox = new BoundingBox();
        itemInstance.calculateBoundingBox(boundingBox);
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);

        // Scale for half extents
        dimensions.scl(0.5f);
        MotionState motionState = new MotionState(itemInstance.transform);
        btCapsuleShape capsuleShape = new btCapsuleShape(dimensions.len() / 5f, dimensions.y);

        float mass = 2f;

        Vector3 intertia = new Vector3();
        capsuleShape.calculateLocalInertia(mass, intertia);
        btRigidBody.btRigidBodyConstructionInfo itemInfo = new btRigidBody.btRigidBodyConstructionInfo(1f, motionState, capsuleShape, intertia);
        btRigidBody body = new btRigidBody(itemInfo);
        // Prevent body from falling over
        body.setAngularFactor(Vector3.Y);

        // Prevent the body from sleeping
        body.setActivationState(Collision.DISABLE_DEACTIVATION);

        bulletPhysicsSystem.addBody(body);
        itemInfo.dispose();
        return new Item(body, itemScene);
    }
    private EnemyController createLevelEnemy(String charModelPath, Vector3 position) {
        BulletEntity enemy = createCharacter(charModelPath, position);
        addSceneToSceneManager(enemy.getModelScene());
        return new EnemyController(enemy, bulletPhysicsSystem);
    }

    // check Methods
    private void checkItemCollision(float delta) {
        Iterator<Item> itr = levelItems.iterator();
        while (itr.hasNext()) {
            Item item = itr.next();
            item.update(delta);

            Vector3 tmpV1 = new Vector3();
            Vector3 tmpV2 = new Vector3();
            item.getModelInstance().transform.getTranslation(tmpV1);
            playerController.getCharacter().getModelInstance().transform.getTranslation(tmpV2);
            if(tmpV1.dst(tmpV2) <= Item.COLLECT_RADIUS) {
                removeSceneFromSceneManager(item.getModelScene());
                bulletPhysicsSystem.removeBody(item.getBody());
                itr.remove();
                // Add bookItem here
                ++bookCollected;
            }
        }
    }
}

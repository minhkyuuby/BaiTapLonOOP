package com.dbdc.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dbdc.game.GameClass;
import com.dbdc.game.GameScreen;
import com.dbdc.game.JsonObject.Enemies;
import com.dbdc.game.JsonObject.LevelData;
import com.dbdc.game.controllers.DynamicCharacterController;
import com.dbdc.game.controllers.EnemyController;
import com.dbdc.game.entities.BulletEntity;
import com.dbdc.game.entities.InteractableEntity;
import com.dbdc.game.entities.InteractableType;
import com.dbdc.game.entities.Item;
import com.dbdc.game.JsonObject.Items;
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
    private SceneAsset itemAsset;
    private Scene playerScene;


    // UI components
    private Stage stage;
    private Skin skin;
    private VisLabel bookCountLabel;
    private Table table;
    private VisLabel isInteractableLabel;

    // Gameplay params
    int bookCollected;
    boolean isInExamArea;
    private List<Item> levelItems;
    private List<EnemyController> enemies;
    private List<EnemyController> diedEnemies;
    private List<InteractableEntity> interactableItems;

//    private MainMenu menuScreen;

    public GamePlay(GameClass game) {
        super(game);
//        menuScreen = menu;
        BulletEntity player = createCharacter("models/character/dogwithpencilandsuitcase.gltf", new Vector3(0, 10, 5));
        playerController = new DynamicCharacterController(player, bulletPhysicsSystem);
        setCameraController(new ThirdPersonCameraController(camera, playerScene.modelInstance));
        addSceneToSceneManager(playerScene);
        camera.position.set(new Vector3(0, 10, -10));
        camera.lookAt(Vector3.Zero);

        /*  gameplay setup *///
        bookCollected = 0;
        isInExamArea = false;
        levelItems = new ArrayList<>();
        enemies = new ArrayList<>();
        diedEnemies = new ArrayList<>();
        interactableItems = new ArrayList<>();

        skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libGDX font under the name "default".
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
    }

    @Override
    public void show() {
        super.show();
        game.audioManager.playGameplayMusic();
        /*   UI setup *///
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        bookCountLabel = new VisLabel();
        bookCountLabel.setPosition(10f,Gdx.graphics.getHeight() -10);
        stage.addActor(bookCountLabel);
        isInteractableLabel = new VisLabel();
        isInteractableLabel.setPosition(Gdx.graphics.getWidth(),Gdx.graphics.getHeight() -10);
        isInteractableLabel.setAlignment(isInteractableLabel.getLabelAlign(), Align.right);
        stage.addActor(isInteractableLabel);
        table = new Table();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        // Load a walkable area
        // Load the JSON file
        FileHandle fileHandle = Gdx.files.internal("json/example.json");
        String json = fileHandle.readString();

        // Parse the JSON into the Item class
        JsonReader jsonReader = new JsonReader();
        Json jsonParser = new Json();
        LevelData leveldata = jsonParser.fromJson(LevelData.class, String.valueOf(jsonReader.parse(json)));
        String levelScenePath = leveldata.getLevelScenePath();
        String levelBodyPath = leveldata.getLevelBodyPath();
        //createLevel("models/level.gltf", "models/level/level.obj");
        createLevel(levelScenePath, levelBodyPath);
        ArrayList<Items> items = leveldata.getItems();
        ArrayList<Enemies> enemies2 = leveldata.getEnemies();
        Items interactableItem = leveldata.getInteractableItem();
        if ( items.size() > 0 ) {
            Items item = items.get(0);

            String itemPath = item.getItemPath();
            Vector3 position = item.getPosition();
            levelItems.add(createLevelItem(itemPath, position));
        }
        if ( enemies2.size() > 0 ) {
            Enemies enemy = enemies2.get(0);

            String CharModelPath = enemy.getCharModelPath();
            Vector3 position = enemy.getPosition();
            enemies.add(createLevelEnemy(CharModelPath, position));
        }
        String itemPath = interactableItem.getItemPath();
        Vector3 position = interactableItem.getPosition();
        interactableItems.add(createLevelInteractable(itemPath, position, InteractableType.FinalExamInteract));
        /*levelItems.add(createLevelItem("models/item/bookItem.gltf", new Vector3(0, 3, 3)));
        enemies.add(createLevelEnemy("models/character/brokenminion.gltf", new Vector3(0, 10, -5)));
        interactableItems.add(createLevelInteractable("models/item/exambook.gltf", new Vector3(5, 3, - 5), InteractableType.FinalExamInteract));*/

        /* Setup player Position */
        Matrix4 transform = new Matrix4();
        transform.setTranslation(0, 3, 0);
        playerController.getCharacter().getBody().setWorldTransform(transform);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.getScreen(GameScreen.MainMenu));
            ClearScreen();
            return;
        }
        super.render(delta);
        playerController.update(delta);
        for (EnemyController enemy: enemies) {
            enemy.update(delta);
        }
        if(playerController.examTaked && playerController.isExamDone) {
            playerController.resetExamStat();
            InteractWithExam();
        }

        checkItemCollision(delta);
        checkInteractable(delta);
        checkAttack(delta);
        checkDiedEnemies(delta);

        /* UI update */
        stage.act();
        stage.draw();
        bookCountLabel.setText("Book count: " + bookCollected);
        if(isInExamArea) {
            isInteractableLabel.setText("exam available: press E to take the final exam!");
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                InteractWithExam();
            }
        } else {
            isInteractableLabel.setText("Not in area");
        }
//        isInteractableLabel.setText("isInvincible: " + playerController.isInvincible);
    }

    @Override
    public void hide() {
        super.hide();
        game.audioManager.stopGameplayMusic();
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        if (playerAsset != null)
            playerAsset.dispose();
        if (levelAsset != null)
            levelAsset.dispose();
        if (itemAsset != null)
            itemAsset.dispose();
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
        if(itemAsset == null)
            itemAsset = new GLTFLoader().load(Gdx.files.internal(itemPath));
        Scene itemScene = new Scene(itemAsset.scene);
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
//        body.setActivationState(Collision.DISABLE_DEACTIVATION);

        bulletPhysicsSystem.addBody(body);
        itemInfo.dispose();
        return new Item(body, itemScene);
    }

    private EnemyController createLevelEnemy(String charModelPath, Vector3 position) {
        BulletEntity enemy = createCharacter(charModelPath, position);
        addSceneToSceneManager(enemy.getModelScene());
        return new EnemyController(enemy);
    }
    private InteractableEntity createLevelInteractable(String itemPath, Vector3 position, InteractableType type) {
        levelAsset = new GLTFLoader().load(Gdx.files.internal(itemPath));
        Scene itemScene = new Scene(levelAsset.scene);
        ModelInstance itemInstance = itemScene.modelInstance;
        addSceneToSceneManager(itemScene); // Add object to scene manager

        renderInstances.add(itemInstance);
        itemInstance.transform.setToTranslation(position);

        BoundingBox boundingBox = new BoundingBox();
        itemInstance.calculateBoundingBox(boundingBox);
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);

        // Scale for half extents
        dimensions.scl(0.5f);
        MotionState motionState = new MotionState(itemInstance.transform);
        btCapsuleShape capsuleShape = new btCapsuleShape(dimensions.len() / 2.5f, dimensions.y);

        float mass = 2f;

        Vector3 intertia = new Vector3();
        capsuleShape.calculateLocalInertia(mass, intertia);
        btRigidBody.btRigidBodyConstructionInfo itemInfo = new btRigidBody.btRigidBodyConstructionInfo(0, motionState, capsuleShape, intertia);
        btRigidBody body = new btRigidBody(itemInfo);
        // Prevent body from falling over
        body.setAngularFactor(Vector3.Y);

        bulletPhysicsSystem.addBody(body);
        itemInfo.dispose();
        return new InteractableEntity(body, itemScene, type);
    }
    // check Methods
    Vector3 tmpV1 = new Vector3();
    Vector3 tmpV2 = new Vector3();

    private void checkItemCollision(float delta) {
        Iterator<Item> itr = levelItems.iterator();

        playerController.getCharacter().getModelInstance().transform.getTranslation(tmpV2);
        while (itr.hasNext()) {
            Item item = itr.next();
            item.update(delta);
            item.getModelInstance().transform.getTranslation(tmpV1);
            if(tmpV1.dst(tmpV2) <= Item.COLLECT_RADIUS) {
                removeSceneFromSceneManager(item.getModelScene());
                bulletPhysicsSystem.removeBody(item.getBody());
                itr.remove();
                // Add bookItem here
                ++bookCollected;
            }
        }
    }
    private void checkInteractable(float delta) {
        Iterator<InteractableEntity> itr = interactableItems.iterator();
        isInExamArea = false;

        playerController.getCharacter().getModelInstance().transform.getTranslation(tmpV2);
        while (itr.hasNext()) {
            InteractableEntity item = itr.next();
            item.update(delta);


            item.getModelInstance().transform.getTranslation(tmpV1);
            if(tmpV1.dst(tmpV2) <= InteractableEntity.INTERACTIVE_RADIUS) {
                isInExamArea = true;
            }
        }
    }

    private void checkAttack(float delta) {
        Iterator<EnemyController> itr = enemies.iterator();
        playerController.getCharacter().getModelInstance().transform.getTranslation(tmpV2);
        while (itr.hasNext()) {
            EnemyController enemy = itr.next();

            enemy.getCharacter().getModelInstance().transform.getTranslation(tmpV1);
            if(tmpV1.dst(tmpV2) <= EnemyController.ATTACK_RADIUS && enemy.isAttacking && !playerController.isInvincible) {
                // Enemy force player
                playerController.GetHit();
//                System.out.println("Attack Player!!");
                playerController.getCharacter().getBody().applyCentralImpulse(tmpV2.sub(tmpV1).scl(13f));
            }
            if(tmpV1.dst(tmpV2) <= DynamicCharacterController.ATTACK_RADIUS && playerController.isAttacking) {
                enemy.Death();
                diedEnemies.add(enemy);
                levelItems.add(createLevelItem("models/item/bookItem.gltf", new Vector3(tmpV1.x, tmpV1.y + 3, tmpV1.z)));
                itr.remove();
            }
        }
    }

    private void checkDiedEnemies(float delta) {
        Iterator<EnemyController> itr = diedEnemies.iterator();
        while (itr.hasNext()) {
            EnemyController enemy = itr.next();
            if (enemy.shouldBeDestroy) {
                removeSceneFromSceneManager(enemy.getCharacter().getModelScene());
                bulletPhysicsSystem.removeBody(enemy.getCharacter().getBody());
                itr.remove();
            }
        }
    }

    /* Gameplay Decision */
    private void InteractWithExam() {
        table.reset();
        table.setFillParent(true);
        table.defaults().pad(0f);
        table.pack();

        table.row();
        TextButton acceptBtn = new TextButton("Take the Exam", skin);
        table.add(acceptBtn);
        table.row();
        TextButton declineBtn = new TextButton("Decline", skin);
        table.add(declineBtn);
        table.setPosition(0,-80);
        table.align(Align.center);

        acceptBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AcceptExam();
            };
        });
        declineBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                DeclineExam();
            };
        });

        playerController.DisableControl();
    }

    private void AcceptExam() {
        playerController.TakeExam();
        table.reset();
    }

    private void DeclineExam() {
        table.reset();
        playerController.EnableControl();
    }

    /* ClEAR SCREEN */
    private void ClearScreen() {
//        dispose();
        playerController.getCharacter().getBody().clearForces();
        playerController.getCharacter().getBody().setLinearVelocity(Vector3.Zero);
//        playerController.getCharacter().getBody().setLinearFactor(Vector3.Zero);
//        playerController.getCharacter().getBody()
        for (Item item: levelItems) {
            removeSceneFromSceneManager(item.getModelScene());
            bulletPhysicsSystem.removeBody(item.getBody());
        }
        levelItems.clear();
        for (InteractableEntity item: interactableItems) {
            removeSceneFromSceneManager(item.getModelScene());
            bulletPhysicsSystem.removeBody(item.getBody());
        }
        interactableItems.clear();
        for (EnemyController enemy : enemies) {
            removeSceneFromSceneManager(enemy.getCharacter().getModelScene());
            bulletPhysicsSystem.removeBody(enemy.getCharacter().getBody());
        }
        enemies.clear();


        bookCollected = 0;
        isInExamArea = false;
    }
}

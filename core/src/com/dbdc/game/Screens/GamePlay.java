package com.dbdc.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
import com.dbdc.game.manager.AudioManager;
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
    private String crtCharacterModelPath;
    private SceneAsset levelAsset;
    private SceneAsset itemAsset;
    private Scene playerScene;
    private Scene levelScene;
    private btRigidBody levelBody;

    // UI components
    private Stage stage;
    private Table table, layoutTable;
    private VisLabel timerLabel;
    private BitmapFont font;
    private Button pressE, acceptBtn, declineBtn, levelBtn;
    private TextureAtlas atlas, resultatlas;
    private Skin uiSkin, resultSkin;
    private SpriteBatch batch;
    // Gameplay params
    GamePlayLevel level;
    boolean timerEnd;
    int bookCollected;
    boolean isInExamArea;
    private int seconds = 3,
            minutes = 0;
    private int LEVELTIME;
    private float accumulatedTime;
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
        level = GamePlayLevel.GiaiTich1;
        accumulatedTime = 0;

    }

    @Override
    public void show() {
        super.show();
        game.audioManager.playGameplayMusic();

        /*  gameplay setup *///
        crtCharacterModelPath = "";
//        levelCountdown = LEVELTIME;
        timerEnd = false;
        font = new BitmapFont(Gdx.files.internal("myfont.fnt"));
        font.getData().setScale(0.8f);

        atlas = new TextureAtlas("screen/gameUI.pack");
        uiSkin = new Skin(atlas);
        resultatlas = new TextureAtlas("screen/resultSkin.pack");
        resultSkin = new Skin(resultatlas);

        batch = new SpriteBatch();
        table = new Table();

        /*   UI setup *///
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Image bookCountBG = new Image(uiSkin.getDrawable("bookcount"));
        Image timerBG = new Image(uiSkin.getDrawable("timer"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        ImageButton.ImageButtonStyle pressEStyle = new ImageButton.ImageButtonStyle();
        pressEStyle.up = uiSkin.getDrawable("pressE_up");
        pressEStyle.down = uiSkin.getDrawable("pressE_down");
        pressE = new ImageButton(pressEStyle);

        stage.addActor(pressE);

        timerLabel = new VisLabel(getFormattedTime(), labelStyle);
        timerLabel.setPosition(85,648);

        layoutTable = new Table();
        layoutTable.top();
        layoutTable.setFillParent(true);

        layoutTable.row();
        layoutTable.add(timerBG).align(Align.left).padTop(10f).padLeft(10f);
        layoutTable.row();
        layoutTable.add(bookCountBG).expandX().align(Align.left).padLeft(10f);

        stage.addActor(layoutTable);
        stage.addActor(timerLabel);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        // Load a walkable area
        // Load the JSON file
        String jsonPath = "json/example.json";
        if(level == GamePlayLevel.GiaiTich1) {
            jsonPath = "json/giaitich1.json";
        }
        if(level == GamePlayLevel.GiaiTich2) {
            jsonPath = "json/giaitich2.json";
        }
        FileHandle fileHandle = Gdx.files.internal(jsonPath);
        String json = fileHandle.readString();

        // Parse the JSON into the Item class
        JsonReader jsonReader = new JsonReader();
        Json jsonParser = new Json();
        LevelData leveldata = jsonParser.fromJson(LevelData.class, String.valueOf(jsonReader.parse(json)));
        createLevel(leveldata.getLevelScenePath(), leveldata.getLevelBodyPath());
        ArrayList<Items> itemsData = leveldata.getItems();
        ArrayList<Enemies> enemiesData = leveldata.getEnemies();
        Items interactableItem = leveldata.getInteractableItem();
        for (Items item: itemsData) {
            levelItems.add(createLevelItem(item.getItemPath(), item.getPosition()));
        }
        for(Enemies e : enemiesData) {
            enemies.add(createLevelEnemy(e.getCharModelPath(), e.getPosition()));
        }
        interactableItems.add(createLevelInteractable(interactableItem.getItemPath(), interactableItem.getPosition(), InteractableType.FinalExamInteract));

        /* Setup player Position */
        Matrix4 transform = new Matrix4();
        transform.setTranslation(0, 3, 0);
        playerController.getCharacter().getBody().setWorldTransform(transform);
    }

    private void updateTimer (float delta) {
        accumulatedTime += delta;
        LEVELTIME = minutes * 60 + seconds;

        if (accumulatedTime >= 1f) {
            LEVELTIME--;
            if (LEVELTIME < 0) {
                LEVELTIME = 0;
            }
            minutes = LEVELTIME / 60;
            seconds = LEVELTIME % 60;

            timerLabel.setText(getFormattedTime());
            accumulatedTime = 0;
        }
    }


    public String getFormattedTime() {
        StringBuilder stringBuilder = new StringBuilder();

        if(minutes < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(minutes);
        stringBuilder.append(":");

        if(seconds < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(seconds);
        return stringBuilder.toString();
    }


    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.getScreen(GameScreen.Levels));
            ClearScreen();
            return;
        }
        super.render(delta);

        playerController.update(delta);
        for (EnemyController enemy: enemies) {
            enemy.update(delta);
        }

        if(isInExamArea) {
            pressE.setPosition(720,400);
            pressE.setVisible(true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                pressE.remove();
                InteractWithExam();
            }
        } else {
            pressE.setVisible(false);
        }

        if(playerController.examTaked && playerController.isExamDone) {
            playerController.resetExamStat();
            showResult(bookCollected);
        }

        checkItemCollision(delta);
        checkInteractable(delta);
        checkAttack(delta);
        checkDiedEnemies(delta);

        /* UI update */
        batch.begin();
        font.draw(batch, "" + bookCollected, 90f,Gdx.graphics.getHeight() - 140f);
        stage.act();
        stage.draw();
        batch.end();

        if(!timerEnd)
            updateTimer(delta);
        // Show result
        if (LEVELTIME <= 0 && !timerEnd) {
            timerEnd = true;
            showResult(0);
        }

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

    public void setLevel(GamePlayLevel level) {
        this.level = level;
    }

    // creatation methods
    private BulletEntity createCharacter(String charModelPath, Vector3 position) {
        if(!(crtCharacterModelPath == charModelPath)) {
            playerAsset = new GLTFLoader().load(Gdx.files.internal(charModelPath));
        }
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

//        renderInstances.add(playerModelInstance);
        bulletPhysicsSystem.addBody(body);
        info.dispose();
        return new BulletEntity(body, playerScene);
    }

    private void createLevel(String levelScenePath, String levelBodyPath) {
        levelAsset = new GLTFLoader().load(Gdx.files.internal(levelScenePath));
        levelScene = new Scene(levelAsset.scene);
        Model sceneModel = Utils3D.loadOBJ(Gdx.files.internal(levelBodyPath));
        ModelInstance sceneInstance = new ModelInstance(sceneModel);
        sceneInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.FOREST));

        addSceneToSceneManager(levelScene);
//        renderInstances.add(sceneInstance);

        btCollisionShape shape = Bullet.obtainStaticNodeShape(sceneInstance.nodes);
        btRigidBody.btRigidBodyConstructionInfo sceneInfo = new btRigidBody.btRigidBodyConstructionInfo(0f, null, shape, Vector3.Zero);
        levelBody = new btRigidBody(sceneInfo);
        sceneInfo.dispose();
        bulletPhysicsSystem.addBody(levelBody);
    }

    private Item createLevelItem(String itemPath, Vector3 position) {
        if(itemAsset == null)
            itemAsset = new GLTFLoader().load(Gdx.files.internal(itemPath));
        Scene itemScene = new Scene(itemAsset.scene);
        ModelInstance itemInstance = itemScene.modelInstance;
//        sceneInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.FOREST));
        addSceneToSceneManager(itemScene);

//        renderInstances.add(itemInstance);
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
                game.audioManager.playSoundEffect(1);
                playerController.GetHit();
//                System.out.println("Attack Player!!");
                playerController.getCharacter().getBody().applyCentralImpulse(tmpV2.sub(tmpV1).scl(13f));
            }
            if(tmpV1.dst(tmpV2) <= DynamicCharacterController.ATTACK_RADIUS && playerController.isAttacking) {
                game.audioManager.playSoundEffect(1);
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

        ImageButton.ImageButtonStyle acceptStyle = new ImageButton.ImageButtonStyle();
        acceptStyle.up = uiSkin.getDrawable("taketheexam_up");
        acceptStyle.down = uiSkin.getDrawable("taketheexam_down");
        acceptBtn = new ImageButton(acceptStyle);

        ImageButton.ImageButtonStyle declineStyle = new ImageButton.ImageButtonStyle();
        declineStyle.up = uiSkin.getDrawable("decline_up");
        declineStyle.down = uiSkin.getDrawable("decline_down");
        declineBtn = new ImageButton(declineStyle);

        table.reset();
        table.setFillParent(true);
        table.defaults().pad(0f);
        table.pack();

        table.row();
        table.add(acceptBtn);
        table.row();
        table.add(declineBtn);
        table.setPosition(218,39);

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
        game.audioManager.playSoundEffect(2);
        playerController.TakeExam();
        table.reset();
    }

    private void resetTimer() {
        seconds = 3;
        minutes = 0;
        accumulatedTime = 0;
        timerEnd = false;
    }

    private void DeclineExam() {
        table.reset();
        playerController.EnableControl();
    }

    private void showResult(int bookColl) {

        ImageButton.ImageButtonStyle backLevelStyle = new ImageButton.ImageButtonStyle();
        backLevelStyle.up = resultSkin.getDrawable("backtolevel_up");
        backLevelStyle.down = resultSkin.getDrawable("backtolevel_down");
        levelBtn = new ImageButton(backLevelStyle);

        Image passBG = new Image(resultSkin.getDrawable("passbg"));
        Image failBG = new Image(resultSkin.getDrawable("failbg"));
        Image Aplus = new Image(resultSkin.getDrawable("A+"));
        Image A = new Image(resultSkin.getDrawable("A"));
        Image Bplus = new Image(resultSkin.getDrawable("B+"));
        Image B = new Image(resultSkin.getDrawable("B"));
        Image Cplus = new Image(resultSkin.getDrawable("C+"));
        Image C = new Image(resultSkin.getDrawable("C"));
        Image Dplus = new Image(resultSkin.getDrawable("D+"));
        Image D = new Image(resultSkin.getDrawable("D"));
        Image book0 = new Image(resultSkin.getDrawable("book0"));
        Image book1 = new Image(resultSkin.getDrawable("book01"));
        Image book2 = new Image(resultSkin.getDrawable("book02"));
        Image book3 = new Image(resultSkin.getDrawable("book03"));
        Image book4 = new Image(resultSkin.getDrawable("book04"));
        Image book5 = new Image(resultSkin.getDrawable("book05"));
        Image book6 = new Image(resultSkin.getDrawable("book06"));
        Image book7 = new Image(resultSkin.getDrawable("book07"));
        Image book8 = new Image(resultSkin.getDrawable("book08"));
        Image book9 = new Image(resultSkin.getDrawable("book09"));
        Image book10 = new Image(resultSkin.getDrawable("book10"));

        table.reset();
        table.setFillParent(true);
        table.defaults().pad(0f);
        table.pack();
        if(bookColl > 2) {
            game.audioManager.playSoundEffect(3);
        } else {
            game.audioManager.playSoundEffect(4);
        }

        switch (bookColl) {
            case 0:
                table.row();
                table.add(failBG);
                book0.setPosition(501,320);
                stage.addActor(book0);
                break;
            case 1:
                table.row();
                table.add(failBG);
                book1.setPosition(501,320);
                stage.addActor(book1);
                break;
            case 2:
                table.row();
                table.add(failBG);
                book2.setPosition(501,320);
                stage.addActor(book2);
                break;
            case 3:
                table.row();
                table.add(passBG);
                book3.setPosition(501,320);
                D.setPosition(562,111);
                stage.addActor(D);
                stage.addActor(book3);
                break;
            case 4:
                table.row();
                table.add(passBG);
                book4.setPosition(501,320);
                Dplus.setPosition(562,111);
                stage.addActor(Dplus);
                stage.addActor(book4);
                break;
            case 5:
                table.row();
                table.add(passBG);
                book5.setPosition(501,320);
                C.setPosition(562,111);
                stage.addActor(C);
                stage.addActor(book5);
                break;
            case 6:
                table.row();
                table.add(passBG);
                book6.setPosition(501,320);
                Cplus.setPosition(562,111);
                stage.addActor(Cplus);
                stage.addActor(book6);
                break;
            case 7:
                table.row();
                table.add(passBG);
                book7.setPosition(501,320);
                B.setPosition(562,111);
                stage.addActor(B);
                stage.addActor(book7);
                break;
            case 8:
                table.row();
                table.add(passBG);
                book8.setPosition(501,320);
                Bplus.setPosition(562,111);
                stage.addActor(Bplus);
                stage.addActor(book8);
                break;
            case 9:
                table.row();
                table.add(passBG);
                book9.setPosition(501,320);
                A.setPosition(562,111);
                stage.addActor(A);
                stage.addActor(book9);
                break;
            case 10:
                table.row();
                table.add(passBG);
                book10.setPosition(501,320);
                Aplus.setPosition(562,111);
                stage.addActor(Aplus);
                stage.addActor(book10);
                break;
        }

        table.row();
        table.add(levelBtn);
        table.setPosition(0,0);
        table.padTop(20f);
        table.align(Align.center).align(Align.bottom).padBottom(20f);

        levelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getScreen(GameScreen.Levels));
                ClearScreen();
            };
        });
        playerController.DisableControl();
    }

    /* ClEAR SCREEN */
    private void ClearScreen() {
//        dispose();
        playerController.EnableControl();
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
        for (EnemyController enemy : diedEnemies) {
            removeSceneFromSceneManager(enemy.getCharacter().getModelScene());
            bulletPhysicsSystem.removeBody(enemy.getCharacter().getBody());
        }
        diedEnemies.clear();

        if(levelScene != null) removeSceneFromSceneManager(levelScene);
        if(levelBody != null) bulletPhysicsSystem.removeBody(levelBody);
        resetTimer();
        bookCollected = 0;
        isInExamArea = false;
    }
}

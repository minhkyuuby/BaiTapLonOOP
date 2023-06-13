package com.dbdc.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dbdc.game.GameClass;
import com.jpcodes.physics.BulletPhysicsSystem;
import com.jpcodes.physics.controllers.camera.CameraController;
import com.jpcodes.physics.controllers.camera.FirstPersonCameraController;
import com.kotcrab.vis.ui.widget.VisLabel;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class PhysicScreen extends DefaultScreen{

    private static boolean drawDebug = false;

    protected PerspectiveCamera camera;
    protected CameraController cameraController;
    protected ModelBatch modelBatch;
    protected Array<ModelInstance> renderInstances;
    protected BulletPhysicsSystem bulletPhysicsSystem;

    private final Array<Color> colors;

    private final Stage stage;
    private final VisLabel fpsLabel;

    final float GRID_MIN = -100f;
    final float GRID_MAX = 100f;
    final float GRID_STEP = 10f;

    private SceneManager sceneManager;
    protected Array<ModelInstance> renderScenes;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private float time;
    private SceneSkybox skybox;
    private DirectionalLightEx light;


    public PhysicScreen(GameClass game) {
        super(game);
        sceneManager = new SceneManager();

        bulletPhysicsSystem = new BulletPhysicsSystem();

        camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 1f;
        camera.far = 200;
        camera.position.set(0,10, 50f);

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        fpsLabel = new VisLabel();
        fpsLabel.setPosition(10, 10);
        stage.addActor(fpsLabel);

        modelBatch = new ModelBatch();
        renderInstances = new Array<>();

        cameraController = new FirstPersonCameraController(camera);
        ((FirstPersonCameraController) cameraController).setVelocity(50f);
        ((FirstPersonCameraController) cameraController).setDegreesPerPixel(0.2f);
        Gdx.input.setInputProcessor(cameraController);

        colors = new Array<>();
        colors.add(Color.PURPLE);
        colors.add(Color.BLUE);
        colors.add(Color.TEAL);
        colors.add(Color.BROWN);
        colors.add(Color.FIREBRICK);

        // setup Scenes
        sceneManager.setCamera(camera);

        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);

    }

    @Override
    public void update(float delta) {
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//            game.setScreen(new LevelSelecting(game));
//            dispose();
//        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            drawDebug = !drawDebug;
        }


        bulletPhysicsSystem.update(delta);
        cameraController.update(delta);
        sceneManager.update(delta);
        ScreenUtils.clear(Color.BLACK, true);

        if (drawDebug) {
            bulletPhysicsSystem.render(camera);
        }
    }

    @Override
    public void draw(float delta) {
        sceneManager.render();
        stage.act();
        stage.draw();
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    protected void addSceneToSceneManager(Scene scene) {
        sceneManager.addScene(scene);
    }


    public void setCameraController(CameraController cameraController) {
        this.cameraController = cameraController;
        Gdx.input.setInputProcessor(cameraController);
    }

    protected void createFloor(float width, float height, float depth) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder meshBuilder = modelBuilder.part("floor", GL20.GL_TRIANGLES, VertexAttribute.Position().usage |VertexAttribute.Normal().usage | VertexAttribute.TexCoords(0).usage, new Material());

        BoxShapeBuilder.build(meshBuilder, width, height, depth);
        btBoxShape btBoxShape = new btBoxShape(new Vector3(width/2f, height/2f, depth/2f));
        Model floor = modelBuilder.end();

        ModelInstance floorInstance = new ModelInstance(floor);
        floorInstance.transform.trn(0, -0.5f, 0f);

        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(0, null, btBoxShape, Vector3.Zero);
        btRigidBody body = new btRigidBody(info);

        body.setWorldTransform(floorInstance.transform);

        renderInstances.add(floorInstance);
        bulletPhysicsSystem.addBody(body);
    }

    protected Color getRandomColor(){
        return colors.get(MathUtils.random(0, colors.size-1));
    }

    @Override
    public void dispose() {
        super.dispose();
        sceneManager.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
    }
}

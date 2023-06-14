package com.dbdc.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
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
import net.mgsx.gltf.scene3d.lights.DirectionalShadowLight;
import net.mgsx.gltf.scene3d.scene.CascadeShadowMap;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class PhysicScreen extends ScreenAdapter {

    private static boolean drawDebug = false;

    protected PerspectiveCamera camera;
    protected CameraController cameraController;
    protected ModelBatch modelBatch;
    protected Array<ModelInstance> renderInstances;
    protected BulletPhysicsSystem bulletPhysicsSystem;

    private final Stage stage;
    private final VisLabel fpsLabel;

    private SceneManager sceneManager;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private float time;
    private SceneSkybox skybox;
    private DirectionalLightEx light;

    protected GameClass game;

    CascadeShadowMap csm;
    DirectionalShadowLight shadowLight;
    public PhysicScreen(GameClass game) {
        this.game = game;
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
        sceneManager.setCascadeShadowMap(new CascadeShadowMap(1));
        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);
        csm = new CascadeShadowMap(2);
        sceneManager.setCascadeShadowMap(csm);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            drawDebug = !drawDebug;
        }

        bulletPhysicsSystem.update(delta);
        cameraController.update(delta);
        ScreenUtils.clear(Color.BLACK, true);
        if (drawDebug) {
            bulletPhysicsSystem.render(camera);
            modelBatch.begin(camera);
            modelBatch.render(renderInstances);
            modelBatch.end();
        }
        sceneManager.update(delta);
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

    @Override
    public void dispose() {
        super.dispose();
        sceneManager.dispose();
        stage.dispose();
        skybox.dispose();
        modelBatch.dispose();
        bulletPhysicsSystem.dispose();
    }
}

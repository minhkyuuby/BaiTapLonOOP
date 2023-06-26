package com.dbdc.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.dbdc.game.GameClass;
import com.dbdc.game.Screens.GamePlay;
import com.jpcodes.physics.utils.Utils3D;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class DaiSoGamePlay extends GamePlay {
    private SceneAsset levelAsset;
    public DaiSoGamePlay(GameClass game) {
        super(game);
        bulletPhysicsSystem.addBody(createLevelPlatform());
    }

    @Override
    public void dispose() {
        super.dispose();
        levelAsset.dispose();
    }

    private btRigidBody createLevelPlatform() {
        levelAsset = new GLTFLoader().load(Gdx.files.internal("models/level.gltf"));
        Scene levelScene = new Scene(levelAsset.scene);
        Model sceneModel = Utils3D.loadOBJ(Gdx.files.internal("models/level/level.obj"));
        ModelInstance sceneInstance = new ModelInstance(sceneModel);
        sceneInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.FOREST));

        addSceneToSceneManager(levelScene);
        renderInstances.add(sceneInstance);

        btCollisionShape shape = Bullet.obtainStaticNodeShape(sceneInstance.nodes);
        btRigidBody.btRigidBodyConstructionInfo sceneInfo = new btRigidBody.btRigidBodyConstructionInfo(0f, null, shape, Vector3.Zero);
        btRigidBody body = new btRigidBody(sceneInfo);
        sceneInfo.dispose();
        return body;
    }
}

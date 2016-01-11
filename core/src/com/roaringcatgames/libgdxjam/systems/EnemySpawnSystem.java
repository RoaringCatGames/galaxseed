package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.Z;
import com.roaringcatgames.libgdxjam.components.BulletComponent;
import com.roaringcatgames.libgdxjam.components.EnemyComponent;
import com.roaringcatgames.libgdxjam.components.WhenOffScreenComponent;

/**
 * Created by barry on 1/10/16 @ 7:35 PM.
 */
public class EnemySpawnSystem extends IteratingSystem {

    private boolean isInitialized = false;
    private float spawnRate = 0.25f;
    private float lastSpawnTime = 0f;
    private float timeElapsed = 0f;

    public EnemySpawnSystem() {
        super(Family.all(EnemyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float timeBetweenSpawn = 1f / spawnRate;
        timeElapsed += deltaTime;

        if (timeElapsed - lastSpawnTime >= timeBetweenSpawn) {

            lastSpawnTime = timeElapsed;
            generateEnemy(-5f, 30f, 5f, -8f);
            generateEnemy(25f, 30f, -5f, -8f);
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    /*******************
     * Private Methods
     *******************/
    private void generateEnemy(float xPos, float yPos, float xVel, float yVel){

            //Generate Bullets here
            Entity enemy = ((PooledEngine) getEngine()).createEntity();
            enemy.add(WhenOffScreenComponent.create());
            enemy.add(KinematicComponent.create());
            float rot = xVel > 0f ? 45f : -45f;
            enemy.add(TransformComponent.create()
                .setPosition(xPos, yPos, Z.enemy)
                .setScale(1f, 1f)
                .setRotation(rot));

            enemy.add(BoundsComponent.create()
                .setBounds(xPos - 2f, yPos - 2f, 4f, 4f)
                .setOffset(0f, 0));
            enemy.add(TextureComponent.create());
            enemy.add(AnimationComponent.create()
                .addAnimation("DEFAULT", new Animation(1f / 6f, Assets.getCometFrames(), Animation.PlayMode.LOOP_PINGPONG)));

            enemy.add(StateComponent.create()
                .set("DEFAULT")
                .setLooping(true));
            enemy.add(VelocityComponent.create()
                .setSpeed(xVel, yVel));
            getEngine().addEntity(enemy);
    }
}

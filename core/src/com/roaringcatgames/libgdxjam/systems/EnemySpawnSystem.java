package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.values.Damage;
import com.roaringcatgames.libgdxjam.Timer;
import com.roaringcatgames.libgdxjam.values.Z;
import com.roaringcatgames.libgdxjam.components.*;

import java.util.Random;

/**
 * Created by barry on 1/10/16 @ 7:35 PM.
 */
public class EnemySpawnSystem extends IteratingSystem {

    private boolean isInitialized = false;
    private Timer cometTimer = new Timer(0.5f);
//    private float spawnRate = 0.25f;  //comets/second
//    private float lastSpawnTime = 0f;
//    private float timeElapsed = 0f;

    private Timer asteroidTimer = new Timer(0.25f);

    private float asteroidX = -5f;
    private Random r;

    public EnemySpawnSystem() {
        super(Family.all(EnemyComponent.class).get());
        r = new Random();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        //Spawn Comets
        if(cometTimer.doesTriggerThisStep(deltaTime)){
            float leftPosition = (4f * r.nextFloat());
            generateEnemy(leftPosition, 50f);
            float rightPosition = (4f * r.nextFloat()) + 16f;  //16-20f
            generateEnemy(rightPosition, 50f);
        }

        //Spawn Asteroids
        if(asteroidTimer.doesTriggerThisStep(deltaTime)){
            float xVel = asteroidX < 0f ? 3f : -3f;
            generateAsteroid(asteroidX, 35f, xVel, -2f);
            asteroidX = asteroidX < 0f ? 33f : -8f;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    /*******************
     * Private Methods
     *******************/
    private void generateAsteroid(float xPos, float yPos, float xVel, float yVel){
        //Generate Bullets here
        Entity enemy = ((PooledEngine) getEngine()).createEntity();
        enemy.add(WhenOffScreenComponent.create());
        enemy.add(KinematicComponent.create());
        enemy.add(ProjectileComponent.create()
                .setDamage(Damage.asteroid));


        enemy.add(TransformComponent.create()
                .setPosition(xPos, yPos, Z.enemy)
                .setScale(1f, 1f));
        float rotSpeed = xVel > 0f ? 180f : -180f;
        enemy.add(RotationComponent.create()
                .setRotationSpeed(rotSpeed));



        SpawnerComponent spawner = SpawnerComponent.create();


        float cnt = r.nextFloat();
        float size;
        float health;
        TextureRegion tr;
        EnemyType eType;
        if(cnt < 0.33f) {
            tr = Assets.getAsteroidA();
            eType = EnemyType.ASTEROID_A;
            size = 2.5f;
            health = 50f;

            spawner.setParticleSpeed(15f)
                .setParticleTextures(Assets.getAsteroidAFrags())
                .setStrategy(SpawnStrategy.ALL_DIRECTIONS)
                .setSpawnRate(2f);
        }else if(cnt < 0.66f){
            tr = Assets.getAsteroidB();
            eType = EnemyType.ASTEROID_B;
            size = 3.75f;
            health = 80f;

            spawner.setParticleSpeed(18f)
                .setParticleTextures(Assets.getAsteroidBFrags())
                .setStrategy(SpawnStrategy.ALL_DIRECTIONS)
                .setSpawnRate(2.5f);
        }else{
            tr = Assets.getAsteroidC();
            eType = EnemyType.ASTEROID_C;
            size = 5f;
            health = 110f;

            spawner.setParticleSpeed(20f)
                .setParticleTextures(Assets.getAsteroidCFrags())
                .setStrategy(SpawnStrategy.ALL_DIRECTIONS)
                .setSpawnRate(3f);
        }
        enemy.add(spawner);
        enemy.add(HealthComponent.create()
            .setHealth(health)
            .setMaxHealth(health));
        enemy.add(CircleBoundsComponent.create()
                .setCircle(xPos, yPos, size / 2f));
        enemy.add(TextureComponent.create()
            .setRegion(tr));
        enemy.add(EnemyComponent.create()
                .setEnemyType(eType));
        enemy.add(VelocityComponent.create()
                .setSpeed(xVel, yVel));
        getEngine().addEntity(enemy);
    }

    private void generateEnemy(float xPos, float yPos){

            boolean isGoingRight = xPos < 10f;
            //Generate Bullets here
            Entity enemy = ((PooledEngine) getEngine()).createEntity();
            enemy.add(WhenOffScreenComponent.create());
            enemy.add(KinematicComponent.create());
            enemy.add(ProjectileComponent.create()
                .setDamage(Damage.comet));
            enemy.add(EnemyComponent.create());

            enemy.add(TransformComponent.create()
                .setPosition(xPos, yPos, Z.enemy)
                .setScale(1f, 1f));

            enemy.add(HealthComponent.create()
                .setMaxHealth(10f)
                .setMaxHealth(10f));

            enemy.add(CircleBoundsComponent.create()
                .setCircle(xPos, yPos, 0.25f)
                .setOffset(0f, -1.25f));

            Array<TextureAtlas.AtlasRegion> frames = r.nextFloat() > 0.5f ? Assets.getRedCometFrames() : Assets.getBlueCometFrames();
            enemy.add(TextureComponent.create());
            enemy.add(AnimationComponent.create()
                .addAnimation("DEFAULT", new Animation(1f / 12f, frames, Animation.PlayMode.LOOP_PINGPONG)));

            enemy.add(StateComponent.create()
                .set("DEFAULT")
                .setLooping(true));

            Vector2 p0 = new Vector2(xPos, yPos);
            float p1x = isGoingRight ? -4.22f : 24.22f;
            Vector2 p1 = new Vector2(p1x, 0f);
            float p2x = isGoingRight ? 42.25f : -22.25f;
            Vector2 p2 = new Vector2(p2x, -32f);
            enemy.add(PathFollowComponent.create()
                .setFacingPath(true)
                .setBaseRotation(180f)
                .setTotalPathTime(6f)
                .setPath(new Bezier<>(p0, p1, p2)));

            getEngine().addEntity(enemy);
    }
}

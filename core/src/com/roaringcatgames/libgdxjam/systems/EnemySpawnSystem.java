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
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.values.Damage;
import com.roaringcatgames.libgdxjam.Timer;
import com.roaringcatgames.libgdxjam.values.Health;
import com.roaringcatgames.libgdxjam.values.Z;
import com.roaringcatgames.libgdxjam.components.*;

import java.util.Random;

/**
 * Created by barry on 1/10/16 @ 7:35 PM.
 */
public class EnemySpawnSystem extends IteratingSystem {

    private static float LeftCometSpawnFrequency = 0.4f;
    private static float RightCometSpawnFrequency = 0.5f;
    private static float CometY = 50f;
    private static float CometXRange = 7f;

    private static float AsteroidSpawnFrequency = 0.25f;
    private static float AsteroidLeftX = -8f;
    private static float AsteroidRightX = 33f;
    private static float AsteroidXVelocity = 3f;
    private static float AsteroidYVelocity = -2f;
    private static float AsteroidY = 35f;
    private static float AsteroidRotationSpeed = 180f;
    private static float AsteroidFragSpeed = 15f;

    private Random r = new Random();
    private Timer leftTimer = new Timer(LeftCometSpawnFrequency);
    private Timer rightTimer = new Timer(RightCometSpawnFrequency);
    private Timer asteroidTimer = new Timer(AsteroidSpawnFrequency);
    private float asteroidX = AsteroidLeftX;

    public EnemySpawnSystem() {
        super(Family.all(EnemyComponent.class).get());
        leftTimer.elapsedTime += RightCometSpawnFrequency/2f;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        //Spawn Comets
        if(leftTimer.doesTriggerThisStep(deltaTime)) {
            float leftPosition = (CometXRange * r.nextFloat());
            generateEnemy(leftPosition, CometY);
        }

        if(rightTimer.doesTriggerThisStep(deltaTime)){
            float rightPosition = (CometXRange * r.nextFloat()) + (App.W - CometXRange);
            generateEnemy(rightPosition, CometY);
        }

        //Spawn Asteroids
        if(asteroidTimer.doesTriggerThisStep(deltaTime)){
            float xVel = asteroidX < 0f ? AsteroidXVelocity : -AsteroidXVelocity;
            generateAsteroid(asteroidX, AsteroidY, xVel, AsteroidYVelocity);
            asteroidX = asteroidX < 0f ? AsteroidRightX : AsteroidLeftX;
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
        float rotSpeed = xVel > 0f ? AsteroidRotationSpeed : -AsteroidRotationSpeed;
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
            health = Health.AsteroidA;

            spawner.setParticleSpeed(AsteroidFragSpeed)
                .setParticleTextures(Assets.getAsteroidAFrags())
                .setStrategy(SpawnStrategy.ALL_DIRECTIONS)
                .setSpawnRate(2f);
        }else if(cnt < 0.66f){
            tr = Assets.getAsteroidB();
            eType = EnemyType.ASTEROID_B;
            size = 3.75f;
            health = Health.AsteroidB;

            spawner.setParticleSpeed(AsteroidFragSpeed + 3f)
                .setParticleTextures(Assets.getAsteroidBFrags())
                .setStrategy(SpawnStrategy.ALL_DIRECTIONS)
                .setSpawnRate(2.5f);
        }else{
            tr = Assets.getAsteroidC();
            eType = EnemyType.ASTEROID_C;
            size = 5f;
            health = Health.AsteroidC;

            spawner.setParticleSpeed(AsteroidFragSpeed + 5f)
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

            boolean isGoingRight = xPos < App.W/2f;
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
                .setMaxHealth(Health.Comet)
                .setMaxHealth(Health.Comet));

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
                    .setTotalPathTime(8f)
                    .setPath(new Bezier<>(p0, p1, p2)));

            getEngine().addEntity(enemy);
    }
}

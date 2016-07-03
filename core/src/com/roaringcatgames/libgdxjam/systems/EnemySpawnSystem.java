package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.data.EnemySpawn;
import com.roaringcatgames.libgdxjam.data.EnemySpawns;
import com.roaringcatgames.libgdxjam.values.Colors;
import com.roaringcatgames.libgdxjam.values.Damage;
import com.roaringcatgames.libgdxjam.Timer;
import com.roaringcatgames.libgdxjam.values.Health;
import com.roaringcatgames.libgdxjam.values.Z;
import com.roaringcatgames.libgdxjam.components.*;

import java.util.Random;

/**
 * System responsible for spawning enemy entities and applying their components.
 */
public class EnemySpawnSystem extends IteratingSystem {

    private static final float ASTEROID_A_PU_CHANCE = 0.1f;
    private static final float ASTEROID_B_PU_CHANCE = 0.2f;
    private static final float ASTEROID_C_PU_CHANCE = 0.4f;

    private static float elapsedTime = 0f;
    private static float homingChance = 0.1f;

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
    private static float AsteroidRotationSpeed = 45f;
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

        elapsedTime += deltaTime;

        if(elapsedTime > 300f) {
            homingChance = 0.5f;
        }if(elapsedTime > 120f) {
            homingChance = 0.4f;
        }else if(elapsedTime > 60f){
            homingChance = 0.3f;
        }else if(elapsedTime > 30f){
            homingChance = 0.2f;
        }

//        for(EnemySpawn spawn: EnemySpawns.getLevelOneSpawns()){
//            if(!spawn.hasSpawned && spawn.spawnTime <= elapsedTime){
//                if(spawn.enemyType == EnemyType.COMET){
//                    generateComet(spawn.startPosition.x, spawn.startPosition.y);
//                }else {
//                    generateAsteroid(spawn.enemyType, spawn.startPosition.x, spawn.startPosition.y,
//                            spawn.speed.x, spawn.speed.y);
//                }
//
//                spawn.hasSpawned = true;
//            }
//        }

        //Spawn Comets
        if(leftTimer.doesTriggerThisStep(deltaTime)) {
            float leftPosition = (CometXRange * r.nextFloat());
            generateComet(leftPosition, CometY);
        }

        if(rightTimer.doesTriggerThisStep(deltaTime)){
            float rightPosition = (CometXRange * r.nextFloat()) + (App.W - CometXRange);
            generateComet(rightPosition, CometY);
        }

        //Spawn Asteroids
        if(asteroidTimer.doesTriggerThisStep(deltaTime)){
            float xVel = asteroidX < 0f ? AsteroidXVelocity : -AsteroidXVelocity;
            float rnd = r.nextFloat();
            EnemyType eType = rnd < getChance(EnemyType.ASTEROID_A) ? EnemyType.ASTEROID_A :
                              rnd < getChance(EnemyType.ASTEROID_B) ? EnemyType.ASTEROID_B :
                                                                        EnemyType.ASTEROID_C;
            generateAsteroid(eType, asteroidX, AsteroidY, xVel, AsteroidYVelocity);
            //Randomize left and right
            asteroidX =  r.nextFloat() < 0.5f ? AsteroidRightX : AsteroidLeftX;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    private float getChance(EnemyType et){
        float chance = 0f;
        switch(et){
            case ASTEROID_A:
                chance = elapsedTime < 30f ? 1f : 0.5f;
                break;
            case ASTEROID_B:
                chance = elapsedTime < 60f ? 1f : 0.8f;
                break;
        }

        return chance;
    }

    /*******************
     * Private Methods
     *******************/
    private void generateAsteroid(EnemyType eType, float xPos, float yPos, float xVel, float yVel){
        //Generate Bullets here
        PooledEngine engine = (PooledEngine)getEngine();
        Entity enemy = engine.createEntity();
        enemy.add(WhenOffScreenComponent.create(engine));
        enemy.add(KinematicComponent.create(engine));
        enemy.add(ProjectileComponent.create(engine)
                .setDamage(Damage.asteroid));

        float rotSpeed = xVel > 0f ? AsteroidRotationSpeed : -AsteroidRotationSpeed;
        enemy.add(RotationComponent.create(engine)
                .setRotationSpeed(rotSpeed));



        SpawnerComponent spawner = SpawnerComponent.create(engine);
        float stratWeight = r.nextFloat();
        float size;
        float health;
        TextureRegion tr;
        //EnemyType eType;
        SpawnStrategy strat = SpawnStrategy.ALL_DIRECTIONS;

        boolean shouldGeneratePowerup = false;

        EnemyColor eColor;
        Color assColor;
        switch(eType){
            case ASTEROID_A:
                tr = Assets.getAsteroidA();
                eColor = EnemyColor.BROWN;
                size = 2.5f;
                health = Health.AsteroidA;
                assColor = Colors.BROWN_ASTEROID;
                shouldGeneratePowerup = r.nextFloat() <= ASTEROID_A_PU_CHANCE;
                strat = stratWeight < (homingChance/4f) ? SpawnStrategy.HOMING_TO_PLAYER : SpawnStrategy.ALL_DIRECTIONS;
                spawner.setParticleSpeed(AsteroidFragSpeed)
                        .setParticleTextures(Assets.getFrags())
                        .setStrategy(strat)
                        .setSpawnRate(1f);
                break;
            case ASTEROID_B:
                tr = Assets.getAsteroidB();
                eColor = EnemyColor.BLUE;
                size = 3.75f;
                health = Health.AsteroidB;
                assColor = Colors.BLUE_ASTEROID;
                shouldGeneratePowerup = r.nextFloat() <= ASTEROID_B_PU_CHANCE;
                strat = stratWeight < (homingChance/2f) ? SpawnStrategy.HOMING_TO_PLAYER : SpawnStrategy.ALL_DIRECTIONS;
                spawner.setParticleSpeed(AsteroidFragSpeed + 3f)
                    .setParticleTextures(Assets.getFrags())
                    .setStrategy(strat)
                    .setSpawnRate(2.5f);
                break;
            case ASTEROID_C:
                tr = Assets.getAsteroidC();
                eColor = EnemyColor.PURPLE;
                size = 5f;
                health = Health.AsteroidC;
                assColor = Colors.PURPLE_ASTEROID;

                shouldGeneratePowerup = r.nextFloat() <= ASTEROID_C_PU_CHANCE;
                strat = stratWeight < homingChance ? SpawnStrategy.HOMING_TO_PLAYER : SpawnStrategy.ALL_DIRECTIONS;
                float spawnRate = r.nextFloat() < 0.1f ? 10f: 4f;
                spawner.setParticleSpeed(AsteroidFragSpeed + 5f)
                    .setParticleTextures(Assets.getFrags())
                    .setStrategy(strat)
                    .setSpawnRate(spawnRate);
                break;
            default:
                tr = Assets.getAsteroidA();
                eColor = EnemyColor.BROWN;
                size = 2.5f;
                health = Health.AsteroidA;
                assColor = Colors.BROWN_ASTEROID;

                strat = stratWeight < (homingChance/4f) ? SpawnStrategy.HOMING_TO_PLAYER : SpawnStrategy.ALL_DIRECTIONS;
                spawner.setParticleSpeed(AsteroidFragSpeed)
                        .setParticleTextures(Assets.getFrags())
                        .setStrategy(strat)
                        .setSpawnRate(1f);
                break;
        }

        enemy.add(TransformComponent.create(engine)
                .setPosition(xPos, yPos, Z.enemy)
                .setScale(1f, 1f)
                .setTint(assColor));
        enemy.add(spawner);
        enemy.add(HealthComponent.create(engine)
            .setHealth(health)
            .setMaxHealth(health));
        enemy.add(CircleBoundsComponent.create(engine)
                .setCircle(xPos, yPos, size / 2f));
        enemy.add(TextureComponent.create(engine)
            .setRegion(tr));
        enemy.add(EnemyComponent.create(engine)
            .setEnemyType(eType)
            .setEnemyColor(eColor)
            .setShouldGeneratePowerup(shouldGeneratePowerup));
        enemy.add(VelocityComponent.create(engine)
                .setSpeed(xVel, yVel));

        getEngine().addEntity(enemy);
    }

    private void generateComet(float xPos, float yPos){
        PooledEngine engine = (PooledEngine) getEngine();
        boolean isGoingRight = xPos < App.W/2f;
        float cometR = r.nextFloat();
        boolean isRed = cometR > 0.5f;
        Animation ani = isRed ? Animations.getRedComet() : Animations.getBlueComet();
        Animation aniFull = isRed ? Animations.getRedCometFull() : Animations.getBlueCometFull();
        EnemyColor color = isRed ? EnemyColor.BROWN:EnemyColor.BLUE;

        Entity enemy = engine.createEntity();
        enemy.add(WhenOffScreenComponent.create(engine));
        enemy.add(KinematicComponent.create(engine));
        enemy.add(ProjectileComponent.create(engine)
            .setDamage(Damage.comet));

        enemy.add(TransformComponent.create(engine)
            .setPosition(xPos, yPos, Z.enemy)
            .setScale(1f, 1f));

        enemy.add(HealthComponent.create(engine)
            .setMaxHealth(Health.Comet)
            .setMaxHealth(Health.Comet));

        enemy.add(CircleBoundsComponent.create(engine)
            .setCircle(xPos, yPos, 0.4f)
            .setOffset(0f, -1.25f));


        enemy.add(EnemyComponent.create(engine)
            .setEnemyType(EnemyType.COMET)
            .setEnemyColor(color));
        enemy.add(TextureComponent.create(engine));
        enemy.add(AnimationComponent.create(engine)
            .addAnimation("DEFAULT", ani)
            .addAnimation("FULL", aniFull));

        enemy.add(StateComponent.create(engine)
            .set("DEFAULT")
            .setLooping(true));

        Vector2 p0 = new Vector2(xPos, yPos);
        float p1x = isGoingRight ? -4.22f : 24.22f;
        Vector2 p1 = new Vector2(p1x, 0f);
        float p2x = isGoingRight ? 42.25f : -22.25f;
        Vector2 p2 = new Vector2(p2x, -32f);
        enemy.add(PathFollowComponent.create(engine)
                .setFacingPath(true)
                .setBaseRotation(180f)
                .setSpeed(1f/8f)
                .setPath(new Bezier<>(p0, p1, p2)));

        getEngine().addEntity(enemy);
    }
}

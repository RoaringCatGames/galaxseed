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
import com.roaringcatgames.libgdxjam.Animations;
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
        PooledEngine engine = (PooledEngine)getEngine();
        Entity enemy = engine.createEntity();
        enemy.add(WhenOffScreenComponent.create(engine));
        enemy.add(KinematicComponent.create(engine));
        enemy.add(ProjectileComponent.create(engine)
                .setDamage(Damage.asteroid));


        enemy.add(TransformComponent.create(engine)
                .setPosition(xPos, yPos, Z.enemy)
                .setScale(1f, 1f));
        float rotSpeed = xVel > 0f ? AsteroidRotationSpeed : -AsteroidRotationSpeed;
        enemy.add(RotationComponent.create(engine)
                .setRotationSpeed(rotSpeed));



        SpawnerComponent spawner = SpawnerComponent.create(engine);
        float cnt = r.nextFloat();
        float size;
        float health;
        TextureRegion tr;
        EnemyType eType;
        EnemyColor eColor;
        if(cnt < 0.33f) {
            tr = Assets.getAsteroidA();
            eType = EnemyType.ASTEROID_A;
            eColor = EnemyColor.BROWN;
            size = 2.5f;
            health = Health.AsteroidA;

            spawner.setParticleSpeed(AsteroidFragSpeed)
                .setParticleTextures(Assets.getAsteroidAFrags())
                .setStrategy(SpawnStrategy.ALL_DIRECTIONS)
                .setSpawnRate(2f);
        }else if(cnt < 0.66f){
            tr = Assets.getAsteroidB();
            eType = EnemyType.ASTEROID_B;
            eColor = EnemyColor.BLUE;
            size = 3.75f;
            health = Health.AsteroidB;

            spawner.setParticleSpeed(AsteroidFragSpeed + 3f)
                .setParticleTextures(Assets.getAsteroidBFrags())
                .setStrategy(SpawnStrategy.ALL_DIRECTIONS)
                .setSpawnRate(2.5f);
        }else{
            tr = Assets.getAsteroidC();
            eType = EnemyType.ASTEROID_C;
            eColor = EnemyColor.PURPLE;
            size = 5f;
            health = Health.AsteroidC;

            float spawnRate = r.nextFloat() < 0.1f ? 10f: 4f;
            spawner.setParticleSpeed(AsteroidFragSpeed + 5f)
                .setParticleTextures(Assets.getAsteroidCFrags())
                .setStrategy(SpawnStrategy.ALL_DIRECTIONS)
                .setSpawnRate(spawnRate);
        }
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
            .setEnemyColor(eColor));
        enemy.add(VelocityComponent.create(engine)
                .setSpeed(xVel, yVel));

        getEngine().addEntity(enemy);
    }

    private void generateEnemy(float xPos, float yPos){
        PooledEngine engine = (PooledEngine) getEngine();
        boolean isGoingRight = xPos < App.W/2f;
        float cometR = r.nextFloat();
        boolean isRed = cometR > 0.5f;
        Animation ani = isRed ? Animations.getRedComet() : Animations.getBlueComet();
        Animation aniFull = isRed ? Animations.getRedCometFull() : Animations.getBlueCometFull();
        EnemyColor color = isRed ? EnemyColor.BROWN:EnemyColor.BLUE;

        //Generate Bullets here
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

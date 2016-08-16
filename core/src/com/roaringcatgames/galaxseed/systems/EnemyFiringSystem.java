package com.roaringcatgames.galaxseed.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.VectorUtils;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.values.Colors;
import com.roaringcatgames.galaxseed.values.Damage;
import com.roaringcatgames.galaxseed.values.Health;
import com.roaringcatgames.galaxseed.values.Z;
import com.roaringcatgames.galaxseed.components.*;

import java.util.Random;

/**
 * Created by barry on 1/14/16 @ 5:59 PM.
 */
public class EnemyFiringSystem extends IteratingSystem {

    private Vector2 spawnVelocity = new Vector2(0f, 0f);
    private Array<Entity> spawners;
    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<SpawnerComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<VelocityComponent> vm;
    private Random r;

    private Vector2[] baseVectors = new Vector2[]{
            new Vector2(1, 0),
            new Vector2(0.5f, 0.5f),
            new Vector2(0, 1),
            new Vector2(-0.5f, 0.5f),
            new Vector2(-1, 0),
            new Vector2(-0.5f, -0.5f),
            new Vector2(0, -1),
            new Vector2(0.5f, -0.5f)
    };

    public EnemyFiringSystem(){
        super(Family.all(SpawnerComponent.class).get());
        em = ComponentMapper.getFor(EnemyComponent.class);
        sm = ComponentMapper.getFor(SpawnerComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        vm = ComponentMapper.getFor(VelocityComponent.class);
        spawners = new Array<>();
        r = new Random();
    }



    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        PooledEngine engine = ((PooledEngine)getEngine());

        for(Entity spawner:spawners){
            EnemyColor parentColor = em.has(spawner) ? em.get(spawner).enemyColor : EnemyColor.BROWN;
            Color fragColor = parentColor == EnemyColor.BROWN ? Colors.BROWN_ASTEROID :
                              parentColor == EnemyColor.BLUE ? Colors.BLUE_ASTEROID :
                                                               Colors.PURPLE_ASTEROID;
            SpawnerComponent sc = sm.get(spawner);
            if(sc.isPaused){
                continue;
            }
            TransformComponent tc = tm.get(spawner);
            float secsBetweenSpawns = 1f/sc.spawnRate;
            sc.elapsedTime += deltaTime;

            if(sc.elapsedTime - sc.lastSpawnTime >= secsBetweenSpawns) {
                sc.lastSpawnTime = sc.elapsedTime;
                switch (sc.strategy) {
                    case ALL_DIRECTIONS:
                        for (Vector2 base : baseVectors) {
                            spawnVelocity.set(VectorUtils.rotateVector(base.cpy(), tc.rotation));
                            spawnVelocity.scl(sc.particleSpeed);
                            int target = r.nextInt(sc.particleTextures.size);

                            Entity frag = engine.createEntity();
                            frag.add(HealthComponent.create(engine)
                                .setMaxHealth(Health.AsteroidFrag)
                                .setMaxHealth(Health.AsteroidFrag));
                            frag.add(EnemyComponent.create(engine)
                                .setEnemyType(EnemyType.ASTEROID_FRAG)
                                .setEnemyColor(parentColor));
                            frag.add(WhenOffScreenComponent.create(engine)
                                    .setHasBeenOnScreen(true));
                            frag.add(TextureComponent.create(engine)
                                    .setRegion(sc.particleTextures.get(target)));
                            frag.add(TransformComponent.create(engine)
                                    .setPosition(tc.position.x, tc.position.y, Z.enemyParticle)
                                    .setTint(fragColor));
                            frag.add(VelocityComponent.create(engine)
                                    .setSpeed(spawnVelocity.x, spawnVelocity.y));
                            frag.add(RotationComponent.create(engine)
                                    .setRotationSpeed(sc.particleSpeed * 10f));
                            frag.add(CircleBoundsComponent.create(engine)
                                    .setCircle(tc.position.x,
                                            tc.position.y,
                                            0.375f));
                            frag.add(ProjectileComponent.create(engine)
                                    .setDamage(Damage.asteroidRock));
                            frag.add(KinematicComponent.create(engine));
                            engine.addEntity(frag);
                        }
                        break;
                    case RANDOM_DIRECTIONS:

                        break;
                    case HOMING_TO_PLAYER:
                        spawnVelocity.set(App.playerLastPosition.cpy().sub(tc.position.x, tc.position.y));
                        int target = r.nextInt(sc.particleTextures.size);
                        Entity frag = engine.createEntity();
                        frag.add(HealthComponent.create(engine)
                                .setMaxHealth(Health.AsteroidFrag)
                                .setMaxHealth(Health.AsteroidFrag));
                        frag.add(EnemyComponent.create(engine)
                                .setEnemyType(EnemyType.ASTEROID_FRAG)
                                .setEnemyColor(parentColor));
                        frag.add(WhenOffScreenComponent.create(engine)
                                .setHasBeenOnScreen(true));
                        frag.add(TextureComponent.create(engine)
                                .setRegion(sc.particleTextures.get(target)));
                        frag.add(TransformComponent.create(engine)
                                .setPosition(tc.position.x, tc.position.y, Z.enemyParticle)
                                .setTint(fragColor));
                        frag.add(VelocityComponent.create(engine)
                                .setSpeed(spawnVelocity.x, spawnVelocity.y));
                        frag.add(RotationComponent.create(engine)
                                .setRotationSpeed(sc.particleSpeed * 10f));
                        frag.add(CircleBoundsComponent.create(engine)
                                .setCircle(tc.position.x,
                                        tc.position.y,
                                        0.375f));
                        frag.add(ProjectileComponent.create(engine)
                                .setDamage(Damage.asteroidRock));
                        frag.add(KinematicComponent.create(engine));
                        engine.addEntity(frag);
                        break;
                }
            }
        }

        spawners.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        spawners.add(entity);
    }
}

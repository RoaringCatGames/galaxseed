package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.VectorUtils;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.values.Damage;
import com.roaringcatgames.libgdxjam.values.Health;
import com.roaringcatgames.libgdxjam.values.Z;
import com.roaringcatgames.libgdxjam.components.*;

import java.util.Random;

/**
 * Created by barry on 1/14/16 @ 5:59 PM.
 */
public class EnemyFiringSystem extends IteratingSystem {

    private Array<Entity> spawners;
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
                            Vector2 spawnVel = VectorUtils.rotateVector(base.cpy(), tc.rotation);
                            spawnVel.scl(sc.particleSpeed);
                            int target = r.nextInt(sc.particleTextures.size);

                            Entity frag = engine.createEntity();
                            frag.add(HealthComponent.create(engine)
                                .setMaxHealth(Health.AsteroidFrag)
                                .setMaxHealth(Health.AsteroidFrag));
                            frag.add(EnemyComponent.create(engine)
                                    .setEnemyType(EnemyType.ASTEROID_FRAG));
                            frag.add(WhenOffScreenComponent.create(engine)
                                    .setHasBeenOnScreen(true));
                            frag.add(TextureComponent.create(engine)
                                    .setRegion(sc.particleTextures.get(target)));
                            frag.add(TransformComponent.create(engine)
                                    .setPosition(tc.position.x, tc.position.y, Z.enemyParticle));
                            frag.add(VelocityComponent.create(engine)
                                    .setSpeed(spawnVel.x, spawnVel.y));
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

package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.components.*;
import com.roaringcatgames.libgdxjam.values.Damage;

/**
 * System to apply damage to Enemies via helicopter Seeds
 */
public class HelicopterSeedSystem extends IteratingSystem{
    private Array<Entity> enemies = new Array<>();
    private Array<Entity> heliSeeds = new Array<>();
    private Entity aura;
    private ScoreComponent scoreCard;

    public HelicopterSeedSystem(){
        super(Family.one(EnemyComponent.class, HelicopterSeedComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        PooledEngine engine = (PooledEngine)getEngine();

        for(Entity h:heliSeeds){
            MultiBoundsComponent seedBounds = K2ComponentMappers.multiBounds.get(h);
            DamageComponent seedDmg = K2ComponentMappers.damage.get(h);

            for(Entity e:enemies){
                EnemyComponent ec = Mappers.enemy.get(e);
                CircleBoundsComponent eBounds = K2ComponentMappers.circleBounds.get(e);
                HealthComponent eHealth = K2ComponentMappers.health.get(e);
                if(eHealth.health > 0f) {
                    for (Bound b : seedBounds.bounds) {
                        if (b.isCircle && b.circle.overlaps(eBounds.circle)) {
                            float newHealth = Math.max(0f, eHealth.health - seedDmg.dps);
                            int plants = (int) Math.ceil(eHealth.health - newHealth);
                            eHealth.setHealth(newHealth);
                            for (int i = 0; i < plants; i++) {
                                EnemyDamageUtil.attachPlant(engine, b.circle, e, ec.enemyType == EnemyType.COMET);
                            }

                            if (eHealth.health == 0f){
                                EnemyDamageUtil.processEnemyDefeated(e, engine);
                            }else{
                                if(Mappers.status.has(e)){
                                    Mappers.status.get(e).setStatus(StatusComponent.EntityStatus.HELI_STUNNED)
                                                         .setTimeInStatus(0f);
                                }else{
                                    e.add(StatusComponent.create(engine)
                                        .setStatus(StatusComponent.EntityStatus.HELI_STUNNED));
                                }
                            }
                            break;
                        }
                    }
                }

            }

        }

        enemies.clear();
        heliSeeds.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(Mappers.enemy.has(entity)){
            //Ignore enemies currently stunned
            if(!Mappers.status.has(entity) ||
                    Mappers.status.get(entity).status != StatusComponent.EntityStatus.HELI_STUNNED){
                enemies.add(entity);
            }

        }else{
            heliSeeds.add(entity);
        }
    }
}

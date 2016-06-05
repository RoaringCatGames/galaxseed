package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.components.*;

/**
 * System to apply damage to Enemies via helicopter Seeds
 */
public class HelicopterSeedSystem extends IteratingSystem{
    private Array<Entity> enemies = new Array<>();
    private Array<Entity> heliSeeds = new Array<>();
    private ScoreComponent scoreCard;
    private Entity player;
    private Vector2 ricochetDirection = new Vector2();

    public HelicopterSeedSystem(){
        super(Family.one(PlayerComponent.class, EnemyComponent.class, HelicopterSeedComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        PooledEngine engine = (PooledEngine)getEngine();

        ImmutableArray<Entity> scores = engine.getEntitiesFor(Family.all(ScoreComponent.class).get());
        if(scores != null && scores.size() > 0){
            scoreCard = scores.first().getComponent(ScoreComponent.class);
        }

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

                            if (scoreCard != null) {
                                scoreCard.setScore(scoreCard.score + plants);
                            }

                            TransformComponent playerPos = K2ComponentMappers.transform.get(player);
                            if(playerPos != null){
                                if(K2ComponentMappers.velocity.has(e)) {
                                    VelocityComponent vc = K2ComponentMappers.velocity.get(e);
                                    ricochetDirection.set(eBounds.circle.x, eBounds.circle.y);
                                    ricochetDirection.sub(playerPos.position.x, playerPos.position.y);
                                    ricochetDirection.nor().scl(Math.abs(vc.speed.x), Math.abs(vc.speed.y));
                                    vc.speed.set(ricochetDirection);
                                }else{
                                    PathFollowComponent pfc = K2ComponentMappers.pathFollow.get(e);
                                    pfc.setSpeed(pfc.speed*-1f);
                                }

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

        }else if(Mappers.helicopterSeed.has(entity)) {
            heliSeeds.add(entity);
        }else{
            player = entity;
        }
    }
}

package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.components.EnemyComponent;
import com.roaringcatgames.libgdxjam.components.Mappers;
import com.roaringcatgames.libgdxjam.components.PollenAuraComponent;
import com.roaringcatgames.libgdxjam.values.Damage;
import com.roaringcatgames.libgdxjam.values.Health;
import com.roaringcatgames.libgdxjam.values.Rates;

/**
 * Applies logic for PollenAura
 */
public class PollenAuraSystem extends IteratingSystem {

    private Array<Entity> enemies = new Array<>();
    private Entity aura;

    public PollenAuraSystem(){
        super(Family.one(EnemyComponent.class, PollenAuraComponent.class).get());

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(aura != null && K2ComponentMappers.circleBounds.has(aura)) {
            CircleBoundsComponent auraBounds = K2ComponentMappers.circleBounds.get(aura);
            DamageComponent auraDamage = K2ComponentMappers.damage.get(aura);

            for(Entity e:enemies){
                TransformComponent epos = K2ComponentMappers.transform.get(e);
                EnemyComponent ec = Mappers.enemy.get(e);
                VelocityComponent enemyVel = K2ComponentMappers.velocity.get(e);


                if(!ec.isPollenated && auraBounds.circle.contains(epos.position.x, epos.position.y)){
                    ec.setPollenated(true);
                    //Quarter the Speed
                    if(enemyVel != null) {
                        enemyVel.speed.scl(Rates.AURA_SLOWDOWN_RATE);
                    }else{
                        //PathFollow
                        PathFollowComponent pfc = K2ComponentMappers.pathFollow.get(e);
                        pfc.setSpeed(pfc.speed * Rates.AURA_SLOWDOWN_RATE);
                    }

                }else if(ec.isPollenated && !auraBounds.circle.contains(epos.position.x, epos.position.y)){
                    ec.setPollenated(false);
                    //Restore
                    if(enemyVel != null) {
                        enemyVel.speed.scl(1f/Rates.AURA_SLOWDOWN_RATE);
                    }else{
                        //PathFollow
                        PathFollowComponent pfc = K2ComponentMappers.pathFollow.get(e);
                        pfc.setSpeed(pfc.speed / Rates.AURA_SLOWDOWN_RATE);
                    }
                }

                if(ec.isPollenated){
                    HealthComponent eh = K2ComponentMappers.health.get(e);

                    eh.health = Math.max(0f, eh.health - (auraDamage.dps*deltaTime));
                    //TODO: Plant trees randomly around enemy edge
                }
            }

            enemies.clear();
            aura = null;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(Mappers.enemy.has(entity)){
            enemies.add(entity);
        }else{
            aura = entity;
        }

    }
}

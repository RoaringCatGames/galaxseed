package com.roaringcatgames.galaxseed.systems;

import aurelienribon.tweenengine.Tween;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.galaxseed.components.*;
import com.roaringcatgames.galaxseed.values.Colors;
import com.roaringcatgames.galaxseed.values.Rates;

/**
 * Applies logic for PollenAura
 */
public class PollenAuraSystem extends IteratingSystem {

    private Array<Entity> enemies = new Array<>();
    private Entity aura;
    private ScoreComponent scoreCard;

    public PollenAuraSystem(){
        super(Family.one(EnemyComponent.class, PollenAuraComponent.class).get());

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(aura != null) {

            ImmutableArray<Entity> scores = getEngine().getEntitiesFor(Family.all(ScoreComponent.class).get());
            if(scores != null && scores.size() > 0){
                scoreCard = scores.first().getComponent(ScoreComponent.class);
            }

            CircleBoundsComponent auraBounds = K2ComponentMappers.circleBounds.get(aura);
            DamageComponent auraDamage = K2ComponentMappers.damage.get(aura);

            for(Entity e:enemies){
                CircleBoundsComponent enemyBounds = K2ComponentMappers.circleBounds.get(e);
                EnemyComponent ec = Mappers.enemy.get(e);
                VelocityComponent enemyVel = K2ComponentMappers.velocity.get(e);
                HealthComponent eh = K2ComponentMappers.health.get(e);
                TransformComponent enemyPos = K2ComponentMappers.transform.get(e);

                boolean isInAura = auraBounds.circle.overlaps(enemyBounds.circle);

                if(!ec.isPollenated && isInAura){
                    ec.setPollenated(true);
                    e.add(TweenComponent.create(getEngine())
                        .addTween(Tween.to(e, K2EntityTweenAccessor.COLOR, 0.3f)
                            .target(Colors.POLLENATED_HIGHLIGHT.r,
                                    Colors.POLLENATED_HIGHLIGHT.g,
                                    Colors.POLLENATED_HIGHLIGHT.b)));
                    //Quarter the Speed
                    scaleVelocity(e, enemyVel, Rates.AURA_SLOWDOWN_RATE);

                }else if(ec.isPollenated && !isInAura){
                    ec.setPollenated(false);
                    //Restore
                    scaleVelocity(e, enemyVel, 1f/Rates.AURA_SLOWDOWN_RATE);
                    Color c = Colors.PLAIN_WHITE;

                    if(ec.enemyType != EnemyType.COMET){
                        c = ec.enemyColor == EnemyColor.BROWN ? Colors.BROWN_ASTEROID :
                            ec.enemyColor == EnemyColor.BLUE ? Colors.BLUE_ASTEROID : Colors.PURPLE_ASTEROID;
                    }

                    e.add(TweenComponent.create(getEngine())
                            .addTween(Tween.to(e, K2EntityTweenAccessor.COLOR, 0.3f)
                                    .target(c.r, c.g, c.b)));
                }


                if(ec.isPollenated && eh.health > 0f){

                    int healthBefore = (int)Math.ceil(eh.health);
                    eh.health = Math.max(0f, eh.health - (auraDamage.dps*deltaTime));
                    int healthAfter = (int)Math.ceil(eh.health);

                    if(healthAfter < healthBefore){
                        int points = (healthBefore-healthAfter);
                        for(int i=0;i<points;i++){
                            EnemyDamageUtil.attachPlant((PooledEngine)getEngine(), auraBounds.circle, e, ec.enemyType == EnemyType.COMET);
                        }

                        if (scoreCard != null) {
                            scoreCard.setScore(scoreCard.score + points);
                        }
                    }

                    if(eh.health == 0f){
                        scaleVelocity(e, enemyVel, 1f / 2f);
                        //e.add(TweenComponent.)
                        EnemyDamageUtil.processEnemyDefeated(e, (PooledEngine)getEngine());
                    }
                    //TODO: Plant trees randomly around enemy edge
                }
            }


        }
        enemies.clear();
        aura = null;
    }

    private void scaleVelocity(Entity e, VelocityComponent enemyVel, float scale){
        if(enemyVel != null) {
            enemyVel.speed.scl(scale);
        }else{
            //PathFollow
            PathFollowComponent pfc = K2ComponentMappers.pathFollow.get(e);
            pfc.setSpeed(pfc.speed * scale);
        }

//        if(enemyVel != null) {
//            float targetX = enemyVel.speed.x * scale;
//            float targetY = enemyVel.speed.y * scale;
//            e.add(TweenComponent.create(getEngine())
//                    .addTween(Tween.to(e, K2EntityTweenAccessor.VELOCITY, 0.3f)
//                            .target(targetX, targetY)
//                            .ease(TweenEquations.easeOutExpo)));
//
//        }else{
//            //PathFollow
//            PathFollowComponent pfc = K2ComponentMappers.pathFollow.get(e);
//            e.add(TweenComponent.create(getEngine())
//                    .addTween(Tween.to(e, K2EntityTweenAccessor.PATH_FOLLOW_SPEED, 0.5f)
//                            .target(pfc.speed * scale)
//                            .ease(TweenEquations.easeOutExpo)));
//        }
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

package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.VectorUtils;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.*;
import com.roaringcatgames.libgdxjam.values.Z;

import java.util.Random;

/**
 * System to apply enemy damage of bullets
 */
public class  EnemyDamageSystem extends IteratingSystem {

    private Array<Entity> bullets = new Array<>();
    private Array<Entity> enemies = new Array<>();

    private ScoreComponent scoreCard;

    public EnemyDamageSystem(){
        super(Family.one(BulletComponent.class, EnemyComponent.class).get());
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        ImmutableArray<Entity> scores = getEngine().getEntitiesFor(Family.all(ScoreComponent.class).get());
        if(scores != null && scores.size() > 0){
            scoreCard = scores.first().getComponent(ScoreComponent.class);
        }

        for(Entity bullet:bullets){
            CircleBoundsComponent bb = K2ComponentMappers.circleBounds.get(bullet);
            for(Entity enemy:enemies){
                if(enemy.isScheduledForRemoval()){
                    continue;
                }
                if(K2ComponentMappers.bounds.has(enemy)) {
                    BoundsComponent eb = K2ComponentMappers.bounds.get(enemy);
                    if (Intersector.overlaps(bb.circle, eb.bounds)) {
                        getEngine().removeEntity(bullet);
                        getEngine().removeEntity(enemy);
                    }
                }else if(K2ComponentMappers.circleBounds.has(enemy)){
                    CircleBoundsComponent cb = K2ComponentMappers.circleBounds.get(enemy);
                    if (cb.circle.overlaps(bb.circle)) {
                        processCollision(bullet, enemy);
                    }
                }
            }
        }

        enemies.clear();
        bullets.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if(!entity.isScheduledForRemoval()) {
            if (Mappers.bullet.has(entity)) {
                bullets.add(entity);
            } else {
               enemies.add(entity);
            }
        }
    }

    private void processCollision(Entity bullet, Entity enemy){

        EnemyComponent ec = Mappers.enemy.get(enemy);
        CircleBoundsComponent bulletBounds = K2ComponentMappers.circleBounds.get(bullet);
        HealthComponent hc;

        hc = K2ComponentMappers.health.get(enemy);
        if(hc.health > 0f) {
            EnemyDamageUtil.attachPlant((PooledEngine)getEngine(), bulletBounds.circle, enemy, ec.enemyType == EnemyType.COMET);
            if (scoreCard != null) {
                scoreCard.setScore(scoreCard.score + 1);
            }

            float startHealth = hc.health;
            applyHealthChange(bullet, hc);


            if (startHealth > 0f && hc.health <= 0f) {
                EnemyDamageUtil.processEnemyDefeated(enemy, (PooledEngine)getEngine());
            }

            getEngine().removeEntity(bullet);
        }
    }

    private void applyHealthChange(Entity bullet, HealthComponent hc) {
        DamageComponent dmg = K2ComponentMappers.damage.get(bullet);
        hc.health = Math.max(0f, (hc.health - dmg.dps));
    }

}

package com.roaringcatgames.galaxseed.systems;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.galaxseed.*;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.VectorUtils;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.galaxseed.components.EnemyComponent;
import com.roaringcatgames.galaxseed.components.Mappers;
import com.roaringcatgames.galaxseed.components.PowerUpComponent;
import com.roaringcatgames.galaxseed.values.Colors;
import com.roaringcatgames.galaxseed.values.Volume;
import com.roaringcatgames.galaxseed.values.Z;

import java.util.Random;

/**
 * Utility to apply damage to Enemies
 */
public class EnemyDamageUtil {
    private static Random r = new Random();
    private static final float healthPackSpeed = -1f;
    private static Vector2 bulletPos = new Vector2();
    private static Vector2 enemyPos = new Vector2();

//    private enum HealthPackType {
//        FERTILIZER,
//        WATER_CAN
//    }

    public static void processEnemyDefeated(Entity enemy, PooledEngine engine){
        TransformComponent enemyTfm = K2ComponentMappers.transform.get(enemy);
        EnemyComponent ec = Mappers.enemy.get(enemy);
        switch (ec.enemyType) {
            case ASTEROID_FRAG:
            case COMET:
                if(K2ComponentMappers.state.has(enemy)){
                    StateComponent sc = K2ComponentMappers.state.get(enemy);
                    sc.setLooping(false);
                    sc.set("FULL");
                }
                if(K2ComponentMappers.pathFollow.has(enemy)){
                    PathFollowComponent pfc = K2ComponentMappers.pathFollow.get(enemy);
                    pfc.setSpeed(pfc.speed/2f);
                    pfc.setFacingPath(false);
                }else if (K2ComponentMappers.velocity.has(enemy)) {
                    VelocityComponent vc = K2ComponentMappers.velocity.get(enemy);
                    vc.speed.scl(0.5f);
                }

                float rotR = r.nextFloat();
                float rotSpeed = (20f*rotR) + 20f;
                if(rotR >0.5f){
                    rotSpeed *= -1f;
                }
                enemy.add(RotationComponent.create(engine)
                        .setRotationSpeed(rotSpeed));
                break;
            case ASTEROID_A:
                attachTreeCover(engine, enemy, Animations.getAsteroidA());
                break;
            case ASTEROID_B:
                attachTreeCover(engine, enemy, Animations.getAsteroidB());
                break;
            case ASTEROID_C:
                attachTreeCover(engine, enemy, Animations.getAsteroidC());
                break;
            default:
                Gdx.app.log("EnemyType", "EnemyType:" + ec.enemyType);
                break;
        }

        //ec.setDamaging(false);
        if (Mappers.spawner.has(enemy)) {
            Mappers.spawner.get(enemy).setPaused(true);
            if (K2ComponentMappers.velocity.has(enemy)) {
                VelocityComponent vc = K2ComponentMappers.velocity.get(enemy);
                vc.speed.scl(1.6f);
            }else{
                Gdx.app.log("EnemyDamageSystem", "Enemy doesn't have Velocity!");
            }

            Sfx.playPlanetBorn(r.nextInt(3));
        }

        enemy.add(TweenComponent.create(engine)
                .addTween(Tween.to(enemy, K2EntityTweenAccessor.COLOR, 2f)
                        .target(Colors.PLANTED_GREEN.r, Colors.PLANTED_GREEN.g, Colors.PLANTED_GREEN.b)
                        .ease(TweenEquations.easeInOutSine)));

        if(ec.shouldGeneratePowerup){
            generatePowerup(engine, enemyTfm.position.x, enemyTfm.position.y);
        }

    }

    private static void generatePowerup(PooledEngine engine, float x, float y){
        if(App.canPowerUp()) {
            generateUpgradePowerUp(engine, x, y);
        }
    }

    private static void attachTreeCover(PooledEngine engine, Entity enemy, Animation ani) {

        TransformComponent tc = K2ComponentMappers.transform.get(enemy);

        Entity treeCover = engine.createEntity();
        treeCover.add(TransformComponent.create(engine)
                .setPosition(tc.position.x, tc.position.y, Z.treeCover)
                .setRotation(tc.rotation));
        treeCover.add(TextureComponent.create(engine));
        treeCover.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(false));

        treeCover.add(FollowerComponent.create(engine)
                .setOffset(0f, 0f)
                .setTarget(enemy)
                .setBaseRotation(0f));

        treeCover.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", ani));
        engine.addEntity(treeCover);
    }

//    private static void generateHealthPack(PooledEngine engine, float x, float y, HealthPackType hType) {
//        Entity healthPack = engine.createEntity();
//        healthPack.add(TransformComponent.create(engine)
//                .setPosition(x, y, Z.healthPack)
//                .setScale(1f, 1f));
//        healthPack.add(RotationComponent.create(engine)
//                .setRotationSpeed(-60f));
//        healthPack.add(TweenComponent.create(engine)
//                .addTween(Tween.to(healthPack, K2EntityTweenAccessor.SCALE, 0.5f)
//                        .target(1.25f, 1.25f)
//                        .ease(TweenEquations.easeInOutBounce)
//                        .repeatYoyo(Tween.INFINITY, 0)));
//        healthPack.add(VelocityComponent.create(engine)
//                .setSpeed(0f, 1f)); //TODO: use configured healthpack speed
//        healthPack.add(TextureComponent.create(engine));
//        Animation ani;
//        TextureRegion glowRegion;
//        float health;
//        if(hType == HealthPackType.FERTILIZER){
//            ani = Animations.getHealthFertilizer();
//            glowRegion = Assets.getFertilizerGlow();
//            health = Health.HealthPackFertilizer;
//        }else{
//            ani = Animations.getHealthWaterCan();
//            glowRegion = Assets.getWaterCanGlow();
//            health = Health.HealthPackWaterCan;
//        }
//        healthPack.add(AnimationComponent.create(engine)
//                .addAnimation("DEFAULT", ani));
//        healthPack.add(StateComponent.create(engine)
//                .set("DEFAULT")
//                .setLooping(true));
//        healthPack.add(HealthPackComponent.create(engine)
//                .setHealth(health)
//                .setInstant(true));
//        healthPack.add(BoundsComponent.create(engine)
//                .setBounds(0f, 0f, 1f, 1f));
//        engine.addEntity(healthPack);
//
//        Entity glow = engine.createEntity();
//        glow.add(TransformComponent.create(engine)
//                .setPosition(x, y, Z.healthGlow));
//        glow.add(TextureComponent.create(engine)
//                .setRegion(glowRegion));
//        glow.add(FollowerComponent.create(engine)
//                .setTarget(healthPack));
//        glow.add(TweenComponent.create(engine)
//                .addTween(Tween.to(glow, K2EntityTweenAccessor.COLOR, 0.5f)
//                        .target(Colors.GLOW_YELLOW.r, Colors.GLOW_YELLOW.g, Colors.GLOW_YELLOW.b)
//                        .ease(TweenEquations.easeInOutBounce)
//                        .repeatYoyo(Tween.INFINITY, 0))
//                .addTween(Tween.to(glow, K2EntityTweenAccessor.SCALE, 0.5f)
//                        .target(1.25f, 1.25f)
//                        .ease(TweenEquations.easeInOutBounce)
//                        .repeatYoyo(Tween.INFINITY, 0)));
//        engine.addEntity(glow);
//    }

    private static void generateUpgradePowerUp(PooledEngine engine, float x, float y){

        Entity powerUp = engine.createEntity();
        powerUp.add(TransformComponent.create(engine)
                .setPosition(x, y, Z.powerUp)
                .setScale(1f, 1f));
        powerUp.add(TweenComponent.create(engine)
                .addTween(Tween.to(powerUp, K2EntityTweenAccessor.SCALE, 0.5f)
                        .target(1.25f, 1.25f)
                        .ease(TweenEquations.easeInOutBounce)
                        .repeatYoyo(Tween.INFINITY, 0)));
        powerUp.add(VelocityComponent.create(engine)
                .setSpeed(0f, healthPackSpeed));
        powerUp.add(TextureComponent.create(engine));

        powerUp.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", Animations.getUpgrade()));
        powerUp.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(true));
        powerUp.add(PowerUpComponent.create(engine)
                .setPowerUpType(PowerUpComponent.PowerUpType.UPGRADE));
        powerUp.add(BoundsComponent.create(engine)
                .setBounds(0f, 0f, 1f, 1f));
        engine.addEntity(powerUp);
    }


    public static void attachPlant(PooledEngine engine, Circle damageSource, Entity enemy, boolean isComet) {

        //CircleBoundsComponent bb = K2ComponentMappers.circleBounds.get(damageSource);
        CircleBoundsComponent eb = K2ComponentMappers.circleBounds.get(enemy);
        TransformComponent et = K2ComponentMappers.transform.get(enemy);
        bulletPos.set(damageSource.x, damageSource.y);//bb.circle.x, bb.circle.y);
        enemyPos.set(eb.circle.x, eb.circle.y);

        Vector2 outVec;
        if(isComet) {
            outVec = enemyPos.cpy();
        }else {
            outVec = bulletPos.sub(enemyPos).nor();
            outVec = outVec.scl(eb.circle.radius);
        }

        Vector2 offsetVec;
        float baseRotation;
        if(isComet){
            offsetVec = eb.offset.cpy().add(0.1f, -0.5f);
        } else{
            offsetVec = VectorUtils.rotateVector(outVec.cpy(), -et.rotation).add(eb.offset);
        }
        baseRotation = offsetVec.angle() - 90f;


        Entity plant = (engine).createEntity();
        plant.add(TransformComponent.create(engine)
                .setPosition(outVec.x, outVec.y, Z.plant)
                .setRotation(et.rotation + baseRotation)
                .setScale(1f, 1f));

        plant.add(TextureComponent.create(engine));
        plant.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(false));
        float rnd = r.nextFloat();
        Animation ani = rnd < 0.3f ? Animations.getGreenTree() :
                rnd < 0.6f ? Animations.getPinkTree() :
                        Animations.getPineTree();
        plant.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", ani));
        plant.add(FollowerComponent.create(engine)
                .setOffset(offsetVec.x, offsetVec.y)
                .setTarget(enemy)
                .setBaseRotation(baseRotation));

        plant.add(ParticleEmitterComponent.create(engine)
                .setParticleImages(Assets.getLeafFrames())
                .setParticleLifespans(0.1f, 0.2f)
                .setSpawnRate(2f)
                .setAngleRange(0f, 360f)
                .setParticleMinMaxScale(0.5f, 0.5f)
                .setSpeed(2f, 3f)
                .setParticleMinMaxScale(0.5f, 0.5f)
                .setShouldFade(true)
                .setZIndex(Z.leaves)
                .setDuration(0.3f));

        engine.addEntity(plant);

        if(PrefsUtil.areSfxEnabled()){
            Assets.getBloomSfx(r.nextInt(6) + 1).play(Volume.BLOOM_TREE_SFX);
        }
    }
}

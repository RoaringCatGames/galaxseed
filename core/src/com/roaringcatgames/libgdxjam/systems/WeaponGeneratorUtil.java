package com.roaringcatgames.libgdxjam.systems;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.*;
import com.roaringcatgames.libgdxjam.values.Damage;
import com.roaringcatgames.libgdxjam.values.Rates;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * Weapon Generator Logic applied in one location
 */
public class WeaponGeneratorUtil {

    private static Array<Vector2> muzzlePositions = new Array<>();
    private static Array<Vector2> auraSizes = new Array<>();
    private static Array<Entity> removableEntities = new Array<>();

    static {
        muzzlePositions.add(new Vector2(-0.5f, 1.6f));
        muzzlePositions.add(new Vector2(0.5f, 1.6f));

        muzzlePositions.add(new Vector2(-0.906f, 0.881f));
        muzzlePositions.add(new Vector2(0.906f, 0.881f));

        muzzlePositions.add(new Vector2(-1.312f, 0.3f));
        muzzlePositions.add(new Vector2(1.312f, 0.3f));

        muzzlePositions.add(new Vector2(-1.512f, 0f));
        muzzlePositions.add(new Vector2(1.512f, 0f));

        auraSizes.add(new Vector2(0.2f, 3.5f));
        auraSizes.add(new Vector2(0.3f, 4.5f));
        auraSizes.add(new Vector2(0.4f, 5f));
        auraSizes.add(new Vector2(0.5f, 5.5f));
    }

    public static void clearWeapon(Engine engine){
        Family familyToRemove = Family.one(GunComponent.class, WeaponDecorationComponent.class, PollenAuraComponent.class).get();
        ImmutableArray<Entity> currentGunEntities = engine.getEntitiesFor(familyToRemove);

        removableEntities.clear();
        for(int i=0;i<currentGunEntities.size();i++){
            removableEntities.add(currentGunEntities.get(i));
        }

        for(Entity e:removableEntities){
            engine.removeEntity(e);
        }
    }

    public static void generateSeedGuns(Entity player, PooledEngine engine){
        PlayerComponent pc = Mappers.player.get(player);

        addGun(player, engine, muzzlePositions.get(0).x, muzzlePositions.get(0).y, Rates.SEED_GUN_TIME_BETWEEN);
        addGun(player, engine, muzzlePositions.get(1).x, muzzlePositions.get(1).y, Rates.SEED_GUN_TIME_BETWEEN);

        if(pc.weaponLevel == WeaponLevel.LEVEL_2 ||
           pc.weaponLevel == WeaponLevel.LEVEL_3 ||
           pc.weaponLevel == WeaponLevel.LEVEL_4){
            //Add 2 more guns
            addGun(player, engine, muzzlePositions.get(2).x, muzzlePositions.get(2).y, Rates.SEED_GUN_TIME_BETWEEN);
            addGun(player, engine, muzzlePositions.get(3).x, muzzlePositions.get(3).y, Rates.SEED_GUN_TIME_BETWEEN);

        }

        if(pc.weaponLevel == WeaponLevel.LEVEL_3 ||
           pc.weaponLevel == WeaponLevel.LEVEL_4){
            //Add 2 more guns
            addGun(player, engine, muzzlePositions.get(4).x, muzzlePositions.get(4).y, Rates.SEED_GUN_TIME_BETWEEN);
            addGun(player, engine, muzzlePositions.get(5).x, muzzlePositions.get(5).y, Rates.SEED_GUN_TIME_BETWEEN);
        }

        if(pc.weaponLevel == WeaponLevel.LEVEL_4){
            //Add Gatling guns
            Gdx.app.log("PowerUpSystem", "Upgrading to level 4!");
            addGun(player, engine,
                    muzzlePositions.get(6).x,
                    muzzlePositions.get(6).y,
                    Rates.SEED_GUN_GATLING_TIME_BETWEEN, true);
            addGun(player, engine,
                    muzzlePositions.get(7).x,
                    muzzlePositions.get(7).y,
                    Rates.SEED_GUN_GATLING_TIME_BETWEEN, true);
        }
    }

    public static void generateHelicopterGuns(Entity player, PooledEngine engine){
        PlayerComponent pc = Mappers.player.get(player);
        TransformComponent tc = K2ComponentMappers.transform.get(player);

        if(pc.weaponLevel != WeaponLevel.LEVEL_4){
            Entity heliGun = engine.createEntity();
            heliGun.add(GunComponent.create(engine)
                .setBulletSpeed(Rates.HELI_GUN_BULLET_SPEED)
                .setTimeBetweenShots(Rates.HELI_GUN_TIME_BETWEEN));
            heliGun.add(TransformComponent.create(engine)
                .setPosition(tc.position.x, tc.position.y, Z.heliGun));
            heliGun.add(FollowerComponent.create(engine)
                .setTarget(player)
                .setMode(FollowMode.STICKY)
                .setOffset(2f, 0f));
            engine.addEntity(heliGun);

        }else{
            Entity leftGun = engine.createEntity();
            leftGun.add(GunComponent.create(engine)
                    .setBulletSpeed(Rates.HELI_GUN_BULLET_SPEED)
                    .setTimeBetweenShots(Rates.HELI_GUN_TIME_BETWEEN)
                    .setLastFireTime(0f));
            leftGun.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(false));
            leftGun.add(TextureComponent.create(engine));
            leftGun.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", Animations.getCannonIdle())
                .addAnimation("FIRING", Animations.getCannonFiring()));
            leftGun.add(TransformComponent.create(engine)
                    .setPosition(tc.position.x, tc.position.y, Z.heliGun)
                    .setScale(tc.scale.x, tc.scale.y));
            leftGun.add(FollowerComponent.create(engine)
                    .setTarget(player)
                    .setMode(FollowMode.STICKY)
                    .setOffset(-0.75f, -0.25f));
            engine.addEntity(leftGun);

            Entity rightGun = engine.createEntity();
            rightGun.add(StateComponent.create(engine)
                    .set("DEFAULT")
                    .setLooping(false));
            rightGun.add(TextureComponent.create(engine));
            rightGun.add(AnimationComponent.create(engine)
                    .addAnimation("DEFAULT", Animations.getCannonIdle())
                    .addAnimation("FIRING", Animations.getCannonFiring()));
            rightGun.add(GunComponent.create(engine)
                    .setBulletSpeed(Rates.HELI_GUN_BULLET_SPEED)
                    .setTimeBetweenShots(Rates.HELI_GUN_TIME_BETWEEN)
                    .setLastFireTime(Rates.HELI_GUN_TIME_BETWEEN/2f));
            rightGun.add(TransformComponent.create(engine)
                    .setPosition(tc.position.x, tc.position.y, Z.heliGun)
                    .setScale(tc.scale.x, tc.scale.y));
            rightGun.add(FollowerComponent.create(engine)
                    .setTarget(player)
                    .setMode(FollowMode.STICKY)
                    .setOffset(0.75f, -0.25f));
            engine.addEntity(rightGun);
        }


    }


    private static void addGun(Entity player, PooledEngine engine,
                        float x, float y, float timeBetweenShots, boolean...hasGun) {

        TransformComponent playerTransform = K2ComponentMappers.transform.get(player);
        boolean isGatling = hasGun != null && hasGun.length == 1 && hasGun[0];

        float lastSynchronizedFireTime = 0f;

        Animation muzzleAni = isGatling ? Animations.getGatlingMuzzle() : Animations.getMuzzle();
        float zMuzzle = isGatling ? Z.gatlingMuzzle : Z.muzzleFlash;
        float yAddOff = isGatling ? 0.7f : 0f;
        float muzzleScaleAdd = isGatling ? 1f : 0.5f;
        Entity muzzle = engine.createEntity();
        muzzle.add(GunComponent.create(engine)
                .setTimeBetweenShots(timeBetweenShots)
                .setLastFireTime(lastSynchronizedFireTime)
                .setBulletSpeed(isGatling ? Rates.SEED_GUN_GATLING_BULLET_SPEED : Rates.SEED_GUN_BULLET_SPEED));
        muzzle.add(FollowerComponent.create(engine)
                .setOffset(x * playerTransform.scale.x, yAddOff + (y * playerTransform.scale.y))
                .setTarget(player)
                .setMode(FollowMode.STICKY));
        muzzle.add(TextureComponent.create(engine));
        muzzle.add(AnimationComponent.create(engine)
                .addAnimation("FIRING", muzzleAni));
        muzzle.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(false));
        muzzle.add(TransformComponent.create(engine)
                .setPosition(playerTransform.position.x, playerTransform.position.y, zMuzzle)
                .setScale(playerTransform.scale.x * muzzleScaleAdd, playerTransform.scale.y * muzzleScaleAdd)
                .setOpacity(0.8f));
        engine.addEntity(muzzle);

        if(isGatling){
            Entity gun = engine.createEntity();
            gun.add(WeaponDecorationComponent.create(engine));
            gun.add(TransformComponent.create(engine)
                    .setPosition(playerTransform.position.x, playerTransform.position.y, Z.gatlingGuns)
                    .setScale(1f, 1f));
            gun.add(TweenComponent.create(engine)
                    .addTween(Tween.to(gun, K2EntityTweenAccessor.SCALE, 0.5f)
                            .target(playerTransform.scale.x, playerTransform.scale.y)
                            .ease(TweenEquations.easeInOutElastic)));
            gun.add(FollowerComponent.create(engine)
                    .setTarget(player)
                    .setOffset(x * playerTransform.scale.x, y * playerTransform.scale.y)
                    .setMode(FollowMode.STICKY));
            gun.add(AnimationComponent.create(engine)
                    .addAnimation("DEFAULT", Animations.getGatlingIdle())
                    .addAnimation("FIRING", Animations.getGatlingFiring()));
            gun.add(TextureComponent.create(engine));
            gun.add(StateComponent.create(engine)
                    .set("DEFAULT")
                    .setLooping(true));
            engine.addEntity(gun);

            Entity smoke = engine.createEntity();
            smoke.add(WeaponDecorationComponent.create(engine));
            smoke.add(TransformComponent.create(engine)
                    .setPosition(0f, 0f, Z.gatlingSmoke)
                    .setScale(1f, 1f));

            smoke.add(FollowerComponent.create(engine)
                    .setTarget(player)
                    .setOffset(x * playerTransform.scale.x, (y * playerTransform.scale.y) - 0.6f)
                    .setMode(FollowMode.STICKY));
            smoke.add(ParticleEmitterComponent.create(engine)
                    .setShouldFade(true)
                    .setAngleRange(150f, 210f)
                    .setShouldLoop(true)
                    .setSpawnRate(3f)
                    .setParticleImages(Assets.getGatlingSmokeParticles())
                    .setParticleLifespans(0.2f, 0.4f)
                    .setParticleMinMaxScale(0.3f, 0.7f)
                    .setSpawnType(ParticleSpawnType.RANDOM_IN_BOUNDS)
                    .setSpeed(1f, 2f)
                    .setZIndex(Z.gatlingSmoke)
                    .setSpawnRange(0.2f, 0.2f)
                    .setPaused(true));
            smoke.add(StateComponent.create(engine)
                    .set("DEFAULT")
                    .setLooping(true));
            engine.addEntity(smoke);
        }
    }


    public static void generateAura(Entity player, PooledEngine engine){
        PlayerComponent pc = Mappers.player.get(player);
        TransformComponent playerPos = K2ComponentMappers.transform.get(player);
        float auraScale = playerPos.scale.x + auraSizes.get(0).x;
        float auraRadius = auraSizes.get(0).y;
        float dps = Damage.auraLevelOne;
        if(pc.weaponLevel == WeaponLevel.LEVEL_2){
            auraScale += auraSizes.get(1).x;
            auraRadius = auraSizes.get(1).y;
            dps = Damage.auraLevelTwo;
        }else if(pc.weaponLevel == WeaponLevel.LEVEL_3){
            auraScale += auraSizes.get(2).x;
            auraRadius = auraSizes.get(2).y;
            dps = Damage.auraLevelThree;

        }else if(pc.weaponLevel == WeaponLevel.LEVEL_4){
            auraScale += auraSizes.get(3).x;
            auraRadius = auraSizes.get(3).y;
            dps = Damage.auraLevelFour;
        }

        //Apply Aura
        Entity aura = engine.createEntity();
        aura.add(PollenAuraComponent.create(engine));
        aura.add(TextureComponent.create(engine));
        aura.add(TransformComponent.create(engine)
                .setPosition(0f, 0f, Z.pollenAura)
                .setScale(auraScale, auraScale));

        Animation auraAni = pc.weaponLevel == WeaponLevel.LEVEL_4 ? Animations.getAuraFinal() : Animations.getAura();
        aura.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", auraAni));
        aura.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(true));
        aura.add(CircleBoundsComponent.create(engine)
                .setCircle(0f, 0f, auraRadius));
        aura.add(FollowerComponent.create(engine)
                .setTarget(player)
                .setMode(FollowMode.STICKY));
        aura.add(DamageComponent.create(engine)
            .setDPS(dps));

        if(pc.weaponLevel == WeaponLevel.LEVEL_4){
            aura.add(ParticleEmitterComponent.create(engine)
                .setSpeed(0.5f, 1f)
                .setParticleImages(Assets.getDandyParticles())
                .setSpawnType(ParticleSpawnType.RANDOM_IN_BOUNDS)
                .setParticleLifespans(0.5f, 1f)
                .setSpawnRange(auraSizes.get(3).y/2f, auraSizes.get(3).y/2f)
                .setShouldLoop(true)
                .setZIndex(Z.dandyParticles)
                .setAngleRange(0f, 360f)
                .setParticleMinMaxScale(0.5f, 1f)
                .setShouldFade(true)
                .setSpawnRate(2f));
        }

        engine.addEntity(aura);
    }
}

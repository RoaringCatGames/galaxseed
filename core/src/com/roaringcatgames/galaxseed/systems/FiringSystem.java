package com.roaringcatgames.galaxseed.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.galaxseed.Animations;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.PrefsUtil;
import com.roaringcatgames.galaxseed.components.*;
import com.roaringcatgames.galaxseed.values.Damage;
import com.roaringcatgames.galaxseed.values.Volume;
import com.roaringcatgames.galaxseed.values.Z;

/**
 * Controls firing of Guns
 */
public class FiringSystem extends IteratingSystem {

    private Sound firingSFX;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<GunComponent> gm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<AnimationComponent> am;

    Array<Entity> muzzles = new Array<>();
    Entity player;

    public FiringSystem(){
        super(Family.one(PlayerComponent.class, GunComponent.class).get());
        tm = ComponentMapper.getFor(TransformComponent.class);
        gm = ComponentMapper.getFor(GunComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);
        firingSFX = Assets.getSeedFiring();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(player != null) {
            PlayerComponent pc = Mappers.player.get(player);
            StateComponent sc = K2ComponentMappers.state.get(player);

            boolean isGunSeeds = pc.weaponType == WeaponType.GUN_SEEDS;
            //Fire all the time.
            if(sc.get() != "DEFAULT") {

                boolean isFiringSeed = false;
                boolean isFiringHelicopter = false;
                for(Entity m:muzzles){
                    StateComponent mState = sm.get(m);
                    GunComponent gc = gm.get(m);
                    AnimationComponent ac = am.get(m);
                    gc.timeElapsed += deltaTime;
                    if(gc.timeElapsed - gc.lastFireTime >= gc.timeBetweenShots) {

                        FollowerComponent follower = K2ComponentMappers.follower.get(m);
                        if(isGunSeeds) {
                            mState.set("FIRING");
                            mState.setLooping(false);
                            generateBullet(follower.offset.x, follower.offset.y, 0f, gc.bulletSpeed);
                            isFiringSeed = true;
                        }else{
                            if(mState != null){
                                mState.set("FIRING");
                            }
                            generateHelicopterSeed(follower.offset.x, follower.offset.y, 0f, gc.bulletSpeed, follower.offset.x < 0f);
                            isFiringHelicopter = true;
                        }
                        gc.lastFireTime = gc.timeElapsed;


                    }else if(mState != null &&
                             !"DEFAULT".equals(mState.get()) && ac.animations.get(mState.get()).isAnimationFinished(mState.time)){

                        mState.set("DEFAULT");
                        K2ComponentMappers.texture.get(m).setRegion(null);
                    }
                }

                if(PrefsUtil.areSfxEnabled()) {
                    if (isFiringSeed) {
                        Assets.getSeedFiring().play(Volume.FIRING_SFX);
                    }else if(isFiringHelicopter){
                        Assets.getSwishSfx().play(Volume.SWISH_SFX);
                    }
                }
            }else if(isGunSeeds){
                for(Entity m:muzzles){
                    StateComponent mState = sm.get(m);
                    if(mState.get() != "DEFAULT") {
                        mState.set("DEFAULT");
                        K2ComponentMappers.texture.get(m).setRegion(null);
                    }
                }
            }
        }

        muzzles.clear();
        player = null;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if(gm.has(entity)){
            muzzles.add(entity);
        }else {
            if (player != null) {
                throw new IllegalStateException("Cannot handle Two Players right now!");
            }
            player = entity;
        }
    }


    private void generateBullet(float xOffset, float yOffset, float xVel, float yVel){
        PooledEngine engine = (PooledEngine)getEngine();
        TransformComponent playerPos = tm.get(player);
        //Generate Bullets here
        Entity bullet = engine.createEntity();
        bullet.add(WhenOffScreenComponent.create(engine));
        bullet.add(KinematicComponent.create(engine));
        bullet.add(TransformComponent.create(engine)
                .setPosition(playerPos.position.x + xOffset, playerPos.position.y + yOffset, Z.seed)
                .setScale(0.5f, 0.5f));
        bullet.add(CircleBoundsComponent.create(engine)
            .setCircle(playerPos.position.x + xOffset, playerPos.position.y + yOffset, 0.125f)
            .setOffset(0f, 0.25f));
        bullet.add(TextureComponent.create(engine));
        bullet.add(DamageComponent.create(engine)
            .setDPS(Damage.seed));
        bullet.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", Animations.getBullet())
                .addAnimation("FLYING", Animations.getBulletFlying()));
        bullet.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(false));
        bullet.add(BulletComponent.create((PooledEngine)getEngine()));
        bullet.add(VelocityComponent.create(engine)
                .setSpeed(xVel, yVel));
        getEngine().addEntity(bullet);
    }

    private Array<Float> xBounds = new Array<>();
    private void generateHelicopterSeed(float xOffset, float yOffset, float xVel, float yVel, boolean isLeft){
        PooledEngine engine = (PooledEngine)getEngine();
        TransformComponent playerPos = K2ComponentMappers.transform.get(player);
        PlayerComponent playerComp = Mappers.player.get(player);
        xBounds.clear();

        float scale = 0.3f;
        float originOffset = -2f;
        float rotation = -120f;
        float rotationSpeed = isLeft ? -360f : 360f;
        TextureRegion tr = playerComp.weaponLevel == WeaponLevel.LEVEL_4 ? Assets.getFinalHelicopterSeed() :
                                                                           Assets.getHelicopterSeed();
        xBounds.add(0f);
        xBounds.add(0.5f);
        xBounds.add(1f);

        if(playerComp.weaponLevel == WeaponLevel.LEVEL_2){
            scale = 0.4f;
            originOffset = -2f;
            xBounds.add(1.5f);
            xBounds.add(2f);
        }else if(playerComp.weaponLevel == WeaponLevel.LEVEL_3 ||
                 playerComp.weaponLevel == WeaponLevel.LEVEL_4){
            scale = 0.5f;
            originOffset = -2f;
            xBounds.add(1.5f);
            xBounds.add(2f);
        }

        float scaleY = isLeft ? -1f*scale : scale;

        if(playerComp.weaponLevel == WeaponLevel.LEVEL_4){
            xOffset -= originOffset;
        }


        //Generate Bullets here
        Entity heliSeed = engine.createEntity();
        heliSeed.add(WhenOffScreenComponent.create(engine));
        heliSeed.add(KinematicComponent.create(engine));
        heliSeed.add(TransformComponent.create(engine)
                .setPosition(playerPos.position.x + xOffset, playerPos.position.y, Z.helicopterSeed)
                .setScale(scale, scaleY)
                .setRotation(rotation)
                .setOriginOffset(originOffset, 0f));
        MultiBoundsComponent mbc = MultiBoundsComponent.create(engine);
        for(Float x:xBounds){
            mbc.addBound(new Bound(new Circle(0f, 0f, 0.25f), x, 0f));
        }
        heliSeed.add(mbc);
        heliSeed.add(TextureComponent.create(engine)
                .setRegion(tr));
        heliSeed.add(DamageComponent.create(engine)
                .setDPS(Damage.helicopterSeed));
        heliSeed.add(HelicopterSeedComponent.create(getEngine()));
        heliSeed.add(VelocityComponent.create(engine)
                .setSpeed(xVel, yVel));
        heliSeed.add(RotationComponent.create(engine)
            .setRotationSpeed(rotationSpeed));
        getEngine().addEntity(heliSeed);
    }
}

package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.*;
import com.roaringcatgames.libgdxjam.values.Damage;
import com.roaringcatgames.libgdxjam.values.Volume;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * Created by barry on 1/3/16 @ 1:14 AM.
 */
public class FiringSystem extends IteratingSystem {

    private float timeElapsed = 0f;
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
            StateComponent sc = player.getComponent(StateComponent.class);

            //Fire all the time.
            if(sc.get() != "DEFAULT") {
                timeElapsed += deltaTime;

                boolean isFiring = false;
                for(Entity m:muzzles){
                    StateComponent mState = sm.get(m);
                    GunComponent gc = gm.get(m);
                    AnimationComponent ac = am.get(m);

                    if(timeElapsed - gc.lastFireTime >= gc.timeBetweenShots) {
                        mState.set("FIRING");
                        mState.setLooping(false);
                        FollowerComponent follower = m.getComponent(FollowerComponent.class);
                        generateBullet(follower.offset.x, follower.offset.y, 0f, gc.bulletSpeed);
                        gc.lastFireTime = timeElapsed;

                        isFiring = true;
                    }else if(!"DEFAULT".equals(mState.get()) && ac.animations.get(mState.get()).isAnimationFinished(mState.time)){
                        mState.set("DEFAULT");
                        K2ComponentMappers.texture.get(m).setRegion(null);
                    }
                }

                if(isFiring){
                    this.firingSFX.play(Volume.FIRING_SFX);
                }
            }else{
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
}

package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.BulletComponent;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.WhenOffScreenComponent;

/**
 * Created by barry on 1/3/16 @ 1:14 AM.
 */
public class FiringSystem extends IteratingSystem {

    private float firingRate = 2f;
    private float timeBetweenFiring = 1f/firingRate;
    private float lastFireTime = 0f;
    private float timeElapsed = 0f;
    private ComponentMapper<TransformComponent> tm;

    Entity player;

    public FiringSystem(){
        super(Family.all(PlayerComponent.class).get());
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(player != null) {
            timeBetweenFiring = 1f / firingRate;
            timeElapsed += deltaTime;

            if (timeElapsed - lastFireTime >= timeBetweenFiring) {

                lastFireTime = timeElapsed;
                generateBullet(0f, 3f);
//                generateBullet(1f, 5f);
//                generateBullet(-1f, 5f);
                generateBullet(1.5f, 2f);
                generateBullet(-1.5f, 2f);

            }
        }

        player = null;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(player != null) {
            throw new IllegalStateException("Cannot handle Two Players right now!");
        }
        player = entity;
    }


    private void generateBullet(float xVel, float yVel){
        TransformComponent playerPos = tm.get(player);
        //Generate Bullets here
        Entity bullet = ((PooledEngine) getEngine()).createEntity();
        bullet.add(WhenOffScreenComponent.create());
        bullet.add(KinematicComponent.create());
        bullet.add(TransformComponent.create()
                .setPosition(playerPos.position.x, playerPos.position.y, playerPos.position.z - 1f)
                .setScale(1f, 1f));
        bullet.add(BoundsComponent.create()
                .setBounds(playerPos.position.x - 0.25f, playerPos.position.y - 0.25f, 0.5f, 0.5f)
                .setOffset(0f, 0.5f));
        bullet.add(TextureComponent.create());
        bullet.add(AnimationComponent.create()
                .addAnimation("DEFAULT", new Animation(1f/2f, Assets.getBulletFrames(), Animation.PlayMode.NORMAL))
                .addAnimation("FLYING", new Animation(1f / 3f, Assets.getBulletFlyingFrames(), Animation.PlayMode.NORMAL)));
        bullet.add(StateComponent.create()
                .set("DEFAULT")
                .setLooping(false));
        bullet.add(BulletComponent.create());
        bullet.add(VelocityComponent.create()
                .setSpeed(xVel, yVel));
        getEngine().addEntity(bullet);
    }
}

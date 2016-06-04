package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.components.*;
import com.roaringcatgames.libgdxjam.values.Rates;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * System to recognize and apply power-up pick-ups
 */
public class PowerUpSystem extends IteratingSystem {

    private Entity player;
    private Array<Entity> powerUps = new Array<>();

    Array<Vector2> muzzlePositions = new Array<>();

    public PowerUpSystem(){
        super(Family.all(BoundsComponent.class)
                .one(PlayerComponent.class, PowerUpComponent.class).get());
        this.muzzlePositions.add(new Vector2(-0.906f, 0.881f));
        this.muzzlePositions.add(new Vector2(0.906f, 0.881f));
        this.muzzlePositions.add(new Vector2(-1.312f, 0.3f));
        this.muzzlePositions.add(new Vector2(1.312f, 0.3f));
        this.muzzlePositions.add(new Vector2(-1.612f, 0.1f));
        this.muzzlePositions.add(new Vector2(1.612f, 0.1f));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(player != null) {
            TransformComponent playerTransform = K2ComponentMappers.transform.get(player);
            BoundsComponent playerBounds = K2ComponentMappers.bounds.get(player);

            for (Entity pu : powerUps) {
                BoundsComponent powerUpBounds = K2ComponentMappers.bounds.get(pu);
                if(powerUpBounds.bounds.overlaps(playerBounds.bounds)){
                    PowerUpComponent pc = Mappers.powerUp.get(pu);
                    switch(pc.powerUpType){
                        case UPGRADE:
                            upgradeWeapon(playerTransform);
                            break;

                    }
                    getEngine().removeEntity(pu);
                }
            }
        }
        this.powerUps.clear();
        this.player = null;
    }


    private void upgradeWeapon(TransformComponent playerTransform) {
        PlayerComponent playerComponent = Mappers.player.get(player);
        PooledEngine engine = (PooledEngine)getEngine();
        //Update WeaponLevel
        if(playerComponent.weaponLevel != WeaponLevel.LEVEL_4){
            playerComponent.weaponLevel = playerComponent.weaponLevel == WeaponLevel.LEVEL_1 ? WeaponLevel.LEVEL_2 :
                    playerComponent.weaponLevel == WeaponLevel.LEVEL_2 ? WeaponLevel.LEVEL_3 :
                            WeaponLevel.LEVEL_4;
            switch(playerComponent.weaponType){
                case GUN_SEEDS:

                    if(playerComponent.weaponLevel == WeaponLevel.LEVEL_2){
                        //Add 2 more guns
                        addGun(playerTransform, engine, muzzlePositions.get(0).x, muzzlePositions.get(0).y, Rates.SEED_GUN_TIME_BETWEEN);
                        addGun(playerTransform, engine, muzzlePositions.get(1).x, muzzlePositions.get(1).y, Rates.SEED_GUN_TIME_BETWEEN);

                    }else if(playerComponent.weaponLevel == WeaponLevel.LEVEL_3){
                        //Add 2 more guns
                        addGun(playerTransform, engine, muzzlePositions.get(2).x, muzzlePositions.get(2).y, Rates.SEED_GUN_TIME_BETWEEN);
                        addGun(playerTransform, engine, muzzlePositions.get(3).x, muzzlePositions.get(3).y, Rates.SEED_GUN_TIME_BETWEEN);
                    }else{
                        //Add Gatling guns
                        Gdx.app.log("PowerUpSystem", "Upgrading to level 4!");
                        addGun(playerTransform, engine, muzzlePositions.get(4).x, muzzlePositions.get(4).y, Rates.SEED_GUN_GATLING_TIME_BETWEEN, true);
                        addGun(playerTransform, engine, muzzlePositions.get(5).x, muzzlePositions.get(5).y, Rates.SEED_GUN_GATLING_TIME_BETWEEN, true);
                    }
                    break;
            }
        }
    }

    private void addGun(TransformComponent playerTransform, PooledEngine engine,
                        float x, float y, float timeBetweenShots, boolean...hasGun) {

        boolean isGatling = hasGun != null && hasGun.length == 1 && hasGun[0];

        float lastSynchronizedFireTime = 0f;
        if(!isGatling) {
            for (Entity m : engine.getEntitiesFor(Family.all(GunComponent.class).get())) {
                GunComponent gc = Mappers.gun.get(m);
                if (gc.lastFireTime > lastSynchronizedFireTime) {
                    lastSynchronizedFireTime = gc.lastFireTime;
                }
            }
        }


        Animation muzzleAni = Animations.getMuzzle();
        Entity muzzle = engine.createEntity();
        muzzle.add(GunComponent.create(engine)
                .setTimeBetweenShots(timeBetweenShots)
                .setLastFireTime(lastSynchronizedFireTime)
                .setBulletSpeed(isGatling ? Rates.SEED_GUN_GATLING_BULLET_SPEED : Rates.SEED_GUN_BULLET_SPEED));
        muzzle.add(FollowerComponent.create(engine)
                .setOffset(x * playerTransform.scale.x, y * playerTransform.scale.y)
                .setTarget(player)
                .setMode(FollowMode.STICKY));
        muzzle.add(TextureComponent.create(engine));
        muzzle.add(AnimationComponent.create(engine)
                .addAnimation("FIRING", muzzleAni));
        muzzle.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(false));
        muzzle.add(TransformComponent.create(engine)
                .setPosition(playerTransform.position.x, playerTransform.position.y, Z.muzzleFlash)
                .setScale(playerTransform.scale.x * 0.5f, playerTransform.scale.y * 0.5f)
                .setOpacity(0.8f));
        getEngine().addEntity(muzzle);

        if(isGatling){
            Entity gun = engine.createEntity();
            gun.add(DecorationComponent.create(engine));
            gun.add(TransformComponent.create(engine)
                .setPosition(playerTransform.position.x, playerTransform.position.y, Z.gatlingGuns)
                .setScale(playerTransform.scale.x, playerTransform.scale.y));
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
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
          if(Mappers.player.has(entity)){
              this.player = entity;
          }else {
              powerUps.add(entity);
          }
    }
}

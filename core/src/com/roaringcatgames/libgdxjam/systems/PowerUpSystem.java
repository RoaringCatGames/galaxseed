package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Engine;
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
                .one(PlayerComponent.class, WeaponComponent.class, PowerUpComponent.class).get());
        this.muzzlePositions.add(new Vector2(-0.906f, 0.881f));
        this.muzzlePositions.add(new Vector2(0.906f, 0.881f));
        this.muzzlePositions.add(new Vector2(-1.312f, 0.3f));
        this.muzzlePositions.add(new Vector2(1.312f, 0.3f));
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
        WeaponComponent wc = Mappers.weapon.get(player);
        PooledEngine engine = (PooledEngine)getEngine();
        //Update WeaponLevel
        if(wc.weaponLevel != WeaponLevel.LEVEL_4){
            wc.weaponLevel = wc.weaponLevel == WeaponLevel.LEVEL_1 ? WeaponLevel.LEVEL_2 :
                    wc.weaponLevel == WeaponLevel.LEVEL_2 ? WeaponLevel.LEVEL_3 :
                            WeaponLevel.LEVEL_4;
            switch(wc.weaponType){
                case GUN_SEEDS:

                    if(wc.weaponLevel == WeaponLevel.LEVEL_2){
                        //Add 2 more guns
                        addGun(playerTransform, engine, muzzlePositions.get(0).x, muzzlePositions.get(0).y);
                        addGun(playerTransform, engine, muzzlePositions.get(1).x, muzzlePositions.get(1).y);

                    }else if(wc.weaponLevel == WeaponLevel.LEVEL_3){
                        //Add 2 more guns
                        addGun(playerTransform, engine, muzzlePositions.get(2).x, muzzlePositions.get(2).y);
                        addGun(playerTransform, engine, muzzlePositions.get(3).x, muzzlePositions.get(3).y);
                    }else{
                        //Add Gatling guns
                        Gdx.app.log("PowerUpSystem", "Upgrading to level 4!");
                    }
                    break;
            }
        }
    }

    private void addGun(TransformComponent playerTransform, PooledEngine engine, float x, float y) {
        Animation muzzleAni = Animations.getMuzzle();
        Entity muzzle = engine.createEntity();
        muzzle.add(GunComponent.create(engine)
                .setFiringRate(Rates.seedGunTimeBetween));
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

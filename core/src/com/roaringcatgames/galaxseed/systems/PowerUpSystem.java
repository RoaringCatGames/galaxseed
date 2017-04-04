package com.roaringcatgames.galaxseed.systems;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.galaxseed.data.entitydefs.Transform;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.galaxseed.Animations;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.Sfx;
import com.roaringcatgames.galaxseed.components.*;
import com.roaringcatgames.galaxseed.values.GameState;
import com.roaringcatgames.galaxseed.values.Rates;
import com.roaringcatgames.galaxseed.values.Z;

/**
 * System to recognize and apply power-up pick-ups
 */
public class PowerUpSystem extends IteratingSystem implements InputProcessor {

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
        this.muzzlePositions.add(new Vector2(-1.512f, 0f));
        this.muzzlePositions.add(new Vector2(1.512f, 0f));
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        //App.game.multiplexer.addProcessor(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        //App.game.multiplexer.removeProcessor(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(player != null && App.getState() != GameState.GAME_OVER) {
            BoundsComponent playerBounds = K2ComponentMappers.bounds.get(player);

            for (Entity pu : powerUps) {
                BoundsComponent powerUpBounds = K2ComponentMappers.bounds.get(pu);
                if(powerUpBounds.bounds.overlaps(playerBounds.bounds)){
                    PowerUpComponent pc = Mappers.powerUp.get(pu);
                    switch(pc.powerUpType){
                        case UPGRADE:
                            TransformComponent powerUpTfm = K2ComponentMappers.transform.get(pu);
                            PlayerComponent playerComponent = Mappers.player.get(player);

                            indicatePowerUp(powerUpTfm, playerComponent, getEngine());
                            upgradeWeapon(playerComponent);
                            break;

                    }
                    getEngine().removeEntity(pu);
                }
            }
        }
        this.powerUps.clear();
    }

    private void indicatePowerUp(TransformComponent powerUpTfm, PlayerComponent playerComponent, Engine engine){
        Entity indicator = engine.createEntity();
        indicator.add(TransformComponent.create(engine)
            .setPosition(powerUpTfm.position.x, powerUpTfm.position.y, Z.powerUpIndicator)
            .setScale(0f, 0f));
        indicator.add(TextureComponent.create(engine));
        indicator.add(StateComponent.create(engine)
            .set("DEFAULT")
            .setLooping(true));

        boolean isMax = playerComponent.weaponLevel == WeaponLevel.LEVEL_3 || playerComponent.weaponLevel == WeaponLevel.LEVEL_4;
        indicator.add(AnimationComponent.create(engine)
            .addAnimation("DEFAULT", isMax ? Animations.getPwrMax() : Animations.getPwrUp()));

        Timeline tl = Timeline.createParallel()
                .push(Tween.to(indicator, K2EntityTweenAccessor.OPACITY, 2f)
                    .target(0f))
                .push(Timeline.createSequence()
                .push(Tween.to(indicator, K2EntityTweenAccessor.SCALE, .5f)
                        .target(1f, 1f))
                .push(Tween.to(indicator, K2EntityTweenAccessor.SCALE, 1f)
                        .target(0f, 0f))
                .push(Tween.to(indicator, K2EntityTweenAccessor.POSITION_XY, 0.5f)
                        .target(-30f, -30f)));

        indicator.add(TweenComponent.create(engine)
            .setTimeline(tl));
        indicator.add(WhenOffScreenComponent.create(engine)
            .setHasBeenOnScreen(true));
        engine.addEntity(indicator);
    }

    private void upgradeWeapon(PlayerComponent playerComponent) {
        if(playerComponent == null){
            return;
        }
        PooledEngine engine = (PooledEngine)getEngine();
        //Update WeaponLevel
        if(playerComponent.weaponType != WeaponType.UNSELECTED &&
                playerComponent.weaponLevel != WeaponLevel.LEVEL_4){


            playerComponent.weaponLevel = playerComponent.weaponLevel == WeaponLevel.LEVEL_1 ? WeaponLevel.LEVEL_2 :
                                          playerComponent.weaponLevel == WeaponLevel.LEVEL_2 ? WeaponLevel.LEVEL_3 :
                                                                                               WeaponLevel.LEVEL_4;
            App.setCurrentWeaponLevel(playerComponent.weaponType, playerComponent.weaponLevel);

            WeaponGeneratorUtil.clearWeapon(engine);
            switch(playerComponent.weaponType){
                case GUN_SEEDS:
                    WeaponGeneratorUtil.generateSeedGuns(player, engine);
                    break;
                case POLLEN_AURA:
                    WeaponGeneratorUtil.generateAura(player, engine);
                    break;
                case HELICOPTER_SEEDS:
                    WeaponGeneratorUtil.generateHelicopterGuns(player, engine);
                    break;
            }
        }

        if(playerComponent.weaponLevel != WeaponLevel.LEVEL_4) {
            Sfx.playUpgradeSound();
        }else{
            Sfx.playMaxUpgradeSound();
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
        getEngine().addEntity(muzzle);

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
                    .setSpawnRate(2f)
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

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
          if(Mappers.player.has(entity)){
              this.player = entity;
          }else {
              powerUps.add(entity);
          }
    }

    @Override
    public boolean keyDown(int keycode) {
//        if(keycode == Input.Keys.SPACE){
//            if(player != null) {
//                upgradeWeapon();
//            }
//        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

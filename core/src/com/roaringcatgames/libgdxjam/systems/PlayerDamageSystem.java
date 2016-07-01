package com.roaringcatgames.libgdxjam.systems;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.PrefsUtil;
import com.roaringcatgames.libgdxjam.components.*;
import com.roaringcatgames.libgdxjam.values.*;

/**
 * System to handle player getting hit
 */
public class PlayerDamageSystem extends IteratingSystem {

    private Entity player;
    private Entity scoreCard;
    private Array<Entity> projectiles = new Array<>();
    private Array<Entity> healthLeaves = new Array<>();

    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<ProjectileComponent> pm;
    private ComponentMapper<CircleBoundsComponent> cm;
    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<ScoreComponent> scm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<HealthLeafComponent> hlm;


    private Sound mediumHitSfx, heavyHitSfx;

    public PlayerDamageSystem(){
        super(Family.one(ScoreComponent.class, PlayerComponent.class,
                         ProjectileComponent.class, HealthLeafComponent.class).get());
        bm = ComponentMapper.getFor(BoundsComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
        pm = ComponentMapper.getFor(ProjectileComponent.class);
        cm = ComponentMapper.getFor(CircleBoundsComponent.class);
        em = ComponentMapper.getFor(EnemyComponent.class);
        scm = ComponentMapper.getFor(ScoreComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        hlm = ComponentMapper.getFor(HealthLeafComponent.class);

        mediumHitSfx = Assets.getPlayerHitMedium();
        heavyHitSfx = Assets.getPlayerHitHeavy();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        BoundsComponent pb = bm.get(player);
        HealthComponent ph = hm.get(player);
        TransformComponent pt = tm.get(player);


        for (Entity proj : projectiles) {
            CircleBoundsComponent cb = cm.get(proj);
            if (Intersector.overlaps(cb.circle, pb.bounds)) {
                ProjectileComponent pp = pm.get(proj);
                processCollision(pt, ph, proj, pp);
            }
        }


        if (ph.health <= 0f && App.getState() != GameState.GAME_OVER) {
            PlayerComponent pc = Mappers.player.get(player);
            App.toggleWeapon(pc.weaponType, false);
            pc.weaponType = WeaponType.UNSELECTED;
            WeaponGeneratorUtil.clearWeapon(getEngine());
            if(App.hasAvailableWeapons()){
                //DO SOMETHING TO SWITCH WEAPONS
                App.setState(GameState.WEAPON_SELECT);
                ph.health = Health.Player;
            }else {
                App.setState(GameState.GAME_OVER);
            }
        }

        projectiles.clear();
        healthLeaves.clear();
    }


    private void processCollision(TransformComponent playerPos, HealthComponent ph, Entity proj, ProjectileComponent pp) {
        TransformComponent projPos = tm.get(proj);
        float scale = 0.5f;
        float xOffset = 0f, yOffset = 0f;
        Vector3 halfPos = playerPos.position.cpy().sub(projPos.position).scl(0.5f);
        xOffset = halfPos.x;
        yOffset = halfPos.y;
        if(pp.damage == Damage.asteroidRock) {
            if(PrefsUtil.areSfxEnabled()) {
                mediumHitSfx.play(Volume.PLAYER_HIT_M);
            }
        }else if(pp.damage == Damage.comet){
            scale = 0.8f;
            if(PrefsUtil.areSfxEnabled()) {
                mediumHitSfx.play(Volume.PLAYER_HIT_M);
            }
        }else if(pp.damage == Damage.asteroid){
            if(PrefsUtil.areSfxEnabled()) {
                heavyHitSfx.play(Volume.PLAYER_HIT_H);
            }
            scale = 1f;
        }

        if(PrefsUtil.isVibrationOn()) {
            Gdx.input.vibrate(500);
        }

        boolean leftFirst = true;
        for(Entity leaf:healthLeaves){
            TransformComponent ltc = tm.get(leaf);
            float rotOffset = K2MathUtil.getRandomInRange(4f, 8f);
            Timeline tl = Timeline.createSequence()
                        .push(Tween.to(leaf, K2EntityTweenAccessor.ROTATION, 0.0125f)
                                .target(ltc.rotation + (leftFirst ? -rotOffset : rotOffset)))
                        .push(Tween.to(leaf, K2EntityTweenAccessor.ROTATION, 0.0125f)
                                    .target(0f))
                        .push(Tween.to(leaf, K2EntityTweenAccessor.ROTATION, 0.0125f)
                                .target(ltc.rotation + (leftFirst ? rotOffset : -rotOffset)))
                    .push(Tween.to(leaf, K2EntityTweenAccessor.ROTATION, 0.0125f)
                            .target(0f))
                    .repeat(3, 0);
            leaf.add(TweenComponent.create(getEngine())
                .setTimeline(tl));
            leftFirst = !leftFirst;
        }

        //Adjust Player Health
        HealthComponent projHealth = hm.get(proj);
        scm.get(scoreCard).score -= (projHealth.maxHealth - projHealth.health);
        ph.health = Math.max(0f, ph.health - pp.damage);


        //Generate Explosion
        EnemyComponent ec = em.get(proj);
        ec.isDamaging = false;
        Animation impactAni;
        switch(ec.enemyColor){
            case BLUE:
                impactAni = Animations.getImpactB();
                break;
            case PURPLE:
                impactAni = Animations.getImpactC();
                break;
            default:
                impactAni = Animations.getImpactA();
                break;
        }

        PooledEngine engine = ((PooledEngine)getEngine());
        Entity explosion = engine.createEntity();
        explosion.add(ExplosionComponent.create(engine));
        explosion.add(TransformComponent.create(engine)
                .setPosition(projPos.position.x + xOffset, projPos.position.y + yOffset, Z.explosion)
                .setScale(scale, scale)
                .setRotation(projPos.rotation));
        explosion.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(false));
        explosion.add(TextureComponent.create(engine));
        explosion.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", impactAni));
        engine.addEntity(explosion);

        //Remove Entity
        engine.removeEntity(proj);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(pm.has(entity)){
            projectiles.add(entity);
        }else if(scm.has(entity)) {
            scoreCard = entity;
        }else if(hlm.has(entity)) {
            healthLeaves.add(entity);
        }else{
            player = entity;
        }
    }
}

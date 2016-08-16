package com.roaringcatgames.galaxseed.systems;

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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.galaxseed.Animations;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.PrefsUtil;
import com.roaringcatgames.galaxseed.components.*;
import com.roaringcatgames.galaxseed.values.*;

/**
 * System to handle player getting hit
 */
public class PlayerDamageSystem extends IteratingSystem {

    private Entity player;
    private Entity scoreCard;
    private Array<Entity> projectiles = new Array<>();
    private Array<Entity> shields = new Array();

    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<ProjectileComponent> pm;
    private ComponentMapper<CircleBoundsComponent> cm;
    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<ScoreComponent> scm;
    private ComponentMapper<TransformComponent> tm;


    private Sound mediumHitSfx, heavyHitSfx;

    public PlayerDamageSystem(){
        super(Family.one(ScoreComponent.class, PlayerComponent.class,
                ShieldComponent.class, ProjectileComponent.class).get());
        bm = ComponentMapper.getFor(BoundsComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
        pm = ComponentMapper.getFor(ProjectileComponent.class);
        cm = ComponentMapper.getFor(CircleBoundsComponent.class);
        em = ComponentMapper.getFor(EnemyComponent.class);
        scm = ComponentMapper.getFor(ScoreComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);

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
            ProjectileComponent pp = pm.get(proj);
            TransformComponent projTrans = K2ComponentMappers.transform.get(proj);

            boolean didHitShield = false;
            for(Entity shield:shields){
                CircleBoundsComponent scb = K2ComponentMappers.circleBounds.get(shield);
                if(cb.circle.overlaps(scb.circle)){
                    Vector3 halfPos = pt.position.cpy().sub(pt.position).scl(0.5f);
                    float scale = getScaleForProjectileExplosion(proj);
                    explodeProjectile(proj, projTrans, scale, halfPos.x, halfPos.y);
                    getEngine().removeEntity(proj);
                    didHitShield = true;
                }
            }

            if (!didHitShield && Intersector.overlaps(cb.circle, pb.bounds)) {
                processCollision(pt, ph, proj, pp);
            }
        }


        if (ph.health <= 0f && App.getState() != GameState.GAME_OVER) {
            PlayerComponent pc = Mappers.player.get(player);
            App.toggleWeapon(pc.weaponType, false);
            pc.weaponType = WeaponType.UNSELECTED;
            float shieldTime = 3f;
            WeaponGeneratorUtil.clearWeapon(getEngine());
            if(App.hasAvailableWeapons()){
                //Add Shield
                generateShield(pt, shieldTime);
                //Trigger Weapon Select
                App.setState(GameState.WEAPON_SELECT);
                ph.health = Health.Player;
            }else {
                App.setState(GameState.GAME_OVER);
            }
        }

        projectiles.clear();
        shields.clear();
    }

    private void generateShield(TransformComponent pt, float shieldTime) {
        Gdx.app.log("Player Damage System", "Adding shield");
        //Add Shield
        PooledEngine engine = ((PooledEngine) getEngine());
        Entity e = engine.createEntity();
        e.add(ShieldComponent.create(engine));
        e.add(TransformComponent.create(engine)
            .setPosition(pt.position.x, pt.position.y, Z.shield)
            .setScale(0.1f, 0.1f));
        e.add(FollowerComponent.create(engine)
                .setTarget(player));
        e.add(CircleBoundsComponent.create(engine)
                .setCircle(pt.position.x, pt.position.y, 3f * 0.1f));

        Timeline scaleUp = Timeline.createParallel()
                .push(
                    Tween.to(e, K2EntityTweenAccessor.SCALE, 0.1f)
                        .target(1f, 1f)
                        .ease(TweenEquations.easeOutElastic))
                .push(
                    Tween.to(e, K2EntityTweenAccessor.BOUNDS_RADIUS, 0.1f)
                        .target(3f, 3f)
                        .ease(TweenEquations.easeOutElastic));

        Timeline decay = Timeline.createParallel()
                .push(
                    Tween.to(e, K2EntityTweenAccessor.BOUNDS_RADIUS, shieldTime)
                        .target(0f))
                .push(
                    Tween.to(e, K2EntityTweenAccessor.SCALE, shieldTime)
                        .target(0f, 0f));


        Timeline timeline = Timeline.createSequence().push(scaleUp).push(decay);

        e.add(TweenComponent.create(engine)
            .setTimeline(timeline));

        e.add(TextureComponent.create(engine));
        e.add(AnimationComponent.create(engine)
            .addAnimation("DEFAULT", Animations.getShield()));
        e.add(StateComponent.create(engine)
            .setLooping(true)
            .set("DEFAULT"));
        engine.addEntity(e);
    }


    private void processCollision(TransformComponent playerPos, HealthComponent ph, Entity proj, ProjectileComponent pp) {
        TransformComponent projPos = tm.get(proj);


        if(PrefsUtil.areSfxEnabled()) {
            mediumHitSfx.play(Volume.PLAYER_HIT_M);
        }

        if(PrefsUtil.isVibrationOn()) {
            Gdx.input.vibrate(500);
        }

        //Adjust Player Health
        HealthComponent projHealth = hm.get(proj);
        scm.get(scoreCard).score -= (projHealth.maxHealth - projHealth.health);
        ph.health = Math.max(0f, ph.health - pp.damage);

        Vector3 halfPos = playerPos.position.cpy().sub(projPos.position).scl(0.5f);
        float scale = getScaleForProjectileExplosion(proj);
        explodeProjectile(proj, projPos, scale, halfPos.x, halfPos.y);


    }

    private float getScaleForProjectileExplosion(Entity proj) {
        float scale = 0.5f;
        if(Mappers.enemy.has(proj)){
            EnemyComponent ec = Mappers.enemy.get(proj);
            scale = ec.enemyType == EnemyType.COMET ? 0.8f : ec.enemyType != EnemyType.ASTEROID_FRAG ? 1f : 0.5f;
        }
        return scale;
    }

    private void explodeProjectile(Entity proj, TransformComponent projPos, float scale, float xOffset, float yOffset) {
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
        }else if(Mappers.shield.has(entity)) {
            shields.add(entity);
        }else{
            player = entity;
        }
    }
}

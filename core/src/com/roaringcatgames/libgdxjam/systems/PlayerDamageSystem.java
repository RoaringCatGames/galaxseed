package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.*;
import com.roaringcatgames.libgdxjam.values.Damage;
import com.roaringcatgames.libgdxjam.values.Shakes;
import com.roaringcatgames.libgdxjam.values.Volume;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * Created by barry on 1/12/16 @ 7:59 PM.
 */
public class PlayerDamageSystem extends IteratingSystem {

    private static float SHAKE_TIME = 0.5f;
    private Entity player;
    private Entity scoreCard;
    private Array<Entity> projectiles = new Array<>();

    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<ProjectileComponent> pm;
    private ComponentMapper<CircleBoundsComponent> cm;
    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<ShakeComponent> sm;
    private ComponentMapper<ScoreComponent> scm;
    private ComponentMapper<TransformComponent> tm;

    private ComponentMapper<AnimationComponent> am;
    private ComponentMapper<StateComponent> stm;


    private Sound lightHitSfx, mediumHitSfx, heavyHitSfx;

    public PlayerDamageSystem(){
        super(Family.one(ScoreComponent.class, PlayerComponent.class, ProjectileComponent.class).get());
        bm = ComponentMapper.getFor(BoundsComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
        pm = ComponentMapper.getFor(ProjectileComponent.class);
        cm = ComponentMapper.getFor(CircleBoundsComponent.class);
        em = ComponentMapper.getFor(EnemyComponent.class);
        sm = ComponentMapper.getFor(ShakeComponent.class);
        scm = ComponentMapper.getFor(ScoreComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);

        am = ComponentMapper.getFor(AnimationComponent.class);
        stm = ComponentMapper.getFor(StateComponent.class);

        lightHitSfx = Assets.getPlayerHitLight();
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
            ProjectileComponent pp = pm.get(proj);
            if (em.has(proj)) {
                EnemyComponent ec = em.get(proj);
                if (!ec.isDamaging) {
                    continue;
                }
            }

            CircleBoundsComponent cb = cm.get(proj);
            if (Intersector.overlaps(cb.circle, pb.bounds)) {
                processCollision(pt, ph, proj, pp);
            }
        }

        projectiles.clear();
    }


    private void processCollision(TransformComponent playerPos, HealthComponent ph, Entity proj, ProjectileComponent pp) {
        TransformComponent projPos = tm.get(proj);
        float shakeTime = Shakes.TimePlayerHitLight;
        float scale = 0.5f;
        float xOffset = 0f, yOffset = 0f;
        Vector3 halfPos = playerPos.position.cpy().sub(projPos.position).scl(0.5f);
        xOffset = halfPos.x;
        yOffset = halfPos.y;
        if(pp.damage == Damage.asteroidRock) {
            mediumHitSfx.play(Volume.PLAYER_HIT_M);
        }else if(pp.damage == Damage.comet){
            scale = 0.8f;
            mediumHitSfx.play(Volume.PLAYER_HIT_M);
        }else if(pp.damage == Damage.asteroid){
            heavyHitSfx.play(Volume.PLAYER_HIT_H);
            shakeTime = Shakes.TimePlayerHitHeavy;
            scale = 1f;
        }


        //SHAKE WHEN HIT!!!
        if(sm.has(player)) {
            ShakeComponent sc = sm.get(player);
            if(sc.isPaused){
                sc.setCurrentTime(0f);
                sc.setPaused(false);
                sc.setDuration(shakeTime);
            }
        }

        HealthComponent projHealth = hm.get(proj);
        scm.get(scoreCard).score -= (projHealth.maxHealth - projHealth.health);

        ph.health = Math.max(0f, ph.health - pp.damage);


        EnemyComponent ec = em.get(proj);
        ec.isDamaging = false;
        Array<TextureAtlas.AtlasRegion> impactFrames;
        switch(ec.enemyColor){
            case BLUE:
                impactFrames = Assets.getImpactB();
                break;
            case PURPLE:
                impactFrames = Assets.getImpactC();
                break;
            default:
                impactFrames = Assets.getImpactA();
                break;
        }

        //Generate Explosion

        App.setSlowed(true);
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
                .addAnimation("DEFAULT", new Animation(1f / 12f, impactFrames)));
        engine.addEntity(explosion);

        //Remove Entity
        engine.removeEntity(proj);
//        proj.getComponent(TransformComponent.class).scale.set(scale, scale);
//        proj.remove(VelocityComponent.class);
//        AnimationComponent ac = am.get(proj);
//        if(ac == null) {
//            proj.add(AnimationComponent.create(getEngine())
//                    .addAnimation("DEFAULT", new Animation(1f / 18f, impactFrames)));
//            proj.add(StateComponent.create(getEngine())
//                    .set("DEFAULT")
//                    .setLooping(false));
//        }else{
//            ac.addAnimation("EXPLODED", new Animation(1f/18f, impactFrames));
//            StateComponent sc = stm.get(proj);
//            sc.setLooping(false);
//            sc.set("EXPLODED");
//        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(pm.has(entity)){
            projectiles.add(entity);
        }else if(scm.has(entity)) {
            scoreCard = entity;
        }else{
            player = entity;
        }
    }
}

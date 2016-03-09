package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.CircleBoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.HealthComponent;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.EnemyComponent;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.ProjectileComponent;
import com.roaringcatgames.libgdxjam.components.ShakeComponent;
import com.roaringcatgames.libgdxjam.values.Damage;
import com.roaringcatgames.libgdxjam.values.Shakes;
import com.roaringcatgames.libgdxjam.values.Volume;

/**
 * Created by barry on 1/12/16 @ 7:59 PM.
 */
public class PlayerDamageSystem extends IteratingSystem {

    private static float SHAKE_TIME = 0.5f;
    private Entity player;
    private Array<Entity> projectiles = new Array<>();

    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<ProjectileComponent> pm;
    private ComponentMapper<CircleBoundsComponent> cm;
    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<ShakeComponent> sm;

    private Sound lightHitSfx, mediumHitSfx, heavyHitSfx;

    public PlayerDamageSystem(){
        super(Family.one(PlayerComponent.class, ProjectileComponent.class).get());
        bm = ComponentMapper.getFor(BoundsComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
        pm = ComponentMapper.getFor(ProjectileComponent.class);
        cm = ComponentMapper.getFor(CircleBoundsComponent.class);
        em = ComponentMapper.getFor(EnemyComponent.class);
        sm = ComponentMapper.getFor(ShakeComponent.class);

        lightHitSfx = Assets.getPlayerHitLight();
        mediumHitSfx = Assets.getPlayerHitMedium();
        heavyHitSfx = Assets.getPlayerHitHeavy();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        BoundsComponent pb = bm.get(player);
        HealthComponent ph = hm.get(player);

        for(Entity proj:projectiles){
            ProjectileComponent pp = pm.get(proj);
            if(em.has(proj)){
                EnemyComponent ec = em.get(proj);
                if(!ec.isDamaging){
                    continue;
                }
            }

            if(bm.has(proj)) {
                BoundsComponent pjb = bm.get(proj);
                if (pb.bounds.overlaps(pjb.bounds)) {
                    //TODO: Do Projectile Explosion stuff
                    processCollision(ph, proj, pp);
                }
            }else if(cm.has(proj)){
                CircleBoundsComponent cb = cm.get(proj);
                if(Intersector.overlaps(cb.circle, pb.bounds)){
                    //TODO: Do Projectile Explosion stuff
                    processCollision(ph, proj, pp);
                }
            }
        }
        projectiles.clear();
    }


    private void processCollision(HealthComponent ph, Entity proj, ProjectileComponent pp) {
        float shakeTime = Shakes.TimePlayerHitLight;
        if(pp.damage == Damage.asteroidRock) {
            mediumHitSfx.play(Volume.PLAYER_HIT_M);
        }else if(pp.damage == Damage.comet){
            mediumHitSfx.play(Volume.PLAYER_HIT_M);
        }else if(pp.damage == Damage.asteroid){
            heavyHitSfx.play(Volume.PLAYER_HIT_H);
            shakeTime = Shakes.TimePlayerHitHeavy;
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

        ph.health = Math.max(0f, ph.health - pp.damage);
        getEngine().removeEntity(proj);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(pm.has(entity)){
            projectiles.add(entity);
        }else{
            player = entity;
        }
    }
}

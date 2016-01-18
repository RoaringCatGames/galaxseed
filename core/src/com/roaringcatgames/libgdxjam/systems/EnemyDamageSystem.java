package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.VectorUtils;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.Z;
import com.roaringcatgames.libgdxjam.components.*;

import java.util.Random;

/**
 * Created by barry on 1/12/16 @ 7:23 PM.
 */
public class EnemyDamageSystem extends IteratingSystem {

    private Array<Entity> bullets = new Array<>();
    private Array<Entity> enemies = new Array<>();

    private ComponentMapper<BulletComponent> bm;
    private ComponentMapper<BoundsComponent> bndm;
    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<CircleBoundsComponent> cm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<DamageComponent> dm;
    private ComponentMapper<SpawnerComponent> sm;
    private ComponentMapper<StateComponent> stm;

    Random r = new Random();

    public EnemyDamageSystem(){
        super(Family.one(BulletComponent.class, EnemyComponent.class).get());
        bm = ComponentMapper.getFor(BulletComponent.class);
        em = ComponentMapper.getFor(EnemyComponent.class);
        bndm = ComponentMapper.getFor(BoundsComponent.class);
        cm = ComponentMapper.getFor(CircleBoundsComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
        dm = ComponentMapper.getFor(DamageComponent.class);
        sm = ComponentMapper.getFor(SpawnerComponent.class);
        stm = ComponentMapper.getFor(StateComponent.class);
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for(Entity bullet:bullets){
            CircleBoundsComponent bb = cm.get(bullet);
            for(Entity enemy:enemies){
                if(enemy.isScheduledForRemoval()){
                    continue;
                }
                if(bndm.has(enemy)) {
                    BoundsComponent eb = bndm.get(enemy);
                    if (Intersector.overlaps(bb.circle, eb.bounds)) {
                        getEngine().removeEntity(bullet);
                        getEngine().removeEntity(enemy);
                    }
                }else if(cm.has(enemy)){
                    CircleBoundsComponent cb = cm.get(enemy);
                    if (cb.circle.overlaps(bb.circle)) {
                        processCollision(bullet, enemy);
                    }
                }
            }
        }

        enemies.clear();
        bullets.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if(!entity.isScheduledForRemoval()) {
            if (bm.has(entity)) {
                bullets.add(entity);
            } else {
               enemies.add(entity);
            }
        }
    }


    private Vector2 bulletPos = new Vector2();
    private Vector2 enemyPos = new Vector2();
    private void processCollision(Entity bullet, Entity enemy){

        EnemyComponent ec = em.get(enemy);
        HealthComponent hc;
        switch(ec.enemyType) {
            case ASTEROID_FRAG:
                getEngine().removeEntity(bullet);
                getEngine().removeEntity(enemy);
                break;

            case COMET:
                attachPlant(bullet, enemy);
                hc = applyHealthChange(bullet, enemy);
            default:
                attachPlant(bullet, enemy);

                hc = applyHealthChange(bullet, enemy);


                if(hc.health <= 0f) {
                    switch(ec.enemyType){
                        case ASTEROID_A:
                            enemy.add(AnimationComponent.create()
                                .addAnimation("DEFAULT", new Animation(1f / 6f, Assets.getAsteroidAFrames())));
                            break;
                        case ASTEROID_B:
                            enemy.add(AnimationComponent.create()
                                    .addAnimation("DEFAULT", new Animation(1f / 6f, Assets.getAsteroidBFrames())));
                            break;
                        case ASTEROID_C:
                            enemy.add(AnimationComponent.create()
                                    .addAnimation("DEFAULT", new Animation(1f / 6f, Assets.getAsteroidCFrames())));
                            break;
                        case COMET:

                            break;
                    }
                    enemy.add(StateComponent.create().set("DEFAULT").setLooping(false));

                    if(sm.has(enemy)){
                        sm.get(enemy).setPaused(true);
                    }
                }


                getEngine().removeEntity(bullet);

                break;
        }
    }

    private HealthComponent applyHealthChange(Entity bullet, Entity enemy) {
        HealthComponent hc = hm.get(enemy);
        DamageComponent dmg = dm.get(bullet);
        hc.health = Math.max(0f, (hc.health - dmg.dps));
        return hc;
    }

    private void attachPlant(Entity bullet, Entity enemy) {
        CircleBoundsComponent bb = cm.get(bullet);
        CircleBoundsComponent eb = cm.get(enemy);
        TransformComponent et = tm.get(enemy);
        bulletPos.set(bb.circle.x, bb.circle.y);
        enemyPos.set(eb.circle.x, eb.circle.y);

        Vector2 outVec = bulletPos.sub(enemyPos).nor();
        outVec = outVec.scl(eb.circle.radius);

        Vector2 offsetVec = VectorUtils.rotateVector(outVec.cpy(), -et.rotation).add(eb.offset);
        float baseRotation = offsetVec.angle() - 90f;

        final Entity plant = ((PooledEngine) getEngine()).createEntity();
        plant.add(TransformComponent.create()
                .setPosition(outVec.x, outVec.y, Z.plant)
                .setRotation(et.rotation + baseRotation));
        plant.add(TextureComponent.create());
        plant.add(StateComponent.create()
                .set("DEFAULT")
                .setLooping(false));
        float rnd = r.nextFloat();
        Array<TextureAtlas.AtlasRegion> trees = rnd < 0.3f ?
                Assets.getGreenTreeFrames() :
                rnd < 0.6f ?
                        Assets.getPinkTreeFrames() :
                        Assets.getPineTreeFrames();
        plant.add(AnimationComponent.create()
                .addAnimation("DEFAULT", new Animation(1f / 9f, trees)));
        plant.add(FollowerComponent.create()
                .setOffset(offsetVec.x, offsetVec.y)
                .setTarget(enemy)
                .setBaseRotation(baseRotation));

        enemy.componentRemoved.add(new Listener<Entity>() {
            @Override
            public void receive(Signal<Entity> signal, Entity object) {
                getEngine().removeEntity(plant);
            }
        });

        getEngine().addEntity(plant);

    }
}

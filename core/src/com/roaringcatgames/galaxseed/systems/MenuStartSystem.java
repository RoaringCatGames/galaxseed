package com.roaringcatgames.galaxseed.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.VectorUtils;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.galaxseed.Animations;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.components.*;
import com.roaringcatgames.galaxseed.values.Z;

import java.util.Random;

/**
 * System to handle menu starting
 */
public class MenuStartSystem extends IteratingSystem{
    private Array<Entity> bullets = new Array<>();
    private Array<Entity> enemies = new Array<>();

    private ComponentMapper<BulletComponent> bm;

    private ComponentMapper<CircleBoundsComponent> cm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<AnimationComponent> am;
    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<DamageComponent> dm;

    Random r = new Random();

    public MenuStartSystem(){
        super(Family.one(BulletComponent.class, MenuItemComponent.class, CircleBoundsComponent.class).get());
        bm = ComponentMapper.getFor(BulletComponent.class);
        cm = ComponentMapper.getFor(CircleBoundsComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
        dm = ComponentMapper.getFor(DamageComponent.class);

    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for(Entity bullet:bullets){
            CircleBoundsComponent bb = cm.get(bullet);
            for(Entity enemy:enemies){
                if(cm.has(enemy)){
                    CircleBoundsComponent cb = cm.get(enemy);
                    if (cb.circle.overlaps(bb.circle)) {
                        processCollision(bullet, enemy);
                    }
                }
            }
        }

        for(Entity enemy:enemies){
            MenuItemComponent mic = Mappers.menuItem.get(enemy);
            if(mic.isFilled){
                float xSpeed = K2MathUtil.getRandomInRange(8f, 15f) * (r.nextFloat() > 0.5f ? -1f : 1f);
                float ySpeed = K2MathUtil.getRandomInRange(8f, 15f) * (r.nextFloat() > 0.5f ? -1f : 1f);
                Vector2 speed = new Vector2(xSpeed, ySpeed);
                float angle = (speed.angle() - 90f) + 180f;
                enemy.add(VelocityComponent.create(getEngine())
                        .setSpeed(xSpeed, ySpeed));
                enemy.add(ParticleEmitterComponent.create((PooledEngine)getEngine())
                        .setParticleImages(Assets.getLeafFrames())
                        .setDuration(10f)
                        .setShouldLoop(true)
                        .setSpeed(15f, 20f)
                        .setZIndex(Z.leaves)
                        .setAngleRange(angle -15f, angle + 15f)
                        .setSpawnRate(17f)
                        .setParticleLifespans(0.3f, 0.5f));
                enemy.remove(MenuItemComponent.class);
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
            } else if(Mappers.menuItem.has(entity)){
                enemies.add(entity);
            }
        }
    }


    private Vector2 bulletPos = new Vector2();
    private Vector2 enemyPos = new Vector2();
    private void processCollision(Entity bullet, Entity enemy){

        HealthComponent enemyHealth = hm.get(enemy);
        if(enemyHealth.health <= 0f){
            return;
        }

        //enemyHealth.health -= dm.get(bullet).dps;
        CircleBoundsComponent bulletCircle = K2ComponentMappers.circleBounds.get(bullet);
        EnemyDamageUtil.attachPlant((PooledEngine) getEngine(), bulletCircle.circle, enemy, false);
        getEngine().removeEntity(bullet);
        if(enemyHealth.health <= 0f) {
            Mappers.menuItem.get(enemy).isFilled = true;
        }

    }

    private void attachPlant(Entity bullet, Entity enemy) {
        PooledEngine engine = (PooledEngine)getEngine();

        CircleBoundsComponent bb = cm.get(bullet);
        CircleBoundsComponent eb = cm.get(enemy);
        TransformComponent et = tm.get(enemy);
        bulletPos.set(bb.circle.x, bb.circle.y);
        enemyPos.set(eb.circle.x, eb.circle.y);

        Vector2 outVec = bulletPos.sub(enemyPos).nor();
        outVec = outVec.scl(eb.circle.radius);

        Vector2 offsetVec = VectorUtils.rotateVector(outVec.cpy(), -et.rotation).add(eb.offset);
        float baseRotation = offsetVec.angle() - 90f;

        Entity plant = engine.createEntity();
        plant.add(TransformComponent.create(engine)
                .setPosition(outVec.x, outVec.y, Z.plant)
                .setRotation(et.rotation + baseRotation));
        plant.add(TextureComponent.create(engine));
        plant.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(false));
        float rnd = r.nextFloat();
        Animation ani = rnd < 0.3f ? Animations.getGreenTree() :
                        rnd < 0.6f ? Animations.getPinkTree() :
                                     Animations.getPineTree();
        plant.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", ani));
        plant.add(FollowerComponent.create(engine)
                .setOffset(offsetVec.x, offsetVec.y)
                .setTarget(enemy)
                .setBaseRotation(baseRotation));

        plant.add(ParticleEmitterComponent.create(engine)
                .setParticleImages(Assets.getLeafFrames())
                .setParticleLifespans(0.1f, 0.2f)
                .setSpawnRate(1.6f)
                .setAngleRange(0f, 360f)
                .setSpeed(2f, 3f)
                .setShouldFade(true)
                .setZIndex(Z.leaves)
                .setDuration(0.3f));

        getEngine().addEntity(plant);
    }
}

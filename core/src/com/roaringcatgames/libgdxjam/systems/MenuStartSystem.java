package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.VectorUtils;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.*;
import com.roaringcatgames.libgdxjam.values.Z;

import java.util.Random;

/**
 * Created by barry on 3/3/16 @ 11:43 PM.
 */
public class MenuStartSystem extends IteratingSystem{
    private Array<Entity> bullets = new Array<>();
    private Array<Entity> enemies = new Array<>();

    private ComponentMapper<BulletComponent> bm;

    private ComponentMapper<CircleBoundsComponent> cm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<AnimationComponent> am;

    Random r = new Random();

    public MenuStartSystem(){
        super(Family.one(BulletComponent.class, MenuItemComponent.class, CircleBoundsComponent.class).get());
        bm = ComponentMapper.getFor(BulletComponent.class);
        cm = ComponentMapper.getFor(CircleBoundsComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);

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

        AnimationComponent ani = am.get(enemy);
        ani.setPaused(false);
        attachPlant(bullet, enemy);
        enemy.add(FadingComponent.create()
            .setPercentPerSecond(50f));
        getEngine().removeEntity(bullet);
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

        Entity plant = ((PooledEngine) getEngine()).createEntity();
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

        getEngine().addEntity(plant);

    }
}

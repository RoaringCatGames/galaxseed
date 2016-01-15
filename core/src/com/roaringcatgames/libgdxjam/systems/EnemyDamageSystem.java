package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.libgdxjam.components.BulletComponent;
import com.roaringcatgames.libgdxjam.components.CircleBoundsComponent;
import com.roaringcatgames.libgdxjam.components.EnemyComponent;

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

    public EnemyDamageSystem(){
        super(Family.one(BulletComponent.class, EnemyComponent.class).get());
        bm = ComponentMapper.getFor(BulletComponent.class);
        em = ComponentMapper.getFor(EnemyComponent.class);
        bndm = ComponentMapper.getFor(BoundsComponent.class);
        cm = ComponentMapper.getFor(CircleBoundsComponent.class);
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for(Entity bullet:bullets){
            BoundsComponent bb = bndm.get(bullet);
            for(Entity enemy:enemies){
                if(enemy.isScheduledForRemoval()){
                    continue;
                }
                if(bndm.has(enemy)) {
                    BoundsComponent eb = bndm.get(enemy);
                    if (bb.bounds.overlaps(eb.bounds)) {
                        getEngine().removeEntity(bullet);
                        getEngine().removeEntity(enemy);
                    }
                }else if(cm.has(enemy)){
                    CircleBoundsComponent cb = cm.get(enemy);
                    if (Intersector.overlaps(cb.circle, bb.bounds)) {
                        getEngine().removeEntity(bullet);
                        getEngine().removeEntity(enemy);
                    }
                }
            }
        }

        enemies.clear();
        bullets.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if(bm.has(entity)){
            bullets.add(entity);
        }else{
            enemies.add(entity);
        }
    }
}

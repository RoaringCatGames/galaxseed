package com.roaringcatgames.galaxseed.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.AnimationComponent;
import com.roaringcatgames.kitten2d.ashley.components.StateComponent;
import com.roaringcatgames.galaxseed.components.BulletComponent;

/**
 * Created by barry on 1/5/16 @ 8:46 PM.
 */
public class BulletSystem extends IteratingSystem {

    ComponentMapper<BulletComponent> bm;
    ComponentMapper<StateComponent> sm;
    ComponentMapper<AnimationComponent> am;

    Array<Entity> bullets;
    public BulletSystem(){
        super(Family.all(BulletComponent.class).get());

        this.bm = ComponentMapper.getFor(BulletComponent.class);
        this.sm = ComponentMapper.getFor(StateComponent.class);
        this.am = ComponentMapper.getFor(AnimationComponent.class);

        bullets = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for(Entity e:bullets){
            StateComponent sc = sm.get(e);
            Animation ani = am.get(e).animations.get(sc.get());
            if(ani != null && sc.get() == "DEFAULT" && ani.isAnimationFinished(sc.time)){
                sc.set("FLYING").setLooping(true);
            }
        }

        bullets.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bullets.add(entity);
    }
}

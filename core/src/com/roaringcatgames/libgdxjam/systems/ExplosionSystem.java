package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.roaringcatgames.kitten2d.ashley.components.AnimationComponent;
import com.roaringcatgames.kitten2d.ashley.components.StateComponent;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.components.ExplosionComponent;

/**
 * Created by barry on 3/25/16 @ 1:31 PM.
 */
public class ExplosionSystem extends IteratingSystem {

    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<AnimationComponent> am;

    public ExplosionSystem(){
        super(Family.all(ExplosionComponent.class, AnimationComponent.class, StateComponent.class).get());
        sm = ComponentMapper.getFor(StateComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StateComponent sc = sm.get(entity);
        AnimationComponent ac = am.get(entity);
        boolean hasAnimation = ac.animations.size > 0;
        Animation ani = ac.animations.get(sc.get());
        if(ani != null){
            if(App.isSlowed()){
                //scaledTime = time * slowScale;
                //time = scaledTime/slowScale;
                //unCountedTime = time - scaledTime
                sc.time += (deltaTime/App.SLOW_SCALE) - deltaTime;
            }
            if(ani.isAnimationFinished(sc.time)){
                getEngine().removeEntity(entity);
            }
        }else {
            Gdx.app.log("Explosion System", "No Animation for state: " + sc.get());
        }
    }
}

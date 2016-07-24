package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.CircleBoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.components.ShieldComponent;

/**
 * System to handle shields
 */
public class ShieldSystem extends IteratingSystem {

    public ShieldSystem(){
        super(Family.all(ShieldComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (K2ComponentMappers.circleBounds.has(entity)){
            CircleBoundsComponent cbc = K2ComponentMappers.circleBounds.get(entity);
            TransformComponent tc = K2ComponentMappers.transform.get(entity);
            if(cbc.circle.radius <= 0f && tc.scale.x <= 0f && tc.scale.y <= 0f){
                getEngine().removeEntity(entity);
            }
        }
    }
}

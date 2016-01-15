package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.components.CircleBoundsComponent;

/**
 * Created by barry on 1/14/16 @ 9:48 PM.
 */
public class CircleBoundsSystem extends IteratingSystem {

    private ComponentMapper<CircleBoundsComponent> cm;
    private ComponentMapper<TransformComponent> tm;

    public CircleBoundsSystem(){
        super(Family.all(CircleBoundsComponent.class, TransformComponent.class).get());

        this.cm = ComponentMapper.getFor(CircleBoundsComponent.class);
        this.tm = ComponentMapper.getFor(TransformComponent.class);
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent tc = tm.get(entity);
        CircleBoundsComponent cc = cm.get(entity);
        cc.circle.setPosition(tc.position.x, tc.position.y);
    }
}

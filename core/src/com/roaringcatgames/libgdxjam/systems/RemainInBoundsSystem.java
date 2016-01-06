package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.components.RemainInBoundsComponent;

/**
 * Created by barry on 1/5/16 @ 7:23 PM.
 */
public class RemainInBoundsSystem extends IteratingSystem {

    private ComponentMapper<RemainInBoundsComponent> ribm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<BoundsComponent> bm;

    private Rectangle bounds;
    private Array<Entity> queue;

    public RemainInBoundsSystem(Vector2 minBounds, Vector2 maxBounds){
        super(Family.all(RemainInBoundsComponent.class, TransformComponent.class).get());

        this.ribm = ComponentMapper.getFor(RemainInBoundsComponent.class);
        this.tm = ComponentMapper.getFor(TransformComponent.class);
        this.bm = ComponentMapper.getFor(BoundsComponent.class);

        this.bounds = new Rectangle(minBounds.x, minBounds.y, (maxBounds.x-minBounds.x), (maxBounds.y-minBounds.y));
        this.queue = new Array<>();

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for(Entity e:queue){
            RemainInBoundsComponent rc = ribm.get(e);
            TransformComponent tc = tm.get(e);

            float left = bounds.x;
            float bottom = bounds.y;
            float right = bounds.x + bounds.width;
            float top = bounds.y + bounds.height;

            switch(rc.mode){
                case CONTAINED:
                    if(!bm.has(e)){
                        Gdx.app.log("RemainInBoundsSystem", "CONTAINED entity does not have Bounds cannot calculate");
                        break;
                    }
                    BoundsComponent bc = bm.get(e);
                    float xAdjust = 0f;
                    if(bc.bounds.x < bounds.x){
                        xAdjust = bounds.x - bc.bounds.x;
                    }else if(bc.bounds.x > (bounds.x + bounds.width)){
                       // xAdjust = -(bc.bounds.x
                    }
                    break;
                case CENTER:

                    break;
                case EDGE:

                    break;
                default:
                    break;
            }
        }
        queue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        queue.add(entity);
    }
}

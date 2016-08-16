package com.roaringcatgames.galaxseed.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.CircleBoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.galaxseed.components.WhenOffScreenComponent;

/**
 * Created by barry on 1/3/16 @ 1:35 PM.
 */
public class CleanUpSystem extends IteratingSystem {

    public Array<Entity> queue;

    private ComponentMapper<WhenOffScreenComponent> wosm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<CircleBoundsComponent> cm;
    private Entity bounds;
    private Rectangle targetBounds;

    public CleanUpSystem(Vector2 minBounds, Vector2 maxBounds){
        super(Family.all(WhenOffScreenComponent.class)
                .one(TransformComponent.class, BoundsComponent.class).get());

        wosm = ComponentMapper.getFor(WhenOffScreenComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        bm = ComponentMapper.getFor(BoundsComponent.class);
        cm = ComponentMapper.getFor(CircleBoundsComponent.class);

        targetBounds = new Rectangle(
                minBounds.x,
                minBounds.y,
                maxBounds.x - minBounds.x,
                maxBounds.y - minBounds.y);

        queue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(bounds == null){
            PooledEngine engine = (PooledEngine)getEngine();
            bounds = engine.createEntity();
            bounds.add(BoundsComponent.create(engine)
                .setBounds(targetBounds.x, targetBounds.y, targetBounds.width, targetBounds.height));
            bounds.add(TransformComponent.create(engine)
                .setPosition(targetBounds.x + (targetBounds.width / 2f),
                             targetBounds.y + (targetBounds.height / 2f),
                             0f));
            getEngine().addEntity(bounds);
        }


        BoundsComponent screenBounds = bm.get(bounds);

        for(Entity e:queue){
            WhenOffScreenComponent wc = wosm.get(e);
            //Allows things to be spawned offscreen, and cleaned up
            // only after EXITING the bounds.
            if(!wc.hasBeenOnScreen){
                if (bm.has(e)) {
                    BoundsComponent bc = bm.get(e);
                    if (bc.bounds.overlaps(screenBounds.bounds)) {
                        wc.setHasBeenOnScreen(true);
                    }
                } else if(cm.has(e)) {
                    CircleBoundsComponent cc = cm.get(e);
                    if (Intersector.overlaps(cc.circle, screenBounds.bounds)){
                        wc.setHasBeenOnScreen(true);
                    }
                }else{
                    TransformComponent tc = tm.get(e);
                    if (screenBounds.bounds.contains(tc.position.x, tc.position.y)) {
                        wc.setHasBeenOnScreen(true);
                    }
                }
            }else {
                if (bm.has(e)) {
                    BoundsComponent bc = bm.get(e);
                    if (!bc.bounds.overlaps(screenBounds.bounds)) {
                        getEngine().removeEntity(e);
                    }
                }else if(cm.has(e)) {
                    CircleBoundsComponent cc = cm.get(e);
                    if (!Intersector.overlaps(cc.circle, screenBounds.bounds)){
                        getEngine().removeEntity(e);
                    }
                } else {
                    TransformComponent tc = tm.get(e);
                    if (!screenBounds.bounds.contains(tc.position.x, tc.position.y)) {
                        getEngine().removeEntity(e);
                    }
                }
            }

        }
        queue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        queue.add(entity);
    }
}

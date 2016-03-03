package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.VectorUtils;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.components.EnemyComponent;
import com.roaringcatgames.libgdxjam.components.FollowerComponent;

/**
 * Created by barry on 1/6/16 @ 7:47 PM.
 */
public class FollowerSystem extends IteratingSystem {

    ComponentMapper<FollowerComponent> fm;
    ComponentMapper<TransformComponent> tm;

    Array<Entity> queue;

    public FollowerSystem(){
        super(Family.all(FollowerComponent.class).get());
        this.queue = new Array<>();
        fm = ComponentMapper.getFor(FollowerComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    EntityListener el;
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        final Engine eg = engine;
        if(el == null){
            el = new EntityListener() {
                private ComponentMapper<FollowerComponent> fm = ComponentMapper.getFor(FollowerComponent.class);

                public void entityAdded(Entity entity) {

                }

                @Override
                public void entityRemoved(Entity entity) {
                    for (Entity follower : eg.getEntitiesFor(Family.all(FollowerComponent.class).get())) {
                        FollowerComponent fc = fm.get(follower);
                        if (fc.target == entity) {
                            fc.target = null;
                            follower.removeAll();
                            eg.removeEntity(follower);
                        }
                    }

                }
            };
        }

        engine.addEntityListener(Family.all(EnemyComponent.class).get(), el);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        if(el != null) {
            engine.removeEntityListener(el);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for(Entity e:queue){
            FollowerComponent fc = fm.get(e);
            if(fc.target != null) {
                TransformComponent targetPos = tm.get(fc.target);
                if (targetPos != null) {
                    switch (fc.followMode) {
                        case STICKY:
                            TransformComponent tc = tm.get(e);
                            Vector2 offset = VectorUtils.rotateVector(fc.offset, targetPos.rotation);
                            tc.position.set(targetPos.position.x + offset.x,
                                            targetPos.position.y + offset.y,
                                            tc.position.z);
                            tc.setRotation(fc.baseRotation + targetPos.rotation);
                            break;
                        case MOVETO:

                            break;
                        default:
                            break;
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

package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.HealthComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.components.HealthPackComponent;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;

/**
 * Created by barry on 4/3/16 @ 6:10 PM.
 */
public class HealthPackSystem extends IteratingSystem {

    private Entity player;
    private Array<Entity> packs = new Array<>();

    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<HealthPackComponent> hpm;

    public HealthPackSystem(){
        super(Family.all(BoundsComponent.class)
                .one(PlayerComponent.class, HealthPackComponent.class).get());
        hm = ComponentMapper.getFor(HealthComponent.class);
        bm = ComponentMapper.getFor(BoundsComponent.class);
        hpm = ComponentMapper.getFor(HealthPackComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(player != null){
            BoundsComponent playerBounds = bm.get(player);
            HealthComponent playerHealth = hm.get(player);
            for(Entity pack:packs){
                BoundsComponent packBounds = bm.get(pack);
                if(playerBounds.bounds.overlaps(packBounds.bounds)){
                    HealthPackComponent packPack = hpm.get(pack);
                    playerHealth.health += packPack.health;
                    getEngine().removeEntity(pack);
                }
            }
        }

        packs.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(hpm.has(entity)){
            packs.add(entity);
        }else{
            player = entity;
        }

    }
}

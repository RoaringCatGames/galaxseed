package com.roaringcatgames.galaxseed.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.components.*;
import com.roaringcatgames.galaxseed.values.Damage;
import com.roaringcatgames.galaxseed.values.Health;
import com.roaringcatgames.galaxseed.values.Z;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.*;

/**
 * Created by barry on 9/6/16 @ 10:10 PM.
 */
public class DemoSystem extends IteratingSystem {

    private Entity player;
    private Array<Entity> demoBlocks = new Array<>();

    private boolean isInitialized = false;

    public DemoSystem(){
        super(Family.one(DemoBlockComponent.class, PlayerComponent.class).get());
    }

    private void init(){
        PooledEngine engine = (PooledEngine)getEngine();
        Entity demoBrick = engine.createEntity();
        demoBrick.add(KinematicComponent.create(engine));
        demoBrick.add(TransformComponent.create(engine)
                .setPosition(App.W / 2f, 235f, Z.enemy)
                .setScale(1f, 1f));
        demoBrick.add(DemoBlockComponent.create(engine));
//        demoBrick.add(spawner);
        demoBrick.add(BoundsComponent.create(engine)
                .setBounds(0f, 0f, App.W, 11.5f));
        demoBrick.add(TextureComponent.create(engine)
                .setRegion(Assets.getDemoBrick()));
        demoBrick.add(VelocityComponent.create(engine)
                .setSpeed(0f, -1.5f));

        engine.addEntity(demoBrick);
        isInitialized = true;
    }

    @Override
    public void update(float deltaTime) {
        if(!isInitialized){
            init();
        }

        super.update(deltaTime);


        BoundsComponent pbc = K2ComponentMappers.bounds.get(player);
        if(pbc != null) {
            for (Entity demo : demoBlocks) {
                BoundsComponent bc = K2ComponentMappers.bounds.get(demo);
                if (bc.bounds.overlaps((pbc.bounds))) {
                    App.toggleWeapon(WeaponType.GUN_SEEDS, false);
                    App.toggleWeapon(WeaponType.HELICOPTER_SEEDS, false);
                    App.toggleWeapon(WeaponType.POLLEN_AURA, false);

                    HealthComponent ph = K2ComponentMappers.health.get(player);
                    ph.health = 0f;
                    break;
                }
            }
        }

        demoBlocks.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if(!Mappers.player.has(entity)) {
            demoBlocks.add(entity);
        }else{
            player = entity;
        }
    }
}

package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.StateComponent;
import com.roaringcatgames.libgdxjam.components.WeaponDecorationComponent;
import com.roaringcatgames.libgdxjam.components.Mappers;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;

/**
 * System to handle the Decorations on fully leveled Guns
 */
public class WeaponDecorationSystem extends IteratingSystem {

    private Entity player;
    private Array<Entity> decorations = new Array<>();

    public WeaponDecorationSystem(){
        super(Family.one(PlayerComponent.class, WeaponDecorationComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(player != null && K2ComponentMappers.state.has(player)){
            StateComponent playerState = K2ComponentMappers.state.get(player);

            boolean isFiring = !"DEFAULT".equals(playerState.get());
            boolean isDead = "DEAD".equals(playerState.get());

            for (Entity deco : decorations) {
                if(!isDead){
                    StateComponent decoState = K2ComponentMappers.state.get(deco);
                    boolean isDecoDefault = "DEFAULT".equals(decoState.get());

                    if (isFiring && isDecoDefault) {
                        decoState.set("FIRING");
                    } else if (!isFiring && !isDecoDefault) {
                        decoState.set("DEFAULT");
                    }

                    if(K2ComponentMappers.particleEmitter.has(deco)){
                        K2ComponentMappers.particleEmitter.get(deco).setPaused(!isFiring);
                    }
                }else{
                    getEngine().removeEntity(deco);
                }
            }

        }

        this.decorations.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(Mappers.player.has(entity)){
            this.player = entity;
        }else{
            this.decorations.add(entity);
        }
    }
}

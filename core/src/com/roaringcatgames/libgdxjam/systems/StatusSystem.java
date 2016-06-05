package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.roaringcatgames.libgdxjam.components.Mappers;
import com.roaringcatgames.libgdxjam.components.StatusComponent;

/**
 *
 */
public class StatusSystem extends IteratingSystem {

    private static final float HELI_STUN_LENGTH = 0.2f; //200 millis

    public StatusSystem(){
        super(Family.all(StatusComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StatusComponent status = Mappers.status.get(entity);
        status.setTimeInStatus(status.timeInStatus+deltaTime);

        switch(status.status){
            case HELI_STUNNED:
                if(status.timeInStatus > HELI_STUN_LENGTH){
                    status.setTimeInStatus(0f);
                    status.setStatus(StatusComponent.EntityStatus.NONE);
                }
                break;
        }
    }
}

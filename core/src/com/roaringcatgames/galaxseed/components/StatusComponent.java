package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Holds data for status on an entity
 */
public class StatusComponent implements Component, Pool.Poolable {

    public EntityStatus status = EntityStatus.NONE;
    public float timeInStatus = 0f;

    public static StatusComponent create(Engine engine){
        return ((PooledEngine)engine).createComponent(StatusComponent.class);
    }

    public StatusComponent setStatus(EntityStatus status){
        this.status = status;
        return this;
    }

    public StatusComponent setTimeInStatus(float time){
        this.timeInStatus = time;
        return this;
    }

    @Override
    public void reset() {
        status = EntityStatus.NONE;
        timeInStatus = 0f;
    }

    public enum EntityStatus{
        NONE, HELI_STUNNED
    }
}

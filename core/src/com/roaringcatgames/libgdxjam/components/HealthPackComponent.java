package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 4/3/16 @ 5:39 PM.
 */
public class HealthPackComponent implements Component, Pool.Poolable {

    public float health = 1f;
    public boolean isInstant = true;
    public float healthPerSecond = 1f;

    public static HealthPackComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(HealthPackComponent.class);
        }else{
            return new HealthPackComponent();
        }
    }

    public HealthPackComponent setHealth(float h){
        this.health = h;
        return this;
    }
    public HealthPackComponent setInstant(boolean isInstant){
        this.isInstant = isInstant;
        return this;
    }
    public HealthPackComponent setHealthPerSecond(float hps){
        this.healthPerSecond = hps;
        return this;
    }

    @Override
    public void reset() {
        this.health = 1f;
        this.isInstant = true;
        this.healthPerSecond = 1f;
    }
}

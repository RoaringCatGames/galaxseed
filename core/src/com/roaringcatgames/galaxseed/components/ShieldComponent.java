package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Identifies a shield
 */
public class ShieldComponent implements Component, Pool.Poolable {

    public float shieldTime = 0f;
    public float shieldMaxLife = 3f;

    public static ShieldComponent create(Engine engine) {
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(ShieldComponent.class);
        }else{
            return new ShieldComponent();
        }
    }

    public ShieldComponent setShieldTime(float time) {
        this.shieldTime = time;
        return this;
    }

    public ShieldComponent addShieldTime(float delta) {
        this.shieldTime += delta;
        return this;
    }

    public ShieldComponent setMaxLife(float max) {
        this.shieldMaxLife = max;
        return this;
    }

    @Override
    public void reset() {
        shieldTime = 0f;
        shieldMaxLife = 0f;
    }
}

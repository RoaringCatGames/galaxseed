package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Stores our PollenAuraComponent
 */
public class PollenAuraComponent implements Component, Pool.Poolable {

    public static PollenAuraComponent create(PooledEngine engine){
        return engine.createComponent(PollenAuraComponent.class);
    }
    @Override
    public void reset() {

    }
}

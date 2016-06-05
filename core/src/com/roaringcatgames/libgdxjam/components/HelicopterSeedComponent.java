package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Signifies a helicopter seed
 */
public class HelicopterSeedComponent implements Component, Pool.Poolable {
    public static HelicopterSeedComponent create(Engine engine){
        return ((PooledEngine)engine).createComponent(HelicopterSeedComponent.class);
    }

    @Override
    public void reset() {

    }
}

package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Signifies a Health Leaf
 */
public class HealthLeafComponent implements Component, Pool.Poolable{
    public static HealthLeafComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(HealthLeafComponent.class);
        }else{
            return new HealthLeafComponent();
        }
    }

    @Override
    public void reset() {

    }
}

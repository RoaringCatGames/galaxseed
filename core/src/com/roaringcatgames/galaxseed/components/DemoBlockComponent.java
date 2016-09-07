package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Dummy component to identify Demo BLocks
 */
public class DemoBlockComponent implements Component, Pool.Poolable {
    public static DemoBlockComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(DemoBlockComponent.class);
        }else{
            return new DemoBlockComponent();
        }
    }
    @Override
    public void reset() {

    }
}

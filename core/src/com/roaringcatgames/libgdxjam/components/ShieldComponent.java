package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Identifies a shield
 */
public class ShieldComponent implements Component, Pool.Poolable {

    public static ShieldComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(ShieldComponent.class);
        }else{
            return new ShieldComponent();
        }
    }

    @Override
    public void reset() {

    }
}

package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Component to mark items as Decorations
 */
public class DecorationComponent implements Component, Pool.Poolable {

    public static DecorationComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(DecorationComponent.class);
        }else{
            return new DecorationComponent();
        }
    }
    @Override
    public void reset() {

    }
}

package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 1/5/16 @ 7:25 PM.
 */
public class RemainInBoundsComponent implements Component, Pool.Poolable {

    public BoundMode mode = BoundMode.CONTAINED;

    public static RemainInBoundsComponent create(PooledEngine engine){
        return engine.createComponent(RemainInBoundsComponent.class);
    }

    public RemainInBoundsComponent setMode(BoundMode m){
        this.mode = m;
        return this;
    }

    @Override
    public void reset() {
        this.mode = BoundMode.CONTAINED;
    }
}

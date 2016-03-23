package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 1/3/16 @ 1:25 AM.
 */
public class BulletComponent implements Component, Pool.Poolable {

    public static BulletComponent create(PooledEngine engine){
        return engine.createComponent(BulletComponent.class);
    }

    @Override
    public void reset() {
    }
}

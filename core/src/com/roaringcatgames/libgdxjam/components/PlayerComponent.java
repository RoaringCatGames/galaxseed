package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 12/29/15 @ 8:11 PM.
 */
public class PlayerComponent implements Component,Pool.Poolable {

    public static PlayerComponent create(PooledEngine engine){
        return engine.createComponent(PlayerComponent.class);
    }

    @Override
    public void reset() {

    }
}

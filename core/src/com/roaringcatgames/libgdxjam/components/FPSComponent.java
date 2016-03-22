package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;
import com.roaringcatgames.libgdxjam.systems.FPSSystem;

/**
 * Created by barry on 3/22/16 @ 5:56 PM.
 */
public class FPSComponent implements Component, Pool.Poolable {


    @Override
    public void reset() {

    }

    public static FPSComponent create(PooledEngine e){
        return e.createComponent(FPSComponent.class);
    }
}

package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Component to mark items as Decorations
 */
public class WeaponDecorationComponent implements Component, Pool.Poolable {

    public static WeaponDecorationComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(WeaponDecorationComponent.class);
        }else{
            return new WeaponDecorationComponent();
        }
    }
    @Override
    public void reset() {

    }
}

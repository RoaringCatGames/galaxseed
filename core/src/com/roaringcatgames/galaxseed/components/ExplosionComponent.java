package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;

/**
 * Created by barry on 3/25/16 @ 1:18 PM.
 */
public class ExplosionComponent implements Component {

    public static ExplosionComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(ExplosionComponent.class);
        }else{
            return new ExplosionComponent();
        }
    }
}

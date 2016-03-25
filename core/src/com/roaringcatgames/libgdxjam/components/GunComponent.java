package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;

/**
 * Created by barry on 3/25/16 @ 3:43 PM.
 */
public class GunComponent implements Component {

    public static GunComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(GunComponent.class);
        }else{
            return new GunComponent();
        }
    }
}

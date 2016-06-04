package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 3/25/16 @ 3:43 PM.
 */
public class GunComponent implements Component, Pool.Poolable{

    public float timeBetweenShots = 1f;
    public float lastFireTime = 0f;

    public static GunComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(GunComponent.class);
        }else{
            return new GunComponent();
        }
    }

    public GunComponent setTimeBetweenShots(float rate){
        this.timeBetweenShots = rate;
        return this;
    }

    public GunComponent setLastFireTime(float fireTime){
        this.lastFireTime = fireTime;
        return this;
    }

    @Override
    public void reset(){
        this.timeBetweenShots = 1f;
        this.lastFireTime = 0f;
    }

}

package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Defines the properties of a gun.
 */
public class GunComponent implements Component, Pool.Poolable{

    public float timeElapsed = 0f;
    public float timeBetweenShots = 1f;
    public float lastFireTime = 0f;
    public float bulletSpeed = 20f;

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

    public GunComponent setBulletSpeed(float speed){
        this.bulletSpeed = speed;
        return this;
    }

    @Override
    public void reset(){
        this.timeBetweenShots = 1f;
        this.lastFireTime = 0f;
        this.bulletSpeed = 20f;
        this.timeElapsed = 0f;
    }
}

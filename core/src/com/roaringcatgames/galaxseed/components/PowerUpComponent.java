package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Component to hold power up data
 */
public class PowerUpComponent implements Component, Pool.Poolable {
    public PowerUpType powerUpType = PowerUpType.UPGRADE;

    public static PowerUpComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(PowerUpComponent.class);
        }else{
            return new PowerUpComponent();
        }
    }

    public PowerUpComponent setPowerUpType(PowerUpType put){
        this.powerUpType = put;
        return this;
    }

    @Override
    public void reset() {
         powerUpType = PowerUpType.UPGRADE;
    }

    public enum PowerUpType{ UPGRADE, SPECIAL, ONE_UP }
}

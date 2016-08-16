package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Component just to track our player
 */
public class PlayerComponent implements Component,Pool.Poolable {

    public WeaponType weaponType = WeaponType.GUN_SEEDS;
    public WeaponLevel weaponLevel = WeaponLevel.LEVEL_1;

    public static PlayerComponent create(PooledEngine engine){
        return engine.createComponent(PlayerComponent.class);
    }

    public PlayerComponent setWeaponType(WeaponType wt){
        this.weaponType = wt;
        return this;
    }

    public PlayerComponent setWeaponLevel(WeaponLevel wl){
        this.weaponLevel = wl;
        return this;
    }

    public void reset() {
        weaponType = WeaponType.GUN_SEEDS;
        weaponLevel = WeaponLevel.LEVEL_1;
    }
}

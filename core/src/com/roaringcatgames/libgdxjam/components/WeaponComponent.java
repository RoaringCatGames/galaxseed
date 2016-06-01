package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Defines the data items for a WeaponComponent
 */
public class WeaponComponent implements Component, Pool.Poolable {

    public WeaponType weaponType = WeaponType.GUN_SEEDS;
    public WeaponLevel weaponLevel = WeaponLevel.LEVEL_1;

    public static WeaponComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(WeaponComponent.class);
        }else{
            return new WeaponComponent();
        }
    }

    public WeaponComponent setWeaponType(WeaponType wt){
        this.weaponType = wt;
        return this;
    }

    public WeaponComponent setWeaponLevel(WeaponLevel wl){
        this.weaponLevel = wl;
        return this;
    }

    public void reset() {
        weaponType = WeaponType.GUN_SEEDS;
        weaponLevel = WeaponLevel.LEVEL_1;
    }
}

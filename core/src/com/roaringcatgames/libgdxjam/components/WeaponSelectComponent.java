package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Component to define Weapon Select component.
 */
public class WeaponSelectComponent implements Component, Pool.Poolable {

    public WeaponType weaponType = WeaponType.UNSELECTED;
    public boolean isRepresentingLevel = false;

    public static WeaponSelectComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(WeaponSelectComponent.class);
        }else{
            return new WeaponSelectComponent();
        }
    }

    public WeaponSelectComponent setWeaponType(WeaponType wt){
        this.weaponType = wt;
        return this;
    }

    public WeaponSelectComponent setRepresentingLevel(boolean isReppin){
        this.isRepresentingLevel = isReppin;
        return this;
    }

    @Override
    public void reset() {
        weaponType = WeaponType.UNSELECTED;
        isRepresentingLevel = false;
    }
}

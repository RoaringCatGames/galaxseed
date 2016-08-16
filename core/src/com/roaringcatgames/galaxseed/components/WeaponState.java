package com.roaringcatgames.galaxseed.components;

/**
 * Wrapper object to hold state of a weapon
 */
public class WeaponState {

    public WeaponLevel level;
    public boolean isDisabled;

    public WeaponState(WeaponLevel level, boolean isDisabled){
        this.level = level;
        this.isDisabled = isDisabled;
    }
}

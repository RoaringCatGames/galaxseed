package com.roaringcatgames.galaxseed;

import com.roaringcatgames.galaxseed.values.Volume;

/**
 * Static utility to easily play sound effects
 */
public class Sfx {

    public static void playClickNoise(){
        if(PrefsUtil.areSfxEnabled()){
            Assets.getClickSfx().play(Volume.CLICK_SFX);
        }
    }
    public static void playSelectNoise(){
        if(PrefsUtil.areSfxEnabled()){
            Assets.getSelectSfx().play(Volume.SELECT_SFX);
        }
    }

    public static void playUpgradeSound(){
        if(PrefsUtil.areSfxEnabled()){
            Assets.getUpgradeSfx().play(Volume.UPGRADE_SFX);
        }
    }

    public static void playPlanetBorn(int position){
        if(PrefsUtil.areSfxEnabled()){
            Assets.getPlanetBornSfx(position%3).play(Volume.PLANET_BORN_SFX);
        }
    }

    public static void playSpeedUp(){
        if(PrefsUtil.areSfxEnabled()){
            Assets.getSpeedUpSfx().play(Volume.SPEED_UP_SFX);
        }
    }

    public static void playSlowDown(){
        if(PrefsUtil.areSfxEnabled()){
            Assets.getSlowDownSfx().play(Volume.SLOW_DOWN_SFX);
        }
    }
}

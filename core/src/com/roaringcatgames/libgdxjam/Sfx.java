package com.roaringcatgames.libgdxjam;

import com.roaringcatgames.libgdxjam.values.Volume;

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
}

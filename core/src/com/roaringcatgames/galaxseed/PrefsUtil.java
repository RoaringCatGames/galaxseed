package com.roaringcatgames.galaxseed;

/**
 * Easy Utility to wrap determining preferences on/off
 */
public class PrefsUtil {

    public static final String MUSIC_KEY = "music";
    public static final String SFX_KEY = "sfx";
    public static final String VIBRA_KEY = "vibration";
    public static final String CTRL_KEY = "controls";
    public static final String GAME_SERVICES = "gameservices";

    public static boolean hasSignedOn() {
        return App.getPrefs().getStoredInt(PrefsUtil.GAME_SERVICES) == 1;
    }

    public static boolean areSfxEnabled(){
        return App.getPrefs().getStoredString("sfx", "On").equals("On");
    }

    public static boolean isVibrationOn(){
        return App.getPrefs().getStoredString("vibration", "On").equals("On");
    }

    public static boolean isMusicOn(){
        return App.getPrefs().getStoredString("music", "On").equals("On");
    }

    public static boolean isControlBoostOn(){
        return App.getPrefs().getStoredString("controls", "On").equals("On");
    }
}

package com.roaringcatgames.libgdxjam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.roaringcatgames.libgdxjam.components.WeaponLevel;
import com.roaringcatgames.libgdxjam.components.WeaponType;
import com.roaringcatgames.libgdxjam.values.GameState;

/**
 * The Global State items that we're okay with tracking in one spot
 */
public class App {

    public static final float MAX_DELTA_TICK = 1f/30f;
    public static final float PPM = 32.0f;
    public static final float W = 20f;
    public static final float H = 30f;

    public static float PAUSE_LENGTH = 1f;
    public static float SLOW_SCALE = 0.1f; //10% speed

    public static final Game Initialize(){
        game = new LifeInSpace();
        return game;
    }
    public static LifeInSpace game;

    //Initial position
    //Bad form with a public global, but whatever here
    public static Vector2 playerLastPosition = new Vector2(W/2f, 7f);

    private static float timeSpentSlow = 0f;


    private static boolean isSlowed = false;


    private static GameState state = GameState.MENU;


    public static Vector2 getPlayerLastPosition(){
        return playerLastPosition;
    }


    private static ObjectMap<WeaponType, WeaponLevel> currentWeaponLevels = new ObjectMap<>();

    static {
        currentWeaponLevels.put(WeaponType.GUN_SEEDS, WeaponLevel.LEVEL_1);
        currentWeaponLevels.put(WeaponType.POLLEN_AURA, WeaponLevel.LEVEL_1);
        currentWeaponLevels.put(WeaponType.HELICOPTER_SEEDS, WeaponLevel.LEVEL_1);
    }

    public static void setTimeSpentSlow(float tss){
        timeSpentSlow = tss;
    }
    public static float getTimeSpentSlow(){
        return timeSpentSlow;
    }
    public static void setSlowed(boolean newSlowed){
        isSlowed = newSlowed;
        if(isSlowed){
            timeSpentSlow = 0f;
        }
    }
    public static boolean isSlowed(){
        return isSlowed;
    }
    public static void setState(GameState newState){
        state = newState;
    }
    public static GameState getState(){
        return state;
    }

    public static WeaponLevel getCurrentWeaponLevel(WeaponType wt){
        return currentWeaponLevels.get(wt);
    }
    public static void setCurrentWeaponLevel(WeaponType wt, WeaponLevel lvl){
        currentWeaponLevels.put(wt, lvl);
    }
}

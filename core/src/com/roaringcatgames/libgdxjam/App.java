package com.roaringcatgames.libgdxjam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.libgdxjam.values.GameState;

/**
 * Created by barry on 12/22/15 @ 7:31 PM.
 */
public class App {

    public static final float MAX_DELTA_TICK = 1f/30f;
    public static final float PPM = 32.0f;
    public static final float W = 20f;
    public static final float H = 30f;

    public static float PAUSE_LENGTH = 1f;
    public static float SLOW_SCALE = 0.3f; //30% speed

    public static final Game Initialize(){
        game = new LifeInSpace();
        return game;
    }
    public static LifeInSpace game;

    public static Vector2 playerLastPosition = new Vector2();

    private static float timeSpentSlow = 0f;
    public static void setTimeSpentSlow(float tss){
        timeSpentSlow = tss;
    }
    public static float getTimeSpentSlow(){
        return timeSpentSlow;
    }

    private static boolean isSlowed = false;
    public static void setSlowed(boolean newSlowed){
        isSlowed = newSlowed;
        if(isSlowed){
            timeSpentSlow = 0f;
        }
    }
    public static boolean isSlowed(){
        return isSlowed;
    }

    private static GameState state = GameState.MENU;
    public static void setState(GameState newState){
        state = newState;
    }
    public static GameState getState(){
        return state;
    }


}

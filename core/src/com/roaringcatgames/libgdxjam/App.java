package com.roaringcatgames.libgdxjam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.roaringcatgames.kitten2d.gdx.helpers.IPreferenceManager;
import com.roaringcatgames.libgdxjam.components.WeaponLevel;
import com.roaringcatgames.libgdxjam.components.WeaponState;
import com.roaringcatgames.libgdxjam.components.WeaponType;
import com.roaringcatgames.libgdxjam.values.GameState;

/**
 * The Global State items that we're okay with tracking in one spot
 */
public class App {

    public static final float MAX_DELTA_TICK = 1f/30f;
    public static final float PPM = 32.0f;
    public static float W = 20f;
    public static float H = 30f;

    public static float SLOW_SCALE = 0.1f; //10% speed

    public static final Game Initialize(){
        game = new LifeInSpace();

        //Gdx.app.log("App", "Initializing");
        return game;
    }
    public static LifeInSpace game;


    public static void setAppWH(float x, float y){
        App.W = x;
        App.H = y;
    }

    //Initial position
    //Bad form with a public global, but whatever here
    public static Vector2 playerLastPosition = new Vector2(W/2f, 7f);

    private static float timeSpentSlow = 0f;


    private static boolean isSlowed = false;


    private static GameState state = GameState.MENU;

    private static ObjectMap<WeaponType, WeaponState> currentWeaponLevels = new ObjectMap<>();

    static {
        currentWeaponLevels.put(WeaponType.GUN_SEEDS, new WeaponState(WeaponLevel.LEVEL_1, false));
        currentWeaponLevels.put(WeaponType.POLLEN_AURA, new WeaponState(WeaponLevel.LEVEL_1, false));
        currentWeaponLevels.put(WeaponType.HELICOPTER_SEEDS, new WeaponState(WeaponLevel.LEVEL_1, false));
        currentWeaponLevels.put(WeaponType.UNSELECTED, new WeaponState(WeaponLevel.LEVEL_1, true));
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
        return currentWeaponLevels.get(wt).level;
    }
    public static void setCurrentWeaponLevel(WeaponType wt, WeaponLevel lvl){
        currentWeaponLevels.get(wt).level = lvl;
    }

    public static void toggleWeapon(WeaponType wt, boolean isEnabled){
        currentWeaponLevels.get(wt).isDisabled = !isEnabled;
    }

    public static boolean isWeaponEnabled(WeaponType wt){
        return !currentWeaponLevels.get(wt).isDisabled;
    }

    public static boolean hasAvailableWeapons(){
        boolean isAvailable = false;
        for(WeaponType key:currentWeaponLevels.keys()){
            if(!currentWeaponLevels.get(key).isDisabled){
                isAvailable = true;
            }
        }
        return isAvailable;
    }

    public static void resetWeapons() {
        resetWeapon(WeaponType.GUN_SEEDS);
        resetWeapon(WeaponType.POLLEN_AURA);
        resetWeapon(WeaponType.HELICOPTER_SEEDS);
    }
    private static void resetWeapon(WeaponType wt){
        WeaponState ws = currentWeaponLevels.get(wt);
        ws.level = WeaponLevel.LEVEL_1;
        ws.isDisabled = false;
    }

    public static IPreferenceManager getPrefs(){
        return game.getPreferenceManager();
    }

    public static boolean canPowerUp() {
        return hasRoomToUpgrade(WeaponType.GUN_SEEDS) ||
                hasRoomToUpgrade(WeaponType.HELICOPTER_SEEDS) ||
                hasRoomToUpgrade(WeaponType.POLLEN_AURA);
    }

    private static boolean hasRoomToUpgrade(WeaponType wt){
        return isWeaponEnabled(wt) && getCurrentWeaponLevel(wt) != WeaponLevel.LEVEL_4;
    }

    public static float getTotalHeight(){
        return game.getViewport().getWorldHeight();
    }

    public static float getTotalWeight(){
        return game.getViewport().getWorldWidth();
    }


    private static Cursor activeCursor;
    private static Cursor hiddenCursor;
    public static void setActiveCursor(Cursor c){
        activeCursor = c;
    }
    public static void setHiddenCursor(Cursor c){
        hiddenCursor = c;
    }

    public static Cursor getActiveCursor(){ return activeCursor; }
    public static Cursor getHiddenCursor() { return hiddenCursor; }
}

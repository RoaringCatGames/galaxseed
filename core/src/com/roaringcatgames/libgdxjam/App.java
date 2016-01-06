package com.roaringcatgames.libgdxjam;

import com.badlogic.gdx.Game;

/**
 * Created by barry on 12/22/15 @ 7:31 PM.
 */
public class App {

    public static final Game Initialize(){
        game = new LifeInSpace();
        return game;
    }
    public static LifeInSpace game;

    public static final float MAX_DELTA_TICK = 1f/30f;
    public static final float PPM = 32.0f;
}

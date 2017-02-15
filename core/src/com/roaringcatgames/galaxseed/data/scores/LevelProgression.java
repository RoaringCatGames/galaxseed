package com.roaringcatgames.galaxseed.data.scores;

import com.badlogic.gdx.utils.ArrayMap;

/**
 * A junky representation of level progression, with levels hard coded in
 */
public class LevelProgression {

    public ArrayMap<String, Boolean> levelUnlocks = new ArrayMap<>();

    public LevelProgression(){
        levelUnlocks.put("2", false);
        levelUnlocks.put("3", false);
        levelUnlocks.put("4", false);
        levelUnlocks.put("5", false);
        levelUnlocks.put("6", false);
        levelUnlocks.put("7", false);
        levelUnlocks.put("8", false);
        levelUnlocks.put("9", false);
    }

}

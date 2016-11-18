package com.roaringcatgames.galaxseed.data.entitydefs;

import com.badlogic.gdx.utils.Array;

/**
 * Created by barry on 11/17/16.
 */
public class AnimationSetDefinition {

    public boolean shouldClearOnBlankState;
    public boolean isPaused;
    public boolean shouldLoop;
    public Array<AnimationDefinition> animations = new Array<>();
}

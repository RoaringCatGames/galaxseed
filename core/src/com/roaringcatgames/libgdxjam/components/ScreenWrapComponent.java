package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 1/8/16 @ 8:31 PM.
 */
public class ScreenWrapComponent implements Component, Pool.Poolable {

    public ScreenWrapMode mode = ScreenWrapMode.HORIZONTAL;
    public boolean shouldRandomizePerpendicularPosition = false;
    public boolean shouldRandomizeTexture = false;
    public boolean isReversed = false;
    public float wrapOffset = 0f;
    public float minPos = 0f;
    public float maxPos = 1f;
    public Array<? extends TextureRegion> possibleRegions;

    public static ScreenWrapComponent create(PooledEngine engine){
        return engine.createComponent(ScreenWrapComponent.class);
    }

    public ScreenWrapComponent setMode(ScreenWrapMode newMode){
        this.mode = newMode;
        return this;
    }

    public ScreenWrapComponent shouldRandomPerpendicularPosition(boolean shouldReset){
        this.shouldRandomizePerpendicularPosition = shouldReset;
        return this;
    }

    public ScreenWrapComponent setRandomizeTexture(boolean shouldShapeShift){
        this.shouldRandomizeTexture = shouldShapeShift;
        return this;
    }
    public ScreenWrapComponent setPossibleRegions(Array<? extends TextureRegion> regions){
        this.possibleRegions = regions;
        return this;
    }

    public ScreenWrapComponent setReversed(boolean isReversed){
        this.isReversed = isReversed;
        return this;
    }

    public ScreenWrapComponent setWrapOffset(float offset){
        this.wrapOffset = offset;
        return this;
    }

    public ScreenWrapComponent setMinMaxPos(float min, float max){
        this.minPos = min;
        this.maxPos = max;
        return this;
    }

    @Override
    public void reset() {
        this.mode = ScreenWrapMode.HORIZONTAL;
        this.shouldRandomizePerpendicularPosition = false;
        this.shouldRandomizeTexture = false;
        this.isReversed = false;
        this.wrapOffset = 0f;
        this.possibleRegions = null;
        this.minPos = 0f;
        this.maxPos = 1f;
    }
}

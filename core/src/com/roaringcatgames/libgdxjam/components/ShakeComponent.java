package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 3/5/16 @ 12:31 PM.
 */
public class ShakeComponent implements Component, Pool.Poolable {
    public Vector2 speeds = new Vector2(1f, 1f);
    public float currentTime = 0f;
    public boolean isPaused = false;
    public Vector2 maxOffets = new Vector2(1f, 1f);
    public float shakeDuration = 0f;


    public static ShakeComponent create(PooledEngine engine){
        return engine.createComponent(ShakeComponent.class);
    }

    public ShakeComponent setSpeed(float spdX, float spdY){
        this.speeds.set(spdX, spdY);
        return this;
    }

    public ShakeComponent setPaused(boolean isPaused){
        this.isPaused = isPaused;
        return this;
    }

    public ShakeComponent setCurrentTime(float time){
        this.currentTime = time;
        return this;
    }

    public ShakeComponent setOffsets(float xOff, float yOff){
        this.maxOffets.set(xOff, yOff);
        return this;
    }

    public ShakeComponent setXOffset(float xOff){
        this.maxOffets.set(xOff, this.maxOffets.y);
        return this;
    }
    public ShakeComponent setYOffset(float yOff){
        this.maxOffets.set(this.maxOffets.x, yOff);
        return this;
    }

    public ShakeComponent setDuration(float duration){
        this.shakeDuration = duration;
        return this;
    }

    @Override
    public void reset() {
        this.speeds.set(1f, 1f);
        this.isPaused = false;
        this.currentTime = 0f;
        this.maxOffets.set(1f, 1f);
        this.shakeDuration = 0f;
    }
}

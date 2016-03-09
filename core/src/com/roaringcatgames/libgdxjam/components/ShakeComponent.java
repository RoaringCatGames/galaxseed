package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by barry on 3/5/16 @ 12:31 PM.
 */
public class ShakeComponent implements Component {
    public Vector2 speeds = new Vector2(1f, 1f);
    public float currentTime = 0f;
    public boolean isPaused = false;
    public Vector2 maxOffets = new Vector2(1f, 1f);
    public float shakeDuration = 0f;


    public static ShakeComponent create(){
        return new ShakeComponent();
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

}

package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 3/5/16 @ 2:18 PM.
 */
public class OscillationComponent implements Component, Pool.Poolable {
    public float minRotation = 0f;
    public float maxRotation = 0f;
    public float rotationSpeed = 1f;
    public boolean isClockwise = false;

    public static OscillationComponent create(PooledEngine engine){
        return engine.createComponent(OscillationComponent.class);
    }

    public OscillationComponent setMinimumRotation(float minRot){
        this.minRotation = minRot;
        return this;
    }

    public OscillationComponent setMaximumRotation(float maxRot){
        this.maxRotation = maxRot;
        return this;
    }

    public OscillationComponent setSpeed(float rotSpeed){
        this.rotationSpeed = rotSpeed;
        return this;
    }

    public OscillationComponent setClockwise(boolean isClockwise){
        this.isClockwise = isClockwise;
        return this;
    }

    @Override
    public void reset() {
        this.minRotation = 0f;
        this.maxRotation = 0f;
        this.rotationSpeed = 1f;
        this.isClockwise = false;
    }
}

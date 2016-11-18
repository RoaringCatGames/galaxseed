package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Pool;

/**
 * Defines how an entity can be adjusted on x and y positions
 */
public class AdjustablePositionComponent implements Component, Pool.Poolable {

    public boolean isAdjusting = false;
    public float xAdjust = 1f;
    public float yAdjust = 1f;
    public float zAdjust = 1f;
    public float selectionWidth = 1f;
    public float selectionHeight = 1f;

    public static AdjustablePositionComponent create(Engine engine){
        return engine.createComponent(AdjustablePositionComponent.class);
    }

    public AdjustablePositionComponent setIsAjusting(boolean isAdjusting){
        this.isAdjusting = isAdjusting;
        return this;
    }

    public AdjustablePositionComponent setXAdjustment(float adjust){
        this.xAdjust = adjust;
        return this;
    }

    public AdjustablePositionComponent setYAdjustment(float adjust){
        this.yAdjust = adjust;
        return this;
    }

    public AdjustablePositionComponent setZAdjust(float adjust){
        this.zAdjust = adjust;
        return this;
    }

    public AdjustablePositionComponent setSelectionWidth(float width){
        this.selectionWidth = width;
        return this;
    }

    public AdjustablePositionComponent setSelectionHeight(float height){
        this.selectionHeight = height;
        return this;
    }

    @Override
    public void reset() {
        this.isAdjusting = false;
        this.xAdjust = 1f;
        this.yAdjust = 1f;
        this.zAdjust = 1f;
        this.selectionHeight = 1f;
        this.selectionWidth  = 1f;
    }
}

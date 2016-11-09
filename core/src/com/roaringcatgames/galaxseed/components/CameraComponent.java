package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Pool;

/**
 * A component to hold a camera object on a system
 */
public class CameraComponent implements Component, Pool.Poolable{

    public Camera camera;

    public CameraComponent setCamera(Camera camera){
        this.camera = camera;
        return this;
    }

    @Override
    public void reset() {
        camera = null;
    }

    public static CameraComponent create(Engine e){
        return e.createComponent(CameraComponent.class);
    }
}

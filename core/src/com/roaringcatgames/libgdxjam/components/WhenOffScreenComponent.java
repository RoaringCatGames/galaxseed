package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 1/3/16 @ 1:45 PM.
 */
public class WhenOffScreenComponent implements Component, Pool.Poolable {

    public boolean hasBeenOnScreen = false;

    public static WhenOffScreenComponent create(Engine engine){
        if(engine instanceof PooledEngine) {
            return ((PooledEngine)engine).createComponent(WhenOffScreenComponent.class);
        }else{
            return new WhenOffScreenComponent();
        }
    }

    public WhenOffScreenComponent setHasBeenOnScreen(boolean beenOnScreen){
        this.hasBeenOnScreen = beenOnScreen;
        return this;
    }

    @Override
    public void reset() {
        this.hasBeenOnScreen = false;
    }
}

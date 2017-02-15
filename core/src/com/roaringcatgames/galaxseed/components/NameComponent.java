package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Pool;

/**
 * Component to Hold a Name value
 */
public class NameComponent implements Component, Pool.Poolable {
    public String name = "--NA--";

    public static NameComponent create(Engine engine){
        return engine.createComponent(NameComponent.class);
    }

    public NameComponent setName(String name){
        this.name = name;
        return this;
    }

    @Override
    public void reset() {
        name = "--NA--";
    }
}

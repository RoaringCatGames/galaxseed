package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Pool;

/**
 * Marks a component as part of an InfoBubble
 */
public class InfoBubbleComponent implements Component, Pool.Poolable {

    public static InfoBubbleComponent create(Engine engine){
        return engine.createComponent(InfoBubbleComponent.class);
    }
    @Override
    public void reset() {

    }
}

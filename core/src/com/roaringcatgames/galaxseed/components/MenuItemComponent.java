package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Identifies a menu item
 */
public class MenuItemComponent implements Component, Pool.Poolable {

    public boolean isFilled = false;
    public static MenuItemComponent create(PooledEngine engine){
        return engine.createComponent(MenuItemComponent.class);
    }

    @Override
    public void reset() {
        isFilled = false;
    }
}

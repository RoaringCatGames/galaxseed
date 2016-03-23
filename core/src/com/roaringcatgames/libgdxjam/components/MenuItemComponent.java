package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 3/3/16 @ 11:54 PM.
 */
public class MenuItemComponent implements Component, Pool.Poolable {

    public static MenuItemComponent create(PooledEngine engine){
        return engine.createComponent(MenuItemComponent.class);
    }

    @Override
    public void reset() {

    }
}

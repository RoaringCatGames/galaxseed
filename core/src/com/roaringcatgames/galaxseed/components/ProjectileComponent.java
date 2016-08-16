package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 1/12/16 @ 8:01 PM.
 */
public class ProjectileComponent implements Component, Pool.Poolable {

    public int damage = 0;

    public static ProjectileComponent create(PooledEngine engine){
        return engine.createComponent(ProjectileComponent.class);
    }

    public ProjectileComponent setDamage(int dmg){
        this.damage = dmg;
        return this;
    }

    @Override
    public void reset() {
        this.damage = 0;
    }
}

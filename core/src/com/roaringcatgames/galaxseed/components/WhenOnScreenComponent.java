package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Pool;

/**
 * Quick system to trigger achievements
 */
public class WhenOnScreenComponent implements Component, Pool.Poolable {

    public String achievementName = "";

    public static WhenOnScreenComponent create(Engine engine){
        return engine.createComponent(WhenOnScreenComponent.class);
    }

    public WhenOnScreenComponent setAchievementName(String name){
        this.achievementName = name;
        return this;
    }


    @Override
    public void reset() {
        this.achievementName = "";
    }
}

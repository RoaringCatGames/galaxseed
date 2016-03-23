package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 3/4/16 @ 12:50 AM.
 */
public class ScoreComponent implements Component, Pool.Poolable {

    public int score = 0;

    public static ScoreComponent create(PooledEngine engine){
        return engine.createComponent(ScoreComponent.class);
    }


    public ScoreComponent setScore(int score){
        this.score = score;
        return this;
    }

    @Override
    public void reset() {
        this.score = 0;
    }
}

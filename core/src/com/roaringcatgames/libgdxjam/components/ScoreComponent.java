package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by barry on 3/4/16 @ 12:50 AM.
 */
public class ScoreComponent implements Component {

    public int score = 0;

    public static ScoreComponent create(){ return new ScoreComponent(); }

    public ScoreComponent setScore(int score){
        this.score = score;
        return this;
    }
}

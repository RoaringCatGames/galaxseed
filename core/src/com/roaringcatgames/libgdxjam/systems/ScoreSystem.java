package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.roaringcatgames.libgdxjam.components.ScoreComponent;
import com.roaringcatgames.libgdxjam.components.TextComponent;

/**
 * Created by barry on 3/4/16 @ 12:57 AM.
 */
public class ScoreSystem extends IteratingSystem {

    private ComponentMapper<ScoreComponent> sm;
    private ComponentMapper<TextComponent> tm;

    public ScoreSystem(){
        super(Family.all(ScoreComponent.class, TextComponent.class).get());
        sm = ComponentMapper.getFor(ScoreComponent.class);
        tm = ComponentMapper.getFor(TextComponent.class);
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ScoreComponent sc = sm.get(entity);
        TextComponent tc = tm.get(entity);

        tc.setText("Score: " + getBufferedScore(sc.score));
    }

    private String getBufferedScore(int score){
        String base = score < 10 ? "0000" :
                      score < 100 ? "000" :
                      score < 1000 ? "00" :
                      score < 10000 ? "0" : "";
        return base += score;
    }
}

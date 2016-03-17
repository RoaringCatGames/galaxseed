package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.AnimationComponent;
import com.roaringcatgames.kitten2d.ashley.components.TextureComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.ScoreComponent;
import com.roaringcatgames.libgdxjam.components.TextComponent;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * Created by barry on 3/4/16 @ 12:57 AM.
 */
public class ScoreSystem extends IteratingSystem {

    private ComponentMapper<ScoreComponent> sm;
    private ComponentMapper<TextComponent> tm;
    private ComponentMapper<TextureComponent> txm;

    private Entity plantGrowth;
    private Entity scoreCard;
    private Array<? extends TextureRegion> animationFrames;
    private int currentScore = 0;

    public ScoreSystem(){
        super(Family.all(ScoreComponent.class, TextComponent.class).get());
        sm = ComponentMapper.getFor(ScoreComponent.class);
        tm = ComponentMapper.getFor(TextComponent.class);
        txm = ComponentMapper.getFor(TextureComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        animationFrames = Assets.getTitleTreeFrames();
        plantGrowth = ((PooledEngine) engine).createEntity();
        plantGrowth.add(TextureComponent.create()
            .setRegion(animationFrames.get(0)));
        plantGrowth.add(TransformComponent.create()
                .setPosition(1f, 1.5f, Z.score)
                .setScale(0.5f, 0.5f));

        engine.addEntity(plantGrowth);

        scoreCard = ((PooledEngine)engine).createEntity();
        BitmapFont fnt = Gdx.graphics.getDensity() > 1f ? Assets.get128Font() : Assets.get64Font();
        scoreCard.add(TextComponent.create()
                .setText("Score: 00000")
                .setFont(fnt));
        scoreCard.add(TransformComponent.create()
                .setPosition(5f, 1.5f, Z.score));
        scoreCard.add(ScoreComponent.create()
                .setScore(0));
        engine.addEntity(scoreCard);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        engine.removeEntity(plantGrowth);
        engine.removeEntity(scoreCard);
        plantGrowth = null;
        scoreCard = null;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        TextureComponent txc = txm.get(plantGrowth);

        int growthPosition = getGrowthPosition(currentScore);
        if(growthPosition >= animationFrames.size){
            growthPosition = animationFrames.size - 1;
        }
        //Gdx.app.log("Score System", "Current Score: " + currentScore + " Image Position: " + growthPosition);

        txc.setRegion(animationFrames.get(growthPosition));
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ScoreComponent sc = sm.get(entity);
        TextComponent tc = tm.get(entity);

        tc.setText(getBufferedScore(sc.score));

        currentScore = sc.score;
    }

    private int getGrowthPosition(int score){

        int pos = score <  10 ?  0 :
                  score <  20 ?  1 :
                  score <  30 ?  2 :
                  score <  40 ?  3 :
                  score <  50 ?  4 :
                  score <  60 ?  5 :
                  score <  70 ?  6 :
                  score <  80 ?  7 :
                  score <  90 ?  8 :
                  score < 100 ?  9 :
                  score < 120 ? 10 :
                  score < 140 ? 11 :
                  score < 160 ? 12 :
                  score < 180 ? 13 :
                  score < 200 ? 14 :
                  score < 220 ? 15 :
                  score < 240 ? 16 :
                  score < 260 ? 17 :
                  score < 280 ? 18 :
                  score < 300 ? 19 :
                  score < 330 ? 20 :
                  score < 360 ? 21 :
                  score < 390 ? 22 :
                  score < 420 ? 23 :
                  score < 450 ? 24 :
                  score < 480 ? 25 :
                  score < 510 ? 26 :
                  score < 550 ? 27 :
                  score < 650 ? 28 :
                                29;
        return pos;
    }
    private String getBufferedScore(int score){
        String base = score < 10 ? "0000" :
                      score < 100 ? "000" :
                      score < 1000 ? "00" :
                      score < 10000 ? "0" : "";
        return base += score;
    }
}

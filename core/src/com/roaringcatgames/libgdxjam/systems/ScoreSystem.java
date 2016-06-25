package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.ParticleEmitterComponent;
import com.roaringcatgames.kitten2d.ashley.components.TextComponent;
import com.roaringcatgames.kitten2d.ashley.components.TextureComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.ScoreComponent;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * System responsible for updating score
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
        PooledEngine pe = (PooledEngine)engine;
        float yPos = App.H - 0.5f;
        animationFrames = Assets.getTitleTreeFrames();
        plantGrowth = pe.createEntity();
        plantGrowth.add(TextureComponent.create(pe)
            .setRegion(animationFrames.get(0)));
        plantGrowth.add(TransformComponent.create(pe)
                .setPosition(1f, yPos, Z.score)
                .setScale(0.5f, 0.5f));
        engine.addEntity(plantGrowth);

        scoreCard = pe.createEntity();
        BitmapFont fnt = Gdx.graphics.getDensity() > 1f ? Assets.get128Font() : Assets.get64Font();
        scoreCard.add(TextComponent.create(pe)
                .setText("Score: 00000")
                .setFont(fnt));
        scoreCard.add(TransformComponent.create(pe)
                .setPosition(5f, yPos, Z.score));
        scoreCard.add(ScoreComponent.create(pe)
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

    private int lastPosition = 0;
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        TextureComponent txc = txm.get(plantGrowth);

        int growthPosition = getGrowthPosition(currentScore);
        if(growthPosition != lastPosition){
            plantGrowth.add(ParticleEmitterComponent.create((PooledEngine) getEngine())
                .setParticleLifespans(0.3f, 0.5f)
                .setShouldFade(true)
                .setSpawnRate(1f*(growthPosition))
                .setAngleRange(-90f, 90f)
                .setParticleImages(Assets.getLeafFrames())
                .setDuration(0.3f)
                .setZIndex(Z.leaves)
                .setSpeed(5f, 8f));
        }

        lastPosition = growthPosition;
        if(growthPosition >= animationFrames.size){
            growthPosition = animationFrames.size - 1;
        }

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

        //GROSSS!!!
        int pos = score <     1 ?  0 :
                  score <    10 ?  1 :
                  score <    50 ?  2 :
                  score <   100 ?  3 :
                  score <   200 ?  4 :
                  score <   400 ?  5 :
                  score <   800 ?  6 :
                  score <  1600 ?  7 :
                  score <  3200 ?  8 :
                  score <  6400 ?  9 :
                  score < 10000 ? 10 :
                  score < 15000 ? 11 :
                  score < 20000 ? 12 :
                  score < 25000 ? 13 :
                  score < 30000 ? 14 :
                                  15;
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

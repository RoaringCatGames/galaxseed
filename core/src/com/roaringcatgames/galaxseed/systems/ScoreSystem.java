package com.roaringcatgames.galaxseed.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.components.ScoreComponent;
import com.roaringcatgames.galaxseed.values.Z;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.TextComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;

/**
 * System responsible for updating score
 */
public class ScoreSystem extends IteratingSystem {

    private ComponentMapper<ScoreComponent> sm;
    private ComponentMapper<TextComponent> tm;

    private Entity scoreCard;
    private int currentScore = 0;

    public ScoreSystem(){
        super(Family.all(ScoreComponent.class, TextComponent.class).get());
        sm = ComponentMapper.getFor(ScoreComponent.class);
        tm = ComponentMapper.getFor(TextComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        PooledEngine pe = (PooledEngine)engine;

        float opacity = 0.5f;
        float densityCutOff = 2.5f;
        float targetUnitOffset = (1f/6f);
        float highDensityMultiplier = 1f;
        scoreCard = pe.createEntity();
        float density = Gdx.graphics.getDensity();
        float textOffset = targetUnitOffset * density;
        float yPos = density > densityCutOff ? App.H - (highDensityMultiplier*textOffset) : App.H - textOffset;
        BitmapFont fnt = density > densityCutOff ? Assets.get128Font() : Assets.get64Font();

        scoreCard.add(TextComponent.create(pe)
                .setText("Score: 00000")
                .setFont(fnt));
        scoreCard.add(BoundsComponent.create(pe)
            .setBounds(0f, 0f, 5f, 5f));
        scoreCard.add(TransformComponent.create(pe)
                .setPosition(App.W/2f, yPos, Z.score)
                .setOpacity(opacity));
        scoreCard.add(ScoreComponent.create(pe)
                .setScore(0));
        engine.addEntity(scoreCard);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        engine.removeEntity(scoreCard);
        scoreCard = null;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ScoreComponent sc = sm.get(entity);
        TextComponent tc = tm.get(entity);

        tc.setText(getBufferedScore(sc.score));

        currentScore = sc.score;
    }

    private String getBufferedScore(int score){
        String base = score < 10 ? "0000" :
                      score < 100 ? "000" :
                      score < 1000 ? "00" :
                      score < 10000 ? "0" : "";
        return base + score;
    }

    public int getScore(){
        return currentScore;
    }
}

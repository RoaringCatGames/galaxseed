package com.roaringcatgames.galaxseed;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.roaringcatgames.galaxseed.data.entitydefs.Transform;
import com.roaringcatgames.galaxseed.values.Z;
import com.roaringcatgames.kitten2d.ashley.IActionResolver;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.components.AnimationComponent;
import com.roaringcatgames.kitten2d.ashley.components.TextureComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.kitten2d.ashley.components.TweenComponent;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;

/**
 * Handles actions fired from the Level Select Screen
 */
public class LevelSelectActionResolver implements IActionResolver {

    private IGameProcessor game;

    public LevelSelectActionResolver(IGameProcessor game){
        this.game = game;
    }

    @Override
    public void resolveAction(String eventName, Entity firingEntity, Engine containerEngine) {
        Gdx.app.log("LevelSelectActionResolver", "Action Firing");
        switch(eventName){
            case "LEVEL_1":
                AnimationComponent ac = K2ComponentMappers.animation.get(firingEntity);
                ac.setPaused(false);
                Gdx.app.log("LevelSelectActionResolver", "Action Fired");
                //game.switchScreens("GAME");
                addLevelInfoBubble("Level 1", Assets.BubbleColor.BLUE, firingEntity, containerEngine);
                break;
            case "LEVEL_2":

                break;
        }
    }

    private void addLevelInfoBubble(String levelName, Assets.BubbleColor bubbleColor, Entity selectAnchor, Engine engine){
        //Create the bg bubble intro
        TransformComponent tc = K2ComponentMappers.transform.get(selectAnchor);
        Entity e = engine.createEntity();
        e.add(TransformComponent.create(engine)
            .setPosition(tc.position.x, tc.position.y, Z.info_bubble)
            .setScale(0.1f, 0.1f));

        e.add(TextureComponent.create(engine)
            .setRegion(Assets.getInfoBubbleBackground(bubbleColor)));
        e.add(TweenComponent.create(engine)
            .addTween(Tween.to(e, K2EntityTweenAccessor.SCALE, 0.5f).target(1f, 1f).ease(TweenEquations.easeOutElastic)));
        engine.addEntity(e);


    }
}

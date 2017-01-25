package com.roaringcatgames.galaxseed;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.roaringcatgames.kitten2d.ashley.IActionResolver;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.AnimationComponent;
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
                game.switchScreens("GAME");
                break;
            case "LEVEL_2":

                break;
        }
    }
}

package com.roaringcatgames.libgdxjam.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.systems.BackgroundSystem;

/**
 * This is an {@link LazyInitScreen} implementation that will
 * handle the Options and Credits view.
 */
public class OptionScreen extends LazyInitScreen {

    private IGameProcessor game;
    private PooledEngine engine;

    public OptionScreen(IGameProcessor game){
        super();
        this.game = game;
    }

    @Override
    protected void init() {
        engine = new PooledEngine();

        Vector2 minBounds = new Vector2(0f, 0f);
        Vector2 maxBounds = new Vector2(game.getCamera().viewportWidth, game.getCamera().viewportHeight);
        engine.addSystem(new BackgroundSystem(minBounds, maxBounds, false, true));


        //Kitten2D Systems
        engine.addSystem(new MovementSystem());
        engine.addSystem(new TweenSystem());
        engine.addSystem(new RenderingSystem(game.getBatch(), game.getCamera(), App.PPM));
        engine.addSystem(new TextRenderingSystem(game.getBatch(), game.getGUICamera(), game.getCamera()));
        engine.addSystem(new DebugSystem(game.getCamera()));
    }

    @Override
    protected void update(float deltaChange) {
        engine.update(Math.min(deltaChange, App.MAX_DELTA_TICK));
    }
}

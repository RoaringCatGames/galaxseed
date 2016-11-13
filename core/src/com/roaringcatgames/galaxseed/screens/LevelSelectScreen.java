package com.roaringcatgames.galaxseed.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.systems.BackgroundSystem;
import com.roaringcatgames.galaxseed.systems.BackgroundSystemConfig;
import com.roaringcatgames.galaxseed.systems.CameraPanningSystem;
import com.roaringcatgames.galaxseed.values.Songs;
import com.roaringcatgames.galaxseed.values.Z;
import com.roaringcatgames.kitten2d.ashley.components.TextureComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;

/**
 * Screen to house the selection of a level. Will be a menu screen without a player, and
 * will be scrollable.
 */
public class LevelSelectScreen extends LazyInitScreen {

    private IGameProcessor game;
    private Engine engine;

    public LevelSelectScreen(IGameProcessor game){
        this.game = game;
    }

    @Override
    protected void init() {
        engine = new PooledEngine();

        Vector2 minBounds = new Vector2(0f, 0f);
        Vector2 maxBounds = new Vector2(App.W, App.H*3f);

        Vector2 minCamBounds = new Vector2(App.W/2f, App.H/2f);
        Vector2 maxCamBounds = new Vector2(App.W/2f, (App.H/2f)*5f);

        //Build Systems
        RotationSystem rotationSystem = new RotationSystem();
        MovementSystem movementSystem = new MovementSystem();
        BoundsSystem boundsSystem = new BoundsSystem();
        ScreenWrapSystem screenWrapSystem = new ScreenWrapSystem(minBounds, maxBounds, App.PPM);
        CameraPanningSystem cameraPanningSystem = new CameraPanningSystem(minCamBounds, maxCamBounds, game, App.PPM,  5f);

        BackgroundSystem bgSystem = new BackgroundSystem(minBounds, maxBounds,
                new BackgroundSystemConfig(false, false, false, false));

        TweenSystem tweenSystem = new TweenSystem();
        RenderingSystem renderingSystem = new RenderingSystem(game.getBatch(), game.getCamera(), App.PPM);
        TextRenderingSystem textRenderingSystem = new TextRenderingSystem(game.getBatch(), game.getGUICamera(), game.getCamera());

        //Add Systems
        engine.addSystem(rotationSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(boundsSystem);
        engine.addSystem(screenWrapSystem);
        engine.addSystem(cameraPanningSystem);

        engine.addSystem(bgSystem);

        engine.addSystem(tweenSystem);
        engine.addSystem(renderingSystem);
        engine.addSystem(textRenderingSystem);

        //Initial Entities
        int item = 1;
        float y = 2f, x = 2f;
        while(y < (App.H*3f)) {
            y += ((App.H/2f)*5f)/7f;
            x = (x+5f)%30f;
            addStickerEntity(x, y, Assets.getLevelPlanet(item++));
        }

        game.playBgMusic(Songs.LEVEL_SELECT);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    protected void update(float deltaChange) {
        engine.update(deltaChange);
    }

    private void addStickerEntity(float x, float y, TextureRegion region){
        Entity e = engine.createEntity();
        e.add(TransformComponent.create(engine)
                .setPosition(x, y, Z.flames)
                .setRotation(30f));
        e.add(TextureComponent.create(engine)
                .setRegion(region));
        engine.addEntity(e);
    }
}

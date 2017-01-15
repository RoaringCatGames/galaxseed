package com.roaringcatgames.galaxseed.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.components.AdjustablePositionComponent;
import com.roaringcatgames.galaxseed.components.Mappers;
import com.roaringcatgames.galaxseed.data.EntityBuilder;
import com.roaringcatgames.galaxseed.data.entitydefs.Transform;
import com.roaringcatgames.galaxseed.systems.AdjustPositionSystem;
import com.roaringcatgames.galaxseed.systems.BackgroundSystem;
import com.roaringcatgames.galaxseed.systems.BackgroundSystemConfig;
import com.roaringcatgames.galaxseed.systems.CameraPanningSystem;
import com.roaringcatgames.galaxseed.values.Songs;
import com.roaringcatgames.galaxseed.values.Z;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.TextureComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.helpers.K2PreferenceManager;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;

import java.util.Comparator;

/**
 * Screen to house the selection of a level. Will be a menu screen without a player, and
 * will be scrollable.
 */
public class LevelSelectScreen extends LazyInitScreen implements EntityListener{

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
                new BackgroundSystemConfig(false, false, false, false, false));

        AdjustPositionSystem adjustPositionSystem = new AdjustPositionSystem(game.getCamera(), game.getViewport(), game);

        TweenSystem tweenSystem = new TweenSystem();
        AnimationSystem animationSystem = new AnimationSystem();
        RenderingSystem renderingSystem = new RenderingSystem(game.getBatch(), game.getCamera(), App.PPM);
        TextRenderingSystem textRenderingSystem = new TextRenderingSystem(game.getBatch(), game.getGUICamera(), game.getCamera());


        engine.addEntityListener(this);

//        TextureAtlas animationAtlas = Assets.am.get("animations/animations.atlas", Assets.TEXTURE_ATLAS);
//        TextureAtlas spritesAtlas = Assets.am.get("sprites/sprites.atlas", Assets.TEXTURE_ATLAS);
//
//        Array<TextureAtlas.AtlasRegion> regions = animationAtlas.getRegions();
//        regions.sort(new Comparator<TextureAtlas.AtlasRegion>() {
//            @Override
//            public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
//                return o1.name.compareTo(o2.name);
//            }
//        });
//        for(TextureAtlas.AtlasRegion region:regions){
//           Gdx.app.log("LEVEL SELECT SCREEN", region.name);
//        }

        //Add Systems
        engine.addSystem(rotationSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(boundsSystem);
        engine.addSystem(screenWrapSystem);
        engine.addSystem(cameraPanningSystem);

        engine.addSystem(bgSystem);

        engine.addSystem(tweenSystem);
        engine.addSystem(animationSystem);
        engine.addSystem(renderingSystem);
        engine.addSystem(textRenderingSystem);

        engine.addSystem(adjustPositionSystem);

        //Initial Entities
        //        int item = 1;
        //        float y = 2f, x = 2f;
        //        while(y < (App.H*3f)) {
        //            y += ((App.H/2f)*5f)/7f;
        //            x = (x+5f)%30f;
        //            addStickerEntity(x, y, Assets.getLevelPlanet(item++));
        //        }

        Array<Entity> entities = EntityBuilder.buildEntities(engine, Assets.getLevel1().entities);
        Gdx.app.log("LevelSelectSystem", "Entities " + entities.size);
        for(Entity e:entities) {
            Gdx.app.log("LevelSelectSystem", "Adding Entity!!");
            engine.addEntity(e);
        }

//        Entity e = engine.createEntity();
//        e.add(TransformComponent.create(engine)
//            .setPosition(App.W/2f, 90.46875f/2f, Z.player + 5f));
//        e.add(TextureComponent.create(engine)
//            .setRegion(Assets.getLevelLayoutRef()));
//        engine.addEntity(e);

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

    /****
     * EntityListener
     ****/

    @Override
    public void entityAdded(Entity entity) {
        if(!Mappers.adjust.has(entity) && K2ComponentMappers.transform.has(entity)){
            entity.add(AdjustablePositionComponent.create(engine)
                .setSelectionHeight(3f)
                .setSelectionWidth(3f));
        }
    }

    @Override
    public void entityRemoved(Entity entity) {

    }
}

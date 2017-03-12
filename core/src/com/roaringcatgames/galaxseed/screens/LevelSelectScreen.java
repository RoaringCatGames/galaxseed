package com.roaringcatgames.galaxseed.screens;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.components.AdjustablePositionComponent;
import com.roaringcatgames.galaxseed.components.Mappers;
import com.roaringcatgames.galaxseed.data.EntityBuilder;
import com.roaringcatgames.galaxseed.data.scores.LevelProgression;
import com.roaringcatgames.galaxseed.systems.AdjustPositionSystem;
import com.roaringcatgames.galaxseed.systems.BackgroundSystem;
import com.roaringcatgames.galaxseed.systems.BackgroundSystemConfig;
import com.roaringcatgames.galaxseed.systems.CameraPanningSystem;
import com.roaringcatgames.galaxseed.values.Colors;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;

/**
 * Screen to house the selection of a level. Will be a menu screen without a player, and
 * will be scrollable.
 */
public class LevelSelectScreen extends LazyInitScreen implements EntityListener{

    private IGameProcessor game;
    private Engine engine;
    private LevelProgression levelProgression = new LevelProgression();

    public LevelSelectScreen(IGameProcessor game){
        this.game = game;
    }

    @Override
    protected void init() {
        engine = new PooledEngine();

        //this.game.getPreferenceManager().updateBoolean("level-3-passed", false);
        for(int i=2;i<10;i++){
            levelProgression.levelUnlocks.put(String.valueOf(i), this.game.getPreferenceManager().getStoredBoolean("level-" + i + "-passed"));
        }

        Vector2 minBounds = new Vector2(0f, 0f);
        Vector2 maxBounds = new Vector2(App.W, App.H*3f);

        Vector2 minCamBounds = new Vector2(App.W/2f, App.H/2f);
        float halfSplits = (90f/(App.H/2f)) - 1f;
        Vector2 maxCamBounds = new Vector2(App.W/2f, (App.H/2f)*halfSplits);

        //Build Systems
        RotationSystem rotationSystem = new RotationSystem();
        MovementSystem movementSystem = new MovementSystem();
        BoundsSystem boundsSystem = new BoundsSystem();
        FollowerSystem followerSystem = new FollowerSystem(Family.one(
                TransformComponent.class
        ).get());
        ScreenWrapSystem screenWrapSystem = new ScreenWrapSystem(minBounds, maxBounds, App.PPM);
        CameraPanningSystem cameraPanningSystem = new CameraPanningSystem(minCamBounds, maxCamBounds, game, App.PPM,  5f);

        BackgroundSystem bgSystem = new BackgroundSystem(minBounds, maxBounds,
                new BackgroundSystemConfig(false, false, false, false, false));

        AdjustPositionSystem adjustPositionSystem = new AdjustPositionSystem(game.getCamera(), game.getViewport(), game);

        TweenSystem tweenSystem = new TweenSystem();
        AnimationSystem animationSystem = new AnimationSystem();
        RenderingSystem renderingSystem = new RenderingSystem(game.getBatch(), game.getCamera(), App.PPM);
        TextRenderingSystem textRenderingSystem = new TextRenderingSystem(game.getBatch(), game.getGUICamera(), game.getCamera());

        ClickableSystem clickableSystem = new ClickableSystem(game, new LevelSelectActionResolver(game));

        engine.addEntityListener(this);

        //Add Systems
        engine.addSystem(rotationSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(boundsSystem);
        engine.addSystem(followerSystem);
        engine.addSystem(screenWrapSystem);
        engine.addSystem(cameraPanningSystem);
        engine.addSystem(clickableSystem);

        engine.addSystem(bgSystem);

        engine.addSystem(tweenSystem);
        engine.addSystem(animationSystem);
        engine.addSystem(renderingSystem);
        engine.addSystem(textRenderingSystem);

        engine.addSystem(adjustPositionSystem);
        engine.addSystem(new DebugSystem(game.getCamera()));

        Array<Entity> entities = EntityBuilder.buildEntities(engine, Assets.getLevelSelect().entities);
        Gdx.app.log("LevelSelectSystem", "Entities " + entities.size);
        for(Entity e:entities) {
            Gdx.app.log("LevelSelectSystem", "Adding Entity!!");
            if(Mappers.name.has(e)){
                String name = Mappers.name.get(e).name;
                if(name.startsWith("level-")){

                    String levelValue = name.substring(6, 7);
                    Gdx.app.log("VALUE", "Level Value: " + levelValue);
                    if(!"1".equals(levelValue) && levelProgression.levelUnlocks.get(levelValue)){
                        TransformComponent tc = K2ComponentMappers.transform.get(e);
                        tc.setTint(Colors.PLAIN_WHITE);
                    }
                }
            }
            engine.addEntity(e);
        }

        //game.playBgMusic(Songs.LEVEL_SELECT);
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

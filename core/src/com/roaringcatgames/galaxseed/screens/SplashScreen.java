package com.roaringcatgames.galaxseed.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.galaxseed.systems.BackgroundSystemConfig;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;
import com.roaringcatgames.galaxseed.Animations;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.systems.BackgroundSystem;
import com.roaringcatgames.galaxseed.values.Z;

import java.util.Random;

/**
 * Screen to handle displaying information while game is loading.
 */
public class SplashScreen extends LazyInitScreen {
    private IGameProcessor game;
    private PooledEngine engine;

    private float minSplashSeconds = 6f;
    private float elapsedTime = 0f;

    public SplashScreen(IGameProcessor game){
        this.game = game;
    }

    @Override
    protected void init() {

        engine = new PooledEngine();
        RenderingSystem render = new RenderingSystem(game.getBatch(), game.getCamera(), App.PPM);

        engine.addSystem(new AnimationSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(render);
        Vector2 minBounds = new Vector2(0f, 0f);
        Vector2 maxBounds = new Vector2(App.W, App.H);
        engine.addSystem(new ScreenWrapSystem(minBounds, maxBounds, App.PPM));
        engine.addSystem(new BackgroundSystem(minBounds, maxBounds, new BackgroundSystemConfig(true, false, false, true, true, false)));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RotationSystem());
        engine.addSystem(new FollowerSystem(Family.all(AnimationComponent.class).get()));

        Entity title = engine.createEntity();
        title.add(TransformComponent.create(engine)
                .setPosition(App.W / 2f, App.H/2f, Z.gameOver)
                .setScale(1f, 1f));
        title.add(TextureComponent.create(engine)
                .setRegion(Assets.getSplashTitle()));
        engine.addEntity(title);


        float catX, catY, catSpeedX, catSpeedY;
        Random r = new Random();
        float yAdjust = r.nextFloat();
        catY = (App.H-4f) * yAdjust + 4f;
        catX = r.nextFloat() < 0.5f ? 16f : 4f;
        catSpeedX = catX == 16f ? -2.5f : 2.5f;
        catSpeedY = catY >= 24f ? -2.5f :
                    catY >= 16f ? -1.0f :
                    catY <= 6f  ?  2.5f : 1.0f;



        Entity loading = engine.createEntity();
        loading.add(VelocityComponent.create(engine)
            .setSpeed(catSpeedX, catSpeedY));
        loading.add(RotationComponent.create(engine)
            .setRotationSpeed(45f));
        loading.add(TransformComponent.create(engine)
                .setPosition(catX, catY, 0f)
                .setScale(0.5f, 0.5f));
        loading.add(TextureComponent.create(engine));
        loading.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(true));
        loading.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", new Animation(1f / 30f, Assets.getLoadingFrames(), Animation.PlayMode.LOOP_PINGPONG)));
        engine.addEntity(loading);

        Entity bubble = engine.createEntity();
        bubble.add(TransformComponent.create(engine)
                .setPosition(16f, 8f, 0f)
            .setScale(0.5f, 0.5f));
        bubble.add(FollowerComponent.create(engine)
            .setTarget(loading)
            .setMode(FollowMode.STICKY)
            .setOffset(-3.75f, 1f));
        bubble.add(TextureComponent.create(engine));
        bubble.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", new Animation(1f / 9f, Assets.getBubbleFrames())));
        bubble.add(StateComponent.create(engine)
                .set("DEFAULT")
            .setLooping(false));

        engine.addEntity(bubble);
    }

    @Override
    protected void update(float delta) {
        elapsedTime += delta;

        if(Assets.am.update() && elapsedTime >= minSplashSeconds){
            Gdx.app.log("Splash Screen", "Assets are Loaded!");
            Animations.init();
            game.switchScreens("MENU");
        }else {
            engine.update(delta);
        }
    }
}

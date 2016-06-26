package com.roaringcatgames.libgdxjam.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.systems.BackgroundSystem;
import com.roaringcatgames.libgdxjam.values.Z;

import java.util.Random;

/**
 * Created by barry on 12/22/15 @ 7:27 PM.
 */
public class SplashScreen extends LazyInitScreen {
    private IGameProcessor game;
    private IScreenDispatcher dispatcher;
    private PooledEngine engine;

    private float minSplashSuggestions = 6f;
    private float elapsedTime = 0f;
    //private OrthographicCamera cam;
    private Viewport viewport;

    public SplashScreen(IGameProcessor game, IScreenDispatcher dispatcher){
        this.game = game;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void init() {

        engine = new PooledEngine();
        RenderingSystem render = new RenderingSystem(game.getBatch(), game.getCamera(), App.PPM);
//        cam = render.getCamera();
//        viewport = new FitViewport(20f, 30f, cam);
//        viewport.apply();
//        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        cam.position.set(cam.viewportWidth/2f, cam.viewportHeight/2f, 0);

        engine.addSystem(new AnimationSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(render);
        Vector2 minBounds = new Vector2(0f, 0f);
        Vector2 maxBounds = new Vector2(game.getCamera().viewportWidth, game.getCamera().viewportHeight);
        engine.addSystem(new ScreenWrapSystem(minBounds, maxBounds, App.PPM));
        engine.addSystem(new BackgroundSystem(minBounds, maxBounds, false, false));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RotationSystem());
        engine.addSystem(new FollowerSystem(Family.all(AnimationComponent.class).get()));

        Entity title = engine.createEntity();
        title.add(TransformComponent.create(engine)
                .setPosition(App.W / 2f, 15f, Z.gameOver)
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

        if(Assets.am.update() && elapsedTime >= minSplashSuggestions){
            Gdx.app.log("Splash Screen", "Assets are Loaded!");
            Animations.init();
            dispatcher.endCurrentScreen();
        }else {
            engine.update(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if(viewport != null) {
            viewport.update(width, height);
        }
    }
}

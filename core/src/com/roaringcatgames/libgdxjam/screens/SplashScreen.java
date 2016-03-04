package com.roaringcatgames.libgdxjam.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.AnimationSystem;
import com.roaringcatgames.kitten2d.ashley.systems.MovementSystem;
import com.roaringcatgames.kitten2d.ashley.systems.RenderingSystem;
import com.roaringcatgames.kitten2d.ashley.systems.RotationSystem;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.systems.BackgroundSystem;
import com.roaringcatgames.libgdxjam.values.Z;
import sun.swing.BakedArrayList;

/**
 * Created by barry on 12/22/15 @ 7:27 PM.
 */
public class SplashScreen extends LazyInitScreen {
    SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private IScreenDispatcher dispatcher;
    private PooledEngine engine;

    private float minSpalshSeconds = 5f;
    private float elapsedTime = 0f;
    private OrthographicCamera cam;

    public SplashScreen(SpriteBatch batch, IScreenDispatcher dispatcher){
        this.batch = batch;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void init() {
        this.shapeRenderer = new ShapeRenderer();

        engine = new PooledEngine();
        RenderingSystem render = new RenderingSystem(batch, App.PPM);
        cam = render.getCamera();
        engine.addSystem(new AnimationSystem());
        engine.addSystem(render);
        Vector2 minBounds = new Vector2(0f, 0f);
        Vector2 maxBounds = new Vector2(cam.viewportWidth, cam.viewportHeight);
        engine.addSystem(new BackgroundSystem(minBounds, maxBounds, false));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RotationSystem());

        Entity title = engine.createEntity();
        title.add(TransformComponent.create()
            .setPosition(App.W/2f, App.H/2f, Z.gameOver)
            .setScale(1f, 1f));
        title.add(TextureComponent.create()
            .setRegion(Assets.getSplashTitle()));
        engine.addEntity(title);

        Entity e = engine.createEntity();

        e.add(TransformComponent.create()
                .setPosition(16f, 10f, 100f)
                .setRotation(0f)
                .setScale(1f, 1f));
        engine.addEntity(e);

        Entity loading = engine.createEntity();
        loading.add(VelocityComponent.create()
            .setSpeed(-4f, 0f));
        loading.add(RotationComponent.create()
            .setRotationSpeed(90f));
        loading.add(TransformComponent.create()
                .setPosition(16f, 10f, 0f)
                .setScale(1f, 1f));
        loading.add(TextureComponent.create());
        loading.add(StateComponent.create()
                .set("DEFAULT")
                .setLooping(true));
        loading.add(AnimationComponent.create()
                .addAnimation("DEFAULT", new Animation(1f / 12f, Assets.getLoadingFrames())));

        engine.addEntity(loading);
    }

    @Override
    protected void update(float delta) {
        elapsedTime += delta;

        if(Assets.am.update() && elapsedTime >= minSpalshSeconds){
            Gdx.app.log("Splash Screen", "Assets are Loaded!");
            dispatcher.endCurrentScreen();
        }else {
            engine.update(delta);

            //TODO: Juice with a progress bar Component/System
            Gdx.gl20.glLineWidth(1f);
            shapeRenderer.setProjectionMatrix(cam.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.rect(0, 0, cam.viewportWidth * Assets.am.getProgress(), cam.viewportHeight / 10f);
            shapeRenderer.end();
        }
    }
}

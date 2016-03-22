package com.roaringcatgames.libgdxjam.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.MenuItemComponent;
import com.roaringcatgames.libgdxjam.components.ParticleEmitterComponent;
import com.roaringcatgames.libgdxjam.systems.*;
import com.roaringcatgames.libgdxjam.values.GameState;
import com.roaringcatgames.libgdxjam.values.Volume;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * Created by barry on 12/22/15 @ 5:51 PM.
 */
public class MenuScreen extends LazyInitScreen {

    private IScreenDispatcher dispatcher;
    private SpriteBatch batch;
    private PooledEngine engine;
    private OrthographicCamera cam;
    private Vector3 touchPoint;
    private Viewport viewport;

    private Music menuSong;
    private Entity plant, p, l, a, y;

    public MenuScreen(SpriteBatch batch, IScreenDispatcher dispatcher) {
        super();
        this.batch = batch;
        this.dispatcher = dispatcher;
        this.touchPoint = new Vector3();
    }


    @Override
    protected void init() {
        engine = new PooledEngine();

        RenderingSystem renderingSystem = new RenderingSystem(batch, App.PPM);
        cam = renderingSystem.getCamera();
        viewport = new FitViewport(App.W, App.H, cam);//new ExtendViewport(App.W, App.H, App.W*2f, App.H*2f, cam);
        viewport.apply();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        menuSong = Assets.getGameOverMusic();

        Vector3 playerPosition = new Vector3(
                cam.position.x,
                5f,
                Z.player);

        //AshleyExtensions Systems
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RotationSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new MenuStartSystem());

        //Custom Systems
        Vector2 minBounds = new Vector2(0f, 0f);
        Vector2 maxBounds = new Vector2(cam.viewportWidth, cam.viewportHeight);
        engine.addSystem(new ScreenWrapSystem(minBounds, maxBounds, App.PPM));
        engine.addSystem(new BackgroundSystem(minBounds, maxBounds, false));
        engine.addSystem(new PlayerSystem(playerPosition, 1f, cam));
        engine.addSystem(new FiringSystem());
        engine.addSystem(new CleanUpSystem(minBounds, maxBounds));
        engine.addSystem(new RemainInBoundsSystem(minBounds, maxBounds));
        engine.addSystem(new BulletSystem());
        engine.addSystem(new FollowerSystem());
        engine.addSystem(new FadingSystem());
        engine.addSystem(new ParticleSystem());
        //Extension Systems
        engine.addSystem(renderingSystem);
        engine.addSystem(new DebugSystem(renderingSystem.getCamera(), Color.CYAN, Color.PINK, Input.Keys.TAB));

        Entity title = engine.createEntity();
        title.add(TextureComponent.create()
                .setRegion(Assets.getTitleImage()));
        title.add(TransformComponent.create()
                .setPosition(10.8f, 25f));
        engine.addEntity(title);

        plant = engine.createEntity();
        plant.add(StateComponent.create()
                .setLooping(false).set("DEFAULT"));
        plant.add(TextureComponent.create());
        plant.add(AnimationComponent.create()
                .addAnimation("DEFAULT", new Animation(1f / 12f, Assets.getTitleTreeFrames()))
                .addAnimation("LEAF", new Animation(1f / 12f, Assets.getTitleTreeLeafFrames(), Animation.PlayMode.LOOP)));
        plant.add(TransformComponent.create()
                .setPosition(1.7f, 24f));


        float xPos = 4f;
        float yPos = 19f;
        p = engine.createEntity();
        p.add(MenuItemComponent.create());
        p.add(TextureComponent.create());
        p.add(CircleBoundsComponent.create()
            .setCircle(xPos, yPos, 2f));
        p.add(AnimationComponent.create()
            .addAnimation("DEFAULT", new Animation(1f / 6f, Assets.getPFrames()))
            .setPaused(true));
        p.add(TransformComponent.create()
                .setPosition(xPos, yPos)
                .setScale(0.5f, 0.5f));
        p.add(StateComponent.create()
                .set("DEFAULT")
                .setLooping(false));
        engine.addEntity(p);
        xPos += 4f;

        l = engine.createEntity();
        l.add(TextureComponent.create());
        l.add(MenuItemComponent.create());
        l.add(CircleBoundsComponent.create()
                .setCircle(xPos, yPos, 2f));
        l.add(AnimationComponent.create()
                .addAnimation("DEFAULT", new Animation(1f / 6f, Assets.getLFrames()))
                .setPaused(true));
        l.add(TransformComponent.create()
                .setPosition(xPos, yPos)
                .setScale(0.5f, 0.5f));
        l.add(StateComponent.create()
                .set("DEFAULT")
                .setLooping(false));
        engine.addEntity(l);
        xPos += 4f;

        a = engine.createEntity();
        a.add(TextureComponent.create());
        a.add(MenuItemComponent.create());
        a.add(CircleBoundsComponent.create()
                .setCircle(xPos, yPos, 2f));
        a.add(AnimationComponent.create()
                .addAnimation("DEFAULT", new Animation(1f / 6f, Assets.getAFrames()))
                .setPaused(true));
        a.add(TransformComponent.create()
                .setPosition(xPos, yPos)
                .setScale(0.5f, 0.5f));
        a.add(StateComponent.create()
                .set("DEFAULT")
                .setLooping(false));
        engine.addEntity(a);
        xPos += 4f;

        y = engine.createEntity();
        y.add(TextureComponent.create());
        y.add(MenuItemComponent.create());
        y.add(CircleBoundsComponent.create()
                .setCircle(xPos, yPos, 2f));
        y.add(AnimationComponent.create()
                .addAnimation("DEFAULT", new Animation(1f / 6f, Assets.getYFrames()))
                .setPaused(true));
        y.add(TransformComponent.create()
                .setPosition(xPos, yPos)
                .setScale(0.5f, 0.5f));
        y.add(StateComponent.create()
                .set("DEFAULT")
                .setLooping(false));
        engine.addEntity(y);

        engine.addEntity(plant);
    }

    @Override
    public void show() {
        super.show();

        menuSong.setVolume(Volume.MENU_MUSIC);
        menuSong.setLooping(true);
        menuSong.play();
    }

    boolean treeLeafing = false;
    /**************************
     * Screen Adapter Methods
     **************************/
    @Override
    protected void update(float deltaChange) {
        engine.update(Math.min(deltaChange, App.MAX_DELTA_TICK));

        if(!treeLeafing) {
            StateComponent sc = plant.getComponent(StateComponent.class);
            AnimationComponent ac = plant.getComponent(AnimationComponent.class);
            if (ac.animations.get(sc.get()).isAnimationFinished(sc.time)) {
                //sc.set("LEAF").setLooping(true);
                plant.add(ParticleEmitterComponent.create()
                        .setDuration(2f)
                        .setParticleImages(Assets.getLeafFrames())
                        .setShouldFade(true)
                        .setAngleRange(-110f, 110f)
                        .setSpawnRate(20f)
                        .setSpeed(2f)
                        .setShouldLoop(true));
                treeLeafing = true;
            }
        }

        if(isReady(p) && isReady(l) && isReady(a) && isReady(y)){
            menuSong.stop();
            dispatcher.endCurrentScreen();
        }
    }

    private boolean isReady(Entity p){
        return !p.getComponent(AnimationComponent.class).isPaused &&
                p.getComponent(TransformComponent.class).opacity == 0f;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if(viewport != null) {
            viewport.update(width, height);
        }
    }
}

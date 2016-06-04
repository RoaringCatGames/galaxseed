package com.roaringcatgames.libgdxjam.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.MenuItemComponent;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.WhenOffScreenComponent;
import com.roaringcatgames.libgdxjam.systems.*;
import com.roaringcatgames.libgdxjam.values.Health;
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
    private Entity plant, p, l, a, y, swipeTutorial;
    private ObjectMap<String, Boolean> readyMap = new ObjectMap<>();

    public MenuScreen(SpriteBatch batch, IScreenDispatcher dispatcher) {
        super();
        this.batch = batch;
        this.dispatcher = dispatcher;
        this.touchPoint = new Vector3();
        readyMap.put("p", false);
        readyMap.put("l", false);
        readyMap.put("a", false);
        readyMap.put("y", false);
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
                App.W/2f,
                App.H/5f,
                Z.player);



        //AshleyExtensions Systems
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RotationSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new AnimationSystem());

        engine.addSystem(new MenuStartSystem());

        //Custom Systems
        Vector2[] muzzlePositions = new Vector2[]{
                new Vector2(-0.5f, 1.6f),
                //new Vector2(-0.906f, 0.881f),
                new Vector2(0.5f, 1.6f),
                //new Vector2(0.906f, 0.881f)
        };

        Vector2 minBounds = new Vector2(0f, 0f);
        Vector2 maxBounds = new Vector2(cam.viewportWidth, cam.viewportHeight);
        engine.addSystem(new ScreenWrapSystem(minBounds, maxBounds, App.PPM));
        engine.addSystem(new BackgroundSystem(minBounds, maxBounds, false, true));
        engine.addSystem(new CleanUpSystem(minBounds, maxBounds));
        engine.addSystem(new PlayerSystem(playerPosition, 0.5f, cam, muzzlePositions));
        engine.addSystem(new FiringSystem());
        engine.addSystem(new RemainInBoundsSystem(minBounds, maxBounds));
        engine.addSystem(new BulletSystem());
        engine.addSystem(new FollowerSystem(Family.all(MenuItemComponent.class).get()));
        engine.addSystem(new FadingSystem());
        engine.addSystem(new ParticleSystem());
        engine.addSystem(new ShakeSystem());
        engine.addSystem(new MoveToSystem());


        engine.addSystem(new PowerUpSystem());
        engine.addSystem(new TweenSystem());
        engine.addSystem(new WeaponDecorationSystem());

        //Extension Systems
        engine.addSystem(renderingSystem);
        engine.addSystem(new DebugSystem(renderingSystem.getCamera(), Color.CYAN, Color.PINK, Input.Keys.TAB));

        float titleSpeed = 6f;
        Entity galaxTitle = engine.createEntity();
        galaxTitle.add(TextureComponent.create(engine)
                .setRegion(Assets.getGalaxTitleImage()));
        galaxTitle.add(TransformComponent.create(engine)
                .setPosition(-5.1f, 24.6f, Z.title));
        galaxTitle.add(MoveToComponent.create(engine)
                .setSpeed(titleSpeed)
                .setTarget(5.4f, 24.8f, Z.title));
        engine.addEntity(galaxTitle);

        Entity seedTitle = engine.createEntity();
        seedTitle.add(TextureComponent.create(engine)
                .setRegion(Assets.getSeedTitleImage()));
        seedTitle.add(TransformComponent.create(engine)
                .setPosition(24.5f, 24.6f, Z.title));
        seedTitle.add(MoveToComponent.create(engine)
                .setSpeed(titleSpeed)
                .setTarget(14.4f, 23.8f, Z.title));
        engine.addEntity(seedTitle);

        plant = engine.createEntity();
        plant.add(StateComponent.create(engine)
                .setLooping(false).set("DEFAULT"));
        plant.add(TextureComponent.create(engine));
        plant.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", Animations.getTitleTree())
                .addAnimation("LEAF", Animations.getTitleTreeLeaf()));
        plant.add(TransformComponent.create(engine)
                .setPosition(10.4f, 25f, Z.titlePlant)
                .setScale(0.85f, 0.85f));


        float xPos = 3f;
        float yPos = 18f;
        p = createPlayAsteroid(xPos, yPos, Animations.getpMenu());
        engine.addEntity(p);
        xPos += 4.5f;

        l = createPlayAsteroid(xPos, yPos, Animations.getlMenu());
        engine.addEntity(l);
        xPos += 4.5f;

        a = createPlayAsteroid(xPos, yPos, Animations.getaMenu());
        engine.addEntity(a);
        xPos += 4.5f;

        y = createPlayAsteroid(xPos, yPos, Animations.getyMenu());
        engine.addEntity(y);

        swipeTutorial = engine.createEntity();
        swipeTutorial.add(TextureComponent.create(engine));
        swipeTutorial.add(AnimationComponent.create(engine)
            .addAnimation("DEFAULT", Animations.getSwipeTutorial()));
        swipeTutorial.add(StateComponent.create(engine)
            .set("DEFAULT")
            .setLooping(true));
        swipeTutorial.add(TransformComponent.create(engine)
            .setPosition(App.W/2f, 2f, Z.tutorial)
            .setOpacity(0.5f));
        engine.addEntity(swipeTutorial);

        engine.addEntity(plant);
    }

    private Entity createPlayAsteroid(float xPos, float yPos, Animation ani) {
        Entity playAsteroid = engine.createEntity();
        playAsteroid.add(WhenOffScreenComponent.create(engine));
        playAsteroid.add(MenuItemComponent.create(engine));
        playAsteroid.add(HealthComponent.create(engine)
            .setMaxHealth(Health.PlayAsteroid)
            .setHealth(Health.PlayAsteroid));
        playAsteroid.add(TextureComponent.create(engine));
        playAsteroid.add(CircleBoundsComponent.create(engine)
            .setCircle(xPos, yPos, 2f));
        playAsteroid.add(AnimationComponent.create(engine)
            .addAnimation("DEFAULT", ani)
            .setPaused(true));
        playAsteroid.add(TransformComponent.create(engine)
                .setPosition(xPos, yPos, Z.playAsteroids)
                .setScale(0.5f, 0.5f));
        playAsteroid.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(false));
        playAsteroid.add(ShakeComponent.create(engine)
            .setSpeed(6f, 4f)
            .setOffsets(0.4f, 0.8f)
            .setCurrentTime(K2MathUtil.getRandomInRange(0f, 4f)));

        return playAsteroid;
    }

    @Override
    public void show() {
        super.show();

        menuSong.setVolume(Volume.MENU_MUSIC);
        menuSong.setLooping(true);
        menuSong.play();

        App.playerLastPosition.set(App.W/2f, App.H/5f);
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
                plant.add(ParticleEmitterComponent.create(engine)
                        .setDuration(200f)
                        .setParticleLifespans(2f, 3f)
                        .setParticleImages(Assets.getLeafFrames())
                        .setShouldFade(true)
                        .setAngleRange(-110f, 110f)
                        .setSpawnRate(60f)
                        .setParticleMinMaxScale(0.5f, 0.5f)
                        .setSpeed(2f, 4f)
                        .setZIndex(Z.titlePlantLeaves)
                        .setShouldLoop(true));
                treeLeafing = true;
            }
        }

        if(isReady(p, "p") && isReady(l, "l") && isReady(a, "a") && isReady(y, "y")){
            menuSong.stop();
            //FUCKING GROSS. What is this, Unity??
            //TransformComponent tc = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(TransformComponent.class);
            //App.playerLastPosition.set(tc.position.x, tc.position.y);
            dispatcher.endCurrentScreen();
        }
    }

    private boolean isReady(Entity p, String key){
        if(readyMap.get(key)){
            return true;
        }

        ParticleEmitterComponent pec = p.getComponent(ParticleEmitterComponent.class);
        boolean isReady = p.isScheduledForRemoval() || p.getComponents().size() == 0 || (pec != null && pec.elapsedTime > 2f);
        readyMap.put(key, isReady);

        return isReady;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if(viewport != null) {
            viewport.update(width, height);
        }
    }
}

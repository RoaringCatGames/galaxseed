package com.roaringcatgames.galaxseed.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;
import com.roaringcatgames.galaxseed.*;
import com.roaringcatgames.galaxseed.components.Mappers;
import com.roaringcatgames.galaxseed.components.MenuItemComponent;
import com.roaringcatgames.galaxseed.components.WeaponType;
import com.roaringcatgames.galaxseed.components.WhenOffScreenComponent;
import com.roaringcatgames.galaxseed.systems.*;
import com.roaringcatgames.galaxseed.values.Health;
import com.roaringcatgames.galaxseed.values.Z;

/**
 * Screen to bring the player into the game
 */
public class MenuScreen extends LazyInitScreen implements InputProcessor{

    private static final float MAX_FLY_TIME = 0.5f;

    private final Vector2 touchPoint = new Vector2();

    private IGameProcessor game;
    private PooledEngine engine;

    private Entity plant, playTarget, optionsTarget, swipeTutorial, exitButton;
    private ObjectMap<String, Boolean> readyMap = new ObjectMap<>();

    public MenuScreen(IGameProcessor game) {
        super();
        this.game = game;
        readyMap.put("options", false);
        readyMap.put("play", false);
    }


    @Override
    protected void init() {
        engine = new PooledEngine();

        RenderingSystem renderingSystem = new RenderingSystem(game.getBatch(), game.getCamera(), App.PPM);

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
        Vector2 minBounds = new Vector2(0f, 0f);
        Vector2 maxBounds = new Vector2(App.W, App.H);
        engine.addSystem(new ScreenWrapSystem(minBounds, maxBounds, App.PPM));
        engine.addSystem(new BackgroundSystem(minBounds, maxBounds, false, true));
        engine.addSystem(new CleanUpSystem(minBounds, maxBounds));
        engine.addSystem(new PlayerSystem(playerPosition, 0.5f, game, WeaponType.GUN_SEEDS));
        engine.addSystem(new FiringSystem());
        engine.addSystem(new RemainInBoundsSystem(minBounds, maxBounds));
        engine.addSystem(new BulletSystem());
        engine.addSystem(new FollowerSystem(Family.all(MenuItemComponent.class).get()));
        engine.addSystem(new FadingSystem());
        engine.addSystem(new ParticleSystem());
        engine.addSystem(new ShakeSystem());
        engine.addSystem(new MoveToSystem());
        engine.addSystem(new PollenAuraSystem());


        engine.addSystem(new PowerUpSystem());
        engine.addSystem(new TweenSystem());
        engine.addSystem(new WeaponDecorationSystem());

        //Extension Systems
        engine.addSystem(renderingSystem);
        engine.addSystem(new TextRenderingSystem(game.getBatch(), game.getGUICamera(), renderingSystem.getCamera()));
        //engine.addSystem(new DebugSystem(renderingSystem.getCamera(), Color.CYAN, Color.PINK, Input.Keys.TAB));

        float titleSpeed = 6f;
        float galaxX = App.W/2f - 4.6f;
        float galaxY = App.H - 5.2f;
        Entity galaxTitle = engine.createEntity();
        galaxTitle.add(TextureComponent.create(engine)
                .setRegion(Assets.getGalaxTitleImage()));
        galaxTitle.add(TransformComponent.create(engine)
                .setPosition(-5.1f, galaxY -2f, Z.title));
        galaxTitle.add(MoveToComponent.create(engine)
                .setSpeed(titleSpeed)
                .setTarget(galaxX, galaxY, Z.title));
        engine.addEntity(galaxTitle);

        float seedX = App.W/2f + 4.4f;
        float seedY = App.H - 6.2f;
        Entity seedTitle = engine.createEntity();
        seedTitle.add(TextureComponent.create(engine)
                .setRegion(Assets.getSeedTitleImage()));
        seedTitle.add(TransformComponent.create(engine)
                .setPosition(App.W + 4.5f, seedY + 2f, Z.title));
        seedTitle.add(MoveToComponent.create(engine)
                .setSpeed(titleSpeed)
                .setTarget(seedX, seedY, Z.title));
        engine.addEntity(seedTitle);

        float plantX = App.W/2f + 0.4f;
        float plantY = App.H - 5f;
        plant = engine.createEntity();
        plant.add(StateComponent.create(engine)
                .setLooping(false).set("DEFAULT"));
        plant.add(TextureComponent.create(engine));
        plant.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", Animations.getTitleTree())
                .addAnimation("LEAF", Animations.getTitleTreeLeaf()));
        plant.add(TransformComponent.create(engine)
                .setPosition(plantX, plantY, Z.titlePlant)
                .setScale(0.85f, 0.85f));




        float xPos = App.W/2f - 5f;
        float yPos = App.H - 14f;
        playTarget = createPlayAsteroid(xPos, yPos, Assets.getPlayAsteroid());
        engine.addEntity(playTarget);
        optionsTarget = createPlayAsteroid(xPos + 10f, yPos, Assets.getOptionsAsteroid());
        engine.addEntity(optionsTarget);

        if(App.isDesktop()) {
            exitButton = engine.createEntity();
            exitButton.add(MenuItemComponent.create(engine));
            exitButton.add(HealthComponent.create(engine)
                    .setMaxHealth(Health.PlayAsteroid)
                    .setHealth(Health.PlayAsteroid));
            exitButton.add(TextureComponent.create(engine)
                    .setRegion(Assets.getExitButton()));
            exitButton.add(CircleBoundsComponent.create(engine)
                    .setCircle(0f, 0f, 1.5f));
            exitButton.add(TransformComponent.create(engine)
                    .setPosition(App.W - 2f, App.H - 2f, Z.playAsteroids)
                    .setScale(0.5f, 0.5f));
            exitButton.add(ShakeComponent.create(engine)
                    .setSpeed(6f, 4f)
                    .setOffsets(0.4f, 0.6f)
                    .setCurrentTime(K2MathUtil.getRandomInRange(0f, 4f)));
            engine.addEntity(exitButton);
        }

        swipeTutorial = engine.createEntity();
        swipeTutorial.add(TextureComponent.create(engine));
        swipeTutorial.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", Animations.getSwipeTutorial()));
        swipeTutorial.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(true));
        swipeTutorial.add(TransformComponent.create(engine)
                .setPosition(App.W / 2f, 2f, Z.tutorial)
                .setOpacity(0.5f));
        engine.addEntity(swipeTutorial);

        engine.addEntity(plant);
    }

    private Entity createPlayAsteroid(float xPos, float yPos, TextureRegion region) {
        Entity playAsteroid = engine.createEntity();
        playAsteroid.add(WhenOffScreenComponent.create(engine));
        playAsteroid.add(MenuItemComponent.create(engine));
        playAsteroid.add(HealthComponent.create(engine)
            .setMaxHealth(Health.PlayAsteroid)
            .setHealth(Health.PlayAsteroid));
        playAsteroid.add(TextureComponent.create(engine)
            .setRegion(region));
        playAsteroid.add(CircleBoundsComponent.create(engine)
                .setCircle(xPos, yPos, 3f));
        playAsteroid.add(TransformComponent.create(engine)
                .setPosition(xPos, yPos, Z.playAsteroids)
                .setScale(1f, 1f));
        playAsteroid.add(ShakeComponent.create(engine)
                .setSpeed(6f, 4f)
                .setOffsets(0.4f, 0.6f)
                .setCurrentTime(K2MathUtil.getRandomInRange(0f, 4f)));

        return playAsteroid;
    }

    @Override
    public void show() {
        super.show();

        game.addInputProcessor(this);
        game.playBgMusic("MENU");
        App.playerLastPosition.set(App.W/2f, App.H/5f);
    }

    @Override
    public void hide() {
        super.hide();
        game.removeInputProcessor(this);
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
                        .setSpawnRate(2f)
                        .setParticleMinMaxScale(0.5f, 0.5f)
                        .setSpeed(2f, 4f)
                        .setZIndex(Z.titlePlantLeaves)
                        .setShouldLoop(true));
                treeLeafing = true;
            }
        }

        if(isReady(playTarget, "play")){
            game.pauseBgMusic();
            game.switchScreens("GAME");
        }

        if(isReady(optionsTarget, "options")){
            game.switchScreens("OPTIONS");
        }
    }

    private boolean isReady(Entity p, String key){
        if(readyMap.get(key)){
            return true;
        }

        ParticleEmitterComponent pec = p.getComponent(ParticleEmitterComponent.class);
        boolean isReady = p.isScheduledForRemoval() || p.getComponents().size() == 0 || (pec != null && pec.elapsedTime > MAX_FLY_TIME);
        readyMap.put(key, isReady);

        return isReady;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPoint.set(screenX, screenY);
        game.getViewport().unproject(touchPoint);

        CircleBoundsComponent playBounds = K2ComponentMappers.circleBounds.get(playTarget);
        CircleBoundsComponent optionsBounds = K2ComponentMappers.circleBounds.get(optionsTarget);




        if (playBounds != null && playBounds.circle.contains(touchPoint)) {
            if (Mappers.menuItem.has(playTarget)) {
                Mappers.menuItem.get(playTarget).isFilled = true;
                Sfx.playSelectNoise();
            }
        } else if (optionsBounds != null && optionsBounds.circle.contains(touchPoint)) {
            if (Mappers.menuItem.has(optionsTarget)) {
                Mappers.menuItem.get(optionsTarget).isFilled = true;
                Sfx.playSelectNoise();
            }
        }

        if(App.isDesktop()){
            CircleBoundsComponent exitBounds = K2ComponentMappers.circleBounds.get(exitButton);
            if(exitBounds != null && exitBounds.circle.contains(touchPoint)) {
                Gdx.app.exit();
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
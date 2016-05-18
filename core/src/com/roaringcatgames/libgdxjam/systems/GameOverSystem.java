package com.roaringcatgames.libgdxjam.systems;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.BulletComponent;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.WhenOffScreenComponent;
import com.roaringcatgames.libgdxjam.screens.IScreenDispatcher;
import com.roaringcatgames.libgdxjam.values.GameState;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * Created by barry on 3/2/16 @ 8:43 PM.
 */
public class GameOverSystem extends IteratingSystem implements InputProcessor {

    private Entity gameOverText;
    private Entity restartButton;
    private Entity rawry;
    private Vector2[] shipPartVelocities = new Vector2[]{
            new Vector2(-5f, 5f),
            new Vector2(-5f, -3f),
            new Vector2(5f, 4f)
    };
    private Music endSong;
    private OrthographicCamera cam;
    private IScreenDispatcher dispatcher;

    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<AnimationComponent> am;

    private boolean hasInitialized = false;

    public GameOverSystem(OrthographicCamera cam, IScreenDispatcher dispatcher){
        super(Family.all(PlayerComponent.class).get());
        this.am = ComponentMapper.getFor(AnimationComponent.class);
        this.sm = ComponentMapper.getFor(StateComponent.class);
        endSong = Assets.getGameOverMusic();
        this.cam = cam;
        this.dispatcher = dispatcher;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        App.game.multiplexer.addProcessor(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        App.game.multiplexer.removeProcessor(this);
    }
    @Override
    public void setProcessing(boolean processing) {
        super.setProcessing(processing);
        PooledEngine engine = (PooledEngine)getEngine();
        if(processing){
            //Pause all of our bullet animations
            for(Entity bullet:engine.getEntitiesFor(Family.all(BulletComponent.class).get())){
                bullet.getComponent(AnimationComponent.class).setPaused(true);
            }
        }else{

            if(endSong.isPlaying()) {
                endSong.stop();
            }
            if(gameOverText != null) {
                gameOverText.getComponent(TransformComponent.class).setHidden(true);
            }

            if(restartButton != null){
                restartButton.getComponent(TransformComponent.class).setHidden(true);
            }
        }
    }

    @Override
    protected void processEntity(Entity player, float deltaTime) {
        StateComponent sc = sm.get(player);
        TransformComponent tc = K2ComponentMappers.tm.get(player);


        if (App.getState() == GameState.GAME_OVER) {
            AnimationComponent ac = am.get(player);
            String state = "DEFAULT";
            String flameState;
            boolean isLooping = true;
            PooledEngine engine = (PooledEngine)getEngine();

            if (!"DEAD".equals(sc.get())) {
                state = "DEAD";
                isLooping = false;
                flameState = "DEAD";
                sc.set(state);
                sc.setLooping(isLooping);
                //fsc.set(flameState);

                //Spawn Roary

                rawry = engine.createEntity();
                rawry.add(TransformComponent.create(engine)
                        .setPosition(tc.position.x, tc.position.y, Z.player)
                        .setScale(0.1f, 0.1f));
                rawry.add(StateComponent.create(engine)
                        .set("DEFAULT")
                        .setLooping(true));
                rawry.add(AnimationComponent.create(engine)
                        .addAnimation("DEFAULT", Animations.getRawry()));

                Timeline secondTL = Timeline.createSequence()
                        .push(Tween.to(rawry, K2EntityTweenAccessor.POSITION_XY, 8f)
                                .target(20f, 10f).ease(TweenEquations.easeNone))
                        .push(Tween.to(rawry, K2EntityTweenAccessor.POSITION_XY, 8f)
                                .target(10f, 0f).ease(TweenEquations.easeNone))
                        .push(Tween.to(rawry, K2EntityTweenAccessor.POSITION_XY, 8f)
                                .target(-4f, 30f).ease(TweenEquations.easeNone))
                        .repeatYoyo(10, 0f);
                Timeline tl = Timeline.createParallel()
                        .push(Tween.to(rawry, K2EntityTweenAccessor.SCALE, 3f)
                                .target(0.25f, 0.25f).ease(TweenEquations.easeOutSine))
                        .push(Tween.to(rawry, K2EntityTweenAccessor.POSITION_Z, 3f)
                                .target(Z.rawry))
                        .push(Tween.to(rawry, K2EntityTweenAccessor.POSITION_XY, 3f)
                                .target(0f, 25f).ease(TweenEquations.easeNone));
                rawry.add(TweenComponent.create(engine).setTimeline(Timeline.createSequence().push(tl).push(secondTL)));
                rawry.add(TextureComponent.create(engine));
                rawry.add(RotationComponent.create(engine)
                        .setRotationSpeed(45f));
                engine.addEntity(rawry);

                for(int i=0;i<shipPartVelocities.length;i++){
                    Entity shipPart = engine.createEntity();
                    shipPart.add(TextureComponent.create(engine)
                        .setRegion(Assets.getShipPart(i)));
                    shipPart.add(RotationComponent.create(engine)
                        .setRotationSpeed(K2MathUtil.getRandomInRange(720f, 2000f)));
                    shipPart.add(TransformComponent.create(engine)
                        .setPosition(tc.position.x, tc.position.y, tc.position.z)
                        .setScale(tc.scale.x, tc.scale.y));
                    shipPart.add(WhenOffScreenComponent.create(engine));
                    shipPart.add(TweenComponent.create(engine)
                            .addTween(Tween.to(shipPart, K2EntityTweenAccessor.POSITION_XY, 2f)
                                    .target(shipPartVelocities[i].x, shipPartVelocities[i].y)
                                    .ease(TweenEquations.easeOutExpo)));
                    engine.addEntity(shipPart);
                }


            } else if (!hasInitialized && ac.animations.get("DEAD").isAnimationFinished(sc.time)) {
                getEngine().removeEntity(player);

                if(gameOverText == null) {
                    gameOverText = engine.createEntity();
                    gameOverText.add(TransformComponent.create(engine)
                            .setPosition(App.W / 2f, App.H / 2f, Z.gameOver));
                    gameOverText.add(TextComponent.create(engine)
                            .setFont(Assets.get64Font())
                            .setText("GAME OVER"));

                    getEngine().addEntity(gameOverText);
                }

                if(restartButton == null){
                    restartButton = engine.createEntity();
                    restartButton.add(TransformComponent.create(engine)
                            .setPosition(App.W/2f, ((App.H/2f)-2.5f), Z.gameOver));
                    restartButton.add(TextComponent.create(engine)
                            .setFont(Assets.get48Font())
                            .setText("Home"));
                    restartButton.add(BoundsComponent.create(engine)
                            .setBounds(0f, 0f, 5f, 1.5f)
                            .setOffset(0f, -0.75f));

                    getEngine().addEntity(restartButton);
                }
                gameOverText.getComponent(TransformComponent.class).setHidden(false);
                restartButton.getComponent(TransformComponent.class).setHidden(false);
                endSong.play();
                hasInitialized = true;
            }


        }
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

    Vector3 touchPoint = new Vector3();
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(hasInitialized && App.getState() == GameState.GAME_OVER){
            touchPoint.set(screenX, screenY, 0f);
            touchPoint = cam.unproject(touchPoint);

            if (restartButton.getComponent(BoundsComponent.class).bounds.contains(touchPoint.x, touchPoint.y)) {
                App.setState(GameState.MENU);
                dispatcher.endCurrentScreen();
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

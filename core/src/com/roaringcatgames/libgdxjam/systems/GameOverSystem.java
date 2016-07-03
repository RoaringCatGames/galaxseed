package com.roaringcatgames.libgdxjam.systems;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.PrefsUtil;
import com.roaringcatgames.libgdxjam.components.BulletComponent;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.WhenOffScreenComponent;
import com.roaringcatgames.libgdxjam.values.GameState;
import com.roaringcatgames.libgdxjam.values.Volume;
import com.roaringcatgames.libgdxjam.values.Z;

import java.util.Random;

/**
 * Handles the game over resolutions
 */
public class GameOverSystem extends IteratingSystem implements InputProcessor {

    private Random r = new Random();
    private IGameProcessor game;
    private Entity gameOverText;
    private Entity restartButton;
    private Array<Vector2> shipPartEndPositions = new Array<>();

    Array<Vector2> randomEdgePoints = new Array<>();
    //private Music endSong;
    private OrthographicCamera cam;

    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<AnimationComponent> am;

    private boolean hasInitialized = false;

    public GameOverSystem(IGameProcessor game){
        super(Family.all(PlayerComponent.class).get());
        this.game = game;
        this.am = ComponentMapper.getFor(AnimationComponent.class);
        this.sm = ComponentMapper.getFor(StateComponent.class);
        //endSong = Assets.getMenuMusic();
        this.cam = game.getCamera();

        randomEdgePoints.addAll(getRandomEdgePoints(20, false));
        shipPartEndPositions.addAll(getRandomEdgePoints(10, true));

        for(int i=0;i<20;i++){
            if(i%2!=0){
                //sides
                boolean isLeft = r.nextFloat() < 0.5f;
                float x = isLeft ? 0f:App.W;
                randomEdgePoints.add(new Vector2(x, K2MathUtil.getRandomInRange(0f, App.H)));
            }else{
                //top/bottom
                boolean isTop = r.nextFloat() < 0.5f;
                float y = isTop ? 0f:App.H;
                randomEdgePoints.add(new Vector2(K2MathUtil.getRandomInRange(0f, App.W), y));
            }
        }
    }


    private Array<Vector2> getRandomEdgePoints(int numberOfPoints, boolean areOffScreen){
        Array<Vector2> points = new Array<>();
        float adjust = areOffScreen ? 10f :0f;
        for(int i=0;i<numberOfPoints;i++){
            if(i%2==0){
                //sides
                boolean isLeft = r.nextFloat() < 0.5f;
                float x = isLeft ? 0f-adjust:App.W + adjust;
                points.add(new Vector2(x, K2MathUtil.getRandomInRange(0f, App.H)));
            }else{
                //top/bottom
                boolean isTop = r.nextFloat() < 0.5f;
                float y = isTop ? 0f-adjust:App.H+adjust;
                points.add(new Vector2(K2MathUtil.getRandomInRange(0f, App.W), y));
            }
        }
        return points;
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
        TransformComponent tc = K2ComponentMappers.transform.get(player);

        if(App.getState() == GameState.GAME_OVER) {
            if(App.isSlowed()){
                App.setSlowed(false);
            }
            AnimationComponent ac = am.get(player);
            PooledEngine engine = (PooledEngine)getEngine();

            if (!"DEAD".equals(sc.get())) {
                sc.set("DEAD");
                sc.setLooping(false);
                tc.setScale(1f, 1f);

                //Spawn Roary
                Entity rawry = engine.createEntity();
                rawry.add(TransformComponent.create(engine)
                        .setPosition(tc.position.x, tc.position.y, Z.player)
                        .setScale(0.1f, 0.1f));
                rawry.add(StateComponent.create(engine)
                        .set("DEFAULT")
                        .setLooping(true));
                rawry.add(AnimationComponent.create(engine)
                        .addAnimation("DEFAULT", Animations.getRawry()));




                Timeline secondTL = Timeline.createSequence();
                for(Vector2 target:randomEdgePoints) {
                    secondTL.push(Tween.to(rawry, K2EntityTweenAccessor.POSITION_XY, 8f)
                            .target(target.x, target.y).ease(TweenEquations.easeNone));
                }
                secondTL.repeatYoyo(10, 0f);

                Timeline tl = Timeline.createParallel()
                        .push(Tween.to(rawry, K2EntityTweenAccessor.SCALE, 5f)
                                .target(0.25f, 0.25f).ease(TweenEquations.easeOutSine))
                        .push(Tween.to(rawry, K2EntityTweenAccessor.POSITION_Z, 5f)
                                .target(Z.rawry))
                        .push(Tween.to(rawry, K2EntityTweenAccessor.POSITION_XY, 5f)
                                .target(0f, 25f).ease(TweenEquations.easeNone));
                rawry.add(TweenComponent.create(engine).setTimeline(Timeline.createSequence().push(tl).push(secondTL)));
                rawry.add(TextureComponent.create(engine));
                rawry.add(RotationComponent.create(engine)
                        .setRotationSpeed(45f));

                rawry.add(ParticleEmitterComponent.create(engine)
                        .setSpawnRate(1.6f)
                        .setSpeed(2f, 3f)
                        .setParticleLifespans(0.3f, 0.5f)
                        .setAngleRange(120f, 180f)
                        .setDuration(6f)
                        .setSpawnType(ParticleSpawnType.RANDOM_IN_BOUNDS)
                        .setSpawnRange(1f, 1f)
                        .setParticleMinMaxScale(1f, 2f)
                        .setShouldFade(true)
                        .setParticleImage(Assets.getSmoke()));
                engine.addEntity(rawry);


                for(int i=0;i< shipPartEndPositions.size;i++){
                    Entity shipPart = engine.createEntity();
                    shipPart.add(TextureComponent.create(engine)
                            .setRegion(Assets.getShipPart(i)));
                    shipPart.add(RotationComponent.create(engine)
                            .setRotationSpeed(K2MathUtil.getRandomInRange(180f, 270f)));
                    shipPart.add(TransformComponent.create(engine)
                            .setPosition(tc.position.x, tc.position.y, tc.position.z-1f)
                            .setScale(0.5f, 0.5f));
                    shipPart.add(WhenOffScreenComponent.create(engine));
                    shipPart.add(TweenComponent.create(engine)
                            .addTween(Tween.to(shipPart, K2EntityTweenAccessor.POSITION_XY, K2MathUtil.getRandomInRange(5f, 25f))
                                    .target(shipPartEndPositions.get(i).x, shipPartEndPositions.get(i).y)
                                    .ease(TweenEquations.easeOutExpo)));
                    shipPart.add(ParticleEmitterComponent.create(engine)
                            .setSpawnRate(1.6f)
                            .setSpeed(2f, 3f)
                            .setParticleLifespans(0.3f, 0.5f)
                            .setAngleRange(120f, 180f)
                            .setDuration(8f)
                            .setSpawnType(ParticleSpawnType.RANDOM_IN_BOUNDS)
                            .setSpawnRange(0.5f, 0.5f)
                            .setParticleMinMaxScale(1f, 2f)
                            .setShouldFade(true)
                            .setParticleImage(Assets.getSmoke()));
                    engine.addEntity(shipPart);
                }

                if(PrefsUtil.areSfxEnabled()){
                    Assets.getExplosionSfx().play(Volume.EXPLOSION_SFX);
                }

            } else if (!hasInitialized && ac.animations.get("DEAD").isAnimationFinished(sc.time)) {


                BitmapFont largeFont = Gdx.graphics.getDensity() > 1f ? Assets.get128Font() : Assets.get64Font();
                BitmapFont subFont = Gdx.graphics.getDensity() > 1f ? Assets.get64Font() : Assets.get48Font();
                if(gameOverText == null) {
                    gameOverText = engine.createEntity();
                    gameOverText.add(TransformComponent.create(engine)
                            .setPosition(App.W / 2f, App.H / 2f, Z.gameOver));
                    gameOverText.add(TextComponent.create(engine)
                            .setFont(largeFont)
                            .setText("GAME OVER"));

                    getEngine().addEntity(gameOverText);
                }

                if(restartButton == null){
                    restartButton = engine.createEntity();
                    restartButton.add(TransformComponent.create(engine)
                            .setPosition(App.W/2f, ((App.H/2f)-2.5f), Z.gameOver));
                    restartButton.add(TextComponent.create(engine)
                            .setFont(subFont)
                            .setText("RE-LAUNCH"));
                    restartButton.add(BoundsComponent.create(engine)
                            .setBounds(0f, 0f, 8f, 3f)
                            .setOffset(0f, -0.75f));

                    getEngine().addEntity(restartButton);
                }
                gameOverText.getComponent(TransformComponent.class).setHidden(false);
                restartButton.getComponent(TransformComponent.class).setHidden(false);
                game.playBgMusic("GAME_OVER");

                getEngine().removeEntity(player);
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
                game.switchScreens("MENU");
                return true;
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

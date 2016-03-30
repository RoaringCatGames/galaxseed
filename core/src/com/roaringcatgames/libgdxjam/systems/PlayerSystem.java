package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.values.Health;
import com.roaringcatgames.libgdxjam.values.Rates;
import com.roaringcatgames.libgdxjam.values.Volume;
import com.roaringcatgames.libgdxjam.values.Z;
import com.roaringcatgames.libgdxjam.components.*;

import java.util.Vector;

/**
 * Created by barry on 12/29/15 @ 8:07 PM.
 */
public class PlayerSystem extends IteratingSystem implements InputProcessor {

    private Array<Vector2> muzzlePositions = new Array<>();

    private boolean isInitialized = false;
    private Entity player;
    private Entity flames;
    private Vector3 initialPosition;
    private float initialScale;

    private Vector2 controlOrigin;

    private Vector2 idleFlameOffset = new Vector2(0f, -2.6f);
    private Vector2 flyingFlameOffset = new Vector2(0f, -3.25f);

    private Music flyingMusic;

    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;


    private OrthographicCamera cam;

    public PlayerSystem(Vector3 initialPosition, float initialScale, OrthographicCamera cam, Vector2...muzzlePositions){
        super(Family.all(PlayerComponent.class).get());
        this.initialPosition = initialPosition;
        this.initialScale = initialScale;

        this.sm = ComponentMapper.getFor(StateComponent.class);
        this.tm = ComponentMapper.getFor(TransformComponent.class);
        this.cam = cam;

        this.flyingMusic = Assets.getFlyingMusic();

        this.controlOrigin = new Vector2();

        if(muzzlePositions != null && muzzlePositions .length > 0){
            for(Vector2 muzzle:muzzlePositions){
                this.muzzlePositions.add(muzzle);
            }
        }else {
            this.muzzlePositions.add(new Vector2(-0.5f, 1.6f));
            this.muzzlePositions.add(new Vector2(-0.906f, 0.881f));
            this.muzzlePositions.add(new Vector2(-1.312f, 0.3f));

            this.muzzlePositions.add(new Vector2(0.5f, 1.6f));
            this.muzzlePositions.add(new Vector2(0.906f, 0.881f));
            this.muzzlePositions.add(new Vector2(1.312f, 0.3f));
        }
    }

    private void init(){
        if(player == null) {
            PooledEngine engine = (PooledEngine)getEngine();

            player = engine.createEntity();
            flames = engine.createEntity();



            player.add(KinematicComponent.create(engine));
            player.add(PlayerComponent.create(engine));
            player.add(HealthComponent.create(engine)
                .setHealth(Health.Player)
                .setMaxHealth(Health.Player));
            player.add(TransformComponent.create(engine)
                .setPosition(initialPosition.x, initialPosition.y, initialPosition.z)
                .setScale(initialScale, initialScale));

            player.add(BoundsComponent.create(engine)
                    .setBounds(0f, 0f, 1f, 1.5f));

            player.add(TextureComponent.create(engine));
            player.add(AnimationComponent.create(engine)
                    .addAnimation("DEFAULT", new Animation(1f / 9f, Assets.getShipIdleFrames()))
                    .addAnimation("FLYING", new Animation(1f / 12f, Assets.getShipFlyingFrames()))
                    .addAnimation("FLYING_LEFT", new Animation(1f / 6f, Assets.getShipFlyingLeftFrames()))
                    .addAnimation("FLYING_RIGHT", new Animation(1f / 6f, Assets.getShipFlyingRightFrames())));
            player.add(RemainInBoundsComponent.create(engine)
                    .setMode(BoundMode.CONTAINED));
            player.add(StateComponent.create(engine)
                    .set("DEFAULT")
                    .setLooping(true));

            player.add(ShakeComponent.create((PooledEngine) getEngine())
                    .setOffsets(0.25f, 0.25f)
                    .setSpeed(0.05f, 0.05f)
                    .setPaused(true));

            player.add(VelocityComponent.create(engine)
                    .setSpeed(0f, 0f));


            //generateMuzzles
            Animation muzzleAni = new Animation(Rates.timeBetweenShots/6f, Assets.getMuzzleFrames());
            for(Vector2 muzzlePos:muzzlePositions){
                Entity muzzle = engine.createEntity();
                muzzle.add(GunComponent.create(engine));
                muzzle.add(FollowerComponent.create(engine)
                    .setOffset(muzzlePos.x*initialScale, muzzlePos.y*initialScale)
                    .setTarget(player)
                    .setMode(FollowMode.STICKY));
                muzzle.add(TextureComponent.create(engine));
                muzzle.add(AnimationComponent.create(engine)
                    .addAnimation("FIRING", muzzleAni));
                muzzle.add(StateComponent.create(engine)
                    .set("DEFAULT")
                    .setLooping(false));
                muzzle.add(TransformComponent.create(engine)
                    .setPosition(initialPosition.x, initialPosition.y, Z.muzzleFlash)
                    .setScale(initialScale*0.5f, initialScale*0.5f)
                    .setOpacity(0.8f));
                engine.addEntity(muzzle);
            }




            getEngine().addEntity(player);

            flames.add(FollowerComponent.create((PooledEngine)getEngine())
                    .setOffset(idleFlameOffset.x * initialScale, idleFlameOffset.y * initialScale)
                    .setTarget(player)
                    .setMode(FollowMode.STICKY));
            flames.add(TextureComponent.create(engine));
            flames.add(TransformComponent.create(engine)
                    .setPosition(initialPosition.x, initialPosition.y - ((3.25f * initialScale) * initialScale), Z.flames)
                    .setScale(initialScale, initialScale));
            flames.add(AnimationComponent.create(engine)
                    .addAnimation("DEFAULT", new Animation(1f / 9f, Assets.getIdleFlamesFrames()))
                    .addAnimation("FLYING", new Animation(1f / 9f, Assets.getFlamesFrames())));
            flames.add(StateComponent.create(engine)
                    .set("DEFAULT")
                    .setLooping(true));
            getEngine().addEntity(flames);

        }
        isInitialized = true;
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
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(!isInitialized){
            init();
        }

        StateComponent sc = sm.get(player);
        TransformComponent tc = tm.get(player);
        tc.position.add(currentPositionChange);

        /**********************
         * Set Animation State
         **********************/
        String state = "DEFAULT";
        String flameState;
        boolean isLooping = true;
        //right
        if(currentPositionChange.x > 0f) {
            state = "FLYING_RIGHT";
            isLooping = false;
        }else if(currentPositionChange.x < 0f) {
            state = "FLYING_LEFT";
            isLooping = false;
        }else if(currentPositionChange.y != 0f){
            state = "FLYING";
        }

        FollowerComponent fc = flames.getComponent(FollowerComponent.class);
        if(state != "DEFAULT"){
            flameState = "FLYING";
            float x = flyingFlameOffset.x;

            if(state == "FLYING_RIGHT"){
                x -= 0.3f;
            }else if(state == "FLYING_LEFT"){
                x += 0.3f;
            }
            fc.setOffset(x*initialScale, flyingFlameOffset.y * initialScale);
        }else{
            flameState = "DEFAULT";
            fc.setOffset(idleFlameOffset.x * initialScale, idleFlameOffset.y * initialScale);
        }

        if(flameState == "FLYING" && !flyingMusic.isPlaying()){
            flyingMusic.setLooping(true);
            flyingMusic.setVolume(Volume.FLYING_MUSIC);
            flyingMusic.play();
        }else if(flyingMusic.isPlaying()){
            flyingMusic.stop();
        }

        if(sc.get() != state) {
            sc.set(state).setLooping(isLooping);
            StateComponent fsc = sm.get(flames);
            if(fsc.get() != flameState){
                fsc.set(flameState);
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D){
            currentPositionChange.add(0.5f, 0f, 0f);
        }else if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A){
            currentPositionChange.add(-0.5f, 0f, 0f);
        }

        if(keycode == Input.Keys.UP || keycode == Input.Keys.W){
            currentPositionChange.add(0f, 0.5f, 0f);
        }else if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S){
            currentPositionChange.add(0f, -0.5f, 0f);
        }
        return false;
    }



    @Override
    public boolean keyUp(int keycode) {
        if(!Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
            !Gdx.input.isKeyPressed(Input.Keys.D) ||
            !Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
            !Gdx.input.isKeyPressed(Input.Keys.A)
         ) {
            if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D ||
                    keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
                currentPositionChange.set(0f, currentPositionChange.y, currentPositionChange.z);
            }
        }

        if(!Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
           !Gdx.input.isKeyPressed(Input.Keys.S) ||
           !Gdx.input.isKeyPressed(Input.Keys.UP) ||
           !Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (keycode == Input.Keys.UP || keycode == Input.Keys.W ||
                    keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                currentPositionChange.set(currentPositionChange.x, 0f, currentPositionChange.z);
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    Vector3 touchPoint = new Vector3();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if(pointer == 0){
            touchPoint.set(screenX, screenY, 0f);
            touchPoint = cam.unproject(touchPoint);
            controlOrigin.set(touchPoint.x, touchPoint.y);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if(pointer == 0){
            currentPositionChange.set(0f, 0f, 0f);
        }
        return false;
    }

    Vector3 currentPositionChange = new Vector3();
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if(pointer == 0) {
            currentPositionChange.set(screenX, screenY, 0f);
            currentPositionChange = cam.unproject(currentPositionChange);
            Vector3 newTouchPosition = currentPositionChange.cpy();

            currentPositionChange.sub(touchPoint);
            touchPoint.set(newTouchPosition);
        }
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

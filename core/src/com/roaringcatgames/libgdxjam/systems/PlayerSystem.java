package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;

/**
 * Created by barry on 12/29/15 @ 8:07 PM.
 */
public class PlayerSystem extends IteratingSystem implements InputProcessor {

    private boolean isInitialized = false;
    private Entity player;
    private Vector3 initialPosition;
    private float initialScale;

    private float deceleration = 2f;
    private float maxVelocity = 4f;
    private float accelerationX = 0f;
    private float accelerationY = 0f;

    private Vector2 controlOrigin;

    private ComponentMapper<VelocityComponent> vm;
    private ComponentMapper<StateComponent> sm;

    private OrthographicCamera cam;

    public PlayerSystem(Vector3 initialPosition, float initialScale, OrthographicCamera cam){
        super(Family.all(PlayerComponent.class).get());
        this.initialPosition = initialPosition;
        this.initialScale = initialScale;
        this.vm = ComponentMapper.getFor(VelocityComponent.class);
        this.sm = ComponentMapper.getFor(StateComponent.class);
        this.cam = cam;

        this.controlOrigin = new Vector2();
    }

    private void init(){
        if(player == null) {
            if (getEngine() instanceof PooledEngine) {
                player = ((PooledEngine) getEngine()).createEntity();
            } else {
                player = new Entity();
            }

            player.add(KinematicComponent.create());
            player.add(PlayerComponent.create());
            player.add(TransformComponent.create()
                    .setPosition(initialPosition.x, initialPosition.y, initialPosition.z)
                    .setScale(initialScale, initialScale));

            player.add(BoundsComponent.create()
                    .setBounds(0f, 0f, 2f, 3f));

            player.add(TextureComponent.create());
            player.add(AnimationComponent.create()
                    .addAnimation("DEFAULT", new Animation(1f / 9f, Assets.getShipIdleFrames()))
                    .addAnimation("FLYING", new Animation(1f / 12f, Assets.getShipFlyingFrames())));
            player.add(StateComponent.create()
                .set("DEFAULT")
                .setLooping(true));
            player.add(VelocityComponent.create()
                    .setSpeed(0f, 0f));

            getEngine().addEntity(player);
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

        VelocityComponent vc = vm.get(player);
        StateComponent sc = sm.get(player);

        float newX = vc.speed.x;
        float newY = vc.speed.y;
        if(accelerationX != 0f && Math.abs(newX) < maxVelocity){
            newX = applyAcceleration(deltaTime, newX, accelerationX);
        }else if(newX != 0f){
            newX = applyDeceleration(deltaTime, newX);
        }

        //Y Accel
        if(accelerationY != 0f && Math.abs(newY) < maxVelocity){
            newY = applyAcceleration(deltaTime, newY, accelerationY);
        }else if(newY != 0f){
            newY = applyDeceleration(deltaTime, newY);
        }

        vc.speed.set(newX, newY);

        //Swap to Flying if animation if Moving
        if(sc.get() != "FLYING" && (newX != 0f || newY != 0f)) {
            sc.set("FLYING").setLooping(true);
        }

        if(newX == 0f && newY == 0f){
            sc.set("DEFAULT");
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D){
            accelerationX = 20f;
        }else if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A){
            accelerationX = -20f;
        }

        if(keycode == Input.Keys.UP || keycode == Input.Keys.W){
            accelerationY = 20f;
        }else if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S){
            accelerationY = -20f;
        }
        return false;
    }



    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D ||
           keycode == Input.Keys.LEFT || keycode == Input.Keys.A){
            accelerationX = 0f;
        }
        if(keycode == Input.Keys.UP || keycode == Input.Keys.W ||
           keycode == Input.Keys.DOWN || keycode == Input.Keys.S){
            accelerationY = 0f;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    Vector3 touchPoint = new Vector3();
    Vector3 dragPoint = new Vector3();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        touchPoint.set(screenX, screenY, 0f);
        touchPoint = cam.unproject(touchPoint);
        controlOrigin.set(touchPoint.x, touchPoint.y);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        accelerationX = 0f;
        accelerationY = 0f;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        dragPoint.set(screenX, screenY, 0f);
        dragPoint = cam.unproject(dragPoint);

        if(dragPoint.x > controlOrigin.x){
            accelerationX = 20f;
        }else if(dragPoint.x < controlOrigin.x){
            accelerationX = -20f;
        }

        if(dragPoint.y > controlOrigin.y){
            accelerationY = 20f;
        }else if(dragPoint.y < controlOrigin.y){
            accelerationY = -20f;
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


    /************************
     * Private Methods
     ************************/
    private float applyDeceleration(float deltaTime, float inputSpeed) {
        float newSpeed;
        boolean isReverse = inputSpeed < 0f;
        float adjust = !isReverse ?  -deceleration *deltaTime : deceleration *deltaTime;
        newSpeed = inputSpeed + adjust;
        newSpeed =  isReverse ? Math.min(0f, newSpeed) : Math.max(0f, newSpeed);
        return newSpeed;
    }

    private float applyAcceleration(float deltaTime, float inputSpeed, float acceleration) {
        float newSpeed;
        float adjust = acceleration *deltaTime;
        newSpeed = inputSpeed + adjust;
        newSpeed = newSpeed > 0 ? Math.min(maxVelocity, newSpeed) : Math.max(-maxVelocity, newSpeed);
        return newSpeed;
    }
}

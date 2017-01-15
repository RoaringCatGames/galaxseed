package com.roaringcatgames.galaxseed.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.components.CameraComponent;
import com.roaringcatgames.galaxseed.components.Mappers;
import com.roaringcatgames.galaxseed.values.Z;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.TextureComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;

/**
 * Updates Entities' Camera Components by moving their camera positions to match the Transform
 *  position of the entity.
 */
public class CameraPanningSystem extends IteratingSystem implements GestureDetector.GestureListener{
    private Vector2 minBounds, maxBounds;
    private IGameProcessor game;
    private GestureDetector gestureDetector;
    private Vector2 positionShift = new Vector2(0f, 0f);
    private Vector2 currentPositionChange = new Vector2(0f, 0f);

    private Entity cameraEntity;

    private float velocityX = 0f, velocityY = 0f;

    private float ppm = 1f,
                  decelerationRate = 2f,
                  panScale = 1f,
                  decelerationModifier = 1f;

    private float PAN_THRESHOLD = 0.2f; //1/4 second
    private float panTimeElapsed = 0f;
    private boolean isPanning = false;
    private boolean panFiredThisFrame = false;

    /**
     * Creates a system that will add an Entity used to track the camera, and allow it to be
     *  controlled via pan and fling.
     *
     * @param minBounds - The minimum x,y coordinates that the camera can be panned too.
     * @param maxBounds - The maximum x,y coordinates that the camera can be panned too.
     * @param game - the IGame Processor that is needed to get access to the current camera.
     * @param movementRates - optional array of floats. You can provide adjustment values for panning and flinging
     *                      index 0: decelerationRate (default is 2f) the velocity/second to reduce fling velocity
     *                      index 1: panThrottle (default is 1f) the scale adjustment to be applied to any pan deltas
     */
    public CameraPanningSystem(Vector2 minBounds, Vector2 maxBounds, IGameProcessor game, float pixelsPerMeter, float...movementRates){
        super(Family.all(CameraComponent.class, TransformComponent.class).get());
        this.minBounds = minBounds;
        this.maxBounds = maxBounds;
        this.game = game;
        this.ppm = pixelsPerMeter;
        gestureDetector = new GestureDetector(this);
        if(movementRates != null && movementRates.length > 0){
            decelerationRate = movementRates[0];
            if(movementRates.length > 1){
                panScale = movementRates[1];
            }
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        if(cameraEntity == null){
            addCameraEntity();
        }
        this.game.addInputProcessor(gestureDetector);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        this.game.removeInputProcessor(gestureDetector);

        engine.removeEntity(cameraEntity);
        cameraEntity = null;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        CameraComponent cc = Mappers.camera.get(cameraEntity);
        TransformComponent tc = K2ComponentMappers.transform.get(cameraEntity);

        if(panFiredThisFrame){
            panTimeElapsed += deltaTime;
        }

        if(panTimeElapsed > PAN_THRESHOLD){
            isPanning = true;
        }

        positionShift.set(0f, 0f);
        decelerationModifier = decelerationRate * deltaTime;

        if(!isPanning && (velocityX != 0f || velocityY != 0f)) {
            velocityX = clampTowardsZero(velocityX, decelerationModifier);
            velocityY = clampTowardsZero(velocityY, decelerationModifier);

            positionShift.add(velocityX * deltaTime, velocityY * deltaTime);
        }else{
            positionShift.add(currentPositionChange.x, currentPositionChange.y).scl(panScale);
            currentPositionChange.set(0f, 0f);
        }

        tc.position.add(positionShift.x, positionShift.y, 0f);
        tc.position.x = MathUtils.clamp(tc.position.x, minBounds.x, maxBounds.x);
        tc.position.y = MathUtils.clamp(tc.position.y, minBounds.y, maxBounds.y);

        cc.camera.position.set(tc.position.x, tc.position.y, tc.position.z);

        if (isPanning && !panFiredThisFrame) {
            isPanning = false;
            panTimeElapsed = 0f;
        }

        //Reset state for next frame
        panFiredThisFrame = false;
    }

    private float clampTowardsZero(float value, float modifier){
        if(value > 0f){
            return MathUtils.clamp(value - modifier, 0f, value);
        }else{
            return MathUtils.clamp(value + modifier, value, 0f);
        }
    }

    /*
     * Entity Builders
     */
    private void addCameraEntity() {
        cameraEntity = getEngine().createEntity();
        cameraEntity.add(TransformComponent.create(getEngine())
                .setPosition(App.W / 2f, App.H / 2f, Z.player)
                .setRotation(30f));
        cameraEntity.add(TextureComponent.create(getEngine())
                .setRegion(Assets.getArtCat()));
        cameraEntity.add(CameraComponent.create(getEngine())
                .setCamera(game.getCamera()));
        getEngine().addEntity(cameraEntity);
    }

    private void log(String msg){
        Gdx.app.log("CameraPanningSystem", msg);
    }

    /*
     * GestureListener Implementation
     */
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
       // log("Touchdown Fired");
        Vector3 touch = new Vector3(x, y, 0);
        this.game.getViewport().unproject(touch);
        log("X: " + touch.x + " Y: " + touch.y);

        velocityX = 0f;
        velocityY = 0f;
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        //log("Tap");
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        //log("LongPress Fired");
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        //log("Fling");
        if(!isPanning) {
            this.velocityX = velocityX / ppm;
            this.velocityY = velocityY / ppm;
            return true;
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        //log("Pan");
        currentPositionChange.set(deltaX/ppm, deltaY/ppm);

        panFiredThisFrame = true;

        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        //log("PanStop");
        currentPositionChange.set(0f, 0f);
        velocityX = 0f;
        velocityY = 0f;
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        //log("Zoom");
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        //log("Pinch");
        return false;
    }

    @Override
    public void pinchStop() {
        //log("PinchStop");
    }
}

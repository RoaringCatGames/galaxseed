package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
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

    private float decel = 1f;
    private float maxVelocity = 5f;
    private float acceleration = 0f;

    private ComponentMapper<VelocityComponent> vm;
    private ComponentMapper<StateComponent> sm;

    public PlayerSystem(Vector3 initialPosition, float initialScale){
        super(Family.all(PlayerComponent.class).get());
        this.initialPosition = initialPosition;
        this.initialScale = initialScale;
        this.vm = ComponentMapper.getFor(VelocityComponent.class);
        this.sm = ComponentMapper.getFor(StateComponent.class);
    }

    private void init(){
        if(player == null) {
            if (getEngine() instanceof PooledEngine) {
                player = ((PooledEngine) getEngine()).createEntity();
            } else {
                player = new Entity();
            }

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
            Gdx.app.log("PlayerSystem", "Player Initializing!");
            init();
        }


        VelocityComponent vc = vm.get(player);
        StateComponent sc = sm.get(player);
        if(acceleration != 0f && Math.abs(vc.speed.x) < maxVelocity){

            if(sc.get() != "FLYING") {
                sc.set("FLYING").setLooping(true);
            }
            float adjust = acceleration*deltaTime;
            float newXSpeed = vc.speed.x + adjust;
            newXSpeed = newXSpeed > 0 ? Math.min(maxVelocity, newXSpeed) : Math.max(-maxVelocity, newXSpeed);
            vc.speed.set(newXSpeed, vc.speed.y);

        }else if(vc.speed.x != 0f){
            boolean isLeft = vc.speed.x < 0f;
            //Decelerate
            float adjust = !isLeft ?  -decel*deltaTime : decel*deltaTime;
            float newSpeed = vc.speed.x + adjust;
            newSpeed =  isLeft ? Math.min(0f, newSpeed) : Math.max(0f, newSpeed);
            vc.speed.set(newSpeed, vc.speed.y);
        }

        if(vc.speed.x == 0f){
            sc.set("DEFAULT");
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

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
        if(screenX < Gdx.graphics.getWidth()/2f) {
            acceleration = -20f;
        }else {
            acceleration = 20f;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        acceleration = 0f;
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

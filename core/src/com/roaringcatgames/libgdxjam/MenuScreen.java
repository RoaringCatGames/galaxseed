package com.roaringcatgames.libgdxjam;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.*;

/**
 * Created by barry on 12/22/15 @ 5:51 PM.
 */
public class MenuScreen extends LazyInitScreen {

    private IScreenDispatcher dispatcher;
    private SpriteBatch batch;

    public MenuScreen(SpriteBatch batch, IScreenDispatcher dispatcher) {
        super();
        this.batch = batch;
        this.dispatcher = dispatcher;
    }

    PooledEngine engine;
    Entity testEntity;
    @Override
    void init() {
        engine = new PooledEngine();

        RenderingSystem renderingSystem = new RenderingSystem(batch, App.PPM);
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RotationSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new GravitySystem(new Vector2(0.2f, -9.8f)));
        engine.addSystem(renderingSystem);
        engine.addSystem(new DebugSystem(renderingSystem.getCamera(), Color.CYAN, Color.PINK));

        Vector3 position = new Vector3(5f, 5f, 0f);
        testEntity = engine.createEntity();
        testEntity.add(TransformComponent.create()
                .setPosition(position.x, position.y, position.z)
                .setRotation(15f)
                .setScale(0.5f, 0.5f));
        testEntity.add(TextureComponent.create()
                .setRegion(Assets.getMoon()));
        testEntity.add(VelocityComponent.create()
                .setSpeed(-0.5f, 10f));
        testEntity.add(BoundsComponent.create()
                .setBounds(position.x - 5f, position.y - 5f, 1f, 1f)
                .setOffset(3f, 3f));
        testEntity.add(RotationComponent.create()
                .setRotationSpeed(200f)
                .setTargetRotation(4000f)
                .setHasTargetRotation(true));
        engine.addEntity(testEntity);

        //TOTAL HACK
        for(int i = 0;i<20;i++){
            for(int j = 0; j<20;j++){
                float x = 8f + i*16f;
                float y = 8f + j*16f;
                Entity e = engine.createEntity();
                e.add(TextureComponent.create()
                    .setRegion(Assets.getBgTile()));
                e.add(TransformComponent.create()
                    .setPosition(x, y, 1f)
                    .setScale(1f, 1f));
                engine.addEntity(e);
            }
        }
    }

    @Override
    void update(float deltaChange) {
        engine.update(Gdx.graphics.getDeltaTime());
        if(testEntity.getComponent(TransformComponent.class).position.y <= 0f){
            testEntity.getComponent(VelocityComponent.class).setSpeed(0f, 15f);
        }
    }
}

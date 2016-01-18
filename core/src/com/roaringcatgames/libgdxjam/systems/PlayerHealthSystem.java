package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.HealthComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.Z;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;

/**
 * Created by barry on 1/17/16 @ 9:58 PM.
 */
public class PlayerHealthSystem extends IteratingSystem {

    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<BoundsComponent> bm;
    private Entity healthBar;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private Entity player;
    private Color healthColor;
    private boolean isInitialized = false;

    public PlayerHealthSystem(OrthographicCamera cam){
        super(Family.all(PlayerComponent.class).get());
        hm = ComponentMapper.getFor(HealthComponent.class);
        bm = ComponentMapper.getFor(BoundsComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        this.cam = cam;
        shapeRenderer = new ShapeRenderer();

        healthColor = new Color(0f, 200f, 50f, 0.8f);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(!isInitialized){
            init();
        }

        Gdx.gl20.glLineWidth(3f);
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        //OuterBar
        HealthComponent hc = hm.get(player);
        TransformComponent tc = tm.get(healthBar);
        BoundsComponent bc = bm.get(healthBar);
        shapeRenderer.rect(bc.bounds.x, bc.bounds.y, bc.bounds.width, bc.bounds.height);
        shapeRenderer.end();

        Gdx.gl20.glLineWidth(1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(healthColor);
        //InnerBar
        float width = bc.bounds.width * (hc.health/hc.maxHealth);
        shapeRenderer.rect(bc.bounds.x, bc.bounds.y, width, bc.bounds.height);
        shapeRenderer.end();
    }

    private void init(){
        healthBar = ((PooledEngine)getEngine()).createEntity();
        healthBar.add(TransformComponent.create()
            .setPosition(10f, 0.6f, Z.health));
        healthBar.add(BoundsComponent.create()
            .setBounds(0.1f, 0.1f, 19.8f, 1f));
        getEngine().addEntity(healthBar);
        isInitialized = true;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        player = entity;
    }
}

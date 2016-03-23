package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.HealthComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.values.GameState;
import com.roaringcatgames.libgdxjam.values.Z;
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
    private Color flashColor;
    private boolean isInitialized = false;
    private float lastHealth = 0f;

    public PlayerHealthSystem(OrthographicCamera cam){
        super(Family.all(PlayerComponent.class).get());
        hm = ComponentMapper.getFor(HealthComponent.class);
        bm = ComponentMapper.getFor(BoundsComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        this.cam = cam;
        shapeRenderer = new ShapeRenderer();

        healthColor = new Color(255f, 0f, 0f, 0.4f);
        flashColor = new Color(255f, 255f, 255f, 0.6f);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(!isInitialized){
            init();
        }

        HealthComponent hc = hm.get(player);
        TransformComponent tc = tm.get(healthBar);
        BoundsComponent bc = bm.get(healthBar);

        if(hc.health <= 0f){
            App.setState(GameState.GAME_OVER);
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl20.glLineWidth(1f);
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


        if(lastHealth != hc.health) {
            shapeRenderer.setColor(flashColor);
        }else{
            shapeRenderer.setColor(healthColor);
        }

        //InnerBar
        float width = bc.bounds.width * (hc.health/hc.maxHealth);
        shapeRenderer.rect(bc.bounds.x, bc.bounds.y, width, bc.bounds.height);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        lastHealth = hc.health;
    }

    private void init(){
        healthBar = ((PooledEngine)getEngine()).createEntity();
        healthBar.add(TransformComponent.create(getEngine())
            .setPosition(App.W/2f, App.H-0.6f, Z.health));
        healthBar.add(BoundsComponent.create(getEngine())
            .setBounds(0.1f, 0.1f, 19.8f, 1f));
        getEngine().addEntity(healthBar);
        isInitialized = true;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        player = entity;
    }
}

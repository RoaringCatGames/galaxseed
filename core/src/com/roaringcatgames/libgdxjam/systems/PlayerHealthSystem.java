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
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.HealthComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
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
    private boolean isInitialized = false;

    Vector2 v1 = new Vector2();
    Vector2 v2 = new Vector2();
    private Bezier<Vector2> path;

    public PlayerHealthSystem(OrthographicCamera cam){
        super(Family.all(PlayerComponent.class).get());
        hm = ComponentMapper.getFor(HealthComponent.class);
        bm = ComponentMapper.getFor(BoundsComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        this.cam = cam;
        shapeRenderer = new ShapeRenderer();

        healthColor = new Color(0f, 200f, 50f, 0.8f);

        path = new Bezier<Vector2>(new Vector2(0, 0), new Vector2(10, 10), new Vector2(20, 30));
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

        Gdx.gl20.glLineWidth(3f);
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        //OuterBar

        shapeRenderer.rect(bc.bounds.x, bc.bounds.y, bc.bounds.width, bc.bounds.height);
        shapeRenderer.end();

        Gdx.gl20.glLineWidth(1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(healthColor);
        //InnerBar
        float width = bc.bounds.width * (hc.health/hc.maxHealth);
        shapeRenderer.rect(bc.bounds.x, bc.bounds.y, width, bc.bounds.height);
        shapeRenderer.end();


//        float k = 100f;
//        Vector2 p0 = new Vector2(0, 0);
//        Vector2 p1 = new Vector2(20, 30);
//        Vector2 p2 = new Vector2(15, 25);
//        Vector2 p3 = new Vector2(1, 1);
//        Vector2 tmp = new Vector2();
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        for(float i = 0; i < k-1; ++i)
//        {
//
//            Bezier.quadratic(v1, (i/k), p0, p1, p2, tmp);
//            Bezier.quadratic(v2, ((i+1f)/k), p0, p1, p2, tmp);
////            path.valueAt(v1, (i/k)*1);
////            path.valueAt(v2, (i+1/k)*1);
////            path.cubic_derivative(v1, (i/k)*1, p0, p1, p2, p3,tmp);
////            path.cubic_derivative(v2, (i+1f/k)*1, p0, p1, p2, p3, tmp);
//            //Gdx.app.log("PLAYER HEALTH SYSTEM", "At Time: " + (i/k) + " V1:" + v1.x + "," + v1.y + " V2: " + v2.x + "," + v2.y);
//            shapeRenderer.line(v1, v2);
//        }
//        shapeRenderer.end();
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

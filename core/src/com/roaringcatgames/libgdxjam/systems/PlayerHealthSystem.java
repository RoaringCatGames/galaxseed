package com.roaringcatgames.libgdxjam.systems;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
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
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.HealthLeafComponent;
import com.roaringcatgames.libgdxjam.components.WhenOffScreenComponent;
import com.roaringcatgames.libgdxjam.values.GameState;
import com.roaringcatgames.libgdxjam.values.Health;
import com.roaringcatgames.libgdxjam.values.Z;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;

/**
 * Created by barry on 1/17/16 @ 9:58 PM.
 */
public class PlayerHealthSystem extends IteratingSystem {

    private Vector2[] leafPositions = new Vector2[] {
        new Vector2(18f, 28f),
        new Vector2(17f, 28.5f),
        new Vector2(16f, 29f),
        new Vector2(14f, 28.5f),
        new Vector2(13f, 28f),
        new Vector2(12f, 27.5f)
    };

    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<BoundsComponent> bm;
    private Entity healthBar;
    private Array<Entity> leaves;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private Entity player;
    private Color healthColor;
    private Color flashColor;
    private boolean isInitialized = false;
    private float lastHealth = 0f;
    private float healthPerLeaf = 0f;

    public PlayerHealthSystem(OrthographicCamera cam){
        super(Family.all(PlayerComponent.class).get());
        hm = ComponentMapper.getFor(HealthComponent.class);
        bm = ComponentMapper.getFor(BoundsComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        this.cam = cam;
        shapeRenderer = new ShapeRenderer();

        healthColor = new Color(255f, 0f, 0f, 0.4f);
        flashColor = new Color(255f, 255f, 255f, 0.6f);
        leaves = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(!isInitialized){
            init();
        }

        HealthComponent hc = hm.get(player);

        if(hc.health <= 0f){
            App.setState(GameState.GAME_OVER);
        }

        int remaingLeaves = (int)Math.ceil(hc.health/healthPerLeaf);

        while(remaingLeaves < leaves.size){
            Entity leaf = leaves.pop();
            TransformComponent tc = tm.get(leaf);
            Timeline tl = Timeline.createSequence()
                    .push(Tween.to(leaf, K2EntityTweenAccessor.POSITION_X, 0.5f)
                               .target(tc.position.x + K2MathUtil.getRandomInRange(-1.2f, 1.2f)))
                    .push(Tween.to(leaf, K2EntityTweenAccessor.POSITION_X, 1f)
                               .target(tc.position.x + K2MathUtil.getRandomInRange(-1.2f, 1.2f)))
                    .repeatYoyo(Tween.INFINITY, 0f);
            leaf.add(TweenComponent.create(getEngine())
                .setTimeline(tl)
                .addTween(Tween.to(leaf, K2EntityTweenAccessor.OPACITY, 2f).target(0f)));
            leaf.add(VelocityComponent.create(getEngine()).setSpeed(0f, -5f));
            leaf.add(WhenOffScreenComponent.create(getEngine()));
        }

//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        Gdx.gl20.glLineWidth(1f);
//        shapeRenderer.setProjectionMatrix(cam.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//
//
//        if(lastHealth != hc.health) {
//            shapeRenderer.setColor(flashColor);
//        }else{
//            shapeRenderer.setColor(healthColor);
//        }
//
//        //InnerBar
//        float width = bc.bounds.width * (hc.health/hc.maxHealth);
//        shapeRenderer.rect(bc.bounds.x, bc.bounds.y, width, bc.bounds.height);
//        shapeRenderer.end();
//        Gdx.gl.glDisable(GL20.GL_BLEND);
//        lastHealth = hc.health;
    }

    private void init(){
        healthPerLeaf = Health.Player/leafPositions.length;

        healthBar = ((PooledEngine)getEngine()).createEntity();
        healthBar.add(TransformComponent.create(getEngine())
            .setPosition(App.W / 2f, App.H - 1f, Z.health)
            .setOpacity(0.9f));
        healthBar.add(TextureComponent.create(getEngine())
            .setRegion(Assets.getBranch()));
        healthBar.add(BoundsComponent.create(getEngine())
                .setBounds(0.1f, 0.1f, 19.8f, 1f));
        getEngine().addEntity(healthBar);

        for(Vector2 pos:leafPositions){
            Entity leaf = ((PooledEngine)getEngine()).createEntity();
            leaf.add(HealthLeafComponent.create(getEngine()));
            leaf.add(TransformComponent.create(getEngine())
                .setPosition(pos.x, pos.y, Z.healthLeaf)
                .setRotation(15f));
            leaf.add(StateComponent.create(getEngine())
                .set("DEFAULT")
                .setLooping(false));
            leaf.add(AnimationComponent.create(getEngine())
                .addAnimation("DEFAULT", Animations.getHealthLeaf()));
            leaf.add(TextureComponent.create(getEngine()));
            getEngine().addEntity(leaf);
            leaves.add(leaf);
        }

        isInitialized = true;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        player = entity;
    }
}

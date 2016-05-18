package com.roaringcatgames.libgdxjam.systems;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.HealthLeafComponent;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.WhenOffScreenComponent;
import com.roaringcatgames.libgdxjam.values.GameState;
import com.roaringcatgames.libgdxjam.values.Health;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * Manages the player health and the corresponding TreeBranch and Leaf entities
 * as the player loses and gains health.
 *
 * Triggres the GameOver state when health reaches 0
 */
public class PlayerHealthSystem extends IteratingSystem implements InputProcessor {

    private Vector2[] initialPositions = new Vector2[]{
        new Vector2(2.38f, 27.349998f),
        new Vector2(5.039999f, 28.810001f),
        new Vector2(5.659997f, 28.059992f),
        new Vector2(9.3600025f, 29.169994f),
        new Vector2(9.830007f, 27.780014f),
        new Vector2(11.049985f, 27.910002f),
        new Vector2(16.75f, 28.270004f),
        new Vector2(17.21998f, 29.439981f),
        new Vector2(18.109976f, 29.759974f),
        new Vector2(18.599987f, 28.320002f)
    };
    private Vector2[] leafPositions = new Vector2[] {
        new Vector2(2.75f, 27f),
        new Vector2(5.75f, 28.77f),
        new Vector2(6.46f, 27.989994f),
        new Vector2(10.169995f, 29.049992f),
        new Vector2(10.040005f, 27.18f),
        new Vector2(11.55998f, 27.36999f),
        new Vector2(17.050007f, 28.09f),
        new Vector2(17.63999f, 29.049973f),
        new Vector2(18.649988f, 29.459967f),
        new Vector2(19.049997f, 28.079996f)
            /**
             * LEAF POSITION: 0: (2.75, 27.0)
             LEAF POSITION: 1: (5.75, 28.77)
             LEAF POSITION: 2: (6.46, 27.989994)
             LEAF POSITION: 3: (10.169995, 29.049992)
             LEAF POSITION: 4: (10.040005, 27.18)
             LEAF POSITION: 5: (11.559998, 27.369999)
             LEAF POSITION: 6: (17.050007, 28.09)
             LEAF POSITION: 7: (17.63999, 29.049973)
             LEAF POSITION: 8: (18.649988, 29.459967)
             LEAF POSITION: 9: (19.049997, 28.079996)
             */
    };

    private Vector2[] originOffsets = new Vector2[]{
        new Vector2(6f, 28f).scl(1f/32f),
        new Vector2(2f, 18f).scl(1f/32f),
        new Vector2(1f, 16f).scl(1f/32f),
        new Vector2(1f, 22f).scl(1f/32f),
        new Vector2(12f, 46f).scl(1f/32f),
        new Vector2(1f, 40f).scl(1f/32f),
        new Vector2(4f, 17f).scl(1f/32f),
        new Vector2(1f, 28f).scl(1f/32f),
        new Vector2(1f, 27f).scl(1f/32f),
        new Vector2(1f, 20f).scl(1f/32f)
    };

    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<TransformComponent> tm;
    private Entity healthBar;
    private Array<Entity> leaves;
    private Entity player;
    private boolean isInitialized = false;
    private float healthPerLeaf = 0f;

    float tweenTime = 2f;
    float delaySeconds = 0.1f;

    public PlayerHealthSystem(OrthographicCamera cam){
        super(Family.all(PlayerComponent.class).get());
        hm = ComponentMapper.getFor(HealthComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        leaves = new Array<>();
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

        if(player != null) {
            HealthComponent hc = hm.get(player);

            if(hc != null) {
                if (hc.health <= 0f && App.getState() != GameState.GAME_OVER) {
                    healthBar.add(TweenComponent.create(getEngine())
                            .addTween(Tween.to(healthBar, K2EntityTweenAccessor.COLOR, 4f)
                                    .target(Color.LIGHT_GRAY.r, Color.LIGHT_GRAY.g, Color.LIGHT_GRAY.b)));
                    App.setState(GameState.GAME_OVER);
                }

                int remainingLeaves = (int) Math.ceil(hc.health / healthPerLeaf);

                if (remainingLeaves > leaves.size && leaves.size < leafPositions.length) {
                    for (int i = 0; i < (remainingLeaves - leaves.size); i++) {
                        addLeaf(leaves.size, 0f);
                    }
                }


                while (remainingLeaves < leaves.size) {
                    Entity leaf = leaves.pop();
                    TransformComponent tc = tm.get(leaf);
                    Timeline tl = Timeline.createSequence()
                            .push(Tween.to(leaf, K2EntityTweenAccessor.POSITION_X, 0.5f)
                                    .target(tc.position.x + K2MathUtil.getRandomInRange(-1.2f, 1.2f)))
                            .push(Tween.to(leaf, K2EntityTweenAccessor.POSITION_X, 1f)
                                    .target(tc.position.x + K2MathUtil.getRandomInRange(-1.2f, 1.2f)))
                            .repeatYoyo(6, 0f);
                    leaf.add(TweenComponent.create(getEngine())
                            .setTimeline(tl)
                            .addTween(Tween.to(leaf, K2EntityTweenAccessor.OPACITY, 2f).target(0f)));

                    leaf.add(VelocityComponent.create(getEngine()).setSpeed(0f, -5f));
                    leaf.add(WhenOffScreenComponent.create(getEngine()));
                }
            }
        }
    }

    private void init(){
        healthPerLeaf = Health.Player/leafPositions.length;

        healthBar = ((PooledEngine)getEngine()).createEntity();
        healthBar.add(TransformComponent.create(getEngine())
                .setPosition(9.3125f, 28.6719f, Z.health));
        healthBar.add(TextureComponent.create(getEngine())
            .setRegion(Assets.getBranch()));
        healthBar.add(BoundsComponent.create(getEngine())
                .setBounds(0.1f, 0.1f, 19.8f, 1f));
        getEngine().addEntity(healthBar);

        for(int i=0;i<initialPositions.length;i++){
            addLeaf(i, delaySeconds*i);
        }

        isInitialized = true;
    }

    private void addLeaf(int index, float delay) {
        Vector2 pos = initialPositions[index];
        Vector2 targetPos = leafPositions[index];
        TextureRegion region = Assets.getBranchLeaf(index);
        Vector2 originOffset = originOffsets[index];
        originOffset.sub(new Vector2(region.getRegionWidth()/2f, region.getRegionHeight()/2f).scl(1f/ App.PPM));
        Entity leaf = ((PooledEngine)getEngine()).createEntity();
        leaf.add(HealthLeafComponent.create(getEngine()));
        leaf.add(BoundsComponent.create(getEngine())
            .setBounds(0f, 0f, 1f, 1f));
        leaf.add(TransformComponent.create(getEngine())
                .setPosition(pos.x, pos.y, Z.healthLeaf)
                .setScale(0.1f, 0.1f)
            .setOriginOffset(originOffset.x, originOffset.y));
        leaf.add(TweenComponent.create(getEngine())
                .addTween(Tween.to(leaf, K2EntityTweenAccessor.SCALE, tweenTime)
                        .target(1f, 1f)
                        .ease(TweenEquations.easeOutElastic)
                        .delay(delaySeconds * index))
                .addTween(Tween.to(leaf, K2EntityTweenAccessor.POSITION_XY, tweenTime)
                        .target(targetPos.x, targetPos.y)
                        .ease(TweenEquations.easeOutBounce)
                .delay(delay)));
        leaf.add(TextureComponent.create(getEngine())
            .setRegion(region));
        getEngine().addEntity(leaf);
        leaves.add(leaf);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        player = entity;
    }

    private int leafIndex = 0;
    private float adjust = 0.25f;

    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.H){
            player.getComponent(HealthComponent.class).health += 10f;
        }
//        if(keycode == Input.Keys.NUM_1){
//            leafIndex = 0;
//        }else if(keycode == Input.Keys.NUM_2){
//            leafIndex = 1;
//        }else if(keycode == Input.Keys.NUM_3){
//            leafIndex = 2;
//        }else if(keycode == Input.Keys.NUM_4){
//            leafIndex = 3;
//        }else if(keycode == Input.Keys.NUM_5){
//            leafIndex = 4;
//        }else if(keycode == Input.Keys.NUM_6){
//            leafIndex = 5;
//        }else if(keycode == Input.Keys.NUM_7){
//            leafIndex = 6;
//        }else if(keycode == Input.Keys.NUM_8){
//            leafIndex = 7;
//        }else if(keycode == Input.Keys.NUM_9){
//            leafIndex = 8;
//        }else if(keycode == Input.Keys.NUM_0){
//            leafIndex = 9;
//        }else if(keycode == Input.Keys.UP){
//            Vector2 pos = leafPositions[leafIndex];
//            pos.set(pos.x, pos.y + adjust);
//        }else if(keycode == Input.Keys.DOWN){
//            Vector2 pos = leafPositions[leafIndex];
//            pos.set(pos.x, pos.y - adjust);
//        }else if(keycode == Input.Keys.LEFT){
//            Vector2 pos = leafPositions[leafIndex];
//            pos.set(pos.x - adjust, pos.y);
//        }else if(keycode == Input.Keys.RIGHT){
//            Vector2 pos = leafPositions[leafIndex];
//            pos.set(pos.x + adjust, pos.y);
//        }else if(keycode == Input.Keys.SPACE){
//            for(int i=0;i<leafPositions.length;i++){
//                Gdx.app.log("LEAF POSITION", i + ": (" + leafPositions[i].x + ", " + leafPositions[i].y + ")");
//            }
//        }else if(keycode == Input.Keys.MINUS){
//            adjust = Math.max(0.01f, adjust - 0.02f);
//            Gdx.app.log("LEAF POSITION", "Adjust: " + adjust);
//        }else if(keycode == Input.Keys.EQUALS){
//            adjust += 0.02f;
//            Gdx.app.log("LEAF POSITION", "Adjust: " + adjust);
//        }
//
//        leaves.get(leafIndex).getComponent(TransformComponent.class).setTint(Color.BLUE);

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

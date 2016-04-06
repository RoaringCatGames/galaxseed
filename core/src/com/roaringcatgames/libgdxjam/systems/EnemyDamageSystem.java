package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.VectorUtils;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.values.Health;
import com.roaringcatgames.libgdxjam.values.Volume;
import com.roaringcatgames.libgdxjam.values.Z;
import com.roaringcatgames.libgdxjam.components.*;

import java.util.Random;

/**
 * Created by barry on 1/12/16 @ 7:23 PM.
 */
public class EnemyDamageSystem extends IteratingSystem {

    private static final float healthPackSpeed = -1f;

    private Array<Entity> bullets = new Array<>();
    private Array<Entity> enemies = new Array<>();

    private ComponentMapper<BulletComponent> bm;
    private ComponentMapper<BoundsComponent> bndm;
    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<CircleBoundsComponent> cm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<DamageComponent> dm;
    private ComponentMapper<SpawnerComponent> sm;
    private ComponentMapper<StateComponent> stm;
    private ComponentMapper<VelocityComponent> vm;
    private ComponentMapper<ShakeComponent> shm;
    private ComponentMapper<AnimationComponent> am;
    private ComponentMapper<PathFollowComponent> pfm;

    private ScoreComponent scoreCard;

    private Sound popSfx;
//    private Sound hitSfx;
//    private Sound plantSfx;

    Random r = new Random();

    public EnemyDamageSystem(){
        super(Family.one(BulletComponent.class, EnemyComponent.class).get());
        bm = ComponentMapper.getFor(BulletComponent.class);
        em = ComponentMapper.getFor(EnemyComponent.class);
        bndm = ComponentMapper.getFor(BoundsComponent.class);
        cm = ComponentMapper.getFor(CircleBoundsComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
        dm = ComponentMapper.getFor(DamageComponent.class);
        sm = ComponentMapper.getFor(SpawnerComponent.class);
        stm = ComponentMapper.getFor(StateComponent.class);
        vm = ComponentMapper.getFor(VelocityComponent.class);
        shm = ComponentMapper.getFor(ShakeComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);
        pfm = ComponentMapper.getFor(PathFollowComponent.class);

        popSfx = Assets.getPlanetPopSfx();
//        hitSfx = Assets.getSeedHitSfx();
//        plantSfx = Assets.getSeedPlantSfx();
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        ImmutableArray<Entity> scores = getEngine().getEntitiesFor(Family.all(ScoreComponent.class).get());
        if(scores != null && scores.size() > 0){
            scoreCard = scores.first().getComponent(ScoreComponent.class);
        }

        for(Entity bullet:bullets){
            CircleBoundsComponent bb = cm.get(bullet);
            for(Entity enemy:enemies){
                if(enemy.isScheduledForRemoval()){
                    continue;
                }
                if(bndm.has(enemy)) {
                    BoundsComponent eb = bndm.get(enemy);
                    if (Intersector.overlaps(bb.circle, eb.bounds)) {
                        getEngine().removeEntity(bullet);
                        getEngine().removeEntity(enemy);
                    }
                }else if(cm.has(enemy)){
                    CircleBoundsComponent cb = cm.get(enemy);
                    if (cb.circle.overlaps(bb.circle)) {
                        processCollision(bullet, enemy);
                    }
                }
            }
        }

        enemies.clear();
        bullets.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if(!entity.isScheduledForRemoval()) {
            if (bm.has(entity)) {
                bullets.add(entity);
            } else {
               enemies.add(entity);
            }
        }
    }


    private Vector2 bulletPos = new Vector2();
    private Vector2 enemyPos = new Vector2();
    private void processCollision(Entity bullet, Entity enemy){

        EnemyComponent ec = em.get(enemy);
        HealthComponent hc;
        //int scoredPoints = 0;

        switch(ec.enemyType) {
//            case ASTEROID_FRAG:
//                getEngine().removeEntity(bullet);
//                getEngine().removeEntity(enemy);
//                break;

            default:
                hc = hm.get(enemy);
                if(hc.health > 0f) {
                    attachPlant(bullet, enemy, ec.enemyType == EnemyType.COMET);
                    if (scoreCard != null) {
                        scoreCard.setScore(scoreCard.score + 1);
                    }

                    float startHealth = hc.health;
                    applyHealthChange(bullet, hc);

                    float fadeSpeed = 50f;
                    boolean addFade = true;
                    if (startHealth > 0f && hc.health <= 0f) {
                        switch (ec.enemyType) {
                            case ASTEROID_FRAG:
                            case COMET:
                                fadeSpeed = 250f;
                                addFade = false;
                                if(stm.has(enemy)){
                                    StateComponent sc = stm.get(enemy);
                                    sc.setLooping(false);
                                    sc.set("FULL");
                                }
                                if(pfm.has(enemy)){
                                    PathFollowComponent pfc = pfm.get(enemy);
                                    pfc.setSpeed(pfc.speed/2f);
                                    pfc.setFacingPath(false);
                                }else if (vm.has(enemy)) {
                                    VelocityComponent vc = vm.get(enemy);
                                    vc.speed.scl(0.5f);
                                }

                                float rotR = r.nextFloat();
                                float rotSpeed = (20f*rotR) + 20f;
                                if(rotR >0.5f){
                                    rotSpeed *= -1f;
                                }
                                enemy.add(RotationComponent.create(getEngine())
                                    .setRotationSpeed(rotSpeed));
                                break;
                            case ASTEROID_A:
                                attachTreeCover(enemy, Assets.getAsteroidAFrames());
                                break;
                            case ASTEROID_B:
                                attachTreeCover(enemy, Assets.getAsteroidBFrames());
                                break;
                            case ASTEROID_C:
                                attachTreeCover(enemy, Assets.getAsteroidCFrames());
                                if(r.nextFloat() < 1f){
                                    PooledEngine engine = (PooledEngine) getEngine();
                                    TransformComponent enemyTfm = tm.get(enemy);
                                    Entity healthPack = engine.createEntity();
                                    healthPack.add(TransformComponent.create(engine)
                                        .setPosition(enemyTfm.position.x, enemyTfm.position.y, Z.healthPack)
                                        .setScale(1f, 1f));
                                    healthPack.add(RotationComponent.create(engine)
                                        .setRotationSpeed(-60f));
                                    healthPack.add(VelocityComponent.create(engine)
                                        .setSpeed(0f, healthPackSpeed));
                                    healthPack.add(TextureComponent.create(engine));
                                    healthPack.add(AnimationComponent.create(engine)
                                        .addAnimation("DEFAULT", new Animation(1f / 4f, Assets.getHealthFrames())));
                                    healthPack.add(StateComponent.create(engine)
                                        .set("DEFAULT")
                                        .setLooping(true));
                                    healthPack.add(HealthPackComponent.create(engine)
                                        .setHealth(Health.HealthPack)
                                        .setInstant(true));
                                    healthPack.add(BoundsComponent.create(engine)
                                        .setBounds(0f, 0f, 1f, 1f));
                                    engine.addEntity(healthPack);
                                }
                                break;
                        }

                        //ec.setDamaging(false);
                        if (sm.has(enemy)) {
                            sm.get(enemy).setPaused(true);
                            if (vm.has(enemy)) {
                                VelocityComponent vc = vm.get(enemy);
                                vc.speed.scl(1.6f);
                            }
                            popSfx.play(Volume.POP_SFX);
                        }

                        if (addFade) {
//                            enemy.add(FadingComponent.create()
//                                    .setPercentPerSecond(fadeSpeed));
//                            enemy.add(ShakeComponent.create()
//                                    .setDuration(fadeSpeed/100f)
//                                    .setOffsets(0.3f, 0.3f)
//                                    .setSpeed(0.05f, 0.05f));
//                            TransformComponent tc = tm.get(enemy);
//                            enemy.add(OscillationComponent.create()
//                                .setMinimumRotation(tc.rotation - 5f)
//                                .setMaximumRotation(tc.rotation + 5f)
//                                .setSpeed(360f));
                        }
                    }

                    getEngine().removeEntity(bullet);
                }
                break;
        }
    }

    private void attachTreeCover(Entity enemy, Array<TextureAtlas.AtlasRegion> frames) {

        PooledEngine engine = (PooledEngine) getEngine();
        TransformComponent tc = tm.get(enemy);

        Entity treeCover = engine.createEntity();
        treeCover.add(TransformComponent.create(engine)
                .setPosition(tc.position.x, tc.position.y, Z.treeCover)
                .setRotation(tc.rotation));
        treeCover.add(TextureComponent.create(engine));
        treeCover.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(false));

        treeCover.add(FollowerComponent.create(engine)
                .setOffset(0f, 0f)
                .setTarget(enemy)
                .setBaseRotation(0f));

        treeCover.add(AnimationComponent.create(engine)
            .addAnimation("DEFAULT", new Animation(1f / 6f, frames)));
        getEngine().addEntity(treeCover);
    }

    private void applyHealthChange(Entity bullet, HealthComponent hc) {
        DamageComponent dmg = dm.get(bullet);
        hc.health = Math.max(0f, (hc.health - dmg.dps));
    }

    private void attachPlant(Entity bullet, Entity enemy, boolean isComet) {

        PooledEngine engine = (PooledEngine)getEngine();

        CircleBoundsComponent bb = cm.get(bullet);
        CircleBoundsComponent eb = cm.get(enemy);
        TransformComponent et = tm.get(enemy);
        bulletPos.set(bb.circle.x, bb.circle.y);
        enemyPos.set(eb.circle.x, eb.circle.y);

        Vector2 outVec;
        if(isComet) {
            outVec = enemyPos.cpy();
        }else {
            outVec = bulletPos.sub(enemyPos).nor();
            outVec = outVec.scl(eb.circle.radius);
        }

        Vector2 offsetVec;
        float baseRotation;
        if(isComet){
            offsetVec = eb.offset.cpy().add(0.1f, -0.5f);
        } else{
            offsetVec =VectorUtils.rotateVector(outVec.cpy(), -et.rotation).add(eb.offset);
        }
        baseRotation = offsetVec.angle() - 90f;


        Entity plant = (engine).createEntity();
        plant.add(TransformComponent.create(engine)
                .setPosition(outVec.x, outVec.y, Z.plant)
                .setRotation(et.rotation + baseRotation)
                .setScale(1f, 1f));

        plant.add(TextureComponent.create(engine));
        plant.add(StateComponent.create(engine)
                .set("DEFAULT")
                .setLooping(false));
        float rnd = r.nextFloat();
        Array<TextureAtlas.AtlasRegion> trees = rnd < 0.3f ?
                Assets.getGreenTreeFrames() :
                rnd < 0.6f ?
                        Assets.getPinkTreeFrames() :
                        Assets.getPineTreeFrames();
        plant.add(AnimationComponent.create(engine)
                .addAnimation("DEFAULT", new Animation(1f / 18f, trees)));
        plant.add(FollowerComponent.create(engine)
                .setOffset(offsetVec.x, offsetVec.y)
                .setTarget(enemy)
                .setBaseRotation(baseRotation));

        Vector2 minSpeeds = offsetVec.cpy().nor().scl(1f, 1f);
        Vector2 maxSpeeds = offsetVec.cpy().nor().scl(4f, 6f);
        plant.add(ParticleEmitterComponent.create(engine)
            .setParticleImages(Assets.getLeafFrames())
            .setParticleLifespans(0.1f, 0.2f)
            .setSpawnRate(100f)
            .setAngleRange(0f, 360f)
            .setSpeed(2f, 3f)
            .setShouldFade(true)
            .setDuration(0.3f));

        getEngine().addEntity(plant);
    }
}

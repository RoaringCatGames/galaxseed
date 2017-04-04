package com.roaringcatgames.galaxseed.systems;

import aurelienribon.tweenengine.Tween;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.galaxseed.*;
import com.roaringcatgames.galaxseed.components.*;
import com.roaringcatgames.galaxseed.screens.SpaceScreenActionResolver;
import com.roaringcatgames.galaxseed.values.GameState;
import com.roaringcatgames.galaxseed.values.Z;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.MovementSystem;
import com.roaringcatgames.kitten2d.ashley.systems.PathFollowSystem;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;

/**
 * A system to manage swapping weapons.
 */
public class WeaponChangeSystem extends IteratingSystem implements InputProcessor {

    private Entity seedSelect;
    private Entity helicopterSelect;
    private Entity auraSelect;
    private Entity overlay;
    private Entity dashboard;

    private Entity seedInfo;
    private Entity helicopterInfo;
    private Entity auraInfo;

    private Array<Entity> arrows = new Array<>();

    private float selectY = 2f;
    private float arrowY = selectY + 4.5f;
    private float arrowRotateY = selectY + 4f;
    private float dashboardY = 2.5f;
    private float infoBoundSize = 1.25f;

    private IGameProcessor game;

    private WeaponType currentWeapon;
    private boolean hasMadeInitialSelection = false;

    public WeaponChangeSystem(IGameProcessor game){
        super(Family.all(WeaponSelectComponent.class).get());
        this.game = game;
    }

    @Override
    public void update(float deltaTime) {
        PlayerComponent pc = getPlayerComponent();
        if(pc != null){
            currentWeapon = pc.weaponType;

            boolean isOverlayShowing = !K2ComponentMappers.transform.get(overlay).isHidden;

            boolean isFirstShowing = isOverlayShowing && App.isWeaponEnabled(WeaponType.GUN_SEEDS) && currentWeapon == WeaponType.UNSELECTED;
            boolean isSecondShowing = isOverlayShowing && App.isWeaponEnabled(WeaponType.HELICOPTER_SEEDS) && currentWeapon == WeaponType.UNSELECTED;
            boolean isThirdShowing = isOverlayShowing && App.isWeaponEnabled(WeaponType.POLLEN_AURA) && currentWeapon == WeaponType.UNSELECTED;

            K2ComponentMappers.transform.get(arrows.get(0)).setHidden(!isFirstShowing);
            K2ComponentMappers.transform.get(arrows.get(1)).setHidden(!isSecondShowing);
            K2ComponentMappers.transform.get(arrows.get(2)).setHidden(!isThirdShowing);
        }
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        WeaponSelectComponent wsc = Mappers.weaponSelect.get(entity);
        boolean isEnabled = App.isWeaponEnabled(wsc.weaponType);


        if(isEnabled){
            K2ComponentMappers.transform.get(entity).setHidden(false);
            if(wsc.isRepresentingLevel){
                //Set the level for the thing
                int level = levelToInt(App.getCurrentWeaponLevel(wsc.weaponType));
                K2ComponentMappers.texture.get(entity)
                        .setRegion(getLevelRegion(wsc.weaponType, level));

            }else if(wsc.weaponType == currentWeapon) {
                AnimationComponent ac = K2ComponentMappers.animation.get(entity);
                if(ac.isPaused){
                    ac.setPaused(false);
                }
            }else{
                AnimationComponent ac = K2ComponentMappers.animation.get(entity);
                if(!ac.isPaused){
                    ac.setPaused(true);
                    K2ComponentMappers.state.get(entity).time = 0f;
                }
            }
        }else{
            //Hide
            if(wsc.isRepresentingLevel) {
                K2ComponentMappers.transform.get(entity).setHidden(true);
            }else{
                StateComponent sc = K2ComponentMappers.state.get(entity);
                if(!"OFF".equals(sc.get())){
                    sc.set("OFF");
                }
            }
        }
    }

    private TextureRegion getLevelRegion(WeaponType wt, int level){
        TextureRegion region = null;
        switch(wt){
            case GUN_SEEDS:
                region = Assets.getSeedLevel(level);
                break;
            case HELICOPTER_SEEDS:
                region = Assets.getHelicopterLevel(level);
                break;
            case POLLEN_AURA:
                region = Assets.getAuraLevel(level);
                break;
        }

        return region;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        App.game.multiplexer.addProcessor(this);
        PooledEngine pEngine = (PooledEngine)engine;

        if(dashboard == null){
            dashboard = pEngine.createEntity();
            dashboard.add(TransformComponent.create(pEngine)
                .setPosition(App.W / 2f, -dashboardY, Z.weaponSelectInterface));
            dashboard.add(TextureComponent.create(pEngine)
                .setRegion(Assets.getSelectInterface()));
            engine.addEntity(dashboard);
        }

        float scale = 0.8f;
        float offset = 5.2f;
        float xPos = App.W/2f - offset;
        float buttonBoundSize = 3f;
        if(seedSelect == null){
            seedSelect = pEngine.createEntity();
            seedSelect.add(WeaponSelectComponent.create(pEngine)
                .setWeaponType(WeaponType.GUN_SEEDS));
            seedSelect.add(TransformComponent.create(pEngine)
                .setPosition(xPos, selectY, Z.weaponSelect)
                .setScale(scale, scale));
            seedSelect.add(BoundsComponent.create(pEngine)
                .setBounds(0f, 0f, buttonBoundSize, buttonBoundSize));
            seedSelect.add(FollowerComponent.create(pEngine)
                .setTarget(dashboard)
                .setMode(FollowMode.STICKY)
                .setOffset(-5.2f, -0.0499f));
            seedSelect.add(TextureComponent.create(pEngine));
            seedSelect.add(StateComponent.create(pEngine).setLooping(true).set("DEFAULT"));
            seedSelect.add(AnimationComponent.create(pEngine)
                    .setShouldClearOnBlankState(true)
                    .addAnimation("DEFAULT", Animations.getSeedPod())
                    .addAnimation("OFF", Animations.getSeedPodOff()));
            pEngine.addEntity(seedSelect);

            seedInfo = engine.createEntity();
            seedInfo.add(ClickableComponent.create(engine)
                .setEventName(SpaceScreenActionResolver.BLASTER_INFO));
            seedInfo.add(TransformComponent.create(engine)
                .setPosition(xPos + 3f, selectY + 1f));
            seedInfo.add(BoundsComponent.create(engine)
                .setBounds(0f, 0f, infoBoundSize, infoBoundSize));
            seedInfo.add(FollowerComponent.create(engine)
                .setTarget(dashboard)
                .setOffset(-2.6f, 1.25f));
            engine.addEntity(seedInfo);

            Entity seedLevel = pEngine.createEntity();
            seedLevel.add(WeaponSelectComponent.create(pEngine)
                    .setWeaponType(WeaponType.GUN_SEEDS)
                    .setRepresentingLevel(true));
            seedLevel.add(TransformComponent.create(pEngine)
                .setPosition(xPos, selectY, Z.weaponSelect));
            seedLevel.add(FollowerComponent.create(pEngine)
                .setTarget(dashboard)
                .setMode(FollowMode.STICKY)
                .setOffset(-4.60899f, -2.249f));
            seedLevel.add(TextureComponent.create(pEngine)
                .setRegion(Assets.getSeedLevel(1)));
            pEngine.addEntity(seedLevel);

            //Add An Arrow
            addArrowIndicator(pEngine, xPos-1f, 20f);
        }

        if(helicopterSelect == null){
            xPos = App.W/2f + 0.2f;
            helicopterSelect = pEngine.createEntity();
            helicopterSelect.add(WeaponSelectComponent.create(engine)
                .setWeaponType(WeaponType.HELICOPTER_SEEDS));
            helicopterSelect.add(TransformComponent.create(pEngine)
                    .setPosition(xPos, selectY, Z.weaponSelect)
                    .setScale(scale, scale));
            helicopterSelect.add(FollowerComponent.create(pEngine)
                .setTarget(dashboard)
                .setMode(FollowMode.STICKY)
                .setOffset(0.2f, 0.2f));
            helicopterSelect.add(BoundsComponent.create(pEngine)
                    .setBounds(0f, 0f, buttonBoundSize, buttonBoundSize));
            helicopterSelect.add(TextureComponent.create(pEngine));
            helicopterSelect.add(StateComponent.create(pEngine).setLooping(true).set("DEFAULT"));
            helicopterSelect.add(AnimationComponent.create(pEngine)
                    .setShouldClearOnBlankState(true)
                    .addAnimation("DEFAULT", Animations.getHelicopterPod())
                    .addAnimation("OFF", Animations.getHelicopterPodOff())
                    .setPaused(true));
            pEngine.addEntity(helicopterSelect);

            Entity helicopterLevel = pEngine.createEntity();
            helicopterLevel.add(WeaponSelectComponent.create(engine)
                    .setWeaponType(WeaponType.HELICOPTER_SEEDS)
                    .setRepresentingLevel(true));
            helicopterLevel.add(TransformComponent.create(pEngine)
                    .setPosition(xPos, selectY, Z.weaponSelect));
            helicopterLevel.add(FollowerComponent.create(pEngine)
                .setTarget(dashboard)
                .setMode(FollowMode.STICKY)
                .setOffset(0.2f, -1.899f));
            helicopterLevel.add(TextureComponent.create(pEngine)
                    .setRegion(Assets.getHelicopterLevel(1)));
            pEngine.addEntity(helicopterLevel);

            helicopterInfo = engine.createEntity();
            helicopterInfo.add(ClickableComponent.create(engine)
                    .setEventName(SpaceScreenActionResolver.COPTER_INFO));
            helicopterInfo.add(TransformComponent.create(engine)
                    .setPosition(xPos + 3f, selectY + 1f));
            helicopterInfo.add(BoundsComponent.create(engine)
                    .setBounds(0f, 0f, infoBoundSize, infoBoundSize));
            helicopterInfo.add(FollowerComponent.create(engine)
                    .setTarget(dashboard)
                    .setOffset(3.05f, 1.25f));
            engine.addEntity(helicopterInfo);

            addArrowIndicator(pEngine, xPos, 0f);
        }

        if(auraSelect == null){
            xPos = (App.W/2f) + offset + 0.55f;
            auraSelect = pEngine.createEntity();
            auraSelect.add(WeaponSelectComponent.create(engine)
                .setWeaponType(WeaponType.POLLEN_AURA));
            auraSelect.add(TransformComponent.create(pEngine)
                    .setPosition(xPos, selectY, Z.weaponSelect)
                    .setScale(scale, scale));
            auraSelect.add(FollowerComponent.create(pEngine)
                .setTarget(dashboard)
                .setMode(FollowMode.STICKY)
                .setOffset(5.75f, -0.0499f));
            auraSelect.add(BoundsComponent.create(pEngine)
                    .setBounds(0f, 0f, buttonBoundSize, buttonBoundSize));
            auraSelect.add(StateComponent.create(pEngine).setLooping(true).set("DEFAULT"));
            auraSelect.add(AnimationComponent.create(pEngine)
                    .setShouldClearOnBlankState(true)
                    .addAnimation("DEFAULT", Animations.getAuraPod())
                    .addAnimation("OFF", Animations.getAuraPodOff())
                    .setPaused(true));
            auraSelect.add(TextureComponent.create(pEngine));
            pEngine.addEntity(auraSelect);

            Entity auraLevel = pEngine.createEntity();
            auraLevel.add(WeaponSelectComponent.create(engine)
                    .setWeaponType(WeaponType.POLLEN_AURA)
                    .setRepresentingLevel(true));
            auraLevel.add(TransformComponent.create(pEngine)
                    .setPosition(xPos, selectY, Z.weaponSelect));
            auraLevel.add(FollowerComponent.create(pEngine)
                .setTarget(dashboard)
                .setMode(FollowMode.STICKY)
                .setOffset(5.099f, -2.249f));
            auraLevel.add(TextureComponent.create(pEngine)
                    .setRegion(Assets.getAuraLevel(1)));
            pEngine.addEntity(auraLevel);

            auraInfo = engine.createEntity();
            auraInfo.add(ClickableComponent.create(engine)
                    .setEventName(SpaceScreenActionResolver.POLLEN_INFO));
            auraInfo.add(TransformComponent.create(engine)
                    .setPosition(xPos + 3f, selectY + 1f));
            auraInfo.add(BoundsComponent.create(engine)
                    .setBounds(0f, 0f, infoBoundSize, infoBoundSize));
            auraInfo.add(FollowerComponent.create(engine)
                    .setTarget(dashboard)
                    .setOffset(8.599f, 0.7f));
            engine.addEntity(auraInfo);

            addArrowIndicator(pEngine, xPos + 1f, -20f);
        }

        if(overlay == null){
            overlay = pEngine.createEntity();
            overlay.add(TransformComponent.create(pEngine)
                    .setPosition(App.W / 2f, App.H / 2f, Z.weaponSelectOverlay)
                    .setHidden(true)
                    .setScale(App.PPM * App.W, App.H * App.PPM));
            overlay.add(TextureComponent.create(pEngine)
                    .setRegion(Assets.getOverlay()));
            pEngine.addEntity(overlay);
        }
    }

    private void addArrowIndicator(PooledEngine pEngine, float xPos, float rotation) {
        Entity e = pEngine.createEntity();
        float y = rotation != 0f ? arrowRotateY : arrowY;
        e.add(TransformComponent.create(pEngine)
                .setPosition(xPos, y, Z.arrow)
                .setRotation(rotation)
                .setHidden(true));
        e.add(StateComponent.create(pEngine)
            .setLooping(true)
            .set("DEFAULT"));
        e.add(AnimationComponent.create(pEngine)
                .addAnimation("DEFAULT", Animations.getArrow()));
        e.add(TextureComponent.create(pEngine));
        pEngine.addEntity(e);
        arrows.add(e);
    }


    private void switchWeapon(WeaponType wt){
        ImmutableArray<Entity> players = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get());

        if(players.size() > 0) {
            Entity player = players.first();

            PlayerComponent pc = Mappers.player.get(player);
            pc.setWeaponType(wt);
            pc.setWeaponLevel(App.getCurrentWeaponLevel(wt));

            WeaponGeneratorUtil.clearWeapon(getEngine());
            PooledEngine engine = (PooledEngine) getEngine();
            switch (wt) {
                case POLLEN_AURA:
                    WeaponGeneratorUtil.generateAura(player, engine);
                    break;
                case GUN_SEEDS:
                    WeaponGeneratorUtil.generateSeedGuns(player, engine);
                    break;
                case HELICOPTER_SEEDS:
                    WeaponGeneratorUtil.generateHelicopterGuns(player, engine);
                    break;
            }
        }
    }



    public void showWeaponSelect() {
        PlayerComponent pc = getPlayerComponent();
        if(pc != null) {
            toggleWeaponSelect(true);
        }
    }

    private PlayerComponent getPlayerComponent() {
        ImmutableArray<Entity> players = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get());
        if(players == null || players.size() < 1){
            return null;
        }
        return Mappers.player.get(players.first());
    }

    private int levelToInt(WeaponLevel level){
        return level == WeaponLevel.LEVEL_1 ? 1 :
                level == WeaponLevel.LEVEL_2 ? 2 :
                level == WeaponLevel.LEVEL_3 ? 3 : 4;
    }

    private void toggleWeaponSelect(boolean isShowing) {
        float target = isShowing ? dashboardY : -dashboardY;
        float time = isShowing ? 0.05f : 0.5f;
        TweenComponent tc = K2ComponentMappers.tween.get(dashboard);
        if(tc != null) {
            for(Tween t:tc.tweens) {

                if(!t.isFinished()){
                    t.pause();
                    t.kill();
                }
            }
        }else{
            tc = TweenComponent.create(getEngine());
        }
        tc.addTween(Tween.to(dashboard, K2EntityTweenAccessor.POSITION_Y, time)
                .target(target));
        dashboard.add(tc);

        if(hasMadeInitialSelection) {
            if (!isShowing) {
                ((GalaxSeedGame) this.game).resumeBgMusic();
                Sfx.playSpeedUp();
                getEngine().getSystem(MovementSystem.class).setProcessing(true);
                getEngine().getSystem(PathFollowSystem.class).setProcessing(true);
                getEngine().getSystem(FiringSystem.class).setProcessing(true);
                getEngine().getSystem(EnemyFiringSystem.class).setProcessing(true);
                getEngine().getSystem(EnemySpawnSystem.class).setProcessing(true);
            } else {
                this.game.pauseBgMusic();
                Sfx.playSlowDown();
                getEngine().getSystem(MovementSystem.class).setProcessing(false);
                getEngine().getSystem(PathFollowSystem.class).setProcessing(false);
                getEngine().getSystem(FiringSystem.class).setProcessing(false);
                getEngine().getSystem(EnemyFiringSystem.class).setProcessing(false);
                getEngine().getSystem(EnemySpawnSystem.class).setProcessing(false);
            }


        }
        K2ComponentMappers.transform.get(overlay).setHidden(!isShowing);
    }

    /**
     * Input processor implementation
     */

    private Vector3 touchPoint = new Vector3();

//    int target = 0;
//    float adjust = 0.05f;

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

        boolean hideCursor = true;
        if(App.getState() == GameState.WEAPON_SELECT) {

            touchPoint.set(screenX, screenY, 0f);
            game.getViewport().unproject(touchPoint);
            PlayerComponent pc = getPlayerComponent();

            BoundsComponent seedBounds = K2ComponentMappers.bounds.get(seedSelect);
            BoundsComponent helicopterBounds = K2ComponentMappers.bounds.get(helicopterSelect);
            BoundsComponent auraBounds = K2ComponentMappers.bounds.get(auraSelect);

            BoundsComponent seedInfoBounds = K2ComponentMappers.bounds.get(seedInfo);
            BoundsComponent heliInfoBounds = K2ComponentMappers.bounds.get(helicopterInfo);
            BoundsComponent auraInfoBounds = K2ComponentMappers.bounds.get(auraInfo);


            if (App.isWeaponEnabled(WeaponType.GUN_SEEDS) && seedBounds.bounds.contains(touchPoint.x, touchPoint.y)) {
                switchWeapon(WeaponType.GUN_SEEDS);
                hideCursor = false;
            } else if (App.isWeaponEnabled(WeaponType.HELICOPTER_SEEDS) && helicopterBounds.bounds.contains(touchPoint.x, touchPoint.y)) {
                switchWeapon(WeaponType.HELICOPTER_SEEDS);
                hideCursor = false;
            } else if (App.isWeaponEnabled(WeaponType.POLLEN_AURA) && auraBounds.bounds.contains(touchPoint.x, touchPoint.y)) {
                switchWeapon(WeaponType.POLLEN_AURA);
                hideCursor = false;
            } else if((!seedInfoBounds.bounds.contains(touchPoint.x, touchPoint.y) &&
                       !heliInfoBounds.bounds.contains(touchPoint.x, touchPoint.y) &&
                       !auraInfoBounds.bounds.contains(touchPoint.x, touchPoint.y)) &&
                      pc != null && pc.weaponType != WeaponType.UNSELECTED){
                App.setState(GameState.PLAYING);
                hasMadeInitialSelection = true;
                toggleWeaponSelect(false);
            }
        }

        if(hideCursor && App.isDesktop()) {
            Gdx.graphics.setCursor(App.getHiddenCursor());
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if(pointer == 0 && App.getState() == GameState.PLAYING) {
            App.setState(GameState.WEAPON_SELECT);
            showWeaponSelect();
        }
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

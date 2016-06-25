package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.Mappers;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.WeaponLevel;
import com.roaringcatgames.libgdxjam.components.WeaponType;
import com.roaringcatgames.libgdxjam.values.GameState;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * A system to manage swapping weapons.
 */
public class WeaponChangeSystem extends EntitySystem implements InputProcessor {

    private Entity seedSelect;
    private Entity seedLevel;
    private Entity helicpoterSelect;
    private Entity helicopterLevel;
    private Entity auraSelect;
    private Entity auraLevel;
    private Entity overlay;

    private OrthographicCamera cam;

    public WeaponChangeSystem(OrthographicCamera cam){
        this.cam = cam;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        App.game.multiplexer.addProcessor(this);
        PooledEngine pEngine = (PooledEngine)engine;

        float xPos = App.W/4f;
        float yPos = 2f;
        if(seedSelect == null){
            seedSelect = pEngine.createEntity();
            seedSelect.add(TransformComponent.create(pEngine)
                .setPosition(xPos, yPos, Z.weaponSelect)
                .setHidden(true));
            seedSelect.add(BoundsComponent.create(pEngine)
                .setBounds(0f, 0f, 2f, 2f));
            seedSelect.add(TextureComponent.create(pEngine));
            seedSelect.add(StateComponent.create(pEngine).setLooping(true).set("DEFAULT"));
            seedSelect.add(AnimationComponent.create(pEngine)
                    .addAnimation("DEFAULT", Animations.getSeedPod()));
            pEngine.addEntity(seedSelect);

            seedLevel = pEngine.createEntity();
            seedLevel.add(TransformComponent.create(pEngine)
                .setPosition(xPos, yPos, Z.weaponSelect)
                .setHidden(true));
            seedLevel.add(TextureComponent.create(pEngine)
                .setRegion(Assets.getSeedLevel(1)));
            pEngine.addEntity(seedLevel);

        }

        if(helicpoterSelect == null){
            xPos = 2f * (App.W/4f);
            helicpoterSelect = pEngine.createEntity();
            helicpoterSelect.add(TransformComponent.create(pEngine)
                    .setPosition(xPos, yPos, Z.weaponSelect)
                    .setHidden(true));
            helicpoterSelect.add(BoundsComponent.create(pEngine)
                    .setBounds(0f, 0f, 2f, 2f));
            helicpoterSelect.add(TextureComponent.create(pEngine));
            helicpoterSelect.add(StateComponent.create(pEngine).setLooping(true).set("DEFAULT"));
            helicpoterSelect.add(AnimationComponent.create(pEngine)
                    .addAnimation("DEFAULT", Animations.getHelicopterPod())
                    .setPaused(true));
            pEngine.addEntity(helicpoterSelect);

            helicopterLevel = pEngine.createEntity();
            helicopterLevel.add(TransformComponent.create(pEngine)
                    .setPosition(xPos, yPos, Z.weaponSelect)
                    .setHidden(true));
            helicopterLevel.add(TextureComponent.create(pEngine)
                    .setRegion(Assets.getHelicopterLevel(1)));
            pEngine.addEntity(helicopterLevel);
        }

        if(auraSelect == null){
            xPos = 3f*(App.W/4f);
            auraSelect = pEngine.createEntity();
            auraSelect.add(TransformComponent.create(pEngine)
                    .setPosition(xPos, yPos, Z.weaponSelect)
                    .setHidden(true));
            auraSelect.add(BoundsComponent.create(pEngine)
                    .setBounds(0f, 0f, 2f, 2f));
            auraSelect.add(StateComponent.create(pEngine).setLooping(true).set("DEFAULT"));
            auraSelect.add(AnimationComponent.create(pEngine)
                .addAnimation("DEFAULT", Animations.getAuraPod())
                .setPaused(true));
            auraSelect.add(TextureComponent.create(pEngine));
            pEngine.addEntity(auraSelect);

            auraLevel = pEngine.createEntity();
            auraLevel.add(TransformComponent.create(pEngine)
                    .setPosition(xPos, yPos, Z.weaponSelect)
                    .setHidden(true));
            auraLevel.add(TextureComponent.create(pEngine)
                    .setRegion(Assets.getAuraLevel(1)));
            pEngine.addEntity(auraLevel);
        }

        if(overlay == null){
            overlay = pEngine.createEntity();
            overlay.add(TransformComponent.create(pEngine)
                .setPosition(App.W/2f, App.H/2f, Z.weaponSelectOverlay)
                .setHidden(true)
                .setScale(App.PPM*App.W, App.H*App.PPM));
            overlay.add(TextureComponent.create(pEngine)
                .setRegion(Assets.getOverlay()));
            pEngine.addEntity(overlay);
        }
    }

    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.NUM_1){
            switchWeapon(WeaponType.GUN_SEEDS);
        }else if(keycode == Input.Keys.NUM_2){
            switchWeapon(WeaponType.POLLEN_AURA);
        }else if(keycode == Input.Keys.NUM_3){
            switchWeapon(WeaponType.HELICOPTER_SEEDS);
        }
        return false;
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

            toggleWeaponSelect(true, wt);
        }
    }


    Vector3 touchPoint = new Vector3();

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

        if(App.isSlowed()){
            touchPoint.set(screenX, screenY, 0f);
            this.cam.unproject(touchPoint);

            BoundsComponent seedBounds = K2ComponentMappers.bounds.get(seedSelect);
            BoundsComponent helicopterBounds = K2ComponentMappers.bounds.get(helicpoterSelect);
            BoundsComponent auraBounds = K2ComponentMappers.bounds.get(auraSelect);
            if(seedBounds.bounds.contains(touchPoint.x, touchPoint.y)){
                switchWeapon(WeaponType.GUN_SEEDS);
            }else if(helicopterBounds.bounds.contains(touchPoint.x, touchPoint.y)){
                switchWeapon(WeaponType.HELICOPTER_SEEDS);
            }else if(auraBounds.bounds.contains(touchPoint.x, touchPoint.y)){
                switchWeapon(WeaponType.POLLEN_AURA);
            }else{
                toggleWeaponSelect(false, WeaponType.GUN_SEEDS);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if(App.getState() != GameState.GAME_OVER && !App.isSlowed()) {
            ImmutableArray<Entity> players = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get());
            PlayerComponent pc = Mappers.player.get(players.first());

            toggleWeaponSelect(true, pc.weaponType);

            int heliLevel = levelToInt(App.getCurrentWeaponLevel(WeaponType.HELICOPTER_SEEDS));
            K2ComponentMappers.texture.get(helicopterLevel).setRegion(Assets.getHelicopterLevel(heliLevel));
            int seedLvl = levelToInt(App.getCurrentWeaponLevel(WeaponType.GUN_SEEDS));
            K2ComponentMappers.texture.get(seedLevel).setRegion(Assets.getHelicopterLevel(seedLvl));
            int auraLvl = levelToInt(App.getCurrentWeaponLevel(WeaponType.POLLEN_AURA));
            K2ComponentMappers.texture.get(auraLevel).setRegion(Assets.getHelicopterLevel(auraLvl));
        }
        return false;
    }

    private int levelToInt(WeaponLevel level){
        return level == WeaponLevel.LEVEL_1 ? 1 :
                level == WeaponLevel.LEVEL_2 ? 2 :
                level == WeaponLevel.LEVEL_3 ? 3 : 4;
    }

    private void toggleWeaponSelect(boolean isShowing, WeaponType currentType) {
        App.setSlowed(isShowing);

        toggleWeaponEntityData(seedSelect, isShowing, currentType == WeaponType.GUN_SEEDS);
        toggleWeaponEntityData(auraSelect, isShowing, currentType == WeaponType.POLLEN_AURA);
        toggleWeaponEntityData(helicpoterSelect, isShowing, currentType == WeaponType.HELICOPTER_SEEDS);
        K2ComponentMappers.transform.get(overlay).setHidden(!isShowing);
        K2ComponentMappers.transform.get(seedLevel).setHidden(!isShowing);
        K2ComponentMappers.transform.get(auraLevel).setHidden(!isShowing);
        K2ComponentMappers.transform.get(helicopterLevel).setHidden(!isShowing);
    }

    private void toggleWeaponEntityData(Entity selector, boolean isShowing, boolean isAnimated){

        K2ComponentMappers.transform.get(selector).setHidden(!isShowing);
        K2ComponentMappers.animation.get(selector).setPaused(!isAnimated);
        if(!isAnimated){
            K2ComponentMappers.state.get(selector).time = 0f;
        }
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

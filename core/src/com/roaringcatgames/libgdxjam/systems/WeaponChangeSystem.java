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
import com.roaringcatgames.libgdxjam.components.WeaponType;
import com.roaringcatgames.libgdxjam.values.GameState;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * A system to manage swapping weapons.
 */
public class WeaponChangeSystem extends EntitySystem implements InputProcessor {

    private Entity seedSelect;
    private Entity helicpoterSelect;
    private Entity auraSelect;
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

        if(seedSelect == null){
            seedSelect = pEngine.createEntity();
            seedSelect.add(TransformComponent.create(pEngine)
                .setPosition(App.W/4f, 0f, Z.weaponSelect)
                .setHidden(true));
            seedSelect.add(BoundsComponent.create(pEngine)
                .setBounds(0f, 0f, 2f, 2f));
            seedSelect.add(TextureComponent.create(pEngine));
            seedSelect.add(StateComponent.create(pEngine).setLooping(false).set("DEFAULT"));
            seedSelect.add(AnimationComponent.create(pEngine)
                    .addAnimation("DEFAULT", Animations.getSeedPod())
                    .addAnimation("SELECTED", Animations.getSeedPodOpening()));
            pEngine.addEntity(seedSelect);
        }

        if(helicpoterSelect == null){
            helicpoterSelect = pEngine.createEntity();
            helicpoterSelect.add(TransformComponent.create(pEngine)
                    .setPosition(2f * (App.W / 4f), 0f, Z.weaponSelect)
                    .setHidden(true));
            helicpoterSelect.add(BoundsComponent.create(pEngine)
                    .setBounds(0f, 0f, 2f, 2f));
            helicpoterSelect.add(TextureComponent.create(pEngine));
            helicpoterSelect.add(StateComponent.create(pEngine).setLooping(false).set("DEFAULT"));
            helicpoterSelect.add(AnimationComponent.create(pEngine)
                    .addAnimation("DEFAULT", Animations.getHelicopterPod())
                    .addAnimation("SELECTED", Animations.getHelicopterPodOpening()));
            pEngine.addEntity(helicpoterSelect);
        }

        if(auraSelect == null){
            auraSelect = pEngine.createEntity();
            auraSelect.add(TransformComponent.create(pEngine)
                    .setPosition(3f*(App.W/4f), 0f, Z.weaponSelect)
                    .setHidden(true));
            auraSelect.add(BoundsComponent.create(pEngine)
                    .setBounds(0f, 0f, 2f, 2f));
            auraSelect.add(StateComponent.create(pEngine).setLooping(false).set("DEFAULT"));
            auraSelect.add(AnimationComponent.create(pEngine)
                .addAnimation("DEFAULT", Animations.getAuraPod())
                .addAnimation("SELECTED", Animations.getAuraPodOpening()));
            auraSelect.add(TextureComponent.create(pEngine));
            pEngine.addEntity(auraSelect);
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
                toggleWeaponSelect(false);
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if(App.getState() != GameState.GAME_OVER && !App.isSlowed()) {
            toggleWeaponSelect(true);
        }
        return false;
    }

    private void toggleWeaponSelect(boolean isShowing) {
        App.setSlowed(isShowing);
        K2ComponentMappers.transform.get(seedSelect).setHidden(!isShowing);
        K2ComponentMappers.transform.get(auraSelect).setHidden(!isShowing);
        K2ComponentMappers.transform.get(helicpoterSelect).setHidden(!isShowing);
        K2ComponentMappers.transform.get(overlay).setHidden(!isShowing);
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

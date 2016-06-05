package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.components.Mappers;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.WeaponType;

/**
 *
 */
public class WeaponChangeSystem extends EntitySystem implements InputProcessor {

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        App.game.multiplexer.addProcessor(this);
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
        Entity player = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();

        PlayerComponent pc = Mappers.player.get(player);
        pc.setWeaponType(wt);
        pc.setWeaponLevel(App.getCurrentWeaponLevel(wt));

        WeaponGeneratorUtil.clearWeapon(getEngine());
        PooledEngine engine = (PooledEngine)getEngine();
        switch(wt){
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

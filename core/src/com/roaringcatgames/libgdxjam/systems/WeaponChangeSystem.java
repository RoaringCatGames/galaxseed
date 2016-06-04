package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.components.*;

import java.util.Map;

/**
 *
 */
public class WeaponChangeSystem extends EntitySystem implements InputProcessor {

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

    }

    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.NUM_1){
            //Apply GUN SEED
        }else if(keycode == Input.Keys.NUM_2){
            //Clear current Guns
            Entity player = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
            ImmutableArray<Entity> currentGunEntities = getEngine().getEntitiesFor(Family.one(GunComponent.class, WeaponDecorationComponent.class).get());

            PlayerComponent pc = Mappers.player.get(player);
            pc.setWeaponType(WeaponType.POLLEN_AURA);
            pc.setWeaponLevel(App.getCurrentWeaponLevel(WeaponType.POLLEN_AURA));

            for(Entity e:currentGunEntities){
                getEngine().removeEntity(e);
            }

            //Apply Aura
            PooledEngine engine = (PooledEngine)getEngine();
            Entity aura = engine.createEntity();
            aura.add(PollenAuraComponent.create(engine));
            aura.add(TextureComponent.create(engine));
            aura.add(AnimationComponent.create(engine));
                //.addAnimation("DEFAULT", Animations.getPollenAura()))
            aura.add(CircleBoundsComponent.create(engine)
                .setCircle(0f, 0f, 3f));
            aura.add(FollowerComponent.create(engine)
                .setTarget(player)
                .setMode(FollowMode.STICKY));
            engine.addEntity(aura);


        }else if(keycode == Input.Keys.NUM_3){
            //APply helicopter seed
        }
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

package com.roaringcatgames.galaxseed.systems;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.galaxseed.components.Mappers;
import com.roaringcatgames.galaxseed.components.PlayerComponent;
import com.roaringcatgames.galaxseed.components.WeaponType;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.components.TextureComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.kitten2d.ashley.components.TweenComponent;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.values.Z;

/**
 * Provides messages in response to events
 */
public class MessageSystem extends EntitySystem implements InputProcessor {

    private Entity tutorial;
    private Entity weaponChoose;

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        PooledEngine e = (PooledEngine)engine;
        tutorial = e.createEntity();
        tutorial.add(TransformComponent.create(e)
                .setPosition(App.W / 2f, App.H / 2f, Z.message)
                .setScale(0.1f, 0.1f));
        tutorial.add(TextureComponent.create(e)
            .setRegion(Assets.getTutorialMessage()));
        tutorial.add(TweenComponent.create(e)
            .addTween(Tween.to(tutorial, K2EntityTweenAccessor.SCALE, 1f)
                    .target(1f, 1f)
                    .ease(TweenEquations.easeOutElastic)));
        e.addEntity(tutorial);

        App.game.multiplexer.addProcessor(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        App.game.multiplexer.removeProcessor(this);
    }

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
        if(tutorial != null && weaponChoose == null){
            getEngine().removeEntity(tutorial);

            PooledEngine e = (PooledEngine)getEngine();
            weaponChoose = e.createEntity();
            weaponChoose.add(TransformComponent.create(e)
                    .setPosition(App.W / 2f, App.H / 2f, Z.message)
                    .setScale(1f, 1f));
            weaponChoose.add(TextureComponent.create(e)
                    .setRegion(Assets.getWeaponChooseMessage()));
            e.addEntity(tutorial);
            return true;
        }else if(weaponChoose != null){
            ImmutableArray<Entity> players = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get());
            if(players != null && players.size() > 0){
                if(Mappers.player.get(players.get(0)).weaponType != WeaponType.UNSELECTED){
                    getEngine().removeEntity(weaponChoose);
                    tutorial = null;
                    weaponChoose = null;
                    return false;
                }
            }

            return true;
        }

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

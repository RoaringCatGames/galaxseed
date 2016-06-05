package com.roaringcatgames.libgdxjam.systems;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.TextureComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.kitten2d.ashley.components.TweenComponent;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * Provides messages in response to events
 */
public class MessageSystem extends EntitySystem implements InputProcessor {

    private Entity tutorial;

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        PooledEngine e = (PooledEngine)engine;
        tutorial = e.createEntity();
        tutorial.add(TransformComponent.create(engine)
                .setPosition(App.W / 2f, App.H / 2f, Z.message)
                .setScale(0.1f, 0.1f));
        tutorial.add(TextureComponent.create(engine)
            .setRegion(Assets.getTutorialMessage()));
        tutorial.add(TweenComponent.create(engine)
            .addTween(Tween.to(tutorial, K2EntityTweenAccessor.SCALE, 1f)
                    .target(1f, 1f)
                    .ease(TweenEquations.easeOutElastic)));
        engine.addEntity(tutorial);

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
        if(tutorial != null) {
            getEngine().removeEntity(tutorial);
            tutorial = null;
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

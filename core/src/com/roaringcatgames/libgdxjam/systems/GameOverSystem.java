package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.TextComponent;
import com.roaringcatgames.libgdxjam.screens.IScreenDispatcher;
import com.roaringcatgames.libgdxjam.values.GameState;
import com.roaringcatgames.libgdxjam.values.Z;

import javax.print.attribute.standard.OrientationRequested;

/**
 * Created by barry on 3/2/16 @ 8:43 PM.
 */
public class GameOverSystem extends IteratingSystem implements InputProcessor {

    private Entity gameOverText;
    private Entity restartButton;
    private Music endSong;
    private OrthographicCamera cam;
    private IScreenDispatcher dispatcher;

    private boolean hasInitialized = false;

    public GameOverSystem(OrthographicCamera cam, IScreenDispatcher dispatcher){
        super(Family.all(PlayerComponent.class).get());
        endSong = Assets.getGameOverMusic();
        this.cam = cam;
        this.dispatcher = dispatcher;
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
    public void setProcessing(boolean processing) {
        super.setProcessing(processing);

        if(processing){
            hasInitialized = false;
            PooledEngine engine = (PooledEngine)getEngine();
            if(gameOverText == null) {
                gameOverText = engine.createEntity();
                gameOverText.add(TransformComponent.create(engine)
                        .setPosition(App.W / 2f, App.H / 2f, Z.gameOver));
                gameOverText.add(TextComponent.create(engine)
                        .setFont(Assets.get64Font())
                        .setText("GAME OVER"));

                getEngine().addEntity(gameOverText);
            }

            if(restartButton == null){
                restartButton = engine.createEntity();
                restartButton.add(TransformComponent.create(engine)
                    .setPosition(App.W/2f, ((App.H/2f)-2.5f), Z.gameOver));
                restartButton.add(TextComponent.create(engine)
                    .setFont(Assets.get48Font())
                    .setText("Home"));
                restartButton.add(BoundsComponent.create(engine)
                    .setBounds(0f, 0f, 5f, 1.5f)
                    .setOffset(0f, -0.75f));

                getEngine().addEntity(restartButton);
            }
            gameOverText.getComponent(TransformComponent.class).setHidden(false);
            restartButton.getComponent(TransformComponent.class).setHidden(false);
            endSong.play();
        }else{
            if(endSong.isPlaying()) {
                endSong.stop();
            }
            if(gameOverText != null) {
                gameOverText.getComponent(TransformComponent.class).setHidden(true);
            }

            if(restartButton != null){
                restartButton.getComponent(TransformComponent.class).setHidden(true);
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

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

    Vector3 touchPoint = new Vector3();
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(App.getState() == GameState.GAME_OVER){
            touchPoint.set(screenX, screenY, 0f);
            touchPoint = cam.unproject(touchPoint);

            if (restartButton.getComponent(BoundsComponent.class).bounds.contains(touchPoint.x, touchPoint.y)) {
                App.setState(GameState.MENU);
                dispatcher.endCurrentScreen();
            }
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

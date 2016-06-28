package com.roaringcatgames.libgdxjam;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.libgdxjam.screens.MenuScreen;
import com.roaringcatgames.libgdxjam.screens.OptionScreen;
import com.roaringcatgames.libgdxjam.screens.SpaceScreen;
import com.roaringcatgames.libgdxjam.screens.SplashScreen;

public class LifeInSpace extends Game implements IGameProcessor {

    public InputMultiplexer multiplexer = new InputMultiplexer();
    public AssetManager am;

    private SpriteBatch batch;

    private OrthographicCamera cam;
    private OrthographicCamera guiCam;
    private Viewport viewport;

    @Override
    public void create () {
        batch = new SpriteBatch();
        cam = new OrthographicCamera(App.W, App.H);
        viewport = new FitViewport(App.W, App.H, cam);
        viewport.apply();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

        guiCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        guiCam.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0f);



        //NOTE: We force finishLoading of the Loading Frames
        //  so we can count on it.
        am = Assets.load();
        setScreen(new SplashScreen(this));
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render () {
        float r = 29/255f;
        float g = 29/255f;
        float b = 27/255f;
        Gdx.gl.glClearColor(r, g, b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if(viewport != null) {
            viewport.update(width, height);
            cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
        }
    }

    /**
     * IGame processor implementations
     */

    @Override
    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void switchScreens(String screenName) {
        switch(screenName){
            case "OPTIONS":
                multiplexer.clear();
                this.setScreen(new OptionScreen(this));
                break;
            case "MENU":
                multiplexer.clear();
                this.setScreen(new MenuScreen(this));
                break;
            case "GAME":
                multiplexer.clear();
                this.setScreen(new SpaceScreen(this));
                break;
        }
    }

    @Override
    public void addInputProcessor(InputProcessor processor) {
        this.multiplexer.addProcessor(processor);
    }

    @Override
    public void removeInputProcessor(InputProcessor processor) {
        this.multiplexer.removeProcessor(processor);
    }

    @Override
    public OrthographicCamera getCamera() {
        return cam;
    }

    @Override
    public OrthographicCamera getGUICamera() {
        return guiCam;
    }

    @Override
    public Viewport getViewport() {
        return viewport;
    }
}

package com.roaringcatgames.libgdxjam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.roaringcatgames.libgdxjam.screens.MenuScreen;
import com.roaringcatgames.libgdxjam.screens.SpaceScreen;
import com.roaringcatgames.libgdxjam.screens.SplashScreen;

public class LifeInSpace extends Game {

    public InputMultiplexer multiplexer = new InputMultiplexer();
    public AssetManager am;

    private SpriteBatch batch;
    private ScreenDispatcher screenDispatcher;

    @Override
    public void create () {
        batch = new SpriteBatch();
        screenDispatcher = new ScreenDispatcher(batch);
        Screen splashScreen = new SplashScreen(batch, screenDispatcher);
        Screen gameScreen = new MenuScreen(batch, screenDispatcher);
        Screen spaceScreen = new SpaceScreen(batch, screenDispatcher);

        screenDispatcher.AddScreen(splashScreen);
        screenDispatcher.AddScreen(gameScreen);
        screenDispatcher.AddScreen(spaceScreen);

        //NOTE: We force finishLoading of the Loading Frames
        //  so we can count on it.
        am = Assets.load();
        setScreen(splashScreen);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render () {
        float r = 29/255f;
        float g = 29/255f;
        float b = 27/255f;
        Gdx.gl.glClearColor(r, g, b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Screen nextScreen = screenDispatcher.getNextScreen();
        if(nextScreen != getScreen()){
            setScreen(nextScreen);
        }

        super.render();
    }
}

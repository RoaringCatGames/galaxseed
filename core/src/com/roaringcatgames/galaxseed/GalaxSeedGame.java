package com.roaringcatgames.galaxseed;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roaringcatgames.galaxseed.screens.*;
import com.roaringcatgames.galaxseed.values.Songs;
import com.roaringcatgames.galaxseed.values.Volume;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.helpers.IPreferenceManager;
import com.roaringcatgames.kitten2d.gdx.helpers.K2PreferenceManager;

public class GalaxSeedGame extends Game implements IGameProcessor, InputProcessor {

    public InputMultiplexer multiplexer = new InputMultiplexer();
    public AssetManager am;

    private SpriteBatch batch;
    private OrthographicCamera cam;
    private OrthographicCamera guiCam;
    private Viewport viewport;
    private Music bgMusic;

    private IAdController adController;
    private IGameServiceController gameServicesController;
    private IPreferenceManager prefManager = new K2PreferenceManager("galaxseed_prefs");


    public GalaxSeedGame(){
        super();
    }
    public GalaxSeedGame(IAdController adController, IGameServiceController gameServicesController){
        super();
        this.adController = adController;
        this.gameServicesController = gameServicesController;
    }

    @Override
    public void create () {
        batch = new SpriteBatch();
        cam = new OrthographicCamera(App.W, App.H);
        viewport = new ExtendViewport(App.W, App.H, cam);// new FitViewport(App.W, App.H, cam);
        viewport.apply();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        App.setAppWH(viewport.getWorldWidth(), viewport.getWorldHeight());

        guiCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        guiCam.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0f);


        //NOTE: We force finishLoading of the Loading Frames
        //  so we can count on it.
        am = Assets.load();
        setScreen(new SplashScreen(this));
        Gdx.input.setInputProcessor(multiplexer);

        //Let the game handle some inputs
        multiplexer.addProcessor(this);

        if(App.isDesktop()){
            Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor-cat.png")), 32, 4);
            App.setActiveCursor(customCursor);

            Cursor hiddenCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor-hidden.png")), 0, 0);
            App.setHiddenCursor(hiddenCursor);

            Cursor downCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor-cat-down.png")), 32, 4);
            App.setDownCursor(downCursor);

            Gdx.graphics.setCursor(customCursor);
        }
    }

    @Override
    public void render () {
        float r = 29/255f;
        float g = 29/255f;
        float b = 27/255f;
        Gdx.gl.glClearColor(r, g, b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        guiCam.update();
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
        multiplexer.clear();
        multiplexer.addProcessor(this);
        switch(screenName){
            case "OPTIONS":
                if(this.adController != null){
                    this.adController.showBannerAd(IAdController.AdPlacement.TOP);
                }
                this.setScreen(new OptionScreen(this, gameServicesController));
                break;
            case "CREDITS":
                this.setScreen(new CreditsScreen(this, gameServicesController));
                break;
            case "MENU":
                if(this.adController != null){
                    this.adController.showBannerAd(IAdController.AdPlacement.TOP);
                }
                this.setScreen(new MenuScreen(this, gameServicesController));
                break;
            case "LEVEL_SELECT":
                if(this.adController != null){
                    this.adController.hideBannerAd(IAdController.AdPlacement.TOP);
                }
                this.setScreen(new LevelSelectScreen(this));
                break;
            case "GAME":
                if(this.adController != null){
                    this.adController.hideBannerAd(IAdController.AdPlacement.TOP);
                }
                this.setScreen(new SpaceScreen(this, gameServicesController));
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
    public void pauseBgMusic() {
        if(bgMusic != null && bgMusic.isPlaying()){
            bgMusic.pause();
        }
    }

    @Override
    public void playBgMusic(String musicName) {
        if(PrefsUtil.isMusicOn()) {
            pauseBgMusic();

            if (bgMusic != null) {
                bgMusic.stop();
            }

            switch (musicName) {
                case Songs.MENU:
                    bgMusic = Assets.getMenuMusic();
                    bgMusic.setLooping(true);
                    bgMusic.setVolume(Volume.MENU_MUSIC);
                    break;
                case Songs.GAME:
                    bgMusic = Assets.getBackgroundMusic();
                    bgMusic.setLooping(true);
                    bgMusic.setVolume(Volume.BG_MUSIC);
                    break;
                case Songs.GAME_OVER:
                    bgMusic = Assets.getGameOverMusic();
                    bgMusic.setLooping(true);
                    break;
                case Songs.LEVEL_SELECT:
                    bgMusic = Assets.getLevelSelectMusic();
                    bgMusic.setLooping(true);
                    break;
            }

            Gdx.app.log("GalaxSeedGame", "Playing Music: " + musicName);
            bgMusic.play();
        }
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

    @Override
    public IPreferenceManager getPreferenceManager() {
        return prefManager;
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
        if(App.isDesktop()) {
            Gdx.graphics.setCursor(App.getDownCursor());
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(App.isDesktop()) {
            Gdx.graphics.setCursor(App.getActiveCursor());
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
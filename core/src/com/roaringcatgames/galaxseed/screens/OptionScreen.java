package com.roaringcatgames.galaxseed.screens;

import aurelienribon.tweenengine.Tween;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.galaxseed.*;
import com.roaringcatgames.galaxseed.systems.BackgroundSystemConfig;
import com.roaringcatgames.galaxseed.values.Songs;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;
import com.roaringcatgames.galaxseed.systems.BackgroundSystem;

/**
 * This is an {@link LazyInitScreen} implementation that will
 * handle the Options and Credits view.
 */
public class OptionScreen extends LazyInitScreen implements InputProcessor {

    private final Vector2 touchPoint = new Vector2();

    private final float BUTTON_RADIUS = 2f;

    private IGameProcessor game;
    private IGameServiceController gameServicesController;
    private PooledEngine engine;

    private Entity musicButton;
    private Entity musicText;
    private Entity sfxButton;
    private Entity sfxText;
    private Entity controlButton;
    private Entity controlText;
    private Entity vibrationSelect;
    private Entity vibrationText;
    private Entity backButton;
    private Entity creditsButton;

    private Sound sfx;

    private float textX;// = App.W/3f + 0.25f;
    private float buttonX;// = App.W - 4f;
    private float musicY;// = App.H - 3f;
    private float sfxY;// = App.H - 6.5f;
    private float ctrlY;// = App.H - 10f;
    private float vibraY;// = App.H - 13.5f
    private float backY;// ;

    private String musicWords = "Music ";
    private String sfxWords = "SFX ";
    private String ctrlWords = "Control Boost ";
    private String vibraWords = "Vibration ";



    public OptionScreen(IGameProcessor game, IGameServiceController gameServices){
        super();
        this.game = game;
        this.gameServicesController = gameServices;

        float totalH = App.getTotalHeight();
        textX = App.W/2f - 5f;
        buttonX = App.W/2f + 5f;
        musicY = totalH - 4f;
        sfxY = totalH - 8f;
        ctrlY = totalH - 11.5f;
        vibraY = totalH - 15f;
        backY = totalH - 21f;
    }

    @Override
    public void show() {
        super.show();
        game.addInputProcessor(this);
    }

    @Override
    public void hide() {
        super.hide();
        game.removeInputProcessor(this);
    }

    @Override
    protected void init() {
        engine = new PooledEngine();

        sfx = Assets.getPlayerHitLight();

        Vector2 minBounds = new Vector2(0f, 0f);
        Vector2 maxBounds = new Vector2(game.getCamera().viewportWidth, game.getCamera().viewportHeight);
        engine.addSystem(new BackgroundSystem(minBounds, maxBounds, new BackgroundSystemConfig(true, false, true, true, true, false)));


        //Kitten2D Systems
        engine.addSystem(new ShakeSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new ScreenWrapSystem(minBounds, maxBounds, App.PPM));
        engine.addSystem(new TweenSystem());
        engine.addSystem(new RenderingSystem(game.getBatch(), game.getCamera(), App.PPM));
        engine.addSystem(new TextRenderingSystem(game.getBatch(), game.getGUICamera(), game.getCamera()));
        //engine.addSystem(new DebugSystem(game.getCamera()));

        BitmapFont baseFont = Gdx.graphics.getDensity() > 1f ? Assets.get48Font() : Assets.get32Font();
        BitmapFont secondaryFont = Gdx.graphics.getDensity() > 1f ? Assets.get24Font() : Assets.get16Font();

        //Setup basic entities
        String musicState = game.getPreferenceManager().getStoredString(PrefsUtil.MUSIC_KEY, "On");
        musicText = addTextEntity(textX, musicY + 0.5f, musicWords + musicState, baseFont);
        String sfxState = game.getPreferenceManager().getStoredString(PrefsUtil.SFX_KEY, "On");
        sfxText = addTextEntity(textX, sfxY + 0.5f, sfxWords + sfxState, baseFont);
        String vibraState = game.getPreferenceManager().getStoredString(PrefsUtil.VIBRA_KEY, "Off");
        vibrationText = addTextEntity(textX, vibraY + 0.5f, vibraWords + vibraState, baseFont);
        String ctrlState = game.getPreferenceManager().getStoredString(PrefsUtil.CTRL_KEY, "Off");
        controlText = addTextEntity(textX, ctrlY + 0.5f, ctrlWords + ctrlState, baseFont);

        sfxButton = addButton(buttonX, sfxY, PrefsUtil.SFX_KEY, sfxState);
        musicButton = addButton(buttonX, musicY, PrefsUtil.MUSIC_KEY, musicState);
        vibrationSelect = addButton(buttonX, vibraY, PrefsUtil.VIBRA_KEY, vibraState);
        controlButton = addButton(buttonX, ctrlY, PrefsUtil.CTRL_KEY, ctrlState);

        backButton = addButton(textX, backY, "BACK", null);
        creditsButton = addButton(buttonX, backY, "CREDITS", null);
    }



    @Override
    protected void update(float deltaChange) {
        engine.update(Math.min(deltaChange, App.MAX_DELTA_TICK));
    }


    private Entity addTextEntity(float x, float y, String text, BitmapFont font, float...bounds){
        Entity textEntity = engine.createEntity();
        textEntity.add(TransformComponent.create(engine)
                .setPosition(x, y));
        textEntity.add(TextComponent.create(engine)
                .setFont(font)
                .setText(text));

        if(bounds != null && bounds.length == 4){
            textEntity.add(BoundsComponent.create(engine)
                .setBounds(0f, 0f, bounds[0], bounds[1])
                .setOffset(bounds[2], bounds[3]));
        }
        engine.addEntity(textEntity);
        return textEntity;
    }

    private Entity addButton(float x, float y, String key, String value){
        Entity button = engine.createEntity();
        button.add(TransformComponent.create(engine)
                .setPosition(x, y)
                .setScale(1f, 1f));
        button.add(TextureComponent.create(engine)
                .setRegion(getButtonRegion(key, value)));
        button.add(CircleBoundsComponent.create(engine)
                .setCircle(0f, 0f, BUTTON_RADIUS));
        button.add(ShakeComponent.create(engine)
                .setSpeed(6f, 4f)
                .setOffsets(0.2f, 0.3f)
                .setCurrentTime(K2MathUtil.getRandomInRange(0f, 4f)));
        engine.addEntity(button);
        return button;
    }

    private TextureRegion getButtonRegion(String key, String value){
        TextureRegion region = null;
        switch(key){
            case PrefsUtil.MUSIC_KEY:
                region = value.equals("On") ? Assets.getMusicOn() : Assets.getMusicOff();
                break;
            case PrefsUtil.SFX_KEY:
                region = value.equals("On") ? Assets.getSfxOn() : Assets.getSfxOff();
                break;
            case PrefsUtil.VIBRA_KEY:
                region = value.equals("On") ? Assets.getVibrationOn() : Assets.getVibrationOff();
                break;
            case PrefsUtil.CTRL_KEY:
                region = value.equals("Off") ? Assets.getControlsSteady() : Assets.getControlsAmplified();
                break;
            case "BACK":
                region = Assets.getBackAsteroid();
                break;
            case "CREDITS":
                region = Assets.getTeamAsteroid();
                break;
        }

        return region;
    }

    /**
     *Input Processor implementation
     */

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
        touchPoint.set(screenX, screenY);
        game.getViewport().unproject(touchPoint);
        if(K2ComponentMappers.circleBounds.get(musicButton).circle.contains(touchPoint)){
            //toggleMusic
            String newValue = "Off";
            String state = game.getPreferenceManager().getStoredString(PrefsUtil.MUSIC_KEY, "On");
            if(state.equals("Off")) {
                newValue = "On";
            }else{
                game.pauseBgMusic();
            }

            game.getPreferenceManager().updateString(PrefsUtil.MUSIC_KEY, newValue);
            if(newValue.equals("On")){
                game.playBgMusic(Songs.MENU_RESUME);
            }
            K2ComponentMappers.texture.get(musicButton).setRegion(getButtonRegion(PrefsUtil.MUSIC_KEY, newValue));
            K2ComponentMappers.text.get(musicText).setText(musicWords + newValue);
            Sfx.playClickNoise();

        }else if(K2ComponentMappers.circleBounds.get(sfxButton).circle.contains(touchPoint)){
            //toggle Sfx
            String newValue = "Off";
            String state = game.getPreferenceManager().getStoredString(PrefsUtil.SFX_KEY, "On");
            if(state.equals("Off")) {
                newValue = "On";
                sfx.play();
            }

            game.getPreferenceManager().updateString(PrefsUtil.SFX_KEY, newValue);
            K2ComponentMappers.texture.get(sfxButton).setRegion(getButtonRegion(PrefsUtil.SFX_KEY, newValue));
            K2ComponentMappers.text.get(sfxText).setText(sfxWords + newValue);
            Sfx.playClickNoise();

        }else if(K2ComponentMappers.circleBounds.get(vibrationSelect).circle.contains(touchPoint)) {
            //toggle Sfx
            String newValue = "Off";
            String state = game.getPreferenceManager().getStoredString(PrefsUtil.VIBRA_KEY, "On");
            if(state.equals("Off")) {
                newValue = "On";
                Gdx.input.vibrate(new long[] {0, 100, 100, 100, 100}, -1);
            }

            game.getPreferenceManager().updateString(PrefsUtil.VIBRA_KEY, newValue);
            K2ComponentMappers.texture.get(vibrationSelect).setRegion(getButtonRegion(PrefsUtil.VIBRA_KEY, newValue));
            K2ComponentMappers.text.get(vibrationText).setText(vibraWords + newValue);

            Sfx.playClickNoise();
        }else if(K2ComponentMappers.circleBounds.get(controlButton).circle.contains(touchPoint)) {
            //toggle Sfx
            String newValue = "Off";
            String state = game.getPreferenceManager().getStoredString(PrefsUtil.CTRL_KEY, "Off");
            if(state.equals("Off")) {
                newValue = "On";
            }

            game.getPreferenceManager().updateString(PrefsUtil.CTRL_KEY, newValue);
            K2ComponentMappers.texture.get(controlButton).setRegion(getButtonRegion(PrefsUtil.CTRL_KEY, newValue));
            K2ComponentMappers.text.get(controlText).setText(ctrlWords + newValue);
            Sfx.playClickNoise();

        }else if(K2ComponentMappers.circleBounds.get(backButton).circle.contains(touchPoint)){
            Sfx.playSelectNoise();
//            if(this.gameServicesController != null){
//                this.gameServicesController.unlockAchievement(AchievementItems.TREE_HIGH);
//                this.gameServicesController.submitScore(1);
//            }
            game.switchScreens("MENU_RESUME");
        }else if(K2ComponentMappers.circleBounds.get(creditsButton).circle.contains(touchPoint)){
            Sfx.playSelectNoise();
            game.switchScreens("CREDITS");
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

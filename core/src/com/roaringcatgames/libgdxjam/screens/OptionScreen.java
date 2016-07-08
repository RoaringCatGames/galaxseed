package com.roaringcatgames.libgdxjam.screens;

import aurelienribon.tweenengine.Tween;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.PrefsUtil;
import com.roaringcatgames.libgdxjam.Sfx;
import com.roaringcatgames.libgdxjam.systems.BackgroundSystem;
import com.roaringcatgames.libgdxjam.values.Volume;

/**
 * This is an {@link LazyInitScreen} implementation that will
 * handle the Options and Credits view.
 */
public class OptionScreen extends LazyInitScreen implements InputProcessor {

    private final Vector2 touchPoint = new Vector2();

    private final float BUTTON_RADIUS = 2f;

    private IGameProcessor game;
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
    private Entity nathan;
    private Entity loi;
    private Entity barry;
    private Entity kfp;

    private Sound sfx;

    private Entity kfpCat;


    private float textX = App.W/3f;
    private float buttonX = App.W - 4f;
    private float musicY = App.H - 3f;
    private float sfxY = App.H - 6.5f;
    private float ctrlY = App.H - 10f;
    private float vibraY = App.H - 13.5f;

    private String musicWords = "Music ";
    private String sfxWords = "SFX ";
    private String ctrlWords = "Control Boost ";
    private String vibraWords = "Vibration ";



    public OptionScreen(IGameProcessor game){
        super();
        this.game = game;
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
        engine.addSystem(new BackgroundSystem(minBounds, maxBounds, false, true));


        //Kitten2D Systems
        engine.addSystem(new ShakeSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new ScreenWrapSystem(minBounds, maxBounds, App.PPM));
        engine.addSystem(new TweenSystem());
        engine.addSystem(new RenderingSystem(game.getBatch(), game.getCamera(), App.PPM));
        engine.addSystem(new TextRenderingSystem(game.getBatch(), game.getGUICamera(), game.getCamera()));
        engine.addSystem(new DebugSystem(game.getCamera()));

        BitmapFont baseFont = Gdx.graphics.getDensity() > 1f ? Assets.get64Font() : Assets.get48Font();
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

        backButton = addButton(App.W / 2f, 12f, "BACK", null);


        float x = App.W/6f;
        float y = 8f;
        float offY = y-0.9f;
        float iconY = y-4f;
        float kfpY = 0.75f;
        loi = addTextEntity(x, y, "Loi L.", baseFont, 6f, 1.25f, 0f, -0.25f);
        addTextEntity(x, offY, "Art Cat", secondaryFont);
        addIcon(x, iconY, Assets.getArtCat(), 0.75f);

        x = App.W/2f;
        nathan = addTextEntity(x, y, "Nathan H.", baseFont, 8f, 1.25f, 0f, -0.25f);
        addTextEntity(x, offY, "Music & SFX", secondaryFont);
        addIcon(x, iconY, Assets.getGvgIcon(), 0.75f);

        x = (App.W/6f)*5f;
        barry = addTextEntity(x, y, "Barry R.", baseFont, 6f, 1.25f, 0f, -0.25f);
        addTextEntity(x, offY, "Code Cat", secondaryFont);
        addIcon(x, iconY, Assets.getCodeCat(), 0.75f);


        kfpCat = addIcon(App.W/2f, -3f, Assets.getColonelCat());

        kfp = addTextEntity(App.W / 2f, kfpY, "Version: 1.0.0-#kentuckyfriedpixels", secondaryFont, 10f, 0.75f, 0f, -0.1f);
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

    private Entity addIcon(float x, float y, TextureRegion region, float...scale){
        float scaleX = 1f, scaleY = 1f;

        if(scale != null){
            if(scale.length == 1){
                scaleX = scale[0];
                scaleY = scale[0];
            }else if(scale.length == 2){
                scaleX = scale[0];
                scaleY = scale[1];
            }
        }
        Entity icon = engine.createEntity();
        icon.add(TransformComponent.create(engine)
                .setPosition(x, y)
                .setScale(scaleX, scaleY));
        icon.add(TextureComponent.create(engine)
                .setRegion(region));
        engine.addEntity(icon);
        return icon;
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
                game.playBgMusic("MENU");
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
            game.switchScreens("MENU");
        }else if(K2ComponentMappers.bounds.get(nathan).bounds.contains(touchPoint)){
            Gdx.net.openURI("http://twitter.com/TheLucidBard");
        }else if(K2ComponentMappers.bounds.get(loi).bounds.contains(touchPoint)){
            Gdx.net.openURI("http://twitter.com/LoiLeMix");
        }else if(K2ComponentMappers.bounds.get(barry).bounds.contains(touchPoint)){
            Gdx.net.openURI("http://twitter.com/barryrowe");
        }else if(K2ComponentMappers.bounds.get(kfp).bounds.contains(touchPoint)){

            kfpCat.add(TweenComponent.create(engine)
                .addTween(Tween.to(kfpCat, K2EntityTweenAccessor.POSITION, 2f)
                    .target(App.W/2f, App.H/2f, 70f)));
            //Gdx.net.openURI("https://itch.io/jam/kentucky-fried-pixels");
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

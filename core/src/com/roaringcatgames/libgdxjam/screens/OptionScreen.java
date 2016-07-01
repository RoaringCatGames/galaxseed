package com.roaringcatgames.libgdxjam.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.systems.BackgroundSystem;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * This is an {@link LazyInitScreen} implementation that will
 * handle the Options and Credits view.
 */
public class OptionScreen extends LazyInitScreen implements InputProcessor {
    private final String MUSIC_KEY = "music";
    private final String SFX_KEY = "sfx";
    private final String VIBRA_KEY = "vibration";
    private final String CTRL_KEY = "controls";
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
        String musicState = game.getPreferenceManager().getStoredString(MUSIC_KEY, "On");
        musicText = addTextEntity(textX, musicY + 0.5f, musicWords + musicState, baseFont);
        String sfxState = game.getPreferenceManager().getStoredString(SFX_KEY, "On");
        sfxText = addTextEntity(textX, sfxY + 0.5f, sfxWords + sfxState, baseFont);
        String vibraState = game.getPreferenceManager().getStoredString(VIBRA_KEY, "Off");
        vibrationText = addTextEntity(textX, vibraY + 0.5f, vibraWords + vibraState, baseFont);
        String ctrlState = game.getPreferenceManager().getStoredString(CTRL_KEY, "Off");
        controlText = addTextEntity(textX, ctrlY + 0.5f, ctrlWords + ctrlState, baseFont);

        sfxButton = addButton(buttonX, sfxY, SFX_KEY, sfxState);
        musicButton = addButton(buttonX, musicY, MUSIC_KEY, musicState);
        vibrationSelect = addButton(buttonX, vibraY, VIBRA_KEY, vibraState);
        controlButton = addButton(buttonX, ctrlY, CTRL_KEY, ctrlState);

        backButton = addButton(App.W/2f, 10f, "BACK", null);


        nathan = addTextEntity(App.W/2f,  6.5f, "Nathan Hutchens", baseFont, 8f, 1.25f, 0f, -0.25f);
        addTextEntity(App.W/2f,  5.6f, "Music", secondaryFont);

        loi = addTextEntity(App.W/2f, 4.5f, "Loi LeMix", baseFont, 6f, 1.25f, 0f, -0.25f);
        addTextEntity(App.W/2f, 3.6f, "Art Cat", secondaryFont);

        barry = addTextEntity(App.W / 2f, 2.5f, "Barry Rowe", baseFont, 6f, 1.25f, 0f, -0.25f);
        addTextEntity(App.W / 2f, 1.6f, "Code Cat", secondaryFont);

        kfp = addTextEntity(App.W / 2f, 0.5f, "Version: 1.0.0-#kentuckyfriedpixels", secondaryFont, 10f, 0.75f, 0f, -0.1f);
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
            case MUSIC_KEY:
                region = value.equals("On") ? Assets.getMusicOn() : Assets.getMusicOff();
                break;
            case SFX_KEY:
                region = value.equals("On") ? Assets.getSfxOn() : Assets.getSfxOff();
                break;
            case VIBRA_KEY:
                region = value.equals("On") ? Assets.getVibrationOn() : Assets.getVibrationOff();
                break;
            case CTRL_KEY:
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
            String state = game.getPreferenceManager().getStoredString(MUSIC_KEY, "On");
            if(state.equals("Off")) {
                newValue = "On";
            }

            game.getPreferenceManager().updateString(MUSIC_KEY, newValue);
            K2ComponentMappers.texture.get(musicButton).setRegion(getButtonRegion(MUSIC_KEY, newValue));
            K2ComponentMappers.text.get(musicText).setText(musicWords + newValue);

        }else if(K2ComponentMappers.circleBounds.get(sfxButton).circle.contains(touchPoint)){
            //toggle Sfx
            String newValue = "Off";
            String state = game.getPreferenceManager().getStoredString(SFX_KEY, "On");
            if(state.equals("Off")) {
                newValue = "On";
            }

            game.getPreferenceManager().updateString(SFX_KEY, newValue);
            K2ComponentMappers.texture.get(sfxButton).setRegion(getButtonRegion(SFX_KEY, newValue));
            K2ComponentMappers.text.get(sfxText).setText(sfxWords + newValue);

        }else if(K2ComponentMappers.circleBounds.get(vibrationSelect).circle.contains(touchPoint)) {
            //toggle Sfx
            String newValue = "Off";
            String state = game.getPreferenceManager().getStoredString(VIBRA_KEY, "On");
            if(state.equals("Off")) {
                newValue = "On";
                Gdx.input.vibrate(new long[] {0, 100, 100, 100, 100}, -1);
            }

            game.getPreferenceManager().updateString(VIBRA_KEY, newValue);
            K2ComponentMappers.texture.get(vibrationSelect).setRegion(getButtonRegion(VIBRA_KEY, newValue));
            K2ComponentMappers.text.get(vibrationText).setText(vibraWords + newValue);

        }else if(K2ComponentMappers.circleBounds.get(controlButton).circle.contains(touchPoint)) {
            //toggle Sfx
            String newValue = "Off";
            String state = game.getPreferenceManager().getStoredString(CTRL_KEY, "Off");
            if(state.equals("Off")) {
                newValue = "On";
            }

            game.getPreferenceManager().updateString(CTRL_KEY, newValue);
            K2ComponentMappers.texture.get(controlButton).setRegion(getButtonRegion(CTRL_KEY, newValue));
            K2ComponentMappers.text.get(controlText).setText(ctrlWords + newValue);

        }else if(K2ComponentMappers.circleBounds.get(backButton).circle.contains(touchPoint)){
            game.switchScreens("MENU");
        }else if(K2ComponentMappers.bounds.get(nathan).bounds.contains(touchPoint)){
            Gdx.net.openURI("http://twitter.com/TheLucidBard");
        }else if(K2ComponentMappers.bounds.get(loi).bounds.contains(touchPoint)){
            Gdx.net.openURI("http://twitter.com/LoiLeMix");
        }else if(K2ComponentMappers.bounds.get(barry).bounds.contains(touchPoint)){
            Gdx.net.openURI("http://twitter.com/barryrowe");
        }else if(K2ComponentMappers.bounds.get(kfp).bounds.contains(touchPoint)){
            Gdx.net.openURI("https://itch.io/jam/kentucky-fried-pixels");
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

package com.roaringcatgames.libgdxjam.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private final float BUTTON_RADIUS = 1f;

    private IGameProcessor game;
    private PooledEngine engine;

    private Entity musicSelect;
    private Entity musicText;
    private Entity sfxSelect;
    private Entity sfxText;
    private Entity controlSelect;
    private Entity controlText;
    private Entity vibrationSelect;
    private Entity vibrationText;


    private float textX = App.W/3f;
    private float buttonX = App.W - 4f;
    private float musicY = App.H - 3f;
    private float sfxY = App.H - 6f;
    private float ctrlY = App.H - 9f;
    private float vibraY = App.H - 12f;



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
        musicText = addTextEntity(textX, musicY + 0.5f, "Music " + musicState, baseFont);
        String sfxState = game.getPreferenceManager().getStoredString(SFX_KEY, "On");
        sfxText = addTextEntity(textX, sfxY + 0.5f, "SFX " + sfxState, baseFont);
        String vibraState = game.getPreferenceManager().getStoredString(VIBRA_KEY, "Off");
        vibrationText = addTextEntity(textX, vibraY + 0.5f, "Vibration " + vibraState, baseFont);
        String ctrlState = game.getPreferenceManager().getStoredString(CTRL_KEY, "Steady");
        controlText = addTextEntity(textX, ctrlY + 0.5f, "Controls " + ctrlState, baseFont);



        musicSelect = addButton(buttonX, musicY, MUSIC_KEY, musicState);
        sfxSelect = addButton(buttonX, sfxY, SFX_KEY, sfxState);
        vibrationSelect = addButton(buttonX, vibraY, VIBRA_KEY, vibraState);
        controlSelect = addButton(buttonX, ctrlY, CTRL_KEY, ctrlState);


        addTextEntity(App.W/2f,  5.5f, "Nathan Hutchens", baseFont);
        addTextEntity(App.W/2f,  4.7f, "Music", secondaryFont);

        addTextEntity(App.W/2f, 3.5f, "Loi LeMix", baseFont);
        addTextEntity(App.W/2f, 2.7f, "Art Cat", secondaryFont);

        addTextEntity(App.W/2f, 1.5f, "Barry Rowe", baseFont);
        addTextEntity(App.W/2f, 0.7f, "Code Cat", secondaryFont);


    }



    @Override
    protected void update(float deltaChange) {
        engine.update(Math.min(deltaChange, App.MAX_DELTA_TICK));
    }


    private Entity addTextEntity(float x, float y, String text, BitmapFont font){
        Entity textEntity = engine.createEntity();
        textEntity.add(TransformComponent.create(engine)
                .setPosition(x, y));
        textEntity.add(TextComponent.create(engine)
                .setFont(font)
                .setText(text));
        engine.addEntity(textEntity);
        return textEntity;
    }

    private Entity addButton(float x, float y, String key, String value){
        Entity button = engine.createEntity();
        button.add(TransformComponent.create(engine)
                .setPosition(x, y));
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
                region = value.equals("Steady") ? Assets.getControlsSteady() : Assets.getControlsAmplified();
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
        if(K2ComponentMappers.circleBounds.get(musicSelect).circle.contains(touchPoint)){
            //toggleMusic
            String newValue = "Off";
            String state = game.getPreferenceManager().getStoredString(MUSIC_KEY, "On");
            if(state.equals("Off")) {
                newValue = "On";
            }

            game.getPreferenceManager().updateString(MUSIC_KEY, newValue);
            K2ComponentMappers.texture.get(musicSelect).setRegion(getButtonRegion(MUSIC_KEY, newValue));
            K2ComponentMappers.text.get(musicText).setText("Music " + newValue);

        }else if(K2ComponentMappers.circleBounds.get(sfxSelect).circle.contains(touchPoint)){
            //toggle Sfx
            String newValue = "Off";
            String state = game.getPreferenceManager().getStoredString(SFX_KEY, "On");
            if(state.equals("Off")) {
                newValue = "On";
            }

            game.getPreferenceManager().updateString(SFX_KEY, newValue);
            K2ComponentMappers.texture.get(sfxSelect).setRegion(getButtonRegion(SFX_KEY, newValue));
            K2ComponentMappers.text.get(sfxText).setText("SFX " + newValue);

        }else if(K2ComponentMappers.circleBounds.get(vibrationSelect).circle.contains(touchPoint)) {
            //toggle Sfx
            String newValue = "Off";
            String state = game.getPreferenceManager().getStoredString(VIBRA_KEY, "On");
            if(state.equals("Off")) {
                newValue = "On";
            }

            game.getPreferenceManager().updateString(VIBRA_KEY, newValue);
            K2ComponentMappers.texture.get(vibrationSelect).setRegion(getButtonRegion(VIBRA_KEY, newValue));
            K2ComponentMappers.text.get(vibrationText).setText("Vibration " + newValue);

        }else if(K2ComponentMappers.circleBounds.get(controlSelect).circle.contains(touchPoint)) {
            //toggle Sfx
            String newValue = "Amplified";
            String state = game.getPreferenceManager().getStoredString(CTRL_KEY, "Steady");
            if(state.equals("Amplified")) {
                newValue = "Steady";
            }

            game.getPreferenceManager().updateString(CTRL_KEY, newValue);
            K2ComponentMappers.texture.get(controlSelect).setRegion(getButtonRegion(CTRL_KEY, newValue));
            K2ComponentMappers.text.get(controlText).setText("Controls " + newValue);

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

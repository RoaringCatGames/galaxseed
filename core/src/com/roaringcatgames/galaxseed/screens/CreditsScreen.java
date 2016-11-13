package com.roaringcatgames.galaxseed.screens;

import aurelienribon.tweenengine.Tween;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.galaxseed.*;
import com.roaringcatgames.galaxseed.systems.BackgroundSystem;
import com.roaringcatgames.galaxseed.systems.BackgroundSystemConfig;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.ashley.systems.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;

/**
 * A screen to display Credits
 */
public class CreditsScreen extends LazyInitScreen implements InputProcessor {

    private static final float BUTTON_RADIUS = 2f;

    private IGameProcessor game;
    private IGameServiceController gameServiceController;
    private PooledEngine engine;
    private Vector2 touchPoint = new Vector2();

    private Entity loi;
    private Entity barry;
    private Entity kfp;
    private Entity nathan;
    private Entity kfpCat;
    private Entity backButton;

    public CreditsScreen(IGameProcessor game, IGameServiceController gameServices)
    {
        this.game = game;
        this.gameServiceController = gameServices;
    }

    /**
     * Lazy Init Screen Implementation
     */

    @Override
    protected void init() {
        this.engine = new PooledEngine();

        Vector2 minBounds = new Vector2(0f, 0f);
        Vector2 maxBounds = new Vector2(game.getCamera().viewportWidth, game.getCamera().viewportHeight);
        engine.addSystem(new BackgroundSystem(minBounds, maxBounds, new BackgroundSystemConfig(false, true, true, true)));

        //Kitten2D Systems
        engine.addSystem(new ShakeSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new ScreenWrapSystem(minBounds, maxBounds, App.PPM));
        engine.addSystem(new TweenSystem());
        engine.addSystem(new RenderingSystem(game.getBatch(), game.getCamera(), App.PPM));
        engine.addSystem(new TextRenderingSystem(game.getBatch(), game.getGUICamera(), game.getCamera()));
        engine.addSystem(new DebugSystem(game.getCamera()));


        BitmapFont baseFont = Gdx.graphics.getDensity() > 1f ? Assets.get48Font() : Assets.get32Font();
        BitmapFont secondaryFont = Gdx.graphics.getDensity() > 1f ? Assets.get24Font() : Assets.get16Font();

        backButton = addButton(App.W / 2f, 5f, "BACK", null, Assets.getBackAsteroid());

        float rcgY = App.H - 3f;
        float loiX = App.W/2f - 3f;
        float barryX = App.W/2f + 3f;
        float nathanX = App.W/2f;
        float backX = App.W/2f;

        //float x = App.W/6f;
        float y = rcgY - 4f;
        float offY = y-1f;
        float iconY = y-4.25f;

        loi = addTextEntity(loiX, rcgY, "Loi L.", baseFont, 6f, 1.25f, 0f, -0.25f);
        addTextEntity(loiX, rcgY-1f, "Art Cat", secondaryFont);
        addIcon(loiX, rcgY - 4.25f, Assets.getArtCat(), 0.75f);

        barry = addTextEntity(barryX, rcgY, "Barry R.", baseFont, 6f, 1.25f, 0f, -0.25f);
        addTextEntity(barryX, rcgY-1f, "Code Cat", secondaryFont);
        addIcon(barryX, rcgY - 4.25f, Assets.getCodeCat(), 0.75f);

        y = y - 4f;
        offY = y-1f;
        iconY = y-4.25f;
        nathan = addTextEntity(nathanX, y, "Nathan H.", baseFont, 8f, 1.25f, 0f, -0.25f);
        addTextEntity(nathanX, offY, "Music & SFX", secondaryFont);
        addIcon(nathanX, iconY, Assets.getGvgIcon(), 0.75f);


        addIcon(loiX, 9f, Assets.getLibGdxIcon(), 0.5f);
        addIcon(App.W/2f, 11f, Assets.getLMGIcon(), 0.5f);
        addIcon(barryX, 9f, Assets.getAshleyIcon(), 0.5f);


        kfpCat = addIcon(-5f, App.H/2f, Assets.getColonelCat());

        float kfpY = 1.75f;
        kfp = addTextEntity(App.W / 2f, kfpY, "Version: 1.0.0-#kentuckyfriedpixels", secondaryFont, 10f, 1.75f, 0f, -0.1f);
    }

    @Override
    protected void update(float deltaChange) {
        engine.update(Math.min(deltaChange, App.MAX_DELTA_TICK));
    }


    ////Private Methods for LazyInit Implemenatation
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
    private Entity addButton(float x, float y, String key, String value, TextureRegion region){
        Entity button = engine.createEntity();
        button.add(TransformComponent.create(engine)
                .setPosition(x, y)
                .setScale(1f, 1f));
        button.add(TextureComponent.create(engine)
                .setRegion(region));
        button.add(CircleBoundsComponent.create(engine)
                .setCircle(0f, 0f, BUTTON_RADIUS));
        button.add(ShakeComponent.create(engine)
                .setSpeed(6f, 4f)
                .setOffsets(0.2f, 0.3f)
                .setCurrentTime(K2MathUtil.getRandomInRange(0f, 4f)));
        engine.addEntity(button);
        return button;
    }

    /**
     * Screen Overrides
     */

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


    /**
     * Input Processor Implementation
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

        if(K2ComponentMappers.bounds.get(nathan).bounds.contains(touchPoint)){
            Gdx.net.openURI("http://twitter.com/TheLucidBard");
        }else if(K2ComponentMappers.bounds.get(loi).bounds.contains(touchPoint)){
            Gdx.net.openURI("http://twitter.com/LoiLeMix");
        }else if(K2ComponentMappers.bounds.get(barry).bounds.contains(touchPoint)){
            Gdx.net.openURI("http://twitter.com/barryrowe");
        }else if(K2ComponentMappers.bounds.get(kfp).bounds.contains(touchPoint)){
            Sfx.playUpgradeSound();
            if(gameServiceController != null){
                this.gameServiceController.unlockAchievement(AchievementItems.KITTEN);
            }
            K2ComponentMappers.transform.get(kfpCat).setPosition(-5f, App.H/2f);
            kfpCat.add(TweenComponent.create(engine)
                    .addTween(Tween.to(kfpCat, K2EntityTweenAccessor.POSITION, 3f)
                            .target(App.W + 5f, App.H/2f, 0f)));
        }else if(K2ComponentMappers.circleBounds.get(backButton).circle.contains(touchPoint)){
            Sfx.playSelectNoise();
            game.switchScreens("OPTIONS");
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

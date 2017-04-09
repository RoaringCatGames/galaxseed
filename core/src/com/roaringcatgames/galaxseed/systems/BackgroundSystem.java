package com.roaringcatgames.galaxseed.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.galaxseed.Animations;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.components.PlayerComponent;
import com.roaringcatgames.galaxseed.components.WhenOffScreenComponent;
import com.roaringcatgames.galaxseed.values.Z;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;

import java.util.Random;

public class BackgroundSystem extends IteratingSystem {

    private final float NUMBER_OF_RANDOM_PLANETS = 30f;

    public float bgSpeed = -1f;
    public float stickerSpeed = -1.5f;
    public float bgStarSpeed = -2.5f;
    private float speedLineSpeedMin = -25f;
    private float speedLineSpeedMax = -40f;
    private float speedLineOpacity = 0.1f;
    private int speedLineCount = 4;
    private int numberOfStars = 20;
    private float starSpeed = -1f;
    private float cloudSpeed = -9f;

    private float left;
    private float bottom;
    private float right;
    private float top;

    private boolean isInitialized = false;

    private BackgroundSystemConfig config;

    private Array<BackgroundSticker> planets;

    protected class BackgroundTile extends BackgroundSticker{
        protected Array<TextureRegion> galaxies;
        protected TextureRegion starsImage;
        protected Array<TextureRegion> clouds;
        protected  BackgroundTile(float x, float y, float rot,
                                  TextureRegion tile, TextureRegion starsTile,
                                  Array<TextureRegion> possibleGalaxies,
                                  Array<TextureRegion> possibleClouds){
            super(x, y, rot, tile);
            this.x = x;
            this.y = y;
            this.rotation = rot;
            this.image = tile;
            this.starsImage = starsTile;
            this.galaxies = possibleGalaxies;
            this.clouds = possibleClouds;
        }
    }

    protected class BackgroundSticker{
        protected float x, y, rotation;
        protected TextureRegion image;

        protected BackgroundSticker(float x, float y, float rot, TextureRegion img) {
            this.x = x;
            this.y = y;
            this.rotation = rot;
            this.image = img;
        }
    }


    public BackgroundSystem(Vector2 minBounds, Vector2 maxBounds, BackgroundSystemConfig config){
        //No components will be modified here, just need a limited class to
        //  create a family
        super(Family.all(PlayerComponent.class).get());
        this.left = minBounds.x;
        this.bottom = minBounds.y;
        this.right = maxBounds.x;
        this.top = maxBounds.y;
        this.config = config;

        if(!config.isShouldMove()){
            bgSpeed = 0f;
            bgStarSpeed = 0f;
            stickerSpeed = 0f;
            starSpeed = 0f;
            cloudSpeed = 0f;
        }
    }

    private void init(){
        PooledEngine engine = ((PooledEngine)getEngine());

        float screenCenter = App.W/2f;
        float leftCenter = screenCenter/2f;
        float jupiterLeft = screenCenter + (51.53125f/3f);
        float jupiterRight = jupiterLeft + 51.53125f;
        float marsX = screenCenter + 7f;

        float saturnLeft = screenCenter - (44.0625f);
        float saturnRight = saturnLeft + 44.0625f;

        planets = new Array<>();

        if(config.isShouldRandomizePlanets()){
            int lastTarget = 0;
            float baseY = 0f;
            for(int i=0;i<NUMBER_OF_RANDOM_PLANETS;i++) {
                float x = MathUtils.random(0f, App.W);
                float y = baseY + MathUtils.random(App.H*2f, App.H*3.5f);
                baseY = y;
                int targetPlanet = 0;
                while(targetPlanet == 0 || targetPlanet == lastTarget){
                    targetPlanet = MathUtils.random(1, 14);
                }
                lastTarget = targetPlanet;

                Array<TextureAtlas.AtlasRegion> regions = Assets.getRandomPlanetRegions(targetPlanet);
                planets.add(new BackgroundSticker(x, y, 0f, regions.get(0)));

            }
        }else {
            planets.add(new BackgroundSticker(screenCenter, 40f, 0f, Assets.getPluto()));
            planets.add(new BackgroundSticker(leftCenter, 72f, 0f, Assets.getNeptune()));
            planets.add(new BackgroundSticker(screenCenter, 120f, 0f, Assets.getUranus()));

            planets.add(new BackgroundSticker(saturnLeft, 175f, 0f, Assets.getSaturnOne()));
            planets.add(new BackgroundSticker(saturnRight, 175f, 0f, Assets.getSaturnTwo()));

            planets.add(new BackgroundSticker(jupiterLeft, 250f, 0f, Assets.getJupiterBottomLeft()));
            planets.add(new BackgroundSticker(jupiterLeft, 301.53125f, 0f, Assets.getJupiterTopLeft()));
            planets.add(new BackgroundSticker(jupiterRight, 301.53125f, 0f, Assets.getJupiterTopRight()));
            planets.add(new BackgroundSticker(jupiterRight, 250f, 0f, Assets.getJupiterBottomrRight()));

            planets.add(new BackgroundSticker(marsX, 415f, 0f, Assets.getMars()));
            planets.add(new BackgroundSticker(leftCenter, 440f, 0f, Assets.getMoon()));
            planets.add(new BackgroundSticker(screenCenter, 460f, 0f, Assets.getEarth()));
            planets.add(new BackgroundSticker(screenCenter, 920f, 0f, Assets.getDonut()));
        }
        Entity vp = engine.createEntity();
        vp.add(BoundsComponent.create(engine)
            .setBounds(left, bottom, (right-left), (top-bottom)));
        engine.addEntity(vp);
        float tileSize = 16f;
        float tileHalfPoint = 8f;

        float startX = left;
        float startY = bottom-tileHalfPoint;
        float xTileCoverage = (right + tileHalfPoint) - (startX);
        float yTileCoverage = (top + tileHalfPoint) - (startY);

        int columns = (int)Math.ceil(xTileCoverage/tileSize);
        int rows = (int)Math.ceil(yTileCoverage/tileSize);

        float topY = 0f;
        Array<BackgroundTile> tiles = new Array<>();
        Random rnd = new Random();
        for(int i = 0;i<columns; i++){
            float x = startX + i*tileSize;
            for(int j=0;j<rows;j++){
                float y = startY + j*tileSize;
                float rVal = rnd.nextFloat();
                float rotation = rVal < 0.25f ? 0f:
                                 rVal < 0.50f ? 90f:
                                 rVal  < 0.75f ? 180f:
                                                 270f;
                float textVal = rnd.nextFloat();
                TextureRegion texture = textVal < 0.5f ? Assets.getBgATile() : Assets.getBgBTile();
                float clearVal = rnd.nextFloat();
                TextureRegion clearTile = clearVal < 0.33f ? Assets.getBgStarsTileA() :
                                          clearVal < 0.66f ? Assets.getBgStarsTileB() :
                                                             Assets.getBgStarsTileC();
                Array<TextureRegion> galaxies = null;
                float galaxyFloat = rnd.nextFloat();
                if(config.isShouldProduceGalaxies() && galaxyFloat < 0.3f){
                    galaxies = new Array<>();
                        galaxies.add(Assets.getGalaxyA());
                        galaxies.add(Assets.getGalaxyB());
                        galaxies.add(Assets.getGalaxyC());
                        galaxies.add(Assets.getGasCluster());
                        galaxies.add(Assets.getGasClusterA());
                        galaxies.add(Assets.getGasClusterB());
                        galaxies.add(Assets.getGasClusterC());
                        galaxies.add(Assets.getGasClusterD());
                        galaxies.add(Assets.getGasClusterE());
                }

                float cloudFloat = rnd.nextFloat();
                Array<TextureRegion> clouds = null;
                if(config.isShouldProduceGalaxies() && cloudFloat <= 0.25f){
                    clouds = new Array();
                    clouds.add(Assets.getBgCloudTileA());
                    clouds.add(Assets.getBgCloudTileB());
                    clouds.add(Assets.getBgCloudTileC());
                }

                tiles.add(new BackgroundTile(x, y, rotation, texture, clearTile, galaxies, clouds));
                topY = y;
            }
        }

        //We have to take off an extra pixel here, because of
        //  a weird issue that ALWAYS causes a 1ish pixel width
        //  flickering gap between tiles on the first wrap.
        //  likely a floating point issue.
        float offset = (topY+tileHalfPoint) - top - (4f/32f);

        for(BackgroundTile bg:tiles){
            //Sometimes add a galaxy
            if(bg.galaxies != null){
                Entity galaxy = engine.createEntity();
                int galaxyPos = rnd.nextInt(bg.galaxies.size);
                float additionalOffest = K2MathUtil.getRandomInRange(0f, 3f);
                float xOff = K2MathUtil.getRandomInRange(-1.5f, 1.5f);
                float yOff = K2MathUtil.getRandomInRange(-1.5f, 1.5f);
                galaxy.add(TextureComponent.create(engine)
                        .setRegion(bg.galaxies.get(galaxyPos)));

                galaxy.add(TransformComponent.create(engine)
                        .setPosition(bg.x + xOff, bg.y + yOff, Z.bg_galaxy)
                        .setRotation(bg.rotation)
                        .setScale(1f, 1f));
                galaxy.add(ScreenWrapComponent.create(engine)
                        .setMode(ScreenWrapMode.VERTICAL)
                        .setReversed(true)
                        .setWrapOffset(offset + additionalOffest)
                        .shouldRandomPerpendicularPosition(true)
                        .setMinMaxPos(0f, right)
                        .setRandomizeTexture(true)
                        .setPossibleRegions(bg.galaxies));

                galaxy.add(VelocityComponent.create(engine)
                        .setSpeed(0f, bgSpeed));
                engine.addEntity(galaxy);
            }

            Entity e = engine.createEntity();
            e.add(TextureComponent.create(engine)
                    .setRegion(bg.image));
            e.add(TransformComponent.create(engine)
                    .setPosition(bg.x, bg.y, Z.bg)
                    .setRotation(bg.rotation)
                    .setScale(1f, 1f));
            e.add(BoundsComponent.create(engine)
                    .setBounds(bg.x - tileHalfPoint, bg.y - tileHalfPoint, tileSize, tileSize));
            e.add(ScreenWrapComponent.create(getEngine())
                    .setMode(ScreenWrapMode.VERTICAL)
                    .setReversed(true)
                    .setWrapOffset(offset));
            e.add(VelocityComponent.create(engine)
                    .setSpeed(0f, bgSpeed));
            engine.addEntity(e);

            Entity starsTile = engine.createEntity();
            starsTile.add(TextureComponent.create(engine)
                    .setRegion(bg.starsImage));
            starsTile.add(TransformComponent.create(engine)
                    .setPosition(bg.x, bg.y, Z.bg_clear)
                    .setRotation(bg.rotation)
                    .setOpacity(0.5f)
                    .setScale(1f, 1f));
            starsTile.add(BoundsComponent.create(engine)
                    .setBounds(bg.x - tileHalfPoint, bg.y - tileHalfPoint, tileSize, tileSize));
            starsTile.add(ScreenWrapComponent.create(getEngine())
                    .setMode(ScreenWrapMode.VERTICAL)
                    .setReversed(true)
                    .setWrapOffset(offset));
            starsTile.add(VelocityComponent.create(engine)
                    .setSpeed(0f, bgStarSpeed));
            engine.addEntity(starsTile);

            if(bg.clouds != null){
                Entity cloudTile = engine.createEntity();
                int cloudPos = rnd.nextInt(bg.clouds.size);
                float additionalOffest = K2MathUtil.getRandomInRange(App.H*2f, App.H*5f);
                float xOff = K2MathUtil.getRandomInRange(-2.5f, 2.5f);
                float yOff = K2MathUtil.getRandomInRange(-2.5f, 2.5f);
                cloudTile.add(TextureComponent.create(engine)
                        .setRegion(bg.clouds.get(cloudPos)));

                cloudTile.add(TransformComponent.create(engine)
                        .setPosition(bg.x + xOff, bg.y + yOff, Z.bg_cloud)
                        .setRotation(bg.rotation)
                        .setScale(1f, 1f));
                cloudTile.add(ScreenWrapComponent.create(engine)
                        .setMode(ScreenWrapMode.VERTICAL)
                        .setReversed(true)
                        .setWrapOffset(offset + additionalOffest)
                        .shouldRandomPerpendicularPosition(true)
                        .setMinMaxPos(0f, right)
                        .setRandomizeTexture(true)
                        .setPossibleRegions(bg.clouds));

                cloudTile.add(VelocityComponent.create(engine)
                        .setSpeed(0f, cloudSpeed));
                engine.addEntity(cloudTile);
            }
        }


        //Speed Lines
        if(config.isShouldProduceSpeedLines()) {
            for (int i = 0; i < speedLineCount; i++) {
                Entity sl = engine.createEntity();
                int speedIndex = rnd.nextInt(5) + 1;
                float x = K2MathUtil.getRandomInRange(0.1f, right - 0.2f);
                float y = K2MathUtil.getRandomInRange(5f, top + 15f);
                TextureAtlas.AtlasRegion region = Assets.getSpeedLine(speedIndex);
                sl.add(TextureComponent.create(engine)
                        .setRegion(region));
                sl.add(VelocityComponent.create(engine)
                        .setSpeed(0f, K2MathUtil.getRandomInRange(speedLineSpeedMin, speedLineSpeedMax)));
                sl.add(TransformComponent.create(engine)
                        .setPosition(x, y, Z.speedLine)
                        .setScale(1f, 1f)
                        .setOpacity(speedLineOpacity));
                sl.add(BoundsComponent.create(engine)
                        .setBounds(
                                x - ((region.getRegionWidth() / 2f) / App.PPM),
                                y - ((region.getRegionHeight() / 2f) / App.PPM),
                                (region.getRegionWidth() / App.PPM),
                                region.getRegionHeight() / App.PPM));
                sl.add(ScreenWrapComponent.create(engine)
                        .setMode(ScreenWrapMode.VERTICAL)
                        .setReversed(true)
                        .shouldRandomPerpendicularPosition(true)
                        .setMinMaxPos(0.1f, right - 0.2f));
                engine.addEntity(sl);
            }
        }

        if(config.isShouldProduceStars()) {
            //Stars
            for (int i = 0; i < numberOfStars; i++) {
                Entity star = engine.createEntity();
                float x = K2MathUtil.getRandomInRange(0.1f, right - 0.2f);
                float y = K2MathUtil.getRandomInRange(0f, top + 15f);
                float typeR = rnd.nextFloat();
                Animation ani = typeR > 0.33f ? Animations.getStarA() :
                        typeR > 0.66f ? Animations.getStarB() :
                                Animations.getStarC();
                star.add(TextureComponent.create(engine));
                star.add(AnimationComponent.create(engine)
                        .addAnimation("DEFAULT", ani));
                StateComponent state = StateComponent.create(engine)
                        .set("DEFAULT")
                        .setLooping(true);
                state.time = rnd.nextFloat();
                star.add(state);
                star.add(VelocityComponent.create(engine)
                        .setSpeed(0f, starSpeed));
                star.add(TransformComponent.create(engine)
                        .setPosition(x, y, Z.star)
                        .setScale(1f, 1f));
                star.add(ScreenWrapComponent.create(engine)
                        .setMode(ScreenWrapMode.VERTICAL)
                        .setReversed(true)
                        .shouldRandomPerpendicularPosition(true)
                        .setMinMaxPos(0.1f, right-0.2f));
                engine.addEntity(star);
            }
        }

        if(config.isShouldProduceStickers()) {
            placePlanets();

        }


        isInitialized = true;
    }

    public void placePlanets() {
        PooledEngine engine = (PooledEngine)getEngine();
        for(BackgroundSticker bgSticker:planets){
            float width = (bgSticker.image.getRegionWidth()/ App.PPM);
            float height = (bgSticker.image.getRegionHeight()/App.PPM);

            Entity sticker = engine.createEntity();
            sticker.add(TransformComponent.create(engine)
                    .setPosition(bgSticker.x, bgSticker.y, Z.bgSticker));
            sticker.add(TextureComponent.create(engine)
                    .setRegion(bgSticker.image));
            sticker.add(VelocityComponent.create(engine)
                    .setSpeed(0f, stickerSpeed));
            sticker.add(KinematicComponent.create(engine));
            sticker.add(BoundsComponent.create(engine)
                .setBounds(bgSticker.x - (width / 2f), (bgSticker.y) - (height / 2f), width, height));
            sticker.add(WhenOffScreenComponent.create(getEngine()));
            engine.addEntity(sticker);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(!isInitialized){
            init();
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}

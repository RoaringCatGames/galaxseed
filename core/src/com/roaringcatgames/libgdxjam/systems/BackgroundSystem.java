package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.Animations;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.values.Z;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.WhenOffScreenComponent;

import java.util.Random;

public class BackgroundSystem extends IteratingSystem {

    public float bgSpeed = -1f;
    public float stickerSpeed = -1.5f;
    public float bgClearSpeed = -2.5f;
    private float speedLineSpeedMin = -25f;
    private float speedLineSpeedMax = -40f;
    private float speedLineOpacity = 0.1f;
    private int speedLineCount = 4;
    private int numberOfStars = 20;
    private float starSpeed = -1f;

    private float left;
    private float bottom;
    private float right;
    private float top;

    private boolean isUsingStickers;
    private boolean isInitialized = false;
    private boolean isUsingStars = false;

    private Array<BackgroundSticker> planets;

    protected class BackgroundTile extends BackgroundSticker{
        protected Array<TextureRegion> galaxies;
        protected TextureRegion clearImage;
        protected  BackgroundTile(float x, float y, float rot, TextureRegion tile, TextureRegion clearTile, Array<TextureRegion> possibleGalaxies){
            super(x, y, rot, tile);
            this.x = x;
            this.y = y;
            this.rotation = rot;
            this.image = tile;
            this.clearImage = clearTile;
            this.galaxies = possibleGalaxies;
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


    public BackgroundSystem(Vector2 minBounds, Vector2 maxBounds, boolean shouldProduceStickers, boolean shouldProduceStars){
        //No components will be modified here, just need a limited class to
        //  create a family
        super(Family.all(PlayerComponent.class).get());
        this.left = minBounds.x;
        this.bottom = minBounds.y;
        this.right = maxBounds.x;
        this.top = maxBounds.y;
        this.isUsingStickers = shouldProduceStickers;
        this.isUsingStars = shouldProduceStars;
    }

    private void init(){
        PooledEngine engine = ((PooledEngine)getEngine());

        planets = new Array<>();
        planets.add(new BackgroundSticker(10f, 40f, 0f, Assets.getPluto()));
        planets.add(new BackgroundSticker(5f, 72f, 0f, Assets.getNeptune()));
        planets.add(new BackgroundSticker(10f, 120f, 0f, Assets.getUranus()));
        planets.add(new BackgroundSticker(10f, 175f, 0f, Assets.getSaturn()));
        planets.add(new BackgroundSticker(10f, 250f, 0f, Assets.getJupiterBottom()));
        planets.add(new BackgroundSticker(10f, 294.22f, 0f, Assets.getJupiterTop()));
        planets.add(new BackgroundSticker(17f, 415f, 0f, Assets.getMars()));
        planets.add(new BackgroundSticker(5f, 440f, 0f, Assets.getMoon()));
        planets.add(new BackgroundSticker(10f, 460f, 0f, Assets.getEarth()));

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
                TextureRegion clearTile = clearVal < 0.33f ? Assets.getBgClearTileA() :
                                          clearVal < 0.66f ? Assets.getBgClearTileB() :
                                                             Assets.getBgClearTileC();
                Array<TextureRegion> galaxies = null;
                float galaxyFloat = rnd.nextFloat();
                if(galaxyFloat < 0.6f){
                    galaxies = new Array<>();
                    if(galaxyFloat < 0.3f){
                        galaxies.add(Assets.getGalaxyA());
                        galaxies.add(Assets.getGalaxyB());
                        galaxies.add(Assets.getGalaxyC());

                    }else{
                        galaxies.add(Assets.getGasCluster());
                        galaxies.add(Assets.getGasClusterA());
                        galaxies.add(Assets.getGasClusterB());
                    }
                }

                tiles.add(new BackgroundTile(x, y, rotation, texture, clearTile, galaxies));
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
                galaxy.add(TextureComponent.create(engine)
                        .setRegion(bg.galaxies.get(galaxyPos)));

                galaxy.add(TransformComponent.create(engine)
                        .setPosition(bg.x, bg.y, Z.bg_galaxy)
                        .setRotation(bg.rotation)
                        .setScale(1f, 1f));
//                galaxy.add(BoundsComponent.create(engine)
//                        .setBounds(bg.x - 4.6875f, bg.y - 4.6875f, 9.375f, 9.375f));
                galaxy.add(ScreenWrapComponent.create(engine)
                        .setMode(ScreenWrapMode.VERTICAL)
                        .setReversed(true)
                        .setWrapOffset(offset + additionalOffest)
                        .shouldRandomPerpendicularPosition(true)
                        .setMinMaxPos(0f, 20f)
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

            Entity clearTile = engine.createEntity();
            clearTile.add(TextureComponent.create(engine)
                    .setRegion(bg.clearImage));
            clearTile.add(TransformComponent.create(engine)
                    .setPosition(bg.x, bg.y, Z.bg_clear)
                    .setRotation(bg.rotation)
                    .setOpacity(0.5f)
                    .setScale(1f, 1f));
            clearTile.add(BoundsComponent.create(engine)
                    .setBounds(bg.x - tileHalfPoint, bg.y - tileHalfPoint, tileSize, tileSize));
            clearTile.add(ScreenWrapComponent.create(getEngine())
                    .setMode(ScreenWrapMode.VERTICAL)
                    .setReversed(true)
                    .setWrapOffset(offset));
            clearTile.add(VelocityComponent.create(engine)
                    .setSpeed(0f, bgClearSpeed));
            engine.addEntity(clearTile);
        }


        //Speed Lines
        for(int i=0;i<speedLineCount;i++){
            Entity sl = engine.createEntity();
            int speedIndex = rnd.nextInt(5) + 1;
            float x = K2MathUtil.getRandomInRange(0.1f, 19.8f);
            float y = K2MathUtil.getRandomInRange(5f, 45f);
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
                .setMinMaxPos(0.1f, 19.8f));
            engine.addEntity(sl);
        }

        if(isUsingStars) {
            //Stars
            for (int i = 0; i < numberOfStars; i++) {
                Entity star = engine.createEntity();
                float x = K2MathUtil.getRandomInRange(0.1f, 19.8f);
                float y = K2MathUtil.getRandomInRange(0f, 45f);
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
                        .setMinMaxPos(0.1f, 19.8f));
                engine.addEntity(star);
            }
        }

        if(isUsingStickers) {
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

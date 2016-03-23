package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.values.Z;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.ScreenWrapComponent;
import com.roaringcatgames.libgdxjam.components.ScreenWrapMode;
import com.roaringcatgames.libgdxjam.components.WhenOffScreenComponent;

import java.util.Random;

/**
 * Created by barry on 1/9/16 @ 6:35 PM.
 */
public class BackgroundSystem extends IteratingSystem {

    public float bgSpeed = -1f;
    private float left;
    private float bottom;
    private float right;
    private float top;

    private boolean isUsingStickers;
    private boolean isInitialized = false;

    protected class BackgroundTile extends BackgroundSticker{
        protected TextureRegion galaxy;
        protected  BackgroundTile(float x, float y, float rot, TextureRegion tile, TextureRegion galaxy){
            super(x, y, rot, tile);
            this.x = x;
            this.y = y;
            this.rotation = rot;
            this.image = tile;
            this.galaxy = galaxy;
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


    public BackgroundSystem(Vector2 minBounds, Vector2 maxBounds, boolean shouldProduceStickers){
        //No components will be modified here, just need a limited class to
        //create a family
        super(Family.all(PlayerComponent.class).get());
        this.left = minBounds.x;
        this.bottom = minBounds.y;
        this.right = maxBounds.x;
        this.top = maxBounds.y;
        this.isUsingStickers = shouldProduceStickers;
    }

    private void init(){
        PooledEngine engine = ((PooledEngine)getEngine());

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
                TextureRegion galaxy = null;
                float galaxyFloat = rnd.nextFloat();
                if(galaxyFloat < 0.5f){
                    if(galaxyFloat < 0.25f){
                        galaxy = Assets.getGalaxyA();
                    }else{
                        galaxy = Assets.getGalaxyB();
                    }
                }

                tiles.add(new BackgroundTile(x, y, rotation, texture, galaxy));
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
            if(bg.galaxy != null){
                Entity galaxy = engine.createEntity();
                galaxy.add(TextureComponent.create(engine)
                        .setRegion(bg.galaxy));

                galaxy.add(TransformComponent.create(engine)
                        .setPosition(bg.x, bg.y, Z.bg_galaxy)
                        .setRotation(bg.rotation)
                        .setScale(1f, 1f));
                galaxy.add(BoundsComponent.create(engine)
                        .setBounds(bg.x - 4.6875f, bg.y - 4.6875f, 9.375f, 9.375f));
                galaxy.add(ScreenWrapComponent.create(engine)
                        .setMode(ScreenWrapMode.VERTICAL)
                        .setReversed(true)
                        .setWrapOffset(offset));
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
            e.add(ScreenWrapComponent.create((PooledEngine)getEngine())
                    .setMode(ScreenWrapMode.VERTICAL)
                    .setReversed(true)
                    .setWrapOffset(offset));
            e.add(VelocityComponent.create(engine)
                    .setSpeed(0f, bgSpeed));
            engine.addEntity(e);
        }


        if(isUsingStickers) {
            int yStep = 35;
            int yIndex = 1;
            for (TextureRegion reg : Assets.getPlanets()) {
                int position = rnd.nextInt(10) + 5;
                float rot = rnd.nextFloat() * 360f;
                float width = (reg.getRegionWidth()/ App.PPM);
                float height = (reg.getRegionHeight()/App.PPM);

                Entity sticker = engine.createEntity();
                sticker.add(TransformComponent.create(engine)
                        .setPosition(position, yIndex * yStep, Z.bgSticker)
                        .setRotation(rot)
                        .setOpacity(0.6f));
                sticker.add(RotationComponent.create(engine)
                    .setRotationSpeed(0.25f));
                sticker.add(TextureComponent.create(engine)
                        .setRegion(reg));
                sticker.add(VelocityComponent.create(engine)
                        .setSpeed(0f, bgSpeed));
                sticker.add(KinematicComponent.create(engine));
                sticker.add(BoundsComponent.create(engine)
                    .setBounds(position - (width / 2f), (yIndex * yStep) - (height / 2f), width, height));
                sticker.add(WhenOffScreenComponent.create((PooledEngine)getEngine()));
                engine.addEntity(sticker);
                yIndex++;
            }
        }


        isInitialized = true;
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

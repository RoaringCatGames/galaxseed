package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.TextureComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.kitten2d.ashley.components.VelocityComponent;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.Z;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.ScreenWrapComponent;
import com.roaringcatgames.libgdxjam.components.ScreenWrapMode;

import java.util.Random;

/**
 * Created by barry on 1/9/16 @ 6:35 PM.
 */
public class BackgroundSystem extends IteratingSystem {

    private float left;
    private float bottom;
    private float right;
    private float top;

    private boolean isInitialized = false;

    public BackgroundSystem(Vector2 minBounds, Vector2 maxBounds){
        //No components will be modified here, just need a limited class to
        //create a family
        super(Family.all(PlayerComponent.class).get());
        this.left = minBounds.x;
        this.bottom = minBounds.y;
        this.right = maxBounds.x;
        this.top = maxBounds.y;
    }

    private void init(){
        PooledEngine engine = ((PooledEngine)getEngine());

        Entity vp = engine.createEntity();
        vp.add(BoundsComponent.create()
            .setBounds(left, bottom, (right-left), (top-bottom)));
        engine.addEntity(vp);
        float tileSize = 16f;
        float tileHalfPoint = 8f;

        float startX = left-tileHalfPoint;
        float startY = bottom-tileHalfPoint;
        float xTileCoverage = (right + tileHalfPoint) - (startX);
        float yTileCoverage = (top + tileHalfPoint) - (startY);

        int columns = (int)Math.ceil(xTileCoverage/tileSize);
        int rows = (int)Math.ceil(yTileCoverage/tileSize);

        float offset = (rows*tileSize) - ((top + tileHalfPoint) - (startY));


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

                //Sometimes add a galaxy
                if(textVal < 0.5f){
                    Entity galaxy = engine.createEntity();
                    if(textVal < 0.25f) {
                        galaxy.add(TextureComponent.create()
                                .setRegion(Assets.getGalaxyA()));
                    }else{
                        galaxy.add(TextureComponent.create()
                                .setRegion(Assets.getGalaxyB()));
                    }
                    galaxy.add(TransformComponent.create()
                            .setPosition(x, y, Z.bg_galaxy)
                            .setRotation(rotation));
                    galaxy.add(BoundsComponent.create()
                            .setBounds(x - 4.6875f, y - 4.6875f, 9.375f, 9.375f));
                    galaxy.add(ScreenWrapComponent.create()
                            .setMode(ScreenWrapMode.VERTICAL)
                            .setReversed(true)
                            .setWrapOffset(offset));
                    galaxy.add(VelocityComponent.create()
                            .setSpeed(0f, -0.5f));
                    engine.addEntity(galaxy);
                }

                Entity e = engine.createEntity();
                e.add(TextureComponent.create()
                    .setRegion(texture));
                e.add(TransformComponent.create()
                    .setPosition(x, y, Z.bg)
                    .setRotation(rotation));
                e.add(BoundsComponent.create()
                    .setBounds(x-tileHalfPoint, y-tileHalfPoint, tileSize, tileSize));
                e.add(ScreenWrapComponent.create()
                    .setMode(ScreenWrapMode.VERTICAL)
                    .setReversed(true)
                    .setWrapOffset(offset));
                e.add(VelocityComponent.create()
                    .setSpeed(0f, -0.5f));
                engine.addEntity(e);
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

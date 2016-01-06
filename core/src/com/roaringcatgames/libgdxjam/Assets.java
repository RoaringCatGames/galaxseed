package com.roaringcatgames.libgdxjam;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by barry on 12/22/15 @ 7:32 PM.
 */
public class Assets {

    public static AssetManager am;

    public static AssetManager load(){
        am = new AssetManager();

        am.load(LOADING_ATLAS, TEXTURE_ATLAS);
        am.load(SPRITE_ATLAS, TEXTURE_ATLAS);
        am.finishLoading();
        am.load(ANI_ATLAS, TEXTURE_ATLAS);

        return am;
    }

    /***
     * SAFE IMMEDIATELY after am.load() is called
     * @return AtlasRegions for the loading animation.
     */
    public static Array<TextureAtlas.AtlasRegion> getLoadingFrames(){
        return am.get(LOADING_ATLAS, TEXTURE_ATLAS).findRegions("loading");
    }
    /***
     * SAFE IMMEDIATELY after am.load() is called
     * @return AtlasRegion for the moon sprite.
     */
    public static TextureAtlas.AtlasRegion getMoon(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("moon");
    }

    /***
     * SAFE IMMEDIATELY after am.load() is called
     * @return AtlasRegion for the bg Tile sprite.
     */
    public static TextureAtlas.AtlasRegion getBgTile(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("SpaceBG");
    }

    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for spaceship idle animation.
     */
    public static Array<TextureAtlas.AtlasRegion> getShipIdleFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("ship/ship-idle");
    }

    ////////////
    //SHIP FLIGHT
    ////////////
    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for spaceship flying animation.
     */
    public static Array<TextureAtlas.AtlasRegion> getShipFlyingFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("ship/ship-fly");
    }
    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for spaceship flying left animation.
     */
    public static Array<TextureAtlas.AtlasRegion> getShipFlyingLeftFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("ship/ship-fly-left");
    }
    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for spaceship flying right animation.
     */
    public static Array<TextureAtlas.AtlasRegion> getShipFlyingRightFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("ship/ship-fly-right");
    }

    /////////////
    //Bullets
    /////////////
    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for seed bullet animation
     */
    public static Array<TextureAtlas.AtlasRegion> getBulletFrames() {
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("bullets/seed");
    }

    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for seed bullet animation
     */
    public static Array<TextureAtlas.AtlasRegion> getBulletFlyingFrames() {
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("bullets/SeedFly");
    }

    private static Class<TextureAtlas> TEXTURE_ATLAS = TextureAtlas.class;
    private static Class<Music> MUSIC = Music.class;
    private static Class<BitmapFont> BITMAP_FONT = BitmapFont.class;
    private static Class<Sound> SOUND = Sound.class;

    private static final String FONT = "fonts/courier-new-bold-32.fnt";
    private static final String LOADING_ATLAS = "animations/loading.atlas";
    private static final String ANI_ATLAS = "animations/animations.atlas";
    private static final String SPRITE_ATLAS = "sprites/sprites.atlas";

}

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
        am.load(BG_MUSIC, MUSIC);
        am.load(GAME_OVER_MUSIC, MUSIC);
        am.load(FIRING_MUSIC, MUSIC);
        am.load(PLAYER_HIT_LIGHT_SFX, SOUND);
        am.load(PLAYER_HIT_MEDIUM_SFX, SOUND);
        am.load(PLAYER_HIT_HEAVY_SFX, SOUND);
        am.load(PLANET_POP_SFX, SOUND);
//        am.load(SEED_PLANT_SFX, SOUND);
//        am.load(SEED_HIT_SFX, SOUND);
        am.load(FONT_32, BITMAP_FONT);
        am.load(FONT_48, BITMAP_FONT);
        am.load(FONT_64, BITMAP_FONT);

        return am;
    }

    /***
     * SAFE IMMEDIATELY after am.load() is called
     * @return AtlasRegions for the loading animation.
     */
    public static Array<TextureAtlas.AtlasRegion> getLoadingFrames(){
        return am.get(LOADING_ATLAS, TEXTURE_ATLAS).findRegions("loading");
    }

    public static TextureAtlas.AtlasRegion getSplashTitle(){
        return am.get(LOADING_ATLAS, TEXTURE_ATLAS).findRegion("RCG");
    }

    /***
     * SAFE IMMEDIATELY after am.load() is called
     * @return AtlasRegion for the moon sprite.
     */
    public static TextureAtlas.AtlasRegion getMoon(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/moon");
    }

    public static TextureAtlas.AtlasRegion getTouchPoint(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("touch-point");
    }

    public static TextureAtlas.AtlasRegion getTitleImage(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("cherry title/Title");
    }
    public static TextureAtlas.AtlasRegion getStartButtonImage(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("cherry title/start");
    }

    public static Array<TextureAtlas.AtlasRegion> getTitleTreeFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("cherry title/cherry-title");
    }
    public static Array<TextureAtlas.AtlasRegion> getTitleTreeLeafFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("cherry title/cherry-title-leaf");
    }
    /***
     * SAFE IMMEDIATELY after am.load() is called
     * @return AtlasRegion for the bg Tile sprite.
     */
    public static TextureAtlas.AtlasRegion getBgATile(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("SpaceBG-a");
    }
    public static TextureAtlas.AtlasRegion getBgBTile(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("SpaceBG-b");
    }
    public static TextureAtlas.AtlasRegion getGalaxyA(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("galaxy-a");
    }
    public static TextureAtlas.AtlasRegion getGalaxyB(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("galaxy-b");
    }


    public static TextureAtlas.AtlasRegion getPlanetA(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/planet-a");
    }
    public static TextureAtlas.AtlasRegion getPlanetB(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/planet-b");
    }
    public static TextureAtlas.AtlasRegion getPlanetC(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/planet-c");
    }
    public static TextureAtlas.AtlasRegion getPlanetD(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/planet-d");
    }
    public static TextureAtlas.AtlasRegion getPlanetE(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/planet-e");
    }
    public static TextureAtlas.AtlasRegion getPlanetF(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/planet-f");
    }
    private static Array<TextureAtlas.AtlasRegion> planets;
    public static Array<TextureAtlas.AtlasRegion> getPlanets(){
        if(planets == null){
            planets = new Array<>();
            planets.add(getPlanetA());
            planets.add(getPlanetB());
            planets.add(getPlanetC());
            planets.add(getPlanetD());
            planets.add(getPlanetE());
            planets.add(getPlanetF());
            planets.add(getMoon());
        }

        return planets;
    }

    /////////////
    //PLAY
    /////////////
    public static Array<TextureAtlas.AtlasRegion> getPFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("PLAY/P");
    }
    public static Array<TextureAtlas.AtlasRegion> getLFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("PLAY/L");
    }
    public static Array<TextureAtlas.AtlasRegion> getAFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("PLAY/A");
    }
    public static Array<TextureAtlas.AtlasRegion> getYFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("PLAY/Y");
    }


    /////////////
    //Enemies
    /////////////
    public static TextureAtlas.AtlasRegion getAsteroidA(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-a/asteroid-a");
    }
    public static Array<TextureAtlas.AtlasRegion> getAsteroidAFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("asteroid-treed/asterroid-a");
    }
    private static Array<TextureAtlas.AtlasRegion> fragsA = new Array<>();
    public static Array<TextureAtlas.AtlasRegion> getAsteroidAFrags(){
        if(fragsA.size == 0){
            fragsA.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-a/asteroid-a-frag-a"));
            fragsA.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-a/asteroid-a-frag-b"));
            fragsA.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-a/asteroid-a-frag-c"));
        }

        return fragsA;
    }

    public static TextureAtlas.AtlasRegion getAsteroidB(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-b/asteroid-b");
    }
    public static Array<TextureAtlas.AtlasRegion> getAsteroidBFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("asteroid-treed/asterroid-b");
    }
    private static Array<TextureAtlas.AtlasRegion> fragsB = new Array<>();
    public static Array<TextureAtlas.AtlasRegion> getAsteroidBFrags(){
        if(fragsB.size == 0){
            fragsB.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-b/asteroid-b-frag-a"));
            fragsB.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-b/asteroid-b-frag-b"));
            fragsB.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-b/asteroid-b-frag-c"));
        }

        return fragsB;
    }

    public static TextureAtlas.AtlasRegion getAsteroidC(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-c/asteroid-c");
    }
    public static Array<TextureAtlas.AtlasRegion> getAsteroidCFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("asteroid-treed/asterroid-c");
    }
    private static Array<TextureAtlas.AtlasRegion> fragsC = new Array<>();
    public static Array<TextureAtlas.AtlasRegion> getAsteroidCFrags(){
        if(fragsC.size == 0){
            fragsC.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-c/asteroid-c-frag-a"));
            fragsC.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-c/asteroid-c-frag-b"));
            fragsC.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroid-c/asteroid-c-frag-c"));
        }

        return fragsC;
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
    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for ship flames.
     */
    public static Array<TextureAtlas.AtlasRegion> getFlamesFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("ship/flames");
    }
    public static Array<TextureAtlas.AtlasRegion> getIdleFlamesFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("ship/propel");
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

    /////////////
    //Life
    /////////////
    public static Array<TextureAtlas.AtlasRegion> getGreenTreeFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("trees/green");
    }
    public static Array<TextureAtlas.AtlasRegion> getPinkTreeFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("trees/pink");
    }
    public static Array<TextureAtlas.AtlasRegion> getPineTreeFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("trees/pine");
    }

    /////////////
    //Comets
    /////////////
    public static Array<TextureAtlas.AtlasRegion> getRedCometFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("enemies/comet-b");
    }
    public static Array<TextureAtlas.AtlasRegion> getBlueCometFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("enemies/comet-a");
    }


    /////////////
    //Music
    /////////////
    public static Music getBackgroundMusic(){
        return am.get(BG_MUSIC, MUSIC);
    }
    public static Music getGameOverMusic(){
        return am.get(GAME_OVER_MUSIC, MUSIC);
    }
    public static Music getFiringMusic(){
        return am.get(FIRING_MUSIC, MUSIC);
    }

    /////////////
    //SFX
    /////////////
    public static Sound getPlanetPopSfx(){
        return am.get(PLANET_POP_SFX, SOUND);
    }

    public static Sound getPlayerHitLight(){
        return am.get(PLAYER_HIT_LIGHT_SFX, SOUND);
    }

    public static Sound getPlayerHitMedium(){
        return am.get(PLAYER_HIT_MEDIUM_SFX, SOUND);
    }

    public static Sound getPlayerHitHeavy(){
        return am.get(PLAYER_HIT_HEAVY_SFX, SOUND);
    }

    /////////////
    //Fonts
    /////////////
    public static BitmapFont get32Font(){
        return am.get(FONT_32, BITMAP_FONT);
    }
    public static BitmapFont get48Font(){
        return am.get(FONT_48, BITMAP_FONT);
    }
    public static BitmapFont get64Font(){
        return am.get(FONT_64, BITMAP_FONT);
    }

    private static Class<TextureAtlas> TEXTURE_ATLAS = TextureAtlas.class;
    private static Class<Music> MUSIC = Music.class;
    private static Class<BitmapFont> BITMAP_FONT = BitmapFont.class;
    private static Class<Sound> SOUND = Sound.class;

    private static final String FONT_32 = "fonts/neuropol-32.fnt";
    private static final String FONT_48 = "fonts/neuropol-48.fnt";
    private static final String FONT_64 = "fonts/neuropol-64.fnt";
    private static final String LOADING_ATLAS = "animations/loading.atlas";
    private static final String ANI_ATLAS = "animations/animations.atlas";
    private static final String SPRITE_ATLAS = "sprites/sprites.atlas";
    private static final String BG_MUSIC = "music/metamorphosis-oga.mp3";
    private static final String GAME_OVER_MUSIC = "music/spacewalk.mp3";
    private static final String FIRING_MUSIC = "music/firing.mp3";
    private static final String PLANET_POP_SFX = "sfx/planet-pop.mp3";

    private static final String PLAYER_HIT_LIGHT_SFX = "sfx/player-hit-light.mp3";
    private static final String PLAYER_HIT_MEDIUM_SFX = "sfx/player-hit-medium.mp3";
    private static final String PLAYER_HIT_HEAVY_SFX = "sfx/player-hit-heavy.mp3";

}

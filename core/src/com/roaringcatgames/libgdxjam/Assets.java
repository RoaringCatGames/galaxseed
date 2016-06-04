package com.roaringcatgames.libgdxjam;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Accessor for our Asset Manager and all of our Assets. Handles
 * asset load order.
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
        am.load(FONT_32, BITMAP_FONT);
        am.load(FONT_48, BITMAP_FONT);
        am.load(FONT_64, BITMAP_FONT);
        am.load(FONT_128, BITMAP_FONT);
        am.load(PLAYER_HIT_LIGHT_SFX, SOUND);
        am.load(PLAYER_HIT_MEDIUM_SFX, SOUND);
        am.load(PLAYER_HIT_HEAVY_SFX, SOUND);
        am.load(PLANET_POP_SFX, SOUND);
        am.load(FIRING_SFX, SOUND);
        am.load(FLYING_MUSIC, MUSIC);

//        am.load(SEED_PLANT_SFX, SOUND);
//        am.load(SEED_HIT_SFX, SOUND);


        return am;
    }

    /***
     * SAFE IMMEDIATELY after am.load() is called
     * @return AtlasRegions for the loading animation.
     */
    public static Array<TextureAtlas.AtlasRegion> getLoadingFrames(){
        return am.get(LOADING_ATLAS, TEXTURE_ATLAS).findRegions("loading");
    }
    public static Array<TextureAtlas.AtlasRegion> getBubbleFrames(){
        return am.get(LOADING_ATLAS, TEXTURE_ATLAS).findRegions("bubble");
    }
    public static TextureAtlas.AtlasRegion getSplashTitle(){
        return am.get(LOADING_ATLAS, TEXTURE_ATLAS).findRegion("RCG");
    }

    /***
     * SAFE IMMEDIATELY after am.load() is called
     * @return AtlasRegion for the moon sprite.
     */
    public static TextureAtlas.AtlasRegion getTitleImage(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("cherry title/Title");
    }

    public static TextureAtlas.AtlasRegion getGalaxTitleImage(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("cherry title/galax");
    }
    public static TextureAtlas.AtlasRegion getSeedTitleImage(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("cherry title/seed");
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
    public static TextureAtlas.AtlasRegion getBgClearTileA(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("stars-a");
    }
    public static TextureAtlas.AtlasRegion getBgClearTileB(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("stars-b");
    }
    public static TextureAtlas.AtlasRegion getBgClearTileC(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("stars-c");
    }
    public static TextureAtlas.AtlasRegion getGalaxyA(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("galaxy-a");
    }
    public static TextureAtlas.AtlasRegion getGalaxyB(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("galaxy-b");
    }
    public static TextureAtlas.AtlasRegion getGalaxyC(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("galaxy-c");
    }
    public static TextureAtlas.AtlasRegion getGasCluster(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("GasCluster");
    }
    public static TextureAtlas.AtlasRegion getGasClusterA(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("GasCluster-a");
    }
    public static TextureAtlas.AtlasRegion getGasClusterB(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("GasCluster-b");
    }

    public static TextureAtlas.AtlasRegion getPluto(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/pluto");
    }
    public static TextureAtlas.AtlasRegion getNeptune(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/neptune");
    }
    public static TextureAtlas.AtlasRegion getUranus(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/uranus");
    }
    public static TextureAtlas.AtlasRegion getSaturn(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/saturn");
    }
    public static TextureAtlas.AtlasRegion getJupiterBottom(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/jupiter-b");
    }
    public static TextureAtlas.AtlasRegion getJupiterTop(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/jupiter-t");
    }
    public static TextureAtlas.AtlasRegion getMars(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/mars");
    }
    public static TextureAtlas.AtlasRegion getMoon(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/moon");
    }
    public static TextureAtlas.AtlasRegion getEarth(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("planets/earth");
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
    public static Array<TextureAtlas.AtlasRegion> getSwipeFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("swipe/swipe");
    }


    public static Array<TextureAtlas.AtlasRegion> getUpgradeFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("pickups/upgrade");
    }

    //////////////
    //BG
    //////////////
    public static TextureAtlas.AtlasRegion getSpeedLine(int i){
        if(i < 0){
            throw new IllegalArgumentException("Speed line is bad");
        }
        String name = "speed/speed" + ((i%4) + 1);
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion(name);
    }

    public static Array<TextureAtlas.AtlasRegion> getStarAFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("stars/stara");
    }
    public static Array<TextureAtlas.AtlasRegion> getStarBFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("stars/starb");
    }
    public static Array<TextureAtlas.AtlasRegion> getStarCFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("stars/starc");
    }

    /////////////
    //Enemies
    /////////////
    private static Array<TextureAtlas.AtlasRegion> frags = new Array<>();
    public static Array<TextureAtlas.AtlasRegion> getFrags(){
        if(frags.size == 0){
            frags.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroids/frag-a"));
            frags.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroids/frag-b"));
            frags.add(am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroids/frag-c"));
        }
        return frags;
    }

    public static TextureAtlas.AtlasRegion getAsteroidA(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroids/asteroid-a"); //asteroid-a/asteroid-a");
    }
    public static Array<TextureAtlas.AtlasRegion> getAsteroidAFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("asteroid-treed/asterroid-a");
    }

    public static TextureAtlas.AtlasRegion getAsteroidB(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroids/asteroid-b"); //asteroid-b/asteroid-b");
    }
    public static Array<TextureAtlas.AtlasRegion> getAsteroidBFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("asteroid-treed/asterroid-b");
    }

    public static TextureAtlas.AtlasRegion getAsteroidC(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("asteroids/asteroid-c"); //asteroid-c/asteroid-c");
    }
    public static Array<TextureAtlas.AtlasRegion> getAsteroidCFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("asteroid-treed/asterroid-c");
    }

    public static Array<TextureAtlas.AtlasRegion> getImpactA(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("impact/ImpactA/impact-asteroid-a");
    }
    public static Array<TextureAtlas.AtlasRegion> getImpactB(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("impact/ImpactB/impact-asteroid-b");
    }
    public static Array<TextureAtlas.AtlasRegion> getImpactC(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("impact/ImpactC/impact-asteroid-c");
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
    public static Array<TextureAtlas.AtlasRegion> getShipDeathFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("ship/ship-death");
    }

    public static Array<TextureAtlas.AtlasRegion> getWreckedCat(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("cat/death");
    }

    private static Array<TextureAtlas.AtlasRegion> shipParts;
    public static TextureAtlas.AtlasRegion getShipPart(int index){
        if(shipParts == null){
            shipParts = new Array<>();
            shipParts.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("ship/pieces/piece-a"));
            shipParts.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("ship/pieces/piece-b"));
            shipParts.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("ship/pieces/piece-c"));
            shipParts.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("ship/pieces/piece-d"));
            shipParts.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("ship/pieces/piece-e"));
            shipParts.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("ship/pieces/piece-f"));
            shipParts.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("ship/pieces/piece-g"));
            shipParts.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("ship/pieces/piece-h"));
            shipParts.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("ship/pieces/piece-i"));
            shipParts.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("ship/pieces/piece-j"));

        }
        return shipParts.get(index % shipParts.size);
    }

    public static TextureAtlas.AtlasRegion getSmoke(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("ship/pieces/smoke");
    }

    /////////////
    //Health Bar
    /////////////
    public static TextureAtlas.AtlasRegion getBranch(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegion("health/branch");
    }
    private static Array<TextureAtlas.AtlasRegion> leaves;
    public static TextureAtlas.AtlasRegion getBranchLeaf(int index){
        if(leaves == null){
            leaves = new Array<>();
            leaves.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("health/leaf-a"));
            leaves.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("health/leaf-b"));
            leaves.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("health/leaf-c"));
            leaves.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("health/leaf-d"));
            leaves.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("health/leaf-e"));
            leaves.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("health/leaf-f"));
            leaves.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("health/leaf-g"));
            leaves.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("health/leaf-h"));
            leaves.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("health/leaf-i"));
            leaves.add(am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("health/leaf-j"));
        }
        if(index < leaves.size){
            return leaves.get(index);
        }else{
            return leaves.get(0);
        }
    }

    /////////////
    //Health Pack
    /////////////
    public static Array<TextureAtlas.AtlasRegion> getFertilizerFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("pickups/health");
    }
    public static TextureAtlas.AtlasRegion getFertilizerGlow(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("pickups/health-glow");
    }
    public static Array<TextureAtlas.AtlasRegion> getWaterCanFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("pickups/health-a");
    }
    public static TextureAtlas.AtlasRegion getWaterCanGlow(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegion("pickups/health-a-glow");
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

    public static Array<? extends TextureRegion> getMuzzleFrames() {
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("muzzleflash/muzzle");
    }

    public static Array<? extends TextureRegion> getGatlingIdle(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("guns/gatling-idle");
    }

    public static Array<? extends TextureRegion> getGatlingFiring(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("guns/gatling-firing");
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
    public static Array<TextureAtlas.AtlasRegion> getLeafFrames(){
        return am.get(SPRITE_ATLAS, TEXTURE_ATLAS).findRegions("leaves/leaf");
    }

    /////////////
    //Comets
    /////////////
    public static Array<TextureAtlas.AtlasRegion> getRedCometFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("enemies/comet-b");
    }
    public static Array<TextureAtlas.AtlasRegion> getRedCometFullFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("enemies/comet-full-b");
    }
    public static Array<TextureAtlas.AtlasRegion> getBlueCometFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("enemies/comet-a");
    }
    public static Array<TextureAtlas.AtlasRegion> getBlueCometFullFrames(){
        return am.get(ANI_ATLAS, TEXTURE_ATLAS).findRegions("enemies/comet-full-a");
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
    public static Music getFlyingMusic(){
        return am.get(FLYING_MUSIC, MUSIC);
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

    public static Sound getSeedFiring() {
        return am.get(FIRING_SFX, SOUND);
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
    public static BitmapFont get128Font() {
        return am.get(FONT_128, BITMAP_FONT);
    }

    private static Class<TextureAtlas> TEXTURE_ATLAS = TextureAtlas.class;
    private static Class<Music> MUSIC = Music.class;
    private static Class<BitmapFont> BITMAP_FONT = BitmapFont.class;
    private static Class<Sound> SOUND = Sound.class;

    private static final String FONT_32 = "fonts/phosphate-32.fnt";
    private static final String FONT_48 = "fonts/phosphate-48.fnt";
    private static final String FONT_64 = "fonts/phosphate-64.fnt";
    private static final String FONT_128 = "fonts/phosphate-128.fnt";
    private static final String LOADING_ATLAS = "animations/loading.atlas";
    private static final String ANI_ATLAS = "animations/animations.atlas";
    private static final String SPRITE_ATLAS = "sprites/sprites.atlas";
    private static final String BG_MUSIC = "music/metamorphosis-oga.mp3";
    private static final String GAME_OVER_MUSIC = "music/spacewalk.mp3";
    private static final String FIRING_SFX = "sfx/seed-fire.mp3";
    private static final String FLYING_MUSIC = "music/flying.mp3";

    private static final String PLANET_POP_SFX = "sfx/planet-pop.mp3";
    private static final String PLAYER_HIT_LIGHT_SFX = "sfx/player-hit-light.mp3";
    private static final String PLAYER_HIT_MEDIUM_SFX = "sfx/player-hit-medium.mp3";
    private static final String PLAYER_HIT_HEAVY_SFX = "sfx/player-hit-heavy.mp3";
}

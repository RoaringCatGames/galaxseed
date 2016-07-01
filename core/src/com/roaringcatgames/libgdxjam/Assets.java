package com.roaringcatgames.libgdxjam;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.roaringcatgames.libgdxjam.data.SpawnList;
import com.roaringcatgames.libgdxjam.data.SpawnListLoader;

/**
 * Accessor for our Asset Manager and all of our Assets. Handles
 * asset load order.
 */
public class Assets {

    public static AssetManager am;

    public static AssetManager load(){
        am = new AssetManager();
        am.setLoader(SpawnList.class, new SpawnListLoader(new InternalFileHandleResolver()));

        am.load("levels/1-level.json", SpawnList.class);

        am.load(LOADING_ATLAS, TEXTURE_ATLAS);
        am.load(SPRITE_ATLAS, TEXTURE_ATLAS);
        am.finishLoading();
        am.load(ANI_ATLAS, TEXTURE_ATLAS);
        am.load(BG_MUSIC, MUSIC);
        am.load(GAME_OVER_MUSIC, MUSIC);
        am.load(MENU_MUISC, MUSIC);
        am.load(FONT_16, BITMAP_FONT);
        am.load(FONT_24, BITMAP_FONT);
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
        return getCachedRegion(ANI_ATLAS, "cherry title/Title");
    }

    public static TextureAtlas.AtlasRegion getGalaxTitleImage(){
        return getCachedRegion(ANI_ATLAS, "cherry title/galax");
    }
    public static TextureAtlas.AtlasRegion getSeedTitleImage(){
        return getCachedRegion(ANI_ATLAS, "cherry title/seed");
    }

    public static Array<TextureAtlas.AtlasRegion> getTitleTreeFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "cherry title/cherry-title");
    }
    public static Array<TextureAtlas.AtlasRegion> getTitleTreeLeafFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "cherry title/cherry-title-leaf");
    }
    /***
     * SAFE IMMEDIATELY after am.load() is called
     * @return AtlasRegion for the bg Tile sprite.
     */
    public static TextureAtlas.AtlasRegion getBgATile(){
        return getCachedRegion(SPRITE_ATLAS, "SpaceBG-a");
    }
    public static TextureAtlas.AtlasRegion getBgBTile(){
        return getCachedRegion(SPRITE_ATLAS, "SpaceBG-b");
    }
    public static TextureAtlas.AtlasRegion getBgClearTileA(){
        return getCachedRegion(SPRITE_ATLAS, "stars-a");
    }
    public static TextureAtlas.AtlasRegion getBgClearTileB(){
        return getCachedRegion(SPRITE_ATLAS, "stars-b");
    }
    public static TextureAtlas.AtlasRegion getBgClearTileC(){
        return getCachedRegion(SPRITE_ATLAS, "stars-c");
    }
    public static TextureAtlas.AtlasRegion getGalaxyA(){
        return getCachedRegion(SPRITE_ATLAS, "galaxy-a");
    }
    public static TextureAtlas.AtlasRegion getGalaxyB(){
        return getCachedRegion(SPRITE_ATLAS, "galaxy-b");
    }
    public static TextureAtlas.AtlasRegion getGalaxyC(){
        return getCachedRegion(SPRITE_ATLAS, "galaxy-c");
    }
    public static TextureAtlas.AtlasRegion getGasCluster(){
        return getCachedRegion(SPRITE_ATLAS, "GasCluster");
    }
    public static TextureAtlas.AtlasRegion getGasClusterA(){
        return getCachedRegion(SPRITE_ATLAS, "GasCluster-a");
    }
    public static TextureAtlas.AtlasRegion getGasClusterB(){
        return getCachedRegion(SPRITE_ATLAS, "GasCluster-b");
    }

    public static TextureAtlas.AtlasRegion getPluto(){
        return getCachedRegion(SPRITE_ATLAS, "planets/pluto");
    }
    public static TextureAtlas.AtlasRegion getNeptune(){
        return getCachedRegion(SPRITE_ATLAS, "planets/neptune");
    }
    public static TextureAtlas.AtlasRegion getUranus(){
        return getCachedRegion(SPRITE_ATLAS, "planets/uranus");
    }
    public static TextureAtlas.AtlasRegion getSaturn(){
        return getCachedRegion(SPRITE_ATLAS, "planets/saturn");
    }
    public static TextureAtlas.AtlasRegion getJupiterBottom(){
        return getCachedRegion(SPRITE_ATLAS, "planets/jupiter-b");
    }
    public static TextureAtlas.AtlasRegion getJupiterTop(){
        return getCachedRegion(SPRITE_ATLAS, "planets/jupiter-t");
    }
    public static TextureAtlas.AtlasRegion getMars(){
        return getCachedRegion(SPRITE_ATLAS, "planets/mars");
    }
    public static TextureAtlas.AtlasRegion getMoon(){
        return getCachedRegion(SPRITE_ATLAS, "planets/moon");
    }
    public static TextureAtlas.AtlasRegion getEarth(){
        return getCachedRegion(SPRITE_ATLAS, "planets/earth");
    }

    /////////////
    //PLAY
    /////////////
    public static TextureAtlas.AtlasRegion getPlayAsteroid(){
        return getCachedRegion(SPRITE_ATLAS, "options/play");
    }
    public static TextureAtlas.AtlasRegion getOptionsAsteroid(){
        return getCachedRegion(SPRITE_ATLAS, "options/options");
    }
    public static TextureAtlas.AtlasRegion getBackAsteroid(){
        return getCachedRegion(SPRITE_ATLAS, "options/back");
    }
    public static Array<TextureAtlas.AtlasRegion> getPFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "PLAY/P");
    }
    public static Array<TextureAtlas.AtlasRegion> getLFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "PLAY/L");
    }
    public static Array<TextureAtlas.AtlasRegion> getAFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "PLAY/A");
    }
    public static Array<TextureAtlas.AtlasRegion> getYFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "PLAY/Y");
    }
    public static Array<TextureAtlas.AtlasRegion> getSwipeFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "swipe/swipe");
    }


    public static Array<TextureAtlas.AtlasRegion> getUpgradeFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "pickups/upgrade");
    }

    //////////////
    //BG
    //////////////
    public static TextureAtlas.AtlasRegion getSpeedLine(int i){
        if(i < 0){
            throw new IllegalArgumentException("Speed line is bad");
        }
        String name = "speed/speed" + ((i%4) + 1);
        return getCachedRegion(SPRITE_ATLAS, name);
    }

    public static Array<TextureAtlas.AtlasRegion> getStarAFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "stars/stara");
    }
    public static Array<TextureAtlas.AtlasRegion> getStarBFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "stars/starb");
    }
    public static Array<TextureAtlas.AtlasRegion> getStarCFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "stars/starc");
    }

    /////////////
    //Enemies
    /////////////
    private static Array<TextureAtlas.AtlasRegion> frags = new Array<>();
    public static Array<TextureAtlas.AtlasRegion> getFrags(){
        if(frags.size == 0){
            frags.add(getCachedRegion(SPRITE_ATLAS, "asteroids/frag-a"));
            frags.add(getCachedRegion(SPRITE_ATLAS, "asteroids/frag-b"));
            frags.add(getCachedRegion(SPRITE_ATLAS, "asteroids/frag-c"));
        }
        return frags;
    }

    public static TextureRegion getAsteroidA(){
        return getCachedRegion(SPRITE_ATLAS, "asteroids/asteroid-a");

    }
    public static Array<TextureAtlas.AtlasRegion> getAsteroidAFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "asteroid-treed/asterroid-a");
    }

    public static TextureRegion getAsteroidB(){
        return getCachedRegion(SPRITE_ATLAS, "asteroids/asteroid-b");
    }
    public static Array<TextureAtlas.AtlasRegion> getAsteroidBFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "asteroid-treed/asterroid-b");
    }


    public static TextureRegion getAsteroidC(){
        return getCachedRegion(SPRITE_ATLAS, "asteroids/asteroid-c");

    }
    public static Array<TextureAtlas.AtlasRegion> getAsteroidCFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "asteroid-treed/asterroid-c");
    }

    public static Array<TextureAtlas.AtlasRegion> getImpactA(){
        return getCachedAnimationFrames(ANI_ATLAS, "impact/ImpactA/impact-asteroid-a");
    }
    public static Array<TextureAtlas.AtlasRegion> getImpactB(){
        return getCachedAnimationFrames(ANI_ATLAS, "impact/ImpactB/impact-asteroid-b");
    }
    public static Array<TextureAtlas.AtlasRegion> getImpactC(){
        return getCachedAnimationFrames(ANI_ATLAS, "impact/ImpactC/impact-asteroid-c");
    }


    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for spaceship idle animation.
     */
    public static Array<TextureAtlas.AtlasRegion> getShipIdleFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "ship/ship-idle");
    }

    ////////////
    //SHIP FLIGHT
    ////////////
    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for spaceship flying animation.
     */
    public static Array<TextureAtlas.AtlasRegion> getShipFlyingFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "ship/ship-fly");
    }
    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for spaceship flying left animation.
     */
    public static Array<TextureAtlas.AtlasRegion> getShipFlyingLeftFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "ship/ship-fly-left");
    }
    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for spaceship flying right animation.
     */
    public static Array<TextureAtlas.AtlasRegion> getShipFlyingRightFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "ship/ship-fly-right");
    }
    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for ship flames.
     */
    public static Array<TextureAtlas.AtlasRegion> getFlamesFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "ship/flames");
    }
    public static Array<TextureAtlas.AtlasRegion> getIdleFlamesFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "ship/propel");
    }
    public static Array<TextureAtlas.AtlasRegion> getShipDeathFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "ship/ship-death");
    }

    public static Array<TextureAtlas.AtlasRegion> getWreckedCat(){
        return getCachedAnimationFrames(ANI_ATLAS, "cat/death");
    }

    private static Array<TextureAtlas.AtlasRegion> shipParts;
    public static TextureAtlas.AtlasRegion getShipPart(int index){
        if(shipParts == null){
            shipParts = new Array<>();
            shipParts.add(getCachedRegion(ANI_ATLAS, "ship/pieces/piece-a"));
            shipParts.add(getCachedRegion(ANI_ATLAS, "ship/pieces/piece-b"));
            shipParts.add(getCachedRegion(ANI_ATLAS, "ship/pieces/piece-c"));
            shipParts.add(getCachedRegion(ANI_ATLAS, "ship/pieces/piece-d"));
            shipParts.add(getCachedRegion(ANI_ATLAS, "ship/pieces/piece-e"));
            shipParts.add(getCachedRegion(ANI_ATLAS, "ship/pieces/piece-f"));
            shipParts.add(getCachedRegion(ANI_ATLAS, "ship/pieces/piece-g"));
            shipParts.add(getCachedRegion(ANI_ATLAS, "ship/pieces/piece-h"));
            shipParts.add(getCachedRegion(ANI_ATLAS, "ship/pieces/piece-i"));
            shipParts.add(getCachedRegion(ANI_ATLAS, "ship/pieces/piece-j"));

        }
        return shipParts.get(index % shipParts.size);
    }

    private static TextureRegion shipSmoke;
    public static TextureRegion getSmoke(){
        if(shipSmoke == null) {
            shipSmoke = getCachedRegion(ANI_ATLAS, "ship/pieces/smoke");
        }
        return shipSmoke;
    }

    /////////////
    //Health Bar
    /////////////
    public static TextureAtlas.AtlasRegion getBranch(){
        return getCachedRegion(SPRITE_ATLAS, "health/branch");
    }
    private static Array<TextureAtlas.AtlasRegion> leaves;
    public static TextureAtlas.AtlasRegion getBranchLeaf(int index){
        if(leaves == null){
            leaves = new Array<>();
            leaves.add(getCachedRegion(ANI_ATLAS, "health/leaf-a"));
            leaves.add(getCachedRegion(ANI_ATLAS, "health/leaf-b"));
            leaves.add(getCachedRegion(ANI_ATLAS, "health/leaf-c"));
            leaves.add(getCachedRegion(ANI_ATLAS, "health/leaf-d"));
            leaves.add(getCachedRegion(ANI_ATLAS, "health/leaf-e"));
            leaves.add(getCachedRegion(ANI_ATLAS, "health/leaf-f"));
            leaves.add(getCachedRegion(ANI_ATLAS, "health/leaf-g"));
            leaves.add(getCachedRegion(ANI_ATLAS, "health/leaf-h"));
            leaves.add(getCachedRegion(ANI_ATLAS, "health/leaf-i"));
            leaves.add(getCachedRegion(ANI_ATLAS, "health/leaf-j"));
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
        return getCachedAnimationFrames(ANI_ATLAS, "pickups/health");
    }
    public static TextureAtlas.AtlasRegion getFertilizerGlow(){
        return getCachedRegion(ANI_ATLAS, "pickups/health-glow");
    }
    public static Array<TextureAtlas.AtlasRegion> getWaterCanFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "pickups/health-a");
    }
    public static TextureAtlas.AtlasRegion getWaterCanGlow(){
        return getCachedRegion(ANI_ATLAS, "pickups/health-a-glow");
    }

    /////////////
    //Bullets
    /////////////
    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for seed bullet animation
     */
    public static Array<TextureAtlas.AtlasRegion> getBulletFrames() {
        return getCachedAnimationFrames(ANI_ATLAS, "bullets/seed");
    }
    /****
     * ONLY SAFE AFTER am.update() is finished.
     * @return AtlasRegions for seed bullet animation
     */
    public static Array<TextureAtlas.AtlasRegion> getBulletFlyingFrames() {
        return getCachedAnimationFrames(ANI_ATLAS, "bullets/SeedFly");
    }

    private static TextureRegion heliSeed;
    public static TextureRegion getHelicopterSeed() {
        if(heliSeed == null) {
            heliSeed = getCachedRegion(ANI_ATLAS, "guns/heli");
        }
        return heliSeed;
    }

    private static TextureRegion finalHeliSeed;
    public static TextureRegion getFinalHelicopterSeed() {
        if(finalHeliSeed == null) {
            finalHeliSeed = getCachedRegion(ANI_ATLAS, "guns/final");
        }
        return finalHeliSeed;
    }

    public static Array<? extends TextureRegion> getMuzzleFrames() {
        return getCachedAnimationFrames(ANI_ATLAS, "muzzleflash/muzzle");
    }

    public static Array<? extends TextureRegion> getGatlingIdle(){
        return getCachedAnimationFrames(ANI_ATLAS, "guns/gatling-idle");
    }

    public static Array<? extends TextureRegion> getGatlingFiring(){
        return getCachedAnimationFrames(ANI_ATLAS, "guns/gatling-firing");
    }
    public static Array<? extends TextureRegion> getGatlingMuzzle(){
        //Such a friendly flash, just wants to cuddle
        return getCachedAnimationFrames(ANI_ATLAS, "guns/nuzzle");
    }
    public static Array<? extends TextureRegion> getGatlingSmoke(){
        return getCachedAnimationFrames(ANI_ATLAS, "guns/smoke");
    }
    public static Array<? extends TextureRegion> getGatlingSmokeParticles(){
        return getCachedAnimationFrames(ANI_ATLAS, "guns/smoke-particle");
    }
    public static Array<? extends TextureRegion> getCannonIdle(){
        return getCachedAnimationFrames(ANI_ATLAS, "guns/cannon-idle");
    }
    public static Array<? extends TextureRegion> getCannonFiring(){
        return getCachedAnimationFrames(ANI_ATLAS, "guns/cannon-firing");
    }


    public static Array<? extends TextureRegion> getAuraFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "guns/aura");
    }
    public static Array<? extends TextureRegion> getAuraFinalFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "guns/aura-final");
    }
    public static Array<? extends TextureRegion> getDandyParticles(){
        return getCachedAnimationFrames(ANI_ATLAS, "guns/dandy-particle");
    }

    public static Array<? extends TextureRegion> getAuraPodFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "weapon-select/aura");
    }
    public static TextureAtlas.AtlasRegion getAuraLevel(int lvl){
        return getCachedRegion(ANI_ATLAS, "weapon-select/aura-lvl-" + lvl);
    }
    public static Array<? extends TextureRegion> getHelicopterPodFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "weapon-select/heli");
    }
    public static TextureAtlas.AtlasRegion getHelicopterLevel(int lvl){
        return getCachedRegion(ANI_ATLAS, "weapon-select/heli-lvl-" + lvl);
    }
    public static Array<? extends TextureRegion> getSeedPodFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "weapon-select/seed");
    }
    public static TextureAtlas.AtlasRegion getSeedLevel(int lvl){
        return getCachedRegion(ANI_ATLAS, "weapon-select/seed-lvl-" + lvl);
    }

    public static TextureAtlas.AtlasRegion getOverlay(){
        return getCachedRegion(SPRITE_ATLAS, "weapon-select/overlay");
    }


    /////////////
    //Life
    /////////////
    public static Array<TextureAtlas.AtlasRegion> getGreenTreeFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "trees/green");
    }
    public static Array<TextureAtlas.AtlasRegion> getPinkTreeFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "trees/pink");
    }
    public static Array<TextureAtlas.AtlasRegion> getPineTreeFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "trees/pine");
    }
    public static Array<TextureAtlas.AtlasRegion> getLeafFrames(){
        return getCachedAnimationFrames(SPRITE_ATLAS, "leaves/leaf");
    }

    /////////////
    //Comets
    /////////////
    public static Array<TextureAtlas.AtlasRegion> getRedCometFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "enemies/comet-b");
    }
    public static Array<TextureAtlas.AtlasRegion> getRedCometFullFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "enemies/comet-full-b");
    }
    public static Array<TextureAtlas.AtlasRegion> getBlueCometFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "enemies/comet-a");
    }
    public static Array<TextureAtlas.AtlasRegion> getBlueCometFullFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "enemies/comet-full-a");
    }


    /////////////
    //Messages
    /////////////
    public static TextureAtlas.AtlasRegion getTutorialMessage(){
        return getCachedRegion(SPRITE_ATLAS, "messages/plant-avoid");
    }


    /////////////
    //Options
    /////////////
    public static TextureAtlas.AtlasRegion getMusicOn(){
        return getCachedRegion(SPRITE_ATLAS, "options/music-on");
    }
    public static TextureAtlas.AtlasRegion getMusicOff(){
        return getCachedRegion(SPRITE_ATLAS, "options/music-off");
    }
    public static TextureAtlas.AtlasRegion getSfxOn(){
        return getCachedRegion(SPRITE_ATLAS, "options/sfx-on");
    }
    public static TextureAtlas.AtlasRegion getSfxOff(){
        return getCachedRegion(SPRITE_ATLAS, "options/sfx-off");
    }
    public static TextureAtlas.AtlasRegion getVibrationOn(){
        return getCachedRegion(SPRITE_ATLAS, "options/vibration-on");
    }
    public static TextureAtlas.AtlasRegion getVibrationOff(){
        return getCachedRegion(SPRITE_ATLAS, "options/vibration-off");
    }
    public static TextureAtlas.AtlasRegion getControlsSteady(){
        return getCachedRegion(SPRITE_ATLAS, "options/ctrl-steady");
    }
    public static TextureAtlas.AtlasRegion getControlsAmplified(){
        return getCachedRegion(SPRITE_ATLAS, "options/ctrl-amplified");
    }

    /////////////
    //Music
    /////////////
    public static Music getMenuMusic(){
        return am.get(MENU_MUISC, MUSIC);
    }
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
    public static BitmapFont get16Font() {
        return am.get(FONT_16, BITMAP_FONT);
    }
    public static BitmapFont get24Font() {
        return am.get(FONT_24, BITMAP_FONT);
    }
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

    private static ObjectMap<String, Array<TextureAtlas.AtlasRegion>> animationsCache = new ObjectMap<>();
    private static ObjectMap<String, TextureAtlas.AtlasRegion> spriteCache = new ObjectMap<>();

    private static Array<TextureAtlas.AtlasRegion> getCachedAnimationFrames(String atlasName, String name){
        if(!animationsCache.containsKey(name)){
            animationsCache.put(name, am.get(atlasName, TEXTURE_ATLAS).findRegions(name));
        }
        return animationsCache.get(name);
    }

    private static TextureAtlas.AtlasRegion getCachedRegion(String atlasName, String name){
        if(!spriteCache.containsKey(name)){
            spriteCache.put(name, am.get(atlasName, TEXTURE_ATLAS).findRegion(name));
        }
        return spriteCache.get(name);
    }

    private static final String FONT_16 = "fonts/skyhook-16.fnt";
    private static final String FONT_24 = "fonts/skyhook-24.fnt";
    private static final String FONT_32 = "fonts/skyhook-32.fnt";
    private static final String FONT_48 = "fonts/skyhook-48.fnt";
    private static final String FONT_64 = "fonts/skyhook-64.fnt";
    private static final String FONT_128 = "fonts/skyhook-128.fnt";
    private static final String LOADING_ATLAS = "animations/loading.atlas";
    private static final String ANI_ATLAS = "animations/animations.atlas";
    private static final String SPRITE_ATLAS = "sprites/sprites.atlas";
    private static final String BG_MUSIC = "music/nebulae-slower-70.mp3";//bgmusic.mp3";
    private static final String MENU_MUISC = "music/title.mp3";
    private static final String GAME_OVER_MUSIC = "music/spacewalk.mp3";
    private static final String FIRING_SFX = "sfx/seed-fire.mp3";
    private static final String FLYING_MUSIC = "music/flying.mp3";

    private static final String PLANET_POP_SFX = "sfx/planet-pop.mp3";
    private static final String PLAYER_HIT_LIGHT_SFX = "sfx/player-hit-light.mp3";
    private static final String PLAYER_HIT_MEDIUM_SFX = "sfx/player-hit-medium.mp3";
    private static final String PLAYER_HIT_HEAVY_SFX = "sfx/player-hit-heavy.mp3";


}

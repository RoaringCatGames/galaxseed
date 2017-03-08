package com.roaringcatgames.galaxseed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.roaringcatgames.galaxseed.data.*;

/**
 * Accessor for our Asset Manager and all of our Assets. Handles
 * asset load order.
 */
public class Assets {

    public enum BubbleColor{
        BLUE, RED, GREEN
    }

    public static AssetManager am;

    public static AssetManager load(){
        am = new AssetManager();
        am.setLoader(Level.class, new LevelLoader(new InternalFileHandleResolver()));
        //am.setLoader(EntityList.class, new EntityListLoader(new InternalFileHandleResolver()));

        am.load("levels/level-select.json", Level.class);
        am.load("levels/1-level.json", Level.class);
        am.load("levels/2-level.json", Level.class);
        am.load("levels/3-level.json", Level.class);
        am.load("levels/test-level.json", Level.class);

        am.load(LOADING_ATLAS, TEXTURE_ATLAS);
        am.load(SPRITE_ATLAS, TEXTURE_ATLAS);
        am.finishLoading();
        am.load(ANI_ATLAS, TEXTURE_ATLAS);
        am.load(GAME_OVER_MUSIC, MUSIC);
        am.load(MENU_MUISC, MUSIC);
        am.load(JUPITER_BG_MUSIC, MUSIC);
        am.load(JUPITER_END_MUSIC, MUSIC);
        am.load(KUPIER_BG_MUSIC, MUSIC);
        am.load(KUPIER_END_MUSIC, MUSIC);
        am.load(URANUS_BG_MUSIC, MUSIC);
        am.load(URANUS_END_MUSIC, MUSIC);
        am.load(NEPTUNE_BG_MUSIC, MUSIC);
        am.load(NEPTUNE_END_MUSIC, MUSIC);
        am.load(TUTORIAL_BG_MUSIC, MUSIC);
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
        am.load(EXPLOSTION_SFX, SOUND);
        am.load(SWISH_SFX, SOUND);
        am.load(BLOOM_1_SFX, SOUND);
        am.load(BLOOM_2_SFX, SOUND);
        am.load(BLOOM_3_SFX, SOUND);
        am.load(BLOOM_4_SFX, SOUND);
        am.load(BLOOM_5_SFX, SOUND);
        am.load(BLOOM_6_SFX, SOUND);
        am.load(PLANET_BORN_1, SOUND);
        am.load(PLANET_BORN_2, SOUND);
        am.load(PLANET_BORN_3, SOUND);
        am.load(FIRING_SFX, SOUND);
        am.load(SELECT_SFX, SOUND);
        am.load(CLICK_SFX, SOUND);
        am.load(UPGRADE_SFX, SOUND);

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
    public static TextureAtlas.AtlasRegion getBgStarsTileA(){
        return getCachedRegion(SPRITE_ATLAS, "stars-a");
    }
    public static TextureAtlas.AtlasRegion getBgStarsTileB(){
        return getCachedRegion(SPRITE_ATLAS, "stars-b");
    }
    public static TextureAtlas.AtlasRegion getBgStarsTileC(){
        return getCachedRegion(SPRITE_ATLAS, "stars-c");
    }
    public static TextureAtlas.AtlasRegion getBgCloudTileA(){
        return getCachedRegion(SPRITE_ATLAS, "clouds-a");
    }
    public static TextureAtlas.AtlasRegion getBgCloudTileB(){
        return getCachedRegion(SPRITE_ATLAS, "clouds-b");
    }
    public static TextureAtlas.AtlasRegion getBgCloudTileC(){
        return getCachedRegion(SPRITE_ATLAS, "clouds-c");
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
    public static TextureAtlas.AtlasRegion getGasClusterC(){
        return getCachedRegion(SPRITE_ATLAS, "GasCluster-c");
    }
    public static TextureAtlas.AtlasRegion getGasClusterD(){
        return getCachedRegion(SPRITE_ATLAS, "GasCluster-d");
    }
    public static TextureAtlas.AtlasRegion getGasClusterE(){
        return getCachedRegion(SPRITE_ATLAS, "GasCluster-e");
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
    public static TextureAtlas.AtlasRegion getSaturnOne(){
        return getCachedRegion(SPRITE_ATLAS, "planets/saturn-1");
    }
    public static TextureAtlas.AtlasRegion getSaturnTwo(){
        return getCachedRegion(SPRITE_ATLAS, "planets/saturn-2");
    }
    public static TextureAtlas.AtlasRegion getJupiterTopLeft(){
        return getCachedRegion(SPRITE_ATLAS, "planets/jupiter-1");
    }
    public static TextureAtlas.AtlasRegion getJupiterTopRight(){
        return getCachedRegion(SPRITE_ATLAS, "planets/jupiter-2");
    }
    public static TextureAtlas.AtlasRegion getJupiterBottomrRight(){
        return getCachedRegion(SPRITE_ATLAS, "planets/jupiter-3");
    }
    public static TextureAtlas.AtlasRegion getJupiterBottomLeft(){
        return getCachedRegion(SPRITE_ATLAS, "planets/jupiter-4");
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
    public static TextureAtlas.AtlasRegion getDonut(){
        return getCachedRegion(SPRITE_ATLAS, "planets/Donut");
    }

    public static TextureAtlas.AtlasRegion getLevelPlanet(int index){
        TextureAtlas.AtlasRegion region = null;
        switch(index){
            case 1:
                region =  getPluto();
                break;
            case 2:
                region = getNeptune();
                break;
            case 3:
                region = getUranus();
                break;
            case 4:
                region = getSaturnOne();
                break;
            case 5:
                region = getJupiterBottomLeft();
                break;
            case 6:
                region = getMars();
                break;
            case 7:
                region = getEarth();
                break;
            default:
                region = getDonut();
                break;
        }

        return region;
    }

    public static Level getLevelSelect(){
        return am.get("levels/level-select.json", Level.class);
    }
    public static Level getLevel1(){
        return am.get("levels/1-level.json", Level.class);
    }
    public static Level getLevel2(){
        return am.get("levels/2-level.json", Level.class);
    }public static Level getLevel3(){
        return am.get("levels/3-level.json", Level.class);
    }
    public static Level getTestLevel() { return am.get("levels/test-level.json", Level.class); }
//    public static EntityList getLevelSelectLayout(){
//        return am.get("levels/level-select-layout.json", EntityList.class);
//    }

    public static TextureAtlas.AtlasRegion getInfoBubbleBackground(){
       return getCachedRegion(SPRITE_ATLAS, "level-screen/bg");
    }

    public static TextureAtlas.AtlasRegion getInfoBubbleLevelName(int levelPos){
        return getCachedRegion(SPRITE_ATLAS, "level-screen/lvl-" + levelPos);
    }

    public static TextureAtlas.AtlasRegion getInfoBubbleTreeBG(){
        return getCachedRegion(SPRITE_ATLAS, "level-screen/tree-bg");
    }

    public static TextureAtlas.AtlasRegion getInfoBubbleTreeFilled(){
        return getCachedRegion(SPRITE_ATLAS, "level-screen/tree-filled");
    }

    public static Array<TextureAtlas.AtlasRegion> getInfoPlayButtonFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "level-screen/play");
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
    public static TextureAtlas.AtlasRegion getTeamAsteroid(){
        return getCachedRegion(SPRITE_ATLAS, "options/team");
    }
    public static TextureAtlas.AtlasRegion getLMGIcon(){
        return getCachedRegion(SPRITE_ATLAS, "options/lmg");
    }
    public static TextureAtlas.AtlasRegion getLibGdxIcon(){
        return getCachedRegion(SPRITE_ATLAS, "options/libgdx");
    }
    public static TextureAtlas.AtlasRegion getAshleyIcon(){
        return getCachedRegion(SPRITE_ATLAS, "options/ashley");
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
    //Google Play
    //////////////
    public static TextureAtlas.AtlasRegion getGPSConnectedIcon(){
        return getCachedRegion(SPRITE_ATLAS, "google-play/controller");
    }
    public static TextureAtlas.AtlasRegion getGPSDisconnectedIcon(){
        return getCachedRegion(SPRITE_ATLAS, "google-play/controller-off");
    }
    public static TextureAtlas.AtlasRegion getGPSAchievementIcon(){
        return getCachedRegion(SPRITE_ATLAS, "google-play/badge");
    }
    public static TextureAtlas.AtlasRegion getGPSLeaderIcon(){
        return getCachedRegion(SPRITE_ATLAS, "google-play/leader");
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
            frags.add(getCachedRegion(SPRITE_ATLAS, "asteroids/frag-d"));
            frags.add(getCachedRegion(SPRITE_ATLAS, "asteroids/frag-e"));
            frags.add(getCachedRegion(SPRITE_ATLAS, "asteroids/frag-f"));
            frags.add(getCachedRegion(SPRITE_ATLAS, "asteroids/frag-g"));
            frags.add(getCachedRegion(SPRITE_ATLAS, "asteroids/frag-h"));
            frags.add(getCachedRegion(SPRITE_ATLAS, "asteroids/frag-i"));

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


    public static Array<TextureAtlas.AtlasRegion> getAnimationFramesByName(String name){
        return getCachedAnimationFrames(ANI_ATLAS, name);
    }

    public static TextureAtlas.AtlasRegion getSpriteByName(String name){
        return getCachedRegion(SPRITE_ATLAS, name);
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
    public static Array<TextureAtlas.AtlasRegion> getGameOverFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "game-over/gameover");
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

    public static TextureAtlas.AtlasRegion getRelaunchButton(){
        return getCachedRegion(SPRITE_ATLAS, "options/relaunch");
    }

    public static TextureAtlas.AtlasRegion getExitButton(){
        return getCachedRegion(SPRITE_ATLAS, "options/exit");
    }

    private static TextureRegion shipSmoke;
    public static TextureRegion getSmoke(){
        if(shipSmoke == null) {
            shipSmoke = getCachedRegion(ANI_ATLAS, "ship/pieces/smoke");
        }
        return shipSmoke;
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

    public static Array<? extends TextureRegion> getShield(){
        return getCachedAnimationFrames(ANI_ATLAS, "shield/shield");
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
    public static Array<? extends TextureRegion> getAuraPodOffFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "weapon-select/aura-off");
    }
    public static TextureAtlas.AtlasRegion getAuraLevel(int lvl){
        return getCachedRegion(ANI_ATLAS, "weapon-select/aura-lvl-" + lvl);
    }
    public static Array<? extends TextureRegion> getHelicopterPodFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "weapon-select/heli");
    }
    public static Array<? extends TextureRegion> getHelicopterPodOffFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "weapon-select/heli-off");
    }
    public static TextureAtlas.AtlasRegion getHelicopterLevel(int lvl){
        return getCachedRegion(ANI_ATLAS, "weapon-select/heli-lvl-" + lvl);
    }
    public static Array<? extends TextureRegion> getSeedPodFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "weapon-select/seed");
    }
    public static Array<? extends TextureRegion> getSeedPodOffFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "weapon-select/seed-off");
    }
    public static TextureAtlas.AtlasRegion getSeedLevel(int lvl){
        return getCachedRegion(ANI_ATLAS, "weapon-select/seed-lvl-" + lvl);
    }

    public static TextureAtlas.AtlasRegion getOverlay(){
        return getCachedRegion(SPRITE_ATLAS, "weapon-select/overlay");
    }

    public static TextureAtlas.AtlasRegion getSelectInterface(){
        return getCachedRegion(SPRITE_ATLAS, "weapon-select/interface");
    }

    public static Array<? extends TextureRegion> getSelectArrowFrames(){
        return getCachedAnimationFrames(ANI_ATLAS, "weapon-select/arrow");
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
    public static TextureAtlas.AtlasRegion getWeaponChooseMessage(){
        return getCachedRegion(SPRITE_ATLAS, "messages/choose-weapon");
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
    public static TextureAtlas.AtlasRegion getArtCat(){
        return getCachedRegion(SPRITE_ATLAS, "options/art-cat");
    }
    public static TextureAtlas.AtlasRegion getCodeCat(){
        return getCachedRegion(SPRITE_ATLAS, "options/code-cat");
    }
    public static TextureAtlas.AtlasRegion getColonelCat(){
        return getCachedRegion(SPRITE_ATLAS, "options/kfp-cat");
    }
    public static TextureAtlas.AtlasRegion getGvgIcon(){
        return getCachedRegion(SPRITE_ATLAS, "options/gvg-logo");
    }

    /////////////
    //Music
    /////////////
    public static Music getMenuMusic(){
        return am.get(MENU_MUISC, MUSIC);
    }
    public static Music getGameOverMusic(){
        return am.get(GAME_OVER_MUSIC, MUSIC);
    }
    public static Music getLevelSelectMusic() { return am.get(GAME_OVER_MUSIC, MUSIC); }
    public static Music getJupiterBGMusic() { return am.get(JUPITER_BG_MUSIC, MUSIC); }
    public static Music getJupiterEndMusic() { return am.get(JUPITER_END_MUSIC, MUSIC); }
    public static Music getKupierBGMusic() { return am.get(KUPIER_BG_MUSIC, MUSIC); }
    public static Music getKupierEndMusic() { return am.get(KUPIER_END_MUSIC, MUSIC); }
    public static Music getNeptuneBGMusic() { return am.get(NEPTUNE_BG_MUSIC, MUSIC); }
    public static Music getNeptuneEndMusic() { return am.get(NEPTUNE_END_MUSIC, MUSIC); }
    public static Music getUranusBGMusic() { return am.get(URANUS_BG_MUSIC, MUSIC); }
    public static Music getUranusEndMusic() { return am.get(URANUS_END_MUSIC, MUSIC); }
    public static Music getTutorialBGMusic() { return am.get(TUTORIAL_BG_MUSIC, MUSIC); }

    /////////////
    //SFX
    /////////////
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

    public static Sound getExplosionSfx() {
        return am.get(EXPLOSTION_SFX, SOUND);
    }

    public static Sound getSwishSfx(){
        return am.get(SWISH_SFX, SOUND);
    }

    public static Sound getPlanetBornSfx(int index){
        Sound s;
        switch(index){
            case 0:
                s = am.get(PLANET_BORN_1, SOUND);
                break;
            case 1:
                s = am.get(PLANET_BORN_2, SOUND);
                break;
            case 2:
                s = am.get(PLANET_BORN_3, SOUND);
                break;
            default:
                s = am.get(PLANET_BORN_1, SOUND);
                break;
        }
        return s;
    }
    public static Sound getBloomSfx(int index) {
        Sound s;
        switch(index){
            case 1:
                s = am.get(BLOOM_1_SFX, SOUND);
                break;
            case 2:
                s = am.get(BLOOM_2_SFX, SOUND);
                break;
            case 3:
                s = am.get(BLOOM_3_SFX, SOUND);
                break;
            case 4:
                s = am.get(BLOOM_4_SFX, SOUND);
                break;
            case 5:
                s = am.get(BLOOM_5_SFX, SOUND);
                break;
            case 6:
                s = am.get(BLOOM_6_SFX, SOUND);
                break;
            default:
                s = am.get(BLOOM_1_SFX, SOUND);
                break;
        }
        return s;
    }

    public static Sound getSelectSfx(){
        return am.get(SELECT_SFX, SOUND);
    }
    public static Sound getUpgradeSfx(){
        return am.get(UPGRADE_SFX, SOUND);
    }
    public static Sound getClickSfx(){
        return am.get(CLICK_SFX, SOUND);
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

    public static Class<TextureAtlas> TEXTURE_ATLAS = TextureAtlas.class;
    public static Class<Music> MUSIC = Music.class;
    public static Class<BitmapFont> BITMAP_FONT = BitmapFont.class;
    public static Class<Sound> SOUND = Sound.class;

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

    private static final String FONT_16 = "fonts/kalibers-16.fnt";
    private static final String FONT_24 = "fonts/kalibers-24.fnt";
    private static final String FONT_32 = "fonts/kalibers-32.fnt";
    private static final String FONT_48 = "fonts/kalibers-48.fnt";
    private static final String FONT_64 = "fonts/kalibers-64.fnt";
    private static final String FONT_128 = "fonts/kalibers-128.fnt";
    private static final String LOADING_ATLAS = "animations/loading.atlas";
    private static final String ANI_ATLAS = "animations/animations.atlas";
    private static final String SPRITE_ATLAS = "sprites/sprites.atlas";

    private static final String MENU_MUISC = "music/title.mp3";
    private static final String GAME_OVER_MUSIC = "music/game-over.mp3";
    private static final String JUPITER_BG_MUSIC = "music/jupiter-bgm.mp3";
    private static final String JUPITER_END_MUSIC = "music/jupiter-results.mp3";
    private static final String KUPIER_BG_MUSIC = "music/kupier-bgm.mp3";
    private static final String KUPIER_END_MUSIC = "music/kupier-results.mp3";
    private static final String NEPTUNE_BG_MUSIC = "music/neptune-bgm.mp3";
    private static final String NEPTUNE_END_MUSIC = "music/neptune-results.mp3";
    private static final String URANUS_BG_MUSIC = "music/uranus-bgm.mp3";
    private static final String URANUS_END_MUSIC = "music/uranus-results.mp3";
    private static final String TUTORIAL_BG_MUSIC = "music/tutorial-bgm.mp3";

    private static final String FIRING_SFX = "sfx/seed-fire.mp3";
    private static final String SWISH_SFX = "sfx/swish.mp3";
    private static final String PLANET_POP_SFX = "sfx/planet-pop.mp3";
    private static final String PLAYER_HIT_LIGHT_SFX = "sfx/player-hit-light.mp3";
    private static final String PLAYER_HIT_MEDIUM_SFX = "sfx/player-hit-medium.mp3";
    private static final String PLAYER_HIT_HEAVY_SFX = "sfx/player-hit-heavy.mp3";
    private static final String EXPLOSTION_SFX = "sfx/explosion.mp3";
    private static final String BLOOM_1_SFX = "sfx/bloom-1.mp3";
    private static final String BLOOM_2_SFX = "sfx/bloom-2.mp3";
    private static final String BLOOM_3_SFX = "sfx/bloom-3.mp3";
    private static final String BLOOM_4_SFX = "sfx/bloom-4.mp3";
    private static final String BLOOM_5_SFX = "sfx/bloom-5.mp3";
    private static final String BLOOM_6_SFX = "sfx/bloom-6.mp3";
    private static final String PLANET_BORN_1 = "sfx/plant-born-1.mp3";
    private static final String PLANET_BORN_2 = "sfx/plant-born-2.mp3";
    private static final String PLANET_BORN_3 = "sfx/plant-born-3.mp3";
    private static final String SELECT_SFX = "sfx/select.mp3";
    private static final String UPGRADE_SFX = "sfx/upgrade.mp3";
    private static final String CLICK_SFX = "sfx/click.mp3";

    public static TextureRegion getDemoBrick() {
        return getCachedRegion(SPRITE_ATLAS, "options/demo");
    }
}

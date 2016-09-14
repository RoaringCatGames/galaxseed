package com.roaringcatgames.galaxseed;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * One place to initialize and build up all of our animations
 */
public class Animations {

    //Menu Screen
    private static Animation pMenu;
    private static Animation lMenu;
    private static Animation aMenu;
    private static Animation yMenu;
    private static Animation titleTree;
    private static Animation titleTreeLeaf;
    private static Animation swipeTutorial;

    //Game
    private static Animation starA;
    private static Animation starB;
    private static Animation starC;
    private static Animation greenTree;
    private static Animation pinkTree;
    private static Animation pineTree;

    private static Animation asteroidA;
    private static Animation asteroidB;
    private static Animation asteroidC;
    private static Animation redComet;
    private static Animation redCometFull;
    private static Animation blueComet;
    private static Animation blueCometFull;
    private static Animation healthFertilizer;
    private static Animation healthWaterCan;

    private static Animation bullet;
    private static Animation bulletFlying;

    private static Animation impactA;
    private static Animation impactB;
    private static Animation impactC;

    private static Animation shipIdle;
    private static Animation shipFlying;
    private static Animation shipFlyingLeft;
    private static Animation shipFlyingRight;
    private static Animation muzzle;
    private static Animation gatlingIdle;
    private static Animation gatlingFiring;
    private static Animation gatlingMuzzle;
    private static Animation gatlingSmoke;

    private static Animation cannonIdle;
    private static Animation cannonFiring;

    private static Animation aura;
    private static Animation auraFinal;

    private static Animation flamesIdle;
    private static Animation flames;

    private static Animation gameOver;
    private static Animation shipDeath;

    private static Animation upgrade;

    private static Animation seedPod;
    private static Animation helicopterPod;
    private static Animation auraPod;

    private static Animation seedPodOff;
    private static Animation helicopterPodOff;
    private static Animation auraPodOff;

    private static Animation arrow;

    private static Animation shield;

    private static Animation rawry;

    public static void init(){
        pMenu = new Animation(1f/6f, Assets.getPFrames());
        lMenu = new Animation(1f/6f, Assets.getLFrames());
        aMenu = new Animation(1f/6f, Assets.getAFrames());
        yMenu = new Animation(1f/6f, Assets.getYFrames());

        titleTree = new Animation(1f/12f, Assets.getTitleTreeFrames());
        titleTreeLeaf = new Animation(1f/12f, Assets.getTitleTreeLeafFrames(), Animation.PlayMode.LOOP);
        swipeTutorial = new Animation(1f/15f, Assets.getSwipeFrames());


        starA = new Animation(1f/3f, Assets.getStarAFrames());
        starB = new Animation(1f/3f, Assets.getStarBFrames());
        starC = new Animation(1f/3f, Assets.getStarCFrames());

        greenTree = new Animation(1f/18f, Assets.getGreenTreeFrames());
        pinkTree = new Animation(1f/18f, Assets.getPinkTreeFrames());
        pineTree = new Animation(1f/18f, Assets.getPineTreeFrames());

        asteroidA = new Animation(1f/6f, Assets.getAsteroidAFrames());
        asteroidB = new Animation(1f/6f, Assets.getAsteroidBFrames());
        asteroidC = new Animation(1f/6f, Assets.getAsteroidCFrames());
        redComet = new Animation(1f/12f, Assets.getRedCometFrames(), Animation.PlayMode.LOOP_PINGPONG);
        redCometFull = new Animation(1f/12f, Assets.getRedCometFullFrames());
        blueComet = new Animation(1f/12f, Assets.getBlueCometFrames(), Animation.PlayMode.LOOP_PINGPONG);
        blueCometFull = new Animation(1f/12f, Assets.getBlueCometFullFrames());
//        healthFertilizer = new Animation(1f/4f, Assets.getFertilizerFrames());
//        healthWaterCan = new Animation(1f/4f, Assets.getWaterCanFrames());

        bullet = new Animation(1f/6f, Assets.getBulletFrames());
        bulletFlying = new Animation(1f/6f, Assets.getBulletFlyingFrames());

        impactA = new Animation(1f/12f, Assets.getImpactA());
        impactB = new Animation(1f/12f, Assets.getImpactB());
        impactC = new Animation(1f/12f, Assets.getImpactC());

        shipIdle = new Animation(1f/6f, Assets.getShipIdleFrames());
        shipFlying = new Animation(1f/12f, Assets.getShipFlyingFrames());
        shipFlyingLeft = new Animation(1f/6f, Assets.getShipFlyingLeftFrames());
        shipFlyingRight = new Animation(1f/6f, Assets.getShipFlyingRightFrames());
        muzzle = new Animation((1f/8f)/6f, Assets.getMuzzleFrames());
        gatlingIdle = new Animation(1f/9f, Assets.getGatlingIdle());
        gatlingFiring = new Animation(1f/9f, Assets.getGatlingFiring());
        gatlingMuzzle = new Animation(1f/144f, Assets.getGatlingMuzzle());
        gatlingSmoke = new Animation(1f/144f, Assets.getGatlingSmoke());

        cannonIdle = new Animation(1f, Assets.getCannonIdle());
        cannonFiring = new Animation(1f/12f, Assets.getCannonFiring());

        aura = new Animation(1f/6f, Assets.getAuraFrames());
        auraFinal = new Animation(1f/6f, Assets.getAuraFinalFrames());

        flamesIdle = new Animation(1f/9f, Assets.getIdleFlamesFrames());
        flames = new Animation(1f/9f, Assets.getFlamesFrames());

        gameOver = new Animation(1f/4f, Assets.getGameOverFrames());
        shipDeath = new Animation(1f/6f, Assets.getShipDeathFrames());

        upgrade = new Animation(1f/6f, Assets.getUpgradeFrames());

        seedPod = new Animation(1f/80f, Assets.getSeedPodFrames());
        helicopterPod = new Animation(1f/80f, Assets.getHelicopterPodFrames());
        auraPod = new Animation(1f/80f, Assets.getAuraPodFrames());

        seedPodOff = new Animation(1f/1f, Assets.getSeedPodOffFrames());
        helicopterPodOff = new Animation(1f/1f, Assets.getHelicopterPodOffFrames());
        auraPodOff = new Animation(1f/1f, Assets.getAuraPodOffFrames());

        arrow = new Animation(1f/120f, Assets.getSelectArrowFrames());

        shield = new Animation(1f/6f, Assets.getShield());

        rawry = new Animation(1f / 30f, Assets.getWreckedCat(), Animation.PlayMode.LOOP_PINGPONG);
    }

    public static Animation getpMenu() {
        return pMenu;
    }

    public static Animation getlMenu() {
        return lMenu;
    }

    public static Animation getaMenu() {
        return aMenu;
    }

    public static Animation getyMenu() {
        return yMenu;
    }

    public static Animation getTitleTree() {
        return titleTree;
    }

    public static Animation getTitleTreeLeaf() {
        return titleTreeLeaf;
    }

    public static Animation getSwipeTutorial() {
        return swipeTutorial;
    }

    public static Animation getStarA() {
        return starA;
    }

    public static Animation getStarB() {
        return starB;
    }

    public static Animation getStarC() {
        return starC;
    }

    public static Animation getGreenTree() {
        return greenTree;
    }

    public static Animation getPinkTree() {
        return pinkTree;
    }

    public static Animation getPineTree() {
        return pineTree;
    }

    public static Animation getAsteroidA() {
        return asteroidA;
    }

    public static Animation getAsteroidB() {
        return asteroidB;
    }

    public static Animation getAsteroidC() {
        return asteroidC;
    }

    public static Animation getHealthFertilizer() {
        return healthFertilizer;
    }

    public static Animation getHealthWaterCan() {
        return healthWaterCan;
    }

    public static Animation getRedComet() {
        return redComet;
    }

    public static Animation getRedCometFull() {
        return redCometFull;
    }

    public static Animation getBlueComet() {
        return blueComet;
    }

    public static Animation getBlueCometFull() {
        return blueCometFull;
    }

    public static Animation getBullet() {
        return bullet;
    }

    public static Animation getBulletFlying() {
        return bulletFlying;
    }

    public static Animation getImpactA() {
        return impactA;
    }

    public static Animation getImpactB() {
        return impactB;
    }

    public static Animation getImpactC() {
        return impactC;
    }

    public static Animation getShipIdle() {
        return shipIdle;
    }

    public static Animation getShipFlying() {
        return shipFlying;
    }

    public static Animation getShipFlyingLeft() {
        return shipFlyingLeft;
    }

    public static Animation getShipFlyingRight() {
        return shipFlyingRight;
    }

    public static Animation getMuzzle() {
        return muzzle;
    }

    public static Animation getGatlingIdle() {
        return gatlingIdle;
    }

    public static Animation getGatlingFiring() {
        return gatlingFiring;
    }

    public static Animation getGatlingMuzzle() {
        return gatlingMuzzle;
    }

    public static Animation getGatlingSmoke() {
        return gatlingSmoke;
    }

    public static Animation getCannonIdle() {
        return cannonIdle;
    }

    public static Animation getCannonFiring() {
        return cannonFiring;
    }

    public static Animation getAura() {
        return aura;
    }

    public static Animation getAuraFinal() {
        return auraFinal;
    }

    public static Animation getFlamesIdle() {
        return flamesIdle;
    }

    public static Animation getFlames() {
        return flames;
    }

    public static Animation getGameOver(){
        return gameOver;
    }

    public static Animation getShipDeath(){
        return shipDeath;
    }

    public static Animation getUpgrade(){
        return upgrade;
    }

    public static Animation getSeedPod() {
        return seedPod;
    }

    public static Animation getHelicopterPod() {
        return helicopterPod;
    }

    public static Animation getAuraPod() {
        return auraPod;
    }

    public static Animation getAuraPodOff() {
        return auraPodOff;
    }

    public static Animation getSeedPodOff() {
        return seedPodOff;
    }

    public static Animation getHelicopterPodOff() {
        return helicopterPodOff;
    }

    public static Animation getArrow() {
        return arrow;
    }

    public static Animation getShield(){
        return shield;
    }
    public static Animation getRawry(){
        return rawry;
    }

}

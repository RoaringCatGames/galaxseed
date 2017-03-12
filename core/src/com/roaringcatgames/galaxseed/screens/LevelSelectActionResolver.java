package com.roaringcatgames.galaxseed.screens;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.roaringcatgames.galaxseed.Animations;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.components.InfoBubbleComponent;
import com.roaringcatgames.galaxseed.data.LevelUtil;
import com.roaringcatgames.galaxseed.data.scores.LevelStats;
import com.roaringcatgames.galaxseed.values.Z;
import com.roaringcatgames.kitten2d.ashley.IActionResolver;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.components.*;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;

/**
 * Handles actions fired from the Level Select Screen
 */
public class LevelSelectActionResolver implements IActionResolver {

    private IGameProcessor game;

    public LevelSelectActionResolver(IGameProcessor game){
        this.game = game;
    }

    @Override
    public void resolveAction(String eventName, Entity firingEntity, Engine containerEngine) {
        Gdx.app.log("LevelSelectActionResolver", "Action Firing");
        switch(eventName){
            case "DISMISS_BUBBLE":
                for(Entity e:containerEngine.getEntitiesFor(Family.all(InfoBubbleComponent.class).get())){
                    containerEngine.removeEntity(e);
                }
                break;
            case "LEVEL_1":
                processLevelClicked(firingEntity, containerEngine, 1, LevelUtil.LEVEL_NAMES[0], "START_LEVEL_1");
                break;
            case "LEVEL_2":
                processLevelClicked(firingEntity, containerEngine, 2, LevelUtil.LEVEL_NAMES[1], "START_LEVEL_2");
                break;
            case "LEVEL_3":
                processLevelClicked(firingEntity, containerEngine, 3, LevelUtil.LEVEL_NAMES[2], "START_LEVEL_3");
                break;
            case "LEVEL_4":
                processLevelClicked(firingEntity, containerEngine, 4, LevelUtil.LEVEL_NAMES[3], "START_LEVEL_4");
                break;
            case "LEVEL_5":
                processLevelClicked(firingEntity, containerEngine, 5, LevelUtil.LEVEL_NAMES[4], "START_LEVEL_5");
                break;
            case "LEVEL_6":
                processLevelClicked(firingEntity, containerEngine, 6, LevelUtil.LEVEL_NAMES[5], "START_LEVEL_6");
                break;
            case "LEVEL_7":
                processLevelClicked(firingEntity, containerEngine, 7, LevelUtil.LEVEL_NAMES[6], "START_LEVEL_7");
                break;
            case "LEVEL_8":
                processLevelClicked(firingEntity, containerEngine, 8, LevelUtil.LEVEL_NAMES[7], "START_LEVEL_8");
                break;
            case "LEVEL_9":
                processLevelClicked(firingEntity, containerEngine, 9, LevelUtil.LEVEL_NAMES[8], "START_LEVEL_9");
                break;
            case "START_LEVEL_1":
                this.game.switchScreens("GAME_1");
                break;
            case "START_LEVEL_2":
                this.game.switchScreens("GAME_2");
                break;
            case "START_LEVEL_3":
                this.game.switchScreens("GAME_3");
                break;
            default:
                Gdx.app.log("LevelSelectActionResolver", "Fired Event: " + eventName);
                break;
        }
    }

    private void processLevelClicked(Entity firingEntity, Engine containerEngine, int level, String levelName, String playActionName){
        AnimationComponent ac = K2ComponentMappers.animation.get(firingEntity);
        ac.setPaused(false);

        Gdx.app.log("LevelSelectActionResolver", "Action Fired: " + levelName);
        String levelJson = this.game.getPreferenceManager()
                .getStoredString(LevelUtil.LEVEL_SCORE_PREFIX + levelName);
        LevelStats stats = LevelUtil.parseLevelStats(level, levelJson);
        int treeCount = LevelUtil.calculateTreeCount(stats);
        addLevelInfoBubble(levelName, playActionName, treeCount, firingEntity, containerEngine);

    }

    private void addLevelInfoBubble(String levelName,
                                    String playActionName,
                                    int treeCount,
                                    Entity selectAnchor,
                                    Engine engine){
        float infoTitleYOff = 3f;
        float infoTitleXOff = -3.35f;
        float infoPlayButtonYOff = -3f;
        float tweenTime = 0.5f;
        float targetX = this.game.getCamera().position.x;
        float targetY = this.game.getCamera().position.y;

        //Create the bg bubble intro
        TransformComponent tc = K2ComponentMappers.transform.get(selectAnchor);

        Entity overlay = engine.createEntity();
        overlay.add(InfoBubbleComponent.create(engine));
        overlay.add(TransformComponent.create(engine)
                .setPosition(App.W / 2f, game.getCamera().position.y, Z.info_bg)
                .setScale(App.PPM * App.W, App.H * App.PPM));
        overlay.add(ClickableComponent.create(engine)
            .setEventName("DISMISS_BUBBLE"));
        overlay.add(BoundsComponent.create(engine)
            .setBounds(0f, 0f, App.W, App.H));
        overlay.add(TextureComponent.create(engine)
                .setRegion(Assets.getOverlay()));

        Entity bgBubble = engine.createEntity();
        bgBubble.add(InfoBubbleComponent.create(engine));
        bgBubble.add(TransformComponent.create(engine)
            .setPosition(tc.position.x, tc.position.y, Z.info_bubble)
            .setScale(0.1f, 0.1f));
        bgBubble.add(TextureComponent.create(engine)
            .setRegion(Assets.getInfoBubbleBackground()));

        Timeline parallel = Timeline.createParallel()
                .push(Tween.to(bgBubble, K2EntityTweenAccessor.SCALE, tweenTime)
                    .target(1f, 1f)
                    .ease(TweenEquations.easeOutBack))
                .push(Tween.to(bgBubble, K2EntityTweenAccessor.POSITION_XY, tweenTime)
                        .target(targetX, targetY)
                        .ease(TweenEquations.easeOutBack));
        bgBubble.add(TweenComponent.create(engine)
            .setTimeline(parallel));

        Entity levelNameEntity = engine.createEntity();
        levelNameEntity.add(InfoBubbleComponent.create(engine));
        levelNameEntity.add(TransformComponent.create(engine)
            .setPosition(tc.position.x + infoTitleXOff, tc.position.y + infoTitleYOff, Z.info_name));
        levelNameEntity.add(FollowerComponent.create(engine)
            .setMode(FollowMode.STICKY)
            .setTarget(bgBubble)
            .setOffset(infoTitleXOff, infoTitleYOff));
        levelNameEntity.add(TextureComponent.create(engine)
            .setRegion(Assets.getInfoBubbleLevelName(LevelUtil.getLevelNumberByName(levelName))));

        Entity playButtonEntity = engine.createEntity();
        playButtonEntity.add(InfoBubbleComponent.create(engine));
        playButtonEntity.add(TransformComponent.create(engine)
            .setPosition(tc.position.x, tc.position.y + infoPlayButtonYOff, Z.info_play));
        playButtonEntity.add(FollowerComponent.create(engine)
            .setMode(FollowMode.STICKY)
            .setTarget(bgBubble)
            .setOffset(0f, infoPlayButtonYOff));
        playButtonEntity.add(TextureComponent.create(engine));
        playButtonEntity.add(AnimationComponent.create(engine)
            .addAnimation("DEFAULT", Animations.getPlayButton())
            .setPaused(true));
        playButtonEntity.add(ClickableComponent.create(engine)
            .setEventName(playActionName));
        playButtonEntity.add(BoundsComponent.create(engine)
            .setBounds(tc.position.x, tc.position.y + infoPlayButtonYOff, 3f, 2f));
        playButtonEntity.add(StateComponent.create(engine)
            .set("DEFAULT")
            .setLooping(false));

        float leftTreeXOffset = (12f/2f)/2f * -1f;
        float rightTreeXOffset = (12f/2f)/2f;
        float treeYOffset = 0f;

        Entity leftTree = engine.createEntity();
        leftTree.add(InfoBubbleComponent.create(engine));
        leftTree.add(TransformComponent.create(engine)
            .setPosition(tc.position.x + leftTreeXOffset, tc.position.y + treeYOffset, Z.info_score));
        leftTree.add(FollowerComponent.create(engine)
                .setMode(FollowMode.STICKY)
                .setTarget(bgBubble)
                .setOffset(leftTreeXOffset, treeYOffset));
        leftTree.add(TextureComponent.create(engine)
                .setRegion(treeCount >= 1 ? Assets.getInfoBubbleTreeFilled() : Assets.getInfoBubbleTreeBG()));

        Entity middleTree = engine.createEntity();
        middleTree.add(InfoBubbleComponent.create(engine));
        middleTree.add(TransformComponent.create(engine)
                .setPosition(tc.position.x, tc.position.y + treeYOffset, Z.info_score));
        middleTree.add(FollowerComponent.create(engine)
                .setMode(FollowMode.STICKY)
                .setTarget(bgBubble)
                .setOffset(0f, treeYOffset));
        middleTree.add(TextureComponent.create(engine)
                .setRegion(treeCount >= 2 ? Assets.getInfoBubbleTreeFilled() : Assets.getInfoBubbleTreeBG()));

        Entity rightTree = engine.createEntity();
        rightTree.add(InfoBubbleComponent.create(engine));
        rightTree.add(TransformComponent.create(engine)
                .setPosition(tc.position.x + rightTreeXOffset, tc.position.y + treeYOffset, Z.info_score));
        rightTree.add(FollowerComponent.create(engine)
                .setMode(FollowMode.STICKY)
                .setTarget(bgBubble)
                .setOffset(rightTreeXOffset, treeYOffset));
        rightTree.add(TextureComponent.create(engine)
                .setRegion(treeCount >= 3 ? Assets.getInfoBubbleTreeFilled() : Assets.getInfoBubbleTreeBG()));

        engine.addEntity(overlay);
        engine.addEntity(bgBubble);
        engine.addEntity(levelNameEntity);
        engine.addEntity(playButtonEntity);
        engine.addEntity(leftTree);
        engine.addEntity(middleTree);
        engine.addEntity(rightTree);
    }
}

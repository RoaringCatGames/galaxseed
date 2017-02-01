package com.roaringcatgames.galaxseed;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.roaringcatgames.galaxseed.data.entitydefs.Transform;
import com.roaringcatgames.galaxseed.data.scores.LevelStats;
import com.roaringcatgames.galaxseed.data.LevelUtil;
import com.roaringcatgames.galaxseed.values.Colors;
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
            case "LEVEL_1":
                AnimationComponent ac = K2ComponentMappers.animation.get(firingEntity);
                ac.setPaused(false);
                Gdx.app.log("LevelSelectActionResolver", "Action Fired");
                //game.switchScreens("GAME");
                String levelJson = this.game.getPreferenceManager()
                                            .getStoredString(LevelUtil.LEVEL_SCORE_PREFIX + LevelUtil.LEVEL_NAMES[0]);
                LevelStats stats = LevelUtil.parseLevelStats(1, levelJson);
                int treeCount = LevelUtil.calculateTreeCount(stats);
                addLevelInfoBubble(LevelUtil.LEVEL_NAMES[0], Assets.BubbleColor.GREEN, treeCount, firingEntity, containerEngine);
                break;
            case "LEVEL_2":

                break;
        }
    }

    private void addLevelInfoBubble(String levelName,
                                    Assets.BubbleColor bubbleColor,
                                    int treeCount,
                                    Entity selectAnchor,
                                    Engine engine){
        float infoTitleYOff = 2f;
        float infoTitleXOff = -2f;
        float tweenTime = 0.5f;
        //Create the bg bubble intro
        TransformComponent tc = K2ComponentMappers.transform.get(selectAnchor);
        Entity bgBubble = engine.createEntity();
        bgBubble.add(TransformComponent.create(engine)
            .setPosition(tc.position.x, tc.position.y, Z.info_bubble)
            .setScale(0.1f, 0.1f));

        bgBubble.add(TextureComponent.create(engine)
            .setRegion(Assets.getInfoBubbleBackground(bubbleColor)));

        Timeline parallel = Timeline.createParallel()
                .push(Tween.to(bgBubble, K2EntityTweenAccessor.SCALE, tweenTime)
                    .target(1f, 1f)
                    .ease(TweenEquations.easeOutBack))
                .push(Tween.to(bgBubble, K2EntityTweenAccessor.POSITION_Y, tweenTime)
                        .target(tc.position.y + 5f)
                        .ease(TweenEquations.easeOutBack));

        bgBubble.add(TweenComponent.create(engine)
            .setTimeline(parallel));

        Entity levelNameEntity = engine.createEntity();
        levelNameEntity.add(TransformComponent.create(engine)
            .setPosition(tc.position.x + infoTitleXOff, tc.position.y + infoTitleYOff, Z.info_name));
        levelNameEntity.add(FollowerComponent.create(engine)
            .setMode(FollowMode.STICKY)
            .setTarget(bgBubble)
            .setOffset(infoTitleXOff, infoTitleYOff));
        levelNameEntity.add(TextureComponent.create(engine)
            .setRegion(Assets.getInfoBubbleLevelName(LevelUtil.getLevelNumberByName(levelName))));

        float leftTreeXOffset = (5f/2f)/2f * -1f;
        float rightTreeXOffset = (5f/2f)/2f;
        float treeYOffset = 0f;

        Entity leftTree = engine.createEntity();
        leftTree.add(TransformComponent.create(engine)
            .setPosition(tc.position.x + leftTreeXOffset, tc.position.y + treeYOffset, Z.info_score)
            .setTint(treeCount >= 1 ? Colors.PLAIN_WHITE : Colors.DULLED_GRAY));
        leftTree.add(FollowerComponent.create(engine)
                .setMode(FollowMode.STICKY)
                .setTarget(bgBubble)
                .setOffset(leftTreeXOffset, treeYOffset));
        leftTree.add(TextureComponent.create(engine)
                .setRegion(Assets.getInfoBubbleTree()));

        Entity middleTree = engine.createEntity();
        middleTree.add(TransformComponent.create(engine)
                .setPosition(tc.position.x, tc.position.y + treeYOffset, Z.info_score)
                .setTint(treeCount >= 2 ? Colors.PLAIN_WHITE : Colors.DULLED_GRAY));
        middleTree.add(FollowerComponent.create(engine)
                .setMode(FollowMode.STICKY)
                .setTarget(bgBubble)
                .setOffset(0f, treeYOffset));
        middleTree.add(TextureComponent.create(engine)
                .setRegion(Assets.getInfoBubbleTree()));

        Entity rightTree = engine.createEntity();
        rightTree.add(TransformComponent.create(engine)
                .setPosition(tc.position.x + rightTreeXOffset, tc.position.y + treeYOffset, Z.info_score)
                .setTint(treeCount == 3 ? Colors.PLAIN_WHITE : Colors.DULLED_GRAY));
        rightTree.add(FollowerComponent.create(engine)
                .setMode(FollowMode.STICKY)
                .setTarget(bgBubble)
                .setOffset(rightTreeXOffset, treeYOffset));
        rightTree.add(TextureComponent.create(engine)
                .setRegion(Assets.getInfoBubbleTree()));

        engine.addEntity(bgBubble);
        engine.addEntity(levelNameEntity);
        engine.addEntity(leftTree);
        engine.addEntity(middleTree);
        engine.addEntity(rightTree);
    }
}

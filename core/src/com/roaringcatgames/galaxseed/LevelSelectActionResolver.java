package com.roaringcatgames.galaxseed;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.roaringcatgames.galaxseed.data.entitydefs.Transform;
import com.roaringcatgames.galaxseed.data.scores.LevelStats;
import com.roaringcatgames.galaxseed.data.scores.ScoreUtil;
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
                                            .getStoredString(ScoreUtil.LEVEL_SCORE_PREFIX + ScoreUtil.LEVEL_NAMES[0]);
                LevelStats stats = ScoreUtil.parseLevelStats(1, levelJson);
                int treeCount = ScoreUtil.calculateTreeCount(stats);
                addLevelInfoBubble("Level 1", Assets.BubbleColor.BLUE, treeCount, firingEntity, containerEngine);
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
        float infoTitleYOff = 2.5f;
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
            .setPosition(tc.position.x, tc.position.y + infoTitleYOff, Z.info_name)
            .setTint(Colors.GLOW_YELLOW));
        levelNameEntity.add(FollowerComponent.create(engine)
            .setMode(FollowMode.STICKY)
            .setTarget(bgBubble)
            .setOffset(0f, infoTitleYOff));
        levelNameEntity.add(TextComponent.create(engine)
            .setText(levelName)
            .setFont(Assets.get32Font()));

        engine.addEntity(bgBubble);
        engine.addEntity(levelNameEntity);
    }
}

package com.roaringcatgames.galaxseed.data.scores;

import com.badlogic.gdx.utils.Json;

/**
 * Utility for parsing Level Scores
 */
public class ScoreUtil {

    private static final String LEVEL_1 = "level-1";
    private static final String LEVEL_2 = "level-2";
    private static final String LEVEL_3 = "level-3";
    private static final String LEVEL_4 = "level-4";
    private static final String LEVEL_5 = "level-5";
    private static final String LEVEL_6 = "level-6";
    private static final String LEVEL_7 = "level-7";
    private static final String LEVEL_8 = "level-8";
    private static final String LEVEL_9 = "level-9";

    public static final String LEVEL_SCORE_PREFIX = "lvlscore_";
    public static final String[] LEVEL_NAMES =  new String[]{
        LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5,
        LEVEL_6, LEVEL_7, LEVEL_8, LEVEL_9
    };

    public static LevelStats parseLevelStats(int level, String levelJson){
        Json json = new Json();
        if(levelJson != null && !"".equals(levelJson.trim())){
            return json.fromJson(LevelStats.class, levelJson);
        }else{
            return new LevelStats(LEVEL_NAMES[level-1], 0, 0, 0, 0);
        }
    }

    public static int calculateTreeCount(LevelStats stats){
        if(stats.getPlayCount() == 0){
            return 0;
        }

        int[] scoreSplits = getScoreSplits(stats.getLevelName());

        return scoreSplits[2] <= stats.getHighScore() ? 3 :
                scoreSplits[1] <= stats.getHighScore() ? 2 :
                scoreSplits[0] <= stats.getHighScore() ? 1 :
                0;
    }


    private static int[] getScoreSplits(String levelName){
        int[] splits;
        switch(levelName){
            case LEVEL_1:
                splits = new int[] { 1, 2, 3 };
                break;
            default:
                splits = new int[] { 200, 200, 200 };
                break;
        }

        return splits;
    }
}

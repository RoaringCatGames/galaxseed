package com.roaringcatgames.galaxseed.data.scores;

/**
 * Class to hold statistics of a level
 */
public class LevelStats {

    private String levelName = "--NOT SET--";
    private int highScore = 0;
    private int weaponCount = 0;
    private int playCount = 0;
    private int failures = 0;

    public LevelStats(String name, int score, int weaponCount, int playCount, int failures){
        this.levelName = name;
        this.highScore = score;
        this.weaponCount = weaponCount;
        this.playCount = playCount;
        this.failures = failures;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getWeaponCount() {
        return weaponCount;
    }

    public int getPlayCount() {
        return playCount;
    }

    public int getFailures() {
        return failures;
    }
}

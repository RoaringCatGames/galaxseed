package com.roaringcatgames.galaxseed;

/**
 * Interface to define Achievement interactions
 */
public interface IGameServiceController {

    boolean isConnected();
    void connectToGameServices();

    void unlockAchievement(String name);
    void showAchievements();

    void submitScore(int score);
    void showLeaderBoard();
}

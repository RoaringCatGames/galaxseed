package com.roaringcatgames.galaxseed.systems;

/**
 * Configuration for the Background System
 */
public class BackgroundSystemConfig{
    private boolean shouldProduceStickers = true;
    private boolean shouldProduceStars = false;
    private boolean shouldProduceSpeedLines = false;
    private boolean shouldMove = true;

    public BackgroundSystemConfig(boolean shouldProduceStickers, boolean shouldProduceStars,
                                  boolean shouldProduceSpeedlines, boolean shouldMove){
        this.shouldProduceStickers = shouldProduceStickers;
        this.shouldProduceStars = shouldProduceStars;
        this.shouldProduceSpeedLines = shouldProduceSpeedlines;
        this.shouldMove = shouldMove;
    }

    public boolean isShouldProduceStickers() {
        return shouldProduceStickers;
    }

    public boolean isShouldProduceStars() {
        return shouldProduceStars;
    }

    public boolean isShouldProduceSpeedLines() {
        return shouldProduceSpeedLines;
    }

    public boolean isShouldMove() {
        return shouldMove;
    }
}
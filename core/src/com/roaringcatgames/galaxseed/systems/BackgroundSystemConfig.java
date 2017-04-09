package com.roaringcatgames.galaxseed.systems;

/**
 * Configuration for the Background System
 */
public class BackgroundSystemConfig{
    private boolean shouldProduceGalaxies = true;
    private boolean shouldProduceStickers = true;
    private boolean shouldProduceStars = false;
    private boolean shouldProduceSpeedLines = false;
    private boolean shouldMove = true;
    private boolean shouldRandomizePlanets = false;

    public BackgroundSystemConfig(boolean shouldProduceGalaxies, boolean shouldProduceStickers, boolean shouldProduceStars,
                                  boolean shouldProduceSpeedlines, boolean shouldMove, boolean shouldRandomizePlanets){
        this.shouldProduceGalaxies = shouldProduceGalaxies;
        this.shouldProduceStickers = shouldProduceStickers;
        this.shouldProduceStars = shouldProduceStars;
        this.shouldProduceSpeedLines = shouldProduceSpeedlines;
        this.shouldMove = shouldMove;
        this.shouldRandomizePlanets = shouldRandomizePlanets;
    }

    public boolean isShouldProduceGalaxies(){
        return shouldProduceGalaxies;
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

    public boolean isShouldRandomizePlanets() {
        return shouldRandomizePlanets;
    }
}
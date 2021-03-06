package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 1/14/16 @ 6:14 PM.
 */
public class SpawnerComponent implements Component, Pool.Poolable{

    public Array<TextureRegion> particleTextures = new Array<>();
    public float particleSpeed = 10f;
    public float spawnRate = 1f;
    public float lastSpawnTime = 0f;
    public float elapsedTime = 0f;
    public boolean isPaused = false;
    public SpawnStrategy strategy = SpawnStrategy.ALL_DIRECTIONS;

    public static SpawnerComponent create(PooledEngine engine){
        return engine.createComponent(SpawnerComponent.class);
    }

    public SpawnerComponent setParticleTextures(Array<? extends TextureRegion> regions){
        this.particleTextures.clear();
        this.particleTextures.addAll(regions);
        return this;
    }

    public SpawnerComponent setSpawnRate(float spawnsPerSecond){
        this.spawnRate = spawnsPerSecond;
        return this;
    }

    public SpawnerComponent setStrategy(SpawnStrategy strat){
        this.strategy = strat;
        return this;
    }

    public SpawnerComponent setParticleSpeed(float speed){
        this.particleSpeed = speed;
        return this;
    }

    public SpawnerComponent setPaused(boolean pause){
        this.isPaused = pause;
        return this;
    }

    @Override
    public void reset() {
        this.particleTextures.clear();
        this.spawnRate = 1f;
        this.lastSpawnTime = 0f;
        this.elapsedTime = 0f;
        this.isPaused = false;
        this.strategy = SpawnStrategy.ALL_DIRECTIONS;
    }
}

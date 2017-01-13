package com.roaringcatgames.galaxseed.data;

import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.galaxseed.components.EnemyType;

/**
 * Model for our Enemy Spawn
 */
public class EnemySpawn {

    public boolean hasSpawned = false;
    public float spawnTime;
    public boolean shouldGeneratePowerUp = false;
    public EnemyType enemyType;
    public SpawnPosition startPosition = new SpawnPosition();
    public SpawnPosition speed = new SpawnPosition();
    public SpawnPosition endPoint = new SpawnPosition();
    public SpawnPosition midBezierPoint = new SpawnPosition();

    public EnemySpawn(){
    }

    public EnemySpawn(float spawnTime, EnemyType eType, float xPos, float yPos, float xVel, float yVel, boolean shouldPowerUp){
        this.spawnTime = spawnTime;
        this.enemyType = eType;
        this.startPosition.set(xPos, yPos);
        this.speed.set(xVel, yVel);
        this.shouldGeneratePowerUp = shouldPowerUp;
    }
    public EnemySpawn(float spawnTime, EnemyType eType, float xPos, float yPos, float midX, float midY, float endX, float endY, float speed, boolean shouldPowerUp){
        this.spawnTime = spawnTime;
        this.enemyType = eType;
        this.startPosition.set(xPos, yPos);
        this.midBezierPoint.set(midX, midY);
        this.endPoint.set(endX, endY);
        this.speed.set(speed, speed);
        this.shouldGeneratePowerUp = shouldPowerUp;
    }
}

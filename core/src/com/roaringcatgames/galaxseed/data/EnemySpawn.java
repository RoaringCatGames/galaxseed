package com.roaringcatgames.galaxseed.data;

import com.roaringcatgames.galaxseed.components.EnemyType;

/**
 * Model for our Enemy Spawn
 */
public class EnemySpawn {

    public boolean hasSpawned = false;
    public float spawnTime;
    public boolean shouldGeneratePowerUp = false;
    public EnemyType enemyType;
    public QuadPositionedVector2 startPosition = new QuadPositionedVector2();
    public QuadPositionedVector2 speed = new QuadPositionedVector2();
    public QuadPositionedVector2 endPoint = new QuadPositionedVector2();
    public QuadPositionedVector2 midBezierPoint = new QuadPositionedVector2();

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

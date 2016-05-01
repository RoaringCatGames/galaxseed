package com.roaringcatgames.libgdxjam.data;

import com.badlogic.gdx.math.Vector2;
import com.roaringcatgames.libgdxjam.components.EnemyType;

/**
 * Created by barry on 5/1/16 @ 3:51 PM.
 */
public class EnemySpawn {

    public boolean hasSpawned = false;
    public float spawnTime;
    public EnemyType enemyType;
    public Vector2 startPosition = new Vector2();
    public Vector2 speed = new Vector2();

    public EnemySpawn(float spawnTime, EnemyType eType, float xPos, float yPos, float xVel, float yVel){
        this.spawnTime = spawnTime;
        this.enemyType = eType;
        this.startPosition.set(xPos, yPos);
        this.speed.set(xVel, yVel);
    }
}

package com.roaringcatgames.galaxseed.data;

import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.components.EnemyType;
import com.roaringcatgames.kitten2d.ashley.K2MathUtil;

import java.util.Random;

/**
 * Static class to generate spawns of enemies
 */
public class EnemySpawns {

    private static Random r = new Random();
    private static Level levelOneSpawns;
    public static Array<EnemySpawn> getLevelOneSpawns(){
        if(levelOneSpawns == null){
            levelOneSpawns = new Level();
            //levelOneSpawns = Assets.am.get("levels/1-level.json", Level.class);
            float left = -8f;
            float right = App.W + 8f;
            float topMin = (App.H + 5f) - 2f;
            float topMax = topMin + 6f;


            for(int i=0;i<360;i++){
                float x = i%2 == 0 && r.nextFloat() >= 0.3f ? left : right;
                float y = K2MathUtil.getRandomInRange(topMin, topMax);
                float xVel = K2MathUtil.getRandomInRange(2f, 8f) * (x != left ? -1f : 1f);
                float yVel = K2MathUtil.getRandomInRange(-2f, -6f);

                if(i >= 20 && i%20 == 0){
                    levelOneSpawns.spawns.add(new EnemySpawn((i * 6f) + 1f, EnemyType.ASTEROID_C, x, y, xVel, yVel));
                    levelOneSpawns.spawns.add(new EnemySpawn((i * 6f) + 5f, EnemyType.ASTEROID_C, x, y, xVel, yVel));
                }else if(i >= 8 && i%8 == 0){
                    levelOneSpawns.spawns.add(new EnemySpawn((i * 6f) + 1f, EnemyType.ASTEROID_B, x, y, xVel, yVel));
                    levelOneSpawns.spawns.add(new EnemySpawn((i * 6f) + 5f, EnemyType.ASTEROID_B, x, y, xVel, yVel));
                }else {
                    levelOneSpawns.spawns.add(new EnemySpawn((i * 6f) + 1f, EnemyType.ASTEROID_A, x, y, xVel, yVel));
                    levelOneSpawns.spawns.add(new EnemySpawn((i * 6f) + 5f, EnemyType.ASTEROID_A, x, y, xVel, yVel));
                }
            }

//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, left, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(5f, EnemyType.ASTEROID_A, -5f, 25f, 2f, 3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
//            levelOneSpawns.spawns.add(new EnemySpawn(3f, EnemyType.ASTEROID_A, 25f, 25f, -2f, -3f));
        }

        return levelOneSpawns.spawns;
    }

    public static void resetSpawns(){
        if(levelOneSpawns != null){
            for(EnemySpawn spawn:levelOneSpawns.spawns){
                spawn.hasSpawned = false;
            }
        }
    }
}

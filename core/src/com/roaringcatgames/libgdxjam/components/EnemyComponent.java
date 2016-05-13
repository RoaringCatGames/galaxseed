package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by barry on 1/10/16 @ 7:36 PM.
 */
public class EnemyComponent implements Component, Pool.Poolable {

    public EnemyType enemyType = EnemyType.ASTEROID_FRAG;
    public EnemyColor enemyColor = EnemyColor.BROWN;
    public boolean isDamaging = true;

    public static EnemyComponent create(PooledEngine engine) {
        return engine.createComponent(EnemyComponent.class);
    }

    public EnemyComponent setEnemyType(EnemyType eType){
        this.enemyType = eType;
        return this;
    }

    public EnemyComponent setEnemyColor(EnemyColor eColor){
        this.enemyColor = eColor;
        return this;
    }

    public EnemyComponent setDamaging(boolean shouldDamage){
        this.isDamaging = shouldDamage;
        return this;
    }

    @Override
    public void reset() {
        enemyType = EnemyType.ASTEROID_FRAG;
        isDamaging = true;
    }
}

package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

/**
 * Component to track enemy data
 */
public class EnemyComponent implements Component, Pool.Poolable {

    public EnemyType enemyType = EnemyType.ASTEROID_FRAG;
    public EnemyColor enemyColor = EnemyColor.BROWN;
    public boolean isDamaging = true;
    public boolean isPollenated = false;
    public boolean shouldGeneratePowerup = false;

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

    public EnemyComponent setPollenated(boolean pollenated){
        this.isPollenated = pollenated;
        return this;
    }

    public EnemyComponent setShouldGeneratePowerup(boolean shouldGenerate){
        this.shouldGeneratePowerup = shouldGenerate;
        return this;
    }

    @Override
    public void reset() {
        enemyType = EnemyType.ASTEROID_FRAG;
        isDamaging = true;
        isPollenated = false;
        shouldGeneratePowerup = false;
    }
}

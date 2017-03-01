package com.roaringcatgames.galaxseed.data;

import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.galaxseed.data.entitydefs.EntityDefinition;

/**
 * Data bag to hold a list of spawns
 */
public class Level {
    public int length = 0;
    public String name = "";
    public String id = "";
    public Array<EnemySpawn> spawns = new Array<EnemySpawn>();
    public Array<EntityDefinition> entities = new Array<EntityDefinition>();
}

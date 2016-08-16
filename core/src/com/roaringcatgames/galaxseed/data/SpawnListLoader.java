package com.roaringcatgames.galaxseed.data;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

/**
 * An asset loader to load in our level data
 */
public class SpawnListLoader extends AsynchronousAssetLoader<SpawnList, SpawnListLoader.EnemySpawnParameter> {

    private Json json = new Json();

    public SpawnListLoader(FileHandleResolver resolver){
        super(resolver);
    }


    private SpawnList spawns;

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, EnemySpawnParameter parameter) {
        spawns = null;
        spawns = json.fromJson(SpawnList.class, file.readString());
    }

    @Override
    public SpawnList loadSync(AssetManager manager, String fileName, FileHandle file, EnemySpawnParameter parameter) {
        SpawnList theSpawns = this.spawns;
        this.spawns = null;
        return theSpawns;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, EnemySpawnParameter parameter) {
        return null;
    }

    static public class EnemySpawnParameter extends AssetLoaderParameters<SpawnList>{

    }
}

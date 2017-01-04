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
public class LevelLoader extends AsynchronousAssetLoader<Level, LevelLoader.EnemySpawnParameter> {

    private Json json = new Json();

    public LevelLoader(FileHandleResolver resolver){
        super(resolver);
    }


    private Level level;

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, EnemySpawnParameter parameter) {
        level = null;
        level = json.fromJson(Level.class, file.readString());
    }

    @Override
    public Level loadSync(AssetManager manager, String fileName, FileHandle file, EnemySpawnParameter parameter) {
        Level theLevel = this.level;
        this.level = null;
        return theLevel;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, EnemySpawnParameter parameter) {
        return null;
    }

    static public class EnemySpawnParameter extends AssetLoaderParameters<Level>{

    }
}

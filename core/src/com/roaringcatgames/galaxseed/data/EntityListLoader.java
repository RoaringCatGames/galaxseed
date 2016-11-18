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
 * Asset loader to load up an EntityList definition
 */
public class EntityListLoader extends AsynchronousAssetLoader<EntityList, EntityListLoader.EntityListParameter> {
    private Json json = new Json();

    public EntityListLoader(FileHandleResolver resolver){
        super(resolver);
    }


    private EntityList entities;

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, EntityListParameter parameter) {
        entities = null;
        entities = json.fromJson(EntityList.class, file.readString());
    }

    @Override
    public EntityList loadSync(AssetManager manager, String fileName, FileHandle file, EntityListParameter parameter) {
        EntityList theEntities = this.entities;
        this.entities = null;
        return theEntities;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, EntityListParameter parameter) {
        return null;
    }

    static public class EntityListParameter extends AssetLoaderParameters<EntityList> {
    }
}

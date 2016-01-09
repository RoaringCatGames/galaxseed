package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by barry on 1/6/16 @ 7:55 PM.
 */
public class FollowerComponent implements Component{
    public Entity target = null;
    public Vector2 offset = new Vector2(0f, 0f);
    public FollowMode followMode = FollowMode.STICKY;

    public static FollowerComponent create(){
        return new FollowerComponent();
    }

    public FollowerComponent setTarget(Entity e){
        this.target = e;
        return this;
    }

    public FollowerComponent setOffset(float x, float y){
        this.offset.set(x, y);
        return this;
    }

    public FollowerComponent setMode(FollowMode mode){
        this.followMode = mode;
        return this;
    }


}

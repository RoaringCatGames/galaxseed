package com.roaringcatgames.galaxseed.data.entitydefs;

import com.badlogic.gdx.utils.Array;

/**
 * Defines an Entity in general, with the data that can be converted to Components
 *  as necessary.
 */
public class EntityDefinition {

    public Transform transform;
    public String spriteName;
    public AnimationSetDefinition animationSet;
    public Bounds bounds;
}

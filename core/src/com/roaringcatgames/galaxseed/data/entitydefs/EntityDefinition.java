package com.roaringcatgames.galaxseed.data.entitydefs;

/**
 * Defines an Entity in general, with the data that can be converted to Components
 *  as necessary.
 */
public class EntityDefinition {

    public String name;
    public Transform transform;
    public String spriteName;
    public AnimationSetDefinition animationSet;
    public Bounds bounds;
    public Clickable clickable;
}

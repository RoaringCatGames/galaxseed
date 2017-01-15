package com.roaringcatgames.galaxseed.data.entitydefs;

import com.badlogic.gdx.graphics.Color;
import com.roaringcatgames.galaxseed.data.QuadPositionedVector2;

/**
 * Serializable Transform Definition
 */
public class Transform {

    public QuadPositionedVector2 position;
    public float z;
    public float scaleX, scaleY;
    public Color tint;
    public float opacity;
    public float rotation;
    public boolean isHidden;
    public float originOffsetX, originOffsetY;
}

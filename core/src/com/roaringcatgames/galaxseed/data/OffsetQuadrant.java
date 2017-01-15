package com.roaringcatgames.galaxseed.data;

/**
 * Enum to help us define the corner points from which we can offset
 */
public enum OffsetQuadrant {
    QUAD_1, QUAD_2, QUAD_3, QUAD_4, CENTER,
    /**
     * Offset from center on X, and use direct value for Y
     */
    CENTER_X,
    /**
     * Use direct value for X, and offset from Y center for Y
     */
    CENTER_Y
}

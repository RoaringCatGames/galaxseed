package com.roaringcatgames.galaxseed.data.entitydefs;

import com.roaringcatgames.kitten2d.ashley.components.ClickableComponent;

/**
 * Defines how a ClickableComponent should be added to an entity
 */
public class Clickable {

    public String eventName;
    public int pointer = ClickableComponent.ALL_POINTERS;
}

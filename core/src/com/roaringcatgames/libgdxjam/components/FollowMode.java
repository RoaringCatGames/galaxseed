package com.roaringcatgames.libgdxjam.components;

/**
 * Created by barry on 1/6/16 @ 8:01 PM.
 */
public enum FollowMode {

    /***
     * The follower will stick with the target entity at the offset from the target's position.
     */
    STICKY,
    /***
     * The follower will move toward the target's position, plus the offest, at a given Velocity.
     */
    MOVETO
}

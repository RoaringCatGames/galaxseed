package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by barry on 1/8/16 @ 8:31 PM.
 */
public class ScreenWrapComponent implements Component {

    public ScreenWrapMode mode = ScreenWrapMode.HORIZONTAL;
    public boolean isReversed = false;
    public float wrapOffset = 0f;

    public static ScreenWrapComponent create(){
        return new ScreenWrapComponent();
    }

    public ScreenWrapComponent setMode(ScreenWrapMode newMode){
        this.mode = newMode;
        return this;
    }

    public ScreenWrapComponent setReversed(boolean isReversed){
        this.isReversed = isReversed;
        return this;
    }

    public ScreenWrapComponent setWrapOffset(float offset){
        this.wrapOffset = offset;
        return this;
    }
}

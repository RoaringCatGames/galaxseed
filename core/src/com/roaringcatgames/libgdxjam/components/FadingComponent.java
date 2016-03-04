package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by barry on 3/3/16 @ 10:08 PM.
 */
public class FadingComponent implements Component {

    public float percentPerSecond = 5f;

    public static FadingComponent create(){
        return new FadingComponent();
    }

    public FadingComponent setPercentPerSecond(float pfps){
        this.percentPerSecond = pfps;
        return this;
    }
}

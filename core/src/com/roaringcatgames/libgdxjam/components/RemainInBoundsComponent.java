package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by barry on 1/5/16 @ 7:25 PM.
 */
public class RemainInBoundsComponent implements Component {

    public BoundMode mode = BoundMode.CONTAINED;

    public static RemainInBoundsComponent create(){
        return new RemainInBoundsComponent();
    }

    public RemainInBoundsComponent setMode(BoundMode m){
        this.mode = m;
        return this;
    }
}

package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by barry on 1/3/16 @ 1:45 PM.
 */
public class WhenOffScreenComponent implements Component {

    public static WhenOffScreenComponent create(){
        return new WhenOffScreenComponent();
    }
}

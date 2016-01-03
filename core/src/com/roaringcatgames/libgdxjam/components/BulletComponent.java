package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by barry on 1/3/16 @ 1:25 AM.
 */
public class BulletComponent implements Component {

    public static BulletComponent create(){
        return new BulletComponent();
    }
}

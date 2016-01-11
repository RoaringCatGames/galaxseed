package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by barry on 1/10/16 @ 7:36 PM.
 */
public class EnemyComponent implements Component {

    public static EnemyComponent create(){
        return new EnemyComponent();
    }
}

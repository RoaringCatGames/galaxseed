package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by barry on 1/12/16 @ 8:01 PM.
 */
public class ProjectileComponent implements Component {

    public int damage = 0;

    public static ProjectileComponent create(){
        return new ProjectileComponent();
    }

    public ProjectileComponent setDamage(int dmg){
        this.damage = dmg;
        return this;
    }
}

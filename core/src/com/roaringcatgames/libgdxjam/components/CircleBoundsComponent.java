package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Circle;

/**
 * Created by barry on 1/14/16 @ 9:44 PM.
 */
public class CircleBoundsComponent implements Component {

    public Circle circle = new Circle();

    public static CircleBoundsComponent create(){
        return new CircleBoundsComponent();
    }

    public CircleBoundsComponent setCircle(float x, float y, float radius){
        this.circle.set(x, y, radius);
        return this;
    }
}

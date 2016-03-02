package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by barry on 3/1/16 @ 8:44 PM.
 */
public class TextComponent implements Component {

    public String text = "";
    public BitmapFont font;

    public static TextComponent create(){
        return new TextComponent();
    }

    public TextComponent setText(String text){
        this.text = text;
        return this;
    }

    public TextComponent setFont(BitmapFont font){
        this.font = font;
        return this;
    }
}

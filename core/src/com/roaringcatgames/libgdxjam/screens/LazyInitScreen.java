package com.roaringcatgames.libgdxjam.screens;

/**
 * Created by barry on 12/22/15 @ 5:55 PM.
 */

import com.badlogic.gdx.ScreenAdapter;

/**
 * Created by barry on 12/12/15 @ 10:57 AM.
 *
 * Lazy initialized Screens will wait until the render loop
 * has been called one time before
 */
public abstract class LazyInitScreen extends ScreenAdapter {

    protected boolean isInitialized = false;

    protected abstract void init();
    protected abstract void update(float deltaChange);

    @Override
    public void render(float delta) {
        super.render(delta);

        if(!isInitialized) {
            init();
            isInitialized = true;
        }

        update(delta);
    }
}

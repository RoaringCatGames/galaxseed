package com.roaringcatgames.libgdxjam.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.LifeInSpace;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(427, 640);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return App.Initialize();
        }
}
package com.roaringcatgames.galaxseed.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.roaringcatgames.galaxseed.App;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(427, 640);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return App.Initialize();
        }

        @Override
        public ApplicationListener createApplicationListener() {
                return App.Initialize();
        }
}
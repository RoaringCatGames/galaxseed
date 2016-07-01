package com.roaringcatgames.libgdxjam.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.LifeInSpace;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "GalaxSeed";
		config.height = 960;
		config.width = 640;
		config.samples = 4;
		config.resizable = false;
//		config.fullscreen = true;
//		config.vSyncEnabled = true;

		new LwjglApplication(App.Initialize(), config);
	}
}

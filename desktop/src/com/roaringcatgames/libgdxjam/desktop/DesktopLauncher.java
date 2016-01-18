package com.roaringcatgames.libgdxjam.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.LifeInSpace;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Life In Space";
		config.height = 960;
		config.width = 640;
		config.samples = 4;
		new LwjglApplication(App.Initialize(), config);
	}
}

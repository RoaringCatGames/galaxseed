package com.roaringcatgames.libgdxjam.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roaringcatgames.libgdxjam.LifeInSpace;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Life In Space";
		config.height = 960;
		config.width = 1280;
		new LwjglApplication(new LifeInSpace(), config);
	}
}

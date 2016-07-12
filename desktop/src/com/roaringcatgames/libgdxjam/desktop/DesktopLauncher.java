package com.roaringcatgames.libgdxjam.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roaringcatgames.libgdxjam.App;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.addIcon("icon-256.png", Files.FileType.Internal);
        config.addIcon("icon-128.png", Files.FileType.Internal);
		config.addIcon("icon-64.png", Files.FileType.Internal);
		config.addIcon("icon-32.png", Files.FileType.Internal);
		config.title = "GalaxSeed";
		config.height = 960;
		config.width = 960;
		config.samples = 4;
		config.resizable = false;
        config.fullscreen = true;

		new LwjglApplication(App.Initialize(), config);
	}
}

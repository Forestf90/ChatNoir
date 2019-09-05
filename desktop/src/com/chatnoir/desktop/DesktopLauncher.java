package com.chatnoir.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chatnoir.ChatNoir;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width =600;
		config.height =900;
		config.resizable = false;
		config.samples = 3;
		new LwjglApplication(new ChatNoir(), config);
	}
}

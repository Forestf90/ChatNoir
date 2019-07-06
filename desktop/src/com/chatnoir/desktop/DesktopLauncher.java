package com.chatnoir.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chatnoir.ChatNoir;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width =500;
		config.height =700;
		config.resizable = false;
		config.samples = 3;
		new LwjglApplication(new ChatNoir(), config);
	}
}

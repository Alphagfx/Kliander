package com.alphagfx.kliander.desktop;

import com.alphagfx.kliander.Kliander;
import com.alphagfx.kliander.utils.Constants;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.APP_WIDTH;
        config.height = Constants.APP_HEIGHT;
        new LwjglApplication(new Kliander(), config);
	}
}

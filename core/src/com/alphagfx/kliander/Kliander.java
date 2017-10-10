package com.alphagfx.kliander;

import com.alphagfx.kliander.screens.GameScreen;
import com.badlogic.gdx.Game;

public class Kliander extends Game {

	@Override
	public void create () {
        setScreen(new GameScreen());

    }


}

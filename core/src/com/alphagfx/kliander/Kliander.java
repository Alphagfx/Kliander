package com.alphagfx.kliander;

import com.alphagfx.kliander.screens.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Box2D;


public class Kliander extends Game {

	@Override
	public void create () {
        // have been told to use box2d.init (preferred)
        Box2D.init();
        Gdx.graphics.setUndecorated(true);
        setScreen(new GameScreen());
    }

    @Override
    public void render() {
        super.render();
    }
}

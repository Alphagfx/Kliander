package com.alphagfx.kliander.screens;

import com.alphagfx.kliander.stages.GameStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {

    private GameStage gameStage;

    public GameScreen() {
        gameStage = new GameStage();
    }

    @Override
    public void render(float delta) {
        //  Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.input.setInputProcessor(gameStage);

        gameStage.draw();
        gameStage.act(delta);
    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {
        gameStage.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }


    @Override
    public void dispose() {

    }
}

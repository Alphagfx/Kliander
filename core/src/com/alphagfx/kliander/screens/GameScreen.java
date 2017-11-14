package com.alphagfx.kliander.screens;

import com.alphagfx.kliander.stages.GameStage;
import com.alphagfx.kliander.stages.UIStage;
import com.alphagfx.kliander.utils.CameraHandle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen {

    private GameStage gameStage;
    private UIStage uiStage;
    private InputMultiplexer inputMultiplexer;

    private CameraHandle camera = new CameraHandle();

    public GameScreen() {

        camera = new CameraHandle();

        gameStage = new GameStage(camera);
        uiStage = new UIStage(new ScreenViewport(), gameStage);
        gameStage.updateActionSet();

        inputMultiplexer = new InputMultiplexer(uiStage, gameStage, camera.getCameraInput());

        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    public void switchStage() {
    }

    @Override
    public void render(float delta) {
        //  Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.getCameraInput().act();

        gameStage.act(delta);
        gameStage.draw();

        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {
        camera.getCameraInput().resize();
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
        gameStage.dispose();
        uiStage.dispose();
    }
}

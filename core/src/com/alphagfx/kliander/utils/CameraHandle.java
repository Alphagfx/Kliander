package com.alphagfx.kliander.utils;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class CameraHandle implements ApplicationListener {

    private OrthographicCamera camera;

    private float rotationSpeed;

    public Vector2 translateToWorld(int screenX, int screenY) {
        Vector2 vector2 = new Vector2();
        vector2.x = (float) screenX / Gdx.graphics.getWidth() * (camera.position.x * 2);
        vector2.y = (1 - (float) screenY / Gdx.graphics.getHeight()) * (camera.position.y * 2);
        return vector2;
    }

    //    debugRender Matrix requirement
    public Matrix4 getMatrixCombined() {
        return camera.combined;
    }


    @Override
    public void create() {
        rotationSpeed = 0.5f;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(30, 30 * (h / w));

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
    }

    @Override
    public void render() {
        handleInput();
        camera.update();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
            camera.zoom += 0.01;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            camera.zoom -= 0.01;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(1, 0);
        }

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100 / camera.viewportWidth);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);

    }

    //    Temporary quick-fix
    @Override
    public void resize(int width, int height) {

        int a = Gdx.graphics.getWidth();
        int b = Gdx.graphics.getHeight();

        camera.viewportWidth = 30f;
        camera.viewportHeight = 30f * b / a;
        camera.update();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void dispose() {

    }
}

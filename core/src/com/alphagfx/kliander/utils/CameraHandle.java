package com.alphagfx.kliander.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class CameraHandle extends ExtendViewport {

    private OrthographicCamera camera;
    private CameraInput cameraInput;

    public CameraHandle() {
        super(20, 20,
                new OrthographicCamera(30, (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth() * 30));

        camera = ((OrthographicCamera) getCamera());
        cameraInput = new CameraInput();

        camera.zoom = 3.3f;

    }

    public CameraInput getCameraInput() {
        return cameraInput;
    }

    public class CameraInput extends InputAdapter {

        private boolean camera_read;

        private void handleInput() {
            if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
                camera.zoom += 0.01;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
                camera.zoom -= 0.01;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                camera.translate(0, 1, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                camera.translate(0, -1, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                camera.translate(-1, 0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                camera.translate(1, 0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                Gdx.app.log("camera", "" + camera.viewportWidth + " " + camera.viewportHeight + " " + camera.zoom);
                Gdx.app.log("camera position", "" + camera.position);
            }

//            camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100 / camera.viewportWidth);

            float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
            float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

//            camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
//            camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);

        }

        //    Temporary quick-fix
        public void resize() {

            camera.viewportWidth = 30f;
            camera.viewportHeight = 30f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();

            handleInput();
        }

        public void act() {

            if (camera_read) {
                handleInput();
            }
        }

        @Override
        public boolean keyDown(int keycode) {

            camera_read = true;

            return true;
        }

        @Override
        public boolean keyUp(int keycode) {

            camera_read = false;

            return true;
        }

        @Override
        public boolean scrolled(int i) {
            camera.zoom += 0.02 * i;
            handleInput();

            return true;
        }
    }

}

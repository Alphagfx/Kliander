package com.alphagfx.kliander.utils;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class CameraHandle implements ApplicationListener {

    private OrthographicCamera camera;
    private ExtendViewport viewport;
    private float rotationSpeed;

    //        Probably should use camera.unproject but there is smth mad happening
//      Now it works :\
    public Vector2 translateToWorld(int screenX, int screenY) {
        /*Vector2 vector2 = new Vector2();
        vector2.x = (float) screenX / Gdx.graphics.getWidth()*camera.viewportWidth*camera.zoom + (camera.position.x - camera.viewportWidth/2);
        vector2.y = (1 - (float) screenY / Gdx.graphics.getHeight())*camera.viewportHeight*camera.zoom + (camera.position.y - camera.viewportHeight/2);
        Gdx.app.log("camera pos", camera.position.toString());
        Gdx.app.log("translate", vector2.toString());
        Gdx.app.log("unproject", camera.unproject(new Vector3(screenX, screenY, 0)).toString());
        */
        Vector3 vector3 = camera.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(vector3.x, vector3.y);
    }

    //    debugRender Matrix requirement
    public Matrix4 getMatrixCombined() {
        return camera.combined;
    }

    public ExtendViewport getViewport() {
        return viewport;
    }

    @Override
    public void create() {
//        rotationSpeed = 0.5f;

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(30, (float) w / h * 30);
        viewport = new ExtendViewport(20, 20, camera);
        viewport.update(w, h);

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.zoom = 3.3f;
        handleInput();
//        camera.update();
    }

    @Override
    public void render() {
//        camera.update();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void scrollZoom(int i) {
        camera.zoom += 0.01 * i;
        handleInput();
        render();
    }

    public void handleInput() {
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
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Gdx.app.log("camera", "" + camera.viewportWidth + " " + camera.viewportHeight + " " + camera.zoom);
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
//        camera.update();
        render();
//        viewport.update(width, height);
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

package com.alphagfx.kliander.stages;

import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameStage extends Stage {

    private World world;

    //  Debug
    private Box2DDebugRenderer debugRenderer;

    //  I dont know what is going on here
    private OrthographicCamera camera;
    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;


    //  Time management
    private final static float TIME_STEP = 1 / 300f;
    private float accumulator = 0;

    private Body subj;

    public GameStage() {
        world = WorldUtils.createWorld();
        debugRenderer = new Box2DDebugRenderer();
        subj = WorldUtils.createSubj(world, new Vector2(5, 5));
        setupCamera();
    }

    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        //  fixed time step
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= delta;
        }
    }

    @Override
    public void draw() {
        super.draw();
        debugRenderer.render(world, camera.combined);
    }
}

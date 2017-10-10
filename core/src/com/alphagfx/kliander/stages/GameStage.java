package com.alphagfx.kliander.stages;

import com.alphagfx.kliander.actors.Creature;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

    private Creature creature;

    public GameStage() {
        world = WorldUtils.createWorld();
        debugRenderer = new Box2DDebugRenderer();
        setupCamera();
        addCreature();
        setupTouchControlAreas();
    }

    public void addCreature() {
        creature = new Creature(WorldUtils.createSubj(world, new Vector2(5, 5)));
        addActor(creature);
    }

    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    private Vector3 touchPoint;
    private Rectangle screenRightSide;

    private void setupTouchControlAreas() {
        touchPoint = new Vector3();
        screenRightSide = new Rectangle(getCamera().viewportWidth / 2, 0, getCamera().viewportWidth / 2,
                getCamera().viewportHeight / 2);
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        translateScreenToWorldCoordinates(screenX, screenY);
        if (rightSideTouched(touchPoint.x, touchPoint.y)) {
            creature.dash();
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }

    private void translateScreenToWorldCoordinates(int x, int y) {
        getCamera().unproject(touchPoint.set(x, y, 0));
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
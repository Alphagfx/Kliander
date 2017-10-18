package com.alphagfx.kliander.stages;

import com.alphagfx.kliander.actors.Creature;
import com.alphagfx.kliander.utils.CameraHandle;
import com.alphagfx.kliander.utils.Constants;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameStage extends Stage {

    private World world;

    //  Debug
    private Box2DDebugRenderer debugRenderer;

    private CameraHandle camera;

    //  Time management
    private final static float TIME_STEP = 1 / 300f;
    private float accumulator = 0;

    private Creature creature;
    private Creature selectedCreature;

    private Body worldBorder;
    private Body obstacle;

    public GameStage() {
        world = WorldUtils.createWorld();
        debugRenderer = new Box2DDebugRenderer();
        camera = new CameraHandle();
        camera.create();
        addCreature();
        setupTouchControlAreas();

        worldBorder = WorldUtils.createWorldBorders(world, new Vector2(1, 1),
                new Vector2(Constants.WORLD_WIDTH - 1, Constants.WORLD_HEIGHT - 1));

        obstacle = WorldUtils.createObsatcle(world, new Vector2(15, 5));
    }


    public void addCreature() {
        creature = new Creature(WorldUtils.createSubj(world, new Vector2(5, 5)));
        addActor(creature);
    }


    //  Controls

    private Vector2 touchPoint;
    private Rectangle screenRightSide;
    private Batch batch;

    private void setupTouchControlAreas() {
        touchPoint = new Vector2();
        screenRightSide = new Rectangle(getCamera().viewportWidth / 2, 0, getCamera().viewportWidth / 2,
                getCamera().viewportHeight / 2);
//        batch = new SpriteBatch();
//        batch.draw();
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        translateScreenToWorldCoordinates(screenX, screenY);
        if (rightSideTouched(touchPoint.x, touchPoint.y) && selectedCreature != null) {
            selectedCreature.dash();
        } else if (WorldUtils.containsInCircle(touchPoint, creature.getPosition(), creature.getUserData().getSelectRange())) {
            System.out.println(touchPoint + " : " + creature.getPosition() + " : " + creature.getUserData().getSelectRange() + " : selected");
            selectedCreature = creature;
        } else if (selectedCreature != null) {
            selectedCreature.moveTo(camera.translateToWorld(screenX, screenY));
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keyCode) {
//        if (keyCode == 'a' && creature != null ) {
//             creature.dash();
//        }
        return super.keyDown(keyCode);
    }

    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }

    private void translateScreenToWorldCoordinates(int x, int y) {
        touchPoint = camera.translateToWorld(x, y);
    }

    //  Main process loop

    @Override
    public void act(float delta) {
        super.act(delta);

        camera.render();

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
        debugRenderer.render(world, camera.getMatrixCombined());
    }

    public void resize(int width, int height) {
        camera.resize(width, height);
    }
}

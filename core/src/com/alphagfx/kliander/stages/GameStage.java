package com.alphagfx.kliander.stages;

import com.alphagfx.kliander.actors.Creature;
import com.alphagfx.kliander.utils.CameraHandle;
import com.alphagfx.kliander.utils.Constants;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

        setupTouchControlAreas();

        worldBorder = WorldUtils.createWorldBorders(world, new Vector2(1, 1),
                new Vector2(Constants.WORLD_WIDTH - 1, Constants.WORLD_HEIGHT - 1));

        obstacle = WorldUtils.createObsatcle(world, new Vector2(15, 5));

        System.out.println(getRoot().getStage() == null);

        for (int i = 0; i < 10; i++) {
            addCreature();
        }
    }


    public void addCreature() {
        Creature newbie = new Creature(WorldUtils.createSubj(world, new Vector2((float) Math.random() * 100, (float) Math.random() * 100)));
        addActor(newbie);
    }

    public void setSelectedCreature(Creature selectedCreature) {
        this.selectedCreature = selectedCreature;
    }

    //  Controls

    private Vector2 touchPoint;
    private Rectangle screenRightSide;
    private Batch batch;

    private void setupTouchControlAreas() {
        touchPoint = new Vector2();
        screenRightSide = new Rectangle(getCamera().viewportWidth / 2, 0, getCamera().viewportWidth / 2,
                getCamera().viewportHeight / 2);

        Gdx.input.setInputProcessor(this);

    }

    @Override
    public boolean scrolled(int amount) {
        camera.scrollZoom(amount);
        return super.scrolled(amount);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        translateScreenToWorldCoordinates(screenX, screenY);

       /* if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && WorldUtils.containsInCircle(touchPoint, creature.getPosition(), creature.getUserData().getSelectRange())) {
            System.out.println(touchPoint + " : " + creature.getPosition() + " : " + creature.getUserData().getSelectRange() + " : selected");
            selectedCreature = creature;
        }*/
//        for (Actor actor : this.getActors()) {
//            System.out.println("hit");
//            selectedCreature = (Creature)actor.hit(touchPoint.x, touchPoint.y, true);
//        }
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            if (selectedCreature != null) {
                selectedCreature.moveTo(camera.translateToWorld(screenX, screenY));
            }
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keyCode) {
        return super.keyDown(keyCode);
    }

    @Override
    public boolean keyTyped(char character) {
        camera.handleInput();

        return super.keyTyped(character);
    }

    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }

    private void translateScreenToWorldCoordinates(int x, int y) {
        touchPoint = camera.translateToWorld(x, y);

//          Smth is wrong and this does not work
//        System.out.println(screenToStageCoordinates(new Vector2(x, y)));
//        Gdx.input.isKeyPressed(Input.Keys.)
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

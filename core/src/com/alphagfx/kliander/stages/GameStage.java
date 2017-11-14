package com.alphagfx.kliander.stages;

import com.alphagfx.kliander.actors.*;
import com.alphagfx.kliander.box2d.IBodyUserData;
import com.alphagfx.kliander.utils.CameraHandle;
import com.alphagfx.kliander.utils.Constants;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameStage extends Stage implements QueryCallback {

    private UIStage uiStage;

    private World world;

    //  Debug
    private Box2DDebugRenderer debugRenderer;

    //  Time management
    private final static float TIME_STEP = 1 / 200f;
    private float accumulator = 0;

    private GameActor selectedGameActor;
    private boolean paused;

    public GameStage(CameraHandle cameraHandle) {

        super(cameraHandle);

        world = WorldUtils.createWorld();

        debugRenderer = new Box2DDebugRenderer();

        setDebugAll(false);

        setContactListener();

        WorldUtils.createWorldBorders(world, new Vector2(1, 1),
                new Vector2(Constants.WORLD_WIDTH - 1, Constants.WORLD_HEIGHT - 1));

        addActor(new Obstacle(WorldUtils.createObstacle(world, new Vector2(15, 5)), 2, 2));

        for (int i = 0; i < 5; i++) {
            float angle = MathUtils.random(MathUtils.PI2);
            angle = 0;
            addCreature(new Vector2((float) Math.random() * 90 + 5, (float) Math.random() * 90 + 5), angle);
        }
        selectedGameActor = new Fighter(WorldUtils.createSubj(world, new Vector2(50, 50), 0));
        addActor(selectedGameActor);

    }

    private void setContactListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                Object objectA = contact.getFixtureA().getBody().getUserData();
                Object objectB = contact.getFixtureB().getBody().getUserData();
                if (objectA instanceof Bullet) {
                    ((Bullet) objectA).setDead(true);
                    if (objectB instanceof IBodyUserData) {
                        ((IBodyUserData) objectB).receiveDamage(((Bullet) objectA).getDamage());
                    }
                }
                if (objectB instanceof Bullet) {
                    ((Bullet) objectB).setDead(true);
                    if (objectA instanceof IBodyUserData) {
                        ((IBodyUserData) objectA).receiveDamage(((Bullet) objectB).getDamage());
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    public void setUiStage(UIStage uiStage) {
        this.uiStage = uiStage;
    }

    public void addCreature(Vector2 position, float rotation) {

        addActor(new Fighter(WorldUtils.createSubj(world, position, rotation)));
    }

    public void setSelectedGameActor(GameActor selectedGameActor) {
        this.selectedGameActor = selectedGameActor;
        Gdx.app.log("set game actor", selectedGameActor.toString());
    }

    public GameActor getSelectedGameActor() {
        return selectedGameActor;
    }

    //  Controls

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector2 touchPoint = screenToStageCoordinates(new Vector2(screenX, screenY));

        world.QueryAABB(this, touchPoint.x, touchPoint.y, touchPoint.x, touchPoint.y);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

            selectedGameActor.doGameAction(uiStage.getSelectedAction(), touchPoint);

        }

        if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {

            if (selectedGameActor instanceof GameActor) {
                selectedGameActor.doGameAction("FIRE", touchPoint);
                Gdx.app.log(selectedGameActor.toString(), selectedGameActor.getActionSet().toString());
            }
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {

            if (selectedGameActor instanceof Creature) {
                ((Creature) selectedGameActor).moveTo(touchPoint);
            }
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    //  Main process loop

    @Override
    public void act(float delta) {
        super.act(delta);

        //  fixed time step
        if (!paused) {
            accumulator += delta;

            while (accumulator >= delta) {
                world.step(TIME_STEP, 6, 2);
                accumulator -= delta;
            }
        }

        WorldUtils.checkDeadBodies(world);

    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    @Override
    public void draw() {
        super.draw();

        debugRenderer.render(world, getCamera().combined);
    }

    @Override
    public boolean reportFixture(Fixture fixture) {
        if (fixture.getBody() != null && fixture.getBody().getUserData() instanceof GameActor) {
            Gdx.app.log("report fixture", fixture.getBody().getUserData().toString());
            setSelectedGameActor(((GameActor) fixture.getBody().getUserData()));
            uiStage.updateActionMenu();
        }
        return false;
    }

    public void updateActionSet() {
        for (Actor actor : getActors()) {
            if (actor instanceof GameActor) {
                uiStage.updateActionSet(((GameActor) actor));
                uiStage.updateActionMenu();
            }
        }

    }

}

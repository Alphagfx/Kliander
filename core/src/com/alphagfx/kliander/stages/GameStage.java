package com.alphagfx.kliander.stages;

import com.alphagfx.kliander.actors.Bullet;
import com.alphagfx.kliander.actors.GameActor;
import com.alphagfx.kliander.box2d.IBodyUserData;
import com.alphagfx.kliander.utils.CameraHandle;
import com.alphagfx.kliander.utils.Constants;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameStage extends Stage implements QueryCallback {

    private UIStage uiStage;

    private World world;

    //  Debug
    private Box2DDebugRenderer debugRenderer;

    //  Time management
    private final static float TIME_STEP = 1 / 300f;
    private float accumulator = 0;

    private GameActor selectedGameActor;
    private GameActor returnedActor;

    private boolean paused;

    public GameStage(CameraHandle cameraHandle) {

        super(cameraHandle);

        world = WorldUtils.createWorld();

        debugRenderer = new Box2DDebugRenderer();

        setDebugAll(false);

        setContactListener();

        WorldUtils.createWorldBorders(world, new Vector2(1, 1),
                new Vector2(Constants.WORLD_WIDTH - 1, Constants.WORLD_HEIGHT - 1));

        for (int i = 0; i < 5; i++) {
            float angle = MathUtils.random(MathUtils.PI2);
            angle = 0;
            addGameActor("Fighter", new Vector2((float) Math.random() * 90 + 5, (float) Math.random() * 90 + 5), angle);
            addGameActor("Obstacle", new Vector2((float) Math.random() * 90 + 5, (float) Math.random() * 90 + 5), angle);
        }

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

    private void addGameActor(String name, Vector2 position, float angle) {
        GameActor gameActor = GameActor.getFactory(name).create(world, position, angle);
        addActor(gameActor);
    }

    void setUiStage(UIStage uiStage) {
        this.uiStage = uiStage;
    }

    public void setSelectedGameActor(GameActor selectedGameActor) {
        this.selectedGameActor = selectedGameActor;
    }

    GameActor getSelectedGameActor() {
        return selectedGameActor;
    }

    //  Controls

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector2 touchPoint = screenToStageCoordinates(new Vector2(screenX, screenY));
        returnedActor = null;
        world.QueryAABB(this, touchPoint.x, touchPoint.y, touchPoint.x, touchPoint.y);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            setSelectedGameActor(returnedActor);
            uiStage.updateActionMenu();
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && selectedGameActor != null) {
            Action action = selectedGameActor.doGameAction(uiStage.getSelectedAction(), returnedActor != null ? returnedActor : touchPoint);
            if (action != null) {
                selectedGameActor.addAction(action);
            }
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    //  Main process loop

    @Override
    public void act(float delta) {
        if (!paused) {
            super.act(delta);

        //  fixed time step
            accumulator += delta;

            while (accumulator >= delta) {
                world.step(TIME_STEP, 6, 2);
                accumulator -= delta;
            }
        }

        WorldUtils.checkDeadBodies(world);
    }

    void setPaused(boolean paused) {
        this.paused = paused;
    }

    boolean isPaused() {
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
            returnedActor = ((GameActor) fixture.getBody().getUserData());
        }
        return false;
    }

    public void updateActionSet() {
        for (Actor actor : getActors()) {
            if (actor instanceof GameActor) {
                uiStage.updateActionSet(((GameActor) actor));
            }
        }
    }

}

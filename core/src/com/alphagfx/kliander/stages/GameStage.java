package com.alphagfx.kliander.stages;

import com.alphagfx.kliander.actors.Creature;
import com.alphagfx.kliander.actors.Fighter;
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

public class GameStage extends Stage {

    private World world;

    //  Debug
    private Box2DDebugRenderer debugRenderer;

    private CameraHandle camera;
    private boolean camera_read;

    //  Time management
    private final static float TIME_STEP = 1 / 200f;
    private float accumulator = 0;

    private Creature selectedCreature;

    private Body worldBorder;
    private Body obstacle;

    public GameStage() {

        world = WorldUtils.createWorld();

        debugRenderer = new Box2DDebugRenderer();

        setContactListener();

        camera = new CameraHandle();
        camera.create();

        setViewport(camera.getViewport());

        worldBorder = WorldUtils.createWorldBorders(world, new Vector2(1, 1),
                new Vector2(Constants.WORLD_WIDTH - 1, Constants.WORLD_HEIGHT - 1));

        obstacle = WorldUtils.createObsatcle(world, new Vector2(15, 5));

        for (int i = 0; i < 5; i++) {
            addCreature();
        }
    }

    private void setContactListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
// FIXME: 11/1/17
                Gdx.app.log("contact listener", " body hit");
                if (contact.getFixtureA().getBody().getUserData() != null) {
                    Gdx.app.log("user data type", ((IBodyUserData) contact.getFixtureA().getBody().getUserData()).getUserDataType().toString());

//                    Gdx.app.log(contact.getFixtureA().ge.);
//                    if ((contact.getFixtureA().getBody() == Fighter.class && ((CreatureUserData)contact.getFixtureB().getBody().getUserData()).getUserActor().getClass() == Bullet.class)
//                            || (((CreatureUserData)contact.getFixtureB().getBody().getUserData()).getUserActor().getClass() == Fighter.class && ((CreatureUserData)contact.getFixtureA().getBody().getUserData()).getUserActor().getClass() == Bullet.class)) {
//                        Gdx.app.log("contact listener", " fighter and bullet hit");
//
//                    }
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


    public void addCreature() {

        Fighter newbie = new Fighter(WorldUtils.createSubj(world, new Vector2((float) Math.random() * 90 + 5, (float) Math.random() * 90 + 5)));

        addActor(newbie);
    }

    public void setSelectedCreature(Creature selectedCreature) {
        this.selectedCreature = selectedCreature;
    }

    //  Controls

    @Override
    public boolean scrolled(int amount) {

        camera.scrollZoom(amount);

        return super.scrolled(amount);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (selectedCreature != null) {
                // FIXME: 11/1/17 angles
                Vector2 vector2 = camera.translateToWorld(screenX, screenY).sub(selectedCreature.getPosition()).nor();
                selectedCreature.moveTo(selectedCreature.getPosition(), MathUtils.atan2(vector2.y, vector2.x) + MathUtils.PI2);
                ((Fighter) selectedCreature).fire();
            }
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {

            ((Fighter) selectedCreature).setTarget(camera.translateToWorld(screenX, screenY));
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            if (selectedCreature != null) {
                selectedCreature.moveTo(camera.translateToWorld(screenX, screenY), MathUtils.random(MathUtils.PI2));
//                selectedCreature.moveTo(camera.translateToWorld(screenX, screenY));
//                keyDown(Input.Keys.C);
            }
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keyCode) {

        camera_read = true;

        if (keyCode == Input.Keys.Z) {
            for (Actor actor : getActors()) {
                if (actor.getClass() == Creature.class) {
                    ((Creature) actor).getActorPosition();
                }
            }
        }

        if (keyCode == Input.Keys.C) {
            selectedCreature.getActorPosition();
        }

        return super.keyDown(keyCode);
    }

    @Override
    public boolean keyUp(int keyCode) {

        camera_read = false;

        return super.keyUp(keyCode);
    }

    //  Main process loop

    @Override
    public void act(float delta) {
        super.act(delta);

        if (camera_read) {
            camera.handleInput();
        }
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

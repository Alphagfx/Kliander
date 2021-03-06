package com.alphagfx.kliander.utils;

import com.alphagfx.kliander.actors.GameActor;
import com.alphagfx.kliander.box2d.IBodyUserData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class WorldUtils {

    public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, true);
    }

    // FIXME: 11/4/17 add Weapon as actor to the GameStage
    public static Body createBody(World world, Vector2 position, float angle, float width, float height) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.angle = angle;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 1f);

        shape.dispose();

        return body;

    }

    public static Body createObstacle(World world, Vector2 position, float x, float y) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(x, y);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 0f);

        shape.dispose();

        return body;
    }

    public static void createWorldBorders(World world, Vector2 vectorBegin, Vector2 vectorEnd) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((vectorBegin != null) ? vectorBegin : new Vector2(0, 0));

        EdgeShape shape = new EdgeShape();
        assert vectorBegin != null;
        shape.set(vectorBegin.x, vectorBegin.y, vectorBegin.x, vectorEnd.y);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 0f);
        shape.set(vectorBegin.x, vectorEnd.y, vectorEnd.x, vectorEnd.y);
        body.createFixture(shape, 0f);
        shape.set(vectorEnd.x, vectorEnd.y, vectorEnd.x, vectorBegin.y);
        body.createFixture(shape, 0f);
        shape.set(vectorBegin.x, vectorBegin.y, vectorEnd.x, vectorBegin.y);
        body.createFixture(shape, 0f);

        shape.dispose();

    }

    public static Body createBullet(World world, Vector2 position) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.bullet = true;

        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 0.01f);

        shape.dispose();

        return body;

    }

    //    Transforms angle in bounds between 0 and 2*PI
    public static float transformAngle(float angle) {
        return ((angle % MathUtils.PI2 + MathUtils.PI2)) % MathUtils.PI2;
    }


    /**
     * Default for Fighter
     */
    public static boolean containsInFOV(Vector2 start, Vector2 end, float startAngle) {
        return containsInFOV(start, end, startAngle, MathUtils.PI / 4);
    }

    /**
     * Contains if FOV
     *
     * @param start      owner body position
     * @param end        target body position
     * @param startAngle owner body orientation
     * @param fov        width of FOV
     */
    public static boolean containsInFOV(Vector2 start, Vector2 end, float startAngle, float fov) {

        Vector2 ownerOrientation =
                new Vector2((float) Math.cos(startAngle), (float) Math.sin(startAngle));
        Vector2 targetOffset = (new Vector2()).set(end).sub(start);

        float product = ownerOrientation.nor().dot(targetOffset.nor());
        float checkAngle = (float) Math.acos(product);

        return checkAngle < fov;
    }

    public static void checkDeadBodies(World world) {

        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            if (body.getUserData() instanceof GameActor && ((GameActor) body.getUserData()).isDead())
                if (!((GameActor) body.getUserData()).remove()) {
                    Gdx.app.log("actor", "actor was not removed properly \n     " + body.getUserData().toString());
                }
            if (body.getUserData() instanceof IBodyUserData)
                ((IBodyUserData) body.getUserData()).destroyBodyUser();

        }
    }

}

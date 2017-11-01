package com.alphagfx.kliander.utils;

import com.alphagfx.kliander.box2d.CreatureUserData;
import com.alphagfx.kliander.box2d.ObstacleUserData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class WorldUtils {

    public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, true);
    }

    public static Body createSubj(World world, Vector2 vector2) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((vector2 != null) ? vector2 : new Vector2(0, 0));

        //  fix me
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 0.5f);
        body.resetMassData();
        body.setUserData(new CreatureUserData(2));

        shape.dispose();

        return body;
    }

    public static Body createObsatcle(World world, Vector2 vector2) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((vector2 != null) ? vector2 : new Vector2(0, 0));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 0f);

        body.setUserData(new ObstacleUserData());

        shape.dispose();

        return body;
    }

    public static Body createWorldBorders(World world, Vector2 vectorBegin, Vector2 vectorEnd) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((vectorBegin != null) ? vectorBegin : new Vector2(0, 0));

        EdgeShape shape = new EdgeShape();
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

        return body;
    }

    public static Body createBullet(World world, Vector2 position) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.bullet = true;

        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 0.1f);

        shape.dispose();

        return body;

    }

    public static Body createWeapon(World world, Vector2 position, float angle, float width, float height) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 100f);
        body.setTransform(position, angle);

        shape.dispose();

        return body;

    }

    //    Transforms angle in bounds between 0 and 2*PI
    public static float transformAngle(float angle) {
        return ((angle % MathUtils.PI2 + MathUtils.PI2)) % MathUtils.PI2;
    }

    //    Basic Circle contains, used instead of a Shape2D
//    Preferred to use Body Fixtures
    public static boolean containsInCircle(Vector2 touch, Vector2 body, int r) {
        return ((touch.x - body.x) * (touch.x - body.x) + (touch.y - body.y) * (touch.y - body.y)) < (r * r);
    }

}

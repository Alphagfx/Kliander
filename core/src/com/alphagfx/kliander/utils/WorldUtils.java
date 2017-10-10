package com.alphagfx.kliander.utils;

import com.alphagfx.kliander.box2d.CreatureUserData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class WorldUtils {

    public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, true);
    }

    public static Body createSubj(World world, Vector2 vector) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((vector != null) ? vector : new Vector2(0, 0));
        PolygonShape shape = new PolygonShape();
        //  fix me
        shape.setAsBox(1, 1);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 0.5f);
        body.resetMassData();
        body.setUserData(new CreatureUserData());
        shape.dispose();
        return body;
    }

//    public static Body createObstacle(World world, Vector2 vector) { }

}

//        Copyright Kliander Alphagfx
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.enums.UserDataType;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Creature extends GameActor {

    private float max_speed = 20;
    private float turn_speed = 5;

    private Vector2 waypoint;
    private float waypoint_angle;

    public Creature(Body body) {
        super(body);

        setUserDataType(UserDataType.CREATURE);

    }

    public Body getBody() {
        return body;
    }

    public void stop() {
        body.setLinearVelocity(0, 0);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void moveTo(Vector2 position, float angle) {

        moveTo(position);
        turnTo(angle);
    }

    public void moveTo(Vector2 position) {

        waypoint = position;

        Vector2 speed = position.cpy().sub(body.getPosition()).nor().scl(max_speed);

        body.setLinearVelocity(speed);

    }

    public void turnTo(float angle) {

        float body_angle = WorldUtils.transformAngle(body.getAngle());

        waypoint_angle = WorldUtils.transformAngle(angle);

        if (Math.abs(waypoint_angle - body_angle) > 0.05) {
            body.setAngularVelocity((((waypoint_angle - body_angle) + MathUtils.PI2) % MathUtils.PI2 > MathUtils.PI) ? -turn_speed : turn_speed);
        }

    }

    public void getActorPosition() {

        Gdx.app.log("angle", "" + waypoint_angle);
        Gdx.app.log("body angle", "" + ((body.getAngle() % MathUtils.PI2 + MathUtils.PI2)) % MathUtils.PI2);
        Gdx.app.log("speed", "" + body.getLinearVelocity());

    }

    /**
     * Updates actor position relative to body position
     * Compares body position and rotation and stops if needed
     *
     * @param delta
     */

    @Override
    public void act(float delta) {

        if (body.isAwake()) {

            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRotation(WorldUtils.transformAngle(body.getAngle()) * MathUtils.radiansToDegrees);

            if (body.getPosition().epsilonEquals(waypoint, 0.05f)) {
                stop();
            }
            if (Math.abs(waypoint_angle - WorldUtils.transformAngle(body.getAngle())) <= 0.05) {
                body.setAngularVelocity(0);
            }
        }
    }
}

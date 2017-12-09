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

import com.alphagfx.kliander.actors.actions.GameAction;
import com.alphagfx.kliander.enums.UserDataType;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;


public class Creature extends GameActor {

    protected static Set<String> actionSet;

    static {
        actionSet = new LinkedHashSet<>(GameActor.actionSet);
        actionSet.addAll(Arrays.asList("MOVE", "TURN", "STOP"));
    }

    @Override
    public Set<String> getActionSet() {
        return actionSet;
    }

    @Override
    public <T> Action doGameAction(String action, T target) {

        switch (action) {
            case "MOVE": {
                RepeatAction repeatAction = new RepeatAction();
                repeatAction.setAction(new GameAction("Move", target) {
                    @Override
                    public boolean act(float delta) {
                        if (body.getPosition().epsilonEquals(getPosition(), 0.05f)) {
                            stop();
                            repeatAction.finish();
                            return true;
                        }
                        ((GameActor) target).moveTo(getPosition());
                        return false;
                    }
                });
                repeatAction.setCount(RepeatAction.FOREVER);
                return repeatAction;
            }
            case "TURN": {
                RepeatAction repeatAction = new RepeatAction();
                repeatAction.setAction(new GameAction("Turn", target) {
                    @Override
                    public boolean act(float delta) {

                        Vector2 vector2 = body.getPosition().sub(getPosition()).nor();
                        float angle = MathUtils.atan2(vector2.y, vector2.x) + MathUtils.PI;

                        if (Math.abs(angle - WorldUtils.transformAngle(body.getAngle())) <= 0.05) {
                            body.setAngularVelocity(0);
                            repeatAction.finish();
                            return true;
                        }
                        turnTo(angle);
                        return false;
                    }
                });
                repeatAction.setCount(RepeatAction.FOREVER);
                return repeatAction;
            }
            default: {
                return super.doGameAction(action, target);
            }
        }

    }

    private float max_speed = 20;
    private float turn_speed = 5;

    public Creature(Body body) {
        super(body);

        setUserDataType(UserDataType.CREATURE);

    }

    public Body getBody() {
        return body;
    }

    private void stop() {
        body.setLinearVelocity(0, 0);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    protected void moveTo(Vector2 position) {

        Vector2 speed = position.cpy().sub(body.getPosition()).nor().scl(max_speed);

        body.setLinearVelocity(speed);

    }

    @Override
    protected void turnTo(float angle) {

        float body_angle = WorldUtils.transformAngle(body.getAngle());

        body.setAngularVelocity((((angle - body_angle) + MathUtils.PI2) % MathUtils.PI2 > MathUtils.PI) ? -turn_speed : turn_speed);

    }

    @Override
    protected void fire(Vector2 target) {

    }

    @Override
    protected void specialAction(String name, Object... objects) {

    }

    /**
     * Updates actor position relative to body position
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        if (body.isAwake()) {

            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRotation(WorldUtils.transformAngle(body.getAngle()) * MathUtils.radiansToDegrees);
        }
    }

    public static class Factory implements com.alphagfx.kliander.actors.Factory<Creature> {

        @Override
        public Creature create(World world, Vector2 position, float angle, Object... objects) {
            return new Creature(WorldUtils.createBody(world, position, 0, 1, 1));
        }
    }
}

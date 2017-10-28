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

import com.alphagfx.kliander.box2d.CreatureUserData;
import com.alphagfx.kliander.box2d.UserData;
import com.alphagfx.kliander.stages.GameStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Creature extends GameActor {

    private float max_speed = 20;
    private float turn_speed = 5;

    private Vector2 waypoint;
    private float waypoint_angle;

    public Creature(Body body) {
        super(body);

        setSize(2, 2);
//        debug
        setDebug(true);

        this.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((GameStage) Creature.this.getStage()).setSelectedCreature(Creature.this);
                System.out.println("pressed");
                super.clicked(event, x, y);
            }
        });
    }



    @Override
    public UserData getUserData() {
        return (CreatureUserData) userData;
    }

    @Override
    public void setUserData(Object object) {
        this.userData = (CreatureUserData) object;
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

    public void moveTo(Vector2 vector2) {
        moveTo(vector2, body.getAngle());
    }

    public void moveTo(Vector2 vector2, float angle) {
        waypoint = vector2;
        waypoint_angle = angle;
        float body_angle = ((body.getAngle() % MathUtils.PI2 + MathUtils.PI2)) % MathUtils.PI2;
        Vector2 speed = vector2.cpy();
        speed.sub(this.body.getPosition());

        body.setLinearVelocity(speed.nor().scl(max_speed));

//        Angular velocity
        if (Math.abs(waypoint_angle - body_angle) > 0.01) {
            body.setAngularVelocity((((waypoint_angle - body_angle) + MathUtils.PI2) % MathUtils.PI2 > MathUtils.PI) ? -turn_speed : turn_speed);
//            System.out.println("speedup");
//            System.out.println("angular " + body.getAngularVelocity());
        }

    }

    public void getActorPosition() {
//        Vector2 position = Creature.this.getStage().stageToScreenCoordinates(body.getPosition());
//        Gdx.app.log("Screen position", position.toString());
        Gdx.app.log("angle", "" + waypoint_angle);
        Gdx.app.log("body angle", "" + ((body.getAngle() % MathUtils.PI2 + MathUtils.PI2)) % MathUtils.PI2);

    }

    @Override
    public void act(float delta) {

        if (body.isAwake()) {

            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

            if (body.getPosition().epsilonEquals(waypoint, 0.05f)) {
                stop();
            }
            if (Math.abs(waypoint_angle - ((body.getAngle() % MathUtils.PI2 + MathUtils.PI2)) % MathUtils.PI2) <= 0.01) {
                body.setAngularVelocity(0);
            }
        }
    }
}
